package dadou.procedural;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListeAleatoire<T> {
	List<T> elements;
	int max;
	Random r;

	public ListeAleatoire(List<T> elements) {
		this.elements = new ArrayList<T>();
		this.elements.addAll(elements);
		r = new Random();
		max = this.elements.size()-1;
	}

	public T donnerElement() {
		if (max == 0) {
			return null;
		}
		int idx = r.nextInt(max);
		T r = this.elements.get(idx);
		this.retirer(idx);
		return r;
	}

	public void retirer(int idx) {
		T r = this.elements.get(idx);
		
		elements.set(idx, this.elements.get(max));
		elements.set(max, r);

		max--;

	}
	public void reset() {
		max =this.elements.size()-1;
	}

}
