package dadou;

public class NomTexture {
	static public class PlusDeTexture extends Exception {
		public String nom;
		public PlusDeTexture(String nom) {
			this.nom= nom;
		}
		public String toString() {
			return nom;
		}

	}

	public String nom;

	public int idx;

	public String toString() {
		return nom;
	}

	int idx() {
		int r = -1;
		for (int u = 0; u < nom.length(); u++) {
			if (nom.charAt(u) == '_') {
				if (nom.indexOf('_', u + 1) < 0) {
					return r;
				}
				r = u;
			}
		}
		return r;

	}

	public String position() {

		int idx = idx();
		if (idx < 0) {
			return "";
		}
		String r = nom.substring(idx + 1);
		return r;

	}
}
