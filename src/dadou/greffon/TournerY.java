package dadou.greffon;

import java.awt.Color;

public class TournerY extends GreffonSelection {

	@Override
	public void exec(int x, int y, int z, int dx, int dy, int dz, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		Color copie [][][]= new Color[dx][dy][dz];
		for(int ux=0;ux < dx;ux++) {
			for(int uy=0;uy < dy;uy++) {
				for(int uz=0;uz < dz;uz++) {
					copie[ux][uy][uz] = cible[ux][uy][uz];
				}	
			}	
		}
		for(int ux=0;ux < dx;ux++) {
			for(int uy=0;uy < dy;uy++) {
				for(int uz=0;uz < dz;uz++) {
					cible[ux][uy][dz-uz-1] = copie[uz][uy][ux];
				}	
			}	
		}

	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "tournerY";
	}

}
