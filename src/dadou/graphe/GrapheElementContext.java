package dadou.graphe;

public class GrapheElementContext implements GrapheCheminTestEcouteur {
	public GrapheElement ge;
	public GrapheElement visite[];
	public Graphe graphe;
	public GrapheElementRef suivant;

	public String nom() {
		return ge.nom;
	}
	public String nomSuivant() {
		return suivant.nom;
	}

	public GrapheElementContext(Graphe graphe) {
		this.graphe = graphe;
		visite = new GrapheElement[graphe.elements.size()];
		this.ge = graphe.racine;

	}

	public void vider() {
		suivant = null;
		for (int i = 0; i < visite.length; i++) {
			visite[i] = null;
		}

	}

	public GrapheElementContext(Graphe graphe, String nom) {
		this.graphe = graphe;
		visite = new GrapheElement[graphe.elements.size()];
		this.ge = graphe.donnerGrapheElement(nom);

	}

	public void supprimer() {
		ge.supprimer(suivant);
		this.graphe.donnerGrapheElement(suivant.nom).supprimer(ge.nom);
	}
	public void annuler() {
		graphe.donnerGrapheElement(suivant.nom).reserve=false;
		
	}
	public void valider() {
		GrapheElement ge = graphe.donnerGrapheElement(suivant.nom);
		ge.visite = true;
		ge.reserve = false;
		this.suivant.poid++;
		for(GrapheElementRef ger:ge.suivants) {
			if (ger.nom.equals(this.ge.nom)) {
				ger.poid++;
			}
		}
		
		
	}

	public String suivant() {
		if (suivant == null) {
			GrapheElementRef result = ge.suivant(this);
			suivant = result;
		} else {
			ge = graphe.donnerGrapheElement(suivant.nom);
			GrapheElementRef result = ge.suivant(this);
			if (result == null) {
				for (int i = 0; i < visite.length; i++) {
					visite[i] = null;
				}
				result = ge.suivant(this);
			}
			suivant = result;

		}
		return suivant.nom;
	}

	public GrapheElementRef suivantPourExploration() {
		
		if (suivant == null) {
			GrapheElementRef result = ge.suivantPourExploration(this);
			if (result == null) {
				return null;
			}
			suivant = result;
		} else {
			ge = graphe.donnerGrapheElement(suivant.nom);
			GrapheElementRef result = ge.suivantPourExploration(this);
			if (result == null) {
			
				return null;
			}
			suivant = result;

		}
		return suivant;
	}
	
       public GrapheElementRef suivantFermeture() {
		
		if (suivant == null) {
			GrapheElementRef result = ge.suivantPourFermeture(this.graphe);
			if (result == null) {
				return null;
			}
			suivant = result;
		} else {
			ge = graphe.donnerGrapheElement(suivant.nom);
			GrapheElementRef result = ge.suivantPourFermeture(this.graphe);
			if (result == null) {
			
				return null;
			}
			suivant = result;

		}
		return suivant;
	}

}
