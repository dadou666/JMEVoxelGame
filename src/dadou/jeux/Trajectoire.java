package dadou.jeux;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dadou.ObjetMobile;
import dadou.event.GameEventLeaf;

public class Trajectoire implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3688532713789375689L;
	public  GameEventLeaf event;
	public String chemin;

	public List<DeplacementObjet> deplacements = new ArrayList<DeplacementObjet>();
	public int nbDeplacement() {
		return deplacements.size();
	}
	public void ajouter(DeplacementObjet dep) {
		deplacements.add(dep);
		
	}

	public void ajouter(int dx, int dy, int dz) {
		if (deplacements.isEmpty()) {
			DeplacementObjet dep = new DeplacementObjet();
			dep.distance = 1;
			dep.dx = dx;
			dep.dy = dy;
			dep.dz = dz;
			deplacements.add(dep);

		} else {
			DeplacementObjet dep = deplacements.get(deplacements.size() - 1);
			if (dep.dx == dx && dep.dy == dy && dep.dz == dz) {
				dep.distance++;
				return;
			}
			dep = new DeplacementObjet();
			dep.distance = 1;
			dep.dx = dx;
			dep.dy = dy;
			dep.dz = dz;
			deplacements.add(dep);

		}
	}
	public void annuler(ObjetMobile om) {
		if (deplacements.isEmpty()) {
			return;
		}
		DeplacementObjet dep = deplacements.get(deplacements.size()-1);
		dep.distance --;
		om.move(-dep.dx, -dep.dy, -dep.dz);
		if (dep.distance == 0){
			deplacements.remove(dep);
		}
		
	}
	

}
