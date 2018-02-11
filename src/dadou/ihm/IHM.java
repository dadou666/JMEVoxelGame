package dadou.ihm;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.jme.math.Vector3f;

import dadou.Game;
import dadou.Shader;
import dadou.Texture2D;

public class IHM {
	ListWidget current;
	ListWidget racine;
	public List<IHMModel> models = new ArrayList<>();
	public int width;
	public int height;
	public int screenWidth;
	public int screenHeight;
	int defaultWidth;
	int defaultHeight;
	boolean fixWidth;
	boolean fixHeight;
	public Shader shaderWidget;
	static public long periodeUpdate=300;
	public long time;

	
	public void updateFromModel() {
		long deltat = System.currentTimeMillis() - time;
		if (deltat >= periodeUpdate) {
			for(IHMModel w:models) {
				w.update();
			}
			time = System.currentTimeMillis();
			return;
		}
		
	}
	public void echelle(float echelle) {
		racine.echelle(echelle);
		
	}

	public void beginX() {
		ListWidgetX l = new ListWidgetX();
		l.parent = current;
		if (current != null) {
			current.elements.add(l);
		} else {
			racine = l;
		}
		current = l;

	}

	public void beginX(Action action) {
		this.beginX();
		action.execute();
		this.end();

	}

	public void beginY(Action action) {
		this.beginY();
		action.execute();
		this.end();

	}

	public void beginY() {
		ListWidgetY l = new ListWidgetY();
		l.parent = current;
		if (current != null) {
			current.elements.add(l);
		} else {
			racine = l;
		}
		current = l;

	}

	public void end() {
		current = current.parent;
		if (current == null) {
			this.creer(shaderWidget);
		}

	}

	public void setSize(int width, int height) {
		this.defaultWidth = width;
		this.defaultHeight = height;

	}

	public void show() {

	}

	public void hide() {

	}

	public void fixWidth(boolean b) {
		this.fixWidth = b;

	}

	public void fixHeight(boolean b) {
		this.fixHeight = b;
	}

	public Widget widget() {
		Widget widget = new Widget();
		widget.width = this.defaultWidth;
		widget.height = this.defaultHeight;
		widget.tex = new Texture2D(this.defaultWidth, this.defaultHeight);

		current.elements.add(widget);
		widget.parent = current;

		return widget;
	}


	public void ajouterModel(IHMModel m) {
		this.models.add(m);
	}


	public void creer(Shader widgetShader) {
		racine.computeLocation();
		width = racine.getWidth();
		height = racine.getHeight();
		racine.buildVBO(widgetShader);
	}

	public IHM ihm() {
		return null;

	}

	public void space(int size) {
		Space space = new Space();
		space.size = size;
		space.parent = current;
		current.elements.add(space);
	}

	public void dessiner(Shader widgetShader) {
		updateFromModel();
		this.racine.dessiner(widgetShader);

	}
	
	public void dessinerAvecTransparence(Shader widgetShader) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		updateFromModel();
		this.racine.dessiner(widgetShader);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public Widget getWidgetAt(int x, int y) {
		float fscreenWidth = screenWidth;
		float fscreenHeight = screenHeight;
		float fwidth = width;
		float fheight = height;
		float fx = x;
		float fy = y;
		fx = fx * fwidth / fscreenWidth;
		fy = fy * fheight / fscreenHeight;
		return racine.getWidgetAt((int) fx, height - (int) fy);

	}

	public Point getPointInWidgetFromMouse(Widget widget, int x, int y) {
		float fscreenWidth = screenWidth;
		float fscreenHeight = screenHeight;
		float fwidth = width;
		float fheight = height;
		float fx = x;
		float fy = y;
		fx = fx * fwidth / fscreenWidth;
		fy = fy * fheight / fscreenHeight;
		int px = (int) fx;
		int py = height - (int) fy;
		if (widget == widget.getWidgetAt(px, py)) {
			return new Point(px - widget.x, py - widget.y);

		}
		return null;

	}

}
