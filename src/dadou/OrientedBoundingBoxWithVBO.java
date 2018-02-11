package dadou;

import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;

public class OrientedBoundingBoxWithVBO extends OrientedBoundingBox {



	public VBOTexture2D debugBox;
	public Objet3D objDebugBox;

	

	public void createDebugBox(Shader shader, Vector3f e) {
		



		int i = 0;
		if (e == null) {
			e = this.getExtent();
		}
		debugBox = VBOTexture2D.createBox(shader, e);
		debugBox.drawLine = true;
		// debugBox.widthLine =0.
		objDebugBox = new Objet3D();
		objDebugBox.brique = debugBox;

	}
}
