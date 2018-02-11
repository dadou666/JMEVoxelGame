package dadou.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.jme.math.Vector3f;

import dadou.BriqueAvecTexture3D;
import dadou.Button;
import dadou.Game;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class HabillageIHM {
	IHM ihm;
	Widget texture;
	public Button b = new Button(2);
	Map<Widget, Color> couleurs = new HashMap<>();
	Widget wColor;
	Habillage habillage;
	public int idxTexture;
	public int dxPixel;
	public int dyPixel;
	public VoxelTexture3D voxelTexture3D;
	public Objet3D obj;
	public ModelClasse mc;
	public boolean modeIHM = true;
	public Game game;
	public String nom;
	public Point debutRectangle;
	public boolean modeRectangle = false;
	public Widget position;

	public HabillageIHM(Habillage habillage, Game game) {
		this.game = game;
		this.habillage = habillage;
		for (int x = 0; x < habillage.dim; x++) {
			for (int y = 0; y < habillage.dim; y++) {
				habillage.SetBlock(x,y,0, Color.BLACK);
			}
		}

		this.voxelTexture3D = this.habillage.creerVoxelTexture3D();

		ihm = game.nouvelleIHM();
		int space = 10;
		int hauteur = 100;
		ihm.beginX();
		ihm.space(space);
		ihm.beginY(() -> {
			ihm.space(space);
			List<Color> colors = new ArrayList<>();
			colors.add(Color.RED);
			colors.add(Color.GREEN);
			colors.add(Color.YELLOW);
			colors.add(Color.BLUE);
			colors.add(Color.GRAY);
			colors.add(Color.LIGHT_GRAY);
			colors.add(Color.DARK_GRAY);
			colors.add(Color.PINK);
			colors.add(Color.CYAN);
			colors.add(Color.ORANGE);
			colors.add(Color.MAGENTA);
			colors.add(Color.WHITE);
			colors.add(Color.BLACK);
			colors.add(new Color(139, 69, 19));

			this.ajouterCouleur(game, colors, space, hauteur);
			ihm.setSize(game.getHeight() - 2 * space, game.getHeight() - 2 * space - hauteur);
			ihm.beginX();
			texture = ihm.widget();
			ihm.setSize(120, 30);
			position = ihm.widget();
			Widget.drawText(position, "0, 0", Color.DARK_GRAY, Color.YELLOW, Color.WHITE);
			ihm.end();
			ihm.space(space);

		});
		ihm.space(space);
		ihm.end();
		Widget.drawRect(texture, Color.WHITE, 0, 0, texture.width, texture.height);
		this.dxPixel = texture.width / (this.habillage.dim);
		this.dyPixel = texture.height / (this.habillage.dim);
		Color copie[][][] = new Color[1][1][1];
		copie[0][0][0] = Color.RED;
		mc = new ModelClasse();
		mc.copie = copie;
		mc.dx = 1;
		mc.dy = 1;
		mc.dz = 1;
		try {

			obj = this.creerObjet(mc, game);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ajouterCouleur(Game game, List<Color> colors, int space, int hauteur) {
		int w = (game.getWidth() - 2 * space) / colors.size();
		ihm.setSize(w, hauteur);
		ihm.beginX();

		for (Color color : colors) {
			Widget wColor = ihm.widget();
			couleurs.put(wColor, color);
			if (color.equals(Color.BLACK)) {
				Widget.drawRect(wColor, new Color(10, 10, 10),Color.RED, 15, 20);
			} else {
				Widget.drawRect(wColor, color,Color.BLACK, 15, 20);
			}
		}
		ihm.end();

	}

	static int bord = 3;

	public void mettreAjourTexture() {

		Graphics2D g = texture.getGraphics2DForUpdate();

		for (int x = 0; x < habillage.dim; x++) {

			for (int y = 0; y < habillage.dim; y++) {
				Color color = habillage.GetBlock(x,habillage.dim - 1 - y,this.idxTexture);

				g.setColor(new Color(1, 1, 1));
				g.fillRect(x * dxPixel, y * dyPixel, dxPixel, dyPixel);
				if (color != null) {
					g.setColor(color);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(x * dxPixel + bord, y * dyPixel + bord, dxPixel - 2 * bord, dyPixel - 2 * bord);

			}
		}

		//g.dispose();

		texture.update();
	}

	public void mettreAjourTexture(int x0, int y0, int x1, int y1, Color color) {

		Graphics2D g = texture.getGraphics2DForUpdate();
		int min_x = Math.min(x0, x1);
		int max_x = Math.max(x0, x1);

		int min_y = Math.min(y0, y1);
		int max_y = Math.max(y0, y1);

		for (int x = min_x; x <= max_x; x++) {

			for (int y = min_y; y <= max_y; y++) {
				habillage.SetBlock(x,habillage.dim - 1 - y,this.idxTexture, color);
				try {
					this.voxelTexture3D.updateBlock(x, y, idxTexture, color);
				} catch (CouleurErreur e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g.setColor(new Color(1, 1, 1));
				g.fillRect(x * dxPixel, y * dyPixel, dxPixel, dyPixel);
				if (color != null) {
					g.setColor(color);
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(x * dxPixel + bord, y * dyPixel + bord, dxPixel - 2 * bord, dyPixel - 2 * bord);
			}
		}

	//	g.dispose();

		texture.update();
	}

	public void ecrirePixel(int x, int y, Color color) {
		// g.setColor(new Color(1,1,1));
		// g.fillRect(x, y , dxPixel, dyPixel);
		Widget.drawRect(texture, color, x + bord, y + bord, this.dxPixel - 2 * bord, this.dyPixel - 2 * bord);
	}

	public Objet3D creerObjet(ModelClasse mc, Game g) throws CouleurErreur, Exception {

		if (mc.vbo == null) {
			mc.initBuffer(g, true, habillage);
		}
		Vector3f size = mc.size.clone();
		VBOBriqueTexture3D vbo = mc.vbo;
		VoxelTexture3D tex = mc.tex;
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();

		BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
		translation.brique = brique;
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);
		System.out.println("size=" + size);
		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		obj.ajouter(translation);

		return obj;

	}

	public Point getPixelPosition() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		Widget w = ihm.getWidgetAt(x, y);

		if (w == this.texture) {

			Point p = ihm.getPointInWidgetFromMouse(texture, x, y);

			int qx = p.x / dxPixel;
			int qy = p.y / dyPixel;
			return new Point(qx, qy);
		}
		return null;
	}

	public void gererSouris() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		Widget w = ihm.getWidgetAt(x, y);

		if (w == this.texture) {
			if (this.wColor == null) {
				return;
			}

			Point p = ihm.getPointInWidgetFromMouse(texture, x, y);

			int px = (p.x / dxPixel) * dxPixel;
			int py = (p.y / dyPixel) * dyPixel;

			Color c = this.couleurs.get(wColor);
			this.ecrirePixel(px, py, c);
			int qx = p.x / dxPixel;
			int qy = p.y / dyPixel;
			if (qx < 0) {
				return;
			}
			if (qy < 0) {
				return;
			}
			if (qx >= habillage.dim) {
				return;
			}
			if (qy >= habillage.dim) {
				return;
			}
			// System.out.println(" px=" + (qx) + " py=" + (qy));

			this.habillage.SetBlock(qx,habillage.dim - 1 - qy,idxTexture, c);
			if (modeRectangle) {
				if (this.debutRectangle != null) {
					this.mettreAjourTexture(debutRectangle.x, debutRectangle.y, qx, qy, c);
					this.debutRectangle = null;

				} else {
					this.debutRectangle = new Point(qx, qy);
				}
			}
			try {
				this.voxelTexture3D.updateBlock(qx, qy, idxTexture, c);
			} catch (CouleurErreur e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;

		}
		Color c = this.couleurs.get(w);
		if (c != null) {
			Color oldC =this.couleurs.get(wColor);
			if (wColor != null) {
				if (oldC.equals(Color.BLACK)) {
					Widget.drawRect(wColor, new Color(10, 10, 10),Color.RED, 15, 20);
				} else {
					Widget.drawRect(wColor, oldC,Color.BLACK, 15, 20);
				}
			
			}
			wColor = w;
			Color newC = this.couleurs.get(wColor);
			if (newC.equals(Color.BLACK)) {
				Widget.drawRect(wColor, new Color(10, 10, 10), 15, 5);
			} else {
				Widget.drawRect(wColor, newC, 15, 5);
			}


		}

	}

	public void dessiner() {
		if (modeIHM) {
			Point p = this.getPixelPosition();
			if (p != null) {
			Widget.drawText(position, "" + p.x + "," + p.y, Color.DARK_GRAY, Color.YELLOW, Color.WHITE);
			}
			if (b.isPressed()) {
				this.gererSouris();
			}
			ihm.dessiner(game.shaderWidget);
		} else {

			obj.dessiner(game.getCamera());
		}
	}
}
