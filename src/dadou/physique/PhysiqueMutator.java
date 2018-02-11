package dadou.physique;

import java.util.ArrayList;
import java.util.List;

import dadou.Espace;
import dadou.Log;
import dadou.mutator.Mutator;

public class PhysiqueMutator extends Mutator {
	public static float epsilonMvt = 0.05f;
	public static float distanceMax = 10.0f;
	public static int numStepMax = 50;
	public List<PhysiqueObjet> objets = new ArrayList<>();
	public Espace espace;
	public boolean transparenceActif = false;
	public int numStepTransparence;
	float transparence = 0.0f;
	float transparenceDeltat;
	public boolean move = false;
	public boolean remove = true;
	public boolean supprimerSiImmobile = false;
	public PhysiqueMonde pm;

	public void testTransparence() {
		for (PhysiqueObjet po : objets) {
			po.calcDistMvt();

			if (po.distMvt > epsilonMvt && po.numStep < numStepMax  && po.distTotal < distanceMax) {
				move = true;
				// Log.print(" distMvt="+po.distMvt+" po="+po);
				return;
			}

		}
		if (!move) {
			return;
		}
		transparenceActif = true;
	}

	public boolean explose() {
		return true;
	}

	public boolean animer() {
		if (supprimerSiImmobile) {
			int n = 0;
			for (PhysiqueObjet po : objets) {
				if (po.show) {
					po.calcDistMvt();
					if (po.distMvt < epsilonMvt) {
						po.supprimer();
					} else {
						n++;
					}
				}
			}
			return n > 0;

		}

		this.obj.transparence = 0.0f;
		if (!transparenceActif) {
			this.testTransparence();
			return true;
		}
		this.obj.transparence = transparence;

		if (this.numStepTransparence == 0) {
			for (PhysiqueObjet po : this.objets) {
				po.supprimer();

			}
			this.obj.transparence = 0.0f;
			// obj.listPO = null;
			if (remove) {
				espace.supprimer(obj);
			}
	
			return false;
		}

		transparence -= this.transparenceDeltat;
		this.numStepTransparence--;
		return true;

	}
}
