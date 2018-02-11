package dadou;

import java.io.Serializable;

import com.jme.math.Vector3f;

abstract public class AbstractJoueur implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2768072807249871319L;
	transient public MondeInterfacePublic i;
	transient public  MondeEventInterface initMEI;
	transient public String nomMonde;
	public static AbstractJoueur  joueur;
	public void chargerDebut() {
		
	}
	public void chargerFin() {
		
	}
	public void sauvegarder() {
		
		
	}
	abstract public void initialiser();
	abstract public void demarer();

	abstract public void boucler();

	abstract public void collisionTire(Vector3f pos, Vector3f direction);

	abstract public void tirer(Vector3f position, Vector3f direction);

	abstract public void appuyerSurClavier(char character, int key);

	abstract public void tomber(float hauteur);

	abstract public void sautEnCours(boolean sautForce, float hauteurSaut);
	abstract public void selectionnerInfoJeux(Integer idx) ;
}
