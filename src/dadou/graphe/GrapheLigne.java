package dadou.graphe;

import com.jme.math.Vector3f;

public class GrapheLigne {
	public Vector3f debut;
	public Vector3f fin;
	public boolean fermeture = false;
	public GrapheLigne(Vector3f debut,Vector3f fin) {
		this.debut = debut;
		this.fin = fin;
		
	}
	

}
