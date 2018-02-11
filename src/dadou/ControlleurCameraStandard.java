package dadou;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.jme.input.MouseInput;
import com.jme.math.FastMath;

import dadou.tools.BrickEditor;

public class ControlleurCameraStandard extends ControlleurCamera {
	boolean tireActif = false;
	public static Button bTire = new Button(1);
	public boolean figerRotationEtTranslationCamera = false;

	public int oldX;
	public int oldY;
	public static Key saut = new Key(Keyboard.KEY_SPACE);
	public CameraPosition cp;
	public static Button selectButton = new Button(0);

	public void figerRotationEtTranslationCamera(boolean b, BrickEditor be) {
		figerRotationEtTranslationCamera = b;
		oldX = MouseInput.get().getXAbsolute();
		oldY = MouseInput.get().getYAbsolute();
		if (b) {
			cp = be.scc.creerCameraPosition();
		} else {
			cp = null;
		}

	}

	public void tireActif(BrickEditor be, boolean b) {
		tireActif = b;
	}

	public static Key avancer = new Key(Keyboard.KEY_Z);
	public static Key reculer = new Key(Keyboard.KEY_S);
	public static Key gauche = new Key(Keyboard.KEY_Q);
	public static Key droite = new Key(Keyboard.KEY_D);

	public void modifierCameraAvecSouris(BrickEditor be) {
		be.scc.zone = 0.1f;
		boolean space = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
		if (!space) {
			this.modifieVitesseCamWithMouse(be);
		} else {
			be.scc.modifieVitesseCamWithMouse(be.vc, be.game, !space);
		}
	}

	float mouseSensitivity = 0.001f;
	public void chargerCameraPosition(CameraPosition cp) {
		this.cp=cp;
		
	}
	public void modifieVitesseCamWithMouse(BrickEditor be) {
		VitesseCamera vc = be.vc;

		vc.reset();

	
		MouseInput.get().getXAbsolute();
		MouseInput.get().getYAbsolute();
		// MouseInput.get().se
		
		float dx = -Mouse.getDX();
	
		float dy = -Mouse.getDY();
		if (be.verouillerRotationEtTranslationCamera) {
			this.figerRotationEtTranslationCamera = true;
			return;
		}

		if (this.figerRotationEtTranslationCamera) {
			this.figerRotationEtTranslationCamera = false;
			MouseInput.get().setCursorPosition(oldX, oldY);

			return;
		}
		vc.rotationInclinaison = mouseSensitivity * dy;
		;
		vc.rotationAxeGravite = mouseSensitivity * dx;
		if (cp != null) {
			vc.rotationInclinaison = 0;
			;
			vc.rotationAxeGravite = 0;
			be.scc.charger(cp);
			cp = null;
		}

	}

	public boolean faireSaut(BrickEditor be) {
		boolean r = saut.isDown();
		if (r) {
			Grappin.annuler();
		}
		return r;

	}

	public boolean tireActif() {
		return tireActif;
	}

	public boolean faireTire(BrickEditor be) {
		if (!tireActif) {
			return false;
		}

		boolean r = bTire.isPressed();

		return r;
	}
	public boolean continuerTire(BrickEditor be) {
		boolean down= bTire.isDown();
		return down;
		
	}
	public void gererAvancerReculer(BrickEditor be) {
		if (this.figerTranslationCamera) {
			return;
		}
		if (Grappin.enCours()) {
			return;
		}
		be.scc.avancer = avancer;
		be.scc.reculer = reculer;
		be.scc.gererAvancerReculer(be.vc);
		if (gauche.isDown()) {
			be.vc.vitesseLeft = 0.5f;
		}
		if (droite.isDown()) {
			be.vc.vitesseLeft = -0.5f;
		}
	}

	public boolean cibleAuCentre(BrickEditor be) {
		return !be.verouillerRotationEtTranslationCamera;
	}

	public Button selectButton() {
		return selectButton;
	}
}
