package dadou.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import org.lwjgl.input.Mouse;

import dadou.Arbre;
import dadou.Button;

public class EditeurArbre<T> {
	WidgetList WidgetList;
	InputfieldList inputFieldList = new InputfieldList();
	Widget remonter;
	public Widget supprimer;
	Widget descendre;
	public Widget ajouter;
	Widget valider;
	Widget annuler;
	Widget nom;
	GestionArbreControlleur<T> controlleur;
	public Arbre<T> arbre;
	int delay = -1;
	final int maxDelay = 7;
	public IHM ihm;

	public void updateText(String text, Widget selection, Color color) {

		Graphics2D g = selection.getGraphics2DForUpdate();
		g.setColor(color);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, selection.width, selection.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, selection.width - 2 * d, selection.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(text, 2 * d, selection.height - 2 * d);
		//g.dispose();

		selection.update();
	}

	public void setModel(Arbre<T> arbre) {
		this.arbre = arbre;
		WidgetList.setModel(arbre.entree());

	}

	public EditeurArbre(GestionArbreControlleur<T> controlleur, Arbre<T> arbre,
			IHM ihm, int width, int height, int nbLigne) {
		this.ihm = ihm;
		ihm.beginY();
		this.arbre = arbre;
		WidgetList = new WidgetList(ihm, width, height, nbLigne, true);
		if (arbre != null) {
			WidgetList.setModel(arbre.entree());
		}
		remonter = ihm.widget();

		descendre = ihm.widget();

		supprimer = ihm.widget();

		valider = ihm.widget();

		annuler = ihm.widget();

		nom = ihm.widget();
		inputFieldList.setInputField(nom, "");
		ajouter = ihm.widget();
		initButton();
		ihm.end();
		this.controlleur = controlleur;
	}

	public void initButton() {
		this.updateText("Remonter", remonter, Color.BLACK);

		this.updateText("Descendre", descendre, Color.BLACK);

		this.updateText("Ajouter", ajouter, Color.BLACK);

		this.updateText("Supprimer", supprimer, Color.BLACK);

		this.updateText("Valider", valider, Color.BLACK);
		this.updateText("Annuler", annuler, Color.BLACK);
	}

	public void processkeyboard() {
		inputFieldList.processKeyboard();
	}

	public void updateSelection() {
		if (delay == 0) {
			this.initButton();
			delay = -1;
		}
		if (delay > 0) {
			delay--;
		}
	}

	public void processMouseButton(Button btn) {
		if (btn.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();

			Widget sel = ihm.getWidgetAt(x, y);
			if (sel != null) {
				processClick(sel);

			}
		}

	}

	public String donnerSelection() {
		return WidgetList.getSelectionValue();
	}

	public boolean processClick(Widget w) {

		if (w == remonter) {
			this.initButton();
			this.updateText("Remonter", remonter, Color.BLUE);
			delay = maxDelay;
			if (arbre.donnerParent() == null) {
				return true;

			}
			arbre = arbre.donnerParent();
			WidgetList.setModel(arbre.entree());
			return true;
		}

		if (w == valider) {
			this.initButton();
			this.updateText("Valider", valider, Color.BLUE);
			delay = maxDelay;

			if (controlleur != null) {
				controlleur.valider(this);

			}
			return true;

		}

		if (w == annuler) {
			this.initButton();
			this.updateText("Annuler", annuler, Color.BLUE);
			delay = maxDelay;

			if (controlleur != null) {
				controlleur.annuler(this);

			}
			return true;

		}
		if (w == descendre) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Descendre", descendre, Color.BLUE);
			ListElement e = WidgetList.getSelection();
			if (e != null) {
				arbre = arbre.enfant(e.value);
				WidgetList.setModel(arbre.entree());
			}
			return true;
		}
		if (w == ajouter) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Ajouter", ajouter, Color.BLUE);
			String value = inputFieldList.models.get(nom).toString();
			if (value.trim().equals("")) {
				return true;
			}
			if (controlleur != null && controlleur.ajouter(this, value)) {
				Arbre<T> nouveau = arbre.ajouter(value);
				if (nouveau != null) {
					WidgetList.setModel(arbre.entree());

				}
			}
			return true;

		}

		if (w == supprimer) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Supprimer", supprimer, Color.BLUE);
			ListElement e = WidgetList.getSelection();
			if (e != null) {
				Arbre<T> sup = arbre.enfant(e.value);
				if (controlleur != null && controlleur.supprimer(this, sup)) {
					arbre.supprimer(e.value);
				}

				WidgetList.setModel(arbre.entree());
				return true;
			}

		}
		if (WidgetList.processClick(w)) {
			return true;
		}
		return inputFieldList.select(w);

	}

	public String donnerSaisie() {
		String value = inputFieldList.models.get(nom).toString();
		return value.trim();
	}

}
