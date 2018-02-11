package dadou.test;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DessinerTexte {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//Font font = Font.decode("arial-10");
		String texte ="Hello power-kube";
		BufferedImage	image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D gr= image.createGraphics();
		//gr.setFont(font);
		
		FontMetrics metrics = gr.getFontMetrics();
		// get the height of a line of text in this
		// font and render context
		int hgt = metrics.getHeight();
		// get the advance of my text in this font
		// and render context
		int adv = metrics.stringWidth(texte);
		// calculate the size of a box to hold the
		// text with some padding.
	
		image = new BufferedImage(adv, hgt, BufferedImage.TYPE_3BYTE_BGR);
		gr= image.createGraphics();
		gr.setColor(Color.BLUE);
		gr.fillRect(0, 0, image.getWidth(),image.getHeight());
		gr.setColor(Color.RED);
		gr.fillRect(0, 0, adv,image.getHeight());
		gr.setColor(Color.WHITE);
		gr.drawString(texte, 0, image.getHeight()-1);
		ImageIO.write(image, "png", new File("d:/dessiner_texte.png"));
	}

}
