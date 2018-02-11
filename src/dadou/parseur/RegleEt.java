package dadou.parseur;

import java.util.List;

public class RegleEt extends Regle{

	public List<Element> elements;
	public Object parser(Parseur system,String nomRegle) throws SyntaxError {
		try {
			int idx= system.idx();
			Object result = Class.forName(system.packageName+nomRegle);
			for(Element elt:elements) {
				if (!elt.parser(system, result)) {
					system.idx(idx);
					return null;
				}
			}
			return result;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

}
