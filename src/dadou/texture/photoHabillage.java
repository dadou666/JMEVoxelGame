package dadou.texture;

import dadou.Habillage;
import dadou.ModelClasse;

public class photoHabillage {
	public ModelClasse mc;
	public String nom;
	public photoHabillage(Habillage hab,int dx,int dy) {
		valeurs = new int[dx][dy];
		this.habillage =hab;
	}
	public Habillage habillage;
	public int [][] valeurs;
	public int valeurBord;
	

}
