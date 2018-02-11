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

import dadou.Shader.ErrorUniform;
import dadou.tools.BrickEditor;

public class VoxelShaderShadowArray extends VoxelShader {

	public void initShader(Brique bt, Shader shader, Vector3f Normal,
			VoxelShaderParam p) {
		shader.use();

		bt.updateShaderUniform(shader);
		p.initUniform(bt, shader);
		LumiereBuffer lb = LumiereBuffer.valeur();
		ZoneBuffer zb = ZoneBuffer.valeur();

		if (Game.rm == Game.RenderMode.Shadow) {
			for (int i = 0; i < lb.nombre; i++) {
				Lumiere l = lb.elements[i];
				float x = l.pos.x;
				float y = l.pos.y;
				float z = l.pos.z;
				float w = l.rayon;
				shader.glUniformfARB(lb.noms[i], x, y, z, w);

			}
			for (int i = 0; i < zb.nombre; i++) {
				Zone l = zb.elements[i];
				float x = l.pos.x;
				float y = l.pos.y;
				float z = l.pos.z;
				float w = l.rayon;
				shader.glUniformfARB(zb.noms[i], x, y, z, w);

			}
			shader.glUniformiARB("nbLumiere", lb.nombre);
			shader.glUniformiARB("nbZone", zb.nombre);
			shader.glUniformfARB("colorZone", ZoneBuffer.colorZone.x,ZoneBuffer.colorZone.y,ZoneBuffer.colorZone.z);
		} else {

			shader.glUniformiARB("nbLumiere", 0);
		}
		shader.glUniformfARB("transparence",
				ObjetMobilePourModelInstance.renduTransparence);
		shader.glUniformmat4ARB("modelView", BrickEditor.modelViewMatrix);
		shader.glUniformfARB("obscur", Game.obscurite);
		

		if (Game.rm == Game.RenderMode.Shadow) {

			glActiveTextureARB(GL_TEXTURE4_ARB);
			glBindTexture(GL_TEXTURE_2D, FBOShadowMap.shadowMap.shadowTextureID);
			
				glActiveTextureARB(GL_TEXTURE5_ARB);
				glBindTexture(GL_TEXTURE_2D,
						FBOShadowMap.shadowMap.shadowVertex1TextureID);

			
			OpenGLTools.exitOnGLError("bind test");
			Vector3f cp = FBOShadowMap.shadowMap.cam.getLocation();
			Vector3f dir = FBOShadowMap.shadowMap.cam.getDirection();
			shader.glUniformfARB("lightPos", cp.x, cp.y, cp.z);
			float cst = cp.dot(dir);
			shader.glUniformfARB("planCam", dir.x, dir.y, dir.z,cst);

			shader.glUniformiARB("shadowVertex1Texture", 5);

			shader.glUniformfARB("tex_dx", 1.0f / ((float) Game.shadowMapWidth));
			shader.glUniformfARB("tex_dy",
					1.0f / ((float) Game.shadowMapHeight));

			shader.glUniformiARB("useShadow", 1);
		} else {
			glActiveTextureARB(GL_TEXTURE4_ARB);
			if (FBOShadowMap.shadowMap != null) {
				glBindTexture(GL_TEXTURE_2D,
						FBOShadowMap.shadowMap.shadowTextureID);
			}

			shader.glUniformiARB("shadowVertex1Texture", 4);
			shader.glUniformiARB("useShadow", 0);
		}
		

	}

	public VoxelShaderShadowArray() {

		String prefixe = "texture_array/shadow/sp/";

		

		shaderVoxelX = new Shader(BriqueAvecTexture3D.class, prefixe
				+ "voxel.frag", prefixe + "voxel.vert", null);
		shaderVoxelY = new Shader(BriqueAvecTexture3D.class, prefixe
				+ "voxel.frag", prefixe + "voxel.vert", null);
		shaderVoxelZ = new Shader(BriqueAvecTexture3D.class, prefixe
				+ "voxel.frag", prefixe + "voxel.vert", null);

	}

}
