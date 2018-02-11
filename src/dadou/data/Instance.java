package dadou.data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instance extends Obj {
	public String type;
	public Map<String, Obj> values = new HashMap<>();

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(type);
		sb.append("{");
		for (Map.Entry<String, Obj> e : values.entrySet()) {
			sb.append(e.getKey());
			sb.append(" ");
			sb.append(e.getValue());
			sb.append(" ");
		}
		sb.append("}");
		return sb.toString();
	}

	public Object createObject() {
		 
	
		try {
			Class cls = Class.forName(type);
			Object r = cls.newInstance();
			for (Map.Entry<String, Obj> e : values.entrySet()) {
				e.getValue().init(r, e.getKey());
				
			}
			return r;
		} catch(Throwable t) {
			t.printStackTrace();
			
		}
		return null;


	}
	public void init(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getField(fieldName);
			field.set(obj, this.createObject());
		} catch (Throwable t) {
			t.printStackTrace();

		}

	}
	public void addIn(List list) {
		Object o = this.createObject();
		if (o == null) {
			return;
		}
		list.add(o);
	}


}
