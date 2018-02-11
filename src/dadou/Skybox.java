package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE0_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class Skybox {
	public static boolean actif = true;

	Texture2D texRight;

	Texture2D texLeft;
	Texture2D texTop;
	Texture2D texBottom;
	Texture2D texBack;
	Texture2D texFront;

	VBOTexture2D vboRight;
	VBOTexture2D vboLeft;
	VBOTexture2D vboTop;
	VBOTexture2D vboBottom;
	VBOTexture2D vboBack;
	VBOTexture2D vboFront;

	int dimX;
	int dimY;

	float dimSkyBox;

	public Skybox(String nomBase) throws IOException {

		texRight = new Texture2D(ImageIO.read(new File(nomBase + "_Right.png")));

		texLeft = new Texture2D(ImageIO.read(new File(nomBase + "_Left.png")));

		texTop = new Texture2D(ImageIO.read(new File(nomBase + "_Top.png")));

		texBottom = new Texture2D(ImageIO.read(new File(nomBase + "_Bottom.png")));

		texFront = new Texture2D(ImageIO.read(new File(nomBase + "_Front.png")));

		texBack = new Texture2D(ImageIO.read(new File(nomBase + "_Back.png")));
		this.dimSkyBox = 1.0f;
		this.creerVBO();

	}

	public void dessiner(Camera cam,VBOTexture2D vbo, Texture2D tex,float dim) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.texId);
		GL11.glPushMatrix();
		Game.shaderSkyBox.use();
		vbo.bind();
		Game.shaderSkyBox.glUniformiARB("skybox", 0);
		Game.shaderSkyBox.glUniformfARB("dim", dim);
	//	Game.shaderSkyBox.glUniformfARB("fogDensity", Game.fogDensity);
	//	Game.shaderSkyBox.glUniformfARB("fogColor", Game.fogColor.x, Game.fogColor.y, Game.fogColor.z);

		Vector3f location =cam.getLocation();
		GL11.glTranslatef(location.x, location.y, location.z);
		vbo.dessiner();
		GL11.glPopMatrix();
		
		vbo.unBindVAO();

	}

	public void dessiner(Camera cam,float dim) {

		dessiner(cam,vboRight, texRight,dim);
		dessiner(cam,vboLeft, texLeft,dim);

		dessiner(cam,vboBack, texBack,dim);
		dessiner(cam,vboFront, texFront,dim);

		dessiner(cam,vboTop, texTop,dim);
		dessiner(cam,vboBottom, texBottom,dim);
	}

	public void creerVBO() {
		float dimSkyBox2 = dimSkyBox/ 2.0f;
		// Render the front quad
		VBOTexture2D vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboRight = vbo;
		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(1.0f, 1);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		

		// Render the left quad
		 vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboBack = vbo;

		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 1);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, dimSkyBox2);

		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		// Render the back quad
		vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboLeft = vbo;

		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 1);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, dimSkyBox2);

		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		// Render the right quad
		vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboFront = vbo;

		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(1, 1);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		// Render the top quad
		vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboTop = vbo;
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(-dimSkyBox2, dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 1);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(dimSkyBox2, dimSkyBox2, -dimSkyBox2);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		// Render the bottom quad
		vbo = new VBOTexture2D(Game.shaderSkyBox);
		vboBottom = vbo;
		vbo.addCoordTexture2D(0, 1);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addCoordTexture2D(0, 0);
		vbo.addVertex(-dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 0);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, dimSkyBox2);
		vbo.addCoordTexture2D(1, 1);
		vbo.addVertex(dimSkyBox2, -dimSkyBox2, -dimSkyBox2);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
	}
}
