package dadou;

import java.io.Serializable;

public class SauvegardeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4525103736922671779L;
	public String nomModelClasse;
	public ModeleEventInterface mei;
	public boolean useBoundingSphere ;
	public void charger(ObjetMobilePourModelInstance om, SauvegardeMonde monde) {
		mei.om = om;
		mei.charger(monde);
		
	}
	public void sauvegarder() {
	
		
	}

}
