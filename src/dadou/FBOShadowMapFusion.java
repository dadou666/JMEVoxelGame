package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE1_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE2_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE3_ARB;
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
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;

import dadou.graphe.GrapheLigne;

public class FBOShadowMapFusion {


	int framebufferID;
	int depthRenderBufferID;
	int shadowRenderBufferID;
	public int shadowTextureID;
	public int width;
	public int height;

	IntBuffer ib;
	public VBOTexture2D vboTest;
	public Shader shaderTest;


	static public void initShader(Shader s, Brique bt) {
		if (Game.rm ==Game.RenderMode.Shadow) {
		
			s.glUniformmat4ARB("DepthBiasMVP", bt.buffer);

		}
	}

	

	public void init(int width, int height, Game game) {
		// Bind the depth buffer
		this.width = width;
		this.height = height;
	

		this.createBuffer();
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("Framebuffer not complete!");
		} else {
			System.out.println("Framebuffer is complete!");
		}
		// ib.flip();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);



		OpenGLTools.exitOnGLError("setFrustum");
	
		shaderTest = new Shader(Shader.class, "differe/fusion_depth_buffer.frag", "differe/fusion_depth_buffer.vert",null);

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

		// Bind the normal render target
		glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, this.shadowRenderBufferID);
		glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_RGBA32F_ARB, width, height);
		glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_RENDERBUFFER_EXT,
				this.shadowRenderBufferID);

	}

	public void genererEtAttacherTexture() {

		// Generate and bind the OGL texture for positions
		shadowTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, shadowTextureID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F_ARB, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		// Attach the texture to the FBO
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, shadowTextureID, 0);

	}

	public void createBuffer() {

		framebufferID = glGenFramebuffersEXT();
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);

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





	public void activer() {

		
	
		
	
		

		OpenGLTools.exitOnGLError("matrix");
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, this.framebufferID);
		GL11.glViewport(0, 0, width, height);

		// glPushAttrib(GL_VIEWPORT_BIT);

		// Clear the render targets
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		// glActiveTextureARB(GL_TEXTURE0_ARB);
		glActiveTextureARB(GL_TEXTURE1_ARB);
		glActiveTextureARB(GL_TEXTURE2_ARB);
		// glActiveTextureARB(GL_TEXTURE3_ARB);
		// glEnable(GL_TEXTURE_2D);

		// Specify what to render an start acquiring
		// glViewport(0, 0, width, height);

		ib = BufferUtils.createIntBuffer(2);

		ib.put(new int[] { GL_COLOR_ATTACHMENT0_EXT });
		ib.flip();
		GL20.glDrawBuffers(ib);

	}

	public void desactiver() {

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
	

		// glPopAttrib();

	}

	public void fusionner(FBOShadowMap map1,FBOShadowMap map2) {
		// Projection setup
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, map1.shadowTextureID);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, map2.shadowTextureID);
		shaderTest.use();

		shaderTest.glUniformiARB("shadowTextureID1", 0);
		shaderTest.glUniformiARB("shadowTextureID2", 1);

		// shader.glUniformfARB("fx", fx);
		// shader.glUniformfARB("fy", fy);
		vboTest.bind();
		vboTest.dessiner();
		vboTest.unBindVAO();
		// Reset OpenGL state
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
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
		
		glDeleteTextures(this.shadowTextureID);
		glDeleteFramebuffersEXT(this.framebufferID);

		glDeleteRenderbuffersEXT(this.depthRenderBufferID);
		glDeleteRenderbuffersEXT(this.shadowRenderBufferID);

	}

}
