package dadou;

import com.jme.math.Quaternion;
import com.jme.renderer.Camera;

public interface Element3D {
	public void dessiner(Camera cam);
	public void initFromShadowMap(Objet3D obj, Camera cam);


	public void delete();;
}
