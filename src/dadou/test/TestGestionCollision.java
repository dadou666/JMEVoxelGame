package dadou.test;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dadou.DecorDeBrique;
import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.ElementDecor;
import dadou.Game;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.collision.OctreePlein;
import dadou.collision.OctreeVide;
import dadou.tools.BrickEditor;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class TestGestionCollision {

	public static void main(String[] args) throws FileNotFoundException,
			ClassNotFoundException, IOException, CouleurErreur, OctreeVide, OctreePlein {
		// TODO Auto-generated method stub
		// Game g = new Game();
		String cheminRessources = OutilFichier.lireFichier(
				"fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool
				.load(cheminRessources + "/base.bin");
		DecorDeBriqueDataElement e = DecorDeBriqueDataElement
				.charger(cheminRessources + "/"
						+ decorDeBriqueData.mondeCourant);
		

		GestionCollision gc=new GestionCollision();/* = new GestionCollision(tex,
				BrickEditor.niveauCollision, 1);*/
	
	long debut = System.currentTimeMillis();
		for (int x = 0; x < e.decorInfo.nbCube; x++) {
			for (int y = 0; y <  e.decorInfo.nbCube; y++) {
				for (int z = 0; z < e.decorInfo.nbCube; z++) {
					if (!ElementDecor.estVide(e.lireCouleur(x, y, z))) {
						gc.ajouterBrique(x, y, z);
						
					}
				}
			}

		}
		System.out.println(" duree 1="+((System.currentTimeMillis()-debut)/1000));
		
	  int dim = e.decorInfo.nbCube;
	
		for (int x = 0; x < dim; x++) {
			for (int y = 0; y < dim; y++) {
				for (int z = 0; z < dim; z++) {
					Color color = e.lireCouleur(x, y, z);
					if (ElementDecor.estVide(color)) {
						if (gc.briquePresente(x, y, z)) {
							throw new Error(" Error " + x + "," + y + "," + z);
						}

					} else {
						if (!gc.briquePresente(x, y, z)) {
							throw new Error(" Error " + x + "," + y + "," + z);
						}

					}
				}

			}

		}
		
		DecorDeBrique decor = new DecorDeBrique(e);
		 debut = System.currentTimeMillis();
		gc = new GestionCollision(decor,BrickEditor.niveauCollision, 1);
		System.out.println(" duree 2="+((System.currentTimeMillis()-debut)/1000));
		gc.verifierOctree(dim);
	
		
	}

}
