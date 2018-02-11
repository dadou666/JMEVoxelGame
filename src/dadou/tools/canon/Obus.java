package dadou.tools.canon;

import java.util.HashSet;
import java.util.Set;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;

import dadou.Objet3D;
import dadou.ObjetMobile;
import dadou.son.Emeteurs;
import dadou.son.Son;

public class Obus {
	public BoundingSphere bs = new BoundingSphere();
	public Objet3D objet3D;
	public float distance;
	public String nomProjectile;
	public Emeteurs emeteurs;
	public Son sonImpact;
	public Set<ObjetMobile> collisions = new HashSet<>();

	public Vector3f direction= new Vector3f();
	public Vector3f directionNormal= new Vector3f();
	public Vector3f origine = new Vector3f();
	public Vector3f oldPos = new Vector3f();
	Obus suivant;
	public void sonImpact(Son sonImpact) {
		if (sonImpact == null) {
			return;
		}
		emeteurs.chargerSon(sonImpact.nom);
		emeteurs.volume(sonImpact.nom, sonImpact.volume);
		this.sonImpact = sonImpact;
		
	}
	public Obus() {
		this.emeteurs = new Emeteurs(null);
	}
	public void demarerSonImpact(Vector3f posJoueur,float rayon) {
		if (this.sonImpact == null) {
			return;
		}
		this.oldPos.subtractLocal(posJoueur);
		float length = this.oldPos.length();
		float coeff = 1 - length/rayon;
		this.emeteurs.demarer(sonImpact.nom, coeff);
		
		
		
	}

}
