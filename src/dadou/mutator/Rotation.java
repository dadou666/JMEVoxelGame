package dadou.mutator;

import com.jme.math.Vector3f;

import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class Rotation extends Mutator {
	
	public Vector3f axe = new Vector3f();

	public Rotation(BrickEditor be,ObjetMobilePourModelInstance om,Vector3f axe) {
		this.be=be;
		if (axe != null) {
		this.axe.set(axe); }
	
		this.obj=om;
		
	}
	public boolean animer() {
		this.sauvegarder();
		obj.rotateCenter(axe, obj.speedRotate);
		this.verifierCollision();
		return true;
	}
	

}
