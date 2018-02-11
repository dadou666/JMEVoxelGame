package dadou;

import java.util.Map;

import com.jme.bounding.BoundingVolume;
import com.jme.renderer.Camera;

import dadou.son.Emeteurs;

public class ElementJeux {
	public Object mark;
	public Emeteurs emeteurs;
	public Object etat;
	public ModeleEventInterface mei;

	public <T extends ModeleEventInterface> T donnerModele(Class<T> clazz) {
		if (mei == null) {
			return null;
		}
		if (mei.getClass().isAssignableFrom(clazz)) {
			return (T) mei;
		}
		return null;
	}

	public boolean testCollision = true;
	public boolean annulerCollisionCamera = false;

	public BoundingVolume getBoundingVolume() {
		return null;
	}

	public float distanceCamera = 0.0f;

	public ModelInstance model() {
		return null;
	}

	public String donnerNom() {
		return null;
	}

	public void calculerDistanceCamera(Camera cam) {

	}

	public void entreeZoneDetection(ElementJeux o, ElementJeux om) {

	}

	public void sortieZoneDetection(ElementJeux o, ElementJeux om) {

	}

	public void finSon(String nomSon) {

	}

}
