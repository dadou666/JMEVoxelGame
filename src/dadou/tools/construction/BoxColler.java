package dadou.tools.construction;

import java.awt.Color;

import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.ModelInstance;
import dadou.ModeleEventInterface;
import dadou.ObjetMobilePourModelInstance;
import dadou.VoxelTexture3D.CouleurErreur;

public class BoxColler extends BoxSelectionAction {
	AnnulerColler annulerColler;
	public ModelInstance modelInstance;
	public ModeleEventInterface mei;

	@Override
	public void action(int px, int py, int pz, int dx, int dy, int dz)
			throws CouleurErreur {
		// TODO Auto-generated method stub
		ModelClasse model = modelInstance.modelClasse;
		Color[][][] copie = model.copie;
		dx = model.dx;
		dy = model.dy;
		dz = model.dz;
		if (editor.selection.BoxCopie == null) {
			return;
		}
		Color[][][] annulerColors = new Color[dx][dy][dz];

		String nom = modelInstance.nomObjet;
		if (nom == null) {
			nom = editor.arbreModelClasse.nomModelInstance();
		}
		if (nom != null && (!model.estElementDecor() || mei != null)) {
			modelInstance.x = px;
			modelInstance.y = py;
			modelInstance.z = pz;
			ModelInstance mi = modelInstance;
			mi.nomObjet = nom;
			if (editor.swingEditor != null) {
				editor.decor.DecorDeBriqueData.ajouterModelInstance(nom, mi);
				editor.swingEditor.reloadTree();
			}
			modelInstance = new ModelInstance(0, 0, 0, model);
			;
			modelInstance.nomObjet = null;
		
			if (editor.espace != null) {
				editor.callbackModification.run();
				ObjetMobilePourModelInstance om = editor.espace.ajouter(mi);
				om.mei = mei;
				mei.om=om;
				om.me = new ModelEvent();
				try {

					om.mei.demarer(om, null);
					if (mei != om.mei) {
						return;
					}
				} catch (Throwable t) {

				}

			}
			mei = null;
			editor.selection.EtatBoxSelection = new DebutBoxSelection();
		

		} else {
			editor.callbackModification.run();
			annulerColler = new AnnulerColler(annulerColors, px, py, pz, dx,
					dy, dz);
			model.coller(px, py, pz, this.editor.decor, annulerColors);
			editor.decor.reconstuire();

		}

	}

	public void annuler() throws CouleurErreur {
		if (annulerColler == null) {
			return;
		}
		int px = annulerColler.x;
		int py = annulerColler.y;
		int pz = annulerColler.z;
		for (int x = 0; x < annulerColler.dx; x++) {
			for (int y = 0; y < annulerColler.dy; y++) {
				for (int z = 0; z < annulerColler.dz; z++) {
					Color color = annulerColler.colors[x][y][z];

					editor.decor.addBrique(x + px, y + py, z + pz);
					editor.decor.ecrireCouleur(x + px, y + py, z + pz, color);

				}
			}

		}

		annulerColler = null;
		editor.decor.reconstuire();

	}

	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return "Coller";
	}

}
