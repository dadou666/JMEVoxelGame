package dadou;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;

public class ElementDecor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -733844082943538285L;

	public int x, y, z;

	transient public Objet3D obj;

	transient boolean estModifie = false;

	public int nbBrique = 0;
	public byte[] bytes;
	transient Octree<OctreeValeur> octree;
	transient VoxelTexture3D tex;

	public float distanceCam;

	transient public BriqueAvecTexture3D brique;

	public void delete() {
		// brique.delete();
		// tex.delete();
	}

	public ElementDecor() {

	}

	/*
	 * private void readObject(java.io.ObjectInputStream stream) throws
	 * IOException, ClassNotFoundException {
	 * 
	 * 
	 * }
	 */
	public int byteToInt(byte b) {
		if (b >= 0) {
			return b;
		}
		int i = b;
		return i + 256;

	}

	public Color lireCouleur(int dim, int x, int y, int z) throws CouleurErreur {
		if (x < 0 || x >= dim) {
			throw new CouleurErreur(" x = " + x + " hors limite " + dim);
		}
		if (y < 0 || y >= dim) {
			throw new CouleurErreur(" x = " + y + " hors limite " + dim);
		}
		if (z < 0 || z >= dim) {
			throw new CouleurErreur(" x = " + z + " hors limite " + dim);
		}
		int idx = (x + (y + z * dim) * dim) * 4;
		if (idx >= bytes.length) {
			throw new CouleurErreur(" x,y,z =" + x + " " + y + " " + z
					+ " dim=" + dim + "," + dim + "," + dim);
		}
		int red = byteToInt(bytes[idx]);
		int green = byteToInt(bytes[idx + 1]);
		int blue = byteToInt(bytes[idx + 2]);
		int alpha = byteToInt(bytes[idx + 3]);
		try {
			return new Color(red, green, blue, alpha);
		} catch (java.lang.IllegalArgumentException e) {
			throw new Error(" Couleur (red=" + red + " green=" + green
					+ " blue=" + blue);
		}
	}

	public void ecrireCouleur(int dim, int x, int y, int z, Color color)
			throws CouleurErreur {
		if (bytes == null) {
			bytes = new byte[dim * dim * dim * 4];
		}
		if (x < 0 || x >= dim) {
			throw new CouleurErreur(" x = " + x + " hors limite " + dim);
		}
		if (y < 0 || y >= dim) {
			throw new CouleurErreur(" x = " + y + " hors limite " + dim);
		}
		if (z < 0 || z >= dim) {
			throw new CouleurErreur(" x = " + z + " hors limite " + dim);
		}
		int idx = (x + (y + z * dim) * dim) * 4;
		if (idx >= bytes.length) {
			throw new CouleurErreur(" x,y,z =" + x + " " + y + " " + z
					+ " dim=" + dim + "," + dim + "," + dim);
		}

		byte r = (byte) color.getRed();
		byte g = (byte) color.getGreen();
		byte b = (byte) color.getBlue();
		byte a = (byte) color.getAlpha();
		try {

			bytes[idx] = r;
			bytes[idx + 1] = g;
			bytes[idx + 2] = b;
			bytes[idx + 3] = a;
			if (tex != null) {
				tex.updateBlock(x, y, z, color);
			}
		} catch (java.lang.IllegalArgumentException e) {
			throw new Error(" Couleur (red=" + r + " green=" + g + " blue=" + b);
		}
	}

	static public boolean estVide(Color a) {
		if (a == null) {
			return true;
		}
		return cmp(a, Color.black);
	}

	static public boolean cmp(Color a, Color b) {
		if (a.getBlue() != b.getBlue()) {
			return false;
		}
		if (a.getRed() != b.getRed()) {
			return false;
		}
		if (a.getGreen() != b.getGreen()) {
			return false;
		}
		return true;
	}

	public void calculerNbBrique(DecorDeBrique decor) throws CouleurErreur {
		int u = decor.DecorDeBriqueData.decorInfo.elementTaille;
		int vx = x * u;
		int vy = y * u;
		int vz = z * u;
		nbBrique = 0;
		for (int x = 0; x < u; x++) {
			for (int y = 0; y < u; y++) {
				for (int z = 0; z < u; z++) {
					if (!estVide(decor.lireCouleur(vx + x, vy + y, vz + z))) {
						nbBrique++;

					}

				}
			}
		}
		if (nbBrique == 0) {
			this.bytes = null;
		}

	}

	public void creerVoxelTexture3D(DecorDeBrique decor) throws CouleurErreur {
		int u = decor.DecorDeBriqueData.decorInfo.elementTaille + 2;
		int elementTaille = decor.DecorDeBriqueData.decorInfo.elementTaille;

		this.tex = new VoxelTexture3D(u);
		int vx = x * elementTaille;
		int vy = y * elementTaille;
		int vz = z * elementTaille;
		int dim = decor.DecorDeBriqueData.decorInfo.nbCube;

		for (int px = 0; px < u; px++) {
			for (int py = 0; py < u; py++) {
				for (int pz = 0; pz < u; pz++) {
					int hx = vx + px - 1;
					int hy = vy + py - 1;
					int hz = vz + pz - 1;
					Color color;
					if (hx < 0 || hx >= dim || hy < 0 || hy >= dim || hz < 0
							|| hz >= dim) {
						color = Color.BLACK;
					} else {
						color = decor.DecorDeBriqueData.lireCouleur(hx, hy, hz);
					}
					tex.SetBlock(px, py, pz, color);

				}
			}
		}
		tex.createTexture3D();

		VBOMinimunPourBriqueAvecTexture3D vbo = null;

		vbo = new VBOMinimunPourBriqueAvecTexture3D(null, tex, decor.lp, 1, 1,
				1, 1.0f, elementTaille, elementTaille, elementTaille);

		brique = vbo.creerBriqueAvecTexture3D(tex);
		;
		obj.brique = brique;
		obj.estElementDecor = true;

	}

	public void construire(DecorDeBrique decor, Octree<OctreeValeur> elt)
			throws CouleurErreur {
		Vector3f center = elt.box.getCenter();

		float extent = elt.box.xExtent;

		obj = new Objet3D();
		obj.translation(center.x - extent, center.y - extent, center.z - extent);
		calculerNbBrique(decor);
		if (nbBrique > 0) {
			if (decor.g.game != null) {
				if (bytes != null) {
					this.creerVoxelTexture3D(decor);

				}
				brique.vs = decor.lp;

				obj.brique = brique;
			}
		} else {
			brique = null;
			bytes = null;
		}

	}

	public void reconstruire(DecorDeBrique decor) throws CouleurErreur {
		estModifie = false;
		int u = decor.DecorDeBriqueData.decorInfo.elementTaille;

		if (brique != null) {
			delete();

		}
		brique = null;
		if (this.nbBrique <= 0) {
			bytes = null;
			return;
		}
		if (bytes != null) {
			this.creerVoxelTexture3D(decor);
		}

	}

	public void dessiner(Camera cam) {

		LumiereBuffer.courant = this.octree.value.lumiereBuffer;
		ZoneBuffer.courant = this.octree.value.zoneBuffer;
		obj.dessiner(cam);
		LumiereBuffer.courant = null;
		ZoneBuffer.courant = null;
	}

}
