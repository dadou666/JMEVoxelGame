package dadou.tools.canon;

import java.awt.Color;

import com.jme.math.Vector3f;

import dadou.ElementDecor;
import dadou.VoxelLigne;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class CallbackCanonDestructionOuConstruction 
		implements CallbackCanon {
	public BrickEditor BrickEditor;
	public boolean reconstruire = false;
	public Canon canon;
	public boolean tireCube=false;


	public CallbackCanonDestructionOuConstruction(Canon canon,
			BrickEditor brickEditor) {
		this.BrickEditor = brickEditor;
		this.canon = canon;
	}

	@Override
	public boolean process(Obus obus) throws CouleurErreur {
		// TODO Auto-generated method stub
		Vector3f pos = obus.objet3D.getTranslationGlobal();
		int dimX = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimY = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimZ = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
	
		
		int x = (int) (pos.x + dimX / 2);
		int y = (int) (pos.y + dimY / 2);
		int z = (int) (pos.z + dimZ / 2);

	

		if (x < 0 || x >= dimX) {
			return false;
		}
		if (y < 0 || y >= dimY) {
			return false;
		}
		if (z < 0 || z >= dimZ) {
			return false;
		}
		if (!BrickEditor.decor.gestionCollision.briquePresente(x, y, z)) {
			return false;
		}
		this.process(x, y, z);
		return true;
	}

	@Override
	public void finish() throws CouleurErreur {
		// TODO Auto-generated method stub
		reconstruire =false;
			
		

	}

	Vector3f oldPos = new Vector3f();

	@Override
	public void saveOldPos(Vector3f oldPos) {
		// TODO Auto-generated method stub
		this.oldPos.set(oldPos);

	}

	
	public void process(int x, int y, int z) throws CouleurErreur {
		if (reconstruire) {
			return;
		}
		Color color = BrickEditor.kubeCourant();
		int dimX = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimY = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimZ = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int t = Math.abs(canon.tailleObus);
		Color oldColor = BrickEditor.decor.lireCouleur(x, y, z);
		if (ElementDecor.estVide(oldColor)) {
			return;
		}
		for (int dx = -t; dx <= t; dx++)
			for (int dy = -t; dy <= t; dy++)
				for (int dz = -t; dz <= t; dz++) {
					int px = Math.max(0, Math.min(x + dx, dimX - 1));
					int py = Math.max(0, Math.min(y + dy, dimY - 1));
					int pz = Math.max(0, Math.min(z + dz, dimZ - 1));
					int d = dx * dx + dy * dy + dz * dz;
					if (d <= t * t || tireCube) {
						oldColor = BrickEditor.decor.lireCouleur(px, py,
								pz);

						if (!ElementDecor.estVide(oldColor) && canon.tailleObus <=0 ) {

							BrickEditor.decor.removeBrique(px, py, pz);
							BrickEditor.decor.ecrireCouleur(px, py, pz, color);
							reconstruire = true;
							
						}
						if (ElementDecor.estVide(oldColor) && canon.tailleObus >0) {

							BrickEditor.decor.addBrique(px, py, pz);
							BrickEditor.decor.ecrireCouleur(px, py, pz, color);
							reconstruire = true;
					
						}

					}
				}
		BrickEditor.decor.reconstuire();

	}

}
