package dadou;

import java.awt.Color;
import java.util.Map;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class GameInfosVertical {
	public IHM ihmInfo;
	public GameInfoVertical arrayGameInfo[];

	public MondeInterfacePrive mondeInterface;

	public GameInfosVertical(MondeInterfacePrive mondeInterface, int n, int space) {
		Game game = mondeInterface.brickEditor.game;
		this.mondeInterface = mondeInterface;
		this.ihmInfo = game.nouvelleIHM();
		this.ihmInfo.beginX();
		this.ihmInfo.space(550);
		this.ihmInfo.beginY();
		//this.ihmInfo.space(150);
		arrayGameInfo = new GameInfoVertical[n];

		for (int i = 0; i < n; i++) {
			this.ihmInfo.beginX();
			arrayGameInfo[i] = new GameInfoVertical(ihmInfo,mondeInterface);


			arrayGameInfo[i].donnee.idxObject = i;

			arrayGameInfo[i].donnee.show = true;
			this.ihmInfo.end();

		}

		this.ihmInfo.space(space);
		this.ihmInfo.end();
		this.ihmInfo.end();

	}

	public void afficherGameInfo(boolean b) {
		for (GameInfoVertical gi : this.arrayGameInfo) {
			if (gi.nomVar != null) {
				gi.donnee.show = b;
				gi.libelle.show= b;
			}
		}
	}

	public void dessiner() {

		ihmInfo.dessiner(mondeInterface.brickEditor.game.shaderWidget);


	}

	public void afficherDonnee(int idx, String nomVar, String libelle) {
		GameInfoVertical gi = this.arrayGameInfo[idx];
		gi.nomVar = nomVar;

		gi.nomLibelle = libelle;
		// gi.libelle.show = true;
		gi.donnee.show = nomVar != null;

	}

}
