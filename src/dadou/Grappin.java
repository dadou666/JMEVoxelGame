package dadou;

import com.jme.math.Vector3f;

import dadou.tools.BrickEditor;
import dadou.tools.canon.Obus;

public class Grappin {
	public static Grappin grappin;
	public Vector3f direction= new  Vector3f();
	public static float  vitesse = 0.25f;;
	public Obus obus;
	public Vector3f positionImpact = new Vector3f();
	public enum Etat { AttenteTire,EnCours };
	public Etat etat;
	public BrickEditor be;
	public Grappin(BrickEditor be) {
		this.be=be;
	}
	static public void gerer(ExplorerCameraControllerBis be) {
		if (grappin == null) {
			return;
		}
		if (!enCours()) {
			return;
		}
		be.translation.addLocal(grappin.direction);
	}
	
	public static void impact(Vector3f positionImpact ) {
		if (grappin == null) {
			return;
		}
		//Log.print(grappin.etat);
			grappin.positionImpact.set(positionImpact);
			grappin.etat = Etat.EnCours;
			grappin.direction.set(grappin.positionImpact);
			grappin.direction.subtractLocal(grappin.be.game.getCamera().getLocation());
			grappin.direction.normalizeLocal();
			grappin.direction.multLocal(vitesse);
		
	}


	public static boolean enCours() {
		if (grappin == null) {
			return false;
		}
		return grappin.etat == Etat.EnCours;
	}
	public static void annuler() {
		if (grappin ==null) {
			return;
		}
		//Log.print(grappin.etat);
		grappin.etat = Etat.AttenteTire;
	}

}
