package dadou;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import dadou.NomTexture.PlusDeTexture;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;
import dadou.tools.SerializeTool;
import dadou.tools.texture.ChoixKube;
import dadou.tools.texture.ChoixTexture;

public class Habillage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	public Map<String, Color> valeurs = new HashMap<>();
	public Color couleurs[][][];
	public int dim;
	public String nomHabillage;
	public String[] noms;
	public transient ChoixKube choixKube;
	public transient ChoixTexture choixTexture;
	public int nbTexture = 256;
 public static Habillage charger(String nom) throws FileNotFoundException, ClassNotFoundException, IOException {
	return	(Habillage) SerializeTool.load( new File(BrickEditor.cheminRessources, nom));
 }
	public void reset(Set<String> nomTextures) {
		String nomsNv[] = null;
		if (nomTextures.size() + 1 > this.nbTexture || noms == null) {
			this.nbTexture = nomTextures.size() + 1;
			nomsNv = new String[nbTexture];
		}
	
		voxelTexture3D = new VoxelTexture3D(dim, dim, nbTexture);

		if (noms != null) {
			for (int i = 0; i < noms.length; i++) {
				String nom = noms[i];
				if (!nomTextures.contains(nom)) {
					noms[i] = null;
				} else {
					if (nomsNv != null) {
						nomsNv[i] = nom;
					}
				}
			}

		}
		if (nomsNv != null) {
			noms = nomsNv;
		}
		valeurs = new HashMap<>();
	}

	public Habillage(int dim) {
		this.dim = dim;

		voxelTexture3D = new VoxelTexture3D(dim, dim, nbTexture);

	}

	public Habillage(int dim, int nbTexture) {
		this.nbTexture = nbTexture;
		this.dim = dim;
	
		voxelTexture3D = new VoxelTexture3D(dim, dim, nbTexture);

	}
	public Color GetBlock(int x,int y,int z) {
		   if (voxelTexture3D != null) {
			   try {
				return voxelTexture3D.getBlockColor(x, y, z);
			} catch (CouleurErreur e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
			return couleurs[x][y][z] ;
		
	}
	public void SetBlock(int x, int y, int z, Color color) {
	   if (voxelTexture3D != null) {
		   try {
			voxelTexture3D.SetBlock(x, y, z, color);
			return;
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
		couleurs[x][y][z] = color;
	}

	public NomTexture creerSiExistPas(String nm) throws PlusDeTexture {
		NomTexture nt = donnerNomTextures().get(nm);
		if (nt == null) {
			nt = creerNomTexture(nm);
		}
		return nt;
	}

	public ChoixKube donnerChoixKube(Game g, FBO fbo, boolean estModelPlancge)
			throws Exception {
		if (choixKube == null) {
			choixKube = new ChoixKube(this, g, fbo, estModelPlancge);
		}
		return choixKube;

	}

	public ChoixTexture donnerChoixTexture(Game g, boolean afficherNom) {
		if (choixTexture == null) {
			choixTexture = new ChoixTexture(this, g, afficherNom);
		}
		return choixTexture;

	}

	public List<NomTexture> noms() {
		ArrayList<NomTexture> r = new ArrayList<>();
		if (noms == null) {
			return r;
		}
		for (int i = 1; i < nbTexture; i++) {
			if (noms[i] != null) {
				NomTexture nomTexture = new NomTexture();
				nomTexture.idx = i;
				nomTexture.nom = noms[i];
				r.add(nomTexture);
			}

		}
		return r;
	}

	public String nom(int idx) {
		if (noms == null) {
			return null;
		}
		return noms[idx];
	}

	public Map<String, NomTexture> donnerNomTextures() {
		Map<String, NomTexture> r = new HashMap<>();
		if (noms == null) {
			return r;
		}
		for (int i = 0; i < nbTexture; i++) {
			String n = noms[i];

			NomTexture nt = new NomTexture();
			nt.idx = i;
			nt.nom = n;
			r.put(n, nt);
		}
		return r;
	}

	public NomTexture creerNomTexture(String nom) throws PlusDeTexture {
		NomTexture r = null;
		if (noms == null) {
			r = new NomTexture();
			r.nom = nom;
			r.idx = 1;
			noms = new String[nbTexture];
			noms[1] = nom;
			return r;
		}
		for (int i = 1; i < nbTexture; i++) {

			String nm = noms[i];
			if (nm != null) {
				if (nm.equals(nom))
					return null;
			} else {
				if (r == null) {
					r = new NomTexture();
					r.nom = nom;
					noms[i] = nom;
					r.idx = i;
				}
			}
		}
		if (r == null) {
			throw new NomTexture.PlusDeTexture(nom);
		}
		return r;

	}

	 private VoxelTexture3D voxelTexture3D;

	public VoxelTexture3D creerVoxelTexture3D(boolean forcer) {
		if (voxelTexture3D != null) {
			
			couleurs = null;
			if (this.voxelTexture3D.texId != null) {

				return this.voxelTexture3D;
			}
		} else {
			if (nbTexture == 0) {
				nbTexture = 256;
			}
			voxelTexture3D = new VoxelTexture3D(dim, dim, nbTexture);
			for (int x = 0; x < dim; x++) {
				for (int y = 0; y < dim; y++) {
					for (int i = 0; i < nbTexture; i++) {
						Color c = this.couleurs[x][y][i];
						if (c != null) {
							try {
								voxelTexture3D.SetBlock(x, y, i, c);
							} catch (CouleurErreur e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			}
		}
		if (!forcer) {
			voxelTexture3D.createTexture2DArray(this);
		} else {
			voxelTexture3D.createTexture3D();

		}
		return voxelTexture3D;
	}

	public VoxelTexture3D creerVoxelTexture3D() {
		return this.creerVoxelTexture3D(false);
	}

	static public int byteToInt(byte b) {
		if (b >= 0) {
			return b;
		}
		int i = b;
		return i + 256;

	}

	public Color valeurPourHabillage(String nomValeur) {
		if (nomValeur == null) {
			return Color.BLACK;
		}
		return valeurs.get(nomValeur);
	}

}
