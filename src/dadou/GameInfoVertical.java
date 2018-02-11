package dadou;

import java.awt.Color;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class GameInfoVertical {
	public MondeInterfacePrive mondeInterface;
	public Widget libelle;
	public Widget donnee;
	public String nomVar;
	public String nomLibelle;
	public String boxColor = "BLACK";
	public Object oldValue;
	public boolean change = false;

	public GameInfoVertical(IHM ihm, MondeInterfacePrive mi) {
		this.mondeInterface = mi;

		ihm.setSize(45, 55);
		libelle = ihm.widget();
		// ihm.space(1);
		ihm.setSize(100, 55);
		donnee = ihm.widget();
		ihm.ajouterModel(() -> {
			modifier();
		});
	}

	public void modifier() {
		if (nomVar != null) {
			Object obj = mondeInterface.mondeInterface.lire(nomVar);
			if (obj != null) {
				if (oldValue == null || !oldValue.equals(obj)) {

					Widget.drawText(donnee, "" + obj, Color.GREEN, Color.BLACK,
							Color.BLACK, 50);

					Widget.drawTextWithIcones(
							libelle,
							nomLibelle,
							nomLibelle,
							Color.CYAN,
							Color.red,
							mondeInterface.selectionColor(boxColor),
							mondeInterface.brickEditor.decorDeBriqueData.icones,
							50);
				}

			}
			oldValue = obj;
		}

	}

}
