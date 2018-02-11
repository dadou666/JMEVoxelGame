package dadou;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class VoxelTexture3D implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9083682492680440093L;
	transient public VoxelTexture3DEcouteur ecouteur;
	public int dimX;
	public int dimY;
	public int dimZ;
	public float fX;
	public float fY;
	public float fZ;
	public transient Integer texId;

	public byte[] bytes;
transient	public ByteBuffer pixel;

	static public class CouleurErreur extends Exception {

		public CouleurErreur(String msg) {
			super(msg);
		}

	}

	public void SetBlock(int x, int y, int z, Color color) throws CouleurErreur {

		if (ElementDecor.estVide(this.getBlockColor(x, y, z))) {
			if (!ElementDecor.estVide(color)) {
				if (ecouteur != null) {
					ecouteur.ajouterBrique(x, y, z);
				}

			}

		} else {
			if (ElementDecor.estVide(color)) {
				if (ecouteur != null) {
					ecouteur.supprimerBrique(x, y, z);
				}

			}

		}

		int idx = (x + (y + z * dimY) * dimX) * 4;
		byte r = (byte) color.getRed();
		byte g = (byte) color.getGreen();
		byte b = (byte) color.getBlue();
		byte a = (byte) color.getAlpha();
		bytes[idx] = r;
		bytes[idx + 1] = g;
		bytes[idx + 2] = b;
		bytes[idx + 3] = a;

	}

	public VoxelTexture3D(int dim) {
		this.dimX = dim;
		this.dimY = dim;
		this.dimZ = dim;
		bytes = new byte[dim * dim * dim * 4];
		pixel = BufferUtils.createByteBuffer(4);
		fX = 1.0f / (float) dim;
		fY = fX;
		fZ = fX;
	}

	public VoxelTexture3D(int dx, int dy, int dz) {
		this.dimX = dx;
		this.dimY = dy;
		this.dimZ = dz;
		bytes = new byte[dx * dy * dz * 4];
		pixel = BufferUtils.createByteBuffer(4);
		fX = 1.0f / (float) dx;
		fY = 1.0f / (float) dy;
		;
		fZ = 1.0f / (float) dz;
		;
	}

	public VoxelTexture3D(byte[] bytes, int dim) throws IOException {
		this.dimX = dim;
		this.dimY = dim;
		this.dimZ = dim;
		this.bytes = bytes;
		pixel = BufferUtils.createByteBuffer(4);
		fX = 1.0f / (float) dim;
		fY = fX;
		fZ = fX;

	}

	public VoxelTexture3D(byte[] bytes, int dx, int dy, int dz) throws IOException {
		this.dimX = dx;
		this.dimY = dy;
		this.dimZ = dz;
		this.bytes = bytes;
		pixel = BufferUtils.createByteBuffer(4);
		fX = 1.0f / (float) dx;
		fY = 1.0f / (float) dy;
		fZ = 1.0f / (float) dz;

	}

	public int byteToInt(byte b) {
		if (b >= 0) {
			return b;
		}
		int i = b;
		return i + 256;

	}

	public byte getRed(int x, int y, int z) {
		int idx = (x + (y + z * dimY) * dimX) * 4;
		return bytes[idx];
	}

	public byte getGreen(int x, int y, int z) {
		int idx = (x + (y + z * dimY) * dimX) * 4;
		return bytes[idx + 1];
	}

	public byte getBlue(int x, int y, int z) {
		int idx = (x + (y + z * dimY) * dimX) * 4;
		return bytes[idx + 2];
	}

	public byte getAlpha(int x, int y, int z) {
		int idx = (x + (y + z * dimY) * dimX) * 4;
		return bytes[idx + 3];
	}

	public void setAlpha(int x, int y, int z, byte b) {
		int idx = (x + (y + z * dimY) * dimX) * 4;
		bytes[idx + 3] = b;
	}

	public Color getBlockColor(int x, int y, int z) throws CouleurErreur {
		if (x < 0 || x >= dimX) {
			throw new CouleurErreur(" x = " + x + " hors limite " + dimX);
		}
		if (y < 0 || y >= dimY) {
			throw new CouleurErreur(" x = " + y + " hors limite " + dimY);
		}
		if (z < 0 || z >= dimZ) {
			throw new CouleurErreur(" x = " + z + " hors limite " + dimZ);
		}
		int idx = (x + (y + z * dimY) * dimX) * 4;
		if (idx >= bytes.length) {
			throw new CouleurErreur(" x,y,z =" + x + " " + y + " " + z + " dim=" + dimX + "," + dimY + "," + dimZ);
		}
		int red = byteToInt(bytes[idx]);
		int green = byteToInt(bytes[idx + 1]);
		int blue = byteToInt(bytes[idx + 2]);
		int alpha = byteToInt(bytes[idx + 3]);
		try {
			return new Color(red, green, blue, alpha);
		} catch (java.lang.IllegalArgumentException e) {
			throw new Error(" Couleur (red=" + red + " green=" + green + " blue=" + blue);
		}
	}

	public void updateBlock(int x, int y, int z, Color color) throws CouleurErreur {
		if (pixel == null) {
			pixel = BufferUtils.createByteBuffer(4);
		}
		if (color == null) {
			color = Color.BLACK;
		}
		pixel.clear();
		pixel.put(0, (byte) color.getRed());
		pixel.put(1, (byte) color.getGreen());
		pixel.put(2, (byte) color.getBlue());
		pixel.put(3, (byte) color.getAlpha());

		this.SetBlock(x, y, z, color);
		if (this.Texture2DArray) {
			OpenGLTools.updateBlockTexture2DArray(texId, x, y, z, 1, 1, 1, pixel);

		} else {
			OpenGLTools.updateBlock(texId, x, y, z, 1, 1, 1, pixel);
		}

	}

	public void updateBlock(int x, int y, int z, int dx, int dy, int dz, ByteBuffer buffer) {
		OpenGLTools.updateBlock(texId, x, y, z, dx, dy, dz, buffer);

	}

	public void createTexture3D() {
		texId = OpenGLTools.createTexture3D(bytes, dimX, dimY, dimZ);
		delete = false;
		OpenGLTools.exitOnGLError("createTexture3D");

	}

	public void createTexture3DLINEAR() {
		texId = OpenGLTools.createTexture3D_LINEAR(bytes, dimX, dimY, dimZ);
		delete = false;
		OpenGLTools.exitOnGLError("createTexture3D");

	}

	boolean Texture2DArray = false;

	public void createTexture2DArray(Habillage h) {
		texId = OpenGLTools.createTexture2DArray(bytes, h);
		Texture2DArray = true;
		delete = false;
		OpenGLTools.exitOnGLError("createTexture3D");

	}

	public void bindTexture() {
		if (Texture2DArray) {

			GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);
		} else {
			OpenGLTools.bindTexture3D(texId);
		}
		OpenGLTools.exitOnGLError("bindTexture");

	}

	public void bindTexture(int unit) {
		if (Texture2DArray) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL13.glActiveTexture(unit);
			GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);
		} else {
			OpenGLTools.bindTexture3D(unit, texId);
		}

	}

	public void save(String fileName) throws IOException {
		/*
		 * for(int x=0;x < dim;x++) { for(int y=0;y < dim;y++){ for(int z=0;z <
		 * dim;z++){ if (!this.getBlockColor(x, y, z).equals(Color.BLACK)){
		 * System.out.println(" Color Write "); }
		 * 
		 * } } }
		 */
		FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(bytes);
		fos.close();
	}

	public void load(String fileName) throws IOException {
		InputStream in = new FileInputStream(fileName);
		long length = (new File(fileName)).length();

		if (length > Integer.MAX_VALUE) {
			throw new IOException("File is too large!");
		}

		bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;

		while (offset < bytes.length && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + fileName);
		}

		/*
		 * for(int x=0;x < dim;x++) { for(int y=0;y < dim;y++){ for(int z=0;z <
		 * dim;z++){ if (!this.getBlockColor(x, y, z).equals(Color.BLACK)){
		 * System.out.println(" Color Read "); }
		 * 
		 * } } }
		 */
		in.close();

	}
	transient boolean delete = false;
	public void delete() {
		if (delete) {
			return;
		}
		OpenGLTools.deleteTexture(texId);
		delete = true;
	}
	public void finalize() {
		
		if (!delete) {
		//	Log.print(" delete "+this);
			this.delete();
		}
	}
}
