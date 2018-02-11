package dadou.tools.construction;

import java.awt.Color;

import dadou.ModelClasse;
import dadou.ModelClasseDecor;
import dadou.VoxelTexture3D.CouleurErreur;


public class BoxCopie extends BoxSelectionAction {

	public Color[][][] copie;
	public boolean estDecor = true;

	public int dx;
	public int dy;
	public int dz;

	@Override
	public void action(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) throws CouleurErreur {
		// TODO Auto-generated method stub
		dx = maxX - minX;
		dy = maxY - minY;
		dz = maxZ - minZ;
		copie = new Color[dx][dy][dz];
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					copie[x][y][z] = editor.decor.lireCouleur(x + minX, y + minY, z + minZ);
				}

			}
		}
		ModelClasse model;
		if (estDecor) {
			model =new ModelClasseDecor();
		} else {
			model = new ModelClasse();
		}
		if (this.editor.choixModelClasse != null) {
			this.editor.choixModelClasse.modifierListe = true;
		}
		model.copie = copie;
		model.dx = dx;
		model.dy = dy;
		model.dz = dz;
		model.initEstVide();
		if (!model.estVide && editor.DialogDemandeNomClasse.supprimerEspaces ) {
			model.supprimerEspaces();

		}
		String nomHabillage = editor.decor.DecorDeBriqueData.nomHabillage;
		try {
			model.initBuffer(editor.game, editor.donnerHabillage(nomHabillage));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.nomHabillage = nomHabillage;
		model.nom = editor.DialogDemandeNomClasse.nomModel;
		editor.decorDeBriqueData.ajouter(model, editor.DialogDemandeNomClasse.nomModel);
		try {
			editor.choixModelClasse = new ChoixModelClasse(editor, editor.mondeInterface.donnerFBO(128, 128),
					nomHabillage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (editor.swingEditor != null) {
		editor.swingEditor.reloadTree(); }
		editor.selection.BoxSelectionAction = null;
		editor.selection.setBoxSelection(false);

		editor.initMenuSelection();

	}

	@Override
	public String getMode() {
		// TODO Auto-generated method stub

		return "Copie";
	}

}
