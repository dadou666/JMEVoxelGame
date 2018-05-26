package terrain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Terrain implements Serializable {
	public int grille[][];

	public int maxValue;
	public int dim;
	public int nbSimplification = 0;

	public java.util.Random random = new java.util.Random();

	public void simplifier() {
		for (int h = 0; h < maxValue; h++) {
			for (int x = 1; x < dim - 2; x++) {
				for (int y = 1; y < dim - 2; y++) {
					if (grille[x][y] == h) {
						grille[x][y] = this.valeurSimplification(x, y);
					}
				}
			}
		}

	}

	public int valeurSimplification(int ux, int uy) {
		int v = grille[ux][uy];
		int np = 0;
		int nm = 0;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				if (dx != 0 || dy != 0) {
					int px = ux + dx;
					int py = uy + dy;
					if (v == grille[px][py]-1) {
						np++;
					}
					if (v  == grille[px][py]+1) {
						nm++;
					}
				}
			}
		}
		if (nm >= 7 && np == 0) {
			nbSimplification++;
			// return v-1;
		}
		if (np >= 7 && nm == 0) {
			nbSimplification++;
			return v + 1;
		}
		return v;

	}

	public Terrain(int n, int maxValue) {
		this.maxValue = maxValue;


		grille = new int[n][n];
		this.dim = n;
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				grille[x][y] = 0;
			}
		}

	}

	public int min(int x, int y) {

		Integer r = null;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				int px = x + dx;
				int py = y + dy;
				if (px > 0 && py > 0 && px < dim && py < dim) {
					if (r == null) {
						r = grille[px][py];
					} else {
						if (grille[px][py] < r) {
							r = grille[px][py];
						}

					}
				}
			}
		}
		return r;

	}

	public boolean canModify(Pos pos) {
		int value = grille[pos.x][pos.y];
		if (value >= maxValue) {
			return false;
		}

		return (value - min(pos.x, pos.y)) < 1;

	}

	public int idx(int max) {

		return random.nextInt(max);

	}

	public void modify(float tx) {

		int hMax = 0;
		float total = dim * dim;
		float nb = 0.0f;

		List<Pos> l = new ArrayList<>();
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				l.add(new Pos(y, x));
			}
		}
		while (true) {

			int idx = idx(l.size());
			// System.out.println(" idx="+idx);
			Pos pos = l.get(idx);

			if (this.canModify(pos)) {
				int value = grille[pos.x][pos.y] + 1;
				if (value <= maxValue) {

					grille[pos.x][pos.y] = value;
					l.add(pos);

					if (value > hMax) {
						hMax = value;
						System.out.println(" hMax=" + hMax);
					}
					if (value == maxValue) {
						nb = nb + 1.0f;
						System.out.println(" tx=" + (nb / total));
					}
					if (nb / total >= tx) {
						return;
					}

				}

			}
		}

	}

	public void modify() {

		int hMax = 0;

		List<Pos> l = new ArrayList<>();
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				l.add(new Pos(y, x));
			}
		}
		while (true) {

			int idx = idx(l.size());
			// System.out.println(" idx="+idx);
			Pos pos = l.get(idx);

			if (this.canModify(pos)) {
				int value = grille[pos.x][pos.y] + 1;
				if (value <= maxValue) {

					grille[pos.x][pos.y] = value;
					l.add(pos);

					if (value > hMax) {
						hMax = value;
						System.out.println(" hMax=" + hMax);
					}
					if (value == maxValue) {
						return;
					}

				}

			}
		}

	}

	public void test() {
		int ne = 0;
		for (int x = 0; x < dim - 2; x++) {
			for (int y = 0; y < dim - 2; y++) {
				int min = this.min(x + 1, y + 1);
				int val = grille[x + 1][y + 1];
				if (val - min > 1) {
					ne++;
				}

			}
		}
		System.out.println(" erreur " + ne);

	}

}
