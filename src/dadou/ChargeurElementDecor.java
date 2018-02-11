package dadou;

import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;

public class ChargeurElementDecor {
	float px;
	float py;
	float pz;
	int x;
	int y;
	int z;

	ChargeurElementDecor suivant;

	public void charger(DecorDeBrique decor) throws CouleurErreur {
		ElementDecor et = decor.DecorDeBriqueData.getElementDecor(x, y, z);

		Vector3f pos = new Vector3f(px, py, pz);

		Octree<OctreeValeur> o = decor.octree.createLeafFor(pos);
		if (decor.g.game != null) {
			decor.init(o);
			o.value.ed = et;
			et.octree = o;
			decor.initAvecVBOMinimun(o);
		} else {
			decor.initHeadless(o);
			o.value.ed = et;
			et.octree = o;
			decor.initAvecVBOMinimun(o);
		}
		int elementTaille = decor.DecorDeBriqueData.decorInfo.elementTaille;
		if (et.nbBrique > 0) {
			for (int ux = 0; ux < elementTaille; ux++) {
				for (int uy = 0; uy < elementTaille; uy++) {
					for (int uz = 0; uz < elementTaille; uz++) {
						int sx = x * elementTaille + ux;
						int sy = y * elementTaille + uy;
						int sz = z * elementTaille + uz;
						if (!ElementDecor
								.estVide(decor.lireCouleur(sx, sy, sz))) {
							decor.gestionCollision.ajouterBrique(sx, sy, sz);

						}
					}
				}
			}
		}

	}

	public int total() {
		int n = 1;
		ChargeurElementDecor tmp = this.suivant;
		while (tmp != null) {
			n++;
			tmp = tmp.suivant;
		}
		return n;
	}

}
