package dadou.greffon;

import java.awt.Color;

public class Boite extends GreffonForme {

	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
	//	Log.print("color",color);
		for(int x=0;x < dx;x++) {
			for(int y=0;y < dy;y++) {
				for(int z=0; z < dz;z++) {
					cible[x][y][z]=color;
				}
			}
			
		}

	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "boite";
	}

}
