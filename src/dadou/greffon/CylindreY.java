package dadou.greffon;

import java.awt.Color;

public class CylindreY extends FormeAvecMin {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		float cx = dx;

		float cz = dz;
		cx = cx / 2.0f;

		cz = cz / 2.0f;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					float px = x;

					float pz = z;
					px = px + 0.5f - cx;

					pz = pz + 0.5f - cz;
					float d = (px * px) / (cx * cx) + (pz * pz) / (cz * cz);
					if (d <= 1) {
						if (d >= min) {
							cible[x][y][z] = color;
						} else {
							cible[x][y][z] = Color.BLACK;
						}
					}
				}
			}

		}
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "cylindreY";
	}

}
