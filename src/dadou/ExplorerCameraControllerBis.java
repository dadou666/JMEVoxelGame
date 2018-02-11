package dadou;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.jme.input.MouseInput;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;

public class ExplorerCameraControllerBis {
	public Quaternion rotationX = new Quaternion();
	public Quaternion rotationY = new Quaternion();
	public Quaternion tmp = new Quaternion();
	public Vector3f translation = new Vector3f();

	public Quaternion saveRotationX = new Quaternion();
	public Quaternion saveRotationY = new Quaternion();

	public Vector3f saveTranslation = new Vector3f();

	public Camera cam;
	static public Vector3f axeY = new Vector3f(0, 1, 0);
	static public Vector3f axeX = new Vector3f(1, 0, 0);
	public int screenWidth;
	public int screenHeight;
	public float speed = 0.1f;
	public Vector3f tmpV = new Vector3f();
	public Key avancer = new Key(Keyboard.KEY_UP);
	public Key reculer = new Key(Keyboard.KEY_DOWN);
	public boolean sautSansDeplacement = false;

	public ExplorerCameraControllerBis(Camera cam) {

		this.cam = cam;

	}

	public boolean charger(CameraPosition cameraPosition) {
		if (cameraPosition == null) {
			return false;
		}
		translation.set(cameraPosition.translation);
		if (cameraPosition.rotationX != null) {
			rotationX.set(cameraPosition.rotationX);
			rotationY.set(cameraPosition.rotationY);
		}

		cam.setLocation(translation);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.update();
		cam.apply();
		return true;

	}

	public CameraPosition creerCameraPosition() {
		CameraPosition cameraPosition = new CameraPosition();
		cameraPosition.rotationX = new Quaternion(rotationX);
		cameraPosition.rotationY = new Quaternion(rotationY);
		cameraPosition.translation = new Vector3f(translation);
		return cameraPosition;

	}

	public void save() {
		saveRotationX.set(rotationX);
		saveRotationY.set(rotationY);
		saveTranslation.set(translation);
	}

	public void restore() {
		Grappin.annuler();
		rotationX.set(saveRotationX);
		rotationY.set(saveRotationY);
		translation.set(saveTranslation);
		cam.setLocation(translation);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.update();
		cam.apply();
	}

	public void deplacerCamera(VitesseCamera vc) {
		Vector3f left = cam.getLeft();
		Vector3f up = cam.getUp();
		tmp.fromAngleNormalAxis(vc.rotationInclinaison, axeX);
		rotationX.multLocal(tmp);
		tmp.fromAngleNormalAxis(vc.rotationAxeGravite, axeY);
		rotationY.multLocal(tmp);
		if (Grappin.enCours()) {
			Grappin.gerer(this);
		} else {
			translation.addLocal(left.x * vc.vitesseLeft + up.x * vc.vitesseUp,
					left.y * vc.vitesseLeft + up.y * vc.vitesseUp, left.z * vc.vitesseLeft + up.z * vc.vitesseUp);
			Vector3f dir = cam.getDirection();
			translation.addLocal(dir.x * vc.vitesseDeplacement, dir.y * vc.vitesseDeplacement,
					dir.z * vc.vitesseDeplacement);
		}
		cam.setLocation(translation);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.update();
		cam.apply();
	}

	public void demiTour() {

		tmp.fromAngleNormalAxis((float) Math.PI, axeY);
		rotationY.multLocal(tmp);
		float a = rotationX.toAngleAxis(tmpV);
		tmpV.multLocal(-1);
		a = a * 2;
		tmp.fromAngleNormalAxis(a, tmpV);
		rotationX.multLocal(tmp);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.update();
		cam.apply();

	}

	public void gererAvancerReculer(VitesseCamera vc) {
	
		if (avancer != null) {
			if (avancer.isDown()) {
				vc.vitesseDeplacement = speed;
			}
		}
		if (reculer != null) {
			if (reculer.isDown()) {
				vc.vitesseDeplacement = -speed;
			}
		}
	}

	public void modifieVitesseCamWithMouse(VitesseCamera vc, Game g, boolean rotate) {

		float x = MouseInput.get().getXAbsolute();
		float y = MouseInput.get().getYAbsolute();

		float width = screenWidth / 2;
		float height = screenHeight / 2;

		float dx = (x - width) / width;
		float dy = (y - height) / height;
		vc.rotationInclinaison = 0;
		vc.rotationAxeGravite = 0;
		vc.vitesseLeft = 0;
		vc.vitesseUp = 0;
		vc.vitesseDeplacement = 0;
		if (rotate) {
			if (Math.abs(dy) > 0.2) {
				vc.rotationInclinaison = -dy / 25;

			}

			if (Math.abs(dx) > 0.2) {
				vc.rotationAxeGravite = -dx / 25;

			}
		} else {

			vc.vitesseLeft = -dx / 2;
			vc.vitesseUp = dy / 2;

		}
		

	}

}
