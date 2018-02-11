package dadou.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.math.Vector3f;

import dadou.CameraPosition;
import dadou.Log;
import dadou.ObjetMobile;
import dadou.procedural.Regle;

public class Graphe {
	GrapheElement racine;
	GrapheElementSelector selector;
	public List<GrapheElement> racines = new ArrayList<>();

	public Map<String, GrapheElement> elements = new HashMap<>();
	List<String> elementsExplorations = new ArrayList<String>();
	public List<Float> distances = new ArrayList<Float>();
	public int idxDistance = 0;

	public void reset() {
		if (racine != null) {
			racine.poid = 0;
			racine.reservation = null;
		}
		for (GrapheElement ge : racines) {
			ge.poid = 0;
			ge.reservation = null;
		}
		for (GrapheElement ge : elements.values()) {
			ge.poid = 0;
			ge.reservation = null;
		}
	}

	public boolean estRacine(String nom) {
		for (GrapheElement ge : racines) {
			if (ge.nom.equals(nom)) {
				return true;
			}
		}
		return false;
	}

	public float distance() {
		return distances.get(idxDistance);
	}

	public int nombreIncomplet() {
		int n = 0;
		for (GrapheElement ge : elements.values()) {
			if (!ge.estComplet(this)) {
				n++;
			}
		}
		return n;

	}

	public boolean estFermer() {

		for (GrapheElement ge : elements.values()) {
			if (ge.suivants.size() > 1 && ge.nombreEnfant() == 1) {

				return false;
			}
		}
		return true;
	}

	public void creerLienInverse() {
		this.creerLienInverse(racine);
		for (GrapheElement ge : this.elements.values()) {
			this.creerLienInverse(ge);
		}

	}

	public void creerLienInverse(GrapheElement ge) {
		for (GrapheElementRef ger : ge.suivants) {
			if (ger.poid > 0) {
				GrapheElement tmp = this.donnerGrapheElement(ger.nom);
				if (tmp.donnerElement(ge.nom) == null) {
					GrapheElementRef nouveau = new GrapheElementRef(ge.nom);
					nouveau.poid++;
					tmp.suivants.add(nouveau);

				} else {
					// System.out.println(" racine deja lier");
				}

			}

		}

	}

	public void supprimerLienInutile() {
		racine.supprimerLienInutile();
		for (GrapheElement ge : this.elements.values()) {
			ge.supprimerLienInutile();
		}

	}

	public int nombre() {
		return elements.size();

	}

	public void afficherGraphe() {
		racine.afficher();
		for (GrapheElement ge : this.elements.values()) {
			ge.afficher();

		}

	}

	public String suivant(String nom, String ancienNom) {
		GrapheElement ge = this.donnerGrapheElement(nom);
		GrapheElement r = null;
		for (GrapheElementRef ger : ge.suivants) {

			GrapheElement e = this.donnerGrapheElement(ger.nom);
			if (ger.poid > 0 && (ancienNom == null || !ancienNom.equals(ger.nom))) {
				if (r == null || e.poid < r.poid) {
					r = e;

				}
			}

		}
		if (r == null) {
			return ge.suivants.get(0).nom;
		}
		r.poid++;
		return r.nom;
	}

	public List<GrapheElementContext> contextsExploration() {
		this.idxDistance = 0;
		;
		List<GrapheElementContext> r = new ArrayList<>();
		do {
			GrapheElementContext gec = this.creerGrapheElementContext(racine.nom);
			boolean reserve = false;
			GrapheElementRef suivant;

			suivant = gec.suivantPourExploration();

			if (suivant != null) {
				r.add(gec);
				// return r;

			}
			for (GrapheElement elt : elements.values()) {
				if (elt.reserve) {
					reserve = true;
				}
				if (elt.visite) {
					gec = this.creerGrapheElementContext(elt.nom);

					suivant = gec.suivantPourExploration();

					if (suivant != null) {
						r.add(gec);
						// return r;

					}

				}

			}
			if (r.isEmpty()) {
				if (reserve) {
					return r;
				}
				this.idxDistance++;

				if (this.idxDistance >= distances.size()) {
					return r;
				}

			}
		} while (r.isEmpty());
		return r;

	}

	public Graphe(GrapheConstructeur gc) {
		for (GrapheSommet gs : gc.sommets) {
			GrapheElement ge = new GrapheElement();
			ge.nom = gs.nom;
			ge.graphe = this;
			this.elements.put(gs.nom, ge);
		}
		for (int i = 0; i < gc.sommets.size(); i++) {
			for (int j = 0; j < gc.sommets.size(); j++) {
				if (gc.liaisonDirecte[i][j] || gc.liaisonDirecte[j][i]) {
					GrapheSommet gsi = gc.sommets.get(i);
					GrapheSommet gsj = gc.sommets.get(j);
					GrapheElementRef a = new GrapheElementRef(gsj.nom);
					a.poid = 1;

					this.elements.get(gsi.nom).ajouter(a);
					a = new GrapheElementRef(gsi.nom);
					a.poid = 1;

					this.elements.get(gsj.nom).ajouter(a);

				}

			}
		}
		this.racines = new ArrayList<GrapheElement>();
		for (GrapheSommet gs : gc.racines()) {
			this.racines.add(this.elements.get(gs.nom));
		}

	}

	public Graphe(List<String> values, String nomDebut) {
		int i = 0;
		elementsExplorations.add(nomDebut);
		for (String nom : values) {
			GrapheElement ge = new GrapheElement();
			ge.nom = nom;
			for (String value : values) {
				if (!value.equals(nom))
					ge.suivants.add(new GrapheElementRef(value));
			}
			ge.suivants.remove(nom);
			ge.graphe = this;
			ge.index = i;
			i++;
			elements.put(nom, ge);

		}
		racine = new GrapheElement();
		racine.nom = nomDebut;
		racine.graphe = this;
		racine.visite = true;
		for (String value : values) {
			racine.suivants.add(new GrapheElementRef(value));
		}

	}

	public int nbRacineLibre() {
		int n = 0;
		for (GrapheElement ge : racines) {
			if (ge.reservation == null) {
				n++;
			}
		}
		return n;
	}

	public int nbRacine() {
		return racines.size();
	}

	public int nbRacineReserve() {
		int n = 0;
		for (GrapheElement ge : racines) {
			if (ge.reservation != null) {
				n++;
			}
		}
		return n;
	}

	public void libererRacines() {
		for (GrapheElement ge : racines) {
			if (ge.reservation != null) {
				ObjetMobile om = (ObjetMobile) ge.reservation;
				if (om.octreeLeaf == null) {
					ge.reservation = null;
					Log.print("erreur liberation");
				}
			}
		}
	}

	public GrapheElement racine() {
		if (this.racines.size() >= 1) {

			for (GrapheElement ge : racines) {
				if (ge.reservation == null) {
					GrapheElement tmp = ge.grapheElementSuivantLibre(null);
					if (tmp != null) {
						ge.tmp = tmp;
						return ge;

					}
				}
			}
			return null;

		}
		return racine;
	};
	
	public int comparer(GrapheElement a, GrapheElement b,Vector3f p,Map<String,CameraPosition> positions) {
		Vector3f posA = positions.get(a.nom()).translation;
		Vector3f posB = positions.get(b.nom()).translation;
		tmp.set(p);
		tmp.subtractLocal(posA);
		float distA=tmp.length();
		tmp.set(p);
		tmp.subtractLocal(posB);
		float distB=tmp.length();
		return Float.compare(distA, distB);
		
	}
	public Vector3f tmp = new Vector3f();
	public GrapheElement racineProche(Vector3f p,Map<String,CameraPosition> positions,int nbProche) {
		if (this.racines.size() >= 1) {
			Collections.sort(racines, (GrapheElement a, GrapheElement b) -> {
			
				return comparer(a,b,p,positions);
						


			});
			int i=0;
			for (GrapheElement ge : racines) {
				if (i > nbProche) {
					return null;
				}
				i++;
				if (ge.reservation == null) {
					GrapheElement tmp = ge.grapheElementSuivantLibre(null);
					if (tmp != null) {
						ge.tmp = tmp;
						return ge;

					}
				}
			}
			return null;

		}
		return racine;
	};

	public GrapheElement donnerGrapheElement(String nom) {
		GrapheElement e = elements.get(nom);
		if (e == null) {
			if (racine == null) {
				return null;
			}
			if (nom.equals(racine.nom)) {
				return racine;
			}
		}
		return e;

	}

	public GrapheElementContext creerGrapheElementContext() {
		return new GrapheElementContext(this);
	}

	public GrapheElementContext creerGrapheElementContext(String nom) {
		return new GrapheElementContext(this, nom);
	}

}
