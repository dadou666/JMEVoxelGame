package dadou.greffon;

import java.awt.Color;

public class TunelX extends GreffonForme {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
	
			for(int x=0;x< dx;x++) {
				for(int y=0;y<dy;y++) {
			
					for(int z=0;z<dz;z++) {
						cible[x][y][z] = Color.BLACK;
						if (z==0 || z==dz-1 || y==0 || y==dy-1) {
							cible[x][y][z]=color;
						}
					}
				}
			}
		
		
		
		
		

	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "TunelX";
	}

}
