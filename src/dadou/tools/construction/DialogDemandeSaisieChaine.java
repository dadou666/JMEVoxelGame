package dadou.tools.construction;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.jme.renderer.Camera;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.tools.EtatBrickEditor;

public class DialogDemandeSaisieChaine extends EtatBrickEditor {
	public IHM ihm;
	public Widget fermer;
	public Widget valider;
	public Widget wNom;
	public InputfieldList inputFieldList;
	public Button button;
	public Widget actionWidget;
	public Widget message;
	public String libelleWidget;

	int delayButton = 10;
	int delayValue = 0;
	public boolean fermerAction = false;

	public String nom;

	public DialogDemandeSaisieChaine(Game game) {
		ihm = game.nouvelleIHM();
		inputFieldList = new InputfieldList();

		button = new Button(0);
		ihm.setSize(128, 32);
		ihm.beginY();

		ihm.beginX();
		ihm.space(200);

		ihm.beginY();
		ihm.space(200);
		ihm.setSize(64, 32);
		Widget.drawText(ihm.widget(), "Nom", Color.BLUE, Color.white, Color.red,0);

		ihm.end();

		ihm.beginY();
		ihm.space(200);
		ihm.setSize(256, 32);
		inputFieldList.setInputField(wNom = ihm.widget(), "");

		ihm.end();

		ihm.end();

		ihm.beginX();
		ihm.space(200);
		ihm.setSize(320, 32);
		ihm.beginY();
		valider = ihm.widget();
		Widget.drawText(valider, "Valider", Color.GREEN, Color.white, Color.red,0);
		fermer = ihm.widget();
		Widget.drawText(fermer, "Annuler", Color.GREEN, Color.white, Color.red,0);
		message = ihm.widget();
		Widget.drawText(message, "", Color.GREEN, Color.white, Color.red,0);
		ihm.space(200);
		ihm.end();

		ihm.space(200);
		ihm.end();
		ihm.end();

	}

	public boolean validerSaisie() {
		return true;

	}

	public void actionValider() {

	}

	public void actionAnnuler() {

	}

	public void gerer() {

		if (delayValue > 0) {
			delayValue--;
			if (delayValue == 0) {
				Widget.drawText(actionWidget, libelleWidget, Color.GREEN,
						Color.white, Color.red);
			}
			return;
		}
		if (fermerAction) {
			if (actionWidget == valider) {
				this.actionValider();

			} else {
				this.actionAnnuler();
			}
			return;

		}
		if (button.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			Widget sel = ihm.getWidgetAt(x, y);
			if (sel == valider) {

				if (!this.validerSaisie()) {
					return;
				}
				delayValue = delayButton;
				fermerAction = true;
				Widget.drawText(valider, "Valider", Color.BLUE, Color.white,
						Color.blue);
				libelleWidget = "Valider";
				this.actionWidget = valider;

				return;
			}
			if (sel == fermer) {
				// delayValue = delayButton;
				delayValue = delayButton;
				fermerAction = true;
				Widget.drawText(fermer, "Annuler", Color.BLUE, Color.white,
						Color.blue);
				libelleWidget = "Annuler";
				this.actionWidget = fermer;
				// Widget.drawText(fermer, "Fermer", Color.BLUE, Color.RED);
				return;
			}
			if (sel != null) {
				inputFieldList.select(sel);

			}
		}
		inputFieldList.processKeyboard();

	}

	public void dessiner(Camera cam) {
		ihm.dessiner(BrickEditor.game.shaderWidget);
	}

}
