package dadou.physique;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Quat4d;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.BulletStats;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.renderer.Camera;

import dadou.BriqueAvecTexture3D;
import dadou.DecompositionModelClasse;
import dadou.Espace;
import dadou.Fps;
import dadou.Game;
import dadou.Log;
import dadou.Objet3D;
import dadou.ObjetMobilePourModelInstance;
import dadou.mutator.Mutator;
import dadou.tools.BrickEditor;

public class PhysiqueMonde {
	private BroadphaseInterface broadphase;
	private CollisionDispatcher dispatcher;
	private ConstraintSolver solver;
	private DefaultCollisionConfiguration collisionConfiguration;
	public DiscreteDynamicsWorld dynamicsWorld;
	public Object marque = new Object();
	public List<RigidBody> rigidBodyDecor = new ArrayList<>();
	public int totalMutator = 0;
	protected Clock clock = new Clock();

	public BrickEditor be;

	public PhysiqueMonde(BrickEditor be) {
		collisionConfiguration = new DefaultCollisionConfiguration();
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		this.be = be;
		broadphase = new DbvtBroadphase();

		// the default constraint solver. For parallel processing you can use a
		// different solver (see Extras/BulletMultiThreaded)
		// BulletMultiThreaded n;
		SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
		solver = sol;

		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);

		dynamicsWorld.setGravity(new Vector3f(0f, -50, 0f));

	}

	List<BoundingBox> decor = new ArrayList<>();

	public void ajouterDecor(BoundingBox zone) {
		decor.clear();
		be.decor.gestionCollision.listeBoxCollision(this, zone, decor);

		// Log.print(" size =" + decor.size());
		for (BoundingBox box : decor) {
			float ex = box.xExtent;
			float ey = box.yExtent;
			float ez = box.zExtent;
			CollisionShape groundShape = new BoxShape(new Vector3f(ex, ey, ez));
			// CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0,
			// 1, 0), 50);

			Transform groundTransform = new Transform();
			groundTransform.setIdentity();
			groundTransform.origin.set(box.getCenter().x, box.getCenter().y,
					box.getCenter().z);

			// We can also use DemoApplication::localCreateRigidBody, but for
			// clarity it is provided here:
			{
				float mass = 0f;

				// rigidbody is dynamic if and only if mass is non zero,
				// otherwise static
				boolean isDynamic = (mass != 0f);

				Vector3f localInertia = new Vector3f(0, 0, 0);
				if (isDynamic) {
					groundShape.calculateLocalInertia(mass, localInertia);
				}

				// using motionstate is recommended, it provides interpolation
				// capabilities, and only synchronizes 'active' objects
				DefaultMotionState myMotionState = new DefaultMotionState(
						groundTransform);
				RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(
						mass, myMotionState, groundShape, localInertia);
				RigidBody body = new RigidBody(rbInfo);

				// add the body to the dynamics world
				dynamicsWorld.addRigidBody(body);
				rigidBodyDecor.add(body);
			}

		}
	}

	public PhysiqueMutator creerDecompositionPhysique(
			ObjetMobilePourModelInstance om, DecompositionModelClasse dmc,
			float impulseFactor, int numStepTransparence, boolean remove) {
		synchronized (this) {
			PhysiqueMutator pm = new PhysiqueMutator();
			pm.remove = remove;
			pm.espace = this.be.espace;

			pm.numStepTransparence = numStepTransparence;
			pm.transparenceDeltat = 1.0f / (float) numStepTransparence;
			pm.transparence = 1.0f;
			pm.pm = this;
		
			dynamicsWorld.setGravity(new Vector3f(0f, -50, 0f));

			com.jme.math.Vector3f global = om.getTranslationGlobal();
			for (int i = 0; i < dmc.elements.size(); i++) {
				Objet3D obj = dmc.elements.get(i);
				PhysiqueObjet po = new PhysiqueObjet();
				po.nom = dmc.noms.get(i);
				BriqueAvecTexture3D brique = dmc.briques.get(i);
				// Log.print(" " + brique.tex.dimX + "," + brique.tex.dimY + ","
				// + brique.tex.dimZ);
				float ex = brique.tex.dimX;
				float ey = brique.tex.dimY;
				float ez = brique.tex.dimZ;
				ex = ex / 2.0f;
				ey = ey / 2.0f;
				ez = ez / 2.0f;
				ex = ex * om.echelle;
				ey = ey * om.echelle;
				ez = ez * om.echelle;
				po.dep.set(ex, ey, ez);
				CollisionShape colShape = new BoxShape(new Vector3f(ex, ey, ez));
				Transform startTransform = new Transform();
				startTransform.setIdentity();

				float mass = 0.5f;
				float totalMass = 1.0f;// brique.tex.dimX*brique.tex.dimY*brique.tex.dimZ;
				mass = totalMass * mass;
				// rigidbody is dynamic if and only if mass is non zero,
				// otherwise
				// static
				boolean isDynamic = (mass != 0f);

				Vector3f localInertia = new Vector3f(0, 0, 0);
				if (isDynamic) {
					colShape.calculateLocalInertia(mass, localInertia);
				}
				// Log.print(" pos elt =" + obj.getTranslationGlobal());
				/*
				 * startTransform.origin.set(obj.getTranslationGlobal().x-ex,
				 * obj.getTranslationGlobal().y-ey,
				 * obj.getTranslationGlobal().z-ez);
				 */
				com.jme.math.Vector3f pp = dmc.positionsInitiale.get(i);
				com.jme.math.Vector3f qq = new com.jme.math.Vector3f();
				qq.set(pp);
				qq.multLocal(om.echelle);
				Quaternion qt = om.obj.getRotationGlobal();
				qt.multLocal(qq);
				startTransform.origin.set(global.x + qq.x, global.y + qq.y,
						global.z + qq.z);
				po.oldOrigin.set(startTransform.origin.x,
						startTransform.origin.y, startTransform.origin.z);
				Quat4f q = new Quat4f();

				// Log.print(" q="+obj.getRotationGlobal());
				q.set(qt.x, qt.y, qt.z, qt.w);
				startTransform.setRotation(q);

				// using motionstate is recommended, it provides interpolation
				// capabilities, and only synchronizes 'active' objects
				DefaultMotionState myMotionState = new DefaultMotionState(
						startTransform);
				RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(
						mass, myMotionState, colShape, localInertia);
				com.jme.math.Vector3f c = om.box.getCenter();
				Vector3f impulse = new Vector3f(startTransform.origin.x - c.x,
						startTransform.origin.y - c.y, startTransform.origin.z
								- c.z);

				impulse.normalize();
				impulse.scale(impulseFactor);
				// Log.print( "impulse = "+impulse);
				Vector3f relPos = new Vector3f();
				RigidBody body = new RigidBody(rbInfo);
				relPos.set(startTransform.origin);
				if (impulseFactor > 0.0f) {
					body.applyImpulse(impulse, relPos);
				}

				// body.setActivationState(CollisionObject.ISLAND_SLEEPING);

				// dynamicsWorld.addRigidBody(body);

				po.body = body;
				po.obj = obj;
				pm.objets.add(po);
				po.monde = this;

			}

			return pm;
		}

	}

	Thread physicThread = null;

	public void step() {

this.privateStep();

	}

	public void privateStep() {
		synchronized(this) {
		try {
			Game.fpsMesure.calculer(() -> {
				if (totalMutator == 0 && !this.rigidBodyDecor.isEmpty()) {
					// Log.print(" remove rigidBody "+this.rigidBodyDecor.size()
					// );
					for (RigidBody rb : this.rigidBodyDecor) {
						dynamicsWorld.removeRigidBody(rb);
					}
					marque = new Object();
					
					this.rigidBodyDecor.clear();
					Log.print(" clear  "+this.rigidBodyDecor.size());
				}
				
				dynamicsWorld.stepSimulation(0.5f);
				dynamicsWorld.debugDrawWorld();
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} }
	}

}
