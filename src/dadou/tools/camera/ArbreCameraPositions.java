package dadou.tools.camera;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.renderer.Camera;

import dadou.Arbre;
import dadou.Button;
import dadou.CameraPosition;
import dadou.Espace;
import dadou.Game;
import dadou.GroupeCameraPosition;
import dadou.Habillage;
import dadou.Log;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheElement;
import dadou.ihm.EditeurArbre;
import dadou.ihm.GestionArbreControlleur;
import dadou.ihm.IHM;
import dadou.tools.BrickEditor;
import dadou.tools.EtatBrickEditor;
import dadou.tools.construction.Selection;

public class ArbreCameraPositions extends EtatBrickEditor implements GestionArbreControlleur<String> {
	EditeurArbre<String> editeurArbre;

	Button button;
	List<String> elementsPourSupression = new ArrayList<String>();
	List<String> elementsPourAjout = new ArrayList<String>();
	IHM ihm;
	Habillage habillage;

	public ArbreCameraPositions(BrickEditor be) {
		Game g = be.game;
		BrickEditor = be;
		button = new Button(0);
		ihm = g.nouvelleIHM();

		ihm.setSize(128, 32);
		ihm.beginX();
		ihm.space(800);
		ihm.beginY();
		ihm.space(400);

		editeurArbre = new EditeurArbre<String>(this, new Arbre<String>(null), ihm, 128, 32, 5);
		// editeurArbre.ajouter.show = false;
		ihm.end();
		ihm.end();
		this.mdlPositionClasse = new ModelClasse();
		this.mdlPositionClasse.init(1, 1, 1);
		this.mdlPositionClasse.echelle = 0.5f;

		this.mdlPositionClasseRacine = new ModelClasse();
		this.mdlPositionClasseRacine.init(1, 1, 1);
		this.mdlPositionClasseRacine.echelle = 0.5f;

		habillage = new Habillage(4);
		try {
			NomTexture nt = habillage.creerNomTexture("red");
			this.mdlPositionClasse.copie[0][0][0] = new Color(nt.idx, nt.idx, nt.idx);
			for (int x = 0; x < habillage.dim; x++) {
				for (int y = 0; y < habillage.dim; y++) {
					habillage.SetBlock(x,y,nt.idx, Color.red);
				}
			}
			nt = habillage.creerNomTexture("blue");
			this.mdlPositionClasseRacine.copie[0][0][0] = new Color(nt.idx, nt.idx, nt.idx);
			for (int x = 0; x < habillage.dim; x++) {
				for (int y = 0; y < habillage.dim; y++) {
					habillage.SetBlock(x,y,nt.idx, Color.blue);
				}
			}
		} catch (PlusDeTexture e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		habillage.creerVoxelTexture3D();

		try {
			this.mdlPositionClasse.initBuffer(be.game, habillage);
			this.mdlPositionClasseRacine.initBuffer(be.game, habillage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Quaternion qTmp = new Quaternion();

	public void creerListePositionCamera(String groupe) {
		for (ObjetMobile om : this.listePositions) {
			om.detachFromOctree();
		}
		this.listePositions.clear();
		if (groupe == null) {
			return;
		}
		GroupeCameraPosition g = this.BrickEditor.decor.DecorDeBriqueData.groupeCameraPositions().get(groupe);
		if (g == null) {
			return;
		}
		for (String nom : g.noms) {
			CameraPosition cp = this.BrickEditor.decor.DecorDeBriqueData.cameraPositions.get(nom);

			qTmp.set(cp.rotationY);
			qTmp.multLocal(cp.rotationX);
			qTmp.loadIdentity();

			ModelClasse mc = this.mdlPositionClasse;
			Graphe graphe = null;
			if (this.BrickEditor.grapheDebug != null) {
				graphe = this.BrickEditor.grapheDebug.graphe;

				GrapheElement ge = (graphe.donnerGrapheElement(nom));
				if (ge != null && graphe.estRacine(ge.nom())) {
					mc = this.mdlPositionClasseRacine;
				//	Log.print(" racine "+nom);
				}

			}

			ObjetMobilePourModelInstance obj = new ObjetMobilePourModelInstance(this.BrickEditor, mc, false);

			obj.nom = "<<" + nom + ">>";

			obj.initTranslationRotationEchelle(cp.translation, qTmp, mc.echelle);

			obj.updateOctree();
			this.listePositions.add(obj);

		}

	}

	public void initialiserModel() {
		editeurArbre.setModel(BrickEditor.decor.DecorDeBriqueData.creerArbreCameraPosition());
	}

	public String nomGroupeEnCours;
	public List<ObjetMobile> listePositions = new ArrayList<>();
	public ModelClasse mdlPositionClasse;
	public ModelClasse mdlPositionClasseRacine;

	public CameraPosition donnerCameraPosition() {

		String r = this.donnerNomCameraPosition();
		CameraPosition cp = BrickEditor.decor.DecorDeBriqueData.donnerCameraPosition(r);
		return cp;

	}

	public String donnerNomCameraPosition() {
		String sel = editeurArbre.donnerSelection();
		if (sel == null) {
			return null;
		}
		if (editeurArbre.arbre.enfant(sel).enfants().size() > 0) {
			return null;
		}
		String chemin = editeurArbre.arbre.chemin("#");
		if (sel.equals("")) {
			return null;
		}
		if (chemin == null) {
			return null;
		}
		String r = chemin + "$" + editeurArbre.donnerSelection();
		/*
		 * CameraPosition cp = BrickEditor.decor.DecorDeBriqueData
		 * .donnerCameraPosition(r);
		 */
		return r;

	}

	@Override
	public void valider(EditeurArbre<String> editeurArbre) {
		// TODO Auto-generated method stub

		Selection selection = BrickEditor.selection;
		BrickEditor.menuCamera.selected = false;
		BrickEditor.menuCamera.updateText();
		BrickEditor.processMenuKey = true;
		selection.BoxSelectionAction = selection.BoxSelectionAction;
		BrickEditor.etat = selection;

		for (String nvGrp : this.elementsPourAjout) {
			BrickEditor.decor.DecorDeBriqueData.ajouterGroupeCameraPosition(nvGrp);
		}
		elementsPourAjout.clear();
		for (String pos : this.elementsPourSupression) {
			BrickEditor.decor.DecorDeBriqueData.supprimerCameraPosition(pos);
		}
		elementsPourSupression.clear();
		CameraPosition cp = this.donnerCameraPosition();
		if (cp == null) {
			String grp;

			Arbre<String> a = editeurArbre.arbre.enfant(editeurArbre.donnerSelection());
			if (a != null) {
				grp = a.chemin("#");
			} else {
				grp = editeurArbre.arbre.chemin("#");
			}

			if (BrickEditor.decor.DecorDeBriqueData.groupeCameraPositions.get(grp) != null) {
				this.nomGroupeEnCours = grp;
			} else {
				BrickEditor.afficherMessage("Pas un groupe " + grp);
			}
		} else {

			this.nomGroupeEnCours = cp.groupe;
			BrickEditor.scc.charger(cp);
			BrickEditor.decor.DecorDeBriqueData.cameraPosition = cp;
		}

	}

	public void gerer() throws CouleurErreur {

		editeurArbre.processkeyboard();
		editeurArbre.updateSelection();
		editeurArbre.processMouseButton(button);
		ihm.dessiner(BrickEditor.game.shaderWidget);

	}

	@Override
	public void annuler(EditeurArbre<String> editeurArbre) {
		// TODO Auto-generated method stub

		Selection selection = BrickEditor.selection;
		BrickEditor.menuCamera.selected = false;
		BrickEditor.menuCamera.updateText();
		BrickEditor.processMenuKey = true;
		selection.BoxSelectionAction = selection.BoxSelectionAction;
		BrickEditor.etat = selection;
		this.elementsPourAjout.clear();
		this.elementsPourSupression.clear();

	}

	@Override
	public boolean supprimer(EditeurArbre<String> editeurArbre, Arbre<String> nv) {
		// TODO Auto-generated method stub
		String nom = this.donnerNomCameraPosition();
		CameraPosition cp = BrickEditor.decor.DecorDeBriqueData.donnerCameraPosition(nom);
		if (cp == null) {
			BrickEditor.afficherMessage("Impossible de supprimer groupe");
			return false;
		}
		this.elementsPourSupression.add(nom);
		return true;
	}

	@Override
	public boolean ajouter(EditeurArbre<String> editeurArbre, String nv) {
		// TODO Auto-generated method stub
		String grp = editeurArbre.arbre.chemin("#");
		if (editeurArbre.arbre.enfants().isEmpty()) {
			BrickEditor.afficherMessage("Impossible de creer dans position");
			return false;

		}
		if (BrickEditor.decor.DecorDeBriqueData.groupeCameraPositions.get(grp) != null) {
			BrickEditor.afficherMessage("Impossible de creer dans groupe");
			return false;
		}
		if (grp != null && !grp.equals("")) {
			grp = grp + "#" + nv;
		} else {
			grp = nv;
		}
		elementsPourAjout.add(grp);

		return true;
	}

}
