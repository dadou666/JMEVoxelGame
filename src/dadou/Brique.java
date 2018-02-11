package dadou;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;

public class Brique implements Element3D {

	public VoxelTexture3D tex;
	public float echelle = 1.0f;
	public float couleurFactor = 0.0f;
	public boolean initShader = true;
	public FloatBuffer buffer;
	public FloatBuffer transformationBuffer;
	public Matrix4f mat = new Matrix4f();
	public VBOBriqueTexture3D vbo;
	public VoxelShaderParam vs;

	public VoxelShader vs() {
		return vbo.voxelShader();
	}

	public void dessiner(Shader shader, VBOTexture3D P) {

		VBOTexture3D vbo = P;
		vbo.shader = shader;

		vbo.bind();

		initShader();

		vbo.dessiner(shader);

		vbo.unBindVAO();

	}

	public void updateShaderUniform(Shader shader) {

	}

	public void initShader() {
		if (Game.rm == Game.RenderMode.Normal) {
			buffer = null;
		}
		vs.initUniform(this);
	}

	public void dessiner(Camera cam) {

		this.bindTexture();
		if (vs.habillage != null) {
			vs.habillage.creerVoxelTexture3D().bindTexture(GL13.GL_TEXTURE1);
		}
		if (Game.rm != Game.RenderMode.Depth && FBOShadowMap.shadowMap != null) {

			// OpenGLTools.bindTexture2D(GL13.GL_TEXTURE3,
			// FBOShadowMap.shadowMap.shadowTextureID);

		}
		OpenGLTools.exitOnGLError("bindTexture");
		if (vbo.afficherX && vbo.X.nbTriangle > 0) {
			dessiner(this.vbo.voxelShader().shaderVoxelX, vbo.X);
		}
		if (vbo.afficherY && vbo.Y.nbTriangle > 0) {
			dessiner(this.vbo.voxelShader().shaderVoxelY, vbo.Y);
		}
		if (vbo.afficherZ && vbo.Z.nbTriangle > 0) {
			dessiner(this.vbo.voxelShader().shaderVoxelZ, vbo.Z);
		}

	}

	public void bindTexture() {
		tex.bindTexture();
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		if (vbo == null) {
			return;
		}
		vbo.delete();
		vbo = null;

	}

	@Override
	public void initFromShadowMap(Objet3D obj, Camera cam) {

		if (buffer == null) {
			buffer = BufferUtils.createFloatBuffer(16);
			transformationBuffer = BufferUtils.createFloatBuffer(16);

		} else {
			if (obj.estElementDecor) {
				// return;
			}
		}
		try {
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			Vector3f translationGlobal = obj.getTranslationGlobal();
			Vector3f axeRotationGlobal = obj.getAxeRotationGlobal();
			float angle = obj.getAngle();
			GL11.glTranslatef(translationGlobal.x, translationGlobal.y,
					translationGlobal.z);
			GL11.glRotatef(angle, axeRotationGlobal.x, axeRotationGlobal.y,
					axeRotationGlobal.z);
			if (FBOShadowMap.shadowMap != null) {
				FBOShadowMap.shadowMap.loadMatrix(buffer, false);
			}
			// if (Game.subPixelShadow) {
			loadTransformationMatrix();
			// }
		} finally {
			GL11.glPopMatrix();
		}
	}

	public void loadTransformationMatrix() {

		transformationBuffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, transformationBuffer);
		transformationBuffer.rewind();
		mat.readFloatBuffer(transformationBuffer);
		mat.fillFloatBuffer(transformationBuffer);

	}

}
