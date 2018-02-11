package dadou.greffon;

import java.awt.Color;

public class Sphere extends GreffonForme {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		
		float cx = dx;
		float cy = dy;
		float cz = dz;
		cx=cx/2.0f;
		cy=cy/2.0f;
		cz=cz/2.0f;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					float px = x;
					float py = y;
					float pz = z;
					px = px+0.5f - cx;
					py = py+0.5f - cy;
					pz = pz+0.5f - cz;
					float d = (px * px)/(cx*cx) + (py * py)/(cy*cy) + (pz * pz)/(cz*cz);
					if (d <= 1 ) {

						cible[x][y][z] = color;
					}
				}
			}

		}
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "Sphere";
	}

}
