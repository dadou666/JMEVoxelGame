package dadou.tools.canon;

import java.util.HashSet;
import java.util.Set;

import com.jme.math.Vector3f;

import dadou.ControlleurCamera;
import dadou.MondeInterfacePrive;
import dadou.ObjetMobile;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class CallbackCanonSphereActionJeux implements CallbackCanon {
	public BrickEditor BrickEditor;
	public float rayon = .5f;
	
	public Canon canon;
	public Vector3f testPos = new Vector3f();

	

	public CallbackCanonSphereActionJeux(BrickEditor BrickEditor,
		 Canon canon) {
		this.BrickEditor = BrickEditor;
		this.canon = canon;


	}

	@Override
	public void saveOldPos(Vector3f oldPos) {
		// TODO Auto-generated method stub

	}

	public boolean verifierCollision(Obus obus) {
		testPos = (obus.oldPos);
		float t = 0.0f;
		float dx = obus.directionNormal.x * rayon;
		float dy = obus.directionNormal.y * rayon;
		float dz = obus.directionNormal.z * rayon;

		// testPos.addLocal(obus.directionNormal.x*rayon,obus.directionNormal.y*rayon,obus.directionNormal.z*rayon);
		while (t <= canon.vitesse) {
			if (BrickEditor.decor.gestionCollision.verifierCollisionSphere(
					testPos, rayon)) {
				testPos.addLocal(dx/2.0f, dy/2.0f, dz/2.0f);
				BrickEditor.mondeInterface.joueur.collisionTire(testPos, obus.directionNormal);
				return true;
			}
			obus.bs.setCenter(testPos);
			obus.bs.setRadius(rayon);
			om = (BrickEditor.espace.getOMFor(obus.bs,true));
			if (om != null ) {
				if (om.transparence > 0.0f) {
					if (this.canon.traverserObjetTransparent) {
						om = null;
						return false;
					}
				}
				if (this.canon.continuerApresImpactSurObjetMobile && ControlleurCamera.controlleur.continuerTire(BrickEditor)) {
					return false;
				}
				return true;
			}
			om = null;

			testPos.addLocal(dx, dy, dz);
			t += rayon;
		}
		// System.out.println(" testPos="+testPos+" - "+obus.objet3D.getTranslationGlobal());
		return false;

	}

	ObjetMobile om;

	@Override
	public boolean process(Obus obus) throws CouleurErreur {
		// TODO Auto-generated method stub
		om = null;
		boolean r = this.verifierCollision(obus);

		if (om == null) {
			return r;
		}
		if (obus.collisions.contains(om)) {
			return r;
		}
		obus.collisions.add(om);
		try {
			if (om.evenement() != null  && !om.explose()) {
				
				om.evenement().tire( om, null, obus);
				
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return r;
	}

	@Override
	public void finish() throws CouleurErreur {
		// TODO Auto-generated method stub

	}

}
