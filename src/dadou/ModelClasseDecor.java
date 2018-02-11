package dadou;

import java.awt.Color;
import java.io.Serializable;

import com.jme.math.Vector3f;

public class ModelClasseDecor extends ModelClasse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean estElementDecor() {
		return true;
	}
	public ModelClasse cloner() {

		ModelClasseDecor r = new ModelClasseDecor();
		r.copie = new Color[dx][dy][dz];
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					r.copie[x][y][z] = copie[x][y][z];
				}

			}
		}
		r.dx = dx;
		r.dy = dy;
		r.dz = dz;
		r.size = new Vector3f(dx, dy, dz);
		r.nomHabillage = nomHabillage;
		return r;

	}
}
