package dadou.greffon;

import java.awt.Color;

import javax.swing.JFrame;

import dadou.param.CubeVariableParam;

public class Brique extends GreffonSelection {

	public int vx = 0;
	public int vy = 0;
	public int vz = 0;

	@Override
	public void exec(int x, int y, int z, int dx, int dy, int dz, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub

		for (int ux = -vx; ux <= vx; ux++) {
			for (int uy = -vy; uy <= vy; uy++) {
				for (int uz = -vz; uz <= vz; uz++) {
					int px = x + ux;
					int py = y + uy;
					int pz = z + uz;
					if (px >= 0 && px < dx) {
						if (py >= 0 && py < dy) {
							if (pz >= 0 && pz < dz) {
								cible[px][py][pz] = color;
							}
						}
					}

				}
			}
		}

	}
	public String toString() {
		return "brique ("+vx+","+vy+","+vz+")";
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "brique";
	}

	public void initGreffon(JFrame parent) {
		new CubeVariableParam(parent,this);

	}

}
