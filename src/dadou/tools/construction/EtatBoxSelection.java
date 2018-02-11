package dadou.tools.construction;

import java.awt.Color;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;

public class EtatBoxSelection {
	public int x;
	public int y;
	public int z;
	public void dessiner(Selection selection, Camera cam){
		
		
	}
	public void setPosition(Selection selection,Vector3f pos) {
		selection.camSelection.positionToZero();
		int dim =selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dx= dim/ 2;
		int dy =dim / 2;
		int dz=dim / 2;
		 x = (int) (pos.x + dx) - (dx);
		 y = (int) (pos.y + dy) - (dy);
		 z = (int) (pos.z + dz) - (dz);
		
		 selection.BrickEditor.info.infoText=" "+(x+dx)+","+(y+dy)+","+(z+dz);
		 selection.camSelection.translation(x, y, z);
		//selection.BrickEditor.initCurrentModelName(pos);
	

	}
	public void setBlock(Selection selection,Vector3f pos, Color color) throws CouleurErreur{
		
	}
	public String getMode(Selection selection) {
		return "Selection";
	}
}
