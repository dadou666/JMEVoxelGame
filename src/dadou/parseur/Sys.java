package dadou.parseur;

import java.util.List;
import java.util.Map;

public class Sys {

	public List<Regle> regles;

	public Sys() {

	}

	public Sys ajouter(String nom, Regle rgl) {
		return null;
	}

	public void donnerRegles(Map<String, Regle> map) throws DuplicationRegle {
		for (Regle rgl : regles) {
			if (map.get(rgl.nomRegle) != null) {
				throw new DuplicationRegle(rgl.nomRegle);

			}
			map.put(rgl.nomRegle, rgl);
		}

	}

	static public class DuplicationRegle extends Exception {
		public DuplicationRegle(String msg) {
			super(msg);
		}

	}

}
