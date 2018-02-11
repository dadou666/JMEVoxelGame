package dadou;

import java.util.HashMap;
import java.util.Map;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class DecompositionModelClasseCtx {
	public DecompositionModelClasse dmc;
	public float distance = 0.0f;
	public boolean appliquerTransparence = true;
	public Map<String, Objet3D> map;

	public void initialiser(float echelle, Vector3f pos, Quaternion q) {
		if (map != null) {
			if (map.isEmpty()) {
				dmc.initialiser(0.0f, echelle, pos, q);
				for (int i = 0; i < dmc.elements.size(); i++) {
					String nom = dmc.noms.get(i);
					map.put(nom, dmc.elements.get(i));
				}
			}
			return;
		}
		if (appliquerTransparence) {
			dmc.initialiser(distance, echelle, pos, q);
		} else {
			dmc.initialiser(0.0f, echelle, pos, q);
		}
	}

	public void dessiner(Camera cam) {
		dmc.dessiner(cam);

	}
}
