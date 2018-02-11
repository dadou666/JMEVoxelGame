package dadou.parseur;

public class ElementConstante extends ElementAbstract{
	public String valeur;
	public boolean parser(Parseur system,Object result,String syntaxError) throws SyntaxError {
		if (!system.verifierConstante(valeur)) {
			if (syntaxError !=null) {
				throw new SyntaxError(syntaxError);
			}
			return false;
			
		}
		
		return true;
		
	}

}
