package dadou.greffon;

import dadou.ModelClasse;

public class ZoneGreffon {
	public int fx, fy, fz;
	public int lx, ly, lz;
	public ModelClasse mc;
	public boolean modifierValeur = false;

	public int dx() {

		int minX = Math.min(lx, fx);
		int maxX = Math.max(lx, fx);

		return maxX - minX+1;

	}

	public int dy() {

		int minY = Math.min(ly, fy);
		int maxY = Math.max(ly, fy);

		return maxY - minY+1;

	}

	public int dz() {

		int minZ = Math.min(lz, fz);
		int maxZ = Math.max(lz, fz);

		return maxZ - minZ+1;

	}

	public int x() {
		return Math.min(lx, fx);
	}

	public int y() {
		return Math.min(ly, fy);
	}

	public int z() {
		return Math.min(lz, fz);
	}

}
