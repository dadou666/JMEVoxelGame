package dadou.data;

import java.lang.reflect.Field;
import java.util.List;

public class Int extends Obj {
	public int value;

	public Int(int value) {
		this.value = value;
	}

	public String toString() {
		return "" + value;
	}

	public void init(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getField(fieldName);
			field.setInt(obj, value);
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}

	public void addIn(List list) {
		list.add(value);
	}

}
