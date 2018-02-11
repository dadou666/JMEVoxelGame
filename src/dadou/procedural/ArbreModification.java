package dadou.procedural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dadou.Log;

public class ArbreModification extends Modification {
	public ArbreModification PX;
	public ArbreModification MX;
	public ArbreModification PY;
	public ArbreModification MY;
	public ArbreModification PZ;
	public ArbreModification MZ;
	public static int profondeurMin = 6;
	public static int profondeurMax = 8;
	public List<String> types;
	public ArbreModification parent;

	public boolean estVide(MondeGenere mg) {
		String type = mg.contenu[x][y][z];
		return type == null;

	}
	public String toString() {
		return ""+type+"("+x+","+y+","+z+")";
	}

	public void ecrire(MondeGenere mg, String s) {
		mg.contenu[x][y][z] = s;
	}

	public int nbModification() {
		int n = 1;
		if (PX != null) {
			n += PX.nbModification();
		}
		if (PY != null) {
			n += PY.nbModification();
		}
		if (PZ != null) {
			n += PZ.nbModification();
		}
		if (MX != null) {
			n += MX.nbModification();
		}
		if (MY != null) {
			n += MY.nbModification();
		}
		if (MZ != null) {
			n += MZ.nbModification();
		}
		return n;

	}

	public ArbreModification(int x, int y, int z, List<String> types) {
		this.types = types;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ArbreModification(int x, int y, int z) {

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ArbreModification(Regles regles, MondeGenere mg) throws InvalidePos, InvalidValue {

		this.x = 0;

		this.y = 0;

		this.z = 0;

		mg.contenu[x][y][z] = null;

		this.generer(regles, mg, 0, false);

	}

	public int comparer(Regle ra, Regle rb) {
		return Float.compare(ra.coeffTri(), rb.coeffTri());

	}

	public boolean tester(int profondeur, Regle regle, boolean estChemin) {
		int nbConnexion = regle.rd.nbConnexion();
		if (estChemin) {
			if (profondeur >= profondeurMax) {
				return nbConnexion == 1;
			}
			return nbConnexion!= 1;
		}
		if (profondeur == 0) {
			return nbConnexion == 1;
		}

		return nbConnexion != 1;// nbConnexion == 0 || nbConnexion == 2
								// ||nbConnexion==3 || nbConnexion==4 ;
	}

	public boolean generer(Regles regles, MondeGenere mg, int profondeur, boolean estChemin)
			throws InvalidePos, InvalidValue {
		if (profondeur > profondeurMax && !estChemin) {
			
			return false;
		}
		List<String> types = mg.listeTypePossible(regles, x, y, z);

		type = null;

		if (types == null) {
			return false;

		}
		Collections.sort(types, (String a, String b) -> {
			Regle ra = regles.regles.get(a);
			Regle rb = regles.regles.get(b);
			return comparer(ra, rb);

		});

		for (String type : types) {

			Regle rgl = regles.regles.get(type);

			if (tester(profondeur, rgl,estChemin)) {
				mg.contenu[x][y][z] = type;

				// Log.print(" test : " + x + y + z + " =>" + type);
				rgl.ajouterPoid();
				if (this.generer(regles, rgl, mg, profondeur + 1, estChemin)) {
			
					return true;
				}

				// rgl.supprimerPoid();
				reset(mg, regles);
				// Log.print(" test fail: "+x+y+z+" =>"+type);
			}
		}

		return false;
	}

	public void reset(MondeGenere mg, Regles regles) {
		if (!this.existe(mg)) {
			return;
		}
		String type = mg.contenu[x][y][z];
		mg.contenu[x][y][z] = null;
		if (type != null) {
			Regle r = regles.regles.get(type);
			r.supprimerPoid();
		}
		for (ArbreModification am : this.ls) {
			am.reset(mg, regles);
		}
		PX = null;
		PY = null;
		PZ = null;
		MX = null;
		MY = null;
		MZ = null;

	}

	List<ArbreModification> ls = new ArrayList<>();

	public ArbreModification creer(List<String> types, Regles regles, MondeGenere mg, int px, int py, int pz,
			int profondeur, boolean estChemin) throws InvalidePos, InvalidValue, GenerationFail {
		if (types == null) {
			return null;
		}
		ArbreModification am = new ArbreModification(px, py, pz);
		if (!am.estVide(mg)) {
			return null;
		}
		ls.add(am);
		if (!am.generer(regles, mg, profondeur, estChemin)) {
			throw new GenerationFail();
		}
		return am;
	}

	public boolean generer(Regles regles, Regle rgl, MondeGenere mg, int profondeur, boolean estChemin)
			throws InvalidePos, InvalidValue {
		ls.clear();
		try {
			PX = creer(rgl.PX, regles, mg, x + 1, y, z, profondeur, estChemin);
			PY = creer(rgl.PY, regles, mg, x, y + 1, z, profondeur, estChemin);
			PZ = creer(rgl.PZ, regles, mg, x, y, z + 1, profondeur, estChemin);
			MX = creer(rgl.MX, regles, mg, x - 1, y, z, profondeur, estChemin);
			MY = creer(rgl.MY, regles, mg, x, y - 1, z, profondeur, estChemin);
			MZ = creer(rgl.MZ, regles, mg, x, y, z - 1, profondeur, estChemin);
		} catch (GenerationFail e) {
			return false;
		}
		return true;
	}

	public boolean existe(MondeGenere mg) {
		if (x < 0) {
			return false;
		}
		if (y < 0) {
			return false;
		}
		if (z < 0) {
			return false;
		}
		if (x >= mg.dx) {
			return false;
		}
		if (y >= mg.dy) {
			return false;
		}
		if (z >= mg.dz) {
			return false;

		}
		return true;

	}

	public boolean positionIdentique(ArbreModification am) {
		if (am.x != x) {
			return false;
		}
		if (am.y != y) {
			return false;
		}
		if (am.z != z) {
			return false;
		}
		return true;
	}

}
