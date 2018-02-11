package dadou.test.avl;

public class noeud {

	/*
	 * attribut
	 */
	float valeur;
	boolean calculerHauteur = true;
	int hauteur;
	Object element;
	noeud FG;
	noeud FD;

	/*
	 * constructeur
	 */
	noeud() {
		valeur = 0;
		FG = null;
		FD = null;
	}

	public void parcourir(Parcourir action) {
		if (FG != null) {
			FG.parcourir(action);
		}
		action.parcourir(this);
		if (FD != null) {
			FD.parcourir(action);
		}
	}

	/*
	 * //////////// // methodes // ////////////
	 */

	noeud getFG()
	/*
	 * Renvoie le Fils Gauche du noeud courant
	 */
	{
		return FG;
	}

	noeud getFD()
	/*
	 * Renvoie le Fils Droit du noeud courant
	 */
	{
		return FD;
	}

	float getvaleur()
	/*
	 * Renvoie la valeur entiere du noeud courant
	 */
	{
		return valeur;
	}

	void setFG(noeud n)
	/*
	 * On assigne au Fils Gauche du noeud courant un nouveau noeud n
	 */
	{
		this.calculerHauteur = true;
		FG = n;
	}

	void setFD(noeud n)
	/*
	 * On assigne au Fils Droit du noeud courant un nouveau noeud n
	 */
	{
		this.calculerHauteur = true;
		FD = n;
	}

	void setvaleur(float v)
	/*
	 * On assigne une valeur entiere au noeud courant
	 */
	{
		valeur = v;
	}

	int est_feuille()
	/*
	 * Renvoie true si le noeud est une feuille false sinon
	 */
	{
		return (((FG == null) && (FD == null)) ? 1 : 0);
	}
}