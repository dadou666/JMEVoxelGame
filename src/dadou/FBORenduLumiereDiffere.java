package dadou;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBDrawBuffers.*;
import static org.lwjgl.opengl.ARBMultitexture.*;
import static org.lwjgl.opengl.ARBTextureFloat.*;
import static org.lwjgl.opengl.EXTFramebufferObject.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL41;
import org.lwjgl.opengl.GL42;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLContext;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;

import dadou.ihm.Action;
import dadou.tools.BrickEditor;

public class FBORenduLumiereDiffere {

	int framebufferID;
	int depthRenderBufferID;

	int colorRenderBufferID;

	int shadowRenderBufferID;

	public VBOTexture2D vbo;

	public VBOTexture2D vboReset;
	public Game game;
	int depthTextureID;
	public int positionTextureID;
	int colorTextureID;
	int normalTextureID;
	public int shadowTextureID;
	IntBuffer ib;
	public int width;
	public int height;
	public Shader shader;

	public Shader shaderNormal;


	public Shader shaderReset;
	public LWJGLCamera cam;

	public void init() {

		shaderReset = new Shader(Shader.class, "differe/reset.frag", "differe/reset.vert", null);
		shaderNormal = new Shader(Shader.class, "differe/differe.frag", "differe/differe.vert", null);

		shader = shaderNormal;
		vbo = creerVBO(shader, true);

		vboReset = creerVBO(shaderReset, false);

	}

	public VBOTexture2D creerVBO(Shader shader, boolean textureCoord) {
		VBOTexture2D vbo = new VBOTexture2D(shader);
		vbo.addVertex(-1, -1, 0.00f);
		vbo.addVertex(-1, 1, 0.0f);
		vbo.addVertex(1, 1, 0.0f);
		vbo.addVertex(1, -1, 0.0f);
		if (textureCoord) {
			vbo.addCoordTexture2D(0, 0);
			vbo.addCoordTexture2D(0, 1);
			vbo.addCoordTexture2D(1, 1);
			vbo.addCoordTexture2D(1, 0);
		}
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		return vbo;

	}

	public void attacherBuffer() {
		// Bind the diffuse render target

		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.colorRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_RENDERBUFFER_EXT,
				colorRenderBufferID);

		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.shadowRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT1_EXT, GL_RENDERBUFFER_EXT,
				shadowRenderBufferID);

		// Bind the depth buffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.depthRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT32, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				this.depthRenderBufferID);

	}

	public void genererEtAttacherTexture() {
		colorTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		// Attach the texture to the FBO
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, this.colorTextureID, 0);

		shadowTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, shadowTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F_ARB, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		// glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA,
		// GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		// Attach the texture to the FBO
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT1_EXT, GL_TEXTURE_2D, this.shadowTextureID, 0);

	}

	public void init(int width, int height) {
		this.width = width;
		this.height = height;

		if (game != null) {
			this.cam = (LWJGLCamera) game.createCamera(width, height, true);
		}
		this.init();
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {

			System.exit(0);
		} else {

			// init our fbo

			framebufferID = glGenFramebuffersEXT();
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
			this.colorRenderBufferID = glGenRenderbuffersEXT();

			this.depthRenderBufferID = glGenRenderbuffersEXT();

			this.shadowRenderBufferID = glGenRenderbuffersEXT();

			this.attacherBuffer();
			this.genererEtAttacherTexture();

			// ib.put(new int[]
			// {GL_COLOR_ATTACHMENT0_EXT,GL_COLOR_ATTACHMENT1_EXT,GL_COLOR_ATTACHMENT2_EXT});
			if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
				System.out.println("Framebuffer not complete!");
			} else {
				System.out.println("Framebuffer is complete!");
			}
			// ib.flip();
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		}
	}

	public void initialiser(Camera cam) {
		this.cam.getLocation().set(cam.getLocation());
		this.cam.getDirection().set(cam.getDirection());
		this.cam.getLeft().set(cam.getLeft());
		this.cam.getUp().set(cam.getUp());
		this.cam.update();
		this.cam.apply();
	}

	public void activer(float r, float g, float b) {

		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.framebufferID);
		// glPushAttrib(GL_VIEWPORT_BIT);

		// Clear the render targets
		glClearColor(r, g, b, 1.0f);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// glActiveTextureARB(GL_TEXTURE0_ARB);
		glActiveTextureARB(GL_TEXTURE1_ARB);
		glActiveTextureARB(GL_TEXTURE2_ARB);
		glActiveTextureARB(GL_TEXTURE3_ARB);
		// glEnable(GL_TEXTURE_2D);

		// Specify what to render an start acquiring
		// glViewport(0, 0, width, height);

		ib = BufferUtils.createIntBuffer(4);

		// ib=IntBuffer.wrap(new int[]
		// {GL_COLOR_ATTACHMENT0_EXT,GL_COLOR_ATTACHMENT1_EXT,GL_COLOR_ATTACHMENT2_EXT});
		ib.put(new int[] { GL_COLOR_ATTACHMENT0_EXT, GL_COLOR_ATTACHMENT1_EXT, GL_COLOR_ATTACHMENT2_EXT,
				GL_COLOR_ATTACHMENT3_EXT });
		ib.flip();
		GL20.glDrawBuffers(ib);
		// GL43.glC
		// GL20.glDrawBuffers(GL_COLOR_ATTACHMENT1_EXT);
		// GL20.glDrawBuffers(GL_COLOR_ATTACHMENT0_EXT);
		/* GL20.glDrawBuffers(GL_COLOR_ATTACHMENT2_EXT); */
	}

	public void activer() {

		this.activer(1.0f, 1.0f, 1.0f);
	}

	public void desactiver() {

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
		// glPopAttrib();

	}

	public void resetFBO(float r, float g, float b) {

		shaderReset.use();
		shaderReset.glUniformfARB("color", r, g, b);
		vboReset.bind();
		vboReset.dessiner();
		vboReset.unBindVAO();
	}

	public void dessinerFBO(EffetEcran rond) {
		this.dessinerFBO(false, rond);

	}

	public Quaternion lightPos;

	public void dessinerFBO(boolean ignorerShadow, EffetEcran effetEcran) {
		// Projection setup

		if (effetEcran != null) {
			shader = effetEcran.shader();
		} else {
			shader = shaderNormal;
		}

		shader.use();

		if (effetEcran != null) {
			effetEcran.initShader(shader);
		}

		glActiveTextureARB(GL_TEXTURE1_ARB);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, this.colorTextureID);
		shader.glUniformiARB("colorTextureID", 1);

		glActiveTextureARB(GL_TEXTURE2_ARB);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, this.shadowTextureID);
		shader.glUniformiARB("shadowTextureID", 2);

		if (Game.aliasing) {
			shader.glUniformfARB("tdx", 1.0f / (float) width);
			shader.glUniformfARB("tdy", 1.0f / (float) height);
		} else {
			shader.glUniformfARB("tdx", 0.0f);
			shader.glUniformfARB("tdy", 0.0f);
		}
		shader.glUniformiARB("showEdge", Game.showEdge);
		shader.glUniformfARB("fogDensity", Game.fogDensity);
		shader.glUniformfARB("fogColor", Game.fogColor.x, Game.fogColor.y, Game.fogColor.z);
		vbo.shader = shader;
		vbo.bind();
		vbo.dessiner();
		vbo.unBindVAO();
		// Reset OpenGL state

		glDisable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);

		glActiveTextureARB(GL_TEXTURE1_ARB);
		glDisable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);

		glActiveTextureARB(GL_TEXTURE2_ARB);
		glDisable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, 0);

	}

	public void supprimer() {
		glDeleteTextures(this.normalTextureID);
		glDeleteTextures(this.positionTextureID);
		glDeleteTextures(this.colorTextureID);
		glDeleteTextures(this.shadowTextureID);
		glDeleteFramebuffersEXT(this.framebufferID);
		glDeleteRenderbuffersEXT(this.colorRenderBufferID);
		glDeleteRenderbuffersEXT(this.shadowRenderBufferID);
		glDeleteRenderbuffersEXT(this.depthRenderBufferID);

	}

	public byte[] getContent() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		ByteBuffer buff = ByteBuffer.allocateDirect(4 * width * height);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff);
		byte r[] = new byte[4 * width * height];
		buff.get(r);
		return r;

	}

}
