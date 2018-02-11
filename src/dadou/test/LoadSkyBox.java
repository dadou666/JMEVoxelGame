package dadou.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LoadSkyBox {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int u=8;
		BufferedImage bi = ImageIO.read(new File("f:/skybox"+u+".png"));
		int dx = bi.getWidth() / 4;
		int dy = bi.getHeight() / 3;
		String noms[] = new String[]{
			"Front",
			"Left",
			"Back",
			"Right"
		};
		for (int i = 0; i < 4; i++) {

			bi.getSubimage(i * dx, dy, dx, dy);
			ImageIO.write(bi.getSubimage(i * dx, dy, dx, dy), "png", new File("f:/skybox"+u+"_"+noms[i]+".png"));

		}
		ImageIO.write(bi.getSubimage(dx, 0, dx, dy), "png", new File("f:/skybox"+u+"_Top.png"));
		ImageIO.write(bi.getSubimage(dx, 2*dy, dx, dy), "png", new File("f:/skybox"+u+"_Bottom.png"));
	}
}
