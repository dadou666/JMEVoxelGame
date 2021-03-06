package dadou;

import java.awt.Color;

import dadou.VoxelTexture3D.CouleurErreur;

public class FaceY extends Face {
	public int maxX;
	public int ux;
	public int uz;
	public int uy;
	public int dx;
	public int dz;

	public float getMaxX() {
		return maxX;
	}

	public float getMaxZ() {
		return maxZ;
	}

	public float getMinX() {
		return minX;
	}

	public float getMinZ() {
		return minZ;
	}

	public int maxZ;
	public int minX;
	public int minZ;
	public boolean estVide = true;

	public boolean calculer(int px, int py, int pz, int dimZ, int dimX, Poid p) throws CouleurErreur {
		minZ = dimZ;
		minX = dimX;
		maxZ = 0;
		maxX = 0;
		estVide = true;
		for (int z = 0; z < dimZ; z++) {
			for (int x = 0; x < dimX; x++) {
				int cz = pz + z;
				int cx = px + x;
				if (mc == null && !estVide(this.getBlockColor(cx, py, cz))) {
					p.ajouter(x, z);

				}
				boolean r = this.testCase(cx, py, cz);
				if (r) {
					estVide = false;
					if (z > maxZ) {
						maxZ = z;
					}
					if (x > maxX) {
						maxX = x;
					}

					if (z < minZ) {
						minZ = z;
					}
					if (x < minX) {
						minX = x;
					}
				}

			}

		}
		maxX++;
		maxZ++;
		// System.out.println("Y maxX="+maxX+" maxZ="+maxZ+" minX="+minX+"
		// minZ="+minZ+" estVide="+estVide+" py="+py);
		return estVide;
	}

	public void calculerPartition(int px, int py, int pz, int dimZ, int dimX, Poid p) throws CouleurErreur {
		ux = px;
		uy = py;
		uz = pz;
		dz = dimZ;
		dx = dimX;

		this.init();
		this.calculer();
		for (int z = 0; z < dimZ; z++) {
			for (int x = 0; x < dimX; x++) {
				int cz = pz + z;
				int cx = px + x;
				if (mc == null && !estVide(this.getBlockColor(cx, py, cz))) {
					p.ajouter(x, z);

				}
			}
		}
	}

	public boolean estVide(Color c) {
		if (!estPlanche){
			return super.estVide(c);
		}
		return c.getGreen() == 0;
	}

	public boolean testCase(int x, int y, int z) throws CouleurErreur {
		if (estPlanche) {
			return !estVide(getBlockColor(x, y, z));
		}
		int d = tex.dimY;
		if (this.mc != null) {
			d = mc.dy;
		}
		if (y == 0) {
			return !cmp(getBlockColor(x, y, z), Color.BLACK);
		}
		if (y == d) {
			return !cmp(getBlockColor(x, y - 1, z), Color.BLACK);

		}
		Color a = getBlockColor(x, y, z);
		Color b = getBlockColor(x, y - 1, z);
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
	public boolean estPlein(int x, int z) {
		// TODO Auto-generated method stub
		try {
			return this.testCase(ux + x, uy, uz + z);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int dx() {
		// TODO Auto-generated method stub
		return dx;
	}

	@Override
	public int dy() {
		// TODO Auto-generated method stub
		return dz;
	}
}
