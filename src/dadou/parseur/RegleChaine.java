package dadou.parseur;

public class RegleChaine extends Regle {
	public Object parser(Parseur system) {
		system.passerCaracteresInutile();
		char c = system.lire();
		if (c != '"' && c!='\'') {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		char u;
		while (!system.estFini()) {
			u = system.lire();
			if (u==c) {
				return sb.toString();
			}
			sb.append(u);
		}
		
		return null;
		
	}
}
