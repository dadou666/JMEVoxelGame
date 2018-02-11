package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE1_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE2_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE3_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBGeometryShader4;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {


	public class ErrorCreationShader extends Error {
		public String s;

		ErrorCreationShader(String s) {
			this.s = s;
		}

		public String toString() {
			return s;
		}

	}

	int program;

	public void use() {
		ARBShaderObjects.glUseProgramObjectARB(program);
		OpenGLTools.exitOnGLError("Shader use");
	}

	String frag;
	String vert;
	String geo;

	public Shader(Class cls, String frag, String vert, String geo) {
		Integer vertShader = 0;
		Integer fragShader = 0;
		Integer geoShader = 0;
		frag = "/dadou/shader/" + frag;
		vert = "/dadou/shader/" + vert;
		if (geo != null) {
			geo = "/dadou/shader/" + geo;
		}
		this.frag = frag;
		this.vert = vert;
		this.geo = geo;
		glActiveTextureARB(GL_TEXTURE1_ARB);

		glActiveTextureARB(GL_TEXTURE2_ARB);

		glActiveTextureARB(GL_TEXTURE3_ARB);
		try {

			vertShader = createShader(cls, vert, ARBVertexShader.GL_VERTEX_SHADER_ARB);

			fragShader = createShader(cls, frag, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

			if (geo != null) {

				geoShader = createShader(cls, geo, ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB);

			}
		} catch (Throwable exc) {
			exc.printStackTrace();
			return;
		} finally {
			if (vertShader == 0 || fragShader == 0)
				throw new ErrorCreationShader("");
		}
		if (geo == null) {
			program = OpenGLTools.createProgramShader(vertShader, fragShader);
		} else {
			program = OpenGLTools.createProgramShader(vertShader, fragShader, geoShader);
		}

	}

	private int createShader(Class cls, String filename, int shaderType) throws Throwable {

		int shader = 0;
		try {
			String source = getSource(cls, filename);
			shader = OpenGLTools.createShader(source, shaderType);

			if (shader == 0)
				return 0;
		} catch (Throwable t) {

			Log.print("shader ", filename);
			throw t;
		}
		return shader;

	}

	public String getSource(Class cls, String name) throws Exception {
		if (cls == null) {
			return this.readFileAsString(name);
		}
		return this.readRessourceAsString(cls, name);
	}

	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj,
				ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private String readRessourceAsString(Class cls, String name) throws Exception {
		StringBuilder source = new StringBuilder();
		Exception exception = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(cls.getResourceAsStream(name), "UTF-8"));

			Exception innerExc = null;
			try {
				String line;
				while ((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch (Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch (Exception exc) {
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		} catch (Exception exc) {
			exception = exc;
		} finally {
			if (exception != null)
				throw exception;
		}

		return source.toString();
	}

	private String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();

		FileInputStream in = new FileInputStream(filename);

		Exception exception = null;

		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			Exception innerExc = null;
			try {
				String line;
				while ((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch (Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch (Exception exc) {
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		} catch (Exception exc) {
			exception = exc;
		} finally {
			try {
				in.close();
			} catch (Exception exc) {
				if (exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}

			if (exception != null)
				throw exception;
		}

		return source.toString();
	}

	public class ErrorUniform extends Error {

		public ErrorUniform(String s) {
			super(s);

		}

	}

	public int getIdx(String name) {

		int idx = ARBShaderObjects.glGetUniformLocationARB(program, name);
		if (idx == -1) {
			throw new ErrorUniform(name + " " + frag + "-" + vert);
		}
		return idx;

	}

	public int getIdxAttribute(String name) {

		int idx = GL20.glGetAttribLocation(program, name);
		if (idx == -1) {
			throw new ErrorUniform(name + " " + frag + "-" + vert);
		}
		return idx;

	}

	public void glUniformfARB(int idx, float value) {

		ARBShaderObjects.glUniform1fARB(idx, value);

	}

	public void glUniformmat4ARB(String name, FloatBuffer fb) {
		ARBShaderObjects.glUniformMatrix4ARB(getIdx(name), false, fb);
		// ARBShaderObjects.glUniform1fARB(getIdx(name), value);

	}

	public void glUniformmat3ARB(String name, FloatBuffer fb) {
		ARBShaderObjects.glUniformMatrix3ARB(getIdx(name), false, fb);
		// ARBShaderObjects.glUniform1fARB(getIdx(name), value);

	}

	public void glUniformfARB(int idx, float value1, float value2) {

		ARBShaderObjects.glUniform2fARB(idx, value1, value2);

	}

	public void glUniformfARB(int idx, float value1, float value2, float value3) {

		ARBShaderObjects.glUniform3fARB(idx, value1, value2, value3);

	}

	public void glUniformfARB(int idx, float value1, float value2, float value3, float value4) {

		ARBShaderObjects.glUniform4fARB(idx, value1, value2, value3, value4);

	}

	public void glUniformiARB(int idx, int value) {

		ARBShaderObjects.glUniform1iARB(idx, value);

	}

	public void glUniformiARB(int idx, int value1, int value2) {

		ARBShaderObjects.glUniform2iARB(idx, value1, value2);

	}

	public void glUniformiARB(int idx, int value1, int value2, int value3) {

		ARBShaderObjects.glUniform3iARB(idx, value1, value2, value3);

	}

	public void glUniformfARB(int idx, int value1, int value2, int value3, int value4) {

		ARBShaderObjects.glUniform4iARB(idx, value1, value2, value3, value4);

	}

	public void glUniformfARB(String name, float value) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform1fARB(idx, value);
		// OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformfARB(String name, float value1, float value2) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform2fARB(idx, value1, value2);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformfARB(String name, float value1, float value2, float value3) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform3fARB(idx, value1, value2, value3);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformfARB(String name, float value1, float value2, float value3, float value4) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform4fARB(idx, value1, value2, value3, value4);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformiARB(String name, int value) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform1iARB(idx, value);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformiARB(String name, int value1, int value2) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform2iARB(idx, value1, value2);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

	public void glUniformiARB(String name, int value1, int value2, int value3) {
		int idx = this.getIdx(name);
		ARBShaderObjects.glUniform3iARB(idx, value1, value2, value3);
		OpenGLTools.exitOnGLError("glUniformfARB");

	}

}
