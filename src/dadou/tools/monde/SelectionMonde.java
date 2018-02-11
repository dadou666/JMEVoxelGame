package dadou.tools.monde;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Mouse;

import dadou.Button;
import dadou.DecorDeBriqueDataElement;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.GestionListe;
import dadou.ihm.GestionListeControlleur;
import dadou.ihm.IHM;
import dadou.ihm.ListElement;
import dadou.ihm.Widget;

import dadou.tools.EtatBrickEditor;
import dadou.tools.BrickEditor;

public class SelectionMonde extends EtatBrickEditor implements GestionListeControlleur {
	public GestionListe GestionListe;
	public IHM ihm;
	public Button button;
	public DecorDeBriqueDataElement data;
	public String nom;

	public void init() {
		GestionListe.setModel(DecorDeBriqueDataElement.lesMondes(this.BrickEditor.cheminRessources));

	}

	public SelectionMonde(BrickEditor be) {
		this.BrickEditor = be;
		button = new Button(0);
		ihm = be.game.nouvelleIHM();
		ihm.beginY();
		ihm.space(300);
		ihm.beginX();
		ihm.space(300);
		ihm.beginY();
		if (BrickEditor.modeEditeur) {
			this.nom = BrickEditor.decorDeBriqueData.mondeCourant;
		} else {
			this.nom = BrickEditor.decorDeBriqueData.nomMondeDemarage;
		}
		GestionListe = new GestionListe(this, DecorDeBriqueDataElement.lesMondes(this.BrickEditor.cheminRessources),
				true, ihm, 128, 32, 5);

		ihm.space(300);
		ihm.end();

		ihm.space(300);
		ihm.end();
	}

	public void gerer() throws CouleurErreur {

		int x = Mouse.getX();
		int y = Mouse.getY();
		if (button.isPressed()) {
			Widget w = ihm.getWidgetAt(x, y);

			GestionListe.processClick(w);
		}
		GestionListe.processkeyboard();
		ihm.dessiner(BrickEditor.game.shaderWidget);

	}

	public void sauvegarder() throws FileNotFoundException, IOException {

		BrickEditor.decor.DecorDeBriqueData.sauvegarder(BrickEditor.cheminRessources + "/" + this.nom);

	}

	public DecorDeBriqueDataElement charger() throws FileNotFoundException, ClassNotFoundException, IOException {
		return DecorDeBriqueDataElement.charger(BrickEditor.cheminRessources + "/" + this.nom);

	}

	public void chargerMonde() throws FileNotFoundException, ClassNotFoundException, IOException, CouleurErreur {
		this.BrickEditor.decor.DecorDeBriqueData.cameraPosition = BrickEditor.scc.creerCameraPosition();
		DecorDeBriqueDataElement data = charger();
		this.BrickEditor.chargeur.init(this.BrickEditor, data);
		// this.BrickEditor.decor.initMonde(this.BrickEditor, data);

	}

	@Override
	public void execute(dadou.ihm.GestionListe gestionListe, boolean valider) throws CouleurErreur {
		// TODO Auto-generated method stub
		this.BrickEditor.etat = this.BrickEditor.selection;

		this.BrickEditor.processMenuKey = true;

		if (valider) {
			for (String nouveau : this.GestionListe.listeAjouter) {
				if (nouveau.endsWith(".wld")) {
					String nomFichier = BrickEditor.cheminRessources + "/" + nouveau;
					try {
						DecorDeBriqueDataElement.creer(BrickEditor.niveau, BrickEditor.elementTaille, nomFichier);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			for (String nomPourSuppression : this.GestionListe.listeSupprimer) {

			}
			ListElement elt = this.GestionListe.getSelection();
			if (elt == null) {
				return;
			}
			if (!this.nom.equals(elt.value)) {
				try {
					this.sauvegarder();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.nom = elt.value;
				try {
					this.chargerMonde();
					this.BrickEditor.decorDeBriqueData.mondeCourant = nom;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
}
