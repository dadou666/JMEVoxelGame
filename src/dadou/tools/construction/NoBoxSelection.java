package dadou.tools.construction;

import java.awt.Color;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.ElementDecor;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class NoBoxSelection extends EtatBoxSelection {
	public void dessiner(Selection selection, Camera cam) {
		selection.shader.use();
		selection.shader.glUniformfARB("dx", 1);
		selection.shader.glUniformfARB("dy", 1);
		selection.shader.glUniformfARB("dz", 1);
		// selection.shader.glUniformfARB("size", 1);
	//	selection.shader.glUniformfARB("color", 0.0f, 0.0f, 0.0f, 1.0f);
		selection.camSelection.dessiner(cam);

	}

	public void setBlock(Selection selection, Vector3f pos, Color color) throws CouleurErreur {
		int dim = selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int px = (int) (pos.x + dim / 2);
		int py = (int) (pos.y + dim / 2);
		int pz = (int) (pos.z + dim / 2);
		// System.out.println( " px="+px+" py="+py+" pz="+pz);
		int tmpX = Math.max(Math.min(px, dim - 1), 0);
		int tmpY = Math.max(Math.min(py, dim - 1), 0);
		int tmpZ = Math.max(Math.min(pz, dim - 1), 0);
		boolean r = tmpX != px || tmpY != py || tmpZ != pz;
		if (r) {
			return;
		}
		// System.out.println( " updateBlock ");

		if (ElementDecor.estVide(color)) {
			selection.BrickEditor.decor.removeBrique(px, py, pz);
		} else {
			selection.BrickEditor.decor.addBrique(px, py, pz);
		}
		if (selection.BrickEditor.menuTexture.selected) {
			Color oldColor = selection.BrickEditor.decor.lireCouleur(tmpX, tmpY, tmpZ);
		
			if (selection.BrickEditor.cameraSurX()!=0) {
				color = new Color(color.getRed(), oldColor.getGreen(), oldColor.getBlue());
			} else
			if (selection.BrickEditor.cameraSurY()!=0) {
				color = new Color(oldColor.getRed(), color.getGreen(), oldColor.getBlue());
			} else
			if (selection.BrickEditor.cameraSurZ()!=0) {
				color = new Color(oldColor.getRed(), oldColor.getGreen(), color.getBlue());
			} else {
				color = new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue());
			}

		}
		selection.BrickEditor.decor.ecrireCouleur(tmpX, tmpY, tmpZ, color);
		selection.BrickEditor.decor.reconstuire();
	}
}
