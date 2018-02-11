package dadou.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import dadou.Log;
import dadou.texture.annotation;
import dadou.texture.photo;

public class ImagePanel extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static String descs[] = { "Simple Copy", "Scale Up", "Scale Down",
			"Scale Up : Bicubic", "Convolve : LowPass", "Convolve : Sharpen",
			"RescaleOp", "LookupOp", };

	int opIndex;
	private BufferedImage bi;
	int w, h;

	public static final float[] SHARPEN3x3 = { // sharpening filter kernel
	0.f, -1.f, 0.f, -1.f, 5.f, -1.f, 0.f, -1.f, 0.f };

	public static final float[] BLUR3x3 = { 0.1f, 0.1f, 0.1f, // low-pass filter
																// kernel
			0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };

	public ImagePanel() {

	}

	public static BufferedImage creerImageAvecAnnotation(File imageSrc,
			photo photo, annotation annotation) throws IOException {
		BufferedImage bi = ImageIO.read(imageSrc);

		if (photo != null) {
			int largeurTexture = photo.largeur * photo.tailleCube;
			int hauteurTexture = photo.hauteur * photo.tailleCube;
			BufferedImage biTmp = redimensionner(bi, largeurTexture,
					hauteurTexture);
			BufferedImage bi2 = new BufferedImage(largeurTexture,
					hauteurTexture, BufferedImage.TYPE_INT_RGB);
			Graphics big = bi2.getGraphics();
			big.drawImage(biTmp, 0, 0, null);
			if (annotation.annotation != null) {
				Font font = new Font("Serif", Font.PLAIN, annotation.taille);
				big.setFont(font);
				try {
					if (annotation.color != null) {
						Color color = (Color) Color.class.getField(
								annotation.color).get(null);
						big.setColor(color);
					}
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
				}
				big.drawString(annotation.annotation, annotation.x,
						annotation.y);
			}
			bi = bi2;
			return bi;

		}
		return bi;
	}

	public void charger(File imageSrc, photo photo, annotation annotation) {
		Log.print(" " + this.getWidth() + " " + this.getHeight());
		int largeur = this.getWidth();
		int hauteur = this.getHeight();

		try {
			bi = creerImageAvecAnnotation(imageSrc, photo, annotation);
			w = bi.getWidth(null);
			h = bi.getHeight(null);
	
			bi = redimensionner(bi, largeur, hauteur);
		} catch (IOException e) {
			System.out.println("Image could not be read");
			// System.exit(1);
		}
		this.repaint();

	}

	public Dimension getPreferredSize() {
		return new Dimension(w, h);
	}

	static String[] getDescriptions() {
		return descs;
	}

	void setOpIndex(int i) {
		opIndex = i;
	}

	public void paint(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g.drawImage(bi, 0, 0, null);
	}

	static public BufferedImage redimensionner(BufferedImage original,
			int largeur, int hauteur) {
		float width = original.getWidth();
		float height = original.getHeight();
		float SCALEX = (float) (largeur) / width;
		float SCALEY = (float) (hauteur) / height;

		BufferedImage bi = new BufferedImage(largeur, hauteur,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D grph = (Graphics2D) bi.getGraphics();
		grph.scale(SCALEX, SCALEY);
		grph.drawImage(original, 0, 0, null);
		grph.dispose();
		return bi;

	}

}