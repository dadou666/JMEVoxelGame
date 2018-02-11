package dadou.parseur;

import java.util.HashMap;
import java.util.Map;

import dadou.parseur.Sys.DuplicationRegle;

public class Parseur {
	private Map<String, Regle> regles = new HashMap<>();
	private int idx;
	private String source;
	public String packageName;

	public Parseur(Sys system,String packageName) throws DuplicationRegle {
		if (system != null) {
			system.donnerRegles(regles);

		}
		this.packageName = packageName;
		
		this.regles.put("entier", new RegleEntier());
		this.regles.put("reel", new RegleReel());
		this.regles.put("chaine", new RegleChaine());
		this.regles.put("symbol", new RegleSymbol());

	}

	public boolean verifierConstante(String valeur) {
		int oldIdx= idx;
		passerCaracteresInutile();
		for (int i = 0; i < valeur.length(); i++) {
			if (valeur.charAt(i) != caracterCourant()) {
				idx = oldIdx;
				return false;

			}
			caracterSuivant();
		}
		return true;

	}

	public Object parse(String nomRegle, String source) {

		this.source = source;
		this.idx = 0;
		Regle regle = regles.get(nomRegle);
		return regle.parser(this);

	}

	public Object parse(String nomRegle) {

		Regle regle = regles.get(nomRegle);
		return regle.parser(this);

	}

	public char lire() {
		char c = this.source.charAt(idx);
		idx++;
		return c;
	}

	public void caracterSuivant() {
		idx++;
	}

	public char caracterCourant() {
		char c = this.source.charAt(idx);

		return c;
	}

	public boolean estFini() {
		return idx >= this.source.length();
	}

	public boolean estCaractereInutile() {
		char c = this.source.charAt(idx);
		if (c == ' ') {
			return true;
		}
		if (c == '\n') {
			return true;
		}
		if (c == '\t') {
			return true;
		}
		return false;
	}

	public void passerCaracteresInutile() {

		while (idx < this.source.length() && this.estCaractereInutile()) {
			idx++;

		}

	}

	public int idx() {
		return this.idx;
	}

	public void idx(int v) {
		this.idx = v;
	}

}
