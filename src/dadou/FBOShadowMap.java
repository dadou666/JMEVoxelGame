package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE1_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE2_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE3_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE4_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE5_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE6_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE7_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;
import static org.lwjgl.opengl.ARBTextureFloat.GL_RGBA16F_ARB;
import static org.lwjgl.opengl.ARBTextureFloat.GL_RGBA32F_ARB;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT1_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT2_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT3_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glDeleteRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;

import dadou.graphe.GrapheLigne;

public class FBOShadowMap {

	public static FBOShadowMap shadowMap;
	int framebufferID;
	int depthRenderBufferID;

	public int shadowTextureID;

	public int shadowVertex1TextureID;
	public int shadowVertex2TextureID;
	public int shadowVertex3TextureID;

	public int width;
	public int height;
	public Vector3f worldLighPos = new Vector3f();
	IntBuffer ib;
	public VBOTexture2D vboTest;
	public Shader shaderTest;
	public Camera cam;

	public Matrix4f projection = new Matrix4f();
	public Matrix4f model = new Matrix4f();
	public Matrix4f view = new Matrix4f();

	Shader shaderLine;

	public Matrix4f tmp = new Matrix4f();
	public Matrix3f tmp3 = new Matrix3f();

	static public void initShader(Shader s, Brique bt) {
		if (Game.rm == Game.RenderMode.Shadow) {

			s.glUniformmat4ARB("DepthBiasMVP", bt.buffer);

		}
	
			if (Game.rm == Game.RenderMode.Shadow || (Game.rm == Game.RenderMode.Depth)) {
				s.glUniformmat4ARB("transformationMat", bt.transformationBuffer);
			}

		
	}

	public void loadMatrix(FloatBuffer buffer, boolean onlyModel) {

		buffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
		buffer.rewind();
		model.readFloatBuffer(buffer);

		// tmp.set(bias);

		tmp.loadIdentity();
		tmp.multLocal(model);
		if (!onlyModel) {
			tmp.multLocal(view);
			tmp.multLocal(projection);

		}
		tmp.fillFloatBuffer(buffer);
		/*
		 * tmp.invertLocal(); tmp.transposeLocal(); tmp.toRotationMatrix(tmp3);
		 * tmp3.fillFloatBuffer(normalBuffer);
		 */

	}

	public void glDrawBuffers() {



			ib = BufferUtils.createIntBuffer(1);

			ib.put(new int[] { GL_COLOR_ATTACHMENT0_EXT });
			ib.flip();
		
		// GL20.GL_DRAW_BUFFER0
		GL20.glDrawBuffers(ib);
		Log.print("Max buffers=", GL11.glGetInteger(GL20.GL_MAX_DRAW_BUFFERS));
		OpenGLTools.exitOnGLError("KO");
		// GL20.glDrawBuffers(ib);
	}

	public void init(int width, int height, Game game) {
		// Bind the depth buffer
		this.width = width;
		this.height = height;

		this.createBuffer();

		this.cam = game.createCamera(width, height, false);

		OpenGLTools.exitOnGLError("setFrustum");
		shaderTest = new Shader(Shader.class, "differe/test_depth_buffer.frag", "differe/test_depth_buffer.vert", null);
		shaderLine = new Shader(Shader.class, "base_sans_vbo.frag", "base_sans_vbo.vert", null);
		vboTest = new VBOTexture2D(shaderTest);
		vboTest.addVertex(-1, -1, 0.00f);
		vboTest.addVertex(-1, 1, 0.0f);
		vboTest.addVertex(1, 1, 0.0f);
		vboTest.addVertex(1, -1, 0.0f);

		vboTest.addCoordTexture2D(0, 0);
		vboTest.addCoordTexture2D(0, 1);
		vboTest.addCoordTexture2D(1, 1);
		vboTest.addCoordTexture2D(1, 0);
		vboTest.addTri(0, 1, 2);
		vboTest.addTri(0, 2, 3);
		vboTest.createVBO();

	}

	public void attacherBuffer() {
		// Bind the diffuse render target

		// Bind the depth buffer
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.depthRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT32, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
				this.depthRenderBufferID);

	}

	public void genererEtAttacherTexture(int tex[], int attachements[]) {
		for (int i = 0; i < tex.length; i++) {
			glBindTexture(GL_TEXTURE_2D, tex[i]);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F_ARB, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, attachements[i], GL_TEXTURE_2D, tex[i], 0);

		}

	}

	public void genererEtAttacherTexture() {

		// Generate and bind the OGL texture for positions
		int tex[];
		int attachements[];


	

			shadowVertex1TextureID = glGenTextures();

		//	shadowVertex2TextureID = glGenTextures();

			//shadowVertex3TextureID = glGenTextures();
			attachements = new int[] { GL_COLOR_ATTACHMENT0_EXT };
//			attachements = new int[] { GL_COLOR_ATTACHMENT0_EXT, GL_COLOR_ATTACHMENT1_EXT, GL_COLOR_ATTACHMENT2_EXT					 };
	//		tex = new int[] {  shadowVertex1TextureID, shadowVertex2TextureID, shadowVertex3TextureID };
			tex = new int[] {  shadowVertex1TextureID };

		
		this.genererEtAttacherTexture(tex, attachements);
	}

	public void createBuffer() {

		framebufferID = glGenFramebuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

		this.depthRenderBufferID = glGenRenderbuffersEXT();

	//if (!Game.subPixelShadow) {
		this.attacherBuffer();
	//}
		// glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		this.genererEtAttacherTexture();
		glDrawBuffers();

		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("Framebuffer not complete!");
		} else {
			System.out.println("Framebuffer is complete!");
		}
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	}

	public void activer(ShadowData sd) {

		

		this.cam.getDirection().set(sd.direction);
		this.cam.getLocation().set(sd.location);
		this.cam.getLeft().set(sd.left);
		this.cam.getUp().set(sd.up);
		LWJGLCamera lwjglCam = (LWJGLCamera) cam;
		this.cam.update();
		this.cam.apply();

		this.view = lwjglCam.getModelViewMatrix();
		this.projection = lwjglCam.getProjectionMatrix();

		OpenGLTools.exitOnGLError("matrix");
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.framebufferID);
		// glBindFramebufferEXT(GL30.GL_DRAW_FRAMEBUFFER,
		// this.shadowRenderBufferID);
		GL11.glViewport(0, 0, width, height);

		// glPushAttrib(GL_VIEWPORT_BIT);

		// Clear the render targets
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		/*
		 * int GL_CONSERVATIVE_RASTERIZATION_NV = 0x9346;
		 * Log.print("GL_CONSERVATIVE_RASTERIZATION_NV"+glIsEnabled(
		 * GL_CONSERVATIVE_RASTERIZATION_NV));
		 * glEnable(GL_CONSERVATIVE_RASTERIZATION_NV);
		 * Log.print("GL_CONSERVATIVE_RASTERIZATION_NV"+glIsEnabled(
		 * GL_CONSERVATIVE_RASTERIZATION_NV));
		 */
		// glActiveTextureARB(GL_TEXTURE0_ARB);
		glActiveTextureARB(GL_TEXTURE1_ARB);
		glActiveTextureARB(GL_TEXTURE2_ARB);
		glActiveTextureARB(GL_TEXTURE3_ARB);
		glActiveTextureARB(GL_TEXTURE4_ARB);
		glActiveTextureARB(GL_TEXTURE5_ARB);
		glActiveTextureARB(GL_TEXTURE6_ARB);
		glActiveTextureARB(GL_TEXTURE7_ARB);

	}

	public byte[] getContent(int texId) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		ByteBuffer buff = ByteBuffer.allocateDirect(4 * 4 * width * height);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff);
		byte r[] = new byte[4 * 4 * width * height];
		buff.get(r);
		return r;

	}

	public void desactiver() {

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

		shadowMap = this;


	}

	public void supprimer() {

	//	glDeleteTextures(this.shadowTextureID);
		glDeleteTextures(this.shadowVertex1TextureID);
		glDeleteFramebuffersEXT(this.framebufferID);

		glDeleteRenderbuffersEXT(this.depthRenderBufferID);

	}
	public void finalize() {
		
		
		supprimer();
	
	}

}
