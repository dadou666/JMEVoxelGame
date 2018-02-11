package dadou;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;

import java.awt.Color;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.ihm.Action;
import dadou.tools.BrickEditor;
import dadou.tools.ModelEditorSwing;

public class FBO {

	public int texId;
	int framebufferID;
	int depthRenderBufferID;
	public int width;
	public int height;
	public FBORenduLumiereDiffere fbo;

	public void init(int width, int height) {
		if (VoxelShaderColor.renduLumiereDiffere) {
			fbo = new FBORenduLumiereDiffere();
			fbo.init(width, height);
		}
		this.width = width;
		this.height = height;

		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
		
			System.exit(0);
		} else {

	

			// init our fbo

			framebufferID = glGenFramebuffersEXT();

			// create a new framebuffer
			texId = glGenTextures(); // and a new texture used as a
										// color buffer
			depthRenderBufferID = glGenRenderbuffersEXT(); // And finally a new
															// depthbuffer

			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); // switch
																		// to
																		// the
																		// new
																		// framebuffer

			// initialize color texture
			glBindTexture(GL_TEXTURE_2D, texId); // Bind the
													// colorbuffer
													// texture
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); // make
																				// it
																				// linear
																				// filterd
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null); // Create
																														// the
																														// texture
																														// data
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, texId, 0); // attach
																												// it
																												// to
																												// the
																												// framebuffer

			// initialize depth renderbuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind
																				// the
																				// depth
																				// renderbuffer
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get
																										// the
																										// data
																										// space
																										// for
																										// it
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT,
					depthRenderBufferID); // bind it to the renderbuffer

			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0); // Swithch back to
															// normal
															// framebuffer
															// rendering

		}
	}


	public void activer() {
		// LWJGLCamera lwglCamera =(LWJGLCamera) cam;
		// lwglCamera.doViewPortChange();// set The Current Viewport to the fbo
		// size

		glBindTexture(GL_TEXTURE_2D, 0); // unlink textures because if we dont
											// it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); // switch to
																	// rendering
																	// on our
																	// FBO

		glClearColor(1.0f, 0.0f, 1.0f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear Screen And
															// Depth Buffer on
															// the fbo to red
		// glLoadIdentity (); // Reset The Modelview Matrix
		// cam.apply();
		// GL11.glLoadIdentity();
		// GL11.glViewport (0, 0, width, height);
	}

	public void desactiver() {

		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);

	}

	public byte[] getContent() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		ByteBuffer buff = ByteBuffer.allocateDirect(4 * width * height);
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buff);
		byte r[] = new byte[4 * width * height];
		buff.get(r);
		return r;

	}

	public void renderDebug(Action action) {

		this.activer();

		action.execute();

		this.desactiver();
	}

	public void render(Action action) {
		if (fbo != null) {
			fbo.activer();

			GL11.glViewport(0, 0, fbo.width, fbo.height);
			fbo.resetFBO(red, green, blue);
			action.execute();

			fbo.desactiver();

		}

		this.activer();

		if (fbo != null) {
			fbo.dessinerFBO(true,null);
		} else {
			action.execute();
		}
		this.desactiver();
	}
	
	float red;
	float green;
	float blue;
	Vector3f direction = new Vector3f();
	Vector3f up = new Vector3f();
	Vector3f left = new Vector3f();

	Vector3f location = new Vector3f();

	public Icone creerIconne(ModelClasse mc, float theta, float phi, float distance, Camera cam, Color couleurFond) {
		this.render(() -> {
			Game.rm =Game.RenderMode.Normal;
			direction.set(cam.getDirection());
			up.set(cam.getUp());
			left.set(cam.getLeft());
			location.set(cam.getLocation());

			Quaternion rotationX = new Quaternion();
			rotationX.fromAngleNormalAxis(theta, Vector3f.UNIT_X);
			Quaternion rotationY = new Quaternion();
			rotationY.fromAngleNormalAxis(phi, Vector3f.UNIT_Y);
			Quaternion tmp = new Quaternion();
			tmp.set(rotationY);
			tmp.multLocal(rotationX);
			cam.setAxes(tmp);
			cam.setLocation(cam.getDirection().mult(-distance));

			cam.update();
			cam.apply();
			GL11.glViewport(0, 0, width, height);
			red = (float) couleurFond.getRed() / 255.0f;
			green = (float) couleurFond.getGreen() / 255.0f;
			blue = (float) couleurFond.getBlue() / 255.0f;
			//GL11.glClearColor(red, green, blue, 0.5f);
			glClear( GL_DEPTH_BUFFER_BIT);
			Objet3D translation = new Objet3D();

			BriqueAvecTexture3D brique = mc.vbo.creerBriqueAvecTexture3D(mc.tex);
			OpenGLTools.exitOnGLError("creerBriqueAvecTexture3D ");
			translation.brique = brique;
			brique.echelle = 1.0f;
			brique.tpos.set(0, 0, 0);
			Vector3f size = new Vector3f();
			size.set(mc.size);

			Vector3f t = size.mult(-0.5f);
			translation.translation(t);
			translation.dessiner(cam);
			Game.rm =Game.etatOmbre.renderMode();

	

		});

		return new Icone(width, height, getContent());

	}

	public Icone creerIconne(Objet3D om, Game g, float theta, float phi, float distance, Color couleurFond) {
		Camera cam = g.getCamera();
		direction.set(cam.getDirection());
		up.set(cam.getUp());
		left.set(cam.getLeft());
		location.set(cam.getLocation());
		Game.rm = Game.RenderMode.Normal;
		Game.aliasing = false;
		this.render(() -> {
	
		
			Quaternion rotationX = new Quaternion();
			rotationX.fromAngleNormalAxis(theta, Vector3f.UNIT_X);
			Quaternion rotationY = new Quaternion();
			rotationY.fromAngleNormalAxis(phi, Vector3f.UNIT_Y);
			Quaternion tmp = new Quaternion();
			tmp.set(rotationY);
			tmp.multLocal(rotationX);
			cam.setAxes(tmp);
			cam.setLocation(cam.getDirection().mult(-distance));

			cam.update();
			cam.apply();
			GL11.glViewport(0, 0, width, height);
			red = (float) couleurFond.getRed() / 255.0f;
			green = (float) couleurFond.getGreen() / 255.0f;
			blue = (float) couleurFond.getBlue() / 255.0f;
			GL11.glClearColor(red, green, blue, 0.5f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			Vector3f dir = cam.getDirection();
			Vector3f pos = cam.getLocation();
			// System.out.println(" pos=" + pos + "dir=" + dir);

		/*	g.setLight0Pos(pos.x, pos.y, pos.z);
			g.setLight1Pos(0, 0, 0);
			g.setLightDirection(GL11.GL_LIGHT0, dir.x, dir.y, dir.z);*/

			om.dessiner(cam);
			


		});
		Game.rm = Game.etatOmbre.renderMode();
		Game.aliasing = true;
		cam.getLocation().set(location);
		cam.getDirection().set(direction);
		cam.getUp().set(up);
		cam.getLeft().set(left);
		cam.update();
		cam.apply();
		return new Icone(width, height, getContent());

	}

	public void creerImageEcranPourDecor(BrickEditor be, String nom) {

		be.decor.DecorDeBriqueData.imageEcran = this.creerImage(be);
		if (nom == null) {
			nom=		(BrickEditor.cheminRessources+"/img.png");
		}
		be.decor.DecorDeBriqueData.imageEcran.save(nom);

	}
	public ImageEcran creerImage(BrickEditor be) {
		this.render(() -> {

			GL11.glViewport(0, 0, width, height);

			GL11.glClearColor(0, 0, 0, 0.5f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
					try {
						be.afficherDecor();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
		});

		ImageEcran ie = new ImageEcran(width, height, getContent());
		return ie;
		//ie.save("e:/test.png");

	}

	public Icone creerIconeEcran(BrickEditor be) {
		this.render(() -> {

			GL11.glViewport(0, 0, width, height);

			GL11.glClearColor(0, 0, 0, 0.5f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			try {
				be.afficherDecor();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Icone ie = new Icone(width, height, getContent());
		return ie;
		//ie.save("e:/test.png");

	}

	public void supprimer() {
		glDeleteTextures(this.texId);

		glDeleteFramebuffersEXT(this.framebufferID);
		glDeleteRenderbuffersEXT(this.depthRenderBufferID);

	}
}
