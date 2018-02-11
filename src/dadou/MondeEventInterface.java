package dadou;

import java.io.Serializable;

import com.jme.math.Vector3f;

abstract public class MondeEventInterface implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7042364225307836563L;
	transient public MondeInterfacePublic i;
	public void chargerDebut(String nomMonde, SauvegardeJoueur sj) {
		this.demarer();
		
	}
	public void chargerFin(String nomMonde) {
		
	}
	public void sauvegarder(String nomMonde) {
		
		
	}
	abstract public void demarer();
	
	abstract public void finSon(String nom);

	abstract public void finaliser();

	abstract public void tomber(float hauteur);

	abstract public void sautEnCours(boolean sautForce, float hauteurSaut);

	abstract public void appuyerSurClavier(char character, int key);

	abstract public void selectionnerInfoJeux(Integer idx);

	abstract public void arreter();

	abstract public void tirer();

	abstract public void entreeZoneDetectionJoueur(ElementJeux om);

	abstract public void sortieZoneDetectionJoueur(ElementJeux om);

	abstract public void boucler();

	abstract public void collisionTire(Vector3f pos, Vector3f dir);

	abstract public void sortieMonde();

	abstract public void finTireSansCollision();

	abstract public void finParcourGroupeCameraPosition();

}
