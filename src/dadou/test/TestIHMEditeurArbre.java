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

import dadou.Arbre;
import dadou.Button;
import dadou.Game;
import dadou.ihm.EditeurArbre;
import dadou.ihm.GestionArbreControlleur;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.ihm.WidgetList;

public class TestIHMEditeurArbre  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game g = new Game();
		WidgetList WidgetList;
		EditeurArbre<String> editeurArbre;

		Button button = new Button(0);
		IHM ihm = g.nouvelleIHM();
		Widget w;
		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);
	
		Arbre<String> arbre= new Arbre<String>(null);
		editeurArbre = new EditeurArbre<String>(null,arbre,ihm,128,32,5);
		
		ihm.end();
		ihm.end();
		GestionArbreControlleur<String> controlleur = new GestionArbreControlleur<String>() {

	

			@Override
			public void valider(EditeurArbre<String> editeurArbre) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void annuler(EditeurArbre<String> editeurArbre) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean supprimer(EditeurArbre<String> editeurArbre,
					Arbre<String> nv) {
				// TODO Auto-generated method stub
				
				return true;
			}

			@Override
			public boolean ajouter(EditeurArbre<String> editeurArbre,
					String nv) {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
		while (!g.isClosed()) {
			// System.out.println(" frame "+k);
			// k++;
			g.checkEscape();

			g.clear();

			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			ihm.dessiner(g.shaderWidget);
			editeurArbre.processkeyboard();
			editeurArbre.updateSelection();
			editeurArbre.processMouseButton(button);
			Display.update();
			Display.sync(30);

			//g.displayBackBuffer();

		}

	}

}
