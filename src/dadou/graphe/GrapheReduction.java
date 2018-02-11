package dadou.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jme.math.Vector3f;

import dadou.CameraPosition;
import dadou.tools.BrickEditor;

public class GrapheReduction {
	public GrapheCheminTest test;
	public List<GrapheCheminTest> newList = new ArrayList<>();
	public List<GrapheCheminTest> oldList = new ArrayList<>();

	public Graphe graphe;
	public BrickEditor be;
	public float speed;
	public float rayon;
	public List<GrapheFermeture> fermetures = new ArrayList<>();

	public GrapheReduction(String racine, List<String> sommets, BrickEditor be,
			float speed, float rayon) {
		graphe = new Graphe(sommets, racine);
		Vector3f p0 = new Vector3f();
		Vector3f p1 = new Vector3f();
		Set<Float> distancesSet = new HashSet<Float>();
		this.be = be;
		for (String s : sommets) {
			GrapheElement ge = graphe.donnerGrapheElement(s);

			p0.set(position(s));
			for (GrapheElementRef ger : ge.suivants) {
				p1.set(position(ger.nom));
				p1.subtractLocal(p0);
				ger.distance = p1.length();
				distancesSet.add(ger.distance);
			}

		}
		GrapheElement ge = graphe.racine();
		p0.set(position(racine));
		for (GrapheElementRef ger : ge.suivants) {

			p1.set(position(ger.nom));
			p1.subtractLocal(p0);
			ger.distance = p1.length();
			distancesSet.add(ger.distance);
		}
		List<Float> distances = new ArrayList<>();
		distances.addAll(distancesSet);
		Collections.sort(distances, new Comparator<Float>() {

			@Override
			public int compare(Float o1, Float o2) {
				// TODO Auto-generated method stub
				return o1.compareTo(o2);
			}
		});
		graphe.distances = distances;

		this.speed = speed;
		this.rayon = rayon;

	}

	public Vector3f position(String nom) {
		return be.decor.DecorDeBriqueData.cameraPositions.get(nom).translation;
	}

	public boolean executerExploration() {
		List<GrapheElementContext> list;

		if (graphe.nombreIncomplet() == 0) {

			return false;

		}
		list = graphe.contextsExploration();

		for (GrapheElementContext gec : list) {

			GrapheCheminTest gct = new GrapheCheminTest(be, gec,
					position(gec.nom()), position(gec.nomSuivant()), rayon,
					speed);
			this.newList.add(gct);

		}
		int count  =this.newList.size();

		this.executerTest();
		return count > 0;

	}

	public void preparerFermeture() {
		for (GrapheElement ge : this.graphe.elements.values()) {
			if (ge.nombreEnfant() == 1 && ge.suivants.size() > 1) {
				this.fermetures.add(new GrapheFermeture(this, ge, this.be,
						rayon, speed));
			}

		}
	}

	public boolean executerFermeture() {

		boolean fini = true;
		for (GrapheFermeture gf : this.fermetures) {

			if (gf.executer()) {
				fini = false;
			}

		}
		return !fini;

	}

	public void executerTest() {
	
		for(GrapheCheminTest e:this.newList) {
			if (e.executer()) {
				this.oldList.add(e);
			}
		}
		List<GrapheCheminTest> tmp;
		this.newList.clear();
		tmp = newList;
		newList = this.oldList;
		this.oldList = tmp;
	

	}

}
