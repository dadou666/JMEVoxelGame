package dadou.test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.ElementDecor;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class CreerColorHabillage {
	static public Color color(Color c, float tx) {
		float b = c.getBlue();
		float r = c.getRed();
		float g = c.getGreen();
		b = (b / 255.0f) * tx;
		r = (r / 255.0f) * tx;
		g = (g / 255.0f) * tx;
		return new Color(r, g, b);

	}

	static public boolean estSurLeBord(Habillage h, int x, int y, int marge) {
		if (x < marge) {
			return true;
		}
		if (y < marge) {
			return true;
		}
		if (x > h.dim - 1 - marge) {
			return true;
		}
		if (y > h.dim - 1 - marge) {
			return true;
		}
		return false;
	}

	public static void main(String[] args)
			throws FileNotFoundException, ClassNotFoundException, IOException, PlusDeTexture, CouleurErreur {
		// TODO Auto-generated method stub
		Habillage conversion = new Habillage(32);
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");

		Map<String, NomTexture> map = new HashMap<>();

		for (ModelClasse mc : decorDeBriqueData.models.values()) {
			System.out.println("rattrapage " + mc.nomSprite3D());
			if (mc.nomHabillage == null && mc.copie != null) {
				for (int x = 0; x < mc.dx; x++) {
					for (int y = 0; y < mc.dy; y++) {
						for (int z = 0; z < mc.dz; z++) {
							Color c = mc.copie[x][y][z];
							String nom = "c" + c.getRed() + "_" + c.getGreen() + "_" + c.getBlue();
							NomTexture nt = map.get(nom);
							if (nt == null) {
								nt = conversion.creerNomTexture(nom);
								for (int px = 0; px < 32; px++) {
									for (int py = 0; py < 32; py++) {
										if (estSurLeBord(conversion, px, py, 2)) {
											conversion.SetBlock(px,py,nt.idx,color(c, 0.85f));
										} else {
											conversion.SetBlock(px,py,nt.idx, c);
										}
									}
								}
								map.put(nom, nt);
							}
							mc.copie[x][y][z] = new Color(nt.idx, nt.idx, nt.idx);

						}
					}
				}
			}
			mc.nomHabillage = "conversion.hab";

		}
		List<String> l = DecorDeBriqueDataElement.lesMondes(cheminRessources);
		for (String nomMonde : l) {
			System.out.println("rattrapage " + nomMonde);
			DecorDeBriqueDataElement de = (DecorDeBriqueDataElement) SerializeTool
					.load(new File(cheminRessources, nomMonde));
			if (de.nomHabillage == null) {
				int d = de.decorInfo.nbCube;
				for (int x = 0; x < d; x++) {
					for (int y = 0; y < d; y++) {
						for (int z = 0; z < d; z++) {
							Color c = de.lireCouleur(x, y, z);
							if (!ElementDecor.estVide(c)) {
								String nom = "c" + c.getRed() + "_" + c.getGreen() + "_" + c.getBlue();
								NomTexture nt = map.get(nom);
								if (nt == null) {
									nt = conversion.creerNomTexture(nom);
									for (int px = 0; px < 32; px++) {
										for (int py = 0; py < 32; py++) {
											if (estSurLeBord(conversion, px, py, 2)) {
												conversion.SetBlock(px,py,nt.idx, color(c, 0.85f));
											} else {
												conversion.SetBlock(px,py,nt.idx, c);
											}
										}
									}
									map.put(nom, nt);
								}
								de.ecrireCouleur(x, y, z, new Color(nt.idx, nt.idx, nt.idx));

							}
						}
					}

				}
				de.nomHabillage = "conversion.hab";
				SerializeTool.save(de, new File(cheminRessources, nomMonde));
			}

		}

		SerializeTool.save(conversion, new File(cheminRessources, "conversion.hab"));
		SerializeTool.save(decorDeBriqueData, new File(cheminRessources + "/base.bin"));

	}

}
