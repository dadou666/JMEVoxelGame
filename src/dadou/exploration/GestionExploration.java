package dadou.exploration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Vector3f;

import dadou.Log;
import dadou.MondeInterfacePrive;
import dadou.MondeInterfacePublic;
import dadou.ObjetMobile;
import dadou.Octree;
import dadou.OrientedBoundingBoxWithVBO;
import dadou.graphe.GrapheDebug;
import dadou.graphe.GrapheLigne;

public class GestionExploration {
	public float rayon = 1.25f;
	public float vitesse = 2.0f;
	public int nbDirection = 50;
	public int nbCube = 3;
	public float hauteur = 1.75f;
	public Vector3f positionCible;
	public int idxPositionCible = 0;

	Vector3f N = new Vector3f();

	public ObjetMobile om;

	public OctreeExploration octree;

	public List<DirectionExploration> directionExplorations = new ArrayList<>();
	public DirectionExploration resultat = new DirectionExploration();

	public void creerNouvelleMarqueVisite() {
		octree.marqueVisitePrecedent = octree.marqueVisite;
		octree.marqueVisite = new Object();
		scannerEnContinue = true;
	}

	MondeInterfacePrive i;
	MondeInterfacePublic ip;
	public int nbVisite = 0;
	public boolean scannerEnContinue = true;

	public boolean scannerEnContinue() {
		if (octree.marqueVisitePrecedent == null) {
			return false;
		}
		return scannerEnContinue;

	}

	public GrapheDebug gd;

	public boolean augmenteBoxExploration(Vector3f pos) {
		return this.octree.augmenteBoxExploration(pos, rayon);
	}

	public void modifierBoxExploration() {
		octree.modifierBoxExploration();
	}

	public void modifierBoxExploration(Vector3f pos) {
		this.octree.modifierBoxExploration(pos, rayon);
	}

	public void demarer(MondeInterfacePublic ip, ObjetMobile om) {
		this.om = om;
		this.ip = ip;
		OrientedBoundingBoxWithVBO box = (OrientedBoundingBoxWithVBO) om
				.getBox();

		Vector3f p = box.getCenter();

		rayon = (box.getExtent().length());
		vitesse = rayon / 2.0f;
		octree.init(p, rayon);

		calculerHauteur(p);
		this.calculerDirectionExploration(p);
		recupererResultat();
		idxPositionCible = 0;
		if (idxPositionCible < resultat.size) {
			positionCible = resultat.positions.get(idxPositionCible);
			marquer(positionCible);
			Vector3f posDir = new Vector3f(positionCible);
			posDir.y = p.y;
			ip.orienterVersPosition(om.donnerNom(), posDir);

		}

	}

	public void rediriger() {
		if (positionCible == null) {
			positionCible = scanner(om.getBox().getCenter());
			if (positionCible == null) {
				return;
			}
			idxPositionCible = 0;
			Vector3f posDir = new Vector3f(positionCible);
			posDir.y = om.getBox().getCenter().y;

			ip.orienterVersPosition(om.donnerNom(), posDir);

		}
	}

	public void finOrientation() {

		if (positionCible == null) {
			return;
		}
		ip.deplacerVers(om.donnerNom(), positionCible);
		idxPositionCible++;
	}

	public void annulerPositionCible() {
		positionCible = null;
	}

	public void finDeplacement() {
		boolean orienter = false;
		Vector3f persoPos = om.getBox().getCenter();
		if (scannerEnContinue()) {

			calculerDirectionExploration(persoPos);

			if (directionExplorations.get(0).size > 1) {
				// Log.print(" route alternative ");
				recupererResultat();

				orienter = true;
				idxPositionCible = 0;

				scannerEnContinue = false;

			}
		}

		if (idxPositionCible < resultat.size) {
			positionCible = resultat.positions.get(idxPositionCible);

		} else {

			positionCible = scanner(persoPos);
			orienter = true;
			// Log.print(" angle =" + ge.resultat.angle);
			idxPositionCible = 0;
		}

		if (positionCible != null) {

			marquer(positionCible);
			this.modifierBoxExploration(positionCible);

			if (!orienter) {
				idxPositionCible++;

				ip.deplacerVers(om.donnerNom(), positionCible);
			} else {
				Vector3f posDir = new Vector3f(positionCible);
				posDir.y = persoPos.y;
				ip.orienterVersPosition(om.donnerNom(), posDir);

			}
		}
	}

	public void calculerHauteur(Vector3f pos) {
		bs.getCenter().set(pos);
		bs.setRadius(rayon);
		hauteur = 0.0f;
		float pas = 0.01f;
		while (!i.brickEditor.decor.gestionCollision.verifierCollision(bs)) {
			hauteur += pas;
			bs.getCenter().y -= pas;

		}
		// Log.print("hauteur=" + hauteur);

	}

	public GestionExploration(MondeInterfacePrive i, int niveau, float d) {
		this.i = i;
		octree = new OctreeExploration(new Vector3f(0, 0, 0), niveau, d);
		for (int u = 0; u < nbDirection; u++) {
			this.directionExplorations.add(new DirectionExploration());
		}

	}

	public GestionExploration(MondeInterfacePrive i, OctreeExploration octree) {
		this.i = i;
		this.octree = octree;
		for (int u = 0; u < nbDirection; u++) {
			this.directionExplorations.add(new DirectionExploration());
		}

	}

	public void marquer(Octree<Object> octree, BoundingSphere bs) {
		if (octree.box.intersects(bs)) {
			if (octree.level == 0) {
				if (octree.value == null) {
					nbVisite++;
				}
				octree.value = this.octree.marqueVisite;
				return;
			}
			if (octree.children == null) {
				octree.init();
			}
			for (Octree<Object> enfant : octree.children) {
				this.marquer(enfant, bs);
			}

		}
	}

	public void marquer(BoundingSphere bs) {
		this.marquer(octree, bs);
	}

	public boolean aVisiterEnPriorite(Octree<Object> o, BoundingSphere bs) {
		if (o.box.intersects(bs)) {
			if (o.level == 0) {
				if (o.value == null) {
					return true;
				}
				if (o.value != octree.marqueVisite) {
					if (octree.marqueVisitePrecedent != null) {
						return o.value != octree.marqueVisitePrecedent;
					}
					return true;

				}
				return false;

			}
			if (o.children == null) {
				return true;
			}
			for (Octree<Object> enfant : o.children) {
				if (this.aVisiterEnPriorite(enfant, bs)) {
					return true;
				}
			}

		}
		return false;

	}

	public boolean aVisiter(Octree<Object> o, BoundingSphere bs) {
		if (o.box.intersects(bs)) {
			if (o.level == 0) {

				if (o.value != octree.marqueVisite) {
					return true;

				}
				return false;
			}
			if (o.children == null) {
				return true;
			}
			for (Octree<Object> enfant : o.children) {
				if (this.aVisiter(enfant, bs)) {
					return true;
				}
			}

		}
		return false;

	}

	public boolean aVisiter(BoundingSphere bs) {
		return aVisiter(octree, bs);
	}

	public boolean aVisiterEnPriorite(BoundingSphere bs) {
		return aVisiterEnPriorite(octree, bs);
	}

	BoundingSphere bs = new BoundingSphere();

	public float anglePourMonterCube(int nbCube) {
		double d = nbCube;
		double a = Math.acos(1.0 / Math.sqrt(1.0 + d * d));
		return (float) a;

	}

	public Vector3f scanner(Vector3f pos) {
		if (octree.marqueVisitePrecedent != null) {
			scannerEnContinue = true;
		}
		this.calculerDirectionExploration(pos);
		recupererResultat();

		if (resultat.size > 0) {

			return resultat.positions.get(0);
		}
		creerNouvelleMarqueVisite();
		scannerEnContinue = true;
		// utiliseMarqueVisitePrecedent = true;
		// Log.print("plus de solution ");
		this.calculerDirectionExploration(pos);
		recupererResultat();
		// utiliseMarqueVisitePrecedent = false;
		if (resultat.size > 0) {

			return resultat.positions.get(0);
		}
		return null;

	}

	public void calculerDirectionExploration(Vector3f pos) {

		float angleDeltat = (float) (2 * Math.PI / (double) nbDirection);
		float angle = 0.0f;
		bs.setRadius(rayon);

		for (int j = 0; j < nbDirection; j++) {

			this.calculerPositions(j, pos, angle);

			angle += angleDeltat;

		}
		// gd = this.creerGrapheDebug();

	}

	public GrapheDebug creerGrapheDebug() {
		List<GrapheLigne> ls = new ArrayList<>();
		for (DirectionExploration de : this.directionExplorations) {
			for (int i = 1; i < de.size; i++) {
				GrapheLigne gl = new GrapheLigne(de.positions.get(i),
						de.positions.get(i - 1));
				ls.add(gl);
			}

		}
		return new GrapheDebug(ls);

	}

	public int chercherIndexResultat() {
		int idx = 0;
		boolean estPrioritaire = false;
		for (int u = 0; u < this.directionExplorations.size(); u++) {
			DirectionExploration de = this.directionExplorations.get(u);
			Vector3f dest = de.destination();
			if (dest != null) {
				if (this.augmenteBoxExploration(dest)) {

					// return u;
				}
			}
		}
		for (int u = 1; u < this.directionExplorations.size(); u++) {
			if (this.directionExplorations.get(u).estPrioritaire) {
				if (!estPrioritaire) {
					idx = u;
				}
				estPrioritaire = true;

				if (this.directionExplorations.get(u).critereSelection() > this.directionExplorations
						.get(idx).critereSelection()) {
					idx = u;
				}
			}
		}
		if (estPrioritaire) {

			return idx;
		}

		idx = 0;
		for (int u = 1; u < this.directionExplorations.size(); u++) {
			if (this.directionExplorations.get(u).critereSelection() > this.directionExplorations
					.get(idx).critereSelection()) {
				idx = u;
			}
		}

		return idx;

	}

	public void recupererResultat() {
		int u = this.chercherIndexResultat();

		this.resultat.size = 0;
		this.resultat.angle = directionExplorations.get(u).angle;
		this.resultat.totalLibre = directionExplorations.get(u).totalLibre;
		for (int i = 0; i < directionExplorations.get(u).size; i++) {

			this.resultat
					.ajouter(directionExplorations.get(u).positions.get(i));
		}

	}

	public void marquer(Vector3f pos) {
		this.bs.setCenter(pos);
		this.marquer(bs);
	}

	public boolean log = false;

	public void calculerPositions(int j, Vector3f pos, float angle) {

		DirectionExploration de = directionExplorations.get(j);
		de.size = 0;
		de.idxDir = j;
		Vector3f currentPos = new Vector3f(pos);
		de.angle = angle;

		while (this.cherchePositionSuivante(currentPos, angle)) {
			currentPos = de.ajouter(bs.getCenter());

		}
		int size = de.size;
		// Log.print(" size ="+size);
		de.size = 0;
		de.totalLibre = 0;
		de.estPrioritaire = false;
		for (int u = 0; u < size; u++) {
			this.bs.getCenter().set(de.positions.get(u));
			if (this.aVisiter(bs)) {
				de.size = u + 1;
				de.totalLibre++;

			}
			if (!de.estPrioritaire) {
				de.estPrioritaire = this.aVisiterEnPriorite(bs);
			}

		}

	}

	public boolean testPosition(Vector3f pos) {
		bs.getCenter().set(pos);
		return (i.brickEditor.decor.espace.octree.box.intersects(bs));
	}

	public boolean verifierCollisionDecor(Vector3f pos) {
		bs.getCenter().set(pos);
		return i.brickEditor.decor.gestionCollision.verifierCollision(bs);
	}

	public boolean testPosition(BoundingVolume bv) {

		return (i.brickEditor.decor.espace.octree.box.intersects(bv));
	}

	public Vector3f tmp = new Vector3f();

	public boolean estLie(Vector3f p, Vector3f q) {
		float dx = q.x - p.x;
		float dy = q.z - p.z;
		float l = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / l;
		dy = dy / l;
		tmp.set(p);
		tmp.subtractLocal(q);
		float length = tmp.length();
		tmp.set(p);
		while (true) {
			boolean r = this.cherchePositionSuivante(tmp, dx, dy);
			tmp.set(bs.getCenter());
			tmp.subtractLocal(p);
			boolean vd = tmp.length() > length;
			tmp.set(bs.getCenter());
			if (vd) {
				return true;
			}
			if (!r) {
				return vd;
			}
		}

	}

	public boolean cherchePositionSuivante(Vector3f pos, float dx, float dy) {

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
			N.multLocal(vitesse);
			bs.getCenter().addLocal(N);

			ObjetMobile omCollision = i.brickEditor.decor.espace.getOMFor(bs, (
					o) -> {
				return o != om;
			});

			if (omCollision == null) {

				// Log.print(" omCollision =" +omCollision);
				if (bs.intersects(i.brickEditor.scc.sphereCam)) {
					return false;
				}
				if (i.brickEditor.decor.espace.octree.box.intersects(bs)) {
					if (!i.brickEditor.decor.gestionCollision
							.verifierCollision(bs) && omCollision == null) {
						bs.getCenter().y -= this.hauteur;
						if (i.brickEditor.decor.gestionCollision
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

	public boolean verfierCollision(BoundingBox box) {
		return i.brickEditor.decor.gestionCollision.verifierCollision(box);
	}

	public boolean cherchePositionSuivante(Vector3f pos, float angle) {
		float dx = (float) Math.cos(angle);
		float dy = (float) Math.sin(angle);
		return this.cherchePositionSuivante(pos, dx, dy);

	}

}
