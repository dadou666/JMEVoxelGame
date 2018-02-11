package dadou.parseur;

public class ElementSansErreur extends Element {
	public boolean parser(Parseur system,Object result)  throws SyntaxError{
		
		
		return this.regle.parser(system, result,null);
		
	}

}
