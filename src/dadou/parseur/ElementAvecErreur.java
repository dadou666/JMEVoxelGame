package dadou.parseur;

public class ElementAvecErreur extends Element {
	
	public String erreur;
	public boolean parser(Parseur system,Object result)  throws SyntaxError{
		
		
		return this.regle.parser(system, result,erreur);
		
	}

}
