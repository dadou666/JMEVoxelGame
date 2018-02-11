package dadou.mutator;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.Log;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class Orientation extends Mutator {

	public Quaternion debut = new Quaternion();
	public Quaternion fin = new Quaternion();
	public Vector3f dest = new Vector3f();

	public static Quaternion tmpQ1 = new Quaternion();
	public static Quaternion tmpQ2 = new Quaternion();
	public static Vector3f tmpV = new Vector3f();
	public Vector3f dir = new Vector3f();
	public float t = 0.0f;
	public float speed;
	public boolean deplacer;

	public static void calculerQuaternionPolaireDirZ(Vector3f dir, Quaternion result) {
		float N = (float) Math.sqrt(dir.x * dir.x + dir.z * dir.z);
		if (N > 0.001f) {

			float theta = (float) Math.acos(dir.x / N);
			tmpV.set(Vector3f.UNIT_Y);
			if (dir.z < 0) {

				// System.out.println("-calculerQuaternionPolaire");
				tmpQ1.fromAngleNormalAxis(theta, tmpV);
			} else {
				tmpV.multLocal(-1);
				tmpQ1.fromAngleNormalAxis(theta, tmpV);
				// System.out.println("+calculerQuaternionPolaire");

			}
			tmpV.set(Vector3f.UNIT_Z);
			tmpQ1.multLocal(tmpV);
			float phi = (float) Math.acos(N);
			if (Float.isNaN(phi)) {
				result.set(tmpQ1);
				result.normalize();
			} else {
				if (dir.y < 0) {
					tmpV.multLocal(-1);
				}
				tmpQ2.fromAngleAxis(phi, tmpV);
				tmpQ2.mult(tmpQ1, result);
				result.normalize();
			}
			// result.set(tmpQ);

		} else {
			float phi = (float) Math.acos(N);
			if (dir.y < 0) {
				phi = -phi;
			}
			result.fromAngleAxis(phi, Vector3f.UNIT_Z);

		}

	}

	public Orientation(BrickEditor be, ObjetMobilePourModelInstance om, Vector3f dest, boolean deplacer, boolean axeZ) {
		this.obj = om;

		this.be = be;
		this.dir.set(dest);
		this.dest.set(dest);
		this.dir.subtractLocal(om.box.getCenter());
		this.dir.normalizeLocal();
		debut.set(obj.getRotationLocal());
		calculerQuaternionPolaireDirZ(dir, fin);

		speed = om.speedRotate;
		this.deplacer = deplacer;

	}
	public Orientation(BrickEditor be, ObjetMobilePourModelInstance om, Vector3f axis, float angle) {
		this.obj = om;

		this.be = be;
		this.dir.set(dest);
		this.dest.set(dest);
		this.dir.subtractLocal(om.box.getCenter());
		this.dir.normalizeLocal();
		debut.set(obj.getRotationLocal());
		fin.fromAngleAxis(angle, axis);

		speed = om.speedRotate;
		this.deplacer = false;

	}

	public void corrigerPosition() {

		this.obj.updateBoxOrientation();
		obj.positionToZero();
		obj.translationMutator(obj.box.getCenter());
		obj.updateTranslationFromBox();

	}

	public boolean estOrientation() {
		return !deplacer;
	}

	public boolean animer() {
		this.sauvegarder();
		boolean continuer = true;

		if (t + speed >= 1.0f) {
			t = 1.0f;
			continuer = false;
		} else {
			t += speed;
		}

		obj.slerp(debut, fin, t);
		this.corrigerPosition();
		obj.detachFromOctree();
		if (this.verifierCollision()) {
			return false;
		}
		obj.updateOctree();
		if (obj.octreeLeaf == null) {
			Log.print("axeX " + obj.box.xAxis.length());
			Log.print("axeY " + obj.box.yAxis.length());
			Log.print("axeZ " + obj.box.zAxis.length());
			Log.print("axeX*axeY " + obj.box.xAxis.dot(obj.box.yAxis));
			Log.print("axeY*axeZ " + obj.box.yAxis.dot(obj.box.zAxis));
			Log.print("axeZ*axeX " + obj.box.zAxis.dot(obj.box.xAxis));
			Log.print("debug");
		}

		if (!continuer) {
			if (deplacer) {
				Mouvement mvt = new Mouvement(be, obj, dest);

				this.controler.initMutator(obj, mvt);
				return false;
			}
			obj.evenement().finOrientation(obj, dest);
		}

		return continuer;
	}
}
