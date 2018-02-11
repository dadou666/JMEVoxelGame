package dadou.mutator;

import dadou.CameraPosition;

public class Parcour extends Mutator {
	public Mutator mutator;
	public Mutator mutatorInterruption;

	public Orientation creerOrientation(String nom) {
		if (nom == null) {
			return null;
		}
		CameraPosition cp = be.decor.DecorDeBriqueData
				.donnerCameraPosition(nom);
		Orientation mvt = new Orientation(be, obj, cp.translation, false, true);

		return mvt;
	}

	public Mouvement creerMouvement(String nom) {

		CameraPosition cp = be.decor.DecorDeBriqueData
				.donnerCameraPosition(nom);
		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();

		Mouvement mvt = new Mouvement(be, obj, cp.translation);

		return mvt;
	}
}
