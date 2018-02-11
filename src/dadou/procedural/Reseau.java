package dadou.procedural;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reseau<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Map<String, Noeud<T>> noeuds;
	public List<String> racines;

	public void lier(String a, String b) {
		Noeud<T> na = noeuds.get(a);
		Noeud<T> nb = noeuds.get(b);
		na.ajouterSuivant(b);
		nb.ajouterSuivant(a);

	}

	public void creerNoeud(String a) {

		Noeud<T> n;
		if (noeuds == null) {
			noeuds = new HashMap<>();
		} else {
			n = noeuds.get(a);
			if (n != null) {
				return;
			}
		}
		n = new Noeud<>();
		n.suivants = new ArrayList<String>();
		noeuds.put(a, n);
	}

	public void ajouterRacine(String nom) {
		if (racines == null) {
			racines = new ArrayList<>();
			racines.add(nom);
		} else {
			if (racines.contains(nom)) {
				return;
			}
			racines.add(nom);
		}
		

	}

}
