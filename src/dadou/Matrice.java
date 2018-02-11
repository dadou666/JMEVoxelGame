package dadou;

/** 
 * Classe Matrice pour la resolution de problemes lineaires
 * @author Alain Jean-Marie
 * @version 1.0 novembre 1999
 */


import java.lang.*;


public class Matrice {
    /**
     * tableau des �l�ments de la matrice. Il est prive pour qu'on ne
     * puisse y acc�der que par les accesseurs get/set.
     */
    private float elem[][];
    public int nLignes;
    public int nColonnes;
    static public class IncompatibleSizeException extends Exception {
    	public IncompatibleSizeException(String s) {
    		
    	}
    	
    }
    static public class SingularMatrixException extends Exception {
    	public SingularMatrixException(String s) {
    		
    	}
    	
    }
    
    /**
     * indicateur bool�en servant � savoir si les quantites calcul�es comme
     * la norme ou les �l�ments spectraux sont encore valables.
     * @see setElem
     */
    private boolean modifiee;
    private float normeInfini;
    /** 
     * Constructeur d'une matrice vide specifi�e par ses dimensions, et
     * initialis�e � 0
     * @param l nombre de lignes
     * @param c nombre de colonnes
     */
    public Matrice( int l, int c ) {
	nLignes = l;
	nColonnes = c;
	elem = new float[nLignes][nColonnes];
	for ( int i = 0; i < nLignes; i++ ) {
	    for ( int j = 0; j < nColonnes; j++ ) {
		elem[i][j] = 0.0f;
	    }
	}
	normeInfini = 0.0f;
	modifiee = false;
    }

    /** 
     * Constructeur d'une matrice � partir d'une existante.
     * @param m matrice � copier
     */
    public Matrice( Matrice m ) {
	nLignes = m.nLignes;
	nColonnes = m.nColonnes;
	elem = new float[nLignes][nColonnes];
	
	for ( int i = 0; i < nLignes; i++ ) {
	    for ( int j = 0; j < nColonnes; j++ ) {
		elem[i][j] = m.elem[i][j];
	    }
	}
	modifiee = m.modifiee;
	if ( modifiee ) {
	    normeInfini = m.normeInfini;
	}
    }

    /**
     * Constructeur d'une matrice � partir d'un tableau de float de dimension
     * 2. Le tableau doit �tre bien rectangulaire.
     * @param t tableau de nombres
     */
    public Matrice( float t[][] ) {
	nLignes = t.length;
	nColonnes = t[0].length;
	elem = new float[nLignes][nColonnes];
	int i = 0;
	int j = 0;
	
	try {
	    for ( i = 0; i < nLignes; i++ ) {
		for ( j = 0; j < nColonnes; j++ ) {
		    elem[i][j] = t[i][j];
		} 		
	    }
	    modifiee = true;

	} catch ( ArrayIndexOutOfBoundsException e ) {
	    System.out.println( "Erreur dans l'initialisation de matrice:" );
	    System.out.println( "l'�l�ment (" + i + "," + j +
				") n'a pas pu �tre trouv�." );
	    return;
	}

    }

    /**
     * Constructeur d'une matrice colonne (vecteur) � partir d'un tableau
     * de float de dimension 1.
     * @param t tableau de nombres
     */
    public Matrice( float t[] ) {
	nLignes = t.length;
	nColonnes = 1;
	elem = new float[nLignes][nColonnes];
	
	for ( int i = 0; i < nLignes; i++ ) {
	    elem[i][0] = t[i];
	}
	modifiee = true;
    }

    /**
     * Constructeur d'une matrice carr�e d'un type donn�. Les types reconnus
     * sont "VIDE" et "IDENTITE"
     */
    public Matrice( int n, String type ) {

	this( n, n);
	
	if ( type.equals( "VIDE" ) ) {
	    // c'est bon
	} else if ( type.equals( "IDENTITE" ) ) {
	    for ( int i = 0; i < n; i++ )
		this.elem[i][i] = 1.0f;
	} else {
	    System.out.println( "Warning: type de matrice:" + type + 
				"non reconnu. Matrice vide cr�e." );
	}

    }

    /** 
     * Accesseur standard en lecture des �l�ments. Il permet d'acc�der
     * � l'�l�ment (i,j) d'une matrice en forme math�matique standard,
     * c'est-�-dire en partant de 1 et non de 0.
     * @param l numero de ligne
     * @param c numero de colonne
     * @exception IndexOutOfBoundsException quand un indice est hors limite */
    public float getElem( int l, int c ) 
	throws IndexOutOfBoundsException {
	if ( ( l < 1 ) || ( l > nLignes ) )
	    throw new IndexOutOfBoundsException( "Indice de ligne " + l +
						 " hors limite ( 1.." +
						 nLignes + ")" );
	if ( ( c < 1 ) || ( c > nColonnes ) )
	    throw new IndexOutOfBoundsException( "Indice de ligne " + l +
						 " hors limite ( 1.." +
						 nLignes + ")" );
	
	return elem[l-1][c-1];
    }
    
    /** 
     * Accesseur standard en �criture des �l�ments. Il permet de
     * donner une valeur � l'�l�ment (i,j) d'une matrice en forme
     * math�matique standard, c'est-�-dire en partant de 1 et non de
     * 0.
     * @param l numero de ligne
     * @param c numero de colonne
     * @exception IndexOutOfBoundsException quand un indice est hors limite */
    public void setElem( int l, int c, float val ) 
	throws IndexOutOfBoundsException {
	if ( ( l < 1 ) || ( l > nLignes ) )
	    throw new IndexOutOfBoundsException( "Indice de ligne " + l +
						 "hors limite (1.." +
						 nLignes + ")" );
	if ( ( c < 1 ) || ( c > nColonnes ) )
	    throw new IndexOutOfBoundsException( "Indice de ligne " + l +
						 "hors limite (1.." +
						 nLignes + ")" );
	
	elem[l-1][c-1] = val;
	modifiee = true;
    }

    /**
     * Addition de matrices �l�ment par �l�ment � la matrice cible.
     * @param b matrice � ajouter
     * @exception IncompatibleSizeException quand les dimensions ne 
     * correspondent pas.
      *
     */
    public void addTo( Matrice b )
	throws IncompatibleSizeException 
    {
	// verification des dimensions 
	if ( ( b.nLignes != nLignes ) || ( b.nColonnes != nColonnes ) )
	    incompatibilite( this, b, "addTo", "<>" );
	
	for ( int i = 0; i < nLignes; i++ ) {
	    for ( int j = 0; j < nColonnes; j++ ) {
		this.elem[i][j] += b.elem[i][j];
	    }
	}
	modifiee = true;
    }
    
    /**
     * Addition de matrices �l�ment par �l�ment.
     * @param a matrice � ajouter
     * @param b matrice � ajouter
     * @return la matrice a+b
     * @exception IncompatibleSizeException quand les dimensions ne 
     * correspondent pas
      *
     */
    public static Matrice add( Matrice a, Matrice b )
	throws IncompatibleSizeException 
    {
	// verification des dimensions 
	if ( ( b.nLignes != a.nLignes ) || ( b.nColonnes != a.nColonnes ) )
	    incompatibilite( a, b, "add", "<>" );
	
	Matrice c = new Matrice( a );
	c.addTo( b );
	
	return c;
    }
    
    /**
     * Multiplication standard de matrices.
     * @param a matrice � multiplier � gauche
     * @param b matrice � multiplier � droite
     * @return la matrice a * b
     * @exception IncompatibleSizeException quand les dimensions ne 
     * correspondent pas
     */
    public static Matrice multiply( Matrice a, Matrice b )
	throws IncompatibleSizeException 
    {
	// verification des dimensions 
	if ( a.nColonnes != b.nLignes ) 
	    incompatibilite( a, b, "multiply", "et" );
	
	float somme;
	
	Matrice c = new Matrice( a.nLignes, b.nColonnes );
	for ( int i = 0; i < a.nLignes; i++ ) {
	    for ( int j = 0; j < b.nColonnes; j++ ) {
		somme = 0.0f;
		for ( int k = 0; k < a.nColonnes; k++ ) {
		    somme += a.elem[i][k] * b.elem[k][j];
		}
		c.elem[i][j] = somme;
	    }
	}
	
	return c;
    }

    /**
     * Resolution d'un systeme lineaire par la methode de Gauss avec choix du
     * pivot max.
     * @param a une matrice carr�e
     * @param b une matrice
     * @return la solution de a*x = b, soit: a^(-1)*b.
     * @exception SingularMatrixException si la matrice a est trouv�e non
     * inversible.
     * @exception IncompatibleSizeException si la matrice n'est pas carr�e
     * ou les tailles ne sont pas compatibles
     */
    public static Matrice solution( Matrice a, Matrice b )
	throws SingularMatrixException, IncompatibleSizeException {
	
	// verification des dimensions
	if ( a.nColonnes != a.nLignes ) {
	    String message =
		new String( "Matrice pas carr�e dans solution(): " +
			a.nLignes + "<>" + a.nColonnes );
	    throw new IncompatibleSizeException( message );
	}

	if ( a.nColonnes != b.nLignes ) {
	    incompatibilite( a, b, "inverse()", "et" );
	}

	int N = a.nLignes;
	Matrice aa = new Matrice( a );
	Matrice x = new Matrice( b );
	// descente; r+1 est le numero d'etape et de colonne � eliminer
	for ( int r = 0; r < N; r++ ) {
	    // recherche du pivot
	    float pmax = Math.abs( aa.elem[r][r] );
	    int imax = r;
	    for ( int i = r+1; i < N; i++ ) {
		if ( Math.abs( aa.elem[i][r] ) > pmax ) {
		    imax = i;
		    pmax = Math.abs( aa.elem[i][r] );
		}
	    }
	    if ( pmax == 0.0 ) {
		throw( new SingularMatrixException ( "Matrice non inversible" ) );
	    }
	    // permutation des lignes de a et b
	    float tmp;
	    for ( int j = r; j < N; j++ ) {
		tmp = aa.elem[r][j];
		aa.elem[r][j] = aa.elem[imax][j];
		aa.elem[imax][j] = tmp;
	    }
	    for ( int j = 0; j < x.nColonnes; j++ ) {
		tmp = x.elem[r][j];
		x.elem[r][j] = x.elem[imax][j];
		x.elem[imax][j] = tmp;
	    }
	    // combinaison lineaire
	    float pivot;
	    for ( int i = r+1; i < N; i++ ) {
		pivot = - aa.elem[i][r] / aa.elem[r][r];
		for ( int j = r; j < N; j++ ) {
		    aa.elem[i][j] += pivot * aa.elem[r][j];
		}
		for ( int j = 0; j < x.nColonnes; j++ ) {
		    x.elem[i][j] += pivot * x.elem[r][j];
		}
	    }
	}
	// Fin de la descente. Remont�e.
	for ( int r = N-1; r >= 0; r-- ) {
	    for ( int i = 0; i < r; i++ ) {
		float pivot = - aa.elem[i][r] / aa.elem[r][r];
		for ( int j = 0; j < x.nColonnes; j++ ) {
		    x.elem[i][j] += pivot * x.elem[r][j];
		}
	    }
	    for ( int j = 0; j < x.nColonnes; j++ ) {
		x.elem[r][j] /= aa.elem[r][r];
	    }
	}

	x.modifiee = true;

	return x;
    }

    /**
     * Inversion d'une matrice par la methode de Gauss avec choix du
     * pivot max.
     * @return la matrice a^(-1)
     * @exception SingularMatrixException si la matrice a est trouv�e non
     * inversible.
     * @exception IncompatibleSizeException si la matrice n'est pas carr�e
     */
    public Matrice inverse( )
	throws SingularMatrixException, IncompatibleSizeException
    {
	// verification des dimensions
	if ( nColonnes != nLignes ) {
	    String message =
		new String( "Matrice pas carr�e dans inverse(): " +
			    nLignes + "<>" + nColonnes );
	    throw new IncompatibleSizeException( message );
	}

	Matrice b = new Matrice( nColonnes, "IDENTITE" );
	Matrice x = solution( this, b );

	return x;
    }

    /** 
     * Calcul de la norme infinie de la matrice.
     * @return la valeur de la norme infinie
     */
    public float normeInfini( ) 
    {
	if ( !modifiee ) 
	    return normeInfini;
	else {
	    float res = 0.0f;
	    float somme;
	    
	    for ( int i = 0; i < nLignes; i++ ) {
		somme = 0.0f;
		for ( int j = 0; j < nColonnes; j++ ) {
		    somme += Math.abs( elem[i][j] );
		}
		if ( somme > res )
		    res = somme;
	    }
	    normeInfini = res;

	    return res;
	}
    }
    
    /**
     * M�thode de synth�se pour les messages d'erreur dus aux
     * incompatibilit�s de dimensions.
     * @param a matrice en cause
     * @param b autre matrice en cause
     * @param fonction nom de la m�thode ayant caus� le probl�me
     * @param type texte explicatif du motif de l'erreur
     * @exception IncompatibleSizeException dans tous les cas
     */
    private static void incompatibilite( Matrice a, Matrice b, 
					 String fonction, String type ) 
	throws IncompatibleSizeException
    {
	String message =
	    new String( "Dimensions de matrices incompatibles dans "
			+ fonction + "(): (" +
			a.nLignes + "x" + a.nColonnes +
			") " + type + " (" +
			b.nLignes + "x" + b.nColonnes + ")" );
	throw new IncompatibleSizeException( message );
    }
    
    /**
     * Conversion standard de la matrice en cha�ne de caract�res.
     * Le format adopt� est: ligne par ligne sur des champs de 15 caract�res.
     * @return une chaine listant la matrice ligne par ligne
     */
    public String toString( ) 
    {
	// creation de la chaine avec estimation grossiere de sa taille
	StringBuffer st = new StringBuffer( nLignes*nColonnes*25 );
	
	for ( int i = 0; i < nLignes; i++ ) {
	    for ( int j = 0; j < nColonnes; j++ ) {
		st.append( "  " + elem[i][j]  );
	    }
	    st.append("\n");
	}
	return st.toString();
	
    }
}