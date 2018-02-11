package dadou.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import dadou.MondeInterfacePublic;

public class MenuJeux {
	public Action action;
	public String nom;
	public String titre;
	public String message;
	public List<MenuJeux> elements = new ArrayList<>();
	public MenuJeux parent;
	int idxSelection = 0;
	int decalage=0;
	int hauteur;
public	String couleur;

	public static MenuJeux creer(String nom, Action a, MenuJeux... elements) {
		MenuJeux r = new MenuJeux();
		r.nom = nom;
		r.action = a;
		for (MenuJeux elt : elements) {
			r.elements.add(elt);
			elt.parent = r;
		}
		return r;
	}

	public static MenuJeux creer(String nom, Action a, List<MenuJeux> elements) {
		MenuJeux r = new MenuJeux();
		r.nom = nom;
		r.action = a;
		for (MenuJeux elt : elements) {
			r.elements.add(elt);
			elt.parent = r;
		}
		return r;
	}
	public static MenuJeux creer(String message,String nom, Action a, MenuJeux... elements) {
		MenuJeux r = new MenuJeux();
		r.message=message;
		r.nom = nom;
		r.action = a;
		for (MenuJeux elt : elements) {
			r.elements.add(elt);
			elt.parent = r;
		}
		return r;
	}

	public static MenuJeux creer(String message,String nom, Action a, List<MenuJeux> elements) {
		MenuJeux r = new MenuJeux();
		r.message=message;
		r.nom = nom;
		r.action = a;
		for (MenuJeux elt : elements) {
			r.elements.add(elt);
			elt.parent = r;
		}
		return r;
	}
	public String titre() {
		if (titre == null) {
			return nom;
		}
		return titre;
	}

	public void initialiser(MondeInterfacePublic i, int largeur, int hauteur, int tailleLigne) {
		if (elements.isEmpty()) {
			return;
		}
		this.idxSelection =0;
		this.hauteur = hauteur;
		Color c = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 150);
		i.tableauTransparent(false);
		i.initialiserTableau(nom, largeur, hauteur * tailleLigne, c);
	
		i.modifierTableau(nom);
		i.nombreElement(hauteur);
		if (message != null) {
			i.centrerMessage(true);
		
			i.ajouterMessage(message);
			i.ajouterMessage("");
			decalage = 2;
		}
		for (MenuJeux element : elements) {
			i.centrerMessage(true);
			

			i.ajouterMessage(element.titre());

		}

		i.modifierMessage("[" + this.elements.get(0).titre() + "]", decalage);
		for (MenuJeux element : elements) {

			element.initialiser(i, largeur, hauteur, tailleLigne);

		}

	}

	public void modifierTitre(MondeInterfacePublic i, int idx) {
		MenuJeux elt = this.elements.get(idx);
		i.couleurMessage(elt.couleur);
		if (idx == this.idxSelection) {

			i.modifierMessage("[" + elt.titre() + "]", idx+decalage);
		} else {
			i.modifierMessage(elt.titre(), idx+decalage);
		}

	}

	public MenuJeux selection() {
		return elements.get(idxSelection);
	}

	public MenuJeux ajouter(String nom, Action action) {
		MenuJeux a = new MenuJeux();
		a.nom = nom;
		a.action = action;
		this.elements.add(a);
		a.parent = this;
		return a;
	}

	public void descendre(MondeInterfacePublic i) {
		if (this.idxSelection + 1 >= elements.size()) {
			return;
		}
		this.modifierSelection(i, this.idxSelection + 1+decalage);
	}

	public void monter(MondeInterfacePublic i) {
		if (this.idxSelection - 1 < 0) {
			return;
		}
		this.modifierSelection(i, this.idxSelection - 1+decalage);
	}

	public void activer(MondeInterfacePublic i) {

		i.modifierTableau(nom);
		i.nombreElement(hauteur);
		i.indexVue(this.idxSelection);
		i.activerTableauAvecTransition(nom);
	}

	public MenuJeux entrer(MondeInterfacePublic i) {

		if (this.elements.isEmpty()) {
			return null;
		}
		MenuJeux r = this.elements.get(this.idxSelection);
		if (!r.elements.isEmpty()) {
			r.activer(i);
		}
		if (r.action != null) {
			if (r.action.executer(r)) {
				return null;
			}
		
		}
	
		if (r.elements.isEmpty()) {
		
			return this;
		}

		return r;
	}

	public void modifierSelection(MondeInterfacePublic i, int idx) {
		if (idx == this.idxSelection) {
			return;
		}
		i.couleurMessage(this.elements.get(this.idxSelection).couleur);
		i.modifierMessage(this.elements.get(this.idxSelection).titre(), this.idxSelection+decalage);
		i.couleurMessage(this.elements.get(idx).couleur);
		i.modifierMessage("[" + this.elements.get(idx).titre() + "]", idx+decalage);
		this.idxSelection = idx;
		i.nombreElement(hauteur);
		i.indexVue(this.idxSelection+decalage);
	}

	public MenuJeux gerer(MondeInterfacePublic i, int key) {
		if (key == i.eventKeyValue("KEY_ESCAPE")) {
			if (parent == null) {
				
				return null;
			} else {
				parent.activer(i);
				return parent;
			}

		}

		if (key == i.eventKeyValue("KEY_RETURN")) {
			return this.entrer(i);
		}
		if (key == i.eventKeyValue("KEY_UP")) {

			this.monter(i);
			return this;
		}
		if (key == i.eventKeyValue("KEY_DOWN")) {

			this.descendre(i);
			return this;
		}
		return this;

	}
}
