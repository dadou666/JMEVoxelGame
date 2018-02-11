package dadou.graphe;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;

import dadou.tools.BrickEditor;

public class GrapheCheminTest {
	public Vector3f position = new Vector3f();
	public Vector3f direction = new Vector3f();
	public Vector3f fin = new Vector3f();
	public float distance;
	public BoundingSphere bs = new BoundingSphere();
	public BrickEditor be;
	public float speed;
	public GrapheCheminTestEcouteur gc;
	public GrapheCheminTest suivant;
	public boolean peutVoler = true;
	public float hauteur;

	public GrapheCheminTest(BrickEditor be, GrapheCheminTestEcouteur gec, Vector3f debut, Vector3f fin, float rayon,
			float speed) {
		position.set(debut);
		direction.set(fin);
		direction.subtractLocal(debut);
		distance = direction.length();
		direction.normalizeLocal();

		direction.multLocal(speed);
		bs.setCenter(position);
		bs.setRadius(rayon);
		this.be = be;
		this.speed = speed;
		this.fin.set(fin);
		this.gc = gec;

	}

	public GrapheCheminTest(BrickEditor be, GrapheCheminTestEcouteur gec, Vector3f debut, Vector3f fin, float rayon,
			float speed, boolean peutVoler,float hauteur) {
		this(be, gec, debut, fin, rayon, speed);
		this.hauteur = hauteur;
		this.peutVoler = peutVoler;

	}

	public boolean executer() {
		boolean stop = false;
		if (distance < speed) {
			position.set(fin);
			stop = true;
		} else {
			distance -= speed;
			position.addLocal(direction);

		}
		bs.getCenter().set(position);

		if (be.decor.gestionCollision.verifierCollision(bs)) {
			gc.annuler();
			gc.supprimer();
			return false;

		}
		if (!peutVoler) {
			boolean echec = true;
			float iy= bs.getCenter().y;
			float h = hauteur;
			while( h > 0) {
			bs.getCenter().y =iy-h;
			h-=bs.radius;
			if (be.decor.gestionCollision.verifierCollision(bs)) {
				echec = false;
				

			}}
			if (echec) {
				gc.annuler();
				gc.supprimer();
				return false;
			}
			bs.getCenter().y =iy;
		}
		if (stop) {
			gc.valider();
			return false;
		}
		return true;

	}

}
