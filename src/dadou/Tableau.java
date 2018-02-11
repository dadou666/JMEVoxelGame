package dadou;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.BrickEditor;


public class Tableau {
	public List<ElementTableau> list = new ArrayList<>();
	public Tableau transition;
	public IHM ihm;
	public Widget widget;
	public BrickEditor brickEditor;
	public int idxVue = 0;
	public int nombreElement = 0;
	public int fontHeight;
	public float echelle=0.0f;
	public float vitesseActivation=0.05f;
	public boolean activer;
	public void echelle(float echelle) {
		this.echelle=echelle;
		ihm.echelle(echelle);
	}
	public void gererActivation(MondeInterfacePrive i) {
		if (activer) {
			if (echelle < 1.0f) {
				echelle+=vitesseActivation;
				if (echelle > 1.0f) {
					echelle = 1.0f;
				}
				ihm.echelle(echelle);
			}
		
			
		} else {
			if (echelle >= 0.0f) {
				echelle = echelle-vitesseActivation;
				if (echelle <=0.0f) {
					echelle =0.0f;
					i.tableau = transition;
				}
				ihm.echelle(echelle);
			}
		
		}
	}

	public int donnerNombreDeLigneAvantIdx() {
		int n = 0;
		for (int i = 0; i < idxVue; i++) {
			n += list.get(i).nbLigne();
		}
		return n;

	}

	public int limite() {
		return Math.min(idxVue + nombreElement, list.size());
	}

	public void nombreElement(int n) {

		this.nombreElement = n;
	}

	public void idxVue(int idx) {
		if (idx < 0) {
			return;
		}
		if (idx + nombreElement > list.size()) {
			return;
		}
		this.idxVue = idx;
		this.modifier();

	}

	public void initialiser(BrickEditor brickEditor, int mwidth, int mheight) {
		this.brickEditor = brickEditor;
		Game game = brickEditor.game;
		this.ihm = game.nouvelleIHM();
		this.ihm.setSize(mwidth, mheight);
		ihm.beginY();
		int height = (game.getHeight() - mheight) / 2;
		int width = (game.getWidth() - mwidth) / 2;
		ihm.space(height);
		ihm.beginX();
		ihm.space(width);
		widget = ihm.widget();

		modifier = true;
		ihm.space(width);

		ihm.end();
		ihm.space(height);

		ihm.end();

	}

	public int taille() {
		int taille = 0;
		for (ElementTableau et : list) {
			taille += et.nbLigne();
		}
		return taille;
	}

	public int indexDepart(int limit) {
		int tailleTotal = 0;
		for (int i = list.size() - 1; i >= 0; i--) {
			int taille = list.get(i).nbLigne();
			tailleTotal += taille;
			if (tailleTotal >= limit) {
				return i + 1;
			}

		}
		return 0;
	}

	public boolean modifier = false;

	public void modifier() {
		modifier = true;
	}

	public void modifierTexture() {

		if (modifier) {
			Widget.drawTextRounded(widget, this, brickEditor.decorDeBriqueData.icones, Color.BLUE, Color.GRAY,
					Color.white);
			modifier = false;
			return;
		}
		for (int i = 0; i < this.nombreElement; i++) {
			if (i + idxVue >= this.list.size()) {
				return;
			}
			ElementTableau e = this.list.get(i + idxVue);
			if (e.modifierTexture()) {

				Widget.drawTextRounded(widget, this, brickEditor.decorDeBriqueData.icones, Color.BLUE, Color.GRAY,
						Color.white);
				return;
			}
		}
	}

	public void dessiner(Shader shader) {

		this.ihm.dessiner(shader);
	}
	public void dessinerAvecTransparence(Shader shader) {

		this.ihm.dessinerAvecTransparence(shader);
	}

}
