package dadou;

import java.util.HashMap;
import java.util.Map;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.jeux.TrajectoireContext;
import dadou.tools.BrickEditor;

public class Espace {
	public Octree<OctreeValeur> octree;
	public Object currentMark;
	public BrickEditor be;
	public Map<String, ListeEvtDetection> mapListeEvtDetection = new HashMap<>();

	public Map<String, ObjetMobilePourModelInstance> mobiles = new HashMap<>();
	public Map<String, TrajectoireContext> trajectoiresContexts = new HashMap<>();

	public void gererTrajectoires() {
		for (Map.Entry<String, TrajectoireContext> et : trajectoiresContexts
				.entrySet()) {
			try {
				TrajectoireContext tc = et.getValue();
				if (tc != null) {
					if (!tc.executer()) {

						// et.setValue(null);
						tc.om.tc = null;
						trajectoiresContexts.put(et.getKey(), null);

					} else {
						if (tc.om.modifierOmbre && !tc.stop) {

							Game.modifierOmbre = 0;
						}
					}
				}
			} catch (JeuxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void boucler() throws StopperBoucle {
		this.boucler(this.octree);

	}

	public void creerEvenementDetectionSon(MondeInterfacePrive i) {
		Joueur j = i.joueur;
		if (j.listSon != null) {
			j.listSon.init();
			octree.visit(j.listSon);
			j.listSon.calculerDistanceCamera(i);
			j.listSon.creerEvenement();

		}

	}

	public void creerEvenementDetection(MondeInterfacePrive i) {
		for (ListeEvtDetection l : mapListeEvtDetection.values()) {
			l.init();
			octree.visit(l);
			l.creerEvenement();

		}

		Joueur j = i.joueur;
		if (j.list != null) {
			j.list.init();
			octree.visit(j.list);
			j.list.creerEvenement();

		}

	}

	public void marquer(Octree<OctreeValeur> oct) {
		if (oct == null || oct.value == null) {
			return;
		}

		oct.mark = currentMark;

	}

	public Espace(BrickEditor be, int niveau, int elementTaille) {

		this.be = be;

	}

	public Vector3f pos = new Vector3f();

	public ObjetMobile getOMFor(Vector3f pos) {

		Octree<OctreeValeur> o = this.octree;
		do {
			int idx = o.getIdxForPos(pos);
			ObjetMobile tmp = o.value.om;
			while (tmp != null) {

				if (tmp.construction == null && tmp.estVisible
						&& tmp.contains(pos)) {
					return tmp;
				}
				tmp = tmp.suivant;

			}
			if (o.children == null) {
				return null;
			}
			o = o.children[idx];
			if (o == null) {
				return null;
			}

		} while (true);

	}

	public ObjetMobile getOMFor(OrientedBoundingBoxWithVBO bs) {
		return getOMFor(octree, bs, true);

	}

	public ObjetMobile getOMFor(OrientedBoundingBoxWithVBO bs,
			boolean ignoreZoneDetection) {
		return getOMFor(octree, bs, ignoreZoneDetection);

	}

	public ObjetMobile getOMFor(OrientedBoundingBoxWithVBO bs, EspaceSelector es) {
		return getOMFor(octree, bs, es);

	}

	public ObjetMobile getOMFor(BoundingSphere bs, boolean ignoreZoneDetection) {
		return getOMFor(octree, bs, ignoreZoneDetection);

	}

	public void vider() {
		this.vider(this.octree);
		this.mobiles = new HashMap<String, ObjetMobilePourModelInstance>();
	}

	public void boucler(Octree<OctreeValeur> oct) throws StopperBoucle {
		if (oct.value != null) {
			ObjetMobile om = oct.value.om;
			while (om != null) {
				try {

					om.animer(this.be);
					if (om.octreeLeaf == null) {
						Log.print(" octreeleaf = null");
					}
					if (om.evenement().boucle(om)) {
						throw new StopperBoucle();
					}
				} catch (Throwable t) {
					if (t instanceof StopperBoucle) {
						throw t;
					}
					t.printStackTrace();

					throw new StopperBoucle();
				}
				om = om.suivant;

			}
		}
		if (oct.children != null) {
			for (Octree<OctreeValeur> o : oct.children) {
				this.boucler(o);

			}
		}

	}

	public void vider(Octree<OctreeValeur> oct) {
		if (oct.value != null) {
			oct.value.om = null;
		}
		if (oct.children != null) {
			for (Octree<OctreeValeur> child : oct.children) {
				this.vider(child);
			}
		}

	}

	public ObjetMobile getOMFor(Octree<OctreeValeur> oct, BoundingSphere bs,
			boolean ignoreZoneDetection) {
		do {

			ObjetMobile tmp = oct.value.om;
			while (tmp != null) {

				if (tmp.estVisible && tmp.contains(bs)
						&& ((tmp.zoneDetection == null) == ignoreZoneDetection)) {

					return tmp;
				}
				tmp = tmp.suivant;

			}
			if (oct.children == null) {
				return null;
			}

			Octree<OctreeValeur> r = oct.getOctreeFor(bs);
			if (r == null) {
				return null;
			}
			if (r == oct) {
				for (Octree<OctreeValeur> o : r.children) {
					ObjetMobile a = getOMFor(o, bs, ignoreZoneDetection);
					if (a != null) {
						return a;
					}

				}
				return null;

			} else {
				return getOMFor(r, bs, ignoreZoneDetection);
			}
		} while (true);

	}

	public ObjetMobile getOMFor(BoundingSphere bs, EspaceSelector es) {
		return this.getOMFor(octree, bs, es);
	}

	public ObjetMobile getOMFor(Octree<OctreeValeur> oct, BoundingSphere bs,
			EspaceSelector es) {
		do {

			ObjetMobile tmp = oct.value.om;
			while (tmp != null) {

				if (tmp.contains(bs) && es.test(tmp)) {

					return tmp;
				}
				tmp = tmp.suivant;

			}
			if (oct.children == null) {
				return null;
			}

			Octree<OctreeValeur> r = oct.getOctreeFor(bs);
			if (r == null) {
				return null;
			}
			if (r == oct) {
				for (Octree<OctreeValeur> o : r.children) {
					ObjetMobile a = getOMFor(o, bs, es);
					if (a != null) {
						return a;
					}

				}
				return null;

			} else {
				return getOMFor(r, bs, es);
			}
		} while (true);

	}

	public void processAll(BoundingSphere bs, EspaceAction es) {
		this.processAll(this.octree, bs, es);
	}

	public void processAll(Octree<OctreeValeur> oct, BoundingSphere bs,
			EspaceAction es) {

		ObjetMobile tmp = oct.value.om;
		while (tmp != null) {

			if (tmp.contains(bs)) {
				es.executer(tmp);
			}
			tmp = tmp.suivant;

		}
		if (oct.children == null) {
			return;
		}

		Octree<OctreeValeur> r = oct.getOctreeFor(bs);
		if (r == null) {
			return;
		}
		if (r == oct) {
			for (Octree<OctreeValeur> o : r.children) {
				processAll(o, bs, es);

			}

		} else {
			processAll(r, bs, es);
		}

	}

	public ObjetMobile getOMFor(Octree<OctreeValeur> oct,
			OrientedBoundingBoxWithVBO box, boolean ignoreZoneDetection) {
		do {

			ObjetMobile tmp = oct.value.om;
			while (tmp != null) {

				if (tmp.estVisible && tmp.getBox().intersects(box)
						&& ((tmp.zoneDetection == null) == ignoreZoneDetection)) {

					return tmp;
				}
				tmp = tmp.suivant;

			}
			if (oct.children == null) {
				return null;
			}

			Octree<OctreeValeur> r = oct.getOctreeFor(box);
			if (r == null) {
				return null;
			}
			if (r == oct) {
				for (Octree<OctreeValeur> o : r.children) {
					ObjetMobile a = getOMFor(o, box, ignoreZoneDetection);
					if (a != null) {
						return a;
					}

				}
				return null;

			} else {
				return getOMFor(r, box, ignoreZoneDetection);
			}
		} while (true);

	}

	public ObjetMobile getOMFor(Octree<OctreeValeur> oct,
			OrientedBoundingBoxWithVBO box, EspaceSelector es) {
		do {

			ObjetMobile tmp = oct.value.om;
			while (tmp != null) {

				if (tmp.getBox().intersects(box) && es.test(tmp)) {

					return tmp;
				}
				tmp = tmp.suivant;

			}
			if (oct.children == null) {
				return null;
			}

			Octree<OctreeValeur> r = oct.getOctreeFor(box);
			if (r == null) {
				return null;
			}
			if (r == oct) {
				for (Octree<OctreeValeur> o : r.children) {
					ObjetMobile a = getOMFor(o, box, es);
					if (a != null) {
						return a;
					}

				}
				return null;

			} else {
				return getOMFor(r, box, es);
			}
		} while (true);

	}

	public ObjetMobile getOMFor(int x, int y, int z) {
		pos.set(x - octree.dim, y - octree.dim, z - octree.dim);

		return this.getOMFor(pos);

	}

	public ObjetMobilePourModelInstance ajouter(ModelInstance m) {
		return this.ajouter(m, m.nomObjet);

	}

	public ObjetMobilePourModelInstance ajouter(ModelInstance m, String nom) {
		ObjetMobilePourModelInstance r = new ObjetMobilePourModelInstance(be, m);

		mobiles.put(nom, r);

		return r;

	}

	public void supprimer(ObjetMobile om) {

		if (om == null) {
			return;
		}
		om.detachFromOctree();
		mobiles.remove(om.donnerNom());
		TrajectoireContext tc = trajectoiresContexts.get(om.donnerNom());
		if (tc != null) {
			trajectoiresContexts.remove(om.donnerNom());

		}

	}

	public void clear() {
		for (ObjetMobilePourModelInstance o : mobiles.values()) {
			o.detachFromOctree();
			o.tc = null;

		}
		trajectoiresContexts = new HashMap<>();

	}

	public void supprimer(ObjetMobilePourModelInstance om) {
		om.detachFromOctree();
		String nom = om.donnerNom();
		this.trajectoiresContexts.remove(nom);
		this.mobiles.remove(nom);
		this.mapListeEvtDetection.remove(nom);

	}

	public void reset() {

		Map<String, ObjetMobilePourModelInstance> map = mobiles;
		mobiles = new HashMap<>();
		mapListeEvtDetection = new HashMap<>();

		for (Map.Entry<String, ObjetMobilePourModelInstance> e : map.entrySet()) {

			ObjetMobilePourModelInstance o = e.getValue();
			if (o.emeteurs != null) {

				o.emeteurs.stop();

			}
			o.animation = null;
			o.tc = null;
			if (o.nom == null) {
				o.reset();
				mobiles.put(e.getKey(), o);

			} else {

				o.detachFromOctree();
			}
		}

		trajectoiresContexts = new HashMap<>();

	}

	/*
	 * public ObjetMobile objetMobileCollisionAvecCamera() {
	 * 
	 * 
	 * 
	 * 
	 * for (int k = 0; k < action.idxVisibilite; k++) { ObjetMobile om =
	 * action.visibleObjetMobile.get(k); if
	 * (action.visibleObjetMobile.get(k).collisionCamera(be.game, verifieur)) {
	 * return om; } } return null; }
	 */

	public ObjetMobile objetMobileCollisionAvecCamera(boolean zoneDetection) {
		return this.getOMFor(this.be.scc.sphereCam, zoneDetection);

	}

	public int nbInvisible = 0;

}
