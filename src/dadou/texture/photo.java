package dadou.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import dadou.Arbre;
import dadou.ConfigLine;
import dadou.DecorDeBriqueData;
import dadou.Habillage;
import dadou.Log;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.tools.BrickEditor;
import dadou.tools.HabillageIHM;
import dadou.tools.ImagePanel;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;
import dadou.tools.UI;

public class photo extends ConfigLine implements Runnable {
	public String nom;
	public String nomClasse;
	public int largeur;
	public int hauteur;
	public int tailleCube;

	public String couleurBord = "BLUE";

	public static Map<String, photo> photos() throws InstantiationException,
			IllegalAccessException {
		if (new File(BrickEditor.cheminRessources + "/photos.txt").exists()) {

			Map<String, photo> r = ConfigLine.lire(photo.class, OutilFichier
					.lireChaineDansFichier(BrickEditor.cheminRessources
							+ "/photos.txt"));

			Map<String, photo> rSansEtoile = new HashMap<>();
			for (Map.Entry<String, photo> et : r.entrySet()) {

				String nom = et.getKey();
				photo base = et.getValue();

				if (nom.charAt(nom.length() - 1) == '*') {
					String rep = (nom.substring(0, nom.length() - 2));
					File file = new File(BrickEditor.cheminRessources + "/"
							+ rep);

					System.out.println(" recherche dans : " + file.getName());
					if (file.exists() && file.isDirectory()) {

						File[] contenu = file.listFiles();
						for (File img : contenu) {
							photo p = new photo();
							String name = img.getName();
							if (name.endsWith(".jpg") || name.endsWith(".png")) {

								p.nom = rep + "/" + name;
								p.nomClasse = p.nom.substring(0,
										p.nom.indexOf("."));
								p.tailleCube = base.tailleCube;
								p.hauteur = base.hauteur;
								p.largeur = base.largeur;
								rSansEtoile.put(p.nom, p);
								System.out.println("chargement " + p.nom);

							}
						}

					}
				} else {
					System.out.println("chargement " + nom);
					rSansEtoile.put(nom, base);
				}

			}
			return rSansEtoile;

		}
		return null;
	}

	@Override
	public void initialiser(String nom, Map<String, String> props) {
		// TODO Auto-generated method stub
		int idx = nom.indexOf(".");
		this.nom = nom;
		if (idx >= 0) {
			this.nomClasse = nom.substring(0, idx);
		}
		largeur = Integer.parseInt(props.get("largeur"));
		hauteur = Integer.parseInt(props.get("hauteur"));
		tailleCube = Integer.parseInt(props.get("tailleCube"));

		couleurBord = props.get("couleurBord");
	}

	public photoHabillage creer(DecorDeBriqueData d)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, PlusDeTexture, IOException {
		photoHabillage ph = this.mc;
		ModelClasse mX = new ModelClasse();
		mX.nomModele = "photos." + nomClasse;
		mX.nomHabillage = nomClasse + ".hab";

		mX.init(1, hauteur, largeur);
		for (int x = 0; x < largeur; x++) {
			for (int y = 0; y < hauteur; y++) {
				mX.copie[0][hauteur - y - 1][largeur - x - 1] = new Color(
						ph.valeurs[x][y], ph.valeurBord, ph.valeurBord);

			}
		}
		ph.mc = mX;
		ph.nom = mX.nomModele + "#cube";
		d.ajouter(mX, ph.nom);
		Log.print(" import " + mX.nomModele);
		return ph;

	}

	public BufferedImage redimensionner(BufferedImage original) {
		float width = original.getWidth();
		float height = original.getHeight();
		float SCALEX = (float) (this.largeur * tailleCube) / width;
		float SCALEY = (float) (this.hauteur * tailleCube) / height;

		BufferedImage bi = new BufferedImage(this.largeur * tailleCube,
				this.hauteur * tailleCube, BufferedImage.TYPE_INT_ARGB);

		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(SCALEX, SCALEY);
		grph.drawImage(original, 0, 0, null);
		grph.dispose();
		return bi;

	}

	public photoHabillage creerHabillage() throws PlusDeTexture, IOException,
			IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		Habillage hab = new Habillage(tailleCube, largeur * hauteur + 2);
		annotation a = annotation.annotations().get(nom);
		BufferedImage img = null;

		if (a == null) {
			img=ImageIO.read(new File(BrickEditor.cheminRessources + "/" + nom));
			img = this.redimensionner(img);
		} else {
			img = ImagePanel.creerImageAvecAnnotation(new File(
					BrickEditor.cheminRessources + "/" + nom), this, a);
		}
		Point p = new Point();
		p.x = largeur;
		p.y = hauteur;
		photoHabillage ph = new photoHabillage(hab, largeur, hauteur);
		for (int ix = 0; ix < p.x; ix++) {

			for (int iy = 0; iy < p.y; iy++) {
				NomTexture nt;

				String nm = nom;
				if (p.x > 1 || p.y > 1) {
					nm = nom + "_" + ix + "_" + iy;
				}
				nt = hab.creerNomTexture(nm);
				ph.valeurs[ix][iy] = nt.idx;

				for (int x = 0; x < hab.dim; x++) {
					for (int y = 0; y < hab.dim; y++) {
						int clr = img
								.getRGB(x + ix * hab.dim, y + iy * hab.dim);
						int red = (clr & 0x00ff0000) >> 16;
						int green = (clr & 0x0000ff00) >> 8;
						int blue = clr & 0x000000ff;
						Color c = new Color(red, green, blue);
						hab.SetBlock(hab.dim - 1 - x, hab.dim - 1 - y, nt.idx,
								c);
						// hab.couleurs[x][y][nt.idx] = c;

					}
				}
			}
		}
		NomTexture nt = hab.creerNomTexture("cb_" + couleurBord);
		for (int x = 0; x < hab.dim; x++) {
			for (int y = 0; y < hab.dim; y++) {

				hab.SetBlock(x, hab.dim - 1 - y, nt.idx, (Color) Color.class
						.getDeclaredField(couleurBord).get(null));

			}
		}
		ph.valeurBord = nt.idx;
		SerializeTool.save(ph.habillage, new File(BrickEditor.cheminRessources,
				nomClasse + ".hab"));
		return ph;

	}

	public photoHabillage mc;

	@Override
	public void run() {
		try {
			mc = this.creerHabillage();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PlusDeTexture e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.print(" erreur " + this.nom);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
