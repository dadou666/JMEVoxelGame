package dadou;

import java.util.ArrayList;
import java.util.List;

public class ParcourGroupeCameraPosition {
	public List<CameraPositionPourParcour> list;
	public int idx = 0;
	public MondeInterfacePrive i;
	public TransitionParcourGroupeCameraPosition transition;
	public boolean testPositionIdentique = true;
	int n = 0;
	CameraPositionPourParcour cppp;

	public ParcourGroupeCameraPosition(MondeInterfacePrive i, String nomGroupe) {

		this.i = i;
		list = new ArrayList<>();
		GroupeCameraPosition grp = i.brickEditor.decor.DecorDeBriqueData
				.groupeCameraPositions().get(nomGroupe);
		for (String nom : grp.noms) {
			list.add(new CameraPositionPourParcour(
					i.brickEditor.decor.DecorDeBriqueData
							.donnerCameraPosition(nom), 1));
		}
		transition = new TransitionParcourGroupeCameraPosition(i,
				list.get(0).cp);
		idx = 1;

	}

	public ParcourGroupeCameraPosition(MondeInterfacePrive i,
			List<CameraPositionPourParcour> list) {
		this.list = list;
		this.i = i;
		testPositionIdentique = false;

	}

	public void nextIdx() {
		idx++;
		if (idx == list.size()) {
			return;
		}

		CameraPosition cp = list.get(idx).cp;

		CameraPosition lastCp = list.get(idx - 1).cp;
		while (cp.translation.equals(lastCp.translation)
				&& cp.rotationX.equals(lastCp.rotationX)
				&& cp.rotationY.equals(lastCp.rotationY)) {
			idx++;
			if (idx == list.size()) {
				return;
			}
			cp = list.get(idx).cp;

		}
		return;

	}

	public void parcourir() {
		if (transition != null) {
			if (transition.process()) {
				transition = null;
			}
			return;
		}
		if (idx == list.size()) {
			i.parcourGroupeCameraPosition = null;

			i.brickEditor.decor.DecorDeBriqueData
					.finParcourGroupeCameraPosition(i);
			return;
		}

		if (cppp == null) {
			cppp = list.get(idx);
			CameraPosition cp = list.get(idx).cp;
			if (i.brickEditor.scc.charger(cp)) {
				i.brickEditor.sautCamera = true;
			}
			n = cppp.n;
		}
		n--;
		if (n <= 0) {

			cppp = null;

			nextIdx();
		}
		
	}
}
