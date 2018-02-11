package dadou.parseur;

import java.util.List;

public class RegleOu extends Regle {
	public List<String> noms;
	public Object parser(Parseur system,String nomRegle) throws SyntaxError {
		for(String nom:noms) {
			Object result = system.parse(nom);
			if (result != null) {
				return result;
			}
		}
		return null;
		
	}

}
