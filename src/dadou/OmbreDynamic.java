package dadou;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class OmbreDynamic extends EtatOmbre {
	public Vector3f position;
	public float theta;
	public float vitesse;
	public float phi;
	public float distance;
	public void modifierOmbre(BrickEditor be) {
		if (be.decor == null) {
			return;
		}
		if (be.decor.DecorDeBriqueData.shadowData == null) {
			Game.etatOmbre = EtatOmbre.OmbreInactive;
			return;
		}
		be.decor.DecorDeBriqueData.shadowData.init(position, distance, phi, theta);
		phi+=vitesse;
		
	}
	
}
