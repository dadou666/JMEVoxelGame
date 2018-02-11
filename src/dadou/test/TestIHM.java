package dadou.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.param.GraphicsParam;

public class TestIHM {
	public static void setText(Widget w, String txt) {
		Widget.drawText(w, txt, Color.BLUE, Color.RED,Color.white,20);
		
	}

	public static void main(String[] args) throws LWJGLException {
		// TODO Auto-generated method stub
		//GraphicsParam.gp = new GraphicsParam();
		Game g = new Game();

		Map<Widget, String> msg = new HashMap<Widget, String>();
		Button button = new Button(0);
		IHM ihm = g.nouvelleIHM();
		Widget wHautGauche;
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.beginY();
		wHautGauche = ihm.widget();
		setText(wHautGauche, "Haut gauche");
		msg.put(wHautGauche, "Haut gauche");
		ihm.space(400);
		Widget wBasGauche = ihm.widget();
		setText(wBasGauche, "Bas gauche");
		msg.put(wBasGauche, "Bas gauche");
		ihm.end();
		ihm.space(700);
		ihm.end();
		
		
		IHM ihm1 = ihm;
		ihm = g.nouvelleIHM();
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(700);
		ihm.beginY();
		Widget wHautDroite = ihm.widget();
		setText(wHautDroite, "Haut droite");
		msg.put(wHautDroite, "Haut droite");
		ihm.space(400);
		Widget wBasDroite = ihm.widget();
		setText(wBasDroite, "Bas droite");
		msg.put(wBasDroite, "Bas droite");
		ihm.end();
		ihm.end();
		
		IHM ihm2= ihm;
		ihm.ajouterModel(() -> {
			setText(wBasDroite, msg.get(wBasDroite));
			setText(wBasGauche, msg.get(wBasGauche));
			setText(wHautDroite, msg.get(wHautDroite));
			setText(wHautGauche, msg.get(wHautGauche));

		});
		int count = 0;
		Widget wUpdate=null;
		while (!g.isClosed()) {
			// System.out.println(" frame "+k);
			// k++;
			g.checkEscape();

			g.clear();

			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			ihm1.dessiner(g.shaderWidget);
			ihm2.dessiner(g.shaderWidget);
			
			if (button.isPressed()) {
				int x = Mouse.getX();
				int y = Mouse.getY();
				System.out.println("Button x=" + x + " y=" + y);
				Widget sel = ihm1.getWidgetAt(x, y);
				if (sel != null) {
					wUpdate = sel;
					
					System.out.println(msg.get(sel));
				}
				sel = ihm2.getWidgetAt(x, y);
				if (sel != null) {
					wUpdate = sel;
					
					System.out.println(msg.get(sel));
				}
			}
			if (wUpdate != null) {
				msg.put(wUpdate, "[" + count+"]");
				
			}
			count++;
			Display.update();
			Display.sync(30);

			g.displayBackBuffer();

		}

	}

}
