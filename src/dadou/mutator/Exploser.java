package dadou.mutator;

import java.awt.Color;

import dadou.Espace;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.canon.GestionExplosionSphere;

public class Exploser extends Echelle {
	GestionExplosionSphere ge;
	Espace espace;
	public String nomExplosion = "RED";
	float d;
	public boolean supprimer = true;
	public boolean reduire = true;
	public boolean explose() {
		return true;
	}
	
	public Exploser(ObjetMobilePourModelInstance om, GestionExplosionSphere ge, Espace espace, boolean supprimer,
			boolean reduire) {
		super(om, 0.01f, 0.05f);

		this.ge = ge;
		this.espace = espace;
		this.supprimer = supprimer;
		this.reduire = reduire;

		d = 4 * om.box.getExtent().length();
		// TODO Auto-generated constructor stub
	}

	public boolean animer() {

		if (!super.animer() || !reduire) {

			ge.explosion(nomExplosion, obj.getTranslationGlobal());
			if (supprimer) {
				espace.supprimer(obj);
			}
			return false;
		}
		return true;
	}

	/*
	 * public boolean verifierCollision() { return false;
	 * 
	 * }
	 */

}
