package dadou.tools.construction;

import java.awt.Color;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Kube;
import dadou.Log;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.greffon.GreffonForme;

public class AfficherBoxSelection extends EtatBoxSelection {
	public int dx;
	public int dy;
	public int dz;
	public float echelle;
	public Objet3D objetColler;
	public ModelClasse mc = null;

	public AfficherBoxSelection(int dx, int dy, int dz, float echelle,
			ModelClasse mc) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.echelle = echelle;
		if (mc != null) {
			objetColler = new Objet3D();
			objetColler.brique = mc.brique;
			mc.brique.echelle = echelle;
		}

	}

	public void dessiner(Selection selection, Camera cam) {
		selection.shader.use();
		selection.shader.glUniformfARB("dx", dx * echelle);
		selection.shader.glUniformfARB("dy", dy * echelle);
		selection.shader.glUniformfARB("dz", dz * echelle);
		// selection.shader.glUniformfARB("size", 1);
		// selection.shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);
		selection.camSelection.translation(((float) dx * (1 - echelle)) / 2.0f,
				((float) dy * (1 - echelle)) / 2.0f,
				((float) dz * (1 - echelle)) / 2.0f);

		if (objetColler != null) {
			objetColler.positionToZero();
			objetColler.translation(selection.camSelection
					.getTranslationGlobal());
			objetColler.dessiner(cam);
		}
		selection.camSelection.dessiner(cam);
		selection.shader.use();
		selection.shader.glUniformfARB("dx", 1.0f);
		selection.shader.glUniformfARB("dy", 1.0f);
		selection.shader.glUniformfARB("dz", 1.0f);
		// selection.shader.glUniformfARB("size", 1);
		// selection.shader.glUniformfARB("color", 1.0f, 1.0f, 0.0f, 0.0f);

		selection.boxCamSelection.dessiner(cam);

	}

	Kube cube = new Kube();

	public void setBlock(Selection selection, Vector3f pos, Color color)
			throws CouleurErreur {
		try {
			selection.BrickEditor.decor.getCube(pos, cube);
			int dx = selection.BoxCopie.dx;
			int dy = selection.BoxCopie.dy;
			int dz = selection.BoxCopie.dz;
			selection.BoxSelectionAction.action(cube.x, cube.y, cube.z, dx, dy,
					dz);
		} catch (CouleurErreur c) {
			Log.print(" erreur ");

		}

	}

}
