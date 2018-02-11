package dadou.parseur;

import java.lang.reflect.Field;

public class ElementRegle extends ElementAbstract {
	public String nomRegle;
	public String variable;
	public boolean parser(Parseur system,Object result,String syntaxError) throws SyntaxError {
		Object valeur = system.parse(nomRegle);
		if (valeur == null) {
			if (syntaxError == null) {
				return false;
			}
			throw new SyntaxError(syntaxError);
		}
		try {
			Field field = result.getClass().getField(variable);
			field.set(result, valeur);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return true;
		
		
		
		
		
		
	}

}
