package dadou;

import java.io.Serializable;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class Lumiere implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5719018375974470214L;
	public Vector3f pos = new Vector3f();
	public float rayon;
	public transient Object mark = null;
	public transient Object markSource=null;
	public transient Objet3D obj;
	public transient BoundingSphere bs;
	public transient Objet3D source;

	public void creerSource() {
		float dim=0.5f;
		VBOTexture2D vbo = new VBOTexture2D(Game.shaderLumiere);
		vbo.addVertex(-dim, -dim, 0.00f);
		vbo.addVertex(-dim, dim, 0.0f);
		vbo.addVertex(dim, dim, 0.0f);
		vbo.addVertex(dim, -dim, 0.0f);

		vbo.addCoordTexture2D(0, 0);
		vbo.addCoordTexture2D(0, 1);
		vbo.addCoordTexture2D(1, 1);
		vbo.addCoordTexture2D(1, 0);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		source = new Objet3D();
		source.brique = vbo;
		source.translation(pos);
	}


	public BoundingSphere boundingSphere(boolean estSource) {
		if (bs == null) {
			bs = new BoundingSphere();

		}
		if (estSource) {
			bs.radius = 0.5f;
		} else {
			bs.radius = rayon;
		}
		bs.getCenter().set(pos);
		return bs;

	}

	public void reset() {
		if (obj != null) {
		
			obj = null;
		}
		if (source != null) {
		
			source = null;
		}
	}

	public void dessinerLumiere(Camera cam) {
		if (source == null) {
			this.creerSource();
		}
		source.positionToZero();
	//	source.translation(pos);

		Game.shaderLumiere.use();
		Game.shaderLumiere.glUniformfARB("size", rayon);
		Game.shaderLumiere.glUniformfARB("position", pos.x, pos.y,pos.z);
		Game.shaderLumiere.glUniformmat4ARB("modelView", BrickEditor.modelViewMatrix);
		source.dessiner(cam);

	}

	transient public Vector3f color = new Vector3f();

	public Vector3f color() {
		if (color == null) {
			color = new Vector3f();
		}
		return color;
	}

	public void dessiner(Camera cam) {
		if (obj == null) {
			bs = new BoundingSphere();
			bs.radius = rayon;

			SphereGenerateur sg = new SphereGenerateur(rayon, 2);

			VBOTexture2D vbo = sg.creerVBOTexture2D(Game.shaderBase);
			vbo.drawLine = true;
			vbo.useVBO = false;
			vbo.action = () -> {

				Game.shaderBase.glUniformfARB("color", color.x, color.y, color.z, 0.0f);

			};
			obj = new Objet3D();
			obj.brique = vbo;

		}
		obj.positionToZero();
		obj.translation(pos);
		bs.getCenter().set(pos);
		obj.dessiner(cam);

	}

}
