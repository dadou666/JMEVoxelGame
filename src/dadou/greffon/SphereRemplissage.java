package dadou.greffon;

import java.awt.Color;

public class SphereRemplissage extends GreffonSelection {
	boolean vider= true;
	public SphereRemplissage(boolean vider) {
		this.vider= vider;
	}
	@Override
	public void exec(int ux, int uy, int uz, int dx, int dy, int dz, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		float cx = ((float)dx)/2.0f;
		float cy = ((float)dy)/2.0f;
		float cz = ((float)dz)/2.0f;
		for (int x = ux; x <ux+ dx; x++) {
			for (int y = uy; y <uy+ dy; y++) {
				for (int z = uz; z <uz+ dz; z++) {
					float px = x;
					float py = y;
					float pz = z;
					px = px+0.5f - (cx+(float)ux);
					py = py+0.5f - (cy+(float)uy);
					pz = pz+0.5f - (cz+(float)uz);
					float d = (px * px)/(cx*cx) + (py * py)/(cy*cy) + (pz * pz)/(cz*cz);
					if (d <1) {

						cible[x][y][z] = color;
					} else {
						if (vider) {
						cible[x][y][z] = Color.BLACK; }
					}
				}
			}

		}
	}
	public String toString() {
		return "sphere";
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		if (vider) {
			return "sphere avec vidage";
		}
		return "sphere";
	}

}
