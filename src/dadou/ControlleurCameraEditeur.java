package dadou;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dadou.tools.BrickEditor;

public class ControlleurCameraEditeur extends ControlleurCamera {
	
	public static Key avancer = new Key(Keyboard.KEY_UP);
	public static Key reculer = new Key(Keyboard.KEY_DOWN);
	public static Key toucheSaut = new Key(Keyboard.KEY_RSHIFT);
	public void modifierCameraAvecSouris(BrickEditor be) {
		boolean lcontrol = Mouse.isButtonDown(0);
		boolean space = Mouse.isButtonDown(1);
		be.scc.zone= 0.2f;
		if (space || lcontrol) {

			be.scc.modifieVitesseCamWithMouse(be.vc, be.game, !space);
		}
	}
	public boolean faireSaut(BrickEditor be) {
		return toucheSaut.isDown();
		
	}
	public boolean faireTire(BrickEditor be) {
		return Mouse.isButtonDown(1);
		
	}
	public void gererAvancerReculer(BrickEditor be) {
		be.scc.avancer = avancer;
		be.scc.reculer = reculer;
		be.scc.gererAvancerReculer(be.vc);
	}

}
