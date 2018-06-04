package dadou;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.EtatOctree;
import dadou.event.GameEventLeaf;
import dadou.procedural.DescriptionKube;
import dadou.procedural.DescriptionKubeInvalide;
import dadou.procedural.ErreurRegle;
import dadou.procedural.InvalidValue;
import dadou.procedural.InvalidePos;
import dadou.procedural.ModelClasseInexistant;
import dadou.procedural.MondeGenere;
import dadou.procedural.Part;
import dadou.procedural.PartFunc;
import dadou.procedural.Regle;
import dadou.procedural.Regles;
import dadou.tools.BrickEditor;
import dadou.tools.SerializeTool;
import dadou.tools.graphics.ConfigValues;
import terrain.Terrain;

public class DecorDeBriqueDataElement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4799226871286994448L;
	public ElementDecor[][][] elementsDecor;


	public DecorInfo decorInfo;
	public String nom;
	public String nomKubeSommet;
	public String nomKubeAutre;
	public ShadowData shadowData;
	public List<Lumiere> lumieres = new ArrayList<>();
	public List<Zone> zones = new ArrayList<>();
	public ImageEcran imageEcran;
	public String nomHabillage;
	public String skyBox;
	public Terrain terrain;
	// public Octree<EtatOctree> octree;
	public Map<String, Object> data = new HashMap<>();
	public Map<String, CameraPosition> cameraPositions;
	public CameraPosition cameraPosition;
	public Map<String, GroupeCameraPosition> groupeCameraPositions;

	public ConfigValues configValues;
	public Map<String, ModelInstance> modelInstances;

	public int ajouterParcelTerrain() {
		if (terrain == null) {
			return 0;
		}
		int r = 0;
		int elementTaille = decorInfo.elementTaille;

		float fElementTaille = elementTaille;
		int dim = terrain.dim;
		float m = fElementTaille * (float) Math.pow(2, decorInfo.niveau - 1);
		for (int px = 0; px < dim - 1; px++) {
			for (int pz = 0; pz < dim - 1; pz++) {

				int h00 = terrain.grille[px][pz];
				int h10 = terrain.grille[px + 1][pz];
				int h01 = terrain.grille[px][pz + 1];
				int h11 = terrain.grille[px + 1][pz + 1];
				int h00z = h00 / elementTaille;
				int h10z = h10 / elementTaille;
				int h11z = h11 / elementTaille;
				int h01z = h01 / elementTaille;
				int ux = px / elementTaille;
				int uz = pz / elementTaille;
				int hMin = Math.min(Math.min(h00z, h11z), Math.min(h01z, h10z));
				ElementDecor ed = this.getElementDecor(ux, hMin, uz);

				if (ed.pt == null) {
					ed.pt = new ParcelTerrain(m, terrain.maxValue, ux, hMin, uz, elementTaille);
					r++;

				}

			}

		}
		return r;
	}

	public void finParcourGroupeCameraPosition(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;

			mei.finParcourGroupeCameraPosition();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finSon(MondeInterfacePrive i, String nomSon) {
		try {
			MondeEventInterface mei = i.mei;

			mei.finSon(nomSon);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finTireSansCollision(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;

			mei.finTireSansCollision();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void sortieMonde(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.sortieMonde();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void collisionTire(MondeInterfacePrive i, Vector3f pos, Vector3f dir) {
		try {
			MondeEventInterface mei = i.mei;

			mei.collisionTire(pos, dir);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void boucler(MondeInterfacePrive i) throws StopperBoucle {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.boucler();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			throw new StopperBoucle();
		}

	}

	public void sortieZoneDetectionJoueur(MondeInterfacePrive i, ElementJeux om) {
		try {
			MondeEventInterface mei = i.mei;

			mei.sortieZoneDetectionJoueur(om);
		} catch (Throwable t) {

		}

	}

	public void entreeZoneDetectionJoueur(MondeInterfacePrive i, ElementJeux om) {
		try {
			MondeEventInterface mei = i.mei;

			mei.entreeZoneDetectionJoueur(om);
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void tirer(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;

			mei.tirer();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void selectionnerInfoJeux(MondeInterfacePrive i, Integer idx) {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.selectionnerInfoJeux(idx);
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public Arbre<String> creerArbreCameraPosition() {
		Arbre<String> r = new Arbre<String>(null);
		r.nom = "camera";
		for (Map.Entry<String, GroupeCameraPosition> e : this.groupeCameraPositions().entrySet()) {
			GroupeCameraPosition gcp = e.getValue();
			for (String nom : gcp.noms) {
				String tmp[] = nom.split("\\$");
				String chemin[] = tmp[0].split("\\#");
				String id = "";
				if (tmp.length >= 2) {
					id = tmp[1];
				}

				r.ajouter(chemin, id);
			}
			if (gcp.noms.isEmpty()) {
				String chemin[] = e.getKey().split("\\#");
				if (chemin.length == 1) {
					r.ajouter(chemin[0]);
				} else {
					r.ajouter(Arrays.copyOfRange(chemin, 0, chemin.length - 1), chemin[chemin.length - 1]);

				}
			}
		}
		return r;

	}

	public void sautEnCours(MondeInterfacePrive i, boolean sautForce, float hauteurSaut) {
		MondeEventInterface mei = i.mei;
		if (mei != null) {
			mei.sautEnCours(sautForce, hauteurSaut);
			return;

		}

	}

	public Color lireCouleur(int x, int y, int z) throws CouleurErreur {
		int elementTaille = decorInfo.elementTaille;
		int px = x / elementTaille;
		int py = y / elementTaille;
		int pz = z / elementTaille;
		int ux = x - elementTaille * px;
		int uy = y - elementTaille * py;
		int uz = z - elementTaille * pz;

		ElementDecor ed = this.elementsDecor[px][py][pz];
		if (ed == null || ed.bytes == null) {
			return Color.BLACK;
		}

		return this.elementsDecor[px][py][pz].lireCouleur(elementTaille, ux, uy, uz);

	}

	public void ecrireCouleur(int x, int y, int z, Color color) throws CouleurErreur {
		int elementTaille = decorInfo.elementTaille;
		int px = x / elementTaille;
		int py = y / elementTaille;
		int pz = z / elementTaille;
		int ux = x - elementTaille * px;
		int uy = y - elementTaille * py;
		int uz = z - elementTaille * pz;
		ElementDecor ed = this.elementsDecor[px][py][pz];
		if (ed == null) {
			ed = this.getElementDecor(px, py, pz);
		}

		ed.ecrireCouleur(elementTaille, ux, uy, uz, color);

	}

	public ModelInstance donnerModelInstance(String nom) {
		if (modelInstances == null) {
			return null;
		}
		return modelInstances.get(nom);
	}

	public void dessinerGroupe(BrickEditor be, String groupe, Camera cam) {
		if (groupe == null) {
			return;
		}
		GroupeCameraPosition g = this.groupeCameraPositions().get(groupe);
		if (g == null) {
			return;
		}
		for (String nom : g.noms) {
			CameraPosition cp = this.cameraPositions.get(nom);
			cp.creerObjet3D(be);
			cp.dessiner(cam);

		}

	}

	public Map<String, GroupeCameraPosition> groupeCameraPositions() {
		if (groupeCameraPositions == null) {

			groupeCameraPositions = new HashMap<>();
			if (this.cameraPositions != null) {
				for (Map.Entry<String, CameraPosition> cp : this.cameraPositions.entrySet()) {
					GroupeCameraPosition g = new GroupeCameraPosition();
					g.noms.add(cp.getKey());
					groupeCameraPositions.put(cp.getKey(), g);
				}
			}
		}
		return groupeCameraPositions;

	}

	public String donnerGroupePour(String nom) {
		CameraPosition cp = this.cameraPositions.get(nom);
		if (cp == null) {
			return null;
		}
		if (cp.groupe == null) {
			return nom;
		}
		return cp.groupe;

	}

	public List<String> nomCameraPositions() {

		ArrayList<String> r = new ArrayList<String>();
		if (cameraPositions == null) {
			return r;
		}
		r.addAll(cameraPositions.keySet());
		return r;
	}

	public void appuyerSurClavier(MondeInterfacePrive i, char character, int key) {

		try {
			MondeEventInterface mei = i.mei;

			if (mei != null) {
				mei.appuyerSurClavier(character, key);
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void tomber(MondeInterfacePrive i, float hauteur) {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.tomber(hauteur);
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public void demarer(MondeInterfacePrive i) {
		try {
			if (this.modelInstances != null) {
				for (ModelInstance mi : this.modelInstances.values()) {
					mi.initChemin();

				}
			}
			if (i.joueur.aj.initMEI != null) {
				i.mei = i.joueur.aj.initMEI;
				i.mei.i = i.mondeInterface;
			}
			if (i.mei == null) {

				i.mei = i.mondeEventInterfaces.get(nom);
			}

			if (i.mei != null) {
				i.mei.demarer();
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void finaliser(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.finaliser();
				return;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public void arreter(MondeInterfacePrive i) {
		try {
			MondeEventInterface mei = i.mei;
			if (mei != null) {
				mei.arreter();
				return;

			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	public List<String> donnerNomsPourGroupe(String nom) {

		GroupeCameraPosition g = this.groupeCameraPositions().get(nom);
		if (g == null) {
			return null;
		}
		return g.noms;
	}

	public List<String> nomGroupes() {
		List<String> r = new ArrayList<>();
		r.addAll(this.groupeCameraPositions().keySet());
		return r;

	}

	public void ajouterGroupeCameraPosition(String nom) {

		GroupeCameraPosition g = this.groupeCameraPositions().get(nom);
		if (g == null) {
			g = new GroupeCameraPosition();
			this.groupeCameraPositions().put(nom, g);
		}

	}

	public boolean ajouterCameraPosition(String nom, CameraPosition cp) {
		if (cameraPositions == null) {
			this.cameraPositions = new HashMap<String, CameraPosition>();

		}

		GroupeCameraPosition g = this.groupeCameraPositions().get(cp.groupe);
		if (g == null) {
			g = new GroupeCameraPosition();
			this.groupeCameraPositions().put(cp.groupe, g);
		}
		if (nom == null) {
			nom = cp.groupe + "$" + g.idx;
			while (this.cameraPositions.get(nom) != null) {
				g.idx++;
				nom = cp.groupe + "$" + g.idx;
			}
		}
		if (!g.noms.isEmpty()) {
			CameraPosition lastCp = this.cameraPositions.get(g.noms.get(g.noms.size() - 1));
			if (cp.translation.equals(lastCp.translation) && cp.rotationX.equals(lastCp.rotationX)
					&& cp.rotationY.equals(lastCp.rotationY)) {
				return false;
			}
		}

		this.cameraPositions.put(nom, cp);
		g.noms.add(nom);
		return true;

	}

	public CameraPosition donnerCameraPosition(String nom) {
		if (cameraPositions == null) {
			return null;
		}
		return cameraPositions.get(nom);

	}

	public void supprimerCameraPosition(String nom) {
		if (cameraPositions == null) {
			return;

		}
		CameraPosition cp = this.cameraPositions.get(nom);
		GroupeCameraPosition g = this.groupeCameraPositions().get(cp.groupe);
		g.noms.remove(nom);
		if (g.noms.isEmpty()) {
			this.groupeCameraPositions().remove(cp.groupe);
		}

		this.cameraPositions.remove(nom);

	}

	public void supprimerGroupeCameraPosition(String nom) {
		if (cameraPositions == null) {
			return;
		}

		GroupeCameraPosition g = this.groupeCameraPositions().get(nom);
		for (String n : g.noms) {
			this.groupeCameraPositions().remove(n);
		}

		this.groupeCameraPositions().remove(nom);

	}

	public void ajouterModelInstance(String nom, ModelInstance model) {
		if (modelInstances == null) {
			modelInstances = new HashMap<String, ModelInstance>();

		}
		model.nomObjet = nom;
		modelInstances.put(nom, model);

	}

	public ConfigValues getConfigValues() {
		if (configValues == null) {

			configValues = new ConfigValues();
		}
		return configValues;
	}

	public void reset() {
		elementsDecor = null;

	}

	public DecorDeBriqueDataElement(int niveau, int elementTaille) {
		decorInfo = new DecorInfo();

		decorInfo.init(this, niveau, elementTaille);

	}

	public void delete() {
		for (ElementDecor[][] a : elementsDecor) {
			for (ElementDecor[] b : a) {
				for (ElementDecor c : b) {
					if (c != null && c.brique != null) {
						c.delete();

					}
				}
			}
		}
	}

	public void sauvegarder(String nomFichier) throws FileNotFoundException, IOException {

		String nomFichierComplet = (new File(nomFichier)).getAbsolutePath();
	
		SerializeTool.save(this, nomFichierComplet);

	}

	static public DecorDeBriqueDataElement charger(String nomFichier)
			throws FileNotFoundException, ClassNotFoundException, IOException {

		DecorDeBriqueDataElement DecorDeBriqueDataElement = (dadou.DecorDeBriqueDataElement) SerializeTool
				.load(nomFichier);
		String nom = new File(nomFichier).getName();
		nom = "monde." + nom.replace(".wld", "");
		DecorDeBriqueDataElement.nom = nom;
		return DecorDeBriqueDataElement;

	}

	public static List<String> lesMondes(String nomRepertoire) {

		File rep = new File(nomRepertoire);
		List<String> list = new ArrayList<String>();

		for (File file : rep.listFiles()) {
			if (file.getName().endsWith(".wld")) {
				list.add(file.getName());
			}

		}
		return list;

	}

	static public DecorDeBriqueDataElement creer(int niveau, int elementTaille, String nomFichier)
			throws FileNotFoundException, IOException {
		DecorDeBriqueDataElement elt = new DecorDeBriqueDataElement(niveau, elementTaille);
		elt.sauvegarder(nomFichier);
		return elt;

	}

	public ElementDecor getElementDecor(int x, int y, int z) {
		ElementDecor et = elementsDecor[x][y][z];
		if (et == null) {
			et = new ElementDecor();
			et.x = x;
			et.y = y;
			et.z = z;
			elementsDecor[x][y][z] = et;
		}
		return et;

	}

	static public void ajouter(Regles rgl, ModelClasse mc, int ix, int iy, int iz, DecorDeBriqueDataElement dbe)
			throws CouleurErreur {
		ElementDecor ed = dbe.getElementDecor(ix, iy, iz);
		if (mc.copie != null) {

			for (int ux = 0; ux < mc.dx; ux++) {
				for (int uy = 0; uy < mc.dy; uy++) {
					for (int uz = 0; uz < mc.dz; uz++) {

						Color color = mc.copie[ux][uy][uz];
						if (color != null) {
							int x = ix * mc.dx + ux;
							int y = iy * mc.dy + uy;
							int z = iz * mc.dz + uz;
							if (!ElementDecor.estVide(color)) {
								dbe.ecrireCouleur(x, y, z, color);
								rgl.nbKube++;
							}
							// Log.print(" ecriture "+x+","+y+","+z);

						}
					}
				}

			}

		}

	}

	static public DescriptionKube verifierRegles(Regles rgl, DecorDeBriqueData decorDeBriqueData)
			throws ModelClasseInexistant, DescriptionKubeInvalide {

		DescriptionKube descKube = null;
		for (Map.Entry<String, Regle> e : rgl.regles.entrySet()) {
			for (String nomModel : e.getValue().rd.nomModels) {
				ModelClasse mc = decorDeBriqueData.models.get(nomModel);
				if (mc == null) {
					throw new ModelClasseInexistant(nomModel);
				}
				DescriptionKube dk = new DescriptionKube();
				dk.dx = mc.dx;
				dk.dy = mc.dy;
				dk.dz = mc.dz;
				dk.nomHabillage = mc.nomHabillage;
				descKube = dk.verifier(nomModel, dk);

			}
		}
		return descKube;

	}

	static public void genererDecor(Regles rgl, MondeGenere mg, String cheminRessources, String nomMonde, PartFunc pf)
			throws FileNotFoundException, ClassNotFoundException, IOException, ModelClasseInexistant,
			DescriptionKubeInvalide, InvalidePos, InvalidValue, CouleurErreur, ErreurRegle {
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");

		DescriptionKube descKube = verifierRegles(rgl, decorDeBriqueData);

		int dim = (int) Math.pow(2, BrickEditor.niveau) * BrickEditor.elementTaille;
		int dx = mg.dx;
		;
		int dy = mg.dy;
		;
		int dz = mg.dz;
		/*
		 * if (dx != mg.dx || dy != mg.dy || dz != mg.dz) { return; }
		 */

		DecorDeBriqueDataElement dbe = new DecorDeBriqueDataElement(BrickEditor.niveau, BrickEditor.elementTaille);
		dbe.nomHabillage = descKube.nomHabillage;
		dbe.configValues = new ConfigValues();
		dbe.configValues.brouillard = 0.008f;
		dbe.cameraPositions = new HashMap<>();
		dbe.skyBox = "skybox3";
		Log.print(" Habillage " + dbe.nomHabillage);
		for (int ix = 0; ix < dx; ix++) {
			for (int iy = 0; iy < dy; iy++) {
				for (int iz = 0; iz < dz; iz++) {
					String nomRegle = mg.contenu[ix][iy][iz];
					// Log.print(" ecriture +"+ix+","+iy+","+iz+" "+nomModel);
					if (nomRegle != null) {
						String nomModel = rgl.nomModel(nomRegle);
						ModelClasse mc = decorDeBriqueData.models.get(nomModel);
						ajouter(rgl, mc, ix, iy, iz, dbe);
						if (pf != null) {
							Part part = new Part();
							part.base = decorDeBriqueData;
							part.x = ix;
							part.y = iy;
							part.z = iz;
							part.decor = dbe;
							part.nomRegle = nomRegle;
							part.regles = rgl;
							part.regle = rgl.regles.get(nomRegle);
							part.dim = descKube.dx;
							part.mg = mg;
							part.dimWorld = dim;
							pf.exec(part);
						}

					} else {
						ModelClasse mc = decorDeBriqueData.models.get("vide");
						ajouter(rgl, mc, ix, iy, iz, dbe);
					}
				}
			}

		}
		dbe.cameraPosition = new CameraPosition();
		dbe.cameraPosition.translation.set(8, 5, 8);
		dbe.sauvegarder(cheminRessources + "\\" + nomMonde);
		Log.print(" nbKube=" + rgl.nbKube);

	}

}
