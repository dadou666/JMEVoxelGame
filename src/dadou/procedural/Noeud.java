package dadou.procedural;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Noeud<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<String> suivants = new ArrayList<>();
	public T data;
	public void ajouterSuivant(String a) {
		if (suivants.contains(a)) {
			return;
		}
		suivants.add(a);
	}
}
