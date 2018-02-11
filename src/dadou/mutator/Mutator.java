package dadou.mutator;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class Mutator {
	public BrickEditor be;
	public Quaternion oldRotation = new Quaternion();
	public Vector3f oldTranslation = new Vector3f();

	public ObjetMobilePourModelInstance obj;
	// public BoundingSphere bs;
	public MutatorControler controler;
	public boolean pause = false;
	public boolean alive = true;
	public boolean collision = false;
	public boolean interruption = false;

	public boolean explose() {
		return false;
	}

	public Mutator interrompre(Mutator m) {
		return m;
	}

	public boolean animer() {
		return false;
	}

	public void redemarer() {
		this.pause = false;
		this.interruption = false;
	}

	public boolean process() {

		try {
			if (pause) {
				return true;
			}
			if (obj.construction != null) {
				return true;
			}
			boolean r = this.animer();
			if (!r) {
				alive = false;
				if (obj.mutator == this) {
					obj.mutator = null;
				}
			}
			return r;
		} catch (Throwable t) {
			obj.mutator = null;
			t.printStackTrace();
			return false;
		}
	}

	public void fini() {

	}

	public boolean estActif() {
		return obj.mutator == this;
	}

	public void sauvegarder() {
		this.oldRotation.set(obj.getRotationLocal());
		this.oldTranslation.set(obj.getTranslationLocal());

	}

	public void annuler() {
		this.obj.setTranslationEtRotation(oldTranslation, oldRotation);

		obj.updateBoxForMouvement();
		obj.updateOctree();

	}

	public boolean verifierCollision() {
		collision = this._verifierCollision();

		return collision;
	}

	public boolean _verifierCollision() {
		if (!obj.estVisible) {
			return false;
		}

		// bs.setCenter(obj.box.get);
		if (this.obj.testCollisionAvecAutreObjetMobile) {
			ObjetMobile om = obj.donnerObjetMobileCollision(be.espace);
			if (om != null && !om.testCollisionAvecAutreObjetMobile) {
				return false;
			}
			ObjetMobile zoneDetection = be.espace.getOMFor(obj.box, false);

			if (zoneDetection != null) {
				obj.evenement().collisionZoneDetection(obj, zoneDetection);
				zoneDetection.evenement().collisionObjetMobile(zoneDetection,
						obj);
				zoneDetection.zoneDetection.ajouter(obj);
			}
			boolean collisionCam = false;
			if (om == null) {
				if (be.scc != null)
					om = be.espace.getOMFor(be.scc.sphereCam, true);
				if (om == this.obj) {

					collisionCam = true;
				} else {

					om = null;
				}
			} else {
				if (om == obj || !om.testCollision) {
					om = null;
				}
			}

			if (om != null) {

				if (!collisionCam) {

					// this.annuler();
					if (om.emetteur != obj && obj.emetteur != om) {

						if (obj.testCollisionAvecAutreObjetMobile) {

							this.annuler();
							om.evenement().collisionObjetMobile(om, obj);
							obj.evenement().collisionObjetMobile(obj, om);
							return true;
						} else {
							obj.testCollisionAvecAutreObjetMobile = true;
							om.evenement().collisionObjetMobile(om, obj);
							obj.evenement().collisionObjetMobile(obj, om);

							return false;
						}
					} else {
						return false;
					}

				} else {
					this.annuler();
					om.evenement().collisionCamera(om, null);

				}

				return true;
			}
		}
		boolean collisionDecor = obj
				.verifierCollision(be.decor.gestionCollision);

		if (collisionDecor && obj.testCollisionAvecDecor) {
			this.annuler();
			obj.evenement().collisionDecor(obj);
			return true;
		}

		return false;

	}

	public boolean estOrientation() {
		return false;
	}

}
