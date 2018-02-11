package dadou;

import java.awt.Color;
import java.awt.Graphics2D;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class GameBar {
	public Widget widget;
	public int limit;
	public Color contour;
	public Color bar;
	int niveau;
	int niveauMax;

	public GameBar(IHM ihm) {
		 ihm.ajouterModel(() -> {
			modifier();
		});
		this.widget = ihm.widget();
		widget.show = false;

	}

	public void modifier() {
		Widget w=widget;
		Graphics2D g = w.getGraphics2DForUpdate();
		g.setColor(Color.black);
		g.fillRect(0, 0, w.width, w.height);
		int l = w.width;
		g.setColor(contour);
		float pmax = (float) niveauMax / (float) limit;
		int dwmax = (int) ((float) l * pmax);
		g.fillRoundRect(0, 0, dwmax, w.height, 25, 25);
		int d = 5;
		g.setColor(Color.black);
		g.fillRoundRect(d, d, dwmax - 2 * d, w.height - 2 * d, 25, 25);
		float p = (float) niveau / (float) limit;
		int dw = (int) ((float) l * p);
		g.setColor(bar);
		g.fillRoundRect(d, d, dw - 2 * d, w.height - 2 * d, 25, 25);

		// g.dispose();

		w.update();
	}

	public void modifier(int niveau, int niveauMax) {

		this.niveau = niveau;
		this.niveauMax = niveauMax;

	}

}
