package dadou;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.ihm.IHM;
import dadou.ihm.IHMModel;
import dadou.ihm.Widget;
import dadou.tools.BrickEditor;
import dadou.tools.graphics.Config;

public class ChargeurDecor {
	public ChargeurElementDecor ChargeurElementDecor;
	public DecorDeBrique decor;
	public BrickEditor g;
	public Widget progression;
	public IHM ihm;
	int progressionWidth = 300;
	int progressionHeight = 30;
	int total;
	int avancement = 0;
	DecorDeBriqueDataElement data;

	public void init(BrickEditor g, DecorDeBriqueDataElement data)
			throws ClassNotFoundException, IOException, CouleurErreur {
		decor = new DecorDeBrique(g, data.decorInfo.niveau);
		if (g.game != null) {
			decor.action.nomSkyBox = data.skyBox;
		}
		this.data = data;
		decor.creerChargeurDecor(this, g, data);

		if (data.imageEcran != null) {
			data.imageEcran.init(g.game.shaderWidget);
		} else if (g.decorDeBriqueData.imageEcran != null) {
			g.decorDeBriqueData.imageEcran.init(g.game.shaderWidget);
		}

		decor.gestionCollision = new GestionCollision(decor,
				data.decorInfo.niveau + 4, 1);
		int nbCube = 0;
		for (int hx = 0; hx < data.decorInfo.nbCube; hx++) {
			for (int hy = 0; hy < data.decorInfo.nbCube; hy++) {
				for (int hz = 0; hz < data.decorInfo.nbCube; hz++) {
					Color c = decor.lireCouleur(hx, hy, hz);
					if (!ElementDecor.estVide(c)) {
						nbCube++;
					}

				}

			}

		}
		Log.print("chargement " + nbCube + " decor=" + decor);
	}

	public void creerIHM() {
		this.ihm = g.game.nouvelleIHM();
		this.ihm.setSize(progressionWidth, progressionHeight);
		ihm.beginY();
		int height = (g.game.getHeight() - progressionHeight) / 2;
		int width = (g.game.getWidth() - progressionWidth) / 2;
		ihm.space(height);
		ihm.beginX();
		ihm.space(width);
		ihm.ajouterModel(() -> {
			afficherAvancement();
		});
		progression = ihm.widget();

		ihm.space(width);

		ihm.end();
		ihm.space(height);

		ihm.end();

	}

	public void afficherAvancement() {
		Widget w = progression;
		if (decor == null) {
			return;
		}

		Graphics2D g = w.getGraphics2DForUpdate();

		g.setColor(Color.black);
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(Color.GREEN);
		g.fillRoundRect(0, 0, w.width, w.height, 25, 25);
		int d = 5;
		g.setColor(Color.black);
		g.fillRoundRect(d, d, w.width - 2 * d, w.height - 2 * d, 25, 25);
		float p = (float) avancement / (float) total;
		int dw = (int) ((float) w.width * p);
		g.setColor(Color.blue);
		g.fillRoundRect(d, d, dw - 2 * d, w.height - 2 * d, 25, 25);

		// g.dispose();

		w.update();
	}

	public void charger(int count) throws CouleurErreur {
		while (count > 0) {
			this.charger();
			count--;
			if (decor == null) {
				if (g.game != null) {
					g.decor.ajouterLumieres();
					g.decor.ajouterZones();
				}
				return;
			}
		}

	}

	public void charger() throws CouleurErreur {
		if (decor == null) {
			return;
		}
		avancement++;

		if (this.ChargeurElementDecor == null) {

			if (g.espace != null) {
				g.espace.clear();
			}
			g.espace.octree = decor.octree;
			if (g.game != null) {
				g.scc.charger(decor.DecorDeBriqueData.cameraPosition);
			}
			if (this.g.decor != null) {
				this.g.decor.reconstuire();
			}
			this.g.decor = decor;

			decor.initialiserModelInstances(g);

			this.g.mondeInterface.initJoueurs();
			g.config = new Config(g.game,
					decor.DecorDeBriqueData.getConfigValues());
			g.config.BrickEditor = g;
			if (g.swingEditor != null) {
				Game.profondeur = g.config.config.profondeur
						* g.config.config.profondeur;
			}

			if (this.g.swingEditor != null && !this.g.mondeInterface.active) {

				this.g.swingEditor.reloadTree();
			}
			decor = null;
			avancement = 0;
			Game.modifierOmbre = 0;
			return;

		}
		ChargeurElementDecor.charger(decor);

		ChargeurElementDecor = ChargeurElementDecor.suivant;

	}

	public void dessiner(Shader shader) {

		if (data.imageEcran != null) {
			data.imageEcran.dessiner(shader);
		} else if (g.decorDeBriqueData.imageEcran != null) {
			g.decorDeBriqueData.imageEcran.dessiner(shader);
		}
		this.ihm.dessiner(shader);
	}
}
