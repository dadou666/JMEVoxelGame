package dadou;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConstructionModelClasse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ModelClasse> list = new ArrayList<>();
	public ConstructionModelClasse(ModelClasse mc) {
		for(int y=0;y < mc.dy ; y++) {
			ModelClasse tmp=mc.cloner();
			for(int uy=y+1;uy < mc.dy;uy++) {
				for(int x=0;x < mc.dx ;x++) {
					for(int z=0;z < mc.dz ;z++) {
						tmp.copie[x][uy][z] = Color.BLACK;
					}
				}
			}
			list.add(tmp);
			
		}
		
		
	}

}
