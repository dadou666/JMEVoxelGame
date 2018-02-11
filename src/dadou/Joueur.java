package dadou;

import com.jme.bounding.BoundingVolume;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.event.GameEvent;
import dadou.event.GameEventLeaf;
import dadou.mutator.Mouvement;
import dadou.mutator.Orientation;
import dadou.son.Emeteurs;
import dadou.son.Son;
import dadou.tools.canon.Canon;

public class Joueur extends ElementJeux {
	public Canon canon;
	public String mcPourTirer;
	public float echelle = 0.25f;
	public MondeInterfacePrive i;
	public Vector3f tmp = new Vector3f();
	private OrientedBoundingBox box;
	public Joueur suivant;
	public Joueur precedent;
	public Octree<OctreeValeur> octreeLeaf;
	public Octree<OctreeValeur> octreeRoot;
	public ListeEvtDetection list;
	public ListeEvtDetectionPourSon listSon;
	public Son sonImpact;
	public AbstractJoueur aj;

	public BoundingVolume getBoundingVolume() {
		return box;
	}

	public void setBox(OrientedBoundingBox box) {
		this.box = box;
	}

	public void entreeZoneDetection(ElementJeux o, ElementJeux om) {
		this.i.brickEditor.decor.DecorDeBriqueData.entreeZoneDetectionJoueur(i, om);

	}

	public void finSon(String nomSon) {

		this.i.brickEditor.decor.DecorDeBriqueData.finSon(i, nomSon);

	}

	public void collisionTire(Vector3f pos, Vector3f direction) {
		if (this.aj != null) {
			this.aj.i = this.i.mondeInterface;
			this.aj.collisionTire(pos, direction);
			return;
		}

		this.i.brickEditor.decor.DecorDeBriqueData.collisionTire(this.i, pos, direction);
	}

	public void sortieZoneDetection(ElementJeux o, ElementJeux om) {
		this.i.brickEditor.decor.DecorDeBriqueData.sortieZoneDetectionJoueur(i, om);

	}

	public void tirer(Vector3f position, Vector3f direction) {
		if (aj != null) {
			aj.i = i.mondeInterface;
			aj.tirer(position, direction);
		} else {
			i.brickEditor.decor.DecorDeBriqueData.tirer(i);
		}
		if (!ControlleurCamera.controlleur.tireActif()) {
			return;

		}

		if (mcPourTirer == null) {
			canon.tirer(this.sonImpact, position, direction);
			return;
		}
		Quaternion q = new Quaternion();
		// direction.set(this.i.brickEditor.game.getCamera().getDirection());
		Orientation.calculerQuaternionPolaireDirZ(direction, q);

		tmp.set(position);
		float d = 2.0f;
		tmp.addLocal(direction.x * d, direction.y * d, direction.z * d);
		ObjetMobilePourModelInstance om = i.creerObjet(mcPourTirer, tmp, q, echelle, null, null, false);

		if (om == null) {
			return;
		}

		om.speedTranslate = canon.vitesse;
		Mouvement mvt = new Mouvement(i.brickEditor, om, direction, canon.distance);
		i.initMutator(om, mvt);

	}

	public void detachFromOctree() {
		if (octreeLeaf == null) {

			return;
		}
		if (octreeLeaf.value.joueur == this) {
			octreeLeaf.value.joueur = suivant;
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

	public void attachToOctree(Octree<OctreeValeur> octree) {

		suivant = octree.value.joueur;
		precedent = null;
		if (octree.value.joueur != null) {
			octree.value.joueur.precedent = this;
		}
		octree.value.joueur = this;

		this.octreeLeaf = octree;

	}

	public Octree<OctreeValeur> getOctree() {

		return getOctree(this.octreeRoot);

	}

	public Octree<OctreeValeur> getOctree(Octree<OctreeValeur> u) {
		Octree<OctreeValeur> o = this.octreeRoot.getOctreeFor(box);
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

	public void updateOctree() {
		if (box == null) {
			return;
		}
		this.detachFromOctree();
		Octree<OctreeValeur> o = getOctree();
		if (o == null) {
			return;
		}
		this.attachToOctree(o);
	}

}
