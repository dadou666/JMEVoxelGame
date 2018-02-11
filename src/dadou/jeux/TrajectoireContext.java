package dadou.jeux;

import java.util.ArrayList;
import java.util.List;

import dadou.Arbre;
import dadou.Espace;
import dadou.JeuxException;
import dadou.MondeInterfacePrive;
import dadou.MondeInterfacePublic;
import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;

public class TrajectoireContext {
	public Espace espace;
	public ObjetMobile om;
	public int idxDeplacement = 0;
	public Arbre<Trajectoire> trajectoire;
	public Chemin chemin;
	

	public int n = 0;
	public boolean executerEvenement = true;

	public String position() {
		String s = "";
		Arbre<Trajectoire> parent = trajectoire.donnerParent();
		Arbre<Trajectoire> arbre = trajectoire;
		while (parent != null) {
			s += "/" + arbre.nom;
			arbre = parent;
			parent = arbre.donnerParent();

		}

		s += "[" + (this.idxDeplacement + 1) + ":"
				+ this.trajectoire.valeur.deplacements.size() + "]";
		return s;

	}

	public void initialiserFin() {
		int dx = 0;
		int dy = 0;
		int dz = 0;
		Arbre<Trajectoire> arbre = om.model.trajectoires;
		Arbre<Trajectoire> dernier = arbre;
		while (arbre != null) {
			for (DeplacementObjet dep : arbre.valeur.deplacements) {
				dx += dep.dx * dep.distance;
				dy += dep.dy * dep.distance;
				dz += dep.dz * dep.distance;

			}
			arbre = chemin.map.get(arbre);
			if (arbre != null) {
				dernier = arbre;
			}

		}
		om.reset();
		om.move(dx, dy, dz);
		this.trajectoire = dernier;
		if (!dernier.valeur.deplacements.isEmpty()) {
			this.idxDeplacement = dernier.valeur.deplacements.size() - 1;

			this.n = 0;
		}

	}

	public boolean estFini() {
		if (trajectoire.valeur.deplacements.isEmpty()) {
			return true;
		}
		if (chemin.map.get(trajectoire) != null) {
			return false;
		}
		return (trajectoire.valeur.deplacements.size() - 1 == idxDeplacement);
	}

	public TrajectoireContext(Arbre<Trajectoire> trajectoire, Chemin chemin,
			ObjetMobile om) {
		this.trajectoire = trajectoire;
		this.chemin = chemin;
		this.om = om;
		
		om.tc = this;
	}

	public void reset() {
		om.reset();
	}

	public float dx(int sens) {
		float r = trajectoire.valeur.deplacements.get(idxDeplacement).dx * sens;
		return r / om.factorSpeed;
	}

	public float dy(int sens) {
		float r = trajectoire.valeur.deplacements.get(idxDeplacement).dy * sens;
		return r / om.factorSpeed;
	}

	public float dz(int sens) {
		float r = trajectoire.valeur.deplacements.get(idxDeplacement).dz * sens;
		return r / om.factorSpeed;
	}

	public int distance() {
		int r = trajectoire.valeur.deplacements.get(idxDeplacement).distance;
		return r * (int) om.factorSpeed;
	}

	public boolean inverse = false;
	public boolean stop = false;

	public boolean executer() throws JeuxException {
	//	om.dep.set(0,0,0);
		if (stop) {
			return true;
		}
		if (trajectoire.valeur.deplacements.isEmpty()) {
			return false;
		}
		int sens = 1;

		if (inverse) {
			sens = -1;
		}
		if (n < distance()) {
			float dx = dx(sens);
			float dy = dy(sens);
			float dz = dz(sens);

			
			om.move(dx, dy, dz);
			om.dep.set(dx,dy,dz);
			n++;
			return true;

		}
		int idx = idxDeplacement + sens;
		n = 0;
		if (idx == trajectoire.valeur.deplacements.size() || idx < 0) {
			executerEvenement = true;
			stop = true;
			this.om.evenement().finTrajectoire(this.om);
			if (inverse) {
				if (trajectoire.donnerParent() == null) {

					executerEvenement = false;
					return !stop;
				}

				trajectoire = trajectoire.donnerParent();
				
				idxDeplacement = trajectoire.valeur.deplacements.size() - 1;
				n = 0;

			}

			return this.executerEvenement("event");
		}
		idxDeplacement = idx;
		return true;
	}




	

	public boolean demarer(String nom) {
	

		Arbre<Trajectoire> t = this.trajectoire.enfant(nom);
		if (t == null) {
			return false;
		}
	
		executerEvenement = false;
		stop = false;
		this.trajectoire = t;
		this.idxDeplacement = 0;
		this.inverse = false;
		return true;

	}

	public boolean executerEvenement(String origine) {
		if (chemin != null) {
			executerEvenement = false;
			stop = false;
			if (inverse) {
				return true;
			}

			Arbre<Trajectoire> arbre = chemin.map.get(this.trajectoire);
			if (arbre != null) {
				this.trajectoire = arbre;
				this.idxDeplacement = 0;
				return true;
			}
			return false;

		}
	
		if (trajectoire.valeur.event == null) {
			return true;
		}
		if (trajectoire.valeur.event.source == null) {
			return true;
		}

		trajectoire.valeur.event.setVar("nomObjet", om.donnerNom());
		trajectoire.valeur.event.setVar("origine", origine);
		trajectoire.valeur.event.exec();
		
		return true;

	}

}
