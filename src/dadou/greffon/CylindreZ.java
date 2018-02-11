package dadou.greffon;

import java.awt.Color;

public class CylindreZ extends FormeAvecMin {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		float cx = dx;
		float cy = dy;

		cx = cx / 2.0f;

		cy = cy / 2.0f;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					float px = x;
					float py = y;
					
					px = px+0.5f - cx;
				
					py = py+0.5f - cy;
					float d = (px * px)/(cx*cx)  + (py * py)/(cy*cy);
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
		return "cylindreZ";
	}

}
