package dadou;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.tools.BrickEditor;
import dadou.tools.SerializeTool;
import dadou.tools.canon.CibleAffichageDistance;

public class DecorDeBrique {
	public Octree<OctreeValeur> octree;
	public VoxelTexture3D tex;

	public DecorDeBriqueDataElement DecorDeBriqueData;

	public List<VisibleOctree<OctreeValeur>> ls = new ArrayList<>();
	public int dimOctree;
	public OctreeActionDecor action;
	public boolean sauvegarde = false;
	public List<ElementDecor> listeElementDecorModifie = new ArrayList<>();

	public GestionCollision gestionCollision;
	public Espace espace;
	public BrickEditor g;

	public void supprimer() {

	}

	public void ajouterLumieres(Octree<OctreeValeur> octree, Lumiere l) {
		if (!octree.box.intersects(l.boundingSphere(false))) {
			return;
		}
		ObjetMobile om = octree.value.om;
		while (om != null) {
			om.lb = null;
			om = om.suivant;
		}
		if (octree.level == 0) {
			// Log.print("ajout"+octree);
			octree.value.ajouterLumiere(l);
			return;
		}

		for (Octree<OctreeValeur> tmp : octree.children) {
			this.ajouterLumieres(tmp, l);
		}

	}

	public void supprimerLumieres() {
		this.supprimerLumieres(octree);

	}

	public void supprimerLumieres(Octree<OctreeValeur> octree) {
		if (octree == null) {
			return;
		}
		if (octree.level == 0) {

			octree.value.lumiereBuffer.resetBuffer();
			return;
		}

		for (Octree<OctreeValeur> tmp : octree.children) {
			this.supprimerLumieres(tmp);
		}

	}

	public void supprimerZones() {
		this.supprimerZones(octree);

	}

	public void supprimerZones(Octree<OctreeValeur> octree) {
		if (octree == null) {
			return;
		}
		if (octree.level == 0) {

			octree.value.zoneBuffer.resetBuffer();
			return;
		}

		for (Octree<OctreeValeur> tmp : octree.children) {
			this.supprimerZones(tmp);
		}

	}

	public void ajouterLumieres() {
		this.supprimerLumieres();
		if (this.DecorDeBriqueData.lumieres == null) {
			return;
		}
		for (Lumiere l : this.DecorDeBriqueData.lumieres) {
			// Log.print("ajout"+l);
			this.ajouterLumieres(octree, l);
		}
	}

	public void ajouterZones() {
		this.supprimerZones();
		if (this.DecorDeBriqueData.zones == null) {
			return;
		}
		for (Zone l : this.DecorDeBriqueData.zones) {
			// Log.print("ajout"+l);
			this.ajouterZones(octree, l);
		}
	}

	public void ajouterZones(Octree<OctreeValeur> octree, Zone l) {
		if (!octree.box.intersects(l.boundingSphere(false))) {
			return;
		}
		ObjetMobile om = octree.value.om;
		while (om != null) {
			om.lb = null;
			om = om.suivant;
		}
		if (octree.level == 0) {
			// Log.print("ajout"+octree);
			octree.value.ajouterZone(l);
			return;
		}

		for (Octree<OctreeValeur> tmp : octree.children) {
			this.ajouterZones(tmp, l);
		}

	}

	public DecorDeBrique(BrickEditor g, DecorDeBriqueDataElement data)
			throws IOException, CouleurErreur, ClassNotFoundException {
		action = new OctreeActionDecor(data.decorInfo.niveau);
		action.decor = this;
		this.g = g;
		espace = g.espace;
		this.initMonde(g, data);

	}

	public DecorDeBrique(BrickEditor g, int niveau) throws IOException, CouleurErreur, ClassNotFoundException {
		if (g.game != null) {
			action = new OctreeActionDecor(niveau);
			action.decor = this;
		}
		espace = g.espace;
		this.g = g;

	}

	public DecorDeBrique(DecorDeBriqueDataElement data) throws IOException, CouleurErreur, ClassNotFoundException {
		this.DecorDeBriqueData = data;

	}

	public void initMonde(BrickEditor g, DecorDeBriqueDataElement data)
			throws IOException, ClassNotFoundException, CouleurErreur {

		if (data.decorInfo == null) {
			data.decorInfo = new DecorInfo();
			int elementTaille = BrickEditor.elementTaille;
			int niveau = BrickEditor.niveau;
			data.decorInfo.init(data, niveau, elementTaille);

		}
		if (DecorDeBriqueData != null) {
			DecorDeBriqueData.delete();

		}
		DecorDeBriqueData = data;
		int elementTaille = data.decorInfo.elementTaille;
		int niveau = data.decorInfo.niveau;

		this.initOctree(g);
		if (g.swingEditor != null) {
			g.swingEditor.reloadTree();
		}
		if (g.espace != null) {
			g.espace.clear();
		}
		g.scc.charger(data.cameraPosition);
		this.initialiserModelInstances(g);

	}

	public void creerChargeurDecor(ChargeurDecor r, BrickEditor g, DecorDeBriqueDataElement data)
			throws IOException, ClassNotFoundException, CouleurErreur {

		if (data.decorInfo == null) {
			data.decorInfo = new DecorInfo();
			int elementTaille = BrickEditor.elementTaille;
			int niveau = BrickEditor.niveau;
			data.decorInfo.init(data, niveau, elementTaille);

		}

		DecorDeBriqueData = data;
		if (data.elementsDecor == null) {
			
			Log.print("init element decor");
			this.initElementDecor();
			r.total = data.nbElementFlux;
			Log.print("creer chargeur decor ");
		
	
		} else {

			ChargeurElementDecor ced = this.creerChargeurDecor(g);

			r._ChargeurElementDecor = ced;
			r.total = ced.total();
		}
		r.decor = this;

		r.g = g;

	}

	public void initElementDecor() {
		int elementTaille = DecorDeBriqueData.decorInfo.elementTaille;
		int niveau = DecorDeBriqueData.decorInfo.niveau;
		Game g = this.g.game;

		// tex.ecouteur = gestionCollision;
		if (g != null) {
			this.initVBOVisibilite(elementTaille);
			lp = g.createVoxelShaderParam(this.g.donnerHabillage(DecorDeBriqueData.nomHabillage));
		}
		octree = new Octree<>(new Vector3f(0, 0, 0), niveau, elementTaille);
		espace.octree = octree;

		if (this.DecorDeBriqueData.elementsDecor == null) {
			this.DecorDeBriqueData.decorInfo.initElementDecor(DecorDeBriqueData);

		}
	}

	public void initialiserModelInstances(BrickEditor g) throws CouleurErreur {
		g.espace.vider();
		if (DecorDeBriqueData.modelInstances == null) {
			return;

		}
		List<String> modelInstancesPourSuppression = new ArrayList<>();

		for (Map.Entry<String, ModelInstance> e : DecorDeBriqueData.modelInstances.entrySet()) {
			ModelInstance mi = e.getValue();

			mi.modelClasse = g.decorDeBriqueData.models.get(mi.modelClasse.nom);

			if (mi.modelClasse == null) {
				modelInstancesPourSuppression.add(mi.nomObjet);

			} else {

				// mi.modelClasse.initBuffer();

				ObjetMobilePourModelInstance om = g.espace.ajouter(mi);

				if (mi.modelClasse.estVide) {
					om.zoneDetection = new ZoneDetection();
					g.mondeInterface.zoneDetections.put(om.donnerNom(), om);

				}
			}

		}
		for (String nomObjet : modelInstancesPourSuppression) {
			DecorDeBriqueData.modelInstances.remove(nomObjet);
		}

	}

	public Shader shaderVisibilite;
	public VBOVisibilite vboVisibilite;

	public void initVBOVisibilite(float dim) {
		shaderVisibilite = new Shader(Shader.class, "visibilite.frag", "visibilite.vert", null);
		VBOVisibilite vbo = new VBOVisibilite(shaderVisibilite, 8, 36);
		vbo.addVertex(0, 0, 0);// 0
		vbo.addVertex(0, dim, 0);// 1
		vbo.addVertex(dim, dim, 0);// 2
		vbo.addVertex(dim, 0, 0);// 3

		vbo.addVertex(0, 0, dim);// 4
		vbo.addVertex(0, dim, dim);// 5
		vbo.addVertex(dim, dim, dim);// 6
		vbo.addVertex(dim, 0, dim);// 7

		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);

		vbo.addTri(4, 5, 6);
		vbo.addTri(4, 6, 7);

		vbo.addTri(1, 2, 6);
		vbo.addTri(1, 6, 5);

		vbo.addTri(0, 4, 7);
		vbo.addTri(0, 7, 3);

		vbo.addTri(3, 2, 6);
		vbo.addTri(3, 6, 7);

		vbo.addTri(0, 1, 5);

		vbo.addTri(0, 5, 4);
		vbo.createVBO();
		vboVisibilite = vbo;

	}

	public Kube getCube(Vector3f pos, Kube cube) throws CouleurErreur {
		int nbCube = this.DecorDeBriqueData.decorInfo.nbCube;
		if (cube == null) {
			cube = new Kube();

		}
		cube.x = (int) (pos.x + nbCube / 2);
		cube.y = (int) (pos.y + nbCube / 2);
		cube.z = (int) (pos.z + nbCube / 2);
		cube.position = new Vector3f(cube.x, cube.y, cube.z);
		cube.position.addLocal(-nbCube / 2, -nbCube / 2, -nbCube / 2);
		cube.position.addLocal(0.5f, 0.5f, 0.5f);

		cube.color = this.lireCouleur(cube.x, cube.y, cube.z);

		return cube;

	}

	public ElementDecor donnerElementDecor(int x, int y, int z) {
		if (x < 0) {
			return null;
		}
		if (y < 0) {
			return null;
		}
		if (z < 0) {
			return null;
		}
		int nbCube = DecorDeBriqueData.decorInfo.nbCube;
		if (x >= nbCube) {
			return null;
		}
		if (y >= nbCube) {
			return null;
		}
		if (z >= nbCube) {
			return null;
		}
		int elementTaille = DecorDeBriqueData.decorInfo.elementTaille;

		int px = x / elementTaille;
		int py = y / elementTaille;
		int pz = z / elementTaille;
		return DecorDeBriqueData.elementsDecor[px][py][pz];
	}

	public static int d[] = new int[] { -1, 0, 1 };

	public void modifierBrique(int x, int y, int z) {

		ElementDecor ed = null;
		for (int dx : d) {
			for (int dy : d) {
				for (int dz : d) {
					int ux = x + dx;
					int uy = y + dy;
					int uz = z + dz;
					ed = this.donnerElementDecor(ux, uy, uz);
					if (ed != null && !ed.estModifie) {
						this.listeElementDecorModifie.add(ed);
						ed.estModifie = true;

					}
				}
			}
		}

	}

	public void addBrique(int x, int y, int z) throws CouleurErreur {
		this.modifierBrique(x, y, z);
	}

	public void ecrireCouleur(int x, int y, int z, Color color) throws CouleurErreur {
		this.DecorDeBriqueData.ecrireCouleur(x, y, z, color);
		if (!ElementDecor.estVide(color)) {

			gestionCollision.ajouterBrique(x, y, z);

		} else {

			gestionCollision.supprimerBrique(x, y, z);

		}

	}

	public Color lireCouleur(int x, int y, int z) throws CouleurErreur {

		return this.DecorDeBriqueData.lireCouleur(x, y, z);
	}

	public void removeBrique(int x, int y, int z) throws CouleurErreur {
		this.modifierBrique(x, y, z);

	}

	public VoxelShaderParam lp;

	public void reconstuire() throws CouleurErreur {
		Game.modifierOmbre = 0;
		if (!this.listeElementDecorModifie.isEmpty()) {
			sauvegarde = true;
		}
		for (ElementDecor e : this.listeElementDecorModifie) {
			e.calculerNbBrique(this);
			e.reconstruire(this);
			e.estModifie = false;

		}
		this.listeElementDecorModifie.clear();

	}

	public void initOctree(BrickEditor be) throws CouleurErreur {
		int elementTaille = DecorDeBriqueData.decorInfo.elementTaille;
		int niveau = DecorDeBriqueData.decorInfo.niveau;
		Game g = be.game;
		// tex.ecouteur = gestionCollision;
		this.initVBOVisibilite(elementTaille);
		lp = g.createVoxelShaderParam(be.donnerHabillage(DecorDeBriqueData.nomHabillage));
		octree = new Octree<>(new Vector3f(0, 0, 0), niveau, elementTaille);
		espace.octree = octree;
		int n = (int) Math.pow(2, niveau);
		float m = (float) Math.pow(2, niveau - 1);
		float fElementTaille = elementTaille;
		int total = 0;
		for (int x = 0; x < n; x++) {

			for (int y = 0; y < n; y++) {
				for (int z = 0; z < n; z++) {
					total++;
					// System.out.println(" total="+total);

					float px = x;
					float py = y;
					float pz = z;
					px = (px + 0.5f - m) * fElementTaille;
					py = (py + 0.5f - m) * fElementTaille;
					pz = (pz + 0.5f - m) * fElementTaille;

					ElementDecor et = DecorDeBriqueData.getElementDecor(x, y, z);

					Vector3f pos = new Vector3f(px, py, pz);

					Octree<OctreeValeur> o = octree.createLeafFor(pos);
					this.init(o);

					o.value.ed = et;
					et.octree = o;

					initAvecVBOMinimun(o);

				}

			}
		}
		gestionCollision = new GestionCollision(this, niveau + 4, 1);

	}

	public ChargeurElementDecor creerChargeurElementDecor(BrickEditor be, ElementDecor ed) {
		int elementTaille = DecorDeBriqueData.decorInfo.elementTaille;
		int niveau = DecorDeBriqueData.decorInfo.niveau;
		Game g = be.game;

		// tex.ecouteur = gestionCollision;
		if (g != null) {
			this.initVBOVisibilite(elementTaille);
			lp = g.createVoxelShaderParam(be.donnerHabillage(DecorDeBriqueData.nomHabillage));
		}

		float m = (float) Math.pow(2, niveau - 1);
		float fElementTaille = elementTaille;

		ChargeurElementDecor r = new ChargeurElementDecor();
		DecorDeBriqueData.elementsDecor[ed.x][ed.y][ed.z] =ed;

		// System.out.println(" total="+total);

		float px = ed.x;
		float py = ed.y;
		float pz = ed.z;
		px = (px + 0.5f - m) * fElementTaille;
		py = (py + 0.5f - m) * fElementTaille;
		pz = (pz + 0.5f - m) * fElementTaille;

		r.x = ed.x;
		r.y = ed.y;
		r.z = ed.z;
		r.px = px;
		r.py = py;
		r.pz = pz;

		return r;
	}

	public ChargeurElementDecor creerChargeurDecor(BrickEditor be) throws CouleurErreur {
		int elementTaille = DecorDeBriqueData.decorInfo.elementTaille;
		int niveau = DecorDeBriqueData.decorInfo.niveau;
		Game g = be.game;

		// tex.ecouteur = gestionCollision;
		if (g != null) {
			this.initVBOVisibilite(elementTaille);
			lp = g.createVoxelShaderParam(be.donnerHabillage(DecorDeBriqueData.nomHabillage));
		}
		octree = new Octree<>(new Vector3f(0, 0, 0), niveau, elementTaille);
		espace.octree = octree;

		int n = (int) Math.pow(2, niveau);
		float m = (float) Math.pow(2, niveau - 1);
		float fElementTaille = elementTaille;
		if (this.DecorDeBriqueData.elementsDecor == null) {
			this.DecorDeBriqueData.decorInfo.initElementDecor(DecorDeBriqueData);

		}

		ChargeurElementDecor r = null;

		for (int x = 0; x < n; x++) {

			for (int y = 0; y < n; y++) {
				for (int z = 0; z < n; z++) {

					// System.out.println(" total="+total);

					float px = x;
					float py = y;
					float pz = z;
					px = (px + 0.5f - m) * fElementTaille;
					py = (py + 0.5f - m) * fElementTaille;
					pz = (pz + 0.5f - m) * fElementTaille;
					ElementDecor ed = this.DecorDeBriqueData.elementsDecor[x][y][z];
					if (ed != null && (ed.bytes != null || ed.pt != null)) {
						ChargeurElementDecor tmp = new ChargeurElementDecor();
						tmp.x = x;
						tmp.y = y;
						tmp.z = z;
						tmp.px = px;
						tmp.py = py;
						tmp.pz = pz;

						tmp.suivant = r;
						r = tmp;
					}

				}

			}
		}

		return r;

	}

	public void init(Octree<OctreeValeur> o) {
		while (o != null) {
			if (o.value == null) {
				o.value = new OctreeValeur();
				o.value.initQuery();
			}
			o = o.parent;

		}
	}

	public void initHeadless(Octree<OctreeValeur> o) {
		while (o != null) {
			if (o.value == null) {
				o.value = new OctreeValeur();

			}
			o = o.parent;

		}
	}

	public void initAvecVBOMinimun(Octree<OctreeValeur> elt) throws CouleurErreur {
		elt.value.ed.construire(this, elt);

	}

	public Vector3f tmp = new Vector3f();

	public void dessiner(final Octree<OctreeValeur> octree, final Camera cam) {
		action.cam = cam;
		octree.execute(cam, action);
	}

	public void dessiner(final Camera cam) throws InterruptedException, Exception {
		action.objetMobiles.clear();
		action.dansCameraMark = new Object();

		action.cibles.clear();
		Game.fps.calculer(() -> {

			dessiner(octree, cam);
			action.render();

		});
		if (Game.fps.nombreImage == 0) {
			long fps = Game.fps.getResult();
			long fpsMesure = Game.fpsMesure.getResult();
			long fpsGlobal = Game.fpsGlobal.getResult();
			Display.setTitle("" + fps + " " + fpsMesure + " " + fpsGlobal);
		}
		if (Game.rm != Game.RenderMode.Depth) {

			if (!action.objetMobilesTransparent.isEmpty()) {
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				for (int i = action.objetMobilesTransparent.size() - 1; i >= 0; i--) {

					ObjetMobile om = action.objetMobilesTransparent.get(i);
					ObjetMobilePourModelInstance.renduTransparence = om.transparence;
					// Log.print(" transparence ",om.mc.nomModele);
					om.dessiner(cam);
				}

			}

			/*
			 * if (this.g.soleil != null) { GL11.glEnable(GL11.GL_BLEND);
			 * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); for
			 * (Lumiere l : action.lumieres) { l.dessinerLumiere(cam); }
			 * this.g.soleil.dessinerLumiere(cam); }
			 */
			ObjetMobilePourModelInstance.renduTransparence = 0.0f;

			GL11.glDisable(GL11.GL_BLEND);
			for (CibleAffichageDistance cad : action.cibles) {
				cad.dessiner(cam);
			}

		}
		// action.renderWithQuery();
	}

}
