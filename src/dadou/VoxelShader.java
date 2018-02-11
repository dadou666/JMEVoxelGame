package dadou;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;

import dadou.Shader.ErrorUniform;

public class VoxelShader {

	public Shader shaderVoxelX;
	public Shader shaderVoxelY;
	public Shader shaderVoxelZ;
	public Vector3f NormalX = new Vector3f(1, 0, 0);
	public Vector3f NormalY = new Vector3f(0, 1, 0);
	public Vector3f NormalZ = new Vector3f(0, 0, 1);

	public VoxelShader() {

	}

	public void initShader(Brique bt, Shader shader, Vector3f Normal, VoxelShaderParam p) {
		shader.use();

		// shader.glUniformfARB("lightPos", 0, 0, 0);

		bt.updateShaderUniform(shader);
		p.initUniform(bt, shader);
		// shader.glUniformfARB("obscur",Game.obscurite);

	}

	public void initUniform(Brique bt, VoxelShaderParam p) {

		initShader(bt, shaderVoxelX, NormalX, p);
		if (p.habillage != null) {
			shaderVoxelX.glUniformiARB("habillage", 1);
		}

		FBOShadowMap.initShader(shaderVoxelX, bt);

		shaderVoxelX.glUniformmat3ARB("textureMat", Game.bufferTextureX);
		shaderVoxelX.glUniformmat3ARB("textureMatInv", Game.bufferTextureXInv);
		shaderVoxelX.glUniformfARB("deltat", bt.tex.fX, bt.tex.fY, bt.tex.fZ);

		initShader(bt, shaderVoxelY, NormalY, p);
		if (p.habillage != null) {
			shaderVoxelY.glUniformiARB("habillage", 1);
		}
		FBOShadowMap.initShader(shaderVoxelY, bt);

		shaderVoxelY.glUniformfARB("deltat", bt.tex.fX, bt.tex.fY, bt.tex.fZ);
		shaderVoxelY.glUniformmat3ARB("textureMat", Game.bufferTextureY);
		shaderVoxelY.glUniformmat3ARB("textureMatInv", Game.bufferTextureYInv);

		initShader(bt, shaderVoxelZ, NormalZ, p);
		if (p.habillage != null) {
			shaderVoxelZ.glUniformiARB("habillage", 1);
		}
		FBOShadowMap.initShader(shaderVoxelZ, bt);

		shaderVoxelZ.glUniformfARB("deltat", bt.tex.fX, bt.tex.fY, bt.tex.fZ);
		shaderVoxelZ.glUniformmat3ARB("textureMat", Game.bufferTextureZ);
		shaderVoxelZ.glUniformmat3ARB("textureMatInv", Game.bufferTextureZInv);

	}

}
