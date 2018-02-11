package dadou;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

import com.jme.math.Vector3f;

public class BriqueAvecTexture3D extends Brique {

	public Vector3f tpos = new Vector3f(0, 0, 0);

	public BriqueAvecTexture3D(VBOBriqueTexture3D vbo, VoxelTexture3D tex) {

		this.tex = tex;
		vs = vbo.vsp;
		this.vbo = vbo;

	}

	public void updateShaderUniform(Shader shader) {
		// System.out.println(" tpos= "+tpos);
		shader.glUniformfARB("echelle", echelle);
		if (Game.rm != Game.RenderMode.Depth) {
			shader.glUniformfARB("couleurfactor", couleurFactor);
		}
		// shader.glUniformfARB("lumieres[0]",)

		shader.glUniformfARB("tp", tpos.x, tpos.y, tpos.z);

	}

}
