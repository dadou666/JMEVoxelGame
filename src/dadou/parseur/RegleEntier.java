package dadou.parseur;

public class RegleEntier extends Regle {
	public Object parser(Parseur system) {
		system.passerCaracteresInutile();
		if (system.estFini()) {
			return null;
		}
		char c = system.lire();
		
		StringBuilder sb= new StringBuilder();
		sb.append(c);
		if (c == '-') {
		
			if (system.estFini()) {
				return null;
			}
			c = system.lire();
			if (!Character.isDigit(c)) {
				return null;
				
			}
			sb.append(c);
		} else {
			if (!Character.isDigit(c)) {
			return null;
			}
		}
		while( true) {
			if (system.estFini()) {
				return Integer.parseInt(sb.toString());
			}
			char cc=system.caracterCourant();
            if (system.estCaractereInutile() || (!Character.isDigit(cc) && !Character.isAlphabetic(cc))) {
            	return Integer.parseInt(sb.toString());
            }
			c = system.lire();
			if (!Character.isDigit(c)) {
				return null;
			}
			sb.append(c);
            
		}
		
		
		
		
		
	}
}
