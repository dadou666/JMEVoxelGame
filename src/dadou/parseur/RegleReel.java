package dadou.parseur;

public class RegleReel extends Regle {
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
				return Float.parseFloat(sb.toString());
			}
            if (system.estCaractereInutile()) {
            	return Float.parseFloat(sb.toString());
            }
			c = system.lire();
			if (!Character.isDigit(c)) {
				if (c =='.') {
					sb.append('.');
					break;
				}
				return null;
			}
			sb.append(c);
            
		}
		while( true) {
			if (system.estFini()) {
				return Float.parseFloat(sb.toString());
			}
            if (system.estCaractereInutile()) {
            	return Float.parseFloat(sb.toString());
            }
			c = system.lire();
			if (!Character.isDigit(c)) {
				return null;
			}
			sb.append(c);
            
		}
		
	}
	

}
