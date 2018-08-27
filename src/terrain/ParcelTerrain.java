package terrain;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE0_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;

import dadou.DecorDeBrique;
import dadou.Game;
import dadou.TrianglePourObjet;
import dadou.VBOTexture2D;
import dadou.tools.BrickEditor;

public class ParcelTerrain implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3729184696551526426L;
	transient public VBOTexture2D vbo;

	public int x;
	public int y;
	public int z;
	public float m;
	public float hMax;
	public int elementTaille;
	public int dim;
	public int grille[][];

	public ParcelTerrain(float m, float hMax, int x, int y, int z, int elementTaille) {
		this.m = m;
		this.hMax = hMax;
		this.x = x;
		this.y = y;
		this.z = z;
		this.elementTaille = elementTaille;

	}

	public void initGrille(Terrain terrain) {
		dim =terrain.dim;
		grille = new int[elementTaille + 1][elementTaille + 1];
		for (int ex = 0; ex <= elementTaille; ex++) {
			for (int ez = 0; ez <= elementTaille; ez++) {

				int px = ex + elementTaille * x;
				int pz = ez + elementTaille * z;
				if (px >= dim || pz >= dim) {

					px = Math.min(px, dim - 1);
					pz = Math.min(pz, dim - 1);

				}
				grille[ex][ez] = terrain.grille[px][pz];
			}

		}
	}


	public void initVBO(BrickEditor editor) {
	

		vbo = new VBOTexture2D(Game.shaderTerrain);
		if (editor.terrain == null) {
			editor.terrain = new Terrain(dim,0);
		}
		for (int ex = 0; ex < elementTaille; ex++) {
			for (int ez = 0; ez < elementTaille; ez++) {

				int px = ex + elementTaille * x;
				int pz = ez + elementTaille * z;
				editor.terrain.grille[px][pz] = grille[ex][ez];
			}

		}
		List<TrianglePourObjet> triangles = new ArrayList<>();
		this.ajouterTriangles(triangles);
		for (TrianglePourObjet to : triangles) {
			int a = vbo.addVertex(to.a);
			vbo.addNormal(to.normal.x, to.normal.y, to.normal.z);
			int b = vbo.addVertex(to.b);
			vbo.addNormal(to.normal.x, to.normal.y, to.normal.z);
			int c = vbo.addVertex(to.c);
			vbo.addNormal(to.normal.x, to.normal.y, to.normal.z);
			vbo.addTri(a, b, c);

		}
		if (triangles.isEmpty()) {
			throw new Error("empty " + x + "-" + y + "-" + z);
		} else {
			// System.out.println(" ok");
		}
		vbo.createVBO();
		triangles = null;

	}

	public void dessiner() {

		vbo.bind();
		Game.shaderTerrain.use();
		Game.shaderTerrain.glUniformfARB("color", Game.colorTerrain.x, Game.colorTerrain.x, Game.colorTerrain.x);
		Game.shaderTerrain.glUniformfARB("hMax", hMax);
		Game.shaderTerrain.glUniformfARB("m", m);

		Game.shaderTerrain.glUniformfARB("a", Game.minColor);

		vbo.dessiner(Game.shaderTerrain);
		vbo.unBindVAO();

	}

	public void ajouterTriangles(Terrain terrain, List<TrianglePourObjet> triangles) {
		for (int ex = 0; ex < elementTaille; ex++) {
			for (int ez = 0; ez < elementTaille; ez++) {

				int px = ex + elementTaille * x;
				int pz = ez + elementTaille * z;
				if (px + 1 < terrain.dim && pz + 1 < terrain.dim) {
					int h00 = terrain.grille[px][pz];
					int h10 = terrain.grille[px + 1][pz];
					int h01 = terrain.grille[px][pz + 1];
					int h11 = terrain.grille[px + 1][pz + 1];
					int h00z = h00 / elementTaille;
					int h10z = h10 / elementTaille;
					int h11z = h11 / elementTaille;
					int h01z = h01 / elementTaille;
					if (px / elementTaille != x || pz / elementTaille != z) {
						throw new Error("erreur calcul");
					}

					int hMin = Math.min(Math.min(h00z, h11z), Math.min(h01z, h10z));
					if (this.y == hMin) {

						float fx0 = px;
						fx0 = (fx0 - m);
						float fx1 = px + 1;
						fx1 = (fx1 - m);
						float fz0 = pz;
						fz0 = (fz0 - m);
						float fz1 = pz + 1;
						fz1 = (fz1 - m);

						Vector3f p00 = new Vector3f(fx0, h00, fz0);
						Vector3f p01 = new Vector3f(fx0, h01, fz1);
						Vector3f p10 = new Vector3f(fx1, h10, fz0);
						Vector3f p11 = new Vector3f(fx1, h11, fz1);
						triangles.add(new TrianglePourObjet(p00, p10, p01));

						triangles.add(new TrianglePourObjet(p11, p01, p10));

					}
				}
			}

		}
	}

	public void ajouterTriangles(List<TrianglePourObjet> triangles) {
		for (int ex = 0; ex < elementTaille; ex++) {
			for (int ez = 0; ez < elementTaille; ez++) {

				int px = ex + elementTaille * x;
				int pz = ez + elementTaille * z;

				int h00 = grille[ex][ez];
				int h10 = grille[ex + 1][ez];
				int h01 = grille[ex][ez + 1];
				int h11 = grille[ex + 1][ez + 1];
				int h00z = h00 / elementTaille;
				int h10z = h10 / elementTaille;
				int h11z = h11 / elementTaille;
				int h01z = h01 / elementTaille;
				if (px / elementTaille != x || pz / elementTaille != z) {
					throw new Error("erreur calcul");
				}

				int hMin = Math.min(Math.min(h00z, h11z), Math.min(h01z, h10z));
				if (this.y == hMin) {

					float fx0 = px;
					fx0 = (fx0 - m);
					float fx1 = px + 1;
					fx1 = (fx1 - m);
					float fz0 = pz;
					fz0 = (fz0 - m);
					float fz1 = pz + 1;
					fz1 = (fz1 - m);

					Vector3f p00 = new Vector3f(fx0, h00, fz0);
					Vector3f p01 = new Vector3f(fx0, h01, fz1);
					Vector3f p10 = new Vector3f(fx1, h10, fz0);
					Vector3f p11 = new Vector3f(fx1, h11, fz1);
					triangles.add(new TrianglePourObjet(p00, p10, p01));

					triangles.add(new TrianglePourObjet(p11, p01, p10));

				}
			}

		}
	}

}
