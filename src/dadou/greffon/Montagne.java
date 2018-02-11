package dadou.greffon;

import java.awt.Color;

import dadou.Habillage;
import dadou.Log;
import dadou.tools.BrickEditor;
import dadou.tools.BrickEditorSwing;
import dadou.tools.DialogCreationMontagne;
import lanceur.UI;

public class Montagne extends GreffonForme {
public	BrickEditor bes;
public boolean utiliseKubeSelection = false;

	public void init(BrickEditorSwing bes) {
		// min = 0.0f;
		this.bes = bes.comportement;
		
		new DialogCreationMontagne(this, bes.frame, bes);

	}


	@Override
	public void exec(int dx, int dy, int dz, Color[][][] source, Color[][][] cible, Color color) {
		// TODO Auto-generated method stub

		float cx = dx;
		float cy = dy;
		float cz = dz;
		cx = cx / 2.0f;

		cz = cz / 2.0f;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					float px = x;
					float py = y;
					float pz = z;
					px = px + 0.5f - cx;

					pz = pz + 0.5f - cz;
					float d = (px * px) / (cx * cx) + (py * py) / (cy * cy) + (pz * pz) / (cz * cz);
					if (d <= 1 ) {

						cible[x][y][z] = color;
					}
				}
			}

		}
		Habillage h = bes.donnerHabillage(bes.decor.DecorDeBriqueData.nomHabillage);
		Color kubeAutre = h.valeurPourHabillage(bes.decor.DecorDeBriqueData.nomKubeAutre);

		Color kubeSommet = h.valeurPourHabillage(bes.decor.DecorDeBriqueData.nomKubeSommet);
		if (this.utiliseKubeSelection) {
			kubeSommet=color;
			kubeAutre=color;
		}

		for (int x = 0; x < dx; x++) {
			for (int z = 0; z < dz; z++) {
				for (int y = 0; y < dy; y++) {
					if (cible[x][y][z] != null) {
						if (y == dy - 1) {
							cible[x][y][z] = kubeSommet;
						} else {
							if (cible[x][y + 1][z] != null) {
								cible[x][y][z] = kubeAutre;
							} else {
								cible[x][y][z] = kubeSommet;
							}

						}

					}
				}
			}
		}

	}

	@Override
	public String nom() {
		// TODO Auto-generated method stub
		return "Montagne";
	}

}
