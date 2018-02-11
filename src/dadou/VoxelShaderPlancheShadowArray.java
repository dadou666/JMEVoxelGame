package dadou;

import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE4_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE5_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE6_ARB;
import static org.lwjgl.opengl.ARBMultitexture.GL_TEXTURE7_ARB;
import static org.lwjgl.opengl.ARBMultitexture.glActiveTextureARB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.lwjgl.opengl.GL13;

import com.jme.math.Vector3f;

import dadou.tools.BrickEditor;

public class VoxelShaderPlancheShadowArray extends VoxelShader {
	public void initUniform(Brique bt, VoxelShaderParam p) {

		initShader(bt, shaderVoxelX, NormalX, p);
		if (p.habillage != null) {
			shaderVoxelX.glUniformiARB("habillage", 1);
		}
		

		shaderVoxelX.glUniformmat3ARB("textureMat", Game.bufferTextureX);
		shaderVoxelX.glUniformmat3ARB("textureMatInv", Game.bufferTextureXInv);
		shaderVoxelX.glUniformfARB("deltat", bt.tex.fX,bt.tex.fY,bt.tex.fZ);
		
		initShader(bt, shaderVoxelY, NormalY, p);
		if (p.habillage != null) {
			shaderVoxelY.glUniformiARB("habillage", 1);
		}
	
		shaderVoxelY.glUniformmat3ARB("textureMat", Game.bufferTextureY);
		shaderVoxelY.glUniformmat3ARB("textureMatInv", Game.bufferTextureYInv);
		shaderVoxelY.glUniformfARB("deltat", bt.tex.fX,bt.tex.fY,bt.tex.fZ);
		initShader(bt, shaderVoxelZ, NormalZ, p);
		if (p.habillage != null) {
			shaderVoxelZ.glUniformiARB("habillage", 1);
		}
	
		shaderVoxelZ.glUniformmat3ARB("textureMat", Game.bufferTextureZ);
		shaderVoxelZ.glUniformmat3ARB("textureMatInv", Game.bufferTextureZInv);
		shaderVoxelZ.glUniformfARB("deltat", bt.tex.fX,bt.tex.fY,bt.tex.fZ);

	}
	public void initShader(Brique bt, Shader shader, Vector3f Normal, VoxelShaderParam p) {
		shader.use();

		bt.updateShaderUniform(shader);
		p.initUniform(bt, shader);

		shader.glUniformfARB("transparence", ObjetMobilePourModelInstance.renduTransparence);
	//	shader.glUniformfARB("fogDensity", Game.fogDensity);
	//	shader.glUniformfARB("fogColor", Game.fogColor.x, Game.fogColor.y, Game.fogColor.z);

/*if (Game.rm == Game.RenderMode.Shadow ) {
			glActiveTextureARB(GL_TEXTURE4_ARB);
			glBindTexture(GL_TEXTURE_2D, FBOShadowMap.shadowMap.shadowTextureID);
			OpenGLTools.exitOnGLError("bind test");
			if (Game.subPixelShadow && false) {
				glActiveTextureARB(GL_TEXTURE5_ARB);
				glActiveTextureARB(GL_TEXTURE6_ARB);
				glActiveTextureARB(GL_TEXTURE7_ARB);
				glBindTexture(GL13.GL_TEXTURE5, FBOShadowMap.shadowMap.shadowVertex1TextureID);
				glBindTexture(GL13.GL_TEXTURE6, FBOShadowMap.shadowMap.shadowVertex2TextureID);
				glBindTexture(GL13.GL_TEXTURE7, FBOShadowMap.shadowMap.shadowVertex3TextureID);

			}
			Vector3f cp = FBOShadowMap.shadowMap.cam.getLocation();

			shader.glUniformfARB("lightPos", cp.x, cp.y, cp.z);
			shader.glUniformiARB("shadowTexture", 4);
			if (Game.subPixelShadow && false) {
				shader.glUniformiARB("shadowVertex1Texture", 5);
				shader.glUniformiARB("shadowVertex2Texture", 6);
				shader.glUniformiARB("shadowVertex3Texture", 7);
			}
			shader.glUniformfARB("tex_dx", 1.0f / ((float) Game.shadowMapWidth));
			shader.glUniformfARB("tex_dy", 1.0f / ((float) Game.shadowMapHeight));

			shader.glUniformiARB("useShadow", 0);
		} else {*/
		//	shader.glUniformiARB("useShadow", 0);
		//}

		// shader.glUniformmat4ARB("modelView", BrickEditor.modelViewMatrix);

	}

	public VoxelShaderPlancheShadowArray() {

		String prefixe = "planche/texture_array/shadow/";
	

			shaderVoxelX = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);
			shaderVoxelY = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);
			shaderVoxelZ = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);
			
	
		}

}
