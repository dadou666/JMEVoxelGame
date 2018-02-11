package dadou.tools.construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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

public class SelectionModelClasse extends EtatBrickEditor implements
		GestionListeControlleur {

	Button button;
	IHM ihm;
	GestionListe gestionListe;
	public EtatBrickEditor old;
	InputfieldList inputFieldList;
	Widget wNom;
	Widget message;

	public SelectionModelClasse(dadou.tools.BrickEditor be) {
		this.BrickEditor = be;

		button = new Button(0);
		ihm = be.game.nouvelleIHM();

		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);
		List<String> values = new ArrayList<String>();
		/*for (String s : be.decorDeBriqueData.models.keySet()) {
			values.add(s);

		}*/
		GestionListConfig config = new GestionListConfig();
		config.supprimer = true;
		//config.ajouter = true;
		gestionListe = new GestionListe(this, values, config, ihm, 128, 32, 5);

		Widget.drawText(ihm.widget(), "Nom", Color.RED, Color.BLUE, Color.BLACK);

		gestionListe.inputFieldList.setInputField(wNom = ihm.widget(), "");
		message = ihm.widget();
		Widget.drawText(message, "", Color.GREEN, Color.white, Color.red);

		ihm.end();
		ihm.end();

	}

	public String nomModelInstance() {
		String s = gestionListe.inputFieldList.models.get(wNom).toString();
		s = s.trim();
		if (s.equals("")) {
			return null;
		}
		return s;
	}
	public List<String> calculerListeModel(String groupe) {
		List<String> values = new ArrayList<String>();
		for (String s : this.BrickEditor.decorDeBriqueData.models.keySet()) {
			if (groupe.isEmpty()) {
				if (s.split("\\.").length == 1) {
					values.add(s);
				}

			} else {
				if (s.startsWith(groupe)) {
					values.add(s);
				}

			}

		}
		return values;
		
	}
	public void initListModel(String groupe) {
		
		gestionListe.inputFieldList.setInputField(wNom, "");
		gestionListe.setModel(this.calculerListeModel(groupe));

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
		Selection selection = BrickEditor.selection;
		if (!this.gestionListe.listeSupprimer.isEmpty()) {
			for (String nom : this.gestionListe.listeSupprimer) {
				this.BrickEditor.decorDeBriqueData.models.remove(nom);
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

		} else {
			ListElement elt = this.gestionListe.getSelection();
			if (BrickEditor.decor.DecorDeBriqueData.donnerModelInstance(this
					.nomModelInstance()) != null) {
				Widget.drawText(message, "Nom existant", Color.GREEN,
						Color.white, Color.red);
				return;

			}
			if (elt == null) {
	
				BrickEditor.etat = old;
				BrickEditor.processMenuKey = true;
				return;
			}

			ModelClasse model = BrickEditor.decorDeBriqueData.models
					.get(elt.value);
			selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0,
					model);
			;
			if (model.echelle == 0.0f) {
				model.echelle = 1.0f;
			}
			selection.BoxSelectionAction = selection.BoxColler;
			selection.EtatBoxSelection = new AfficherBoxSelection(model.dx,
					model.dy, model.dz,model.echelle, null);
		}
		BrickEditor.etat = selection;
		BrickEditor.processMenuKey = true;

	}
}
