package dadou.tools.canon;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

public abstract class FormeCible {

	static Map<String, FormeCible> formes ;

	static public Map<String, FormeCible> formes()

	{
		if (formes == null) {
			formes = new HashMap<>();
			formes.put("cercle", new FormeCercle());
			formes.put("losange", new FormeLosange());
			formes.put("croix", new FormeCroix());
		}
		return formes;

	}

	abstract public void forme(Graphics2D g);
}
