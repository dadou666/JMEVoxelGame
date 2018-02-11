package dadou.graphe;

public class GrapheElementRef {
	public String nom;
	public long poid;
	public float distance;
	public boolean fermeture =false;
	public GrapheElementRef(String nom) {
		this.nom = nom;
		poid = 0;
	}
	public String toString() {
		return nom;
	}
	public boolean egale(String nom) {
		if (nom== null){
			return false;
		}
		return nom.equals(this.nom);
	}

}
