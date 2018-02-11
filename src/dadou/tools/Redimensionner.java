package dadou.tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Redimensionner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File rep = new File("D:/textures/firework");
		String prefixe = "fireworks_";
		int i = 0;
		for (File file : rep.listFiles()) {
			BufferedImage img = ImageIO.read(file);
		
			Image imgScale =img.getScaledInstance(64,64, java.awt.Image.SCALE_AREA_AVERAGING);
			
			
			ImageIO.write( toBufferedImage(imgScale), "jpg", new File(rep,prefixe+i+".jpg"));
			i++;
			
		}

	}
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
