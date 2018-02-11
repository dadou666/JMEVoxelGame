package dadou;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;

import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public class ModelClasse implements Serializable {
	/**
	 * 
	 */

	private static final long serialVersionUID = 3573391423481810292L;

	public Color[][][] copie;
	public ModelEvent evenement;
	public boolean estModelPlanche = false;
	public boolean modifierOmbre = false;
	public float echelle = 1.0f;
	public String nomModele;
	public String nomHabillage;
	public float transparence = 0.0f;
	private transient Map<Integer,DecompositionModelClasse> decomposition;
	
	public DecompositionModelClasse decompositionCube(BrickEditor be, int taille) throws CouleurErreur, Exception {
		DecompositionModelClasse r=null;
		if (decomposition == null) {
			decomposition = new HashMap<>();
			r =  new DecompositionModelClasse(this, be, taille);
			decomposition.put(taille, r);
			
		} else {
			r = decomposition.get(taille);
		if (r == null) {
			r = new DecompositionModelClasse(this, be, taille);
			decomposition.put(taille,r);

		}
		}
		return r;
	}

	public void execute(ModelClasseAction action) {
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					action.execute(x, y, z, copie[x][y][z]);
				}
			}

		}
	}

	public boolean estElementDecor() {
		return false;
	}

	public String nomModele() {
		if (nomModele != null) {
			return nomModele;
		}
		return nom;
	}

	public String toString() {
		String r = this.nomSprite3D();
		if (r == null) {
			return "*";
		}
		return r;
	}

	public String nomSprite3D() {

		int idx = nom.indexOf("#");
		if (idx == -1) {
			return null;
		}
		return nom.substring(idx + 1);

	}

	transient public VBOBriqueTexture3D vbo;

	transient public VoxelTexture3D tex;
	transient public BriqueAvecTexture3D brique;

	public String nom;

	public int dx;
	public int dy;
	public int dz;
	public Vector3f size;
	public float dim = 1.0f;
	public boolean estVide;

	public int nombreDeCubePourFaceX(int x) {
		int n = 0;
		for (int y = 0; y < dy; y++) {
			for (int z = 0; z < dz; z++) {
				Color color = copie[x][y][z];
				if (!ElementDecor.estVide(color)) {
					n++;
				}

			}

		}
		return n;
	}

	public int nombreDeCubePourFaceY(int y) {
		int n = 0;
		for (int x = 0; x < dx; x++) {
			for (int z = 0; z < dz; z++) {
				Color color = copie[x][y][z];
				if (!ElementDecor.estVide(color)) {
					n++;
				}

			}

		}
		return n;
	}

	public int nombreDeCubePourFaceZ(int z) {
		int n = 0;
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				Color color = copie[x][y][z];
				if (!ElementDecor.estVide(color)) {
					n++;
				}

			}

		}
		return n;
	}

	public void supprimerEspaces() {
		int minX = 0, maxX = dx - 1;
		int minY = 0, maxY = dy - 1;
		int minZ = 0, maxZ = dz - 1;
		while (this.nombreDeCubePourFaceX(maxX) == 0) {
			maxX--;

		}
		while (this.nombreDeCubePourFaceY(maxY) == 0) {
			maxY--;

		}
		while (this.nombreDeCubePourFaceZ(maxZ) == 0) {
			maxZ--;

		}

		while (this.nombreDeCubePourFaceX(minX) == 0) {
			minX++;

		}
		while (this.nombreDeCubePourFaceY(minY) == 0) {
			minY++;

		}
		while (this.nombreDeCubePourFaceZ(minZ) == 0) {
			minZ++;

		}
		int newDx = maxX - minX + 1;
		int newDy = maxY - minY + 1;
		int newDz = maxZ - minZ + 1;
		Color newColors[][][] = new Color[newDx][newDy][newDz];
		for (int x = 0; x < newDx; x++) {
			for (int y = 0; y < newDy; y++) {
				for (int z = 0; z < newDz; z++) {
					newColors[x][y][z] = copie[x + minX][y + minY][z + minZ];
				}
			}
		}
		this.copie = newColors;

		this.dx = newDx;
		this.dy = newDy;
		this.dz = newDz;

	}

	public void initEstVide() {
		if (copie == null) {
			estVide = true;
		} else {
			for (int x = 0; x < dx; x++) {
				for (int y = 0; y < dy; y++) {
					for (int z = 0; z < dz; z++) {
						if (!ElementDecor.estVide(copie[x][y][z])) {
							estVide = false;
							return;

						}
					}

				}
			}
			estVide = true;
			copie = null;
		}
	}

	public void setColor(int x, int y, int z, Color color) {
		if (x < 0) {
			return;
		}
		if (y < 0) {
			return;
		}
		if (z < 0) {
			return;
		}
		if (x >= dx) {
			return;
		}
		if (y >= dy) {
			return;
		}
		if (z >= dz) {
			return;
		}
		this.copie[x][y][z] = color;

	}

	public ModelClasse cloner() {

		ModelClasse r = new ModelClasse();
		r.copie = new Color[dx][dy][dz];
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					r.copie[x][y][z] = copie[x][y][z];
				}

			}
		}
		r.dx = dx;
		r.dy = dy;
		r.dz = dz;
		size = new Vector3f(dx, dy, dz);
		r.nomHabillage = nomHabillage;
		return r;

	}

	public void supprimer(int r) {
		for (int x = -r; x < r; x++) {
			for (int y = -r; y < r; y++) {
				for (int z = -r; z < r; z++) {
					if (x * x + y * y + z * z >= r * r) {
						this.setColor(x + dx / 2, y + dy / 2, z + dz / 2, Color.BLACK);

					}

				}
			}

		}

	}

	public void initBuffer(Game g, Habillage newParam) throws CouleurErreur, Exception {
		this.initBuffer(g, false, newParam);

	}

	public void initBufferFromTexteX(String nomHabillage, Habillage hab, String texte, String kubeName)
			throws CouleurErreur, Exception {
		// Font font = Font.decode("arial-22");

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D gr = image.createGraphics();
		// gr.setFont(font);
		FontMetrics fm = gr.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(texte, gr);
		image = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		gr = image.createGraphics();
		gr.setColor(Color.BLACK);
		gr.clearRect(0, 0, image.getWidth(), image.getHeight());
		gr.setColor(Color.WHITE);
		gr.drawString(texte, 0, image.getHeight() - 1);
		this.init(1, image.getHeight(), image.getWidth());
		Color color = hab.valeurPourHabillage(kubeName);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (image.getRGB(x, y) == Color.WHITE.getRGB()) {
					this.setColor(0, image.getHeight() - y - 1, image.getWidth() - x - 1, color);

				}
			}
		}
		this.nomHabillage = nomHabillage;

	}

	public void initBufferFromTexteY(String nomHabillage, Habillage hab, String texte, String kubeName)
			throws CouleurErreur, Exception {
		// Font font = Font.decode("arial-22");

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D gr = image.createGraphics();
		// gr.setFont(font);
		FontMetrics fm = gr.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(texte, gr);
		image = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		gr = image.createGraphics();
		gr.setColor(Color.BLACK);
		gr.clearRect(0, 0, image.getWidth(), image.getHeight());
		gr.setColor(Color.WHITE);
		gr.drawString(texte, 0, image.getHeight() - 1);
		this.init(image.getWidth(), 1, image.getHeight());
		Color color = hab.valeurPourHabillage(kubeName);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (image.getRGB(x, y) == Color.WHITE.getRGB()) {
					this.setColor(x, 0, y, color);

				}
			}
		}
		this.nomHabillage = nomHabillage;

	}

	public void initBufferFromTexteZ(String nomHabillage, Habillage hab, String texte, String kubeName)
			throws CouleurErreur, Exception {
		// Font font = Font.decode("arial-22");

		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D gr = image.createGraphics();
		// gr.setFont(font);
		FontMetrics fm = gr.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(texte, gr);
		image = new BufferedImage((int) rect.getWidth(), (int) rect.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		gr = image.createGraphics();
		gr.setColor(Color.BLACK);
		gr.clearRect(0, 0, image.getWidth(), image.getHeight());
		gr.setColor(Color.WHITE);
		gr.drawString(texte, 0, image.getHeight() - 1);
		this.init(image.getHeight(), image.getWidth(), 1);
		Color color = hab.valeurPourHabillage(kubeName);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (image.getRGB(x, y) == Color.WHITE.getRGB()) {
					this.setColor(y, x, 0, color);

				}
			}
		}
		this.nomHabillage = nomHabillage;

	}

	public void initBuffer(Game g, boolean force, Habillage newParam) throws CouleurErreur, Exception {
		dim = 1.0f;
		if (!force || copie == null) {
			this.initEstVide();
			if (estVide) {
				size = new Vector3f(dx, dy, dz);
				size.multLocal(dim);
				return;
			}
		}
		byte bytes[] = new byte[4 * dx * dy * dz];

		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					this.SetBlock(bytes, x, y, z, copie[x][y][z]);

				}
			}

		}

		tex = new VoxelTexture3D(bytes, dx, dy, dz);

		tex.createTexture3D();

		if (!estModelPlanche) {
			vbo = new VBOMinimunPourBriqueAvecTexture3D(this, tex, g.createVoxelShaderParam(newParam), 0, 0, 0, dim, dx,
					dy, dz);

		} else {
			vbo = new VBOMinimunPourPlancheAvecTexture3D(this, tex, g.createVoxelShaderParam(newParam), 0, 0, 0, dim,
					dx, dy, dz);

		}
		size = new Vector3f(dx, dy, dz);
		size.multLocal(dim);
		brique = vbo.creerBriqueAvecTexture3D(tex);

	}

	VoxelShaderParam getVoxelShaderParam(Game g) {
		if (g == null) {
			return null;
		}
		return g.createVoxelShaderParam();
	}

	public void SetBlock(byte[] bytes, int x, int y, int z, Color color) {
		if (color == null) {
			color =Color.BLACK;
		}
		int idx = (x + (y + z * dy) * dx) * 4;
		byte r = (byte) color.getRed();
		byte g = (byte) color.getGreen();
		byte b = (byte) color.getBlue();
		byte a = (byte) color.getAlpha();
		bytes[idx] = r;
		bytes[idx + 1] = g;
		bytes[idx + 2] = b;
		bytes[idx + 3] = a;

	}

	public void init(int dx, int dy, int dz) {
		if (copie

		== null) {
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
			copie = new Color[dx][dy][dz];
			for (int x = 0; x < dx; x++) {
				for (int y = 0; y < dy; y++) {
					for (int z = 0; z < dz; z++) {

						copie[x][y][z] = Color.BLACK;

					}

				}
			}
			size = new Vector3f(dx, dy, dz);
		}
	}
	public void init(int dx, int dy, int dz,Color init) {
		if (copie

		== null) {
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
			copie = new Color[dx][dy][dz];
			for (int x = 0; x < dx; x++) {
				for (int y = 0; y < dy; y++) {
					for (int z = 0; z < dz; z++) {

						copie[x][y][z] =init;

					}

				}
			}
			size = new Vector3f(dx, dy, dz);
		}
	}

	public boolean coller(int px, int py, int pz, DecorDeBrique decor, Color annulerColors[][][]) throws CouleurErreur {
		if (copie == null) {
			return true;
		}

		/*
		 * for (int x = 0; x < dx; x++) { for (int y = 0; y < dy; y++) { for
		 * (int z = 0; z < dz; z++) { Color color = decor.lireCouleur(x + px, y
		 * + py, z + pz); if (!ElementDecor.estVide(color)) {
		 * 
		 * return false;
		 * 
		 * } }
		 * 
		 * } }
		 */
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					Color color = copie[x][y][z];
					if (annulerColors != null) {
						annulerColors[x][y][z] = decor.lireCouleur(x + px, y + py, z + pz);
					}
					if (!ElementDecor.estVide(color)) {

						decor.addBrique(x + px, y + py, z + pz);
						decor.ecrireCouleur(x + px, y + py, z + pz, color);

					}
				}

			}
		}
		return true;
	}

	public boolean contientPoint(float rayon, Point centre, Point p) {

		float h = 3.0f * rayon;
		float u = (float) Math.sqrt(h * h + rayon * rayon);
		float angle = (float) (2 * Math.acos(rayon / u));

		float dx = centre.x - rayon - p.x;
		float dy = centre.y - p.y;
		if (dx * dx + dy * dy <= rayon * rayon) {
			return true;
		}

		dx = centre.x + rayon - p.x;
		dy = centre.y - p.y;
		if (dx * dx + dy * dy <= rayon * rayon) {
			return true;
		}

		float nx = p.x - centre.x;
		float ny = p.y - (centre.y + h);
		float n = (float) Math.sqrt(nx * nx + ny * ny);
		float w = (float) Math.acos((ny * h) / (n * h));
		if (n >= h) {
			return false;
		}
		System.out.println(" angle =" + w);
		return !(w >= (-angle) && w <= (angle));

	}

	public void initCoeur(String nomHabillage, Habillage hab, String kubeName, int r, int profondeur) {

		this.init(1 + 2 * profondeur, 4 * r, 4 * r - 1);
		Color color = hab.valeurPourHabillage(kubeName);
		Point centre = new Point(2 * r, r);

		for (int x = 0; x < 4 * r - 1; x++) {
			for (int y = 0; y < 4 * r; y++) {
				if (this.contientPoint(r, centre, new Point(x + 1, y))) {
					this.copie[profondeur][4 * r - y - 1][x] = color;

				} else {
					this.copie[profondeur][4 * r - y - 1][x] = Color.BLACK;
				}

			}

		}
	
		for (int d = 1; d < profondeur; d++) {
			r--;
			centre = new Point(2 * r, r);
		

			for (int x = 0; x < 4 * r - 1; x++) {
				for (int y = 0; y < 4 * r; y++) {
					if (this.contientPoint(r, centre, new Point(x + 1, y))) {
						this.copie[profondeur+d][4 * r - y - 1 + d*2][x + 2*d] = color;
						this.copie[profondeur-d][4 * r - y - 1 + d*2][x + 2*d] = color;

					} else {
						this.copie[profondeur+d][4 * r - y - 1 + d*2][x + 2*d] = Color.BLACK;
						this.copie[profondeur-d][4 * r - y - 1 + d*2][x + 2*d] = Color.BLACK;
					}

				}

			}

		}

	}

}
