package dadou.tools.canon;

import org.lwjgl.input.Mouse;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.EtatBrickEditor;

public class EtatCanon extends EtatBrickEditor {
	public Canon canon;
	public Vector2f screenPos = new Vector2f();
	public Vector3f selPos = new Vector3f();

	public void gerer() throws CouleurErreur {
		int wheel = Mouse.getDWheel();
		if (wheel > 0) {
			canon.tailleObus++;
		
		}
		if (wheel < 0) {
			canon.tailleObus =  canon.tailleObus - 1;
			
		}
		if (BrickEditor.b.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			screenPos.set(x, y);
			Camera cam = BrickEditor.game.getCamera();

			selPos.set(cam.getWorldCoordinates(screenPos, 1));
			selPos.subtractLocal(cam.getWorldCoordinates(screenPos, 0));
			selPos.normalizeLocal();

			canon.tirer(null,BrickEditor.game.getCamera().getLocation(), selPos);
		}
		BrickEditor.gererCamera();

	}

}
