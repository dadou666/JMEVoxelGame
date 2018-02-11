package dadou.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Collection extends Obj {
	public List<Obj> values = new ArrayList<>();

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		boolean first = true;
		for (Obj o : values) {
			if (!first) {
				sb.append(" ");
			}
			sb.append(o);
			first = false;
		}
		sb.append("]");
		return sb.toString();
	}
	
	public void init(Object obj, String fieldName) {
		List list = new ArrayList();
		for (Obj o : values) {
			o.addIn(list);

		}
		try {
			Field field = obj.getClass().getField(fieldName);
			field.set(obj, list);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}
