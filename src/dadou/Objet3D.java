package dadou;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

import dadou.physique.PhysiqueObjet;

public class Objet3D {

	private Objet3D parent;

	private Quaternion rotationGlobal;
	private Quaternion rotationLocal;
	public boolean estElementDecor = false;

	public Quaternion getRotationGlobal() {
		return rotationGlobal;
	}

	public Quaternion getRotationLocal() {
		return rotationLocal;
	}

	private Vector3f axeRotationGlobal = new Vector3f();

	private float angle = 0.0f;
	private Vector3f translationGlobal;

	public Vector3f getTranslationGlobal() {
		return translationGlobal;
	}

	public Vector3f getTranslationLocal() {
		return translationGlobal;
	}

	private Vector3f translationLocal;
	public Quaternion tmp = new Quaternion();
	private Vector3f tmpV = new Vector3f();

	public Objet3D() {
		translationGlobal = new Vector3f(0, 0, 0);
		rotationGlobal = new Quaternion();
		rotationGlobal.loadIdentity();
		translationLocal = new Vector3f(0, 0, 0);
		rotationLocal = new Quaternion();
		rotationLocal.loadIdentity();

	}

	public Objet3D parent() {
		return parent;
	}

	private void globalRotation(Vector3f in, Vector3f out) {
		rotationGlobal.mult(in, out);
	}

	public Vector3f localEnGlobal(final Vector3f in, Vector3f store) {
		if (store == null)
			store = new Vector3f();
		// multiply with scale first, then rotate, finally translate (cf.
		// Eberly)
		return rotationGlobal.mult(store.set(in), store).addLocal(
				translationGlobal);
	}

	private void _translation(Vector3f t) {
		translationLocal.addLocal(t);
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}

	private void _translation(float x, float y, float z) {
		tmpV.set(x, y, z);
		_translation(tmpV);

	}

	protected void modifierTranslationGlobal() {
		if (parent != null) {
			translationGlobal = parent.localEnGlobal(translationLocal,
					translationGlobal);
		} else {
			translationGlobal.set(translationLocal);
		}
		for (Objet3D enfant : enfants) {
			enfant.modifierTranslationGlobal();
		}
		if (camera != null) {
			camera.setLocation(translationGlobal);

		}
	}

	public void setRotation(Quaternion q) {
		this.rotationLocal.set(q);
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();
	}

	public void setTranslationEtRotation(Vector3f t, Quaternion q) {
		this.rotationLocal.set(q);
		this.translationLocal.set(t);
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}

	public void slerp(Quaternion debut, Quaternion fin, float t) {
		rotationLocal.slerp(debut, fin, t);
		rotationLocal.normalize();
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();
	}

	protected void modifierRotationGlobal() {
		if (parent != null) {
			parent.rotationGlobal.mult(rotationLocal, rotationGlobal);
		} else {
			rotationGlobal.set(rotationLocal);
		}
		rotationGlobal.normalize();

		angle = rotationGlobal.toAngleAxis(axeRotationGlobal)
				* FastMath.RAD_TO_DEG;
		;

		for (Objet3D enfant : enfants) {
			enfant.modifierRotationGlobal();
		}
		if (camera != null) {
			camera.setAxes(rotationGlobal);

		}
	}

	private void _rotation(Vector3f axe, float a) {
		tmp.fromAngleAxis(a, axe);
		rotationLocal.multLocal(tmp);
		// rotationLocal.normalize();
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}

	private void _rotation(Quaternion q) {
		// tmp.fromAngleAxis(a, axe);
		rotationLocal.multLocal(q);
		// rotationLocal.normalize();
		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}

	public void translation(Vector3f t) {
		this._translation(t);

	}

	public void positionToZero() {
		this._translation(-translationLocal.x, -translationLocal.y,
				-translationLocal.z);
	}

	public void translation(float x, float y, float z) {
		this._translation(x, y, z);

	}

	public void rotation(Vector3f axe, float a) {
		this._rotation(axe, a);

	}

	public void rotation(Vector3f c, Vector3f axe, float a) {
		translationLocal.subtractLocal(c);
		tmp.fromAngleAxis(a, axe);
		rotationLocal.multLocal(tmp);

		tmp.multLocal(translationLocal);

		translationLocal.addLocal(c);

		tmp.multLocal(rotationLocal);
		rotationLocal.set(tmp);
		rotationLocal.normalize();

		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}

	public void rotation(Vector3f c, Quaternion q) {
		translationLocal.subtractLocal(c);
		tmp.set(q);
		tmp.multLocal(translationLocal);
		translationLocal.addLocal(c);

		tmp.multLocal(rotationLocal);
		rotationLocal.set(tmp);
		rotationLocal.normalize();

		this.modifierRotationGlobal();
		this.modifierTranslationGlobal();

	}
	public boolean visible = true;
	public void dessiner(Camera cam) {
		if (!visible) {
			return;
		}
		if (brique != null) {

			brique.initFromShadowMap(this, cam);

		
				GL11.glPushMatrix();

				GL11.glTranslatef(translationGlobal.x, translationGlobal.y,
						translationGlobal.z);
				GL11.glRotatef(angle, axeRotationGlobal.x, axeRotationGlobal.y,
						axeRotationGlobal.z);

				brique.dessiner(cam);
		

				GL11.glPopMatrix();
			
		}

		for (Objet3D o : enfants) {
			o.dessiner(cam);

		}
	}

	public void dessiner(Camera cam, PhysiqueObjet po) {

		if (brique != null) {

			brique.initFromShadowMap(this, cam);

			try {
				GL11.glPushMatrix();

				po.loadMatrix();
				brique.dessiner(cam);
			} catch (Throwable t) {
				t.printStackTrace();
			} finally {

				GL11.glPopMatrix();
			}
		}

		for (Objet3D o : enfants) {
			o.dessiner(cam);

		}
	}

	public float getAngle() {
		return angle;
	}

	public Vector3f getAxeRotationGlobal() {
		return axeRotationGlobal;
	}

	public void supprimer(Objet3D obj) {
		obj.parent = null;
		enfants.remove(obj);
	}

	public void ajouter(Objet3D obj) {
		if (obj.parent != null) {
			obj.parent.supprimer(obj);
		}
		obj.parent = this;
		enfants.add(obj);
	}

	public Objet3D enfant(int i) {
		return enfants.get(i);
	}

	private List<Objet3D> enfants = new ArrayList<Objet3D>();
	public Element3D brique;
	public Camera camera;

}
