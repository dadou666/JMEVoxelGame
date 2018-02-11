package dadou.data;

import java.lang.reflect.Field;
import java.util.List;

public class Str extends Obj {
	public String value;
	public Str(String s) {
		this.value=s;
	}
	public String toString() {
		if ( value.contains("'")) {
			return "\""+value+"\"";
			
			
		} else {
			return "'"+value+"'";
			
		}
	}
	public void init(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getField(fieldName);
			field.set(obj, value);
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}
	public void addIn(List list) {
		list.add(value);
	}


}
