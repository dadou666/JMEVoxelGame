package dadou;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.mutator.Animation;
import dadou.mutator.AnimationEtape;
import dadou.mutator.Mouvement;
import dadou.mutator.Mutator;
import dadou.physique.PhysiqueObjet;
import dadou.tools.BrickEditor;

public class ObjetMobilePourModelInstance extends ObjetMobile {

	public Vector3f dim;
	public OrientedBoundingBoxWithVBO box;
	private BoundingSphere boundingSphere;
	public AnimationEtape animation;

	static public float renduTransparence = 0.0f;
	public float speedRotate = 0.01f;
	public float speedTranslate = 1.0f;
	public float speedEchelle = 0.01f;

	public VoxelShaderParam lp;
	static final public Vector3f tmp = new Vector3f();

	// L'orientation des objet mobile est la direction X
	public boolean estVisible(Vector3f pos, float angle, float h) {
		tmp.set(pos);
		tmp.subtractLocal(box.getCenter());
		tmp.normalizeLocal();
		float c = tmp.dot(box.getXAxis());
		if (c <= 0) {
			return false;
		}
		if (Math.abs(pos.y - box.getCenter().y) >= h) {
			return false;
		}
		return (float) Math.acos(c) < angle;

	}

	public String toString() {
		return this.donnerNom() + "[" + this.mc.nomModele() + "]";
	}

	public ObjetMobile donnerObjetMobileCollision(Espace espace) {
		ObjetMobile om = null;
		if (boundingSphere != null) {

			om = espace.getOMFor(boundingSphere, true);
		} else {
			om = espace.getOMFor(box);
		}
		return om;
	}

	public void deplacerBoundingVolume(Vector3f dir) {
		box.getCenter().addLocal(dir);
		if (this.boundingSphere != null) {
			this.boundingSphere.getCenter().addLocal(dir);
		}

	}

	public boolean verifierCollision(GestionCollision gc) {
		// return gc.verifierCollision(box);
		if (boundingSphere != null) {
			return gc.verifierCollision(boundingSphere);
		} else {
			return gc.verifierCollision(box);
		}
	}

	public void animer(BrickEditor be) {
		if (animation != null) {
			ModelClasse tmp = animation.donnerModelClasse();
			if (tmp == null) {
				Animation copy = animation.animation;
				animation = null;
				this.evenement().finAnimation(this, copy);
				return;
			}
			this.mc = tmp;
		
			this.dim.set(mc.size);
			this.dim.multLocal(echelle);
		/*	this.box.getExtent().set(dim.x / 2.0f, dim.y / 2.0f, dim.z / 2.0f);
			if (boundingSphere != null) {
				boundingSphere.setRadius(this.box.extent.length());
			}*/
		//	this.obj.positionToZero();
		//	this.obj.translation(this.box.getCenter());
		//	this.updateTranslationFromBox();

		}

	}

	public void calculerPositionInitial() {
		obj.positionToZero();
		if (model == null) {
			return;
		}
		// Log.print(" dim ="+octreeRoot.dim+" "+model.x+" "+model.y+"
		// "+model.z);
		obj.translation(model.x - octreeRoot.dim, model.y - octreeRoot.dim,
				model.z - octreeRoot.dim);

		obj.translation((mc.size.x * (1 - model.modelClasse.echelle)) / 2.0f,
				(mc.size.y * (1 - model.modelClasse.echelle)) / 2.0f,
				(mc.size.z * (1 - model.modelClasse.echelle)) / 2.0f);

		if (model != null && model.rotation != null) {

			Quaternion q = new Quaternion();
			q.loadIdentity();
			this.obj.setRotation(q);
			this.updateBoxForMouvement();
			this.rotateCenter(model.rotation);
		}

	}

	public Vector3f U = new Vector3f();
	public Quaternion Q = new Quaternion();

	public void rotate(Vector3f c, Vector3f axe, float angle) {
		Q.fromAngleAxis(angle, axe);
		this.rotate(c, Q);

	}

	public void rotate(Vector3f c, Quaternion q) {
		obj.rotation(c, q);
		this.updateBoxForMouvement();

	}

	public void rotateCenter(Vector3f axe, float angle) {
		U.set(box.getCenter());
		// U.multLocal(-1);
		this.rotate(U, axe, angle);
	}

	Vector3f centre = null;

	public void rotateCenter(Quaternion q) {
		// this.updateBoxForMouvement();

		// U.multLocal(-1);
		this.detachFromOctree();
		U.set(box.getCenter());

		this.rotate(U, q);
		this.updateOctree();
		this.updateBoxForMouvement();
		// Log.print(" avant =" + c + " apres=" + box.getCenter()+" U
		// ="+centre);

	}
	public void translation(Vector3f dir) {
		this.detachFromOctree();
		obj.translation(dir);
		this.updateOctree();
		this.updateBoxForMouvement();
	}

	public void initTranslationRotationEchelle(Vector3f position,
			Quaternion rotation, float echelle) {
		this.echelle = echelle;
		this.dim = new Vector3f();
		this.dim.set(mc.size);
		this.dim.multLocal(echelle);
		if (rotation != null) {
			this.obj.setRotation(rotation);
		}

		this.obj.positionToZero();
		obj.translation(position);
		this.box.getCenter().set(position);
		box.getExtent().set(dim.x / 2.0f, dim.y / 2.0f, dim.z / 2.0f);
		if (this.boundingSphere != null) {
			this.boundingSphere.setRadius(this.box.extent.length());
			this.boundingSphere.getCenter().set(position);
		}
		this.updateBoxOrientation();
		this.updateTranslationFromBox();

	}

	public void updateTranslationFromBox() {
		obj.translation(-box.xAxis.x * (dim.x / 2.0f), -box.xAxis.y
				* (dim.x / 2.0f), -box.xAxis.z * (dim.x / 2.0f));
		obj.translation(-box.yAxis.x * (dim.y / 2.0f), -box.yAxis.y
				* (dim.y / 2.0f), -box.yAxis.z * (dim.y / 2.0f));
		obj.translation(-box.zAxis.x * (dim.z / 2.0f), -box.zAxis.y
				* (dim.z / 2.0f), -box.zAxis.z * (dim.z / 2.0f));

	}

	public void updateBoxOrientation() {
		if (box == null) {
			box = new OrientedBoundingBoxWithVBO();

		}
		Quaternion q = obj.getRotationGlobal();
		// box.transform(q, null, null);
		// box.getCenter().set(position);
		box.xAxis.set(Vector3f.UNIT_X);
		box.yAxis.set(Vector3f.UNIT_Y);
		box.zAxis.set(Vector3f.UNIT_Z);
		q.multLocal(box.xAxis);
		q.multLocal(box.yAxis);
		q.multLocal(box.zAxis);
	}

	public void updateBoxForMouvement() {
		this.updateBoxOrientation();
		updateOrientedBoudingBox();
		// box.transform(rotate, translate, scale)

	}

	public void updateOrientedBoudingBox() {
		if (box == null) {
			box = new OrientedBoundingBoxWithVBO();

		}
		box.getCenter().set(obj.getTranslationGlobal());
		box.getCenter().addLocal(box.xAxis.x * (dim.x / 2.0f),
				box.xAxis.y * (dim.x / 2.0f), box.xAxis.z * (dim.x / 2.0f));
		box.getCenter().addLocal(box.yAxis.x * (dim.y / 2.0f),
				box.yAxis.y * (dim.y / 2.0f), box.yAxis.z * (dim.y / 2.0f));
		box.getCenter().addLocal(box.zAxis.x * (dim.z / 2.0f),
				box.zAxis.y * (dim.z / 2.0f), box.zAxis.z * (dim.z / 2.0f));
		box.getExtent().set(dim.x / 2.0f, dim.y / 2.0f, dim.z / 2.0f);
		if (this.boundingSphere != null) {
			this.boundingSphere.setRadius(this.box.extent.length());
			this.boundingSphere.getCenter().set(box.getCenter());
		}
		// box.transform(rotate, translate, scale)

	}

	public ObjetMobilePourModelInstance(BrickEditor be, ModelInstance model) {

		this.model = model;
		this.init(be, model.modelClasse, false);

	}

	public boolean useBoundingSphere() {
		return this.boundingSphere != null;
	}

	public BoundingSphere getBoundingSphere() {
		return boundingSphere;
	}

	public void setBoundingSphere(BoundingSphere boundingSphere) {
		this.boundingSphere = boundingSphere;
	}

	public void init(BrickEditor be, ModelClasse modelClasse,
			boolean useBoundingSphere) {

		Octree<OctreeValeur> octree = be.espace.octree;
		Game g = be.game;
		mc = modelClasse;
		transparence = modelClasse.transparence;
		modifierOmbre = modelClasse.modifierOmbre;
		this.octreeRoot = octree;

		if (model != null) {
			dim = (mc.size.mult(model.modelClasse.echelle));
		}
		this.obj = new Objet3D();
		if (!mc.estVide) {
			lp = mc.getVoxelShaderParam(g);

			if (model != null) {

				this.echelle = model.modelClasse.echelle;
			}

		}
		this.calculerPositionInitial();
		box = new OrientedBoundingBoxWithVBO();
		if (useBoundingSphere) {
			this.boundingSphere = new BoundingSphere();
		}

		if (model != null) {
			this.updateBoxForMouvement();
			Octree<OctreeValeur> o = getOctree();
			this.attachToOctree(o);
		}

	}

	public ObjetMobilePourModelInstance(BrickEditor be,
			ModelClasse modelClasse, boolean useBoundingSphere) {

		this.init(be, modelClasse, useBoundingSphere);

	}

	public Octree<OctreeValeur> getOctree(Octree<OctreeValeur> u) {
		Octree<OctreeValeur> o = null;
		if (this.boundingSphere != null) {
			o = this.octreeRoot.getOctreeFor(this.boundingSphere);
		} else {
			o = this.octreeRoot.getOctreeFor(box);
		}
		if (o == null) {
			return null;
		}
		if (u == o) {
			return o;
		}
		if (o.children == null) {
			return o;
		}
		if (o.level == 0) {
			return o;
		}
		return getOctree(o);

	}

	public OrientedBoundingBoxWithVBO getBox() {
		return box;
	}

	public Octree<OctreeValeur> getOctree() {

		return getOctree(this.octreeRoot);

	}

	public void echelle(float echelle) {

		this.dim.set(mc.size);
		this.dim.multLocal(echelle);
		this.box.getExtent().set(dim.x / 2.0f, dim.y / 2.0f, dim.z / 2.0f);
		if (this.boundingSphere != null) {
			this.boundingSphere.setRadius(this.box.extent.length());
		}
		this.obj.positionToZero();
		this.obj.translation(this.box.getCenter());
		this.updateTranslationFromBox();
		this.echelle = echelle;

	}

	public void move(Vector3f dep) {
		this.move(dep.x, dep.y, dep.z);

	}

	public void move(float dx, float dy, float dz) {

		this.obj.translation(dx, dy, dz);

		this.detachFromOctree();
		this.updateOrientedBoudingBox();
		Octree<OctreeValeur> o = getOctree();
		this.attachToOctree(o);

		// System.out.println("val="+octreeLeaf+" "+old);

	}

	public void reset() {
		this.detachFromOctree();
		this.calculerPositionInitial();

		estVisible = true;

		echelle = model.modelClasse.echelle;
		dim.set(mc.size);
		dim.multLocal(model.modelClasse.echelle);
		this.updateOrientedBoudingBox();
		Octree<OctreeValeur> o = getOctree();
		if (o == null) {
			return;
		}
		this.attachToOctree(o);

	}

	public boolean contains(BoundingSphere bs) {
		if (this.boundingSphere != null) {
			return this.boundingSphere.intersects(bs);
		}
		return bs.intersects(box);

	}

	public boolean contains(Vector3f pos) {
		rotationInverse.set(obj.getRotationGlobal());
		rotationInverse.inverseLocal();
		tmp.set(pos);
		tmp.subtractLocal(obj.getTranslationGlobal());
		rotationInverse.multLocal(tmp);

		if (tmp.x < 0) {
			return false;
		}
		if (tmp.y < 0) {
			return false;
		}
		if (tmp.z < 0) {
			return false;
		}
		if (tmp.x > dim.x) {
			return false;
		}
		if (tmp.y > dim.y) {
			return false;
		}
		if (tmp.z > dim.z) {
			return false;
		}
		return true;

	}

	public boolean error = false;
	public void activePhysique() {
		if (this.listPO == null) {
			return;
		}
		for(PhysiqueObjet po:this.listPO) {
			po.activate();
		}
	}
	public void dessiner(Camera cam) {
		// System.out.println(" nom="+model.nomObjet);
		if (!estVisible) {
			return;
		}
		if (!mc.estVide) {
			if (construction == null) {
				obj.brique = mc.brique;
			} else {
				BriqueAvecTexture3D brique = construction.donnerBrique();
				brique.echelle = echelle;
				brique.couleurFactor = this.couleurFactor;
				obj.brique = brique;

			}
			mc.brique.echelle = echelle;

			mc.brique.couleurFactor = this.couleurFactor;
			LumiereBuffer.courant = lb;
			ZoneBuffer.courant = zb;
			Game.RenderMode old = Game.rm;
			if (this.etatOmbre == EtatOmbre.OmbreInactive || !utiliserLumiere) {
				Game.rm = Game.RenderMode.Normal;
			}
			if (dmc != null) {
				dmc.initialiser(echelle, this.getTranslationLocal(),
						this.getRotationLocal());

				dmc.dessiner(cam);
			} else if (listPO != null) {
				for (PhysiqueObjet po : listPO) {
					po.dessiner(cam);
				}
			} else {
				try {
					if (!error) {
						obj.dessiner(cam);
						if (construction == null)
							for (ModelClasse mcSecondaire : this.mcMap.values()) {
								if (mcSecondaire != null) {
									obj.brique = mcSecondaire.brique;
									mcSecondaire.brique.echelle = echelle;

									mcSecondaire.brique.couleurFactor = this.couleurFactor;
									obj.dessiner(cam);
								}

							}
					}

				} catch (Throwable t) {
					error = true;
					Log.print(" classe =" + this.mc.nom);
					t.printStackTrace();
				}
			}
			Game.rm = old;
			LumiereBuffer.courant = null;
			ZoneBuffer.courant = null;
		}
		this.dessinerDebugBox(cam);
	}

	public void dessinerDebugBox(Camera cam) {
		if (box.debugBox == null) {
			return;
		}
		Shader shader = box.debugBox.shader;
		shader.use();

		// selection.shader.glUniformfARB("size", 1);
		shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);

		shader.glUniformfARB("echelle", echelle);
		box.objDebugBox.positionToZero();
		box.objDebugBox.translation(box.getCenter());
		box.objDebugBox.setRotation(obj.getRotationGlobal());
		box.objDebugBox.dessiner(cam);

	}

	public void dessinerBox(Camera cam, Objet3D o, VBOTexture2D vbo) {
		float deltat = 0.00f;
		vbo.shader.use();
		vbo.shader.glUniformfARB("dx", dim.x + 2 * deltat);
		vbo.shader.glUniformfARB("dy", dim.y + 2 * deltat);
		vbo.shader.glUniformfARB("dz", dim.z + 2 * deltat);
		// selection.shader.glUniformfARB("size", 1);
		// vbo.shader.glUniformfARB("color", 1.0f, 1.0f, 1.0f, 0.0f);
		o.positionToZero();
		o.translation(obj.getTranslationGlobal());
		o.translation(-deltat, -deltat, -deltat);
		o.dessiner(cam);
	}

	public void calculerDistanceCamera(Camera cam) {

		tmp.set(cam.getLocation());
		tmp.subtractLocal(obj.getTranslationGlobal());
		distanceCamera = tmp.lengthSquared();

	}

	public void finSon(String nomSon) {
		this.evenement().finSon(this, nomSon);

	}

}
