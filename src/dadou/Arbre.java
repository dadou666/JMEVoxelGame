package dadou;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import dadou.event.GameEventNode;

public class Arbre<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2878699983616000001L;
	public T valeur;
	private Arbre<T> parent;
	public String nom;
	public String affichage;
	transient public DefaultMutableTreeNode node;

	public Arbre<T> donnerParent() {
		return parent;
	}

	public Arbre(Arbre<T> parent) {
		this.parent = parent;
	}

	public Arbre(Arbre<T> parent, T valeur) {
		this.parent = parent;
		this.valeur = valeur;
	}

	private Map<String, Arbre<T>> enfants = new HashMap<String, Arbre<T>>();
	private List<Arbre<T>> enfantsList = new ArrayList<>();

	public List<String> cheminListe(List<String> lst) {
		if (lst == null) {
			lst = new ArrayList<>();

		}
		if (this.parent != null) {
			this.parent.cheminListe(lst);
		}

		lst.add(nom);
		return lst;

	}

	public Arbre<T> ajouter(String nom) {

		if (enfants.get(nom) != null) {
			return null;
		}
		Arbre<T> result = new Arbre<T>(this);
		result.nom = nom;

		enfants.put(nom, result);
		enfantsList.add(result);
		if (node != null) {
			result.createMutableNodeTree();
			node.add(result.node);
		}
		return result;

	}

	public Arbre<T> ajouter(String[] chemin, String nom) {
		Arbre<T> arbre = this;
		for (String n : chemin) {
			Arbre<T> nv = arbre.enfant(n);
			if (nv == null) {
				nv = arbre.ajouter(n);

			}
			arbre = nv;

		}
		return arbre.ajouter(nom);

	}

	public boolean supprimer(String nom) {
		Arbre<T> result = this.enfants.get(nom);
		if (result == null) {
			return false;
		}
		if (this.node != null) {
			result.node.removeFromParent();

		}
		result.parent = null;
		enfants.remove(nom);
		enfantsList.remove(result);
		return true;
	}

	public Collection<Arbre<T>> enfants() {
		return enfants.values();
	}

	public Arbre<T> enfant(String nom) {
		return enfants.get(nom);
	}

	public int nombreEnfant() {
		return enfants.size();
	}

	public List<String> entree() {
		List<String> r = new ArrayList<String>();
		r.addAll(enfants.keySet());
		return r;
	}

	public String chemin(String separateur) {
		if (parent == null) {

			return this.nom;
		}
		String chemin = parent.chemin(separateur);
		if (chemin == null) {
			return nom;
		}
		return chemin + separateur + nom;

	}

	public void createMutableNodeTree() {
		node = new DefaultMutableTreeNode(this);
		for ( Arbre<T> e : this.enfantsList) {
			e.createMutableNodeTree();
			node.add(e.node);
		}

	}

	public String toString() {
		if (affichage != null) {
			return affichage;
		}
		if (this.nom == null || this.nom.isEmpty()) {
			return "*";
		}
		return this.nom;
	}

}
