package dadou.mutator;

import com.jme.math.Vector3f;

import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class RotationAvecLimite extends Mutator {

	public Vector3f axe = new Vector3f();
	public Vector3f origin = new Vector3f();

	public float limite;

	public RotationAvecLimite(BrickEditor be, ObjetMobilePourModelInstance om, Vector3f axe, float limite) {
		this.be = be;
		if (axe != null) {
			this.axe.set(axe);
		}

		this.obj = om;
		this.limite = limite;

	}

	public RotationAvecLimite(BrickEditor be, ObjetMobilePourModelInstance om, Vector3f axe, float limite,
			Vector3f origin) {
		this.be = be;
		if (axe != null) {
			this.axe.set(axe);
		}

		this.obj = om;
		this.limite = limite;
		this.origin = origin;

	}

	public boolean animer() {
		this.sauvegarder();
		float speed = obj.speedRotate;
		if (limite < obj.speedRotate) {
			speed = limite;
			limite = 0.0f;
		} else {
			limite -= obj.speedRotate;
		}
		if (origin == null) {

			obj.rotateCenter(axe, speed);
		} else {

			obj.rotate(origin, axe, speed);
		}
		this.verifierCollision();
		return limite > 0.0f;
	}

}
