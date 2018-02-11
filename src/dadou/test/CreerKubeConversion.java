package dadou.test;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import dadou.Habillage;
import dadou.NomTexture;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class CreerKubeConversion {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		Habillage h = (Habillage) SerializeTool.load(cheminRessources + "/conversion.hab");
		for(NomTexture nt:h.noms()) {
			h.valeurs.put(nt.nom, new Color(nt.idx,nt.idx,nt.idx));
		}
		SerializeTool.save(h, new File(cheminRessources, "conversion.hab"));
	}

}
