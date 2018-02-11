package dadou;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.ietf.jgss.Oid;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;
import dadou.tools.canon.CibleAffichageDistance;

public class OctreeActionDecor implements OctreeAction<OctreeValeur> {
	public Camera cam;
	public DecorDeBrique decor;
	public int nbDessine = 0;

	public IntBuffer samples;
	public float pixelFactorPourTest = 0.35f;
	public List<Lumiere> lumieres = new ArrayList<>();
	public List<Octree<OctreeValeur>> objetMobileOctree = new ArrayList<>();
	public List<Octree<OctreeValeur>> visibleOctree;
	public List<CibleAffichageDistance> cibles = new ArrayList<>();

	public List<ObjetMobile> objetMobiles = new ArrayList<>();
	public List<ObjetMobile> objetMobilesTransparent = new ArrayList<>();
	public List<ObjetMobile> objetMobilesVisible = new ArrayList<>();

	public int numTriAvantTest = 1000;

	public float h;
	public Octree<OctreeValeur> premierOct;

	

	public Objet3D worldBox;
	public Objet3D worldBoxDepth;
	public Objet3D worldBoxQuery;
	public int queryWorldBox;


	public int LimitVisibilitePixelCount = 10;
	public String nomSkyBox = "skyBox";

	public Vector3f tmp = new Vector3f();

	public int totalOctreeDansCamera;
	public int totalOctreeCache;
	public int totalTrie;
	public int totalOctreeIngnore;
	public float queryDim;

	public OctreeActionDecor(int niveau) {
		// TODO Auto-generated constructor stubisib

		samples = BufferUtils.createIntBuffer(1);
		visibleOctree = new ArrayList<>();

		worldBox = this.createWorldBox(niveau, Game.shaderWorldBox);
		worldBoxDepth = this.createWorldBoxForQuery(niveau + 1, Game.shaderWorldBoxDepth);

		this.initQuery();
		nbCase = (int) Math.pow(2, niveau);
		worldBoxQuery = this.createWorldBoxForQuery(niveau, Game.shaderWorldBoxVisibilite);

	}

	public void initQuery() {
		IntBuffer queries = BufferUtils.createIntBuffer(1);
		GL15.glGenQueries(queries);
		queryWorldBox = queries.get();
	}

	public Objet3D createWorldBoxForQuery(int niveau, Shader shader) {
		this.queryDim = (float) (Math.pow(2, niveau) * BrickEditor.elementTaille);

		float dim = queryDim;

		VBOTexture2D vboWorldBoxVisibility = new VBOTexture2D(shader);
		int i = 0;

		vboWorldBoxVisibility.addVertex(0, 0, 0);// 0

		vboWorldBoxVisibility.addVertex(0, dim, 0);// 1

		vboWorldBoxVisibility.addVertex(dim, dim, 0);// 2

		vboWorldBoxVisibility.addVertex(dim, 0, 0);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.addVertex(0, 0, dim);// 0

		vboWorldBoxVisibility.addVertex(0, dim, dim);// 1

		vboWorldBoxVisibility.addVertex(dim, dim, dim);// 2

		vboWorldBoxVisibility.addVertex(dim, 0, dim);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.addVertex(0, 0, 0);// 0

		vboWorldBoxVisibility.addVertex(0, 0, dim);// 1

		vboWorldBoxVisibility.addVertex(0, dim, dim);// 2

		vboWorldBoxVisibility.addVertex(0, dim, 0);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.addVertex(dim, 0, 0);// 0

		vboWorldBoxVisibility.addVertex(dim, 0, dim);// 1

		vboWorldBoxVisibility.addVertex(dim, dim, dim);// 2

		vboWorldBoxVisibility.addVertex(dim, dim, 0);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.addVertex(0, 0, 0);// 0

		vboWorldBoxVisibility.addVertex(0, 0, dim);// 1

		vboWorldBoxVisibility.addVertex(dim, 0, dim);// 2

		vboWorldBoxVisibility.addVertex(dim, 0, 0);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.addVertex(0, dim, 0);// 0

		vboWorldBoxVisibility.addVertex(0, dim, dim);// 1

		vboWorldBoxVisibility.addVertex(dim, dim, dim);// 2

		vboWorldBoxVisibility.addVertex(dim, dim, 0);// 3

		vboWorldBoxVisibility.addTri(i, i + 1, i + 2);
		vboWorldBoxVisibility.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBoxVisibility.createVBO();
		Objet3D worldBox = new Objet3D();
		worldBox.brique = vboWorldBoxVisibility;
		worldBox.translation(-dim / 2, -dim / 2, -dim / 2);
		return worldBox;
	}

	public float dim;

	public Skybox skyBox() {
		if (nomSkyBox == null) {
			return null;
		}
		Skybox skyBox = BrickEditor.skyboxMap.get(nomSkyBox);
		if (skyBox == null) {
			try {
				skyBox = new Skybox(BrickEditor.cheminRessources + "/" + nomSkyBox);
				BrickEditor.skyboxMap.put(nomSkyBox, skyBox);
			} catch (IOException e) {
				nomSkyBox = null;
				return null;
			}
		}
		return skyBox;
	}

	public Objet3D createWorldBox(int niveau, Shader shader) {
		dim = (float) (Math.pow(2, niveau) * BrickEditor.elementTaille);

		VBOTexture2D vboWorldBox = new VBOTexture2D(shader);
		vboWorldBox.normalActif = false;
		int i = 0;

		vboWorldBox.addNormal(0, 0, 1);
		vboWorldBox.addVertex(0, 0, 0);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(0, 0, 1);
		vboWorldBox.addVertex(0, dim, 0);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(0, 0, 1);
		vboWorldBox.addVertex(dim, dim, 0);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(0, 0, 1);
		vboWorldBox.addVertex(dim, 0, 0);// 3
		vboWorldBox.addCoordTexture2D(1, 0);
		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.addNormal(0, 0, -1);
		vboWorldBox.addVertex(0, 0, dim);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(0, 0, -1);
		vboWorldBox.addVertex(0, dim, dim);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(0, 0, -1);
		vboWorldBox.addVertex(dim, dim, dim);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(0, 0, -1);
		vboWorldBox.addVertex(dim, 0, dim);// 3
		vboWorldBox.addCoordTexture2D(1, 0);
		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.addNormal(1, 0, 0);
		vboWorldBox.addVertex(0, 0, 0);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(1, 0, 0);
		vboWorldBox.addVertex(0, 0, dim);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(1, 0, 0);
		vboWorldBox.addVertex(0, dim, dim);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(1, 0, 0);
		vboWorldBox.addVertex(0, dim, 0);// 3
		vboWorldBox.addCoordTexture2D(1, 0);
		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.addNormal(-1, 0, 0);
		vboWorldBox.addVertex(dim, 0, 0);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(-1, 0, 0);
		vboWorldBox.addVertex(dim, 0, dim);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(-1, 0, 0);
		vboWorldBox.addVertex(dim, dim, dim);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(-1, 0, 0);
		vboWorldBox.addVertex(dim, dim, 0);// 3
		vboWorldBox.addCoordTexture2D(1, 0);
		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.addNormal(0, 1, 0);
		vboWorldBox.addVertex(0, 0, 0);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(0, 1, 0);
		vboWorldBox.addVertex(0, 0, dim);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(0, 1, 0);
		vboWorldBox.addVertex(dim, 0, dim);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(0, 1, 0);
		vboWorldBox.addVertex(dim, 0, 0);// 3
		vboWorldBox.addCoordTexture2D(1, 0);
		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.addNormal(0, -1, 0);
		vboWorldBox.addVertex(0, dim, 0);// 0
		vboWorldBox.addCoordTexture2D(0, 0);

		vboWorldBox.addNormal(0, -1, 0);
		vboWorldBox.addVertex(0, dim, dim);// 1
		vboWorldBox.addCoordTexture2D(0, 1);

		vboWorldBox.addNormal(0, -1, 0);
		vboWorldBox.addVertex(dim, dim, dim);// 2
		vboWorldBox.addCoordTexture2D(1, 1);

		vboWorldBox.addNormal(0, -1, 0);
		vboWorldBox.addVertex(dim, dim, 0);// 3
		vboWorldBox.addCoordTexture2D(1, 0);

		vboWorldBox.addTri(i, i + 1, i + 2);
		vboWorldBox.addTri(i, i + 2, i + 3);
		i += 4;

		vboWorldBox.createVBO();
		Objet3D worldBox = new Objet3D();
		worldBox.brique = vboWorldBox;
		worldBox.translation(-dim / 2, -dim / 2, -dim / 2);
		return worldBox;

	}

	int nbCase;
	public boolean worldBoxVisible;
	public float pixelFactor;

	public boolean dessinerWorldBoxQuery(Camera cam) {
		GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, queryWorldBox);
		Game.shaderWorldBoxVisibilite.use();
		Game.shaderWorldBoxVisibilite.glUniformfARB("dim", 1.0f);

		worldBoxQuery.dessiner(cam);
		GL11.glFlush();
		GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);
		GL15.glGetQueryObject(queryWorldBox, GL15.GL_QUERY_RESULT, samples);
		int pixelCount = samples.get(0);
		pixelFactor = ((float) pixelCount) / Game.screenPixelCount;
		// System.out.println(" pixelCount ="+pixelCount);
		worldBoxVisible = pixelCount >= LimitVisibilitePixelCount;
		return worldBoxVisible;
	}

	public boolean testVisibilite() {
		return this.pixelFactor <= this.pixelFactorPourTest;
	}

	public void dessinerWorldBox(Camera cam) {
		Game.shaderWorldBox.use();
		if (Game.rm == Game.RenderMode.Depth) {
			worldBoxDepth.dessiner(cam);

		} else {
			if (Soleil.debug) {
				return;
			}
			Skybox skyBox = this.skyBox();
			if (skyBox == null) {
				Game.shaderWorldBox.glUniformfARB("size", 1);
				Game.shaderWorldBox.glUniformfARB("color", Game.fogColor.x, Game.fogColor.y, Game.fogColor.z, 0.0f);
			//	Game.shaderWorldBox.glUniformfARB("fogDensity", Game.fogDensity);
			//	Game.shaderWorldBox.glUniformfARB("fogColor", Game.fogColor.x, Game.fogColor.y, Game.fogColor.z);

				worldBox.dessiner(cam);
			} else {

				skyBox.dessiner(cam, dim);
			}
		}

	}

	public void ajouter(OctreeValeur ov) {
		ObjetMobile om = ov.om;
		while (om != null) {
			// if (cam.contains(om.getBox()) != Camera.OUTSIDE_FRUSTUM) {
			this.objetMobiles.add(om);
			if (om.cad != null ) {
				this.cibles.add(om.cad);
				om.cad.calculerPositionEcranObjectif(om, cam);
			}
			// }

			om = om.suivant;
		}

	}

	public void testVisibilite(Octree<OctreeValeur> o) {

		/*
		 * if (o.value.estVide()) { return; }
		 */
		if (!testVisibilite()) {
			return;
		}
		GL11.glPushMatrix();

		Vector3f center = o.box.getCenter();
		float extent = o.box.xExtent;
		GL11.glTranslatef(center.x - extent, center.y - extent, center.z - extent);

		// GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, occquery);
		GL15.glBeginQuery(GL15.GL_SAMPLES_PASSED, o.value.occquery);

		decor.vboVisibilite.bind();
		decor.shaderVisibilite.use();
		decor.shaderVisibilite.glUniformfARB("dim", 1.0f);
		decor.vboVisibilite.dessiner();

		GL15.glEndQuery(GL15.GL_SAMPLES_PASSED);

		GL11.glPopMatrix();
		OpenGLTools.exitOnGLError("testVisibilite");

	}

	public int getVisibilitePixelCount(Octree<OctreeValeur> o) {
		if (!this.testVisibilite()) {
			return LimitVisibilitePixelCount + 1;
		}
		GL15.glGetQueryObject(o.value.occquery, GL15.GL_QUERY_RESULT, samples);
		OpenGLTools.exitOnGLError("getVisibilitePixelCount");
		return samples.get(0);

	}

	public void trierObjetMobile(Camera cam) {
		for (ObjetMobile om : this.objetMobilesVisible) {
			om.calculerDistanceCamera(cam);
		}
		Collections.sort(objetMobilesVisible, new Comparator<ObjetMobile>() {

			@Override
			public int compare(ObjetMobile arg0, ObjetMobile arg1) {
				// TODO Auto-generated method stub
				if (arg0 == arg1) {
					return 0;
				}
				if (arg0 == null) {
					return 1;
				}
				if (arg1 == null) {
					return -1;
				}
				if (arg0.distanceCamera == arg1.distanceCamera) {
					return 0;
				}
				if (arg0.distanceCamera > arg1.distanceCamera) {
					return 1;
				}
				return -1;
			}

		});

		/*
		 * if (idxVisibilite > 0 && this.visibleOctree.get(0).value.distanceCam
		 * >= 99999999999.0f) { throw new Error("ko"); }
		 */

	}

	@Override
	public boolean execute(Octree<OctreeValeur> oct, boolean allIn) {
		// TODO Auto-generated method stub
		if (oct.children == null) {
			this.calculerDistanceCam(oct);
			float dist = oct.value.ed.distanceCam;
			if (dist >= Game.profondeur && Game.rm != Game.RenderMode.Depth) {
				return false;
			}
			if (oct.value.contientElementDecor()) {

				oct.value.ed.obj.brique = oct.value.ed.brique;

				nbDessine++;

			}

			this.visibleOctree.add(oct);

			oct.mark = this.dansCameraMark;

			return false;
		}

		this.ajouter(oct.value);
		return true;
	}

	public int nbObjetMobile;
	public int deltatCulling;

	public void calculerObjetMobileOctree(ObjetMobile om, Octree<OctreeValeur> o) {
		if (!om.getBox().intersects(o.box)) {
			return;
		}
		if (o.children == null) {
			if (o.mark != this.visibleMark && o.mark == this.dansCameraMark && !o.value.contientElementDecor()) {
				o.mark = this.enCoursMark;
				objetMobileOctree.add(o);
			}
			return;
		}
		for (Octree<OctreeValeur> child : o.children) {
			this.calculerObjetMobileOctree(om, child);
		}
	}

	public void calculerObjetMobileOctree() {
		for (ObjetMobile om : this.objetMobiles) {

			if (om.octreeLeaf != null) {
				this.calculerObjetMobileOctree(om, om.octreeLeaf);
			}
		}

	}

	public void render() throws InterruptedException {
		this.objetMobilesTransparent.clear();
		this.visibleMark = new Object();
		this.enCoursMark = new Object();
		this.objetMobilesVisible.clear();
		this.lumieres.clear();
		LumiereBuffer.markSource = new Object();
		objetMobileOctree.clear();
		/*
		 * for (ObjetMobile om : this.objetMobiles) {
		 * 
		 * if (om.octreeLeaf != null) { this.testVisibilitePourObjetMobile(om,
		 * om.octreeLeaf); } }
		 * 
		 * for (Octree<OctreeValeur> oct : this.visibleOctreeCulling) {
		 * 
		 * if (oct.value.contientElementDecor() || oct.mark == this.enCoursMark)
		 * { this.visibleOctree.add(oct); } }
		 */

		totalOctreeDansCamera = this.visibleOctree.size();

		this.trier();

		this.renderWithQuery();
		nbInvisible = 0;
		for (ObjetMobile om : this.objetMobiles) {
			if (om.octreeLeaf != null && this.estVisible(om, om.octreeLeaf)) {
				if (Game.rm == Game.RenderMode.Shadow) {
					this.calculerLumieres(om);
					this.calculerZones(om);
				}
				this.objetMobilesVisible.add(om);
			} else {
				nbInvisible++;
			}
		}
		this.trierObjetMobile(cam);
		this.visibleOctree.clear();

		this.dessinerObjetMobilesAvecTest();

		nbObjetMobile = this.objetMobiles.size();

		this.visibleOctree.clear();
		// if (Game.rm != Game.RenderMode.Depth) {
		this.dessinerWorldBox(cam);
	
		// }
	}

	public void calculerDistanceCam(Octree<OctreeValeur> o) {
		tmp.set(cam.getLocation());
		tmp.subtractLocal(o.box.getCenter());
		o.value.ed.distanceCam = tmp.lengthSquared();

	}

	public void trier() {
	
		Collections.sort(visibleOctree, new Comparator<Octree<OctreeValeur>>() {

			@Override
			public int compare(Octree<OctreeValeur> arg0, Octree<OctreeValeur> arg1) {
				// TODO Auto-generated method stub
				if (arg0 == arg1) {
					return 0;
				}
				if (arg0 == null) {
					return 1;
				}
				if (arg1 == null) {
					return -1;
				}
				if (arg0.value.ed.distanceCam == arg1.value.ed.distanceCam) {
					return 0;
				}
				if (arg0.value.ed.distanceCam > arg1.value.ed.distanceCam) {
					return 1;
				}
				return -1;
			}

		});

		/*
		 * if (idxVisibilite > 0 && this.visibleOctree.get(0).value.distanceCam
		 * >= 99999999999.0f) { throw new Error("ko"); }
		 */

	}

	public void dessinerAvecQuery(List<Octree<OctreeValeur>> lst, int limit) {
		int u = lst.size();
		for (int k = limit; (k < u); k++) {
			Octree<OctreeValeur> octree = lst.get(k);
			if (octree.mark == this.enCoursMark || octree.value.contientElementDecor()) {
				int n = this.getVisibilitePixelCount(octree);
				if (n <= LimitVisibilitePixelCount) {
					totalOctreeCache++;

				} else {
					octree.mark = this.visibleMark;
					// this.dessinerBox(octree.box);
					if (octree.value.contientElementDecor()) {

						octree.value.ed.dessiner(cam);
					}
					this.ajouterSourceLumiereVisible(octree);

				}

			}
		}
	}

	public int testVisibilite(List<Octree<OctreeValeur>> lst, int limit) {
		if (!this.testVisibilite()) {
			return lst.size();
		}
		int u = lst.size();
		for (int k = limit; (k < u); k++) {
			Octree<OctreeValeur> o = lst.get(k);
			if (o.value.contientElementDecor() || o.mark == this.enCoursMark) {
				this.testVisibilite(o);
			}

		}
		return u;
	}

	int totalCube = 0;

	public void ajouterSourceLumiereVisible(Octree<OctreeValeur> o) {
	/*	for (int i = 0; i < o.value.lumiereBuffer.nombreLumiere; i++) {

			Lumiere l = o.value.lumiereBuffer.lumieres[i];
			if (l != null && l.markSource != LumiereBuffer.markSource && cam.contains(l.boundingSphere(true)) != Camera.OUTSIDE_FRUSTUM) {
				this.lumieres.add(l);

			}

		}*/
	}

	public int limitVisibleOctree(List<Octree<OctreeValeur>> lst) {
		int limit = 0;

		int size = lst.size();
		for (int k = 0; (k < size) && ((totalCube < numTriAvantTest)); k++) {
			Octree<OctreeValeur> o = lst.get(k);
			// this.marquer(o);
			o.mark = this.visibleMark;

			if (o.value.contientElementDecor()) {

				o.value.ed.dessiner(cam);

				totalCube += o.value.ed.brique.vbo.numTri;

			}
			this.ajouterSourceLumiereVisible(o);
			limit++;

		}
		return limit;
	}

	public void renderWithQuery() throws InterruptedException {
		totalCube = 0;
		int limit = 0;
		totalOctreeCache = 0;

		totalTrie = 0;
		limit = this.limitVisibleOctree(this.visibleOctree);
		totalTrie += this.visibleOctree.size();

		// this.dessinerObjetMobilesSansTest();

		// System.out.println("totalCube="+totalCube);
		GL11.glFlush();
		if (!this.dessinerWorldBoxQuery(cam) && Game.rm != Game.RenderMode.Depth) {
			this.totalOctreeIngnore = this.visibleOctree.size() - limit;
			totalOctreeCache = 0;

			return;

		}
		// totalTrie+=this.visibleOctree2.size();
		this.calculerObjetMobileOctree();
		this.testVisibilite(this.visibleOctree, limit);
		this.dessinerAvecQuery(visibleOctree, limit);

		GL11.glFlush();

	}

	public int nbInvisible;
	public int nbVisible;

	Object visibleMark;
	Object enCoursMark;
	Object dansCameraMark;

	public void calculerLumieres(ObjetMobile om) {
		if (om.lb == null) {
			om.lb = new LumiereBuffer();
		}
		om.lb.resetBuffer();

		this.calculerLumieres(om, om.octreeLeaf);
	}
	public void calculerZones(ObjetMobile om) {
		if (om.zb == null) {
			om.zb = new ZoneBuffer();
		}
		om.zb.resetBuffer();

		this.calculerZones(om, om.octreeLeaf);
	}
	public void calculerLumieres(ObjetMobile om, Octree<OctreeValeur> o) {
		if (!om.getBox().intersects(o.box)) {
			return;
		}
		if (o.children == null) {
			for (int i = 0; i < o.value.lumiereBuffer.nombre; i++) {
				Lumiere l = o.value.lumiereBuffer.elements[i];
				if (l.boundingSphere(false).intersects(om.getBox())) {
					om.lb.ajouter(l);
				}
			}
			return;
		}
		for (Octree<OctreeValeur> child : o.children) {
			this.calculerLumieres(om, child);
		}

	}
	public void calculerZones(ObjetMobile om, Octree<OctreeValeur> o) {
		if (!om.getBox().intersects(o.box)) {
			return;
		}
		if (o.children == null) {
			for (int i = 0; i < o.value.zoneBuffer.nombre; i++) {
				Zone l = o.value.zoneBuffer.elements[i];
				if (l.boundingSphere(false).intersects(om.getBox())) {
					om.zb.ajouter(l);
				}
			}
			return;
		}
		for (Octree<OctreeValeur> child : o.children) {
			this.calculerZones(om, child);
		}

	}
	public void testVisibilitePourObjetMobile(ObjetMobile om, Octree<OctreeValeur> o) {
		if (!om.getBox().intersects(o.box)) {
			return;
		}
		if (o.children == null) {
			if (o.mark != this.visibleMark) {
				o.mark = this.enCoursMark;
			}
			return;
		}
		for (Octree<OctreeValeur> child : o.children) {
			this.testVisibilitePourObjetMobile(om, child);
		}

	}

	public boolean estVisible(ObjetMobile om, Octree<OctreeValeur> o) {
		if (!om.getBox().intersects(o.box)) {
			return false;
		}
		if (o.children == null) {

			if (o.mark == this.visibleMark) {
				return true;
			}
			return false;
			// throw new Error("pas calculer");

		}
		for (Octree<OctreeValeur> child : o.children) {
			if (this.estVisible(om, child)) {
				return true;
			}
		}
		return false;

	}

	public void dessinerObjetMobilesAvecTest() {

		nbVisible = 0;

		for (int k = 0; k < this.objetMobilesVisible.size(); k++) {
			ObjetMobile om = this.objetMobilesVisible.get(k);
			Octree<OctreeValeur> o = om.octreeLeaf;
			if (om.zoneDetection == null && o != null) {

				if (om.transparence > 0.0f) {
					this.objetMobilesTransparent.add(om);
				} else {
					if (Game.rm != Game.RenderMode.Depth || om.modifierOmbre) {
						om.dessiner(cam);
						nbVisible++;
					}

				}

			}

		}

	}

}
