package dadou.tools.construction;

import java.awt.Color;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class DebutBoxSelection extends EtatBoxSelection {
	
	public void dessiner(Selection selection, Camera cam){
		selection.shader.use();
		selection.shader.glUniformfARB("dx", 1);
		selection.shader.glUniformfARB("dy", 1);
		selection.shader.glUniformfARB("dz", 1);
		//selection.shader.glUniformfARB("size", 1);
		Color color = Color.BLACK;
	//	selection.shader.glUniformfARB("color",color.getRed(), color.getGreen(), color.getBlue(), 0.0f);
		selection.camSelection.dessiner(cam);
		
	}
	public String getMode(Selection selection) {
		return selection.BoxSelectionAction.getMode();
		
	}
	public void setBlock(Selection selection,Vector3f pos, Color color){
		int dim = selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int px = (int) (pos.x + dim / 2);
		int py = (int) (pos.y + dim / 2);
		int pz = (int) (pos.z + dim / 2);
		// System.out.println( " px="+px+" py="+py+" pz="+pz);
		int tmpX = Math.max(Math.min(px, dim- 1), 0);
		int tmpY = Math.max(Math.min(py, dim - 1), 0);
		int tmpZ = Math.max(Math.min(pz, dim - 1), 0);
		boolean r = tmpX != px || tmpY != py || tmpZ != pz;
		if (r) {
			return;
		}
		FinBoxSelection FinBoxSelection = new FinBoxSelection();
		FinBoxSelection.px=tmpX;
		FinBoxSelection.py=tmpY;
		FinBoxSelection.pz=tmpZ;
		
		FinBoxSelection.wx=x;
		FinBoxSelection.wy=y;
		FinBoxSelection.wz=z;
		FinBoxSelection.VoxelDessinerLigne = new VoxelDessinerLigneRec();
		FinBoxSelection.VoxelDessinerLigne.b = selection.BrickEditor;
		
		selection.EtatBoxSelection = FinBoxSelection;
		
		
	}
}
