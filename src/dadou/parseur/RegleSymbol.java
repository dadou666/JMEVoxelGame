package dadou.parseur;

public class RegleSymbol extends Regle {
	public Object parser(Parseur system) {
		if (system.estFini()) {
			return null;
		}

		system.passerCaracteresInutile();
		StringBuilder sb = new StringBuilder();
		while (true) {
			if (system.estFini()) {
				if (sb.length() == 0) {
					return null;
				}
				return sb.toString();
			}
			char c = system.caracterCourant();
			if (Character.isDigit(c) || Character.isAlphabetic(c) || c == '_') {
				sb.append(c);
				system.caracterSuivant();
			} else {
				if (sb.length() == 0) {
					return null;
				}
				return sb.toString();
			}

		}

	}
}
