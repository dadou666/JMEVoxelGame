package dadou.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GrapheElement {
	public List<GrapheElementRef> suivants = new ArrayList<>();
	public Graphe graphe;
	public Object reservation = null;
	public GrapheElement tmp;
	public int index;
	public boolean visite = false;
	public boolean reserve = false;
	public long poid = 0;

	String nom;

	public String nom() {
		return nom;
	}

	public String toString() {
		return nom;
	}

	public void ajouter(GrapheElementRef ger) {
		for (GrapheElementRef tmp : suivants) {
			if (tmp.egale(ger.nom)) {
				return;
			}
		}
		suivants.add(ger);
	}

	public void trierSuivants() {
		Collections.sort(suivants, new Comparator<GrapheElementRef>() {

			@Override
			public int compare(GrapheElementRef o1, GrapheElementRef o2) {
				// TODO Auto-generated method stub
				return Float.compare(o1.distance, o2.distance);
			}
		});

	}

	public GrapheElementRef donnerElement(String nom) {
		for (GrapheElementRef ger : this.suivants) {
			if (ger.nom.equals(nom)) {
				return ger;
			}

		}
		return null;
	}

	public void supprimerLienInutile() {
		List<GrapheElementRef> nouveauSuivant = new ArrayList<>();
		for (GrapheElementRef ger : suivants) {
			if (ger.poid > 0) {
				nouveauSuivant.add(ger);
			}

		}
		suivants = nouveauSuivant;
	}

	public boolean estComplet(Graphe graphe) {
		for (GrapheElementRef ger : suivants) {
			if (!graphe.donnerGrapheElement(ger.nom).visite) {
				return false;
			}
		}
		return true;
	}

	public void afficher() {
		System.out.print(nom);
		System.out.print("->");
		for (GrapheElementRef ger : suivants) {
			if (ger.poid > 0) {
				System.out.print(ger.nom + " ");
			}
		}
		System.out.println();

	}

	public int nombreEnfant() {
		int n = 0;
		for (GrapheElementRef ger : this.suivants) {
			if (ger.poid > 0 && !ger.nom.equals(nom)) {
				n++;
			}
		}
		return n;

	}

	public GrapheElementRef suivantPourFermeture(Graphe graphe) {
		if (reserve) {
			return null;
		}
		int n = this.nombreEnfant();
		if (n > 1) {
			return null;
		}

		for (GrapheElementRef ger : this.suivants) {

			for (int i = 0; i < graphe.distances.size(); i++) {
				float dist = graphe.distances.get(i);
				if (ger.distance <= dist && ger.poid == 0) {
					// ger.poid++;
					reserve = true;
					return ger;
				}
			}
		}

		return null;

	}

	public String suivantLibre(Object reservation, String nomPrecedent) {
		GrapheElement r = grapheElementSuivantLibre(reservation, nomPrecedent);
		if (r == null) {
			// r= this.grapheElementSuivantLibre(reservation);
			if (r == null) {
				return null;
			}
		}
		r.reservation = reservation;
		r.poid++;
		return r.nom;
	}

	public GrapheElement grapheElementSuivantLibre(Object reservation) {

		GrapheElement r = null;
		for (GrapheElementRef ger : suivants) {

			GrapheElement e = graphe.donnerGrapheElement(ger.nom);
			if (e.reservation == null && ger.poid > 0 && (!nom.equals(ger.nom))) {
				if (r == null || e.poid < r.poid) {
					r = e;

				}
			}

		}
		if (r == null) {
			return null;
		}
		r.reservation = reservation;
		r.poid++;
		return r;
	}

	public GrapheElement grapheElementSuivantLibre(Object reservation, String nomPrecedent) {

		GrapheElement r = null;
		if (suivants.size() != 1) {
			if (graphe.selector != null) {
				r = graphe.selector.grapheElementSuivantLibre(reservation,this, nomPrecedent);
			} else {
				for (GrapheElementRef ger : suivants) {

					GrapheElement e = graphe.donnerGrapheElement(ger.nom);
					if (e.reservation == null && ger.poid > 0 && (!nom.equals(ger.nom)) && !ger.egale(nomPrecedent)) {
						if (r == null || e.poid < r.poid) {
							r = e;

						}
					}

				}
			}
			if (r == null) {
				return null;
			}
		} else {
			r = graphe.donnerGrapheElement(suivants.get(0).nom);
		}
		r.reservation = reservation;
		r.poid++;
		return r;
	}

	public GrapheElementRef suivantPourExploration(GrapheElementContext gec) {

		for (int i = 0; i < suivants.size(); i++) {
			GrapheElementRef ger = suivants.get(i);
			GrapheElement ge = gec.graphe.donnerGrapheElement(ger.nom);

			if (!ge.visite && !ge.reserve) {

				if (ger.distance < gec.graphe.distance()) {
					// ge.visite = false;
					ge.reserve = true;

					return ger;
				}

			}
		}

		return null;
	}

	public GrapheElementRef suivant(GrapheElementContext ctx) {

		GrapheElementRef result = null;

		for (int i = 0; i < suivants.size(); i++) {
			GrapheElementRef ger = suivants.get(i);

			if (ger.poid > 0 && result == null) {
				result = ger;

			} else {
				if (ger.poid < result.poid && ger.poid > 0) {
					result = ger;

				}
			}

		}
		if (result != null) {
			result.poid++;

		}
		return result;
	}

	public void supprimer(GrapheElementRef nom) {
		suivants.remove(nom);
	}

	public void supprimer(String nom) {
		GrapheElementRef ref = null;
		for (GrapheElementRef r : this.suivants) {
			if (r.nom.equals(nom)) {
				ref = r;
				break;
			}
		}
		if (ref != null) {
			this.suivants.remove(ref);
		}
	}

}
