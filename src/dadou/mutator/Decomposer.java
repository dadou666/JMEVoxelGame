package dadou.mutator;

import dadou.DecompositionModelClasseCtx;
import dadou.Espace;
import dadou.ModelClasse;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.canon.GestionExplosionSphere;

public class Decomposer extends Mutator {
	public float distance;

	public float vitesse;
	public Espace espace;
	

	public boolean explose() {
		return true;
	}

	public Decomposer(ObjetMobilePourModelInstance om, Espace espace, float distance, float vitesse, int taille, boolean appliquerTransparence) {
		DecompositionModelClasseCtx ctx = new DecompositionModelClasseCtx();
		try {
			ModelClasse mc = om.mc;
			ctx.appliquerTransparence =appliquerTransparence;
			ctx.dmc = mc.decompositionCube(espace.be, taille);
			om.dmc = ctx;
			ctx.dmc.echelleElement = om.echelle;
		
		
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.vitesse = vitesse;
		om.dmc.distance = 0.0f;
	
		this.distance = distance;
		this.espace = espace;
		this.obj = om;
	

	}

	public boolean animer() {
		obj.annulerCollisionCamera = true;
		
		obj.dmc.distance += vitesse;
		if (obj.dmc.distance >= distance) {
			obj.evenement().finDecomposition(obj);
			espace.supprimer(obj);
			return false;
		}
		if (obj.dmc.appliquerTransparence) {
			obj.transparence = 1.0f - obj.dmc.distance / distance;
			obj.dmc.dmc.echelleElement = obj.echelle;

		} else {
			obj.dmc.dmc.echelleElement= obj.echelle*(1.0f - obj.dmc.distance / distance);
		}
		return true;

	}

}
