package dadou.greffon;

import java.awt.Color;

import javax.swing.JFrame;

import dadou.param.CercleParam;

public class Cercle extends GreffonSelection {
	public String axe;
	public int rayonInterne;
	public int rayonExterne;

	@Override
	public void exec(int x, int y, int z, int dx, int dy, int dz, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub
		int rayon = rayonExterne;
		if (axe.equals("Rouge")) {
			
			for (int py = -rayon; py <= rayon; py++) {
				for (int pz = -rayon; pz <= rayon; pz++) {
					float u = py * py + pz * pz;
					if (u <= rayon * rayon && u >= rayonInterne*rayonInterne) {
						int ay = py + y;
						int az = pz + z;
						int ax = x;
						if (ay >= 0 && ay < dy) {
							if (az >= 0 && az < dz) {
								cible[ax][ay][az] = color;

							}
						}
					}

				}

			}

		}

		if (axe.equals("Vert")) {
			for (int px = -rayon; px <= rayon; px++) {
				for (int pz = -rayon; pz <= rayon; pz++) {
					float u = px * px + pz * pz;
					if (u <= rayon * rayon && u >= rayonInterne*rayonInterne) {
						int ay = y;
						int az = pz + z;
						int ax = px + x;
						if (ax >= 0 && ax < dx) {
							if (az >= 0 && az < dz) {
								cible[ax][ay][az] = color;

							}
						}
					}

				}

			}

		}

		if (axe.equals("Bleue")) {
			for (int px = -rayon; px <= rayon; px++) {
				for (int py = -rayon; py <= rayon; py++) {
					float u = px * px + py * py;
					if (u <= rayon * rayon && u >= rayonInterne*rayonInterne) {
						int ay = py + y;
						int az = z;
						int ax = px + x;
						if (ax >= 0 && ax < dx) {
							if (ay >= 0 && ay < dy) {
								cible[ax][ay][az] = color;

							}
						}
					}

				}

			}

		}

	}
	public String toString() {
		return "cercle("+this.axe+":"+this.rayonExterne+","+this.rayonInterne+")";
	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "cercle";
	}

	public void initGreffon(JFrame parent) {
		new CercleParam(parent,this);
	}

}
