package dadou;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class DecompositionModelClasse {
	public Objet3D obj;
	public List<Vector3f> positionsInitiale = new ArrayList<>();
	public List<Vector3f> deplacements = new ArrayList<>();
	public List<BriqueAvecTexture3D> briques = new ArrayList<>();
	public List<Objet3D> elements = new ArrayList<>();
	public List<String> noms = new ArrayList<>();
	public float echelleElement;
	public int taille;
	public Map<String, ModelClasse> map = new HashMap<>();

	public Vector3f centre = new Vector3f();
	public ModelClasse mc;
	public BrickEditor be;

	public DecompositionModelClasse(ModelClasse mc, BrickEditor be,
			int tailleMorceau) throws CouleurErreur, Exception {
		obj = new Objet3D();
		centre.set(mc.dx, mc.dy, mc.dz);
		centre.multLocal(0.5f);
		this.taille = tailleMorceau;
		this.mc = mc;
		this.be = be;
		elements = this.creerElements();
	}

	List<Objet3D> creerElements() throws CouleurErreur, Exception {
		List<Objet3D> r = new ArrayList<>();
		int dx = mc.dx / taille;
		int dy = mc.dy / taille;
		int dz = mc.dz / taille;

		for (int x = 0; x <= dx; x++) {
			for (int y = 0; y <= dy; y++) {
				for (int z = 0; z <= dz; z++) {

					this.creerMorceau(r, mc, be, x * taille, y * taille,
							z * taille, taille);
				}
			}
		}
		return r;

	}

	public void creerMorceau(List<Objet3D> elements, ModelClasse mc,
			BrickEditor be, int x, int y, int z, int tailleMorceau)
			throws CouleurErreur, Exception {
		if (x >= mc.dx) {
			return;
		}
		if (y >= mc.dy) {
			return;
		}
		if (z >= mc.dz) {
			return;
		}
		if (tailleMorceau == 1) {
			if (this.estValidePourDecomposition(mc, x, y, z)) {
				Color color = mc.copie[x][y][z];
				String nom = "" + x + "," + y + "," + z;
				ModelClasse mcCube = be.modelClassePourCube(color,
						mc.nomHabillage);

				noms.add(nom);
				Vector3f pos = new Vector3f();
				pos.set(x, y, z);
				pos.addLocal(0.5f, 0.5f, 0.5f);
				Vector3f dep = new Vector3f();
				dep.set(pos);
				dep.subtractLocal(centre);
				dep.normalizeLocal();
				deplacements.add(dep);
				positionsInitiale.add(pos);
				Objet3D cube = new Objet3D();
				cube.brique = mcCube.brique;
				this.briques.add(mcCube.brique);
				cube.translation(pos);
				cube.translation(-0.5f, -0.5f, -0.5f);
				obj.ajouter(cube);
				elements.add(cube);

			}
			return;
		}
		if (!estValidePourDecomposition(mc, x, y, z, tailleMorceau)) {
			return;
		}

		int tailleX = this.tailleMorceauX(mc, x, tailleMorceau);
		int tailleY = this.tailleMorceauY(mc, y, tailleMorceau);
		int tailleZ = this.tailleMorceauZ(mc, z, tailleMorceau);
		String nom = "" + x + "," + y + "," + z;
		String nomModelClasse = mc.nom + ":" + nom;
		ModelClasse mcCube = be.dynamicsModelClasse.get(nomModelClasse);
		if (mcCube == null) {
			mcCube = new ModelClasse();
			mcCube.init(tailleX, tailleY, tailleZ);
			mcCube.nom = nomModelClasse;
			noms.add(nom);
			be.dynamicsModelClasse.put(nomModelClasse, mcCube);
			for (int ux = 0; ux < tailleX; ux++) {
				for (int uy = 0; uy < tailleY; uy++) {
					for (int uz = 0; uz < tailleZ; uz++) {
						int mx = ux + x;
						int my = uy + y;
						int mz = uz + z;
						if (mx >= mc.dx || my >= mc.dy || mz >= mc.dz) {
							throw new Error("" + mx + " " + mc.dx + " " + my
									+ " " + mc.dy + " " + mz + " " + mc.dz);

						}
						if (this.estValidePourDecomposition(mc, mx, my, mz)) {
							mcCube.setColor(ux, uy, uz, mc.copie[mx][my][mz]);
						} else {
							mcCube.setColor(ux, uy, uz, Color.BLACK);

						}

					}
				}
			}

			mcCube.initBuffer(be.game, be.donnerHabillage(mc.nomHabillage));
		}
		float ftailleX = tailleX;
		float ftailleY = tailleY;
		float ftailleZ = tailleZ;
		Vector3f pos = new Vector3f();
		pos.set(x, y, z);
		pos.addLocal(ftailleX / 2.0f, ftailleY / 2.0f, ftailleZ / 2.0f);
		Vector3f dep = new Vector3f();
		dep.set(pos);
		dep.subtractLocal(centre);
		dep.normalizeLocal();
		deplacements.add(dep);
		positionsInitiale.add(pos);
		Objet3D cube = new Objet3D();
		cube.brique = mcCube.brique;
		this.briques.add(mcCube.brique);
		cube.translation(pos);
		cube.translation(-ftailleX / 2.0f, -ftailleY / 2.0f, -ftailleZ / 2.0f);
		obj.ajouter(cube);
		elements.add(cube);

	}

	int tailleMorceauX(ModelClasse mc, int x, int tailleMorceau) {
		if (x + tailleMorceau <= mc.dx) {
			return tailleMorceau;
		}
		return mc.dx - x;

	}

	int tailleMorceauY(ModelClasse mc, int y, int tailleMorceau) {
		if (y + tailleMorceau <= mc.dy) {
			return tailleMorceau;
		}
		return mc.dy - y;

	}

	int tailleMorceauZ(ModelClasse mc, int z, int tailleMorceau) {
		if (z + tailleMorceau <= mc.dz) {
			return tailleMorceau;
		}
		return mc.dz - z;

	}

	public boolean estValidePourDecomposition(ModelClasse mc, int x, int y,
			int z, int tailleMorceau) {

		for (int dx = 0; dx < tailleMorceau; dx++) {
			for (int dy = 0; dy < tailleMorceau; dy++) {
				for (int dz = 0; dz < tailleMorceau; dz++) {

					if (this.estValidePourDecomposition(mc, x + dx, y + dy, z
							+ dz)) {
						return true;
					}

				}

			}

		}
		return false;

	}

	public void initialiser(float distance, float echelle, Vector3f pos,
			Quaternion q) {
		if (q != null) {
			obj.setRotation(q);
		}
		obj.positionToZero();
		obj.translation(pos);
		if (echelleElement == 0.0f) {
			echelleElement = echelle;
		}

		for (int i = 0; i < positionsInitiale.size(); i++) {
			Objet3D cube = obj.enfant(i);
			cube.positionToZero();
			Vector3f p = this.positionsInitiale.get(i);
			Vector3f dep = this.deplacements.get(i);
			this.briques.get(i).echelle = echelleElement;
			cube.translation(echelle * (p.x - 0.5f) + distance * dep.x, echelle
					* (p.y - 0.5f) + distance * dep.y, echelle * (p.z - 0.5f)
					+ distance * dep.z);

		}

	}

	public boolean estValidePourDecomposition(ModelClasse mc, int x, int y,
			int z) {
		if (x >= mc.dx) {
			return false;
		}
		if (y >= mc.dy) {
			return false;
		}
		if (z >= mc.dz) {
			return false;

		}
		if (ElementDecor.estVide(mc.copie[x][y][z])) {
			return false;
		}
		int dep[] = new int[] { -1, 1 };
		int n = 0;
		for (int dx : dep) {
			if (x + dx >= 0 && x + dx < mc.dx) {
				if (!ElementDecor.estVide(mc.copie[x + dx][y][z])) {
					n++;
				}
			}

		}
		for (int dy : dep) {
			if (y + dy >= 0 && y + dy < mc.dy) {
				if (!ElementDecor.estVide(mc.copie[x][y + dy][z])) {
					n++;
				}
			}
		}
		for (int dz : dep) {
			if (z + dz >= 0 && z + dz < mc.dz) {
				if (!ElementDecor.estVide(mc.copie[x][y][z + dz])) {
					n++;
				}
			}

		}
		return n < 6;

	}

	public void dessiner(Camera cam) {
		obj.dessiner(cam);

	}

}
