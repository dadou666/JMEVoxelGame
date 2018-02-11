package dadou.tools.construction;

import java.awt.Color;

import dadou.Game;
import dadou.ihm.Widget;



public class DialogDemandeNomClasse extends DialogDemandeSaisieChaine {
	public String nomModel;
	public boolean supprimerEspaces;
	public DialogDemandeNomClasse(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	public boolean validerSaisie() {
		nom = inputFieldList.models.get(wNom).toString();

		if (BrickEditor.decorDeBriqueData.models.get(nom) != null) {
			Widget.drawText(message, "Nom existant", Color.GREEN,
					Color.white, Color.red,0);
			return false;

		}
		
		nomModel = nom;
	
		return true;
		
	}
	public void actionValider() {
		BrickEditor.selection.BoxSelectionAction = BrickEditor.selection.BoxCopie;
		BrickEditor.selection.setBoxSelection(true);
		BrickEditor.etat = BrickEditor.selection;
		BrickEditor.processMenuKey = true;
		
	}
	public void actionAnnuler() {
	
	}
}
