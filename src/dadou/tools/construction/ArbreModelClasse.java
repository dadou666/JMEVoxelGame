package dadou.tools.construction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import dadou.Arbre;
import dadou.Button;
import dadou.Game;
import dadou.ModelClasse;
import dadou.ModelInstance;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.EditeurArbre;
import dadou.ihm.GestionArbreControlleur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.ihm.WidgetList;
import dadou.tools.EtatBrickEditor;
import dadou.tools.BrickEditor;

public class ArbreModelClasse extends EtatBrickEditor implements
		GestionArbreControlleur<String> {
	EditeurArbre<String> editeurArbre;

	Button button;
	List<String> elementsPourSupression = new ArrayList<String>();
	IHM ihm;

	public ArbreModelClasse(BrickEditor be) {
		Game g = be.game;
		BrickEditor = be;
		button = new Button(0);
		ihm = g.nouvelleIHM();

		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);

		editeurArbre = new EditeurArbre<String>(this,
				be.decorDeBriqueData.creerArbreModel(), ihm, 128, 32, 5);
		editeurArbre.ajouter.show = false;
		ihm.end();
		ihm.end();

	}

	public void gerer() throws CouleurErreur {

		editeurArbre.processkeyboard();
		editeurArbre.updateSelection();
		editeurArbre.processMouseButton(button);
		ihm.dessiner(BrickEditor.game.shaderWidget);

	}

	public void initialiserModel() {
		editeurArbre.setModel(BrickEditor.decorDeBriqueData.creerArbreModel());
	}

	public String donnerNomModeClasse() {
		String sel = editeurArbre.donnerSelection();
		if (sel == null) {
			return null;
		}
		if (editeurArbre.arbre.enfant(sel).enfants().size() > 0) {
			return null;
		}
		if (sel.equals("")) {
			return editeurArbre.arbre.chemin(".");
		}
		return editeurArbre.arbre.chemin(".") + "#"
				+ editeurArbre.donnerSelection();
	}
	public String saisie;
	public String nomModelInstance() {
		String s =saisie;
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.equals("")) {
			return null;
		}
		Integer i = indexSerie(s);
		if (i != null) {
			s = s + "[" + i + "]";
		}
		System.out.println("s="+s);
		return s;
	}

	public Integer indexSerie(String nom) {
		String pattern = nom + "[";
		Integer idx = null;
		if (BrickEditor.decor.DecorDeBriqueData.modelInstances != null) {
			for (String s : BrickEditor.decor.DecorDeBriqueData.modelInstances
					.keySet()) {
				if (s.startsWith(pattern)) {
					String sidx = s.substring(pattern.length(),
							s.indexOf("]") );
					int u = Integer.parseInt(sidx);
					if (idx == null || u >= idx) {
						idx = u + 1;
					}

				}

			}

		}
		if (idx== null && BrickEditor.decor.DecorDeBriqueData.modelInstances != null &&  BrickEditor.decor.DecorDeBriqueData.modelInstances.get(nom)!=null) {
			return 0;
		}
		return idx;

	}

	@Override
	public void valider(EditeurArbre<String> editeurArbre) {
		// TODO Auto-generated method stub

		String nom = this.donnerNomModeClasse();
		saisie = editeurArbre.donnerSaisie();
		if (saisie != null) {
			Integer i = this.indexSerie(saisie);
			if (i != null) {
				BrickEditor.afficherMessage("Element avec index " + i);
			}

		}

		for (String s : this.elementsPourSupression) {
			this.BrickEditor.decorDeBriqueData.models.remove(s);
		}
		if (!this.elementsPourSupression.isEmpty()) {
			this.BrickEditor.swingEditor.reloadTree();
		}
		elementsPourSupression.clear();
	
		Selection selection = BrickEditor.selection;
		if (nom != null) {

			ModelClasse model = BrickEditor.decorDeBriqueData.models.get(nom);
			selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0,
					model);
			;

			if (model.echelle == 0.0f) {
				model.echelle = 1.0f;
			}
			selection.BoxSelectionAction = selection.BoxColler;
			selection.EtatBoxSelection = new AfficherBoxSelection(model.dx,
					model.dy, model.dz, model.echelle, null);
			BrickEditor.etat = selection;
			selection.old = old;
		} else {
			selection.BoxSelectionAction = selection.BoxSelectionAction;
			BrickEditor.etat = selection;
		}
		BrickEditor.processMenuKey = true;

		if (nom == null) {
			BrickEditor.afficherMessage("Aucun element pour ajout");

		}

	}

	@Override
	public void annuler(EditeurArbre<String> editeurArbre) {
		// TODO Auto-generated method stub
	
		BrickEditor.processMenuKey = true;
		BrickEditor.etat = old;
	}

	@Override
	public boolean supprimer(EditeurArbre<String> editeurArbre, Arbre<String> nv) {
		// TODO Auto-generated method stub
		if (nv.enfants().size() >= 1) {
			BrickEditor.afficherMessage("Element non vide");
			return false;

		}
		String nom = nv.donnerParent().chemin(".");
		if (!nv.nom.equals("")) {
			nom = nom + "#" + nv.nom;
		}
		elementsPourSupression.add(nom);
		return true;

	}

	@Override
	public boolean ajouter(EditeurArbre<String> editeurArbre, String nv) {
		// TODO Auto-generated method stub
		return false;
	}
}
