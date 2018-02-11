package dadou.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;

import dadou.DecorDeBriqueData;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class ModifierModelClasse {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");
        ModelClasse mc = decorDeBriqueData.models.get("objet.sav#model");
		String nom ="objet.portail";
		Habillage h = (Habillage) SerializeTool.load(cheminRessources + "/hd.hab");
		int dx=6;
		int dy=6
				
			;
		int dz=6;
	
			for (int x = 0; x < mc.dx; x++) {
				for (int y = 0; y < mc.dy; y++) {
					for (int z = 0; z < mc.dz; z++) {
					//	int o= h.donnerNomTextures().get("e"+i).idx;
					//	mc.copie[x][y][z] =new Color(o,o,o);

					}
				}

			
			decorDeBriqueData.models.put(mc.nom, mc);
		}
		SerializeTool.save(decorDeBriqueData, cheminRessources + "/base.bin");
	}

}
