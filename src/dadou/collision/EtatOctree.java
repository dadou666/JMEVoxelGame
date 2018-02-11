package dadou.collision;

import java.awt.Color;
import java.io.Serializable;

import com.jme.math.Vector3f;

import dadou.DecorDeBrique;
import dadou.ElementDecor;
import dadou.Octree;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;

public class EtatOctree implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8588930562229311216L;
	public int x, y, z;
	public int dim;
	public int max;
	public int count;
	public Object marque = null;

	public EtatOctree(int x, int y, int z, int dim) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dim = dim;
		max = dim * dim * dim;
		count = 0;
	}

	public boolean estVide() {

		return count == 0;

	}

	public boolean estPlein() {
		return count == max;
	}

	public boolean estFeuille() {
		return dim == 1;
	}

	public boolean init(DecorDeBrique decor,Octree<EtatOctree> octree)  {

		if (estFeuille()) {
			return false;
		}
		if (estPlein()) {
			return false;
		}
		if (estVide()) {
			return false;

		}
		int d[] = { 0, dim / 2 };
		octree.init();
		for (int dx : d) {
			for (int dy : d) {
				for (int dz : d) {
					int px = x + dx;
					int py = y + dy;
					int pz = z + dz;
					int idx = this.getIdx(px, py, pz);
					EtatOctree eo =new EtatOctree(px,py,pz,dim/2);
					octree.children[idx].value = eo;
					//eo.init(tex,octree.children[idx]);

				}
			}
		}
		return true;

	}
	public void initRec(DecorDeBrique decor,Octree<EtatOctree> octree) throws CouleurErreur {
		this.computeCount(decor);
		if (this.init(decor, octree)) {
		
		for(Octree<EtatOctree> o:octree.children) {
			o.value.initRec(decor, o);
			
		}}

	}
	public int getIdx(int px, int py, int pz) {
		int idx = 0;
		int cx = x + dim / 2;
		int cy = y + dim / 2;
		int cz = z + dim / 2;
		if (px >= cx) {
			idx += 1;
		}
		if (py >= cy) {
			idx += 2;
		}
		if (pz >= cz) {
			idx += 4;
		}
		return idx;

	}

	public void computeCount(DecorDeBrique decor) throws CouleurErreur {
		count = 0;
		for (int px = x; px < x + dim; px++) {
			for (int py = y; py < y + dim; py++) {
				for (int pz = z; pz < z + dim; pz++) {
					Color color = decor.lireCouleur(px, py, pz);
					if (!ElementDecor.estVide(color)) {
						count++;
					}
				}

			}
		}

	}
}
