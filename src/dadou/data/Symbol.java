package dadou.data;

import java.lang.reflect.Field;
import java.util.List;

public class Symbol extends Obj {
	public String valeur;

	public Symbol(String valeur) {
		this.valeur = valeur;
	}

	public String toString() {
		return valeur;
	}
	public void init(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getField(fieldName);
			field.set(obj, this);
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}
	public void addIn(List list) {
		list.add(this);
	}
}
