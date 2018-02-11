package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE0_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

public class Texture2D {
	public int texId;
	private boolean init = false;

	public int width, height;
	public BufferedImage image;
	public byte[] bytes;

	public Texture2D(int width, int height) {
		this.width = width;
		this.height = height;

	}

	public Texture2D(BufferedImage img) {

		this.width = img.getWidth();
		this.height = img.getHeight();
		this.image = img;
		this.updateRGBA(asBytes());
	}
	public void finalize() {
		OpenGLTools.deleteTexture(texId);
	}
	public byte[] asBytes() {
		BufferedImage bi = image;
		int dimX = bi.getWidth();
		int dimY = bi.getHeight();
		
		if (bytes == null) {
			bytes = new byte[dimX * dimY * 4];
		}
	
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y < dimY; y++) {
				int idx = (x + y * dimX) * 4;
				int clr = bi.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;
				byte r = (byte) red;
				byte g = (byte) green;
				byte b = (byte) blue;
				byte a = (byte) 255;
				bytes[idx] = r;
				bytes[idx + 1] = g;
				bytes[idx + 2] = b;
				bytes[idx + 3] = a;

			}
		}

		return bytes;

	}

	public Graphics2D getGraphics2DForUpdate() {
		if (image != null) {

			OpenGLTools.exitOnGLError("Update");

		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		}
		return image.createGraphics();

	}

	public void update() {

	updateRGBA(asBytes());
	/*	//image.getData().
		updateRGBA(image.getRaster().getPixel(0, 0,(int[]) null));*/
	}

	public void update(byte data[]) {
		if (init) {
			OpenGLTools.updateTexture(texId, width, height, data);
			return;

		}
		texId = OpenGLTools.createTexture(width, height, data);
		init = true;

	}

	public void updateRGBA(byte data[]) {
		if (init) {
			OpenGLTools.updateTextureRGBA(texId, width, height, data);
			return;
		}
		texId = OpenGLTools.createTextureRGBA(width, height, data);
		init = true;
	}
	public void updateRGBA(int data[]) {
		if (init) {
			OpenGLTools.updateTextureRGBA(texId, width, height, data);
			return;
		}
		texId = OpenGLTools.createTextureRGBA(width, height, data);
		init = true;
	}

	public void bind() {
		glActiveTextureARB(GL_TEXTURE0_ARB);
		glEnable(GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
	}

}
