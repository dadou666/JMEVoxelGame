package dadou.tools.construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Mouse;

import dadou.Button;
import dadou.ModelClasse;
import dadou.ModelInstance;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.GestionListConfig;
import dadou.ihm.GestionListe;
import dadou.ihm.GestionListeControlleur;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.ListElement;
import dadou.ihm.Widget;
import dadou.tools.EtatBrickEditor;

public class SelectionGroupeModelClasse extends EtatBrickEditor implements
		GestionListeControlleur {

	Button button;
	IHM ihm;
	GestionListe gestionListe;
	public EtatBrickEditor old;

	List<String> calculerGroupes(dadou.tools.BrickEditor be) {
		Set<String> groupes = new HashSet<>();
		for (String s : be.decorDeBriqueData.models.keySet()) {
			String path[] = s.split("\\.");
			if (path.length == 1) {
				groupes.add("");
			} else {
				groupes.add(path[0] + ".");
			}

		}
		List<String> r = new ArrayList<String>();
		r.addAll(groupes);
		return r;
	}

	public SelectionGroupeModelClasse(dadou.tools.BrickEditor be) {
		this.BrickEditor = be;

		button = new Button(0);
		ihm = be.game.nouvelleIHM();

		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);

		GestionListConfig config = new GestionListConfig();
		config.supprimer = true;
		gestionListe = new GestionListe(this, this.calculerGroupes(be), config,
				ihm, 128, 32, 5);

		ihm.end();
		ihm.end();

	}

	public void initListGroupe() {

		gestionListe.setModel(this.calculerGroupes(this.BrickEditor));

	}

	public void gerer() throws CouleurErreur {

		int x = Mouse.getX();
		int y = Mouse.getY();
		if (button.isPressed()) {
			Widget w = ihm.getWidgetAt(x, y);

			gestionListe.processClick(w);
		}
		gestionListe.processkeyboard();
		ihm.dessiner(BrickEditor.game.shaderWidget);

	}

	@Override
	public void execute(GestionListe gestionListe, boolean valider) {
		// TODO Auto-generated method stub
		if (!valider) {
	
			BrickEditor.processMenuKey = true;
			BrickEditor.etat = old;
			return;

		}
		for (String nomGroupe : this.gestionListe.listeSupprimer) {
			for (String nomModel : BrickEditor.selectionModel
					.calculerListeModel(nomGroupe)) {
				this.BrickEditor.decorDeBriqueData.models.remove(nomModel);
			}
			this.BrickEditor.espace.reset();
			try {
				this.BrickEditor.decor
						.initialiserModelInstances(this.BrickEditor);
			} catch (CouleurErreur e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BrickEditor.etat = old;
			BrickEditor.processMenuKey = true;
			return;

		}

		ListElement elt = this.gestionListe.getSelection();

		if (elt == null) {

			BrickEditor.etat = old;
			BrickEditor.processMenuKey = true;
			return;
		}

		BrickEditor.etat = this.BrickEditor.selectionModel;
		this.BrickEditor.selectionModel.initListModel(elt.value);
		this.BrickEditor.selectionModel.old = old;
		BrickEditor.processMenuKey = false;

	}
}
