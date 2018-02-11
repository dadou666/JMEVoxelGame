package dadou.test.avl;

public class arbre {

	// attribut
	private noeud racine;

	// constructeur
	public arbre() {
		racine = null;
	}

	public void parcourir(Parcourir p) {
		racine.parcourir(p);

	}
	/*
	 * //////////// // methodes // ////////////
	 */

	public noeud getRacine()
	/*
	 * Renvoie le noeud racine
	 */
	{
		return racine;
	}

	public int taille() {
		return taille(racine);
	}

	private int taille(noeud n) {
		if (n != null)
			return taille(n.getFG()) + taille(n.getFD()) + 1;
		else
			return 0;
	}

	public int nbr_feuille() {
		return nbr_feuille(racine);
	}

	public boolean recherche_valeur(noeud n, int val) {
		if (n == null)
			return false;
		else if (n.getvaleur() == val)
			return true;
		else {
			if (val > n.getvaleur())
				return (recherche_valeur(n.getFD(), val));
			else
				return (recherche_valeur(n.getFG(), val));
		}
	}

	private int nbr_feuille(noeud n)
	/*
	 * Renvoie le nombre de feuille que contient l'avl de racine n
	 */
	{
		if (n != null)
			if (n.est_feuille() == 1)
				return 1;
			else
				return nbr_feuille(n.getFG()) + nbr_feuille(n.getFD());
		else
			return 0;
	}

	public boolean arbre_vide()
	/*
	 * Renvoie true si l'arbre est vide false sinon
	 */
	{
		return ((racine == null) ? true : false);
	}

	private int Facteur_equilibre(noeud n)
	/*
	 * Renvoie la valeur du facteur d'équilibre du noeud n
	 */
	{
		return (Hauteur(n.getFD()) - Hauteur(n.getFG()));
	}

	public int Hauteur() {
		return Hauteur(racine);
	}

	private int Hauteur(noeud n)
	/*
	 * Renvoie la hauteur du noeud n
	 */
	{
		if (n == null)
			return 0;

		if (n.calculerHauteur) {

			n.hauteur = (1 + (Math.max(Hauteur(n.getFD()), Hauteur(n.getFG()))));
			n.calculerHauteur = false;

		} else {
		//	System.out.println("en cache\n");
		}
		return n.hauteur;
	}






	public void ajouter_noeud(noeud valeur) {
		racine = Ajouter_noeud(racine, valeur);
	}

	private noeud Ajouter_noeud(noeud n, noeud valeur)
	/*
	 * Ajout d'un entier val dans un AVL
	 */
	{
		if (n == null) {
			n = valeur;
		} else {
			if (valeur.getvaleur() < n.getvaleur())
				n.setFG(Ajouter_noeud(n.getFG(), valeur)); // ajout à gauche

			else
				n.setFD(Ajouter_noeud(n.getFD(), valeur)); // ajout à droite

			if (Math.abs(Facteur_equilibre(n)) >= 2)
				n = equilibrage(n);
		}
		return n;
	}

	private noeud equilibre_chemin(noeud haut, noeud bas)
	/*
	 * Parcours l'arbre du noeud modifié au noeud le plus bas et cherche le
	 * desiquilibre
	 */
	{
		if (haut != null) {
			if (haut.getvaleur() > bas.getvaleur())
				haut.setFG(equilibre_chemin(haut.getFG(), bas));
			else
				haut.setFD(equilibre_chemin(haut.getFD(), bas));
			if (Math.abs(Facteur_equilibre(haut)) >= 2)
				haut = equilibrage(haut);
		}
		return haut;
	}

	public void Suppression(int val) {
		racine = Recherche(racine, val);
	}

	private noeud Recherche(noeud n, int val)
	/*
	 * Renvoie le noeud contenant la valeur val null sinon
	 */
	{
		if (n != null) {
			if (n.getvaleur() == val) {
				return Supprimer_noeud(n);
			} else {
				if (val > n.getvaleur())
					n.setFD(Recherche(n.getFD(), val));
				else
					n.setFG(Recherche(n.getFG(), val));

				if (Math.abs(Facteur_equilibre(n)) >= 2)
					n = equilibrage(n);
			}
		}
		return n;
	}

	private noeud Supprimer_noeud(noeud n)
	/*
	 * supprime le noeud n dans un avl
	 */
	{
		noeud n1, Pere;

		if (n.getFG() == null)
			return n.getFD();
		else if (n.getFD() == null)
			return n.getFG();
		else {
			n1 = n.getFG();
			Pere = n;
			while (n1.getFD() != null) {
				Pere = n1;
				n1 = n1.getFD();
			}
			n.setvaleur(n1.getvaleur());
			if (Pere == n)
				Pere.setFG(n1.getFG());
			else
				Pere.setFD(n1.getFG());
			n = equilibre_chemin(n, Pere);
			return n;
		}
	}

	public void destruction()
	/*
	 * Destruction de l'abre dans sa globalité. On utilise les propriétés du
	 * garbage collector a notre avantage : --> on ne met donc que racine a null
	 * et l'arbre sera detruit automatiquement
	 */
	{
		racine = null;
	}

	private noeud equilibrage(noeud n)
	/*
	 * On cherche quel equilibrage on va utilisé en fonction des facteurs
	 * d'équilibres
	 */
	{

		switch (Facteur_equilibre(n)) {
		case 2:
			switch (Facteur_equilibre(n.getFD())) {
			case -1:
				n = equ_DG(n);
				break;

			case 0:
				n = equ_DD(n);
				break;

			case 1:
				n = equ_DD(n);
				break;
			}
			break;

		case -2:
			switch (Facteur_equilibre(n.getFG())) {
			case -1:
				n = equ_GG(n);
				break;

			case 0:
				n = equ_GG(n);
				break;

			case 1:
				n = equ_GD(n);
				break;
			}
			break;
		}
		return n;
	}

	private noeud equ_DD(noeud N)
	/*
	 * rotation gauche
	 */
	{
		noeud NFD = N.getFD();
		N.setFD(NFD.getFG());
		NFD.setFG(N);
		return NFD;
	}

	private noeud equ_GG(noeud N)
	/*
	 * rotation droite
	 */
	{
		noeud NFG = N.getFG();
		N.setFG(NFG.getFD());
		NFG.setFD(N);
		return NFG;
	}

	private noeud equ_GD(noeud N)
	/*
	 * rotation gauche/droite
	 */
	{
		N.setFG(equ_DD(N.getFG())); // Rotation gauche sur le SAG de N
		return equ_GG(N); // Rotation droite sur N
	}

	private noeud equ_DG(noeud N)
	/*
	 * rotation droite/gauche
	 */
	{
		N.setFD(equ_GG(N.getFD())); // Rotation droite sur le SAD de N
		return equ_DD(N); // Rotation gauche sur N
	}

	public String arbre_chaine() {
		return arbre_chaine(racine);
	}

	private String arbre_chaine(noeud n)
	/*
	 * Renvoie la representation textuelle de l'avl sous la forme ( Racine,
	 * FilsGauche(SAG), FilsDroit(SAD))
	 */
	{
		if (n != null) {
			return Float.toString(n.getvaleur()) + "( " + arbre_chaine(n.getFG()) + ", " + arbre_chaine(n.getFD())
					+ " )";
		} else
			return "ø";
	}
}