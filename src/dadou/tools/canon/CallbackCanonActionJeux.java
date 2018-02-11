package dadou.tools.canon;

import java.awt.Color;

import com.jme.math.Vector3f;


import dadou.ElementDecor;
import dadou.MondeInterfacePrive;
import dadou.ObjetMobile;
import dadou.VoxelLigne;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class CallbackCanonActionJeux extends VoxelLigne implements
		CallbackCanon {
	public BrickEditor BrickEditor;
	public boolean impact = false;
	public Canon canon;
	public MondeInterfacePrive mondeInterface;
	public String nomJoueur;
	

	public CallbackCanonActionJeux(String nomJoueur, Canon canon,
			MondeInterfacePrive mondeInterface) {
		this.BrickEditor = mondeInterface.brickEditor;
		this.canon = canon;
		this.mondeInterface = mondeInterface;
		this.nomJoueur = nomJoueur;
	}

	@Override
	public boolean process(Obus obus) throws CouleurErreur {
		// TODO Auto-generated method stub
		Vector3f pos = obus.objet3D.getTranslationGlobal();
		int dimX = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimY = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dimZ = BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int x = (int) (pos.x + dimX / 2);
		int y = (int) (pos.y + dimY / 2);
		int z = (int) (pos.z + dimZ / 2);

		int oldX = (int) (oldPos.x + dimX / 2);
		int oldY = (int) (oldPos.y + dimY / 2);
		int oldZ = (int) (oldPos.z + dimZ / 2);

		if (x < 0 || x >= dimX) {
			return false;
		}
		if (y < 0 || y >= dimY) {
			return false;
		}
		if (z < 0 || z >= dimZ) {
			return false;
		}
		this.init(new int[] { oldX, oldY, oldZ

		}, new int[] { x, y, z });
		this.execute();
		return impact;
	}

	@Override
	public void finish() {
		if (impact) {
			impact = false;
		}

	}

	Vector3f oldPos = new Vector3f();

	@Override
	public void saveOldPos(Vector3f oldPos) {
		// TODO Auto-generated method stub
		this.oldPos.set(oldPos);

	}

	@Override
	public void process(int x, int y, int z) throws CouleurErreur {
		if (impact) {
			return;
		}

		Color oldColorA = BrickEditor.decor.lireCouleur(x, y, z);
		

		if (!ElementDecor.estVide(oldColorA) ) {
			
			impact = true;
		}
		
		
		ObjetMobile om = this.mondeInterface.brickEditor.espace.getOMFor(x, y, z);
	
		if (om == null) {
			return;
		}
		impact = true;
		
	
		try {
			if (om.evenement() != null && !om.explose()) {
			om.evenement().tire( om, null, null); }
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
