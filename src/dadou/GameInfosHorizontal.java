package dadou;

import java.awt.Color;
import java.util.Map;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class GameInfosHorizontal {
	public IHM ihmInfo;
	public GameInfo arrayGameInfo[];
	public GameBar arrayGameBar[];
	public MondeInterfacePrive mondeInterface;

	public GameInfosHorizontal(MondeInterfacePrive mondeInterface, int n, int nbBar, int largeurInfo) {
		Game game = mondeInterface.brickEditor.game;
		this.mondeInterface = mondeInterface;
		this.ihmInfo = game.nouvelleIHM();
		this.ihmInfo.beginY();
		arrayGameBar = new GameBar[nbBar];
		this.ihmInfo.setSize(Math.max(1000,n*100), 30);
		for (int i = 0; i < nbBar; i++) {
			
			arrayGameBar[i] = new GameBar(this.ihmInfo);
		}
		this.ihmInfo.beginX();
		arrayGameInfo = new GameInfo[n];

		this.ihmInfo.setSize(largeurInfo, 65);
		for (int i = 0; i < n; i++) {
			arrayGameInfo[i] = new GameInfo(ihmInfo,mondeInterface);
		
			arrayGameInfo[i].donnee.idxObject = i;
			ihmInfo.space(5);
		

		}
		this.ihmInfo.end();
		this.ihmInfo.space(650);
		this.ihmInfo.end();

	}

	public void afficherGameInfo(boolean b) {
		for (GameInfo gi : this.arrayGameInfo) {
			if (gi.nomVar != null) {
				gi.donnee.show = b;
			}
		}
	}

	public void dessiner() {
	
		ihmInfo.dessiner(mondeInterface.brickEditor.game.shaderWidget);
	

	}

	public void afficherDonnee(int idx, String nomVar, String libelle) {
		GameInfo gi = this.arrayGameInfo[idx];
		gi.nomVar = nomVar;

		gi.nomLibelle = libelle;
		// gi.libelle.show = true;
		gi.donnee.show = nomVar != null;

	}

}
