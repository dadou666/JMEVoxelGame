package dadou.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Functor extends Collection {
	public String name;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(name);
		sb.append("(");
		boolean first = true;
		for (Obj o : values) {
			if (!first) {
				sb.append(" ");
			}
			sb.append(o);
			first = false;
		}
		sb.append(")");
		return sb.toString();
	}

	public void init(Object obj, String fieldName) {
		try {
			List list = (List) Class.forName(name).newInstance();
			for (Obj o : values) {
				o.addIn(list);

			}
			try {
				Field field = obj.getClass().getField(fieldName);
				field.set(obj, list);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}
}
