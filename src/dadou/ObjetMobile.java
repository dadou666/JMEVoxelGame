package dadou;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.graphe.GrapheElement;
import dadou.jeux.TrajectoireContext;
import dadou.mutator.Decomposer;
import dadou.mutator.Exploser;
import dadou.mutator.Mutator;
import dadou.physique.PhysiqueMonde;
import dadou.physique.PhysiqueObjet;
import dadou.tools.BrickEditor;
import dadou.tools.canon.CibleAffichageDistance;

public class ObjetMobile extends ElementJeux {
	public float factorSpeed = 1;
	public String nom;
	public Objet3D obj;

	public boolean utiliserLumiere = true;
	public ModelInstance model;
	public ModelClasse mc;
	public HashMap<String,ModelClasse> mcMap = new HashMap<>();
	public DecompositionModelClasseCtx dmc;
	public float echelle = 1.0f;
	public float couleurFactor = 0.0f;
	public boolean testCollisionAvecDecor = true;
	public boolean estVisible = true;
	public ModelEvent me;
	public float transparence = 0.0f;
	public List<PhysiqueObjet> listPO =null;
	public Mutator mutator;
	public EtatOmbre etatOmbre = EtatOmbre.OmbreActive;
	public ConstructionModelClasseEnCours construction;
	public LumiereBuffer lb;
	public ZoneBuffer zb;
	public boolean modifierOmbre = false;
	public ObjetMobile suivant;
	public ObjetMobile precedent;
	public Octree<OctreeValeur> octreeLeaf;
	public Octree<OctreeValeur> octreeRoot;
	public ZoneDetection zoneDetection;
	public CibleAffichageDistance cad;
	public ObjetMobile emetteur;
	public void activePhysique() {
		
	}
	public boolean explose() {

		if (mutator != null && (mutator.explose())) {
			return true;
		}
		return false;

	}

	public void animer(BrickEditor be) {

	}

	public boolean testCollisionAvecAutreObjetMobile = true;

	public Vector3f dep = new Vector3f();

	public TrajectoireContext tc;

	public String donnerNom() {
		if (nom == null) {
			return model.nomObjet;
		}
		return nom;
	}

	public Quaternion rotationInverse = new Quaternion();
	public Vector3f tmp = new Vector3f();

	public ModelInstance model() {
		return model;
	}

	public Octree<OctreeValeur> getOctree() {
		return null;
	}

	public void updateOctree() {
		this.detachFromOctree();
		Octree<OctreeValeur> o = getOctree();
		if (o == null) {
			return;
		}
		this.attachToOctree(o);
	}

	public ModelEvent evenement() {
		if (me == null) {
			throw new Error(" nom classe =" + mc.nomModele() + "  " + this.donnerNom());

		}
		return me;
	}

	public void entreeZoneDetection(ElementJeux obj, ElementJeux oEntree) {
		this.evenement().entreeZoneDetection(obj, oEntree);

	}

	public void sortieZoneDetection(ElementJeux o, ElementJeux oSortie) {
		this.evenement().sortieZoneDetection(o, oSortie);
	}

	public BoundingVolume getBox() {
		return null;
	}

	public BoundingVolume getBoundingVolume() {
		return getBox();
	}

	public void dessiner(Camera cam) {
		if (lb != null && lb.nombre > 0) {
			Log.print("Objet mobile dans la lumiere");
		}
		LumiereBuffer.courant = lb;
		ZoneBuffer.courant=zb;
		obj.dessiner(cam);
		LumiereBuffer.courant = null;
		ZoneBuffer.courant=null;

	}
	public void rotateCenter(Quaternion q) {
		
	}
	public void calculerPositionInitial() {
		
	}

	public void detachFromOctree() {
		if (octreeLeaf == null) {

			return;
		}
		if (octreeLeaf.value.om == this) {
			octreeLeaf.value.om = suivant;
			if (suivant != null) {
				suivant.precedent = null;
			}
			suivant = null;
			precedent = null;
			octreeLeaf = null;
			return;
		}
		precedent.suivant = suivant;
		if (suivant != null) {
			suivant.precedent = precedent;
		}
		suivant = null;
		precedent = null;
		octreeLeaf = null;
	}

	public void slerp(Quaternion debut, Quaternion fin, float t) {
		this.obj.slerp(debut, fin, t);

	}

	public void setTranslationEtRotation(Vector3f t, Quaternion q) {
		this.obj.setTranslationEtRotation(t, q);

	}

	public Quaternion getRotationLocal() {
		return obj.getRotationLocal();
	}
	public Quaternion getRotationGlobal() {
		return obj.getRotationGlobal();
	}
	public Vector3f getTranslationLocal() {
		return obj.getTranslationLocal();
	}

	public Vector3f getTranslationGlobal() {
		return obj.getTranslationGlobal();
	}

	public void translation(Vector3f dir) {
		obj.translation(dir);

	}
	public void translationMutator(Vector3f dir) {
		obj.translation(dir);

	}

	public void translation(float x, float y, float z) {
		obj.translation(x, y, z);

	}

	public void positionToZero() {
		this.obj.positionToZero();

	}

	public void attachToOctree(Octree<OctreeValeur> octree) {

		suivant = octree.value.om;
		precedent = null;
		if (octree.value.om != null) {
			octree.value.om.precedent = this;
		}
		octree.value.om = this;

		this.octreeLeaf = octree;

	}

	public boolean contains(Vector3f pos) {
		return false;
	}

	public boolean contains(Vector3f pos, float rayon) {
		return false;
	}

	public boolean contains(BoundingSphere bs) {
		return false;
	}

	public void echelle(float echelle) {

	}

	public void move(float dx, float dy, float dz) {

	}

	public void dessinerBox(Camera cam, Objet3D obj, VBOTexture2D vbo) {

	}

	public void reset() {

	}

	public void calculerDistanceCamera(Camera cam) {

	}

}
