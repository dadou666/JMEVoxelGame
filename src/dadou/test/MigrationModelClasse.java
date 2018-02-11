package dadou.test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import dadou.DecorDeBriqueData;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class MigrationModelClasse {
	public static boolean error = false;

	public static int convertir(Habillage source, Habillage cible, int idx) throws PlusDeTexture {
		if (idx == 0) {
			return 0;
		}
		String nom = source.nom(idx);
		NomTexture nt = cible.donnerNomTextures().get(nom);
		if (nt != null) {
			return nt.idx;
		}
		nt = cible.creerNomTexture(nom);
		// source.

		for (int x = 0; x < source.dim; x++) {
			for (int y = 0; y < source.dim; y++) {
				cible.SetBlock(x,y,nt.idx, source.GetBlock(x,y,idx));
			}
		}
		return nt.idx;

	}

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");
		String models[] = new String[] { "decor.porte_coulissanteZ#bleue", "decor.porte_coulissanteZ#verte_gauche",
				"decor.porte_coulissanteZ#rouge_gauche", "decor.porte_coulissanteZ#verte_droite",
				"decor.porte_coulissanteZ#rouge_droite"

		};
		String nomHabillage = "decor2.hab";

		Habillage decorHab = (Habillage) SerializeTool.load(cheminRessources + "/" + nomHabillage);
		Habillage hdHab = (Habillage) SerializeTool.load(cheminRessources + "/hd.hab");
		
		for (Map.Entry<String, ModelClasse> e : decorDeBriqueData.models.entrySet()) {
			if (e.getKey().startsWith("decor.transporteur#v1")) {

				if (e.getValue().nomHabillage != null && e.getValue().nomHabillage.equals("hd.hab")) {

					e.getValue().execute((x, y, z, c) -> {
						int red = c.getRed();
						int blue = c.getBlue();
						int green = c.getGreen();
						try {
							e.getValue().copie[x][y][z] = new Color(convertir(hdHab, decorHab, red),
									convertir(hdHab, decorHab, green), convertir(hdHab, decorHab, blue));
						} catch (Exception e1) {
							error = true;

							e1.printStackTrace();
						}

					});

					e.getValue().nomHabillage = nomHabillage;

				}
				// e.getValue().nomHabillage = "hd.hab";

			}
		}
		if (error) {
			return;
		}
		SerializeTool.save(decorHab, new File(cheminRessources, "/" + nomHabillage));
		SerializeTool.save(decorDeBriqueData, cheminRessources + "/base.bin");
		/*
		 * ModelClasse mc = decorDeBriqueData.models.get("objet.sav#model");
		 * String nom ="objet.portail"; Habillage h = (Habillage)
		 * SerializeTool.load(cheminRessources + "/hd.hab"); int dx=6; int dy=6
		 * 
		 * ; int dz=6;
		 * 
		 * for (int x = 0; x < mc.dx; x++) { for (int y = 0; y < mc.dy; y++) {
		 * for (int z = 0; z < mc.dz; z++) { // int o=
		 * h.donnerNomTextures().get("e"+i).idx; // mc.copie[x][y][z] =new
		 * Color(o,o,o);
		 * 
		 * } }
		 * 
		 * 
		 * decorDeBriqueData.models.put(mc.nom, mc); }
		 * SerializeTool.save(decorDeBriqueData, cheminRessources +
		 * "/base.bin");
		 */
	}

}
