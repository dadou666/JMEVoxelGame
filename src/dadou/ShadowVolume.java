package dadou;

import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;

import dadou.collision.GestionCollision;

public class ShadowVolume {
	OrientedBoundingBoxWithVBO principal;
	OrientedBoundingBoxWithVBO secondaire;
	float globalZ;
	Vector3f axe= new Vector3f();
	public void diviser(GestionCollision gc,int niveau) {
		if (niveau == 0) {
			return;
		}
		float extentZ = principal.extent.z/2.0f;
		axe.set(principal.zAxis);
		axe.multLocal(extentZ);
		secondaire.setCenter(principal.getCenter());
		secondaire.getCenter().addLocal(axe);
		principal.getCenter().subtractLocal(axe);
		principal.getExtent().z=extentZ;
		secondaire.getExtent().z=extentZ;
		if (gc.verifierCollision(principal)) {
			 diviser(gc,niveau-1);
			 return;
		}
		OrientedBoundingBoxWithVBO tmp = secondaire;
		secondaire = principal;
		principal = secondaire;
		 diviser(gc,niveau-1);
		 globalZ=globalZ/2.0f;
		
		
		
		
		
	}

}
