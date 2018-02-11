package dadou.mutator;

import com.jme.math.Vector3f;

import dadou.DecompositionModelClasseCtx;
import dadou.Espace;
import dadou.ModelClasse;
import dadou.ObjetMobilePourModelInstance;
import dadou.tools.canon.GestionExplosionSphere;

public class DecomposerRecomposer extends Mutator {
	public float distance;

	public float vitesse;
	public Espace espace;
	public boolean recomposer;
	Vector3f axeRotation;
	boolean appliquerTransparence ;
	public DecomposerRecomposer(ObjetMobilePourModelInstance om, Espace espace, float distance, float vitesse,
			int taille, boolean appliquerTransparence, Vector3f axe) {
		DecompositionModelClasseCtx ctx = new DecompositionModelClasseCtx();
		this.recomposer = false;
		this.axeRotation=axe;
		this.appliquerTransparence =appliquerTransparence;
		try {
			ModelClasse mc = om.mc;
			ctx.appliquerTransparence = true;
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
		if (axeRotation != null){
			this.sauvegarder();
			obj.rotateCenter(axeRotation, obj.speedRotate);
			this.verifierCollision();
		}
		obj.annulerCollisionCamera = true;
		if (!recomposer) {
			obj.dmc.distance += vitesse;
		} else {
			obj.dmc.distance -= vitesse;
		}
		if (obj.dmc.distance <= 0) {
			obj.dmc = null;
			return false;
		}
		if (obj.dmc.distance >= distance) {
			recomposer = true;
			return true;
		}
		if (appliquerTransparence) {
			obj.transparence = 1.0f - obj.dmc.distance / distance;
			obj.dmc.dmc.echelleElement = obj.echelle;

		} 
		return true;

	}

}
