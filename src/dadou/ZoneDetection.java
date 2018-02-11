package dadou;

import java.util.ArrayList;
import java.util.List;

import com.jme.bounding.BoundingBox;

 public class ZoneDetection {
	public List<ObjetMobile> contenu=new ArrayList<>();
	public ZoneDetection suivante;

	public int idx=0;
	public void reset() {
		idx = 0;
		
	}
	public void ajouter(ObjetMobile om) {
		if (idx == contenu.size()) {
			contenu.add(om);
		
		
		} else {
		
		contenu.set(idx,om);
		}
		idx++;
	}
	public ObjetMobile element(int i) {
		if (i >= idx ) {
			return null;
		}
		return contenu.get(i);
	}
	public int taille() {
		return idx;
	}
	
	
}
