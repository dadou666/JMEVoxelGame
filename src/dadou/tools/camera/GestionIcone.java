package dadou.tools.camera;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.jme.renderer.Camera;

import dadou.Button;
import dadou.CameraPosition;
import dadou.DecorDeBriqueDataElement;
import dadou.FBO;
import dadou.Icone;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.GestionListe;
import dadou.ihm.GestionListeControlleur;
import dadou.ihm.IHM;
import dadou.ihm.ListElement;
import dadou.ihm.Widget;
import dadou.tools.BrickEditor;
import dadou.tools.EtatBrickEditor;

public class GestionIcone extends EtatBrickEditor implements
		GestionListeControlleur {

	public GestionListe GestionListe;
	public IHM ihm;
	public Button button;

	public FBO fbo;

	public void init() {

		GestionListe.setModel(BrickEditor.decorDeBriqueData.nomIcones());

	}

	public GestionIcone(BrickEditor be) {
		this.BrickEditor = be;
		button = new Button(0);
		ihm = be.game.nouvelleIHM();
		ihm.beginY();
		ihm.space(300);
		ihm.beginX();
		ihm.space(300);
		ihm.beginY();
		fbo = new FBO();
		fbo.init(100, 40);

		GestionListe = new GestionListe(this, new ArrayList<String>(), true,
				ihm, 128, 32, 5);

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

	@Override
	public void execute(dadou.ihm.GestionListe gestionListe, boolean valider)
			throws CouleurErreur {
		// TODO Auto-generated method stub
		this.BrickEditor.etat = this.BrickEditor.selection;
		
		this.BrickEditor.processMenuKey = true;

		if (valider) {

			for (String nouveau : this.GestionListe.listeAjouter) {
				fbo.activer();
				Camera cam = this.BrickEditor.game.getCamera();

				cam.apply();
				GL11.glViewport(0, 0, fbo.width, fbo.height);
				try {
					this.BrickEditor.afficherDecor();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Icone icone = new Icone(fbo.width, fbo.height, fbo.getContent());
				this.BrickEditor.decorDeBriqueData.ajouterIcone(nouveau, icone);
				icone.save("c:/tmp/" + nouveau + ".png");

				fbo.desactiver();

			}

			for (String nomPourSuppression : this.GestionListe.listeSupprimer) {
				this.BrickEditor.decorDeBriqueData
						.supprimerIcone(nomPourSuppression);
			}
			ListElement elt = this.GestionListe.getSelection();
			if (elt == null) {
				BrickEditor.menuCamera.selected = false;
				return;
			}

			this.BrickEditor.etat = old;

		} else {
		
		}

	}

}
