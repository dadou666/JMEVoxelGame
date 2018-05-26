package dadou;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;

import terrain.Terrain;

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

	public ParcelTerrain(float m, float hMax, int x, int y, int z, int elementTaille) {
		this.m = m;
		this.hMax = hMax;
		this.x = x;
		this.y = y;
		this.z = z;
		this.elementTaille = elementTaille;

	}
	public void initGestionCollision(DecorDeBrique decor) {
		for (int ex = 0; ex < elementTaille; ex++) {
			for (int ez = 0; ez < elementTaille; ez++) {
				int px = ex + elementTaille * x;
				int pz = ez + elementTaille * z;
				int h=decor.DecorDeBriqueData.terrain.grille[px][pz];
				decor.gestionCollision.ajouterBrique(px, h, pz);
			}
		}

		
	}

	public void initVBO(Terrain terrain) {
		vbo = new VBOTexture2D(Game.shaderTerrain);
		List<TrianglePourObjet> triangles = new ArrayList<>();
		this.ajouterTriangles(terrain, triangles);
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
		//	System.out.println(" ok");
		}
		vbo.createVBO();
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

}
