package dadou.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.jme.renderer.Camera;

import dadou.Button;
import dadou.FBO;
import dadou.Game;
import dadou.Icone;
import dadou.Objet3D;
import dadou.OpenGLTools;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class ChoixGenerique<T> extends EtatBrickEditor {
	Widget[][] tableau;
	Map<Widget, ElementChoix<T>> tableauElementChoix = new HashMap<>();

	Widget wModification;
	Widget wModificationOld;

	public List<ElementChoix<T>> liste = new ArrayList<>();
	public ElementChoix<T> choix;
	FBO fbo;
	Game game;
	public int marge = 6;
	float speed = 0.008f;
	public int idx = 0;
	public IHM ihm;
	public int dx;
	public int dy;
	public Widget haut;
	public Widget bas;
	public boolean recharger = false;

	public int width() {
		return fbo.width;
	}

	public int height() {
		return fbo.height;
	}

	public int margeIHM = 150;

	public void init(int dx, int dy, FBO fbo, Game game) {

		this.game = game;
		ihm = game.nouvelleIHM();
		this.fbo = fbo;
		int width = width();
		int height = height();
		this.dx = dx;
		this.dy = dy;

		ihm.beginY();
		ihm.space(margeIHM);
		tableau = new Widget[dx][dy];

		ihm.beginX();
		ihm.space(margeIHM + dx * (width + marge) / 2 - 100);
		ihm.setSize(200, 40);
		haut = ihm.widget();
		ihm.space(margeIHM);
		ihm.end();
		ihm.setSize(width + marge, height + marge);

		for (int y = 0; y < dy; y++) {
			ihm.beginX();
			ihm.space(margeIHM);
			for (int x = 0; x < dx; x++) {
				tableau[x][y] = ihm.widget();

			}
			ihm.space(margeIHM);
			ihm.end();
		}
		ihm.beginX();
		ihm.space(margeIHM + dx * (width + marge) / 2 - 100);
		ihm.setSize(200, 40);
		bas = ihm.widget();
		ihm.space(margeIHM);
		ihm.end();
		ihm.space(margeIHM);
		ihm.end();
		this.modifierTableau();
		Graphics2D g = bas.getGraphics2DForUpdate();
		g.setColor(Color.black);
		g.fillRect(0, 0, bas.width, bas.height);
		g.setColor(Color.cyan);
		g.setStroke(new BasicStroke(2));
		g.fillPolygon(new int[] { 0, bas.width / 2, bas.width }, new int[] { 0,
				bas.height, 0 }, 3);
		// g.dispose();

		bas.update();

		g = haut.getGraphics2DForUpdate();
		g.setColor(Color.black);
		g.fillRect(0, 0, haut.width, haut.height);
		g.setColor(Color.cyan);
		g.setStroke(new BasicStroke(2));
		g.fillPolygon(new int[] { 0, haut.width / 2, haut.width }, new int[] {
				haut.height, 0, haut.height }, 3);
		// g.dispose();

		haut.update();
	}

	public void modifierTableau() {
		int u = idx;

		for (int y = 0; y < dy; y++) {
			for (int x = 0; x < dx; x++) {
				Widget w = tableau[x][y];
				if (u < this.liste.size()) {
					ElementChoix<T> ec = liste.get(u);
					this.contourColor = Color.GREEN;
					this.afficher(ec, w);
					this.tableauElementChoix.put(w, ec);
				} else {
					this.contourColor = Color.GREEN;
					Widget.drawRect(w, Color.BLACK, 0, marge);
					this.tableauElementChoix.put(w, null);
				}
				u++;
			}
		}

	}

	public void modifier() {
		if (recharger) {
			this.contourColor = Color.GREEN;
			this.modifierTableau();

			wModificationOld = null;
			recharger = false;
			return;
		}
		ElementChoix<T> ec = this.tableauElementChoix.get(wModification);

		if (ec == null) {
			if (this.wModificationOld != null) {
				this.contourColor = Color.GREEN;

				this.afficher(this.tableauElementChoix.get(wModificationOld),
						wModificationOld);
				this.wModificationOld = null;
			}
			return;
		}

		if (this.wModificationOld != null) {
			this.contourColor = Color.GREEN;

			this.afficher(this.tableauElementChoix.get(wModificationOld),
					wModificationOld);
		}
		this.wModificationOld = this.wModification;
		this.contourColor = Color.RED;

		ec.a += speed;
		this.afficher(ec, wModification);

	}

	Color contourColor = Color.GREEN;

	public void afficher(ElementChoix<T> ec, Widget w) {
 if (ec == null) {
	 return;
 }
		Objet3D om = ec.obj;
		if (om == null) {

			Graphics2D g = w.getGraphics2DForUpdate();

			if (ec.image != null) {
				g.drawImage(ec.image, marge / 2, marge / 2, width(), height(),
						null);

			} else {
				g.setColor(Color.BLUE);
				g.fillRect(marge / 2, marge / 2, width(), height());
			}
			g.setColor(contourColor);
			g.setStroke(new BasicStroke(marge));
			g.drawRect(0, 0, width() + marge, height() + marge);
			// g.dispose();

			w.update();
			return;
		}

		Icone icone = fbo.creerIconne(om, game, 3.14f / 5.0f, ec.a,
				ec.distance, Color.BLUE);

		BufferedImage bi = icone.getImage();
		Graphics2D g = w.getGraphics2DForUpdate();
		// g.setColor(Color.BLUE);
		// g.fillRect(0, 0, widgetModifier.width,widgetModifier.height);
		g.drawImage(bi, marge / 2, marge / 2, width(), height(), null);
		g.setColor(contourColor);
		g.setStroke(new BasicStroke(marge));
		g.drawRect(0, 0, width() + marge, height() + marge);

		// g.dispose();

		w.update();
	}

	public void dessiner(Camera cam) {
		OpenGLTools.exitOnGLError("glBindTexture");

		ihm.dessiner(game.shaderWidget);
	}

	public Button b = new Button(0);
	public boolean quitter = false;

	public void gerer() throws CouleurErreur {

		int x = Mouse.getX();
		int y = Mouse.getY();
		quitter = false;

		this.wModification = ihm.getWidgetAt(x, y);
		this.choix = null;
		ElementChoix<T> ec = this.tableauElementChoix.get(this.wModification);
		boolean keyFlecheHaut = false;

		boolean keyFlecheBas = false;
		if (this.BrickEditor != null) {
			keyFlecheHaut = this.BrickEditor.keyFlecheBas.isPressed();
			keyFlecheBas = this.BrickEditor.keyFlecheHaut.isPressed();
		}
		if (b.isPressed() || keyFlecheHaut || keyFlecheBas) {
			if (wModification == haut || keyFlecheHaut) {
				int newIdx = Math.max(idx - dx, 0);
				if (idx != newIdx) {
					idx = newIdx;
					recharger = true;
				}
				return;
			}
			if (wModification == bas || keyFlecheBas) {
				if (idx + dx >= this.liste.size()) {
					return;
				}
				idx += dx;
				recharger = true;
				return;
			}

			this.choix = ec;
			quitter = true;
		}
	}
}
