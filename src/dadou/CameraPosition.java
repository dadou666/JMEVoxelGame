package dadou;

import java.io.Serializable;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class CameraPosition implements Serializable{
	/**
	 * 
	 */
	public String groupe;
	private static final long serialVersionUID = -2928845354740525086L;
	public Quaternion rotationX= new Quaternion();
	public Quaternion rotationY= new Quaternion();
	public Vector3f translation=new Vector3f();
	public transient Objet3D obj;
	public transient Shader shaderBox;
	public void  creerObjet3D(BrickEditor be) {
		if (obj == null) {
			VBOTexture2D vbo = VBOTexture2D.createBox(be.shaderBox, be.scc.boxCam.getExtent());
			 obj = new Objet3D();
			 shaderBox = be.shaderBox;
			obj.brique = vbo;
			vbo.drawLine = true;
			if (rotationY != null) {
			be.scc.tmp.set(rotationY);
			be.scc.tmp.multLocal(rotationX);
			}
			obj.setRotation(be.scc.tmp);
			obj.positionToZero();
			obj.translation(translation);
		
		}
		
		
	}
	public void dessiner(Camera cam) {
		if (obj != null) {
		
			shaderBox.use();
			
			// selection.shader.glUniformfARB("size", 1);
			shaderBox.glUniformfARB("color", 1.0f, 0.5f, 0.5f, 0.0f);
			shaderBox.glUniformfARB("echelle", 1.0f);
			obj.dessiner(cam);
		}
		
	}
	
}
