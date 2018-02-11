package dadou.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jme.math.Vector3f;

import dadou.tools.BrickEditor;

public class GrapheFermeture implements GrapheCheminTestEcouteur {
	public GrapheReduction graphe;
	public GrapheElement elt;
	public BrickEditor brickEditor;
	public List<GrapheElementRef> suivants = new ArrayList<>();
	public int idx = 0;
	public GrapheCheminTest test;
	public boolean fermer = false;

	public float speed;
	public float rayon;
	public GrapheElementRef fermetureRef;

	public GrapheFermeture(GrapheReduction graphe, GrapheElement elt,
			BrickEditor brickEditor, float rayon, float speed) {
		this.graphe = graphe;
		this.elt = elt;
		for (GrapheElementRef ger : elt.suivants) {
			if (ger.poid == 0) {
				suivants.add(ger);
			}
		}
		this.brickEditor = brickEditor;
		Collections.sort(suivants, new Comparator<GrapheElementRef>() {

			@Override
			public int compare(GrapheElementRef o1, GrapheElementRef o2) {
				// TODO Auto-generated method stub
				return Float.compare(o1.distance, o2.distance);
			}
		});
		this.rayon = rayon;
		this.speed = speed;

	}
	public boolean  estFini() {
		if (fermer) {
			return true;
		}
		if (idx >= suivants.size()) {
			return true;
		}
		return false;
	}
	public boolean executer() {
		if (fermer) {
			return false;
		}
		if (idx >= suivants.size()) {
			return false;
		}
		if (test == null) {
			test = new GrapheCheminTest(brickEditor, this,
					graphe.position(elt.nom),
					graphe.position(suivants.get(idx).nom), rayon, speed);
			

		}
		if (!test.executer()) {
			test = null;
			idx++;
			return !fermer;
		}

		return true;
	}

	@Override
	public void valider() {
		// TODO Auto-generated method stub
		fermetureRef = this.suivants.get(idx);
		fermetureRef.poid++;
		fermetureRef.fermeture = true;
		fermer = true;

	}
	

	@Override
	public void annuler() {
		// TODO Auto-generated method stub

	}

	@Override
	public void supprimer() {
		// TODO Auto-generated method stub

	}

}
