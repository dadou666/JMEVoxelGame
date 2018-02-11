package dadou.tools.couleur;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.EtatBrickEditor;

public class RGBColorSelector extends EtatBrickEditor {
	Map<Widget, Color> colors;
	Widget color;
	ColorSelector red;
	ColorSelector green;
	ColorSelector blue;
	public IHM ihm;
	Button button;
	int idx = 0;

	public List<Color> getColorsList() {
		List<Color> list = new ArrayList<Color>();
		for (Color color : colors.values()) {
			list.add(color);
		}
		return list;

	}

	public void createWidgetColor(List<Color> colorsList) {
		color = ihm.widget();
		Color c = getColor(colorsList);
		colors.put(color, c);
		setColor(c, false);

	}

	public Color getColor(List<Color> colorsList) {
		Color r = Color.BLACK;
		if (colorsList == null) {
			return r;
		}
		if (idx >= colorsList.size()) {
			return r;
		}
		r = colorsList.get(idx);
		idx++;
		return r;
	}

	public RGBColorSelector(Game game, List<Color> colorsList) {
		colors = new HashMap<Widget, Color>();
		button = new Button(0);
		idx = 0;
		ihm = game.nouvelleIHM();
		ihm.beginY();
		ihm.space(300);
		ihm.beginX();
		ihm.space(300);
		ihm.beginY();
		red = new ColorSelector(ihm, Color.RED, 110, 40);
		green = new ColorSelector(ihm, Color.GREEN, 110, 40);
		blue = new ColorSelector(ihm, Color.BLUE, 110, 40);

		ihm.end();
		ihm.setSize(40, 40);

		ihm.beginY();

		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		ihm.end();
		ihm.beginY();

		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		ihm.end();
		ihm.beginY();

		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		createWidgetColor(colorsList);
		ihm.end();

		ihm.space(300);
		ihm.end();
		ihm.space(300);
		ihm.end();
		// setColor(Color.BLACK, false);

	}

	public void setColor(Color c, boolean select) {
		Graphics2D g = color.getGraphics2DForUpdate();
		if (select) {
			int marge = 3;
			this.red.setColorValue(c.getRed());
			this.green.setColorValue(c.getGreen());
			this.blue.setColorValue(c.getBlue());
			g.setColor(Color.red);
			g.fillRect(0, 0, color.width, color.height);
			g.setColor(Color.green);
			g.fillRect(marge, marge, color.width - 2 * marge, color.height - 2
					* marge);
			marge = 6;
			g.setColor(c);
			g.fillRect(marge, marge, color.width - 2 * marge, color.height - 2
					* marge);
		} else {
			g.setColor(c);
			g.fillRect(0, 0, color.width, color.height);

		}
		g.dispose();
		color.update();
	}

	public Color getColor() {
		String nomHabillage =this.BrickEditor.decor.DecorDeBriqueData.nomHabillage;
	
		if (nomHabillage!= null) {
		
			return this.BrickEditor.couleurPourHabillage(nomHabillage);
					
		}
		Color r = colors.get(color);
		if (this.BrickEditor.grille) {
			r = new Color(r.getRed(), r.getGreen(), r.getBlue(), 0);
		} else {
			r = new Color(r.getRed(), r.getGreen(), r.getBlue(), 255);
		}
		return r;
	}

	public boolean action(Widget sel) {
		if (sel == null) {
			return false;
		}
		if (button.isPressed()) {
			Color c = colors.get(sel);
			if (c != null) {
				if (sel != color) {
					this.setColor(colors.get(color), false);
				}
				color = sel;
				this.setColor(c, true);
				return true;

			}

			boolean r = red.action(sel) || green.action(sel)
					|| blue.action(sel);
			Color newC = new Color(red.value, green.value, blue.value);
			this.setColor(newC, true);
			colors.put(color, newC);
			return r;

		}
		return false;

	}

	public void gerer() {
		int x = Mouse.getX();
		int y = Mouse.getY();
		Widget w = ihm.getWidgetAt(x, y);
		action(w);
		ihm.dessiner(BrickEditor.game.shaderWidget);

	}

	public void echanger() {
		BrickEditor.etat = BrickEditor.selection;

	}

}
