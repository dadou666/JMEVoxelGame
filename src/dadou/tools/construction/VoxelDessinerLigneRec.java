package dadou.tools.construction;

import java.awt.Color;

import dadou.ElementDecor;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;



public class VoxelDessinerLigneRec extends dadou.VoxelLigneRec {
	public BrickEditor b;
	public Color color;
	public boolean dessinerLigne() {
		return b.selection.dessinerLigne;
	}
	@Override
	public void process(int x, int y, int z) throws CouleurErreur {
		// TODO Auto-generated method stub
	//	System.out.println(" x="+x+" y="+y+" z="+z);
		if (ElementDecor.estVide(color)) {
			b.decor.removeBrique(x, y, z);
		} else {
			b.decor.addBrique(x, y, z);
		}
		b.decor.ecrireCouleur(x, y, z, color);
	}

}
