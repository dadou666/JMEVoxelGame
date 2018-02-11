package dadou.tools.canon;

import com.jme.bounding.BoundingVolume;
import com.jme.math.Plane;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;
import dadou.Octree;

public class CibleAffichageDistance {
	long periode = 1000;
	long t;
	public String nom;
	public Cible cible;
	public float distanceAffichage = 1000.0f;
	public Integer valeur;

	public CibleAffichageDistance(float size) {
		t = 0;
		cible = new Cible();
		cible.size =size;
	}

	public Vector2f screenPos = new Vector2f();
	public Vector3f selPos = new Vector3f();
	Vector3f positionObjectif;
	public Vector3f tmp = new Vector3f();

	public void modifierCible(int distance) {
		long duree = System.currentTimeMillis() - t;
		if (duree > periode) {

			cible.initialiserCibleAvecValeur(nom, distance);
			t = System.currentTimeMillis();
		}

	}

	public void dessiner(Camera cam) {
		if (this.positionObjectif != null) {
			screenPos.set(positionObjectif.x, positionObjectif.y);
			selPos.set(cam.getWorldCoordinates(this.screenPos, 0.1f));
			cible.dessiner(selPos, cam);
		}

	}

	public void calculerPositionEcranObjectif(ObjetMobile om, Camera cam) {

		BoundingVolume box = om.getBoundingVolume();
		tmp.set(box.getCenter());
		tmp.subtractLocal(cam.getLocation());
		float length = tmp.length();
		if (length > distanceAffichage) {
			positionObjectif = null;
			return;
		}
		int planeOk = 0;
		int side;
		for (int plane : Octree.frustumPlanes) {
			side = box.whichSide(cam.getPlane(plane));
			// side = this.getSide(cam.getPlane(plane));
			if (side == Plane.NEGATIVE_SIDE) {

				positionObjectif = null;
				return;
			} else if (side == Plane.POSITIVE_SIDE || side == Plane.NO_SIDE) {
				planeOk++;
			}

		}

		if (planeOk == Octree.frustumPlanes.length) {
			if (positionObjectif == null) {
				positionObjectif = new Vector3f();

			}
			cam.getScreenCoordinates(box.getCenter(), positionObjectif);
			if (valeur != null) {
				modifierCible(valeur);
			} else {
				modifierCible((int) length);
			}
			positionObjectif.z = 0.0f;
			// Log.print("position="+this.positionObjectif);

		} else {

			positionObjectif = null;
		}

	}

}
