package dadou.mutator;

import dadou.CameraPosition;
import dadou.Log;
import dadou.ObjetMobilePourModelInstance;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheElement;
import dadou.tools.BrickEditor;

public class ParcourGraphe extends Parcour {

	public GrapheElement ge;
	public String nomPrecedent;
	public GrapheElement racine;
	public Graphe graphe;

	public ParcourGraphe(BrickEditor be, ObjetMobilePourModelInstance obj, Graphe graphe, String nom,
			GrapheElement racine) {
		this.be = be;
		this.graphe = graphe;
		this.obj = obj;
		// Log.printCaller("occupation racine " + this.obj);
		this.nom = nom;
		racine.reservation = obj;
		this.racine = racine;
		ge = graphe.donnerGrapheElement(nom);
		this.nomPrecedent = racine.nom();
		mutator = this.creerMouvement(ge.nom());

	}

	public String nom;

	public void libererRacine() {

		if (racine == null) {
			return;
		}

		// Log.printCaller("liberation racine " + this.obj);
		if (racine.reservation == this.obj) {
			racine.reservation = null;

			racine = null;
		} else {
			//Log.print(" pas de liberation de racine");
		}
	}

	public Mutator interrompre(Mutator m) {
		if (m.estOrientation()) {
			if ((this.mutator != null && this.mutator.getClass() == Orientation.class) || nom == null) {
				interruption = false;
				return this;
			}
			interruption = true;
			this.mutatorInterruption = m;

			return this;
		}
		if (ge != null) {
			ge.reservation = null;
			if (racine!=null && racine.reservation == this.obj) {
				racine.reservation = null;
			}

		}
		this.libererRacine();
		return m;
	}

	public void fini() {
		if (ge != null) {
			ge.reservation = null;

		}
		this.libererRacine();

	}
	public static boolean trace = false;
	public boolean animer() {
	
		if (this.mutatorInterruption != null) {
		 
			boolean r = mutatorInterruption.animer();
			this.mutator = null;
			if (r) {
				return r;
			}
			if (mutatorInterruption.collision) {
				mutatorInterruption = null;
				if (racine != null) {
					racine.reservation = null;
					racine = null;
				}
				return false;
			}
			mutatorInterruption = null;
			if (racine != null) {

			}
			mutator = this.creerOrientation(nom);

		}
		if (interruption) {
			return true;
		}
		if (mutator == null) {

			String tmp = nomPrecedent;
			if (nom != null) {
				nomPrecedent = nom;
			}

			nom = ge.suivantLibre(obj, tmp);

			if (nom == null) {
				nom = ge.suivantLibre(obj, null);
				nomPrecedent = null;
				if (nom == null) {
					return true;
				}
			}

			ge.reservation = null;
			ge = this.ge.graphe.donnerGrapheElement(nom);
			mutator = this.creerOrientation(nom);

		}
		boolean r = mutator.animer();
	
		if (r) {
			return r;
		}
		if (mutator.collision) {
			ge.reservation = null;
			this.libererRacine();
			
			return false;
		}
		if (mutator.getClass() == Orientation.class) {
			mutator = this.creerMouvement(ge.nom());
			this.libererRacine();
		} else {
			mutator = null;
			return true;
		}
		r = mutator.animer();
		 
		if (!r) {
			if (mutator.collision) {
				ge.reservation = null;
				this.libererRacine();
				return false;
			}
			mutator = null;
		}
		return true;

	}

	public boolean estActif() {
		if (obj.mutator == this) {
			return true;
		}
		ge.reservation = null;
		return false;
	}
	public boolean estOrientation() {
		if (mutator != null) {
			return mutator.estOrientation();
		}
		if (this.mutatorInterruption != null) {
			return this.mutatorInterruption.estOrientation();
		}
		return false;
	}

}
