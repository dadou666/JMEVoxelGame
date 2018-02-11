package dadou.collision;

import java.awt.Color;
import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;

import dadou.DecorDeBrique;
import dadou.ElementDecor;
import dadou.Octree;
import dadou.OctreeDivision;
import dadou.OrientedBoundingBoxWithVBO;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.physique.PhysiqueMonde;
import dadou.tools.BrickEditor;
import dadou.VoxelTexture3DEcouteur;

public class GestionCollision implements VoxelTexture3DEcouteur {
	public Octree<EtatOctree> octree;

	public DecorDeBrique decor;

	public GestionCollision(DecorDeBrique decor, int niveau, float d)
			throws CouleurErreur {
		octree = new Octree<>(new Vector3f(0, 0, 0), niveau, d);
		octree.value = new EtatOctree(0, 0, 0, (int) Math.pow(2, niveau));
		this.decor = decor;
		// octree.value.initRec(decor, octree);

	}

	public GestionCollision(DecorDeBrique decor, Octree<EtatOctree> octree) {
		this.octree = octree;
		this.decor = decor;
	}

	public GestionCollision() {
		octree = new Octree<>(new Vector3f(0, 0, 0),
				BrickEditor.niveauCollision, 1);
		octree.value = new EtatOctree(0, 0, 0, (int) Math.pow(2,
				BrickEditor.niveauCollision));
	}

	public boolean verifierCollisionSphere(Vector3f p, float rayon) {
		return this.verifierCollisionSphere(octree, p, rayon);

	}

	public void listeBoxCollision(OctreeDivision od, Vector3f p, float rayon,
			List<BoundingBox> resultat) {
		this.listeBoxCollision(octree, od, p, rayon, resultat);
	}

	public void listeBoxCollision(PhysiqueMonde pm,BoundingBox box, List<BoundingBox> resultat) {
		this.listeBoxCollision(pm,octree, box, resultat);
	}

	public void listeBoxCollision(PhysiqueMonde pm,Octree<EtatOctree> o, BoundingBox box,
			List<BoundingBox> resultat) {
		if (o.value.estVide()) {
			return;
		}
		if (o.value.estPlein()) {

			if (o.box.intersects(box) && o.value.marque != pm.marque) {
				BoundingBox bb = new BoundingBox();
				bb.getCenter().set(o.box.getCenter());
				bb.xExtent =o.box.xExtent;
				bb.yExtent =o.box.yExtent;
				bb.zExtent =o.box.zExtent;
				o.value.marque = pm.marque;
				resultat.add(bb);
			}
			return;

		}
		if (o.level == 0 || o.children == null) {
			return;
		}

		for (Octree<EtatOctree> child : o.children) {
			this.listeBoxCollision(pm,child, box, resultat);
		}

		return;

	}

	public void listeBoxCollision(Octree<EtatOctree> o, OctreeDivision od,
			Vector3f p, float rayon, List<BoundingBox> resultat) {
		if (o.value.estVide()) {
			return;
		}
		if (o.value.estPlein()) {

			if (o.collision(p, rayon)) {
				if (o.level == 0) {
					BoundingBox bb = new BoundingBox();
					bb.getCenter().set(o.box.getCenter());
					bb.xExtent = o.box.xExtent;
					bb.yExtent = o.box.yExtent;
					bb.zExtent = o.box.zExtent;
					resultat.add(bb);
					return;
				}
				od.init(o);
				o.bs.radius = rayon;
				o.bs.getCenter().set(p);
				od.calculer(o.bs, resultat);

			}

		}
		if (o.level == 0 || o.children == null) {
			return;
		}
		Octree<EtatOctree> u = o.getOctreeFor(p, rayon);
		if (u == o) {
			for (Octree<EtatOctree> child : u.children) {
				this.listeBoxCollision(child, od, p, rayon, resultat);
			}

			return;

		}
		this.listeBoxCollision(u, od, p, rayon, resultat);
	}

	public boolean verifierCollision(OrientedBoundingBoxWithVBO box) {
		return this.verifierCollision(octree, box);

	}

	public boolean verifierCollision(BoundingBox box) {
		return this.verifierCollision(octree, box);

	}

	public boolean verifierCollision(BoundingSphere bs) {
		return this.verifierCollisionSphere(octree, bs.getCenter(), bs.radius);

	}

	public boolean verifierCollision(Octree<EtatOctree> octree,
			OrientedBoundingBoxWithVBO box) {
		if (octree.value.estVide()) {
			return false;
		}
		if (octree.value.estPlein()) {

			return octree.box.intersects(box);
		}
		Octree<EtatOctree> oct = octree.getOctreeFor(box);
		if (oct == null) {
			return false;
		}
		if (oct == octree) {
			for (Octree<EtatOctree> child : octree.children) {
				if (verifierCollision(child, box)) {
					return true;
				}

			}
			return false;
		}
		return verifierCollision(oct, box);

	}

	public boolean verifierCollision(Octree<EtatOctree> octree, BoundingBox box) {
		if (octree.value.estVide()) {
			return false;
		}
		if (octree.value.estPlein()) {

			return octree.box.intersects(box);
		}
		Octree<EtatOctree> oct = octree.getOctreeFor(box);
		if (oct == null) {
			return false;
		}
		if (oct == octree) {
			for (Octree<EtatOctree> child : octree.children) {
				if (verifierCollision(child, box)) {
					return true;
				}

			}
			return false;
		}
		return verifierCollision(oct, box);

	}

	public boolean verifierCollisionSphere(Octree<EtatOctree> o, Vector3f p,
			float rayon) {
		if (o.value.estVide()) {
			return false;
		}
		if (o.value.estPlein()) {
			return o.collision(p, rayon);
		}
		Octree<EtatOctree> u = o.getOctreeFor(p, rayon);
		if (u == o) {
			for (Octree<EtatOctree> child : u.children) {
				if (this.verifierCollisionSphere(child, p, rayon)) {
					return true;
				}
			}
			return false;

		}
		return this.verifierCollisionSphere(u, p, rayon);

	}

	public void ajouterBrique(int x, int y, int z) {
		if (this.briquePresente(x, y, z)) {
			return;
		}
		this.ajouterBrique(octree, x, y, z);

	}

	public void verifierOctree(int dim) throws CouleurErreur {

		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				for (int z = 0; z < dim; z++) {
					Color color = decor.lireCouleur(x, y, z);
					if (ElementDecor.estVide(color)) {
						if (this.briquePresente(x, y, z)) {
							throw new Error(" Error " + x + "," + y + "," + z);
						}

					} else {
						if (!this.briquePresente(x, y, z)) {
							throw new Error(" Error " + x + "," + y + "," + z);
						}

					}
				}

			}

		}

	}

	public boolean briquePresente(int x, int y, int z) {
		return this.briquePresente(octree, x, y, z);
	}

	public boolean briquePresente(Octree<EtatOctree> o, int x, int y, int z) {
		if (o.value.estPlein()) {
			return true;
		}
		if (o.value.estVide()) {
			return false;
		}
		int idx = o.value.getIdx(x, y, z);
		return this.briquePresente(o.children[idx], x, y, z);

	}

	public void ajouterBrique(Octree<EtatOctree> o, int x, int y, int z)
			throws OctreePlein {
		if (o.value.estPlein()) {
			throw new OctreePlein();

		}

		boolean estVide = o.value.estVide();
		o.value.count++;

		if (o.value.estFeuille()) {
			return;
		}
		if (o.value.estPlein()) {
			o.children = null;
			return;

		}
		if (estVide) {
			o.value.init(decor, o);
		}
		int idx = o.value.getIdx(x, y, z);
		this.ajouterBrique(o.children[idx], x, y, z);

	}

	public void supprimerBrique(int x, int y, int z) throws CouleurErreur {
		if (!this.briquePresente(x, y, z)) {
			return;
		}
		this.supprimerBrique(octree, x, y, z);
	}

	public void supprimerBrique(Octree<EtatOctree> o, int x, int y, int z)
			throws OctreeVide, CouleurErreur {

		if (o.value.estVide()) {
			throw new OctreeVide();
		}

		boolean estPlein = o.value.estPlein();
		o.value.count--;
		if (o.value.estVide()) {
			o.children = null;
			return;

		}
		if (estPlein) {
			o.value.initRec(decor, o);
			return;
		}
		int idx = o.value.getIdx(x, y, z);
		this.supprimerBrique(o.children[idx], x, y, z);

	}

}
