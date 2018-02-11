package dadou;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

public class Icone implements Serializable {
	private static final long serialVersionUID = -7725743070839512967L;
	public int width;
	public int height;
	public byte[] data;
	transient private BufferedImage image;

	public BufferedImage getImage() {
		if (image != null) {
			return image;
		}
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		ByteBuffer buff = ByteBuffer.allocateDirect(4 * width * height);
		buff.put(data);
		buff.get(0);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int idx = 4 * (x + y * width);
				image.getRaster().setPixel(
						x,
						height-1-y,
						new int[] { buff.get(idx), buff.get(idx + 1),
								buff.get(idx + 2) });

			}
		}
		return image;

	}

	public Icone(int width, int height, byte data[]) {
		this.width = width;
		this.height = height;
		this.data = data;

	}

	public void save(String fileName) {
	

		try {
			ImageIO.write(this.getImage(), "png", new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
