package dadou;

import dadou.VoxelTexture3D.CouleurErreur;

public abstract class VoxelLigne {
	int d[];
	int i[];
	int f[];
	int o[];

	public VoxelLigne() {
	

	}

	public void init(int i[], int f[]) {
		this.i = i;
		this.f = f;
		d = new int[i.length];
		 o = new int[i.length];
		for (int u = 0; u < i.length; u++) {
			int h = f[u] - i[u];
			d[u] = Math.abs(h);
			if (h < 0) {
				o[u] = -1;

			} else {
				o[u] = 1;
			}
		}

	}

	public void _process(int t[]) throws CouleurErreur {
		this.process(o[0] *t[0] +  i[0], o[1] *t[1] +  i[1], o[2] *t[2] +  i[2]);

	}

public 	abstract void process(int x, int y, int z) throws CouleurErreur;

	public void execute() throws CouleurErreur {
		int iMax;
		int max;
		int a;
		int b;

		iMax = 0;
		max = d[0];
		a = 1;
		b = 2;
		if (d[1] > max) {
			iMax = 1;
			max = d[1];
			b = 2;
			a = 0;

		}
		if (d[2] > max) {
			iMax = 2;
			max = d[2];
			b = 1;
			a = 0;
		}

		int r[] = new int[] { 0, 0, 0 };

		int e = 2 * r[a] - max;
		int f = 2 * r[b] - max;
		this._process(r);
		for (int q = 1; q <= max; q++) {
			r[iMax] = q;
			if (e > 0) {
				r[a] = r[a] + 1;

				e = e + (2 * d[a] - 2 * max);
			} else {
				e = e + (2 * d[a]);
			}

			if (f > 0) {
				r[b] = r[b] + 1;

				f = f + (2 * d[b] - 2 * max);
			} else {
				f = f + (2 * d[b]);
			}
			this._process(r);

		}

	}

}
