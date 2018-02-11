package dadou.ihm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

import dadou.ElementTableau;
import dadou.Game;
import dadou.Icone;
import dadou.OpenGLTools;
import dadou.Shader;
import dadou.Tableau;
import dadou.Texture2D;
import dadou.VBOTexture2D;

public class Widget implements Element {

	public int x, y, width, height;
	public float centerX;
	public float centerY;
	public float echelle = 1.0f;
	public VBOTexture2D vbo;

	public Texture2D tex;

	public Font defaultFont;

	public ListWidget parent;
	public int idxObject;
	public boolean show = true;
	Graphics2D g;
	public IHMModel model;

	public void finalize() {
		OpenGLTools.deleteTexture(tex.texId);
		
	}

	public static void drawText(Widget w, String txt, Color colorTxt, Color colorBox, Color colorBackground) {
		drawText(w, txt, colorTxt, colorBox, colorBackground, 0);
	}

	public static void drawText(Widget w, String txt, Color colorTxt, Color colorBox, Color colorBackground, int o) {

		if (txt == null) {
			return;
		}

		Graphics2D g = w.getGraphics2DForUpdate();

		Font font = Font.decode("arial-22");
	
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(colorBackground);
		g.fillRoundRect(0, 0, w.width, w.height, o, o);
		g.setColor(colorBox);
		int d = 5;
		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, o, o);
		g.setFont(font);
		int height = g.getFontMetrics().getHeight();
		int q=(w.height-height);
		g.setColor(colorTxt);

		g.drawString(txt,  0, w.height-q/2);

		// g.dispose();

		w.update();

	}

	public static void drawRect(Widget w, Color color, int o, int mg) {

		Graphics2D g = w.getGraphics2DForUpdate();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(color);
		g.fillRoundRect(mg, mg, w.width - 2 * mg, w.height - 2 * mg, o, o);

		// g.dispose();

		w.update();

	}

	public static void drawRect(Widget w, Color color, int x, int y, int dx, int dy) {

		Graphics2D g = w.getGraphics2DForUpdate();

		g.setColor(color);
		g.fillRect(x, y, dx, dy);

		// g.dispose();

		w.update();

	}

	public static void drawRect(Widget w, Color color, Color contour, int o, int d) {

		Graphics2D g = w.getGraphics2DForUpdate();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(contour);
		g.fillRoundRect(0, 0, w.width, w.height, o, o);

		g.setColor(color);

		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, o, o);

		// g.dispose();

		w.update();

	}

	public static void drawTextWithIcones(Widget w, String txt, String nomIcone, Color colorTxt, Color colorBox,
			Color colorBackground, Map<String, Icone> icones, int o) {

		if (txt == null) {
			return;
		}

		Graphics2D g = w.getGraphics2DForUpdate();

		Font font = Font.decode("arial-22");
		g.setColor(Color.BLACK);

		g.fillRect(0, 0, w.width, w.height);
		g.setColor(colorBackground);
		g.fillRoundRect(0, 0, w.width, w.height, o, o);
		g.setColor(colorBox);
		int d = 5;
		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, o, o);
		g.setFont(font);
		g.setColor(colorTxt);
		Icone icone = null;
		if (icones != null) {
			icone = icones.get(nomIcone);
		}
		// d+=3;
		if (icone != null) {
			g.drawImage(
					icone.getImage().getScaledInstance(w.width , w.height , java.awt.Image.SCALE_DEFAULT),
					0, 0, null);

		}
		g.setStroke(new BasicStroke(6));
		g.setColor(colorTxt);

		if (icone == null) {
			g.drawString(txt, 2 * d, w.height - 2 * d);
		}
		g.setColor(colorBackground);
		g.drawRoundRect(0, 0, w.width, w.height, o, o);

		// g.dispose();

		w.update();

	}

	public static void drawTextRounded(Widget w, Tableau tableau, Map<String, Icone> icones, Color colorTxt,
			Color colorBox, Color colorBackground) {

		if (tableau == null) {
			return;
		}

		Graphics2D g = w.getGraphics2DForUpdate();

		Font font = new Font("Monospaced", Font.PLAIN, 22);

		int d = 5;
		int o = 20;
		g.setColor(Color.black);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(colorBackground);
		g.fillRoundRect(0, 0, w.width, w.height, o, o);
		g.setColor(colorBox);

		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, o, o);
		g.setFont(font);
		int i = 0;
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight() + 4;
		int nb = (w.height - 2 * d) / fontHeight;
		i = fontHeight;

		int tailleTotal = 0;
		int idx = tableau.idxVue;
		while (idx < tableau.limite() && tailleTotal < nb) {

			ElementTableau et = tableau.list.get(idx);
			g.setColor(colorTxt);
			et.afficher(g, d, 2 * d + i, d, fontHeight, icones, null);
			// g.drawString(txt, d, 2 * d + i);
			i += fontHeight * et.nbLigne();
			idx++;
			tailleTotal += et.nbLigne();
		}
		// g.dispose();

		w.update();

	}

	public static void drawTextRounded(Widget w, List<String> listTxt, Color colorTxt, Color colorBox,
			Color colorBackground) {

		if (listTxt == null) {
			return;
		}

		Graphics2D g = w.getGraphics2DForUpdate();

		Font font = Font.decode("arial-22");

		int d = 5;
		int o = 20;
		g.setColor(Color.black);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(colorBackground);
		g.fillRoundRect(0, 0, w.width, w.height, o, o);
		g.setColor(colorBox);

		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, o, o);
		g.setFont(font);
		int i = 0;
		FontMetrics fm = g.getFontMetrics();
		int fontHeight = fm.getHeight() + 4;
		int nb = (w.height - 2 * d) / fontHeight;
		i = fm.getHeight();
		int start = Math.max(listTxt.size() - nb, 0);
		int end = Math.min(start + nb, listTxt.size());
		for (int idx = start; idx < end; idx++) {
			String txt = listTxt.get(idx);

			g.setColor(colorTxt);

			g.drawString(txt, d, 2 * d + i);
			i += fontHeight;
		}
		// g.dispose();

		w.update();

	}

	public void show() {
		show = true;

	}

	public void hide() {
		show = false;

	}

	public Point geMousePos() {
		return null;
	}

	@Override
	public Widget getWidgetAt(int x, int y) {
		// TODO Auto-generated method stub
		if (x < this.x || x > this.x + this.width) {
			return null;
		}
		if (y < this.y || y > this.y + this.height) {
			return null;
		}
		return this;
	}

	@Override
	public ListWidget getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void computeLocation(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	@Override
	public void buildVBO(Shader shader, int widthGlobal, int heightGlobal) {
		// TODO Auto-generated method stub

		float fwidthGlobal = widthGlobal;
		float fheightGlobal = heightGlobal;
		float fwidth = width;
		float fheight = height;
		float fx = x;
		float fy = y;
		fx = fx / (fwidthGlobal - 1);
		fy = fy / (fheightGlobal - 1);
		fwidth = fwidth / fwidthGlobal;
		fheight = fheight / fheightGlobal;
		fx = 2.0f * fx - 1.0f;
		fy = -2.0f * fy + 1.0f;
		fwidth = fwidth * 2.0f;
		fheight = fheight * 2.0f;

		centerX = fx + fwidth / 2.0f;
		centerY = fy - fheight / 2.0f;
		vbo = new VBOTexture2D(shader);
		vbo.addVertex(fx, fy, 0.00f);

		vbo.addVertex(fx, fy - fheight, 0.0f);
		vbo.addVertex(fx + fwidth, fy - fheight, 0.0f);
		vbo.addVertex(fx + fwidth, fy, 0.0f);

		vbo.addCoordTexture2D(0, 0);
		vbo.addCoordTexture2D(0, 1);
		vbo.addCoordTexture2D(1, 1);
		vbo.addCoordTexture2D(1, 0);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();

	}

	public Quaternion couleurTransparente;

	public void dessiner(Shader widgetShader) {
		if (!show) {
			return;
		}
		if (tex.image == null) {
			return;
		}

		widgetShader.use();
		tex.bind();
		widgetShader.glUniformiARB("texture", 0);
		if (couleurTransparente == null) {
			widgetShader.glUniformfARB("couleurTransparente", 0, 0, 0, 0);

		} else {
			widgetShader.glUniformfARB("couleurTransparente", couleurTransparente.x, couleurTransparente.y,
					couleurTransparente.z, couleurTransparente.w);
		}
		widgetShader.glUniformfARB("discardColor", 0, 0, 0, 0);
		widgetShader.glUniformfARB("center", centerX, centerY, 0);
		widgetShader.glUniformfARB("echelle", echelle);
		vbo.bind();
		vbo.dessiner();
		vbo.unBindVAO();

	}

	public Graphics2D getGraphics2DForUpdate() {
		if (g == null) {
			g = tex.getGraphics2DForUpdate();
		}
		return g;

	}

	public void update() {

		tex.update();

		OpenGLTools.exitOnGLError("Error update tex");

	}

	@Override
	public void echelle(float echelle) {
		// TODO Auto-generated method stub
		this.echelle = echelle;

	}

}
