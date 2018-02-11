package dadou.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import dadou.ConfigLine;
import dadou.DecorDeBriqueData;
import dadou.Habillage;
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelClasseDecor;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.tools.BrickEditor;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class textureImport extends ConfigLine {

	public String nom;
	public String nomObjet;

	public int largeur;
	public int hauteur;


	public String couleurBord = "BLUE";

	public static Map<String, textureImport> textureImports() throws InstantiationException, IllegalAccessException {
		if (new File(BrickEditor.cheminRessources + "/textures.txt").exists()) {

			return ConfigLine.lire(textureImport.class,
					OutilFichier.lireChaineDansFichier(BrickEditor.cheminRessources + "/photos.txt"));
		}
		return null;
	}

	public static void chargerTextures(Habillage hab, String nomHabillage, String fichier)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, NoSuchFieldException,
			SecurityException, PlusDeTexture, IOException, ClassNotFoundException {
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool
				.load(BrickEditor.cheminRessources + "/base.bin");
		Map<String, textureImport> map = ConfigLine.lire(textureImport.class,
				OutilFichier.lireChaineDansFichier(BrickEditor.cheminRessources + "/" + fichier));
		HashSet<String> set = new HashSet<>();
	
		for(textureImport ti:map.values()) {
			ti.ajouterListeNomTexture(set);
		}
		
		
		hab.reset(set);
		for (Map.Entry<String, textureImport> e : map.entrySet()) {
			e.getValue().creer(decorDeBriqueData, hab, nomHabillage);

		}
		SerializeTool.save(decorDeBriqueData, BrickEditor.cheminRessources + "/base.bin");

	}
	public void ajouterListeNomTexture(Set<String> ls) {
		ls.add("cb_"+couleurBord);
		if (largeur == 1 && hauteur == 1) {
			ls.add(nomObjet);
			return;
		}
		for (int ix = 0; ix <largeur; ix++) {

			for (int iy = 0; iy < hauteur; iy++) {
				
					ls.add(nomObjet + "_" + ix + "_" + iy);
			}}
				
		
	}

	@Override
	public void initialiser(String nom, Map<String, String> props) {
		// TODO Auto-generated method stub
		String fichier = props.get("fichier");
		if (fichier == null) {
			fichier = nom;
			int idx = fichier.indexOf(".");
			this.nomObjet = fichier.substring(0, idx);
		} else {
			this.nomObjet = nom;
		}

		
		this.nom = fichier;
	
		String slargeur = props.get("largeur");
		if (slargeur == null) {
			largeur = 1;
		} else {
			largeur = Integer.parseInt(slargeur);
		}
		String shauteur = props.get("hauteur");
		if (shauteur == null) {
			hauteur = 1;
		} else {
			hauteur = Integer.parseInt(props.get("hauteur"));
		}
		couleurBord = props.get("couleurBord");
	}

	public void creer(DecorDeBriqueData d, Habillage hab, String nomHabillage) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException, PlusDeTexture, IOException {

		if (largeur == 1 && hauteur == 1) {
			this.ajouterDansHabillageMono(hab);
			return;
		}
		photoHabillage ph = this.ajouterDansHabillageMulti(hab);
		ModelClasseDecor mX = new ModelClasseDecor();
		mX.nomModele = "photos." + nomObjet;
		mX.nomHabillage = nomHabillage;

		mX.init(1, hauteur, largeur);
		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur; y++) {
				mX.copie[0][hauteur - y - 1][largeur - x - 1] = new Color(ph.valeurs[x][y], ph.valeurBord,
						ph.valeurBord);

			}
		}
		ph.mc = mX;
		d.ajouter(mX, mX.nomModele + "#cubeX");

		ModelClasseDecor mY = new ModelClasseDecor();
		mY.nomModele = "photos." + nomObjet;
		mY.nomHabillage = nomHabillage;

		mY.init(largeur, 1, hauteur);
		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur; y++) {
				mY.copie[largeur-x-1][0][largeur-y-1] = new Color(ph.valeurBord,ph.valeurs[x][y],
						ph.valeurBord);

			}
		}
		ph.mc = mY;
		d.ajouter(mY, mY.nomModele + "#cubeY");

		ModelClasseDecor mZ = new ModelClasseDecor();
		mZ.nomModele = "photos." + nomObjet;
		mZ.nomHabillage = nomHabillage;

		mZ.init(largeur,hauteur, 1);
		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur; y++) {
				mZ.copie[largeur - x - 1][hauteur - y - 1][0] = new Color( ph.valeurBord, ph.valeurBord,
						ph.valeurs[x][y]);

			}
		}
		ph.mc = mZ;
		d.ajouter(mZ, mZ.nomModele + "#cubeZ");

	}

	public BufferedImage redimensionner(BufferedImage original, Habillage hab) {
		float width = original.getWidth();
		float height = original.getHeight();
		float SCALEX = (float) (this.largeur * hab.dim) / width;
		float SCALEY = (float) (this.hauteur * hab.dim) / height;

		BufferedImage bi = new BufferedImage(this.largeur * hab.dim, this.hauteur * hab.dim,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(SCALEX, SCALEY);
		grph.drawImage(original, 0, 0, null);
		grph.dispose();
		return bi;

	}

	public photoHabillage ajouterDansHabillageMulti(Habillage hab) throws PlusDeTexture, IOException,
			IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		BufferedImage img = ImageIO.read(new File(BrickEditor.cheminRessources + "/" + nom));
		img = this.redimensionner(img, hab);

		Point p = new Point();
		p.x = largeur;
		p.y = hauteur;
		photoHabillage ph = new photoHabillage(hab, largeur, hauteur);
		for (int ix = 0; ix < p.x; ix++) {

			for (int iy = 0; iy < p.y; iy++) {
				NomTexture nt;

				String nm = nomObjet;
				if (p.x > 1 || p.y > 1) {
					nm = nomObjet + "_" + ix + "_" + iy;
				}
				nt =hab.creerSiExistPas(nm);
				ph.valeurs[ix][iy] = nt.idx;

				for (int x = 0; x < hab.dim; x++) {
					for (int y = 0; y < hab.dim; y++) {
						int clr = img.getRGB(x + ix * hab.dim, y + iy * hab.dim);
						int red = (clr & 0x00ff0000) >> 16;
						int green = (clr & 0x0000ff00) >> 8;
						int blue = clr & 0x000000ff;
						Color c = new Color(red, green, blue);
						hab.SetBlock(hab.dim - 1 - x,hab.dim - 1 - y,nt.idx, c);
						// hab.couleurs[x][y][nt.idx] = c;

					}
				}
			}
		}
		NomTexture nt = hab.creerSiExistPas("cb_"+couleurBord);
		if (nt != null) {
			for (int x = 0; x < hab.dim; x++) {
				for (int y = 0; y < hab.dim; y++) {

					hab.SetBlock(x,hab.dim - 1 - y,nt.idx, (Color) Color.class.getField(couleurBord).get(null));

				}
			}
		}
		ph.valeurBord = nt.idx;

		return ph;

	}

	public void ajouterDansHabillageMono(Habillage hab) throws PlusDeTexture, IOException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {

		BufferedImage img = ImageIO.read(new File(BrickEditor.cheminRessources + "/" + nom));
		img = this.redimensionner(img, hab);

		NomTexture nt;

		String nm = nomObjet;

		nt = hab.creerSiExistPas(nm);

		for (int x = 0; x < hab.dim; x++) {
			for (int y = 0; y < hab.dim; y++) {
				int clr = img.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				Color c = new Color(red, green, blue);
				hab.SetBlock(hab.dim - 1 - x,hab.dim - 1 - y,nt.idx, c);
				// hab.couleurs[x][y][nt.idx] = c;

			}
		}
		hab.valeurs.put(nomObjet, new Color(nt.idx, nt.idx, nt.idx));

	}

}
