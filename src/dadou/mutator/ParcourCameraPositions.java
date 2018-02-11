package dadou.mutator;

import java.util.List;

import dadou.ObjetMobilePourModelInstance;
import dadou.tools.BrickEditor;

public class ParcourCameraPositions extends Parcour {
	public List<String> noms;
	public int idx = 0;
	public int increment = 1;
	public String nom;

	public ParcourCameraPositions(BrickEditor be,
			ObjetMobilePourModelInstance om, String nomGroupe,
			boolean depuisDebut) {
		this.be = be;
		this.obj = om;
		noms = be.decor.DecorDeBriqueData.donnerNomsPourGroupe(nomGroupe);
		if (depuisDebut) {
			increment = 1;
			idx = 0;
		} else {
			increment = -1;
			idx = noms.size() - 1;
		}

	}

	public ParcourCameraPositions(BrickEditor be,
			ObjetMobilePourModelInstance om, List<String> noms,
			boolean depuisDebut) {
		this.be = be;
		this.obj = om;
		this.noms = noms;
		if (depuisDebut) {
			increment = 1;
			idx = 0;
		} else {
			increment = -1;
			idx = noms.size() - 1;
		}

	}

	public Mutator interrompre(Mutator m) {
		if (m.estOrientation()) {
			if (this.mutator != null
					&& this.mutator.getClass() == Orientation.class) {
				return this;
			}
			this.interruption = true;
			this.mutatorInterruption = m;

			return this;
		}

		return m;
	}

	public boolean animer() {
		if (this.mutatorInterruption != null) {
			boolean r = mutatorInterruption.animer();
			this.mutator = null;
			if (r) {
				return r;
			}
			if (mutatorInterruption.collision) {
				mutatorInterruption = null;
				return false;
			}
			mutatorInterruption = null;
			idx -= increment;

		}
		if (mutator == null) {
			if (idx < 0 || idx >= noms.size()) {
				this.obj.mutator = null;
				this.obj.evenement().finParcour(obj);

				return false;
			}
			nom = noms.get(idx);
			mutator = this.creerOrientation(nom);
			idx += increment;

		}
		boolean r = mutator.animer();
		if (r) {
			return r;
		}
		if (mutator.collision) {
			if (this.obj.mutator == this) {
				this.obj.mutator = null;
			}
			return false;
		}
		if (mutator.getClass() == Orientation.class) {
			mutator = this.creerMouvement(nom);
		} else {
			mutator = null;

		}

		return true;

	}
}
