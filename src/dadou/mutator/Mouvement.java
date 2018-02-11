package dadou.mutator;

import java.util.Vector;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class Mouvement extends Mutator {

	public Vector3f dir = new Vector3f();

	public Vector3f dirTotal = new Vector3f();
	public float distance;
	public float distanceMax;
	public Vector3f destination = new Vector3f();
	public Vector3f tmp = new Vector3f();
	public Vector3f axe = new Vector3f();

	public float speed;
	public float extent;
	public float speedTest;
	public float speedReste;

	public Mouvement(BrickEditor be, ObjetMobilePourModelInstance obj,
			Vector3f destination) {
		this.be = be;
		this.obj = obj;
		
		extent = obj.box.getExtent().length();
		// rot1.fromAngleAxis(speedRotate, Vector3f.UNIT_X);
		// rot2.fromAngleAxis(speedRotate, Vector3f.UNIT_Z);
		this.init(destination, obj.speedTranslate);
	}

	public Mouvement(BrickEditor be, ObjetMobilePourModelInstance obj,
			Vector3f direction, float distance) {
		this.be = be;
		this.obj = obj;

		extent = obj.box.getExtent().length();
		// rot1.fromAngleAxis(speedRotate, Vector3f.UNIT_X);
		// rot2.fromAngleAxis(speedRotate, Vector3f.UNIT_Z);
		this.destination.set(obj.box.getCenter());
		this.destination.addLocal(distance * direction.x, distance
				* direction.y, distance * direction.z);

		this.init(destination, obj.speedTranslate);
	}


	public void init(Vector3f destination, float speed) {

		this.destination.set(destination);

		dir.set(this.destination);
		dir.subtractLocal(obj.box.getCenter());

		distanceMax = dir.length();
		distance = 0.0f;
		this.speed = speed;
		dir.normalizeLocal();
		this.dirTotal.set(dir);

		if (speed >= 2 * extent) {
			speedTest = extent;
			speedReste = speed - (float) ((int) (speed / (2 * extent))) * 2
					* extent;
			speedReste = speedReste / extent;
		} else {

			speedTest = speed;
			speedReste = 0.0f;
		}
		dir.multLocal(speedTest);
		//obj.dep = new Vector3f();
		dirTotal.multLocal(speed);

	}

	public void corrigerPosition() {

		this.obj.updateBoxOrientation();
		obj.positionToZero();
		obj.translationMutator(obj.box.getCenter());
		obj.updateTranslationFromBox();

	}

	public boolean animer() {
		this.sauvegarder();
		boolean estFini = false;
		float t = 1.0f;
		if (this.distance + speed >= this.distanceMax) {
			estFini = true;
			this.dir.multLocal((this.distanceMax - this.distance) / speed);
			this.distance = this.distanceMax;
		} else {
			this.distance += speed;
			t = distance / distanceMax;
		}

		obj.detachFromOctree();
		float u = speedTest;
		obj.dep.set(Vector3f.ZERO);
		obj.dep.subtractLocal(obj.getBox().getCenter());
		while (u <= speed) {
			this.sauvegarder();
			obj.translationMutator(dir);
			obj.deplacerBoundingVolume(dir);
			
		
			u += speedTest;
			if (this.verifierCollision()) {
				obj.dep.addLocal(obj.getBox().getCenter());
				return false;
			}
		}
		if (speedReste > 0.0f) {
			this.sauvegarder();
			//System.out.println("nbTest="+nbTest);
			float rx= dir.x*speedReste;
			float ry=dir.y*speedReste;
			float rz=dir.z*speedReste;
			obj.translation(rx,ry,rz);
			obj.box.getCenter().addLocal(rx,ry,rz);
		
			if (this.verifierCollision()) {
				obj.dep.addLocal(obj.getBox().getCenter());
				return false;
			}
			
		}
		obj.dep.addLocal(obj.getBox().getCenter());

		if (estFini) {
			//obj.dep.set(Vector3f.ZERO);
			obj.evenement().finDeplacement(obj);
		}
		obj.updateOctree();
		return !estFini;

	}

}
