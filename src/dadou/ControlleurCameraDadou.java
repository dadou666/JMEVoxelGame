package dadou;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dadou.tools.BrickEditor;

public class ControlleurCameraDadou extends ControlleurCamera {
	public static Key keyTire = new Key(Keyboard.KEY_D);
	public static Key keySaut = new Key(Keyboard.KEY_Q);

	public static Key avancer = new Key(Keyboard.KEY_Z);
	public static Key reculer = new Key(Keyboard.KEY_S);
	public static Button selectButton = new Button(2);
	public boolean tireActif;
	public CameraPosition cp;
	public Button selectButton() {
		return selectButton;
	}
	public void chargerCameraPosition(CameraPosition cp) {
		this.cp=cp;
		
	}
	public void modifierCameraAvecSouris(BrickEditor be) {
		boolean lcontrol = Mouse.isButtonDown(0);
		boolean space = Mouse.isButtonDown(1);
		be.scc.zone = 0.2f;
		if (space || lcontrol) {

			be.scc.modifieVitesseCamWithMouse(be.vc, be.game, !space);
		}
	}

	public boolean faireSaut(BrickEditor be) {
		boolean r = keySaut.isDown();
		if (r) {
			Grappin.annuler();
		}
		return keySaut.isDown();

	}

	public boolean tireActif() {
		return tireActif;
	}

	public boolean faireTire(BrickEditor be) {
		if (!tireActif) {
			return false;
		}

		boolean r = keyTire.isPressed();
		
		return r;
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
	}

	public void tireActif(BrickEditor be, boolean b) {
		this.tireActif = b;

	}
}
