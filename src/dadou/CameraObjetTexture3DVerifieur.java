package dadou;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class CameraObjetTexture3DVerifieur  {
	public Vector3f leftUp;
	public Vector3f leftDown;
	public Vector3f rightUp;
	public Vector3f rightDown;
	
	public Vector3f leftUpTex;
	public Vector3f leftDownTex;
	public Vector3f rightUpTex;
	public Vector3f rightDownTex;
	
	public Vector2f screenPos;
	public int querie;
	IntBuffer samples;
	Shader shader;
	public FBO fbo;
	public Vector3f tmp = new Vector3f();

	public CameraObjetTexture3DVerifieur() {
		leftUp = new Vector3f();
		leftDown = new Vector3f();
		rightUp = new Vector3f();
		rightDown = new Vector3f();
		
		leftUpTex = new Vector3f();
		leftDownTex = new Vector3f();
		rightUpTex = new Vector3f();
		rightDownTex = new Vector3f();
		
		screenPos = new Vector2f();
		IntBuffer queries;
		queries = BufferUtils.createIntBuffer(1);
		samples = BufferUtils.createIntBuffer(1);
		GL15.glGenQueries(queries);
		querie = queries.get();
		shader = new Shader(Shader.class, "verif_cam_objet.frag", "verif_cam_objet.vert",null);

	}
	Quaternion rotationInverse = new Quaternion();
	Vector3f position = new Vector3f();
	
	public void divide(Vector3f pos,float dimX,float dimY,float dimZ) {
		
		pos.x = pos.x/dimX;
		pos.y = pos.y/dimY;
		pos.z = pos.z/dimZ;
		
		
	}
	public float abs(float a) {
	/*	if (a < 0) {
			return -a;
			
		}*/
		return a;
	}
	public boolean verifierCollision(Game g,Quaternion rotation,  VoxelTexture3D tex,Vector3f pos,float dim,float z) {

		int x = 0;
		int y = 0;
		screenPos.set(x, y);
		//float z = 0.1f;
		Camera cam = g.getCamera();
		tmp.set(pos);
	
		
		leftUp.set(cam.getWorldCoordinates(screenPos, z));

		screenPos.set(x + g.getWidth(), y);

		rightUp.set(cam.getWorldCoordinates(screenPos, z));

		screenPos.set(x + g.getWidth(), y + g.getHeight());

		rightDown.set(cam.getWorldCoordinates(screenPos, z));

		screenPos.set(x, y + g.getHeight());

		leftDown.set(cam.getWorldCoordinates(screenPos, z));
	

		// System.out.println(" leftUp="+leftUp+" rightUp="+rightUp+" rightDown="+rightDown+" leftDown="+leftDown);
		float dimX = tex.dimX;
		float dimY = tex.dimY;
		float dimZ = tex.dimZ;
		dimX =dimX *dim;
		dimY =dimY *dim;
		dimZ =dimZ *dim;
		tmp.addLocal(dimX/2.0f, dimY/2.0f, dimZ/2.0f);
		float h = tmp.dot(cam.getDirection())-leftUp.dot(cam.getDirection());
		if (h*h > (dimX*dimX+dimY*dimY+dimZ*dimZ)) {
		//	System.out.println(" trop loin ");
			return false;
		}
		if (fbo != null) {
			fbo.activer();
		}
		// System.out.println(" dim="+tex.dim);
		// GL15.glBeginQuery(GL33.GL_ANY_SAMPLES_PASSED, querie);
		GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, querie);
		rotationInverse.set(rotation);
		rotationInverse.inverseLocal();
		position.set(pos);
		shader.use();
		rotationInverse.multLocal(position);
		//shader.glUniformfARB("dimX", dimX*dim);
	//	shader.glUniformfARB("dimY", dimY*dim);
	//	shader.glUniformfARB("dimZ", dimZ*dim);
	//	shader.glUniformfARB("px", position.x);
	//	shader.glUniformfARB("py", position.y);
	//	shader.glUniformfARB("pz", position.z);
		
		
		shader.glUniformiARB("texture0", 0);
		
		
		OpenGLTools.exitOnGLError("erreur");

		tex.bindTexture(GL13.GL_TEXTURE0);
	
		
	
		
	
		// shader.glUniformfARB("texture", 0);
	
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		leftUpTex.set(leftUp);
		leftUpTex.subtractLocal(pos);
		
		
		
		leftDownTex.set(leftDown);
		leftDownTex.subtractLocal(pos);
		
		
		rightUpTex.set(rightUp);
		rightUpTex.subtractLocal(pos);
		
		
		rightDownTex.set(rightDown);
		rightDownTex.subtractLocal(pos);
	
		
		rotationInverse.multLocal(leftUpTex);
		rotationInverse.multLocal(rightUpTex);
		rotationInverse.multLocal(rightDownTex);
		rotationInverse.multLocal(leftDownTex);
		divide(rightDownTex,dimX,dimY,dimZ);
		divide(rightUpTex,dimX,dimY,dimZ);
		divide(leftDownTex,dimX,dimY,dimZ);
		divide(leftUpTex,dimX,dimY,dimZ);
		
		/*System.out.println(" leftUpTex="+leftUpTex);
		System.out.println(" rightUpTex="+rightUpTex);
		System.out.println(" rightDownTex="+rightDownTex);
		System.out.println(" leftDownTex="+leftDownTex);*/
		
		GL11.glTexCoord3f(abs(leftUpTex.x), abs(leftUpTex.y), abs(leftUpTex.z));
		GL11.glVertex3f(leftUp.x, leftUp.y, leftUp.z);
		
		GL11.glTexCoord3f(abs(rightUpTex.x), abs(rightUpTex.y), abs(rightUpTex.z));
		GL11.glVertex3f(rightUp.x, rightUp.y, rightUp.z);
		
		GL11.glTexCoord3f(abs(rightDownTex.x), abs(rightDownTex.y), abs(rightDownTex.z));
		GL11.glVertex3f(rightDown.x, rightDown.y, rightDown.z);
		
		GL11.glTexCoord3f(abs(leftDownTex.x), abs(leftDownTex.y), abs(leftDownTex.z));
		GL11.glVertex3f(leftDown.x, leftDown.y, leftDown.z);

		GL11.glEnd();
		GL11.glPopMatrix();
		// GL15.glEndQuery(GL33.GL_ANY_SAMPLES_PASSED);
		GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
		samples = BufferUtils.createIntBuffer(1);
		GL15.glGetQueryObject(querie, GL15.GL_QUERY_RESULT, samples);
		// System.out.println(" querie =" + samples.get());
		if (fbo != null) {
			fbo.desactiver();
		}
		return samples.get() > 0;

	}

}
