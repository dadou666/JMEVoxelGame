package dadou.exploration;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;

import dadou.EspaceSelector;
import dadou.Log;
import dadou.ObjetMobile;
import dadou.Octree;
import dadou.OrientedBoundingBoxWithVBO;
import dadou.graphe.GrapheDebug;
import dadou.graphe.GrapheElement;
import dadou.graphe.GrapheLigne;
import dadou.tools.BrickEditor;

public class GestionGrapheExploration implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8047930569756974697L;
	public Octree<GrapheExploration> octree;
	transient public BrickEditor brickEditor;
	Vector3f N = new Vector3f();
	public float rayon = 2.00f;
	public int nbCube = 3;
	public float vitesse = 3.2f;
	public float hauteur = 1.5f;
	public int nbDirection = 40;
	transient public List<Class> classPourExclusions;
	public int idx = 0;

	public GrapheExploration racine;
	public List<GrapheExploration> listeFeuilles;
	public List<GrapheExploration> elements = new ArrayList<GrapheExploration>();
	public int idxEnregistrer = 0;

	public void enregistrer() {
		if (idxEnregistrer >= elements.size()) {
			return;
		}
		this.enregistrer(elements.get(idxEnregistrer));
		idxEnregistrer++;
		if (idxEnregistrer >= elements.size()) {
			Log.print(" fin eneregistrer");
			return;
		}
	}
	public void enregistrerTous() {
		while (idxEnregistrer < elements.size()) {
		
		this.enregistrer(elements.get(idxEnregistrer));
		idxEnregistrer++;
	
		}
	}
	public boolean lire(String nomFichier) throws IOException {
		File file = new File(BrickEditor.cheminRessources + "/" + nomFichier);
		if (!file.exists()) {
			return false;
		}
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);

		racine = GrapheExploration.lire(this, dis);

		dis.close();
		return true;

	}

	public void ecrire(String nomFichier) {
		File file = new File(BrickEditor.cheminRessources + "/" + nomFichier);
		if (!file.exists()) {

			try {
				FileOutputStream fos = new FileOutputStream(file);
				DataOutputStream dos = new DataOutputStream(fos);

				racine.ecrire(dos);

				fos.close();

			} catch (IOException ex) {

			}
		}
	}

	public GrapheExploration ancetreCommun(GrapheExploration a,
			GrapheExploration b) {
		if (a == b) {
			return a;
		}
		if (a.profondeur == b.profondeur) {
			return ancetreCommun(a.parent, b.parent);
		}
		if (a.profondeur < b.profondeur) {
			return ancetreCommun(a, b.parent);

		}
		return ancetreCommun(a.parent, b);

	}

	public void filtrerAvecProfondeurAncetreCommun(int idx, int profondeur) {
		if (idx >= listeFeuilles.size()) {
			return;
		}
		List<GrapheExploration> r = new ArrayList<GrapheExploration>();
		for (int i = 0; i <= idx; i++) {
			r.add(listeFeuilles.get(i));
		}
		GrapheExploration ge = listeFeuilles.get(idx);

		for (int u = idx + 1; u < listeFeuilles.size(); u++) {
			GrapheExploration geTmp = listeFeuilles.get(u);
			GrapheExploration ancetreCommun = this.ancetreCommun(ge, geTmp);
			if (ancetreCommun == null) {
				r.add(geTmp);
			} else {

				if (ancetreCommun.profondeur <= profondeur) {
					r.add(geTmp);
				}
			}
		}
		this.listeFeuilles = r;
		this.filtrerAvecProfondeurAncetreCommun(idx + 1, profondeur);

	}

	public List<GrapheExploration> listeFeuillesTrie() {
		if (listeFeuilles != null) {
			return listeFeuilles;
		}
		List<GrapheExploration> r = new ArrayList<GrapheExploration>();
		racine.listeFeuilles(r);
		for (GrapheExploration ge : r) {
			ge.calculerDistanceCheminDepuisRacine();
		}
		Collections.sort(r, (a, b) -> {
			return Float.compare(a.distanceRacine, b.distanceRacine);
		});
		Collections.reverse(r);
		// Log.print(" nombre de feuilles " + r.size());
		listeFeuilles = r;
		return r;

	}

	public void init(BrickEditor be, ObjetMobile om, int numStep,
			List<Class> classes) {
		this.brickEditor = be;
		OrientedBoundingBoxWithVBO box = (OrientedBoundingBoxWithVBO) om
				.getBox();

		Vector3f p = box.getCenter();

		rayon = (box.getExtent().length());

		calculerHauteur(p);
		// this.hauteur = 9;
		GrapheExploration ge = new GrapheExploration();
		ge.pos.set(p);

		ge.bs.setRadius(rayon);
		this.numStep = numStep;
		ge.bs.getCenter().set(ge.pos);

		racine = ge;
		racine.gge = this;
		this.enregistrer(octree, ge);
		this.lsOld.add(ge);
		this.classPourExclusions = new ArrayList<>();
		for (Class cls : classes) {
			this.classPourExclusions.add(cls);
		}

	}

	public void init(BrickEditor be, Vector3f pos, float rayon, int numStep,
			List<Class> classes) {
		this.brickEditor = be;

		Vector3f p = pos;
		this.rayon = rayon;

		calculerHauteur(p);
		// this.hauteur = 9;
		GrapheExploration ge = new GrapheExploration();
		ge.pos.set(p);

		ge.bs.setRadius(rayon);
		this.numStep = numStep;
		ge.bs.getCenter().set(ge.pos);

		racine = ge;
		racine.gge = this;
		this.enregistrer(octree, ge);
		this.lsOld.add(ge);
		classPourExclusions = new ArrayList<>();
		for (Class cls : classes) {
			this.classPourExclusions.add(cls);
		}

	}

	public void calculerHauteur(Vector3f pos) {
		BoundingSphere bs = new BoundingSphere();
		bs.getCenter().set(pos);
		bs.setRadius(rayon);
		hauteur = 0.0f;
		float pas = 0.01f;
		while (!brickEditor.decor.gestionCollision.verifierCollision(bs)) {
			hauteur += pas;
			bs.getCenter().y -= pas;

		}

		// Log.print("hauteur=" + hauteur);

	}

	public boolean verifierPosition(Vector3f pos) {
		BoundingSphere bs = new BoundingSphere();
		bs.getCenter().set(pos);
		bs.setRadius(rayon);
		return brickEditor.decor.gestionCollision.verifierCollision(bs);
	}

	public float anglePourMonterCube(int nbCube) {
		double d = nbCube;
		double a = Math.acos(1.0 / Math.sqrt(1.0 + d * d));
		return (float) a;

	}

	public boolean cherchePositionSuivante(BoundingSphere bs, Vector3f pos,
			float angle) {
		float dx = (float) Math.cos(angle);
		float dy = (float) Math.sin(angle);
		return this.cherchePositionSuivante(bs, pos, dx, dy, (o) -> {
			return !this.classPourExclusions.contains(o.mei.getClass())
					&& o.testCollisionAvecAutreObjetMobile;
		});

	}

	public void chercherListeGrapheExploration(BoundingSphere bs,
			List<GrapheExploration> rs) {
		this.chercherListeGrapheExploration(octree, bs, rs);
		Vector3f pos = bs.getCenter();
		Collections.sort(rs, (GrapheExploration a, GrapheExploration b) -> {
			float da = a.pos.distance(pos);
			float db = b.pos.distance(pos);
			return Float.compare(da, db);
			// return Long.compare(a.visitTime, b.visitTime);

			});

	}

	public void chercherListeGrapheExploration(Octree<GrapheExploration> o,
			BoundingSphere bs, List<GrapheExploration> rs) {
		if (o.box.intersects(bs)) {
			if (o.level == 0) {
				if (o.value == null) {
					return;
				}
				o.value.bs.getCenter().set(o.value.pos);
				o.value.bs.setRadius(rayon);
				if (o.value.bs.intersects(bs)) {
					rs.add(o.value);
				}
				return;
			}
			if (o.children == null) {
				return;
			}
			for (Octree<GrapheExploration> enfant : o.children) {
				this.chercherListeGrapheExploration(enfant, bs, rs);

			}

		}

	}

	public boolean cheminDepuisVers(ObjetMobile om, BoundingSphere bs,
			Vector3f arrive, List<Vector3f> chemin) {
		if (chemin != null) {
			chemin.clear();
		}
		Vector3f depart = om.getBox().getCenter();
		bs.getCenter().set(depart);
		bs.setRadius(rayon);
		Vector3f currentPos = new Vector3f(depart);
		float dx = arrive.x - depart.x;
		float dz = arrive.z - depart.z;
		float norm = (float) Math.sqrt(dx * dx + dz * dz);
		dx = dx / norm;
		dz = dz / norm;

		while (this.cherchePositionSuivante(bs, currentPos, dx, dz, (o) -> {
			return o != om && o.testCollisionAvecAutreObjetMobile;
		})) {
			currentPos = new Vector3f(bs.getCenter());
			float rx = currentPos.x - depart.x;
			float rz = currentPos.z - depart.z;
			float norm2 = (float) Math.sqrt(rx * rx + rz * rz);
			if (norm2 < norm) {
				if (chemin != null) {
					chemin.add(currentPos);
				}
			} else {
				if (chemin != null && chemin.isEmpty()) {
					return false;
				}
				return true;
			}

		}
		return false;

	}

	public ObjetMobile omCollision(BoundingSphere bs, EspaceSelector es) {
		return brickEditor.decor.espace.getOMFor(bs, es);

	}

	List<GrapheExploration> cibles = new ArrayList<>();
	GrapheExploration oldCible = null;

	public GrapheExploration donnerGrapheExplorationJoueur() {

		cibles.clear();
		getGrapheExploration(brickEditor.scc.boxCam, this.octree, cibles);
		if (cibles.isEmpty()) {
			return oldCible;
		}
		oldCible = cibles.get(0);
		return cibles.get(0);
	}

	public void getGrapheExploration(OrientedBoundingBox box,
			Octree<GrapheExploration> o, List<GrapheExploration> ls) {
		if (o.box.intersects(box)) {
			if (o.value != null && o.value.bs.intersects(box)) {
				ls.add(o.value);

			}
			if (o.children == null) {
				return;
			}
			for (Octree<GrapheExploration> enfant : o.children) {
				getGrapheExploration(box, enfant, ls);
			}
		}

	}

	public boolean cherchePositionSuivante(BoundingSphere bs, Vector3f pos,
			float dx, float dy, EspaceSelector es) {
		bs.setRadius(this.rayon);
		for (int n = -nbCube; n < nbCube; n++) {
			Vector3f P = bs.getCenter();
			P.set(pos);
			float inclinaison = this.anglePourMonterCube(n);
			if (n < 0) {
				inclinaison = -inclinaison;
			}
			N.x = dx;
			N.z = dy;
			N.y = 0.0f;
			N.multLocal((float) Math.cos(inclinaison));
			N.addLocal(0.0f, (float) Math.sin(inclinaison), 0.0f);
			N.normalizeLocal();
			N.multLocal(2 * this.rayon);
			bs.getCenter().addLocal(N);
			ObjetMobile omCollision = brickEditor.decor.espace.getOMFor(bs, es);

			if (omCollision == null) {
				if (brickEditor.scc != null
						&& bs.intersects(brickEditor.scc.sphereCam)) {
					return false;
				}
				if (brickEditor.decor.espace.octree.box.intersects(bs)) {
					if (!brickEditor.decor.gestionCollision
							.verifierCollision(bs)) {
						bs.getCenter().y -= this.hauteur;
						if (brickEditor.decor.gestionCollision
								.verifierCollision(bs)) {
							bs.getCenter().y += this.hauteur;
							return true;

						}
						bs.getCenter().y += this.hauteur;

					}
				} else {

					return false;
					// Log.print(" hors monde ");
				}
			}
		}

		return false;

	}

	synchronized public GrapheExploration chercherDansOctree(
			Octree<GrapheExploration> o, GrapheExploration r) {
		if (o.box.intersects(r.bs)) {
			if (o.level == 0) {
				if (o.value != null) {
					if (o.value == r.parent) {
						return null;

					}
					if (o.value.bs.intersects(r.bs)) {
						return o.value;
					}
				}
			}
			if (o.children == null) {
				return null;
			}
			for (Octree<GrapheExploration> enfant : o.children) {
				GrapheExploration ge = this.chercherDansOctree(enfant, r);
				if (ge != null) {

					return ge;

				}

			}

		}
		return null;

	}

	public int totalGrapheExploration = 0;

	public void incrementerTotalGrapheExploration() {
		totalGrapheExploration++;
		// Log.print(" total " + totalGrapheExploration + " " + this);

	}

	synchronized public void enregistrer(Octree<GrapheExploration> o,
			GrapheExploration courant) {
		if (o.box.intersects(courant.bs)) {
			if (o.level == 0) {

				o.value = courant;
				return;
			}
			if (o.children == null) {

				o.init();

			}
			for (Octree<GrapheExploration> enfant : o.children) {
				this.enregistrer(enfant, courant);

			}

		}
		return;

	}

	public GrapheExploration calculerGrapheExploration(int j,
			GrapheExploration gp, float angle) {
		GrapheExploration r = new GrapheExploration();
		r.gge = this;
		r.parent = gp;
		Vector3f currentPos = new Vector3f(gp.pos);
		r.bs.setRadius(rayon);
		GrapheExploration ge = null;

		if (this.cherchePositionSuivante(r.bs, currentPos, angle)) {
			currentPos = new Vector3f(r.bs.getCenter());

			ge = this.chercherDansOctree(octree, r);
			if (ge != null) {
				return null;
			}
			r.pos = currentPos;
			r.omCollision = brickEditor.decor.espace.getOMFor(r.bs, (om) -> {
				return true;
			});

			r.bs.getCenter().set(r.pos);
			gp.enfants.add(r);

			return r;
		}
		return null;

	}

	public void enregistrer(GrapheExploration ge) {
		ge.bs.setRadius(rayon);
		ge.bs.setCenter(ge.pos);
		enregistrer(octree, ge);

	}

	public void calculerGrapheExploration(GrapheExploration ge) {

		float angleDeltat = (float) (2 * Math.PI / (double) nbDirection);
		float angle = 0.0f;
		ge.bs.setRadius(rayon);

		for (int j = 0; j < nbDirection; j++) {

			GrapheExploration r = this.calculerGrapheExploration(j, ge, angle);
			if (r != null) {

				enregistrer(octree, r);
				Log.print(" enregistrer " + r);

			}
			angle += angleDeltat;

		}

	}

	public List<GrapheExploration> lsOld = new ArrayList<>();
	List<GrapheExploration> lsNew = new ArrayList<>();
	int numStep;

	public boolean calculerGrapheExplorationEnLargeur(int total) {
		if (racine.nbNonVisisite <= 0) {
			racine.resetNonVisite();
		}
		if (lsOld.isEmpty()) {
			// Log.print(" fini "+total);
			return false;
		}
		// Log.print(" exploration "+total+" "+lsOld.size());
		while (total > 0) {
			if (idx < lsOld.size()) {
				GrapheExploration ge = lsOld.get(idx);

				ge.run();

				if (grapheDebug != null) {

					// /this.creerGrapheDebug(ge);
				}
				idx++;
				total--;
			} else {

				List<GrapheExploration> lsTmp = lsOld;
				lsOld.clear();
				lsOld = lsNew;
				lsNew = lsTmp;

				idx = 0;
				if (lsOld.isEmpty()) {
					return false;
				}

			}

		}

		racine.resetNonVisite();
		return true;

	}

	public void creerGrapheDebug(GrapheExploration ge) {
		if (ge.parent != null) {
			GrapheLigne gl = new GrapheLigne(ge.parent.pos, ge.pos);

			// Log.print(" " + ge.parent.pos + " -> " + ge.pos);
			grapheDebug.lignes.add(gl);
			// Log.print(" developper "+ge+" "+grapheDebug.lignes.size());
		}
		if (ge.enfants == null) {
			return;
		}

		for (GrapheExploration enfant : ge.enfants) {

			this.creerGrapheDebug(enfant);

		}

	}

	public void creerGrapheDebug() {
		List<GrapheLigne> ls = new ArrayList<>();
		grapheDebug = new GrapheDebug(ls);
		if (racine.enfants == null) {
			return;
		}

		this.creerGrapheDebug(racine);

	}

	transient public GrapheDebug grapheDebug;

}
