package dadou;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBTextureStorage;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTTextureCompressionS3TC;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.glu.GLU;

import dadou.Shader.ErrorCreationShader;

public class OpenGLTools {
	
	static public int updateTexture(int texId,int width, int height, byte[] data) {
		//int texId = GL11.glGenTextures();
		exitOnGLError("createTexture");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 3);
		bb.put(data);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB4, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}
	static public int createTexture(int width, int height, byte[] data) {
		int texId = GL11.glGenTextures();
		exitOnGLError("createTexture");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 3);
		bb.put(data);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB4, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
				bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}
static public int updateTextureRGBA(int texId,int width, int height, int[] data) {
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		IntBuffer bb = BufferUtils.createIntBuffer(width*height);
		bb.put(data);
		bb.flip();

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_INT,
				bb);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}
	static public int updateTextureRGBA(int texId,int width, int height, byte[] data) {
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 4);
		bb.put(data);
		bb.flip();

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				bb);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}
	static public int createTextureRGBA(int width, int height, int[] data) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		IntBuffer bb = BufferUtils.createIntBuffer(width*height);
		bb.put(data);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}
	static public int createTextureRGBA(int width, int height, byte[] data) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 4);
		bb.put(data);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture");
		return texId;

	}

	static public int createProgramShader(int vertexShader, int fragmentShader) {
		int program = ARBShaderObjects.glCreateProgramObjectARB();
		exitOnGLError("errrr");
		if (program == 0) {
			return 0;
		}
		/*
		 * if the vertex and fragment shaders setup sucessfully, attach them to
		 * the shader program, link the sahder program (into the GL context I
		 * suppose), and validate
		 */
		ARBShaderObjects.glAttachObjectARB(program, vertexShader);
		ARBShaderObjects.glAttachObjectARB(program, fragmentShader);
		exitOnGLError("errrr");
		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			System.out.println(getLogInfo(program));
			throw new Error(getLogInfo(program));

		}

	/*	ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			throw new Error(getLogInfo(program));
		}*/
		exitOnGLError("errrr");
		return program;

	}
	static public int createProgramShader(int vertexShader, int fragmentShader,int geoShader) {
		int program = ARBShaderObjects.glCreateProgramObjectARB();
		exitOnGLError("errrr");
		if (program == 0) {
			return 0;
		}
		/*
		 * if the vertex and fragment shaders setup sucessfully, attach them to
		 * the shader program, link the sahder program (into the GL context I
		 * suppose), and validate
		 */
		ARBShaderObjects.glAttachObjectARB(program, vertexShader);
		ARBShaderObjects.glAttachObjectARB(program, fragmentShader);
		ARBShaderObjects.glAttachObjectARB(program, geoShader);
		exitOnGLError("errrr");
		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			System.out.println(getLogInfo(program));
			throw new Error(getLogInfo(program));

		}

	/*	ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program,
				ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			throw new Error(getLogInfo(program));
		}*/
		exitOnGLError("errrr");
		return program;

	}
	static public int createShader(String source, int type) {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(type);

			if (shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, source);
			// System.out.println("" + shaderType + "\n" + source + "\n");
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader,
					ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			exitOnGLError("errrr");
			return shader;
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}

	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj,
				ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	static public int createTexture3D(byte[] bytes, int dx, int dy, int dz) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL12.GL_TEXTURE_3D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(dx * dy * dz * 4);
		bb.put(bytes);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, dx, dy, dz, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glBindTexture(GL12.GL_TEXTURE_3D, 0);
		// bytes = null;
		exitOnGLError("createTexture3D");
		return texId;

	}

	static public int createTexture3D_LINEAR(byte[] bytes, int dx, int dy, int dz) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL12.GL_TEXTURE_3D, texId);
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(dx * dy * dz * 4);
		bb.put(bytes);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, dx, dy, dz, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		// GL30.glGenerateMipmap(GL12.GL_TEXTURE_3D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER,
		 * GL11.GL_NEAREST); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		 */
		GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, 0);
		// bytes = null;
		exitOnGLError("createTexture3D");
		return texId;

	}
	static public boolean useMipmap = false;
	static int mipmapLevel(int dim) {
	 if (!useMipmap) {
		 return 2;
	 }
		if (dim <= 1) {
			return 1;
		}
		return mipmapLevel(dim / 2) + 1;

	}

	static public int createTexture2DArray(byte data[], Habillage h) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);

		int dim = h.dim;
		ByteBuffer bb = BufferUtils.createByteBuffer(dim * dim * h.nbTexture* 4);
		bb.put(data);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		// GL42 g;
		int mipmap =  mipmapLevel(dim);
		ARBTextureStorage.glTexStorage3D(GL30.GL_TEXTURE_2D_ARRAY, mipmap, GL11.GL_RGBA8, dim, dim, h.nbTexture);
		exitOnGLError("createTexture3D");
		GL12.glTexSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, 0, dim, dim, h.nbTexture, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				bb);

		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D_ARRAY);
		// GL12.glTexImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, GL11.GL_RGBA, dim,
		// dim, 256, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		exitOnGLError("createTexture3D");

		float d = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);

		// System.out.println(" d="+d);
		GL11.glTexParameterf(GL30.GL_TEXTURE_2D_ARRAY, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, d);
		exitOnGLError("createTexture3D");
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		// GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER,
		// GL11.GL_NEAREST_MIPMAP_LINEAR);
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, 0);
		// bytes = null;
		exitOnGLError("createTexture3D");
		return texId;

	}

	static public void exitOnGLError(String errorMessage) {

		int errorValue = GL11.glGetError();

		if (errorValue != GL11.GL_NO_ERROR) {

			String errorString = GLU.gluErrorString(errorValue);

			// System.err.println("ERROR - " + errorMessage + ": " +
			// errorString);

			// throw new Error("ERROR - " + errorMessage + ": " + errorString);

		}

	}

	static public void updateBlock(int texId, int x, int y, int z, int dx, int dy, int dz, ByteBuffer pixel) {
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, texId);
		GL12.glTexSubImage3D(GL12.GL_TEXTURE_3D, 0, x, y, z, dx, dy, dz, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixel);
		exitOnGLError("updateBlock");

	}

	static public void updateBlockTexture2DArray(int texId, int x, int y, int z, int dx, int dy, int dz,
			ByteBuffer pixel) {
		GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, texId);
		GL12.glTexSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, x, y, z, dx, dy, dz, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				pixel);
		exitOnGLError("updateBlock");

	}

	static public int createTexture2D(int dx, int dy, byte bytes[]) {
		int texId = GL11.glGenTextures();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		int type = EXTTextureCompressionS3TC.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		// EXTTextureCompressionS3TC. e;
		// /GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		// All RGB bytes are aligned to each other and each component is 1 byte

		// GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		ByteBuffer bb = BufferUtils.createByteBuffer(dx * dy * 4);
		bb.put(bytes);
		bb.flip();

		// Upload the texture data and generate mip maps (for scaling)

		/*
		 * GL12.glTexImage3D(GL12.GL_TEXTURE_3D, 0, GL11.GL_RGBA, size, size,
		 * size, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		 */
		// GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, dx, dy, 0,
		// GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, dx, dy, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		/*
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S,
		 * GL12.GL_CLAMP_TO_EDGE); GL11.glTexParameteri(GL12.GL_TEXTURE_3D,
		 * GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		 * GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R,
		 * GL12.GL_CLAMP_TO_EDGE);
		 */
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		exitOnGLError("createTexture2D");
		return texId;

	}

	

	static public void bindTexture3D(int texId) {
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, texId);
	}

	static public void bindTexture3D(int unit, int texId) {
		GL11.glEnable(GL12.GL_TEXTURE_3D);
		GL13.glActiveTexture(unit);
		GL11.glBindTexture(GL12.GL_TEXTURE_3D, texId);
	}

	static public void bindTexture2D(int texId) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
	}

	static public void bindTexture2D(int unit, int texId) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(unit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
	}

	static public int getCompressedSize(int texId) {
		bindTexture2D(texId);
		// IntBuffer ib = BufferUtils.createIntBuffer(1);
		// ib.clear();
		return GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL13.GL_TEXTURE_COMPRESSED_IMAGE_SIZE);
		/*
		 * GL11.glGetTexLevelParameter(GL11.GL_TEXTURE_2D, 0,
		 * GL13.GL_TEXTURE_COMPRESSED_IMAGE_SIZE, ib);
		 */

	}

	static public void deleteTexture(int texId) {

		GL11.glDeleteTextures(texId);
		exitOnGLError("deleteTexture");

	}
}
