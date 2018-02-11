package dadou.greffon;

import java.awt.Color;

public class CylindreX extends FormeAvecMin {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub

	
		float cy = dy;
		float cz = dz;
		cy = cy / 2.0f;

		cz = cz / 2.0f;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					
					float py = y;
					float pz = z;
					py = py+0.5f - cy;
				
					pz = pz+0.5f - cz;
					float d = (py * py)/(cy*cy)  + (pz * pz)/(cz*cz);
					if (d <= 1 ) {
						if ( d >=min) {
						cible[x][y][z] = color;
						} else {
							cible[x][y][z]=Color.BLACK;	
						}
						}
				}
			}

		}
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "cylindreX";
	}

}
