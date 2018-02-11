package dadou;

import java.io.Serializable;

import com.jme.math.Vector3f;

import dadou.mutator.Animation;

abstract public class ModeleEventInterface implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3010716386505931523L;
	transient public MondeInterfacePublic i;
	public ModelEvent me;
	public transient ObjetMobilePourModelInstance om;
	public Vector3f impactPosition = new Vector3f();
	public boolean testCollisionAvecDecor = true;

	public void charger(SauvegardeMonde monde) {

	}

	public void sauvegarder() {

	}

	abstract public void entree(ObjetMobile o, String position);

	abstract public void sortie(ObjetMobile om);

	abstract public void entreeZoneDetection(ElementJeux obj, ElementJeux oEntree);

	abstract public void sortieZoneDetection(ElementJeux obj, ElementJeux oSortie);

	abstract public void tire(ObjetMobile om, String position);

	abstract public void collisionCamera(ObjetMobile om);

	abstract public void finDeplacement(ObjetMobile om);

	abstract public void finSon(ObjetMobile om, String nomSon);

	abstract public void finParcour(ObjetMobile om);

	abstract public void demarer(ObjetMobile om, Object args);

	abstract public void boucle(ObjetMobile om);

	abstract public void finEchelle(ObjetMobile om);

	abstract public void collisionObjetMobile(ObjetMobile om1, ObjetMobile om2);

	abstract public void collisionZoneDetection(ObjetMobile om1, ObjetMobile om2);

	abstract public void collisionDecor(ObjetMobile om);

	abstract public void finOrientation(ObjetMobile om, Vector3f destination);

	abstract public void finAnimation(ObjetMobile om, Animation animation);

	abstract public void finTrajectoire(ObjetMobile om);

	abstract public void finDecomposition(ObjetMobile om);

}
