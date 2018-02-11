package dadou.procedural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dadou.Log;

public class MondeGenere {
	public String[][][] contenu;
	public int dx, dy, dz;
	ValidationRegle validationRegle;

	public Map<String, Modification> listeModification() {
		Map<String, Modification> map = new HashMap<>();
		for (int x = 0; x < dx; x++) {

			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					if (contenu[x][y][z] != null) {
						Modification m = new Modification(x, y, z, this);
						map.put(m.nom(), m);
					}
				}
			}

		}
		return map;

	}

	public MondeGenere(int dx, int dy, int dz) {
		contenu = new String[dx][dy][dz];
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;

	}

	public List<String> listeTypePossible(Regles regles, int x, int y, int z)
			throws InvalidePos, InvalidValue {
		if (valeur(x, y, z) != null) {
			return null;
		}
		return this.listeTypePossibleSansTest(regles, x, y, z);

	}

	public List<String> listeTypePossibleSansTest(Regles regles, int x, int y,
			int z) throws InvalidePos, InvalidValue {

		List<String> result = new ArrayList<>();
		for (Map.Entry<String, Regle> e : regles.regles.entrySet()) {
			Regle rgl = e.getValue();
			try {
				if (!rgl.nombreSupperieurMax()
						&& estValide(e.getKey(), regles, rgl, x, y, z)) {
					if (validationRegle == null
							|| validationRegle.valider(this, x, y, z, rgl,
									regles)) {
						result.add(e.getKey());
					}
				}
			} catch (InvalidePos p) {

			}
		}
		if (result.isEmpty()) {
			return null;
		}
		return result;

	}

	public int verifier(Regles regles) throws InvalidePos, Error {
		int n = 0;

		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					String type = contenu[x][y][z];
					if (type != null) {

						n++;
						Regle r = regles.regles.get(type);
						if (r.PX != null) {
							if (!r.PX.contains(this.PX(x, y, z))) {
								throw new Error("erreur generation " + type
										+ " " + x + "," + y + "," + z);
							}
							if (r.PY != null) {
								if (!r.PY.contains(this.PY(x, y, z))) {
									throw new Error("erreur generation " + type
											+ " " + x + "," + y + "," + z);

								}

							}
							if (r.PZ != null) {
								if (!r.PZ.contains(this.PZ(x, y, z))) {
									throw new Error("erreur generation " + type
											+ " " + x + "," + y + "," + z
											+ " ->" + this.PZ(x, y, z));

								}

							}
							if (r.MX != null) {
								if (!r.MX.contains(this.MX(x, y, z))) {
									throw new Error("erreur generation " + type
											+ " " + x + "," + y + "," + z
											+ " ->" + this.MX(x, y, z));

								}

							}
							if (r.MY != null) {
								if (!r.MY.contains(this.MY(x, y, z))) {
									throw new Error("erreur generation " + type
											+ " " + x + "," + y + "," + z);

								}

							}
							if (r.MZ != null) {
								if (!r.MZ.contains(this.MZ(x, y, z))) {
									throw new Error("erreur generation " + type
											+ " " + x + "," + y + "," + z);

								}

							}

						}
					}
				}
			}
		}

		return n;
	}

	public boolean estValide(String nom, Regles regles, Regle rgl, int x,
			int y, int z) throws InvalidePos {
		if (rgl.PX != null) {
			String PX = this.PX(x, y, z);
			if (PX != null && !rgl.PX.contains(PX)) {
				return false;
			}

		}
		if (rgl.PY != null) {
			String PY = this.PY(x, y, z);
			if (PY != null && !rgl.PY.contains(PY)) {
				return false;
			}

		}
		if (rgl.PZ != null) {
			String PZ = this.PZ(x, y, z);
			if (PZ != null && !rgl.PZ.contains(PZ)) {
				return false;
			}

		}
		if (rgl.MX != null) {
			String MX = this.MX(x, y, z);
			if (MX != null && !rgl.MX.contains(MX)) {
				return false;
			}

		}
		if (rgl.MY != null) {
			String MY = this.MY(x, y, z);
			if (MY != null && !rgl.MY.contains(MY)) {
				return false;
			}

		}
		if (rgl.MZ != null) {
			String MZ = this.MZ(x, y, z);
			if (MZ != null && !rgl.MZ.contains(MZ)) {
				return false;
			}

		}
		// test inverse

		try {
			String PX = this.PX(x, y, z);
			if (PX != null) {
				Regle tmpR = regles.regles.get(PX);
				if (tmpR.MX != null && !tmpR.MX.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}

		try {
			String PY = this.PY(x, y, z);
			if (PY != null) {
				Regle tmpR = regles.regles.get(PY);
				if (tmpR.MY != null && !tmpR.MY.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}
		try {
			String PZ = this.PZ(x, y, z);
			if (PZ != null) {
				Regle tmpR = regles.regles.get(PZ);
				if (tmpR.MZ != null && !tmpR.MZ.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}

		try {
			String MX = this.MX(x, y, z);
			if (MX != null) {
				Regle tmpR = regles.regles.get(MX);
				if (tmpR.PX != null && !tmpR.PX.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}

		try {
			String MY = this.MY(x, y, z);
			if (MY != null) {
				Regle tmpR = regles.regles.get(MY);
				if (tmpR.PY != null && !tmpR.PY.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}
		try {
			String MZ = this.MZ(x, y, z);
			if (MZ != null) {
				Regle tmpR = regles.regles.get(MZ);
				if (tmpR.PZ != null && !tmpR.PZ.contains(nom)) {
					return false;
				}

			}
		} catch (InvalidePos pos) {

		}

		return true;
	}

	public String valeur(int x, int y, int z) throws InvalidePos {
		if (x < 0 || x >= dx) {
			throw new InvalidePos();
		}
		if (y < 0 || y >= dy) {
			throw new InvalidePos();
		}
		if (z < 0 || z >= dz) {
			throw new InvalidePos();
		}
		return contenu[x][y][z];

	}

	public String PX(int x, int y, int z) throws InvalidePos {
		return valeur(x + 1, y, z);
	}

	public String MX(int x, int y, int z) throws InvalidePos {
		return valeur(x - 1, y, z);
	}

	public String PY(int x, int y, int z) throws InvalidePos {
		return valeur(x, y + 1, z);
	}

	public String MY(int x, int y, int z) throws InvalidePos {
		return valeur(x, y - 1, z);
	}

	public String PZ(int x, int y, int z) throws InvalidePos {
		return valeur(x, y, z + 1);
	}

	public String MZ(int x, int y, int z) throws InvalidePos {
		return valeur(x, y, z - 1);
	}

	// ---------------------------------------------------------
	public Modification ModPX(int x, int y, int z) throws InvalidePos {
		return new Modification(x + 1, y, z, this);

	}

	public Modification ModMX(int x, int y, int z) throws InvalidePos {
		return new Modification(x - 1, y, z, this);
	}

	public Modification ModPY(int x, int y, int z) throws InvalidePos {
		return new Modification(x, y + 1, z, this);
	}

	public Modification ModMY(int x, int y, int z) throws InvalidePos {
		return new Modification(x, y - 1, z, this);
	}

	public Modification ModPZ(int x, int y, int z) throws InvalidePos {
		return new Modification(x, y, z + 1, this);
	}

	public Modification ModMZ(int x, int y, int z) throws InvalidePos {
		return new Modification(x, y, z - 1, this);
	}

	public boolean verfier(Regles regles, ArbreModification am) {
		List<String> types;
		try {
			types = this.listeTypePossibleSansTest(regles, am.x, am.y, am.z);
		} catch (InvalidePos | InvalidValue e) {
			// TODO Auto-generated catch block
			return false;
		}
		if (types == null) {
			return false;

		}
		return types.contains(am.type);

	}

	public List<ArbreModification> creerBranches(Regles regles, int profondeur,
			int frequence, boolean estChemin, String typeModifiable)
			throws InvalidePos, InvalidValue {
		List<ArbreModification> ls = new ArrayList<>();
		List<ArbreModification> rs = new ArrayList<>();
		this.listAjoutBranche(regles, ls, typeModifiable);
		for (Regle r : regles.regles.values()) {
			r.nbEchec = 0;
			r.totalPoid = r.rd.poid;
		}
		ArbreModification.profondeurMax = profondeur;
		for (int i = 0; i < ls.size(); i += frequence) {
			ArbreModification am = ls.get(i);
			Regle rgl = regles.regles.get(am.type);
			String oldType = contenu[am.x][am.y][am.z];
			am.ecrire(this, null);
			if (this.verfier(regles, am)) {
				am.ecrire(this, am.type);

				if (!am.generer(regles, rgl, this, 1, estChemin)) {
					for (ArbreModification tmp : am.ls) {
						tmp.reset(this, regles);
					}
					contenu[am.x][am.y][am.z] = oldType;

				} else {
					Log.print("oldType=" + oldType + "->"
							+ contenu[am.x][am.y][am.z]);
					rs.add(am);
					this.verifier(rgl, am.x, am.y, am.z);
					// return rs;

				}
			} else {
				// Log.print("Modif elimination");
				am.ecrire(this, oldType);
			}
			// this.verifier(regles);

		}
		return rs;

	}

	public void verifier(Regle rgl, int x, int y, int z) {
		try {
			if (this.PX(x, y, z) != null) {
				if (rgl.PX == null || !rgl.PX.contains(this.PX(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}
		try {
			if (this.MX(x, y, z) != null) {
				if (rgl.MX == null || !rgl.MX.contains(this.MX(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}
		try {
			if (this.PY(x, y, z) != null) {
				if (rgl.PY == null || !rgl.PY.contains(this.PY(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}
		try {
			if (this.MY(x, y, z) != null) {
				if (rgl.MY == null || !rgl.MY.contains(this.MY(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}
		try {
			if (this.PZ(x, y, z) != null) {
				if (rgl.PZ == null || !rgl.PZ.contains(this.PZ(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}
		try {
			if (this.MZ(x, y, z) != null) {
				if (rgl.MZ == null || !rgl.MZ.contains(this.MZ(x, y, z))) {
					throw new Error(" error");
				}
			}
		} catch (InvalidePos | Error e) {

		}

	}

	public void listAjoutBranche(Regles regles, List<ArbreModification> ls,
			String typeModif) {
		for (int x = 0; x < dx; x++)
			for (int y = 0; y < dy; y++)
				for (int z = 0; z < dz; z++) {

					try {
						String oldType = contenu[x][y][z];
						if (oldType != null) {
							Regle rglOld = regles.regles.get(oldType);
							if (rglOld.estType(typeModif)) {
								contenu[x][y][z] = null;

								List<String> types = listeTypePossibleSansTest(
										regles, x, y, z);
								contenu[x][y][z] = oldType;

								if (types != null) {
									for (String type : types) {
										Regle rgl = regles.regles.get(type);
										if (rgl.nbConnexion() == 3) {

											ArbreModification am = new ArbreModification(
													x, y, z);
											this.verifier(rgl, x, y, z);

											am.type = type;
											ls.add(am);
										}
									}

								}
							}
						}

					} catch (InvalidePos e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidValue e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
	}

}
