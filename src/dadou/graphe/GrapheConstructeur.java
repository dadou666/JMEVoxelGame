package dadou.graphe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jme.math.Vector3f;

import dadou.Log;
import dadou.tools.BrickEditor;

public class GrapheConstructeur implements GrapheCheminTestEcouteur {
	public List<GrapheSommet> sommets = new ArrayList<>();
	public List<GrapheArc> arcs = new ArrayList<>();
	public boolean liaisonDirecte[][];
	

	public BrickEditor be;
	public boolean etatLiaison;
	public float rayon;
	public boolean peutVoler;
	public float hauteur;
	static public float distanceMax = 31;

	public GrapheConstructeur(BrickEditor be, List<GrapheSommet> sommets, float rayon, boolean peutVoler, float hauteur,
			boolean grapheSimple) {
		this.sommets = sommets;
		this.be = be;
		this.hauteur = hauteur;
		this.peutVoler = peutVoler;
		this.rayon = rayon;
		liaisonDirecte = new boolean[sommets.size()][sommets.size()];
		
		Vector3f tmp = new Vector3f();
		for (int i = 0; i < sommets.size(); i++) {
			for (int j = i + 1; j < sommets.size(); j++) {
				liaisonDirecte[i][j] = false;
				liaisonDirecte[j][i] = false;
				
				GrapheArc ga = new GrapheArc();
				ga.a = i;
				ga.b = j;
				ga.gc = this;
				tmp.set(sommets.get(i).position);
				tmp.subtractLocal(sommets.get(j).position);
				ga.distance = tmp.length();
				if (ga.distance < distanceMax || !grapheSimple) {
					arcs.add(ga);
				}

			}
		}
		Collections.sort(arcs, new Comparator<GrapheArc>() {

			@Override
			public int compare(GrapheArc o1, GrapheArc o2) {
				// TODO Auto-generated method stub
				if (o1.distance == o2.distance) {
					return 0;
				}
				if (o1.distance < o2.distance) {
					return -1;
				}
				return 1;
			}

		});

	}

	public List<GrapheSommet> racines() {
		List<GrapheSommet> r = new ArrayList<>();
		for (int i = 0; i < sommets.size(); i++) {
			if (totalSuivant(i) == 1) {
				r.add(sommets.get(i));
			}

		}
		return r;

	}

	public int totalSuivant(int i) {
		int n = 0;
		for (int j = 0; j < sommets.size(); j++) {
			if (liaisonDirecte[i][j]) {
				n++;
			}
		}
		return n;

	}

	public Graphe calculer(float vitesse) {

		for (int i = 0; i < sommets.size(); i++) {
			for (int j = i + 1; j < sommets.size(); j++) {
				liaisonDirecte[i][j] = false;
				liaisonDirecte[j][i] = false;
			}
		}

		boolean suivants[] = new boolean[sommets.size()];
		long millisecond = System.currentTimeMillis();
		for (int i = 0; i < arcs.size(); i++) {
			GrapheArc a = arcs.get(i);

			if (System.currentTimeMillis() - millisecond > 1000) {
				Log.print(" avancement =" + i + "/" + arcs.size() + " distance=" + a.distance);
				millisecond = System.currentTimeMillis();
			}
			
				reset(suivants);
				this.suivants(a.a, suivants);
		

			if (!suivants[a.b] && this.accessible(a, rayon, vitesse, peutVoler, hauteur)) {
				liaisonDirecte[a.a][a.b] = true;
				liaisonDirecte[a.b][a.a] = true;
			

			}
		}
		return new Graphe(this);

	}

	public Graphe calculerGrapheSimple(float vitesse) {

		for (int i = 0; i < sommets.size(); i++) {
			for (int j = i + 1; j < sommets.size(); j++) {
				liaisonDirecte[i][j] = false;
				liaisonDirecte[j][i] = false;
			}
		}

		long millisecond = System.currentTimeMillis();
		boolean suivants[] = new boolean[sommets.size()];
		for (int i = 0; i < arcs.size(); i++) {
			GrapheArc a = arcs.get(i);

			if (System.currentTimeMillis() - millisecond > 1000) {
				Log.print(" avancement =" + i + "/" + arcs.size() + " distance=" + a.distance);
				millisecond = System.currentTimeMillis();
			}
			a.run();
			if (a.utilisable &&  this.accessible(a, rayon, vitesse, peutVoler, hauteur)) {
				
				//	reset(suivants);
				//	this.suivants(a.a, suivants);
					
				
				//if (!suivants[a.b]) {
					liaisonDirecte[a.a][a.b] = true;
					liaisonDirecte[a.b][a.a] = true;
				
			//	}

			}
		}
		return new Graphe(this);

	}

	public Vector3f pos(int i) {
		return sommets.get(i).position;
	}

	public boolean accessible(GrapheArc ga, float rayon, float vitesse, boolean peutVoler, float hauteur) {
		GrapheCheminTest gct = new GrapheCheminTest(be, this, pos(ga.a), pos(ga.b), rayon, vitesse, peutVoler, hauteur);
		/*
		 * if (!be.estAccessibleDepuis(pos(ga.a), pos(ga.b), rayon / 2.0f, rayon
		 * / 2.0f, null)) { etatLiaison = false;
		 * 
		 * } else {
		 */
		while (gct.executer())
			;
		// }
		return etatLiaison;

	}

	public void suivants(int i, boolean[] suivants) {

		for (int u = 0; u < sommets.size(); u++) {

			if (liaisonDirecte[i][u] && !suivants[u]) {
				suivants[u] = true;

				this.suivants(u, suivants);
			}

		}
	

	}

	public void reset(boolean suivants[]) {
		for (int i = 0; i < suivants.length; i++) {
			suivants[i] = false;
		}
	}

	@Override
	public void valider() {
		// TODO Auto-generated method stub
		etatLiaison = true;
	}

	@Override
	public void annuler() {
		// TODO Auto-generated method stub
		etatLiaison = false;
	}

	@Override
	public void supprimer() {
		// TODO Auto-generated method stub
		etatLiaison = false;
	}

}
