package dadou;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.jeux.Trajectoire;

public class ModelInstance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7725743070839512967L;
	public ModelClasse modelClasse;
	public int x, y, z;
	public Quaternion rotation;
	public transient boolean visible = true;

	public String nomObjet;
	private Map<String,String> params = new HashMap<>();
	
	public Arbre<Trajectoire> trajectoires;
	public ModelEvent evenement;
	public Map<String,String> params() {
		if (params == null) {
			params = new HashMap<>();
		}
		return params;
	}
	
	public void initChemin(String base, Arbre<Trajectoire> arbre) {
		if (arbre == null) {
			return;
		}
		if (arbre.valeur == null) {
			return;
		}
		arbre.valeur.chemin = base + "/";
		for (String nom : arbre.entree()) {
			this.initChemin(base + "/" + nom, arbre.enfant(nom));

		}

	}

	public void initChemin() {
		if (trajectoires == null) {
			return;
		}
		this.initChemin(nomObjet, trajectoires);

	}

	public ModelInstance(int x, int y, int z, ModelClasse mc) {
		this.modelClasse = mc;
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
