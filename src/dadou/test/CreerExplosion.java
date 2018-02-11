package dadou.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.math.FastMath;

import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.ElementDecor;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class CreerExplosion {
	static public Color color(Color c, float tx) {
		float b = c.getBlue();
		float r = c.getRed();
		float g = c.getGreen();
		b = (b / 255.0f) * tx;
		r = (r / 255.0f) * tx;
		g = (g / 255.0f) * tx;
		return new Color(r, g, b);

	}

	static public boolean estSurLeBord(Habillage h, int x, int y, int marge) {
		if (x < marge) {
			return true;
		}
		if (y < marge) {
			return true;
		}
		if (x > h.dim - 1 - marge) {
			return true;
		}
		if (y > h.dim - 1 - marge) {
			return true;
		}
		return false;
	}

	public static void main(String[] args)
			throws FileNotFoundException, ClassNotFoundException, IOException, PlusDeTexture, CouleurErreur {
		// TODO Auto-generated method stub
		Habillage conversion = new Habillage(32);
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		float d = conversion.dim / 2;
		int h=1;
		Color line = Color.RED;
		Color fond =Color.BLACK;
		for (float i = 0; i < conversion.dim ; i++) {
			NomTexture nt = conversion.creerNomTexture("e"+(int)i);
			BufferedImage bimage = new BufferedImage(conversion.dim, conversion.dim, BufferedImage.TYPE_INT_ARGB);

			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			float n = 10;
			bGr.setColor(fond);
			bGr.fillRect(0,0, conversion.dim, conversion.dim);
			bGr.setColor(line);
			bGr.fillRect(0, (int)i, conversion.dim, 1);
			bGr.dispose();
			for (int x = 0; x < conversion.dim; x++) {
				for (int y = 0; y < conversion.dim; y++) {
					int clr = bimage.getRGB(x, y);
					int red = (clr & 0x00ff0000) >> 16;
					int green = (clr & 0x0000ff00) >> 8;
					int blue = clr & 0x000000ff;
					Color c = new Color(red, green, blue);
					conversion.SetBlock(x,conversion.dim - 1 - y,nt.idx, c);

				}
			}

		}

		SerializeTool.save(conversion, new File(cheminRessources, "sprite.hab"));

	}

}
