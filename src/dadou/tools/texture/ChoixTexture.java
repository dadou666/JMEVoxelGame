package dadou.tools.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.BriqueAvecTexture3D;
import dadou.Button;
import dadou.FBO;
import dadou.Game;
import dadou.Habillage;
import dadou.Icone;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.Objet3D;
import dadou.ObjetMobile;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.ChoixGenerique;
import dadou.tools.ElementChoix;
import dadou.tools.EtatBrickEditor;

public class ChoixTexture extends ChoixGenerique<NomTexture> {
	Map<String, BufferedImage> images = new HashMap<>();
	Map<String, BufferedImage> imageInverses = new HashMap<>();
	public boolean afficherNon = false;

	public int width() {
		return h.dim;
	}

	public int height() {
		return h.dim;
	}

	Habillage h;

	public ChoixTexture(Habillage h, Game game, boolean afficherNom) {
		List<ElementChoix<NomTexture>> r = new ArrayList<>();
		this.h = h;
		this.afficherNon = afficherNom;

		ElementChoix<NomTexture> ec = null;

		for (NomTexture n : h.noms()) {
			ec = new ElementChoix<>();

			ec.image = this.creerImage(h, n);
			this.images.put(n.nom, ec.image);
			this.imageInverses.put(n.nom, this.creerImageInverse(h, n));
			ec.valeur = n;

			r.add(ec);

		}
		NomTexture n = new NomTexture();
		n.idx = 0;
		ec = new ElementChoix<>();

		ec.image = this.creerImage(h, n);
		this.images.put(n.nom, ec.image);
		this.imageInverses.put(n.nom, this.creerImageInverse(h, n));
		ec.valeur = n;

		r.add(ec);
		this.liste.addAll(r);
		this.margeIHM = 100;
		marge = 2;
		init(6, 6, null, game);

	}

	public void initialiser(boolean inverse) {
		for (ElementChoix<NomTexture> ec : this.liste) {
			if (inverse) {
				ec.image = this.imageInverses.get(ec.valeur.nom);
			} else {
				ec.image = this.images.get(ec.valeur.nom);
			}
		}

		this.modifierTableau();
	}

	BufferedImage creerImage(Habillage h, NomTexture n) {

		BufferedImage image = new BufferedImage(h.dim, h.dim, BufferedImage.TYPE_3BYTE_BGR);
		if (n.idx == 0) {
			for (int x = 0; x < h.dim; x++) {
				for (int y = 0; y < h.dim; y++) {
					Color color = Color.BLACK;

					image.getRaster().setPixel(x, h.dim - 1 - y,
							new int[] { color.getRed(), color.getGreen(), color.getBlue() });

				}
			}

			Graphics2D g = image.createGraphics();
			g.setColor(Color.RED);
			g.drawString("@", 3, h.dim / 2);
			g.dispose();

		} else {
			for (int x = 0; x < h.dim; x++) {
				for (int y = 0; y < h.dim; y++) {
					Color color = h.GetBlock(x,y,n.idx);

					if (color == null) {
						color = Color.BLACK;
					}
					image.getRaster().setPixel(x, h.dim - 1 - y,
							new int[] { color.getRed(), color.getGreen(), color.getBlue() });

				}
			}
			if (this.afficherNon) {
				Graphics2D g = image.createGraphics();
				g.setColor(Color.RED);
				g.drawString(n.position(), 3, h.dim / 2);
				g.dispose();
			}
		}
		return image;

	}

	BufferedImage creerImageInverse(Habillage h, NomTexture n) {

		BufferedImage image = new BufferedImage(h.dim, h.dim, BufferedImage.TYPE_3BYTE_BGR);
		if (n.idx == 0) {
			for (int x = 0; x < h.dim; x++) {
				for (int y = 0; y < h.dim; y++) {
					Color color = Color.BLACK;

					image.getRaster().setPixel(x, h.dim - 1 - y,
							new int[] { color.getRed(), color.getGreen(), color.getBlue() });

				}
			}

			Graphics2D g = image.createGraphics();
			g.setColor(Color.RED);
			g.drawString("@", 3, h.dim / 2);
			g.dispose();
		} else {
			for (int x = 0; x < h.dim; x++) {
				for (int y = 0; y < h.dim; y++) {
					Color color = h.GetBlock(x,y,n.idx);
					if (color == null) {
						color = Color.BLACK;
					}
					image.getRaster().setPixel(h.dim - 1 - x, h.dim - 1 - y,
							new int[] { color.getRed(), color.getGreen(), color.getBlue() });

				}
			}
			if (this.afficherNon) {
				Graphics2D g = image.createGraphics();
				g.setColor(Color.RED);
				g.drawString(n.position(), 3, h.dim / 2);
				g.dispose();
			}
		}
		return image;

	}

	public ModelClasse creerModelClasse(Color color) throws CouleurErreur, Exception {

		Color copie[][][] = new Color[1][1][1];
		copie[0][0][0] = color;
		ModelClasse mc = new ModelClasse();
		mc.copie = copie;
		mc.dx = 1;
		mc.dy = 1;
		mc.dz = 1;
		// ModelClasse mc = decorDeBriqueData.models.get("objet.arme");

		return mc;

	}

	public Objet3D creerObjet(ModelClasse mc, Game g, Habillage h) throws CouleurErreur, Exception {

		if (mc.vbo == null) {
			mc.initBuffer(g, true, h);
		}
		Vector3f size = mc.size.clone();
		VBOBriqueTexture3D vbo = mc.vbo;
		VoxelTexture3D tex = mc.tex;
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();

		BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
		translation.brique = brique;
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);

		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		obj.ajouter(translation);

		return obj;

	}

	public void gerer() throws CouleurErreur {
		super.gerer();
		if (quitter && BrickEditor != null) {
			BrickEditor.etat = BrickEditor.selection;
			BrickEditor.selection.BoxSelectionAction =null;
			BrickEditor.selection.setBoxSelection(false);
			BrickEditor.selection.placerLumiere = false;
			BrickEditor.processMenuKey = true;
			if (choix == null) {
				BrickEditor.menuTexture.selected = false;
				BrickEditor.menuTexture.updateText();
			}
			return;

		}
		if (BrickEditor != null && choix != null) {
			BrickEditor.selection.BoxSelectionAction =null;
			BrickEditor.selection.placerLumiere = false;
			BrickEditor.selection.setBoxSelection(false);
			BrickEditor.processMenuKey = true;
			BrickEditor.etat = BrickEditor.selection;
			// BrickEditor.menuCouleur.selected = false;
			// BrickEditor.menuCouleur.updateText();
		}
	}

}
