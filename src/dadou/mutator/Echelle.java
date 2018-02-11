package dadou.mutator;

import dadou.ObjetMobilePourModelInstance;

public class Echelle extends Mutator {
	public float echelle;
	public float speed;

	public Echelle(ObjetMobilePourModelInstance om, float echelle) {
		this.echelle = echelle;
		if (om.echelle > echelle) {
			this.speed = -om.speedEchelle;
		} else {
			this.speed = om.speedEchelle;

		}
		this.obj = om;

	}

	public Echelle(ObjetMobilePourModelInstance om, float echelle,float speed) {
		this.echelle = echelle;
		if (om.echelle > echelle) {
			this.speed = -speed;
		} else {
			this.speed =speed;

		}
		this.obj = om;

	}
	public boolean animer() {
		if (Math.abs(obj.echelle - echelle) <= Math.abs(speed)) {
			float oldEchelle = obj.echelle;

			obj.echelle(echelle);
			obj.detachFromOctree();
			if (this.verifierCollision()) {
				obj.echelle(oldEchelle);
				
			}
			obj.updateOctree();
			obj.evenement().finEchelle(obj);
			return false;
		}
		float oldEchelle = obj.echelle;
		obj.echelle(obj.echelle + speed);
		obj.detachFromOctree();
		if (this.verifierCollision()) {
			obj.echelle(oldEchelle);
			obj.updateOctree();
			return false;
		}
		obj.updateOctree();
		return true;
	}
	public void annuler() {
		
	}
}
