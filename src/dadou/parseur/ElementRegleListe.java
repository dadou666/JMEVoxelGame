package dadou.parseur;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ElementRegleListe extends ElementRegle {
	public String separateur;
	public TypeListe typeListe;

	public boolean peutEtreVide() {
		return true;
	}

	public boolean parser(Parseur system, Object result, String syntaxError) throws SyntaxError {
		List<Object> liste = new ArrayList<>();
		while (true) {
			Object valeur = system.parse(nomRegle);
			if (valeur == null) {
				if (!liste.isEmpty() && !separateur.isEmpty()) {
					throw new SyntaxError(" attendu " + nomRegle);
				}
				break;
			}
			if (!separateur.isEmpty() && !system.verifierConstante(separateur)) {
				break;
			}
			liste.add(valeur);

		}

		try {
			if (syntaxError != null) {
				if (liste.isEmpty() && !this.typeListe.peutEtreVide()) {
					throw new SyntaxError(syntaxError);
				}
			}
			Field field = result.getClass().getField(variable);
			field.set(result, liste);
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
