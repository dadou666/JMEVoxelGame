package dadou.exploration;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;

public class DirectionExploration {
	
	public List<Vector3f> positions = new ArrayList<>();
	public int size=0;
	public boolean estPrioritaire;
	public int totalLibre=0;
	public float angle = 0.0f;
	public int idxDir;
	
	public Vector3f ajouter(Vector3f position) {
		if (size == positions.size()) {
			positions.add(new Vector3f());
		}
		Vector3f r=positions.get(size);
		r.set(position);
		size++;
		return r;
	}
	public int critereSelection() {
		return totalLibre;
	}
	public Vector3f destination() {
		if (size == 0) {
			return null;
		}
		return positions.get(size-1);
	}

}
