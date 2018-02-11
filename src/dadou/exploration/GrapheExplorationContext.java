package dadou.exploration;

import java.util.ArrayList;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

import dadou.Log;
import dadou.MondeInterfacePublic;
import dadou.ObjetMobile;
import dadou.VoxelTexture3D.CouleurErreur;

public class GrapheExplorationContext {
	public enum Etat {
		Translation, Rotation, Arret
	};

	Etat etat;

	public GrapheExploration arrive;

	public MondeInterfacePublic i;

	public List<GrapheExploration> oldScans = new ArrayList<>();
	public GestionGrapheExploration gge;
	public long naissance;
	public List<Vector3f> chemin = new ArrayList<>();
	public List<GrapheExploration> scans = new ArrayList<>();

	public BoundingSphere bs = new BoundingSphere();
	public Vector3f direction = new Vector3f(1, 0, 0);
	public Vector3f directionNew = new Vector3f(1, 0, 0);
	public GrapheExploration cible;

	public GrapheExplorationContext(MondeInterfacePublic i,
			GrapheExploration racine) {
		this.arrive = racine;
		this.gge = racine.gge;
		this.i = i;
		this.etat = Etat.Arret;
		naissance = System.currentTimeMillis();
	}

	public void finDeplacement(ObjetMobile om) {
		etat = Etat.Arret;

	}

	public void finOrientation(ObjetMobile om) {

		etat = Etat.Translation;
		i.deplacerVers(om.donnerNom(), arrive.pos);

	}

	public void allerVersParent(ObjetMobile om) {

		Vector3f tmpPos = new Vector3f();

		tmpPos.set(arrive.parent.pos);

		tmpPos.setY(om.getBox().getCenter().y);

		arrive = arrive.parent;
	
		this.orienterVers(tmpPos, om);

	}

	public void orienterVers(Vector3f pos, ObjetMobile om) {
		float x = pos.x - om.getBox().getCenter().x;
		float y = pos.z - om.getBox().getCenter().z;

		this.directionNew.set(x, 0, y);
		if (this.directionNew.equals(Vector3f.ZERO)) {
			Log.print(" ZERO ");
			return;
		}
		// Log.print("direction=" + this.directionNew);
		this.directionNew.normalizeLocal();
		float dot = this.directionNew.dot(direction);
		if (dot <= -1.0f) {
			dot = -1.0f;
		}
		if (dot >= 1.0f) {
			dot = 1.0f;
		}
		// Log.print(" dot=" + dot);
		float angle = FastMath.acos(dot);
		// angle = (float) Math.toDegrees(angle);
		Vector3f axe = direction.cross(directionNew);
		if (axe.equals(Vector3f.ZERO)) {
			if (angle == 0.0f) {
				// Log.print(" rien ");
				return;
			}
			// Log.print(" ZERO " + angle);

			axe = new Vector3f(0.0f, 1, 0);

			angle = FastMath.PI;
		}
		axe.normalizeLocal();
		// axe.negateLocal();
		// Log.print(" angle =" + angle + " - " + this.direction + "-" + axe);
	
		i.orienterAvecAxeEtAngle(om.donnerNom(), axe, angle);
		etat = Etat.Rotation;
		// this.direction.set(directionNew);
	}

	public boolean alive = true;

	public void initScan() {
	//	arrive = null;

	}

	public GrapheExploration choisirScan(ObjetMobile om) {

		for (GrapheExploration ge : scans) {
			if (gge.cheminDepuisVers(om, bs, ge.pos, chemin)
					&& !oldScans.contains(ge)) {
				return ge;

			}

		}

		return null;
	}

	public void boucle(ObjetMobile om) {
		// TODO Auto-generated method stub
	//	Log.print(" etat "+this+" "+etat+" "+om.mutator);
		cible = this.gge.donnerGrapheExplorationJoueur();

		if (cible == null) {
			Log.print(" rien ");
		}
		if (etat == Etat.Arret) {

			GrapheExploration tmp = null;

			if (cible != null) {
				tmp = arrive.suivant(cible);

				if (tmp == null) {

					this.allerVersParent(om);
					return;

				}

				arrive = tmp;

				arrive.visitTime = System.currentTimeMillis();

				Vector3f tmpPos = new Vector3f();

				tmpPos.set(arrive.pos);
				tmpPos.setY(om.getBox().getCenter().y);
		
				this.orienterVers(tmpPos, om);
			}

		}

	}

	public void collisionObjetMobile(ObjetMobile om) {
		if (etat != Etat.Translation) {
			return;
		}
		bs.setRadius(40);
		bs.getCenter().set(om.getBox().getCenter());
		scans.clear();
		if (oldScans.size() >= 5) {
			oldScans.clear();
		}
		gge.chercherListeGrapheExploration(bs, scans);
		GrapheExploration ge = this.choisirScan(om);
		if (ge != null && ge != gge.racine) {

			oldScans.add(ge);

			arrive = ge;
			

			Vector3f tmpPos = new Vector3f();
			
			tmpPos.set(ge.pos);
			tmpPos.setY(om.getBox().getCenter().y);
		//	Log.print(" scan l=" + tmpPos.distance(om.getBox().getCenter()));
			this.orienterVers(tmpPos, om);
		
			return;
		};
		this.detruire(om);
		return;

	}

	public boolean detruire(ObjetMobile om) {

		alive = false;
		return true;

	}

}
