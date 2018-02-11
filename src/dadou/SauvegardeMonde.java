package dadou;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import dadou.tools.BrickEditor;

public class SauvegardeMonde implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3937749057599981161L;
	public MondeEventInterface monde;
	public String nomMonde;
	public Map<String, SauvegardeModel> elements = new HashMap<>();

	public void chargerDebut(SauvegardeJoueur sj) {
		monde.chargerDebut(nomMonde, sj);
	}

	public void chargerFin() {
		monde.chargerFin(nomMonde);
	}

	public void chargerElements(BrickEditor be) {
		monde.i = be.mondeInterface.mondeInterface;
		for (Map.Entry<String, SauvegardeModel> sm : elements.entrySet()) {
			SauvegardeModel smdl = sm.getValue();
			ModelClasse mc = be.decorDeBriqueData.models
					.get(smdl.nomModelClasse);
			if (mc != null) {
				ObjetMobilePourModelInstance obj = new ObjetMobilePourModelInstance(
						be, mc, smdl.useBoundingSphere);
				be.espace.mobiles.put(sm.getKey(), obj);
				obj.mei = smdl.mei;
				obj.me = new ModelEvent();
				obj.nom = sm.getKey();
				obj.mei.i = be.mondeInterface.mondeInterface;

				try {
					smdl.charger(obj, this);
					obj.octreeRoot = be.espace.octree;
					obj.updateOctree();
				} catch (Throwable t) {
					be.espace.mobiles.remove(sm.getKey());
				}
				// Log.print(" charger "+sm.getKey()+" - "+smdl.nomModelClasse);
			
			}

		}
	}

}
