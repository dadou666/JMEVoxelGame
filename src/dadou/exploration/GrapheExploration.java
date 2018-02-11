package dadou.exploration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.bounding.BoundingSphere;
import com.jme.math.Vector3f;

import dadou.Log;
import dadou.ModeleEventInterface;
import dadou.ObjetMobile;
import dadou.graphe.GrapheElement;

public class GrapheExploration implements Runnable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4653387364017453342L;
	public GestionGrapheExploration gge;
	public BoundingSphere bs = new BoundingSphere();
	public Vector3f pos = new Vector3f();
	public GrapheExploration parent;

	public List<GrapheExploration> enfants;
	public ObjetMobile omCollision;
	public long visitTime = 0;
	public int nbNonVisisite = 0;
	public float distanceRacine = 0.0f;
	public float distanceChemin = 0.0f;
	public int profondeur = 0;
	public GrapheExploration geDistanceMax;

	public void ecrire(Map<GrapheExploration, Integer> adresses,
			DataOutputStream stream) throws IOException {
		stream.writeFloat(pos.x);
		stream.writeFloat(pos.y);
		stream.writeFloat(pos.z);
	
		if (parent != null) {

			stream.writeInt(adresses.get(this.parent));
		}

			Integer adr = adresses.size();
		adresses.put(this, adr);
		if (enfants != null) {
			for (GrapheExploration ge : enfants) {
				ge.ecrire(adresses, stream);
			}
		}

	}

	public void ecrire(DataOutputStream stream) throws IOException {
		Map<GrapheExploration, Integer> adresses = new HashMap<>();
		this.ecrire(adresses, stream);
	}

	public static GrapheExploration lire(GestionGrapheExploration gge,
			DataInputStream stream) throws IOException {
		List<GrapheExploration> elements = new ArrayList<>();
		GrapheExploration result = null;
		while (stream.available() > 0) {
			if (result == null) {
				result = new GrapheExploration();
				gge.elements.add(result);
				result.lireDonnees(stream);
				result.gge = gge;
				elements.add(result);
			} else {
				GrapheExploration tmp = new GrapheExploration();
				gge.elements.add(tmp);
				tmp.lireDonnees(stream);
				tmp.parent = elements.get(stream.readInt());
				if (tmp.parent.enfants == null) {
					tmp.parent.enfants = new ArrayList<>();
				}
				tmp.gge = gge;
				tmp.parent.enfants.add(tmp);
				elements.add(tmp);
			}

		}
		return result;
	}

	public void lireDonnees(DataInputStream dis) throws IOException {
		Vector3f pos = new Vector3f();
		pos.x = dis.readFloat();
		pos.y = dis.readFloat();
		pos.z = dis.readFloat();
		this.pos = pos;
	
	}

	public <T extends ModeleEventInterface> void lister(Class<T> cls,
			List<GrapheExploration> liste) {
		if (omCollision != null) {
			if (omCollision.mei.getClass() == cls) {
				liste.add(this);
			}
		}
		if (this.enfants == null) {
			return;
		}
		for (GrapheExploration ge : this.enfants) {
			ge.lister(cls, liste);

		}
	}

	public void calculerDistanceCheminDepuisRacine() {
		if (parent == null) {
			profondeur = 0;
			distanceRacine = 0.0f;
			return;
		}
		parent.calculerDistanceCheminDepuisRacine();
		profondeur = parent.profondeur + 1;
		distanceRacine = parent.distanceRacine + parent.pos.distance(pos);

	}

	public void listeFeuilles(List<GrapheExploration> feuilles) {
		if (enfants == null || enfants.isEmpty()) {
			feuilles.add(this);
			return;
		}
		for (GrapheExploration ge : enfants) {
			ge.listeFeuilles(feuilles);
		}
	}

	public void visitTime(long visitTime) {
		if (parent != null) {
			// parent.visitTime(visitTime);
		}
		this.visitTime = visitTime;

	}

	public boolean estVide() {
		if (enfants == null) {
			return true;

		}
		if (enfants.isEmpty()) {
			return true;
		}
		return false;
	}

	public GrapheExploration suivant(boolean chercherDistanceMax) {
		if (chercherDistanceMax) {
			this.calculerDistanceMax();
			for (GrapheExploration ge : enfants) {
				if (ge.geDistanceMax == geDistanceMax) {
					return ge;
				}
			}
		}
		GrapheExploration tmp = enfants.get(0);
		long minTime = tmp.visitTime;
		for (GrapheExploration enfant : enfants) {
			if (enfant.visitTime < minTime) {
				tmp = enfant;
				minTime = tmp.visitTime;
				if (minTime == 0) {
					break;
				}
			}
		}
		if (parent != null) {
			if (parent.visitTime < tmp.visitTime) {
				return null;
			}
		}
		return tmp;

	}

	public boolean estAncetreDe(GrapheExploration enfant) {
		if (enfant == this) {
			return true;
		}
		if (enfant == null) {
			return false;
		}

		return estAncetreDe(enfant.parent);

	}

	public GrapheExploration suivant(GrapheExploration cible) {
		if (enfants == null) {
			return null;
		}
		for (GrapheExploration enfant : enfants) {
			if (enfant.estAncetreDe(cible)) {
				return enfant;
			}
		}
		return null;

	}

	public void resetNonVisite() {
		nbNonVisisite = 1;
		this.visitTime = 0;
		if (enfants == null || enfants.isEmpty()) {

			return;
		}
		for (GrapheExploration ge : enfants) {
			ge.resetNonVisite();
			nbNonVisisite += ge.nbNonVisisite;
		}

	}

	public void visiter() {
		nbNonVisisite--;
		if (parent != null) {
			parent.visiter();
		}
	}

	public GrapheExploration suivantNonVisite() {

		GrapheExploration tmp = enfants.get(0);
		long minTime = tmp.nbNonVisisite;
		for (GrapheExploration enfant : enfants) {
			if (enfant.nbNonVisisite > minTime) {
				tmp = enfant;
				minTime = tmp.nbNonVisisite;

			}
		}
		if (parent != null && minTime <= 0) {

			return null;

		}
		Log.print(" nbNonVisite=" + tmp.nbNonVisisite);
		if (tmp.visitTime == 0) {
			tmp.visiter();
		}
		tmp.visitTime = System.currentTimeMillis();
		return tmp;

	}

	public void resetCalculerDistanceMax() {
		this.geDistanceMax = null;
		if (enfants == null || enfants.isEmpty()) {
			return;
		}
		for (GrapheExploration ge : enfants) {
			ge.resetCalculerDistanceMax();
		}
	}

	public void calculerDistanceMax() {
		if (geDistanceMax != null) {
			return;
		}
		if (this.estVide()) {
			this.geDistanceMax = this;
			return;
		}

		for (GrapheExploration ge : enfants) {
			ge.calculerDistanceMax();
			if (geDistanceMax == null) {
				geDistanceMax = ge.geDistanceMax;

			} else {
				if (ge.geDistanceMax.distanceChemin > geDistanceMax.distanceChemin) {
					geDistanceMax = ge.geDistanceMax;
				}
			}
		}

	}

	@Override
	public void run() {
		if (enfants != null) {
			return;
		}
		distanceRacine = pos.distance(gge.racine.pos);
		if (parent != null) {
			distanceChemin = parent.distanceChemin + pos.distance(parent.pos);

		}

		enfants = new ArrayList<>();

		float angleDeltat = (float) (2 * Math.PI / (double) gge.nbDirection);
		float angle = 0.0f;
		bs.setRadius(gge.rayon);

		for (int j = 0; j < gge.nbDirection; j++) {

			GrapheExploration r = gge.calculerGrapheExploration(j, this, angle);
			if (r != null) {

				gge.enregistrer(gge.octree, r);
				gge.incrementerTotalGrapheExploration();
				gge.lsNew.add(r);
			}
			angle += angleDeltat;

		}
		if (enfants.isEmpty() && parent == this.gge.racine) {
			// Log.print(" suppression enfant racine");
			this.gge.racine.enfants.remove(this);
		}

	}

	public String toString() {

		if (parent == null) {
			return "" + pos;
		}
		return "" + parent.pos + "->" + pos ;

	}
}
