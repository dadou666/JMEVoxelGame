package terrain;

public class TerrainSin extends Terrain {

	public TerrainSin(int n, int deltatMax, int maxValue,float factor) {
		super(n, deltatMax, maxValue);
		this.factor = factor;
	}

	double t = 0.0f;
	double factor;

	public int idx(int max) {
		double dmax = max;
		double r = (dmax / 2.0) * Math.sin(factor * t) + (dmax / 2.0);
	//	System.out.println("r=" + r+" t ="+t);
		t = t + 1.0;
		int ri= Math.min((int) r,max-1);
		return ri;

	}
}
