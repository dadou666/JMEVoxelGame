package dadou;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;
import dadou.tools.graphics.Info;

public class ExplorerAuSolCameraControllerBis extends ExplorerCameraControllerBis {
	public boolean calculerHauteur = true;

	static public Vector3f tmpVect = new Vector3f();

	public Vector3f direction = new Vector3f(0, 0, 1);

	BrickEditor editor;
	public Kube cube = new Kube();
	public boolean auSol = false;

	public boolean auSol() {
		if (Grappin.enCours()) {
			return false;
		}
		return auSol;
	}

	public OrientedBoundingBoxWithVBO boxCam = new OrientedBoundingBoxWithVBO();
	public OrientedBoundingBoxWithVBO boxCamTest = new OrientedBoundingBoxWithVBO();
	public BoundingSphere sphereCamTest = new BoundingSphere();
	public BoundingSphere sphereCam = new BoundingSphere();
	public Vector3f tmpA = new Vector3f();
	public Vector3f tmpB = new Vector3f();

	public void calculerBox(Camera cam, VitesseCamera vc) {

		screenPos.set(screenWidth / 2, 0);
		float z = 0.5f;

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		screenPos.set(0, 0);
		tmpB.set(cam.getWorldCoordinates(screenPos, 0));
		tmpA.subtractLocal(tmpB);
		float extentX = tmpA.length();

		screenPos.set(0, screenHeight / 2);
		// float z = 0.1f;

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		screenPos.set(0, 0);
		tmpB.set(cam.getWorldCoordinates(screenPos, 0));
		tmpA.subtractLocal(tmpB);
		float extentY = tmpA.length();

		screenPos.set(screenWidth / 2, screenHeight / 2);
		// float z = 0.1f;

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));

		tmpB.set(cam.getLocation());
		tmpA.subtractLocal(tmpB);
		tmpA.multLocal(0.5f);
		tmpB.addLocal(tmpA);
		float deltat = vc.vitesseDeplacement / 2.0f;
		float extentZ = tmpA.length() + z + FastMath.abs(deltat);

		tmpA.set(cam.getDirection());
		tmpA.multLocal(deltat);
		tmpB.subtractLocal(tmpA);

		boxCam.getCenter().set(tmpB);

		boxCam.getExtent().set(extentX, extentY, extentZ);
		boxCam.getXAxis().set(cam.getLeft());
		boxCam.getYAxis().set(cam.getUp());
		boxCam.getZAxis().set(cam.getDirection());

		screenPos.set(screenWidth, screenHeight);

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		tmpA.subtractLocal(cam.getLocation());
		float radius = tmpA.length();
		sphereCam.getCenter().set(cam.getLocation());
		sphereCam.setRadius(radius);

	}

	public void calculerBoxTest(float distance, float dim) {
		screenPos.set(screenWidth / 2, screenHeight / 2);

		tmpA.set(cam.getWorldCoordinates(screenPos, 0));
		Vector3f u = cam.getDirection();
		float d2 = distance / 2.0f;

		tmpA.addLocal(u.x * d2, u.y * d2, u.z * d2);
		boxCamTest.getCenter().set(tmpA);

		boxCamTest.getExtent().set(dim, dim, d2);
		boxCamTest.getXAxis().set(cam.getLeft());
		boxCamTest.getYAxis().set(cam.getUp());
		boxCamTest.getZAxis().set(cam.getDirection());

	}

	public ObjetMobile donnerObjetMobilePourHauteur(BoundingSphere bs, float h) {
		bs.getCenter().set(translation);
		bs.getCenter().y -= h;
		ObjetMobile om = this.editor.espace.getOMFor(bs, true);
		if (om != null) {
			if (om.annulerCollisionCamera) {
				return null;
			}
		}

		return om;

	}

	public boolean collisionDecorPourHauteur(BoundingSphere bs, float h) {
		bs.getCenter().set(translation);
		bs.getCenter().y -= h;
		boolean r = this.editor.decor.gestionCollision.verifierCollisionSphere(bs.getCenter(), bs.getRadius());

		return r;
	}

	public ExplorerAuSolCameraControllerBis(BrickEditor editor, Camera cam) {
		super(cam);
		this.editor = editor;
	}

	public Vector3f dep = new Vector3f();

	public void deplacerPositionCamera(Vector3f depCopy) {
		translation.addLocal(depCopy);
		cam.setLocation(translation);

		cam.update();
		cam.apply();

	}

	public void deplacerPositionCamera(float dx, float dy, float dz) {
		translation.addLocal(dx, dy, dz);
		cam.setLocation(translation);

		cam.update();
		cam.apply();

	}

	public void initCameraPosition(float distance, float theta, float phi, Vector3f pos, CameraPosition cp) {

		cp.rotationX.fromAngleNormalAxis(phi, Vector3f.UNIT_X);

		cp.rotationY.fromAngleNormalAxis(theta, Vector3f.UNIT_Y);

		tmpVect.set(0, 0, 1);
		tmp.set(cp.rotationY);
		tmp.multLocal(cp.rotationX);

		tmp.multLocal(tmpVect);

		tmpVect.multLocal(-distance);
		cp.translation.set(pos);
		cp.translation.addLocal(tmpVect);
	//	Log.print(" translation=" +cp.translation+"/"+ cam.getLocation());

	}

	public void deplacerCamera(VitesseCamera vc) {

		if ((!auSol())) {

			super.deplacerCamera(vc);
			return;

		}

		tmp.fromAngleNormalAxis(vc.rotationInclinaison, axeX);
		rotationX.multLocal(tmp);
		tmp.fromAngleNormalAxis(vc.rotationAxeGravite, axeY);
		rotationY.multLocal(tmp);
		/*
		 * translation.addLocal(left.x * vc.vitesseLeft + up.x * vc.vitesseUp,
		 * left.y * vc.vitesseLeft + up.y * vc.vitesseUp, left.z vc.vitesseLeft
		 * + up.z * vc.vitesseUp); Vector3f dir = cam.getDirection();
		 */

		// translation.addLocal(dir.x * vc.vitesseDeplacement, dir.y *
		// vc.vitesseDeplacement, dir.z * vc.vitesseDeplacement);
		tmpVect.set(axeX);
		rotationY.multLocal(tmpVect);

		translation.addLocal(tmpVect.x * vc.vitesseLeft * vc.vitesseDeplacementX, 0,
				tmpVect.z * vc.vitesseLeft * vc.vitesseDeplacementZ);

		tmpVect.set(direction);
		rotationY.multLocal(tmpVect);

		translation.addLocal(tmpVect.x * vc.vitesseDeplacement * vc.vitesseDeplacementX, 0,
				tmpVect.z * vc.vitesseDeplacement * vc.vitesseDeplacementZ);
		// translation.addLocal(dep);
		// dep.set(0, 0, 0);

		/*
		 * try {
		 * 
		 * this.calculerHauteur(); } catch (CouleurErreur e) {
		 * 
		 * e.printStackTrace(); }
		 */
		cam.setLocation(translation);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);

		cam.update();
		cam.apply();
	}

	public boolean tremblement = false;

	Random r = new Random();

	public void updateCamera() {
		cam.setLocation(translation);
		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		if (tremblement) {
			this.tmpV.set(translation);
			float fUp = r.nextFloat();
			float fLeft = r.nextFloat();
			this.tmpA.set(cam.getUp());
			this.tmpA.multLocal(fUp);

			this.tmpB.set(cam.getLeft());
			this.tmpB.multLocal(fLeft);
			cam.getLocation().addLocal(this.tmpA);
			cam.getLocation().addLocal(this.tmpB);

		}

		cam.update();
		cam.apply();
		if (tremblement) {
			this.translation.set(tmpV);
		}

	}

	public boolean updateDebugBox = false;
	public Vector3f postionPourHauteur = new Vector3f();
	Vector2f screenPos = new Vector2f();

	public void demiTour() {
		if (!auSol()) {
			super.demiTour();
			return;
		}
		tmp.fromAngleNormalAxis((float) Math.PI, axeY);
		rotationY.multLocal(tmp);

		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.update();
		cam.apply();

	}

	public float zone = 0.2f;

	public void modifieVitesseCamWithMouse(VitesseCamera vc, Game g, boolean rotate) {

		vc.reset();

		// Mouse.updateCursor();

		float x = MouseInput.get().getXAbsolute();
		float y = MouseInput.get().getYAbsolute();

		// editor.info.infoText = "(" + x + "," + y + ")";
		if (!auSol()) {
			super.modifieVitesseCamWithMouse(vc, g, rotate);
			return;

		}
		boolean move = false;
		;

		float width = screenWidth / 2;
		float height = screenHeight / 2;
		float dx = (x - width) / width;
		float dy = (y - height) / height;
		if (rotate) {
			if (Math.abs(dy) > zone) {
				vc.rotationInclinaison = -dy / 17;

			}

			if (Math.abs(dx) > zone) {
				vc.rotationAxeGravite = -dx / 15;

			}
		} else {

			vc.vitesseLeft = -dx / 2;
			vc.vitesseUp = dy / 2;
			if (Math.abs(dy) < zone) {
				vc.vitesseUp = 0.0f;
			} else {
				move = true;
			}
			if (Math.abs(dx) < zone) {
				vc.vitesseLeft = 0.0f;
			} else {
				move = true;
			}

		}

	}

	public int hauteur;

	public CameraPosition donnerCameraPosition() {
		CameraPosition cp = new CameraPosition();
		cp.rotationX = new Quaternion(this.rotationX);
		cp.rotationY = new Quaternion(this.rotationY);
		cp.translation = this.translation.clone();
		return cp;

	}

}
