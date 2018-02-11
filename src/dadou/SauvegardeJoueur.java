package dadou;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SauvegardeJoueur implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6686271657089799348L;
	public Date dateSauvegarde;
	public AbstractJoueur joueur;
	public String mondeEnCours;
	public Map<String,SauvegardeMonde> mondes = new HashMap<>();
	public void chargerDebut(MondeInterfacePrive ip) {
		if (joueur == null) {
			return;
		}
		joueur.i=ip.mondeInterface;
		joueur.chargerDebut();
		
	}
	public void chargerFin(MondeInterfacePrive ip) {
		if (joueur  == null) {
			return;
		}
		joueur.chargerFin();
		
	}
	
	public void sauvegarder(MondeInterfacePrive ip) {
		this.joueur.sauvegarder();
		this.mondeEnCours = ip.nomMonde;
		SauvegardeMonde sm =	this.mondes.get(ip.nomMonde); 
		if (sm == null) {
			sm = new SauvegardeMonde();
			sm.monde = ip.mei;
			this.mondes.put(ip.nomMonde, sm);
		}
	
		sm.monde.sauvegarder(mondeEnCours);
		sm.elements = new HashMap<String,SauvegardeModel>();
		for(Map.Entry<String,ObjetMobilePourModelInstance> e:ip.brickEditor.espace.mobiles.entrySet()) {
			
			ModeleEventInterface mei = e.getValue().mei;
			SauvegardeModel smdl =new SauvegardeModel();
			smdl.nomModelClasse = e.getValue().mc.nom;
			smdl.mei = mei;
			sm.elements.put(e.getKey(), smdl);
			smdl.useBoundingSphere = e.getValue().useBoundingSphere();
			Log.print(" sauvegarde "+e.getKey()+" - "+smdl.nomModelClasse);
			smdl.mei.sauvegarder();
			
			
		}
	}

}
