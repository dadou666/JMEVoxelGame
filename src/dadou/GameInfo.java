package dadou;

import java.awt.Color;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class GameInfo {
	public MondeInterfacePrive mi;
	public Widget libelle;
	public Widget donnee;
	public String nomVar;
	public String nomLibelle;
	public String boxColor = "BLUE";
	public Object oldValue;
	public boolean change = false;

	public GameInfo(IHM ihm, MondeInterfacePrive mi) {
		this.mi = mi;
		 ihm.ajouterModel(() -> {
			modifierDonnee();
		});
		donnee = ihm.widget();
		donnee.show = false;

	}

	public void modifierDonnee() {
		if (nomVar != null) {
			Object obj = mi.mondeInterface.lire(nomVar);
			if (obj != null) {
				if (oldValue == null || !oldValue.equals(obj) || change) {
					change = false;

					Widget.drawTextWithIcones(donnee, "" + obj, nomLibelle, Color.CYAN, Color.red,
							mi.selectionColor(boxColor), mi.brickEditor.decorDeBriqueData.icones, 50);
				}
			}
			oldValue = obj;
		}
	}

}
