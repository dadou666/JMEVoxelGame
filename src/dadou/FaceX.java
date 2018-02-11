package dadou;

import java.awt.Color;

import dadou.VoxelTexture3D.CouleurErreur;

public class FaceX extends Face {
	public int minY;
	public int dz;
	public int dy;
	public int uz;
	public int ux;
	public int uy;

	public float getMinY() {
		return minY;
	}

	public float getMinZ() {
		return minZ;
	}

	public float getMaxY() {
		return maxY;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public int minZ;
	public int maxY;
	public int maxZ;

	public boolean estVide = true;

	public boolean calculer(int px, int py, int pz, int dimZ, int dimY, Poid p) throws CouleurErreur {
		minZ = dimZ;
		minY = dimY;
		maxZ = 0;
		maxY = 0;
		estVide = true;

		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				int cz = pz + z;
				int cy = py + y;
				if (mc == null && !estVide(this.getBlockColor(px, cy, cz))) {
					p.ajouter(y, z);

				}

				boolean r = this.testCase(px, cy, cz);
				if (r) {
					estVide = false;
					if (z > maxZ) {
						maxZ = z;
					}
					if (y > maxY) {
						maxY = y;
					}

					if (z < minZ) {
						minZ = z;
					}
					if (y < minY) {
						minY = y;
					}
				}

			}

		}
		maxY++;
		maxZ++;
		// System.out.println("X maxY="+maxY+" maxZ="+maxZ+" minY="+minY+"
		// minZ="+minZ+" estVide="+estVide+" px="+px);
		return estVide;
	}

	public void calculerPartition(int px, int py, int pz, int dimZ, int dimY, Poid p) throws CouleurErreur {

		this.ux = px;
		this.uy = py;
		this.uz = pz;
		this.dy = dimY;
		this.dz = dimZ;

		this.init();
		this.calculer();
		for (int z = 0; z < dimZ; z++) {
			for (int y = 0; y < dimY; y++) {
				int cz = pz + z;
				int cy = py + y;
				if (mc == null && !estVide(this.getBlockColor(px, cy, cz))) {
					p.ajouter(y, z);

				}
			}
		}

	}

	public boolean estVide(Color c) {
		if (!estPlanche) {
			return super.estVide(c);
		}
		return c.getRed() == 0;
	}

	public boolean testCase(int x, int y, int z) throws CouleurErreur {
		if (estPlanche) {
			return !estVide(getBlockColor(x, y, z));
		}
		int d = tex.dimX;
		if (this.mc != null) {
			d = mc.dx;
		}
		if (x == 0) {
			return !cmp(getBlockColor(x, y, z), Color.BLACK);
		}
		if (x == d) {
			return !cmp(getBlockColor(x - 1, y, z), Color.BLACK);

		}
		Color a = getBlockColor(x, y, z);
		Color b = getBlockColor(x - 1, y, z);
		if (cmp(a, Color.BLACK) && cmp(b, Color.BLACK)) {
			return false;
		}
		if (cmp(a, Color.BLACK)) {
			return true;
		}
		if (cmp(b, Color.BLACK)) {
			return true;
		}

		return a.getAlpha() != b.getAlpha();

	}

	public boolean cmp(Color a, Color b) {
		if (a.getBlue() != b.getBlue()) {
			return false;
		}
		if (a.getRed() != b.getRed()) {
			return false;
		}
		if (a.getGreen() != b.getGreen()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean estPlein(int y, int z) {
		// TODO Auto-generated method stub
		try {
			return this.testCase(ux, uy + y, uz + z);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int dx() {
		// TODO Auto-generated method stub
		return dy;
	}

	@Override
	public int dy() {
		// TODO Auto-generated method stub
		return dz;
	}
}
