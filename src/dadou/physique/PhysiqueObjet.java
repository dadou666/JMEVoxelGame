package dadou.physique;

import java.nio.FloatBuffer;

import javax.vecmath.Quat4f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Log;
import dadou.Objet3D;

public class PhysiqueObjet {
	public Objet3D obj;
	public RigidBody body;
	public PhysiqueMonde monde;
	public String nom;
	public Transform m = new Transform();
	private static float[] glMat = new float[16];
	public Vector3f initPos = null;
	public Vector3f oldOrigin = new Vector3f();
	public Vector3f dep = new Vector3f();
	public Quat4f q = new Quat4f();
	public Vector3f tmp = new Vector3f();
	public Quaternion qq = new Quaternion();
	private static FloatBuffer floatBuf = BufferUtils.createFloatBuffer(16);
	public float distMvt = 10.0f;
	public float distTotal = 0.0f;
	public boolean show = true;
	public boolean actif = false;
	public int numStep =0;

	public void supprimer() {
		if (!show) {
			return;
		}
		show = false;
		this.monde.dynamicsWorld.removeRigidBody(body);
		
	}
	public void calcDistMvt() {
		if (!actif) {
			return;
		}
		numStep++;
		DefaultMotionState myMotionState = (DefaultMotionState) body
				.getMotionState();
		m.set(myMotionState.graphicsWorldTrans);
		float px= myMotionState.graphicsWorldTrans.origin.x;
		float py= myMotionState.graphicsWorldTrans.origin.y;
		float pz= myMotionState.graphicsWorldTrans.origin.z;
		float dx = px- oldOrigin.x;
		float dy = py - oldOrigin.y;
		float dz = pz - oldOrigin.z;
		oldOrigin.set(px,py,pz);
		if (initPos == null) {
			initPos = new Vector3f(oldOrigin);
		} else {
			distTotal = initPos.distance(oldOrigin);
		}

		distMvt = dx * dx + dy * dy + dz * dz;
	
	}
	public void activate() {
		this.monde.dynamicsWorld.addRigidBody(body);
		body.setActivationState(CollisionObject.ACTIVE_TAG);
		actif = true;
	}
	public void loadMatrix() {

		DefaultMotionState myMotionState = (DefaultMotionState) body
				.getMotionState();
		m.set(myMotionState.graphicsWorldTrans);
	

		m.getOpenGLMatrix(glMat);
		floatBuf.clear();
		floatBuf.put(glMat).flip();
		myMotionState.graphicsWorldTrans.getRotation(q);
		
		qq.set(q.x, q.y, q.z, q.w);
		tmp.set(dep);
		qq.multLocal(tmp);
		GL11.glTranslatef(-tmp.x, -tmp.y, -tmp.z);
		GL11.glMultMatrix(floatBuf);

	}

	public void dessiner(Camera cam) {
		if (!show) {
			return;
		}
		obj.dessiner(cam, this);
	}

}
