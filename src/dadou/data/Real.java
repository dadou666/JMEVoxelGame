package dadou.data;

import java.lang.reflect.Field;
import java.util.List;

public class Real extends Obj {
	public float value;
	public Real(float v) {
		this.value=v;
	}
	public String toString() {
		return  ""+value;
	}
	public void init(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getField(fieldName);
			field.setFloat(obj, value);
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}
	public void addIn(List list) {
		list.add(value);
	}


}
