package dadou.procedural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Zone {

	public List<Piece> pieces = new ArrayList<Piece>();



	public boolean verifier() {
		boolean existH0 = false;
		for (Piece p : pieces) {
		
			for (Piece q : p.proches) {
				if (Math.abs(p.h - q.h) > 1) {
					return false;
				}
			}

		}

		return true;

	}

	public Piece entree;
	public Piece sortie;
	public List<Piece> sols = new ArrayList<Piece>();

	public boolean modifier(ListeAleatoire<Piece> la) {
		Piece p;

		while ((p = la.donnerElement()) != null) {

			p.h++;
			if (this.verifier()) {
				la.reset();
				return true;
			}
			p.h--;

		}
		return false;

	}

	public void calculerHauteur(Monde m) {

		int numIteration = 20 * pieces.size();
		int initTotal =  20 * pieces.size();
		List<Piece> ls = new ArrayList<Piece>();
		ls.addAll(pieces);
		ls.remove(entree);
		ls.remove(sortie);
		ListeAleatoire<Piece> la = new ListeAleatoire<Piece>(pieces);
		int nbSol = Math.max(pieces.size() / 100, 1);
		System.out.println(" nbSol " + nbSol );
		while (nbSol > 0) {
			Piece p = la.donnerElement();
			ls.remove(p);
			nbSol--;

		}
		if (ls.isEmpty()) {
			return;
		}
		la = new ListeAleatoire<Piece>(ls);
		int total = 0;
		while (numIteration > 0) {
			if (!this.modifier(la)) {
			
				break;

			} 
			total++;
			numIteration--; 

		}
		System.out.println(" total ="+total+"/"+initTotal);

	}

	public void creerGraphhe() {
		for (int i = 0; i < pieces.size(); i++) {

			for (int j = i + 1; j < pieces.size(); j++) {
				Piece a = pieces.get(i);
				Piece b = pieces.get(j);
				if (a.intersection(b)) {
					a.proches.add(b);
					b.proches.add(a);

				}

			}

		}
	}

	public boolean verifier(List<Piece> ls, Piece p, Monde monde) {
		for (Separation sep : monde.separations) {
			if (sep.a.intersection(p)) {
				return false;
			}
			if (sep.b.intersection(p)) {
				return false;
			}

		}
		for (Piece q : ls) {
			if (q.intersection(p)) {
				return false;
			}
		}
		return true;
	}

}
