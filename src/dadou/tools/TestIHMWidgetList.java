package dadou.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.ihm.WidgetList;

public class TestIHMWidgetList {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game();
		WidgetList WidgetList;

		Button button = new Button(0);
		IHM ihm = g.nouvelleIHM();
		Widget w;
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);
		WidgetList = new WidgetList(ihm, 128, 32, 6,true);
		ihm.end();
		ihm.end();
		List<String> list = new ArrayList<String>();
		list.add("Cas1");
		list.add("Cas2");
		list.add("Cas3");
		list.add("Cas4");
		list.add("Cas5");
		list.add("Cas6");
		WidgetList.setModel(list);
		while (!g.isClosed()) {
			// System.out.println(" frame "+k);
			// k++;
			g.checkEscape();

			g.clear();

			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			ihm.dessiner(g.shaderWidget);

			if (button.isPressed()) {
				int x = Mouse.getX();
				int y = Mouse.getY();

				Widget sel = ihm.getWidgetAt(x, y);
				if (sel != null) {
					WidgetList.processClick(sel);

				}
			}

			Display.sync(30);

			g.displayBackBuffer();

		}

	}

}
