package dadou.test;

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

import com.jme.input.InputSystem;

import dadou.Arbre;
import dadou.Button;
import dadou.Game;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.EditeurArbre;
import dadou.ihm.GestionListe;
import dadou.ihm.GestionListeControlleur;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.ListElement;
import dadou.ihm.Widget;
import dadou.ihm.WidgetList;

public class TestIHMGestionListe {
	static List<String> values = new ArrayList<String>();
	static boolean showGestionListe = false;

	public static void main(String[] args) throws CouleurErreur {
		// TODO Auto-generated method stub
		Game g = new Game();
		WidgetList WidgetList;
		GestionListe gestionListe;

		Button button = new Button(0);
		IHM ihm = g.nouvelleIHM();
		Widget w;
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);

		values.add("toto");
		values.add("titi");
		gestionListe = new GestionListe(new GestionListeControlleur() {

			@Override
			public void execute(GestionListe gestionListe, boolean valider) {
				// TODO Auto-generated method stub
				if (valider) {
					values = gestionListe.model;
					ListElement e = gestionListe.getSelection();
					if (e != null) {
						System.out.println(e.value);
					}
				}
				showGestionListe = false;
				return ;
			}

		}, values, false, ihm, 128, 32, 5);

		ihm.end();
		ihm.end();

		while (!g.isClosed()) {
			// System.out.println(" frame "+k);
			// k++;
			g.checkEscape();

			g.clear();
			
			InputSystem.update();
			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			if (g.KeyReturn.isPressed() && !showGestionListe) {
				showGestionListe = true;
				gestionListe.setModel(values);

			}
			if (showGestionListe) {
				ihm.dessiner(g.shaderWidget);
				//gestionListe.processkeyboard();
				gestionListe.updateSelection();
				if (button.isPressed()) {
					int x = Mouse.getX();
					int y = Mouse.getY();

					Widget sel = ihm.getWidgetAt(x, y);
					if (sel != null) {
						gestionListe.processClick(sel);

					}
				}

			}
			Display.sync(30);

			g.displayBackBuffer();

		}

	}

}
