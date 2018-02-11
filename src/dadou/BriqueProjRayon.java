package dadou;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class BriqueProjRayon extends VBOTexture2D {
	public VBOTexture2D vbo;
	public VoxelTexture3D tex;
	public Shader shader;
	public float echelle = 1.0f;
	public static final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
	public static final Matrix4f modelViewMatrix = new Matrix4f();
	public static final Matrix3f rotationMatrix = new Matrix3f();
	public static final FloatBuffer rotation = BufferUtils.createFloatBuffer(9);

	public BriqueProjRayon(VoxelTexture3D vt, Shader shader) {
		super(shader);
		this.tex =vt;
		this.creerVBO();

	}

	public void calculer() {
		modelView.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
		modelView.rewind();
		modelViewMatrix.readFloatBuffer(modelView);

		modelViewMatrix.toRotationMatrix(rotationMatrix);
		rotationMatrix.invertLocal();
		rotationMatrix.fillFloatBuffer(rotation);

	}

	public void initShader(Shader s) {
		this.calculer();
		s.glUniformfARB("echelle", echelle);
		s.glUniformfARB("dim", this.tex.dimX, this.tex.dimY, this.tex.dimZ);
		s.glUniformmat3ARB("rotationInverse", rotation);
s.glUniformiARB("texture", 0);
		

	}

	public void creerVBO() {
	
		int i = 0;
		float dimX = (float) this.tex.dimX;
		float dimY = (float) this.tex.dimY;
		float dimZ = (float) this.tex.dimZ;

		addNormal(0, 0, 1);
		addVertex(0, 0, 0);// 0
		// //addCoordTexture2D(0, 0);

		addNormal(0, 0, 1);
		addVertex(0, dimY, 0);// 1
		//// addCoordTexture2D(0, 1);

		addNormal(0, 0, 1);
		addVertex(dimX, dimY, 0);// 2
		// addCoordTexture2D(1, 1);

		addNormal(0, 0, 1);
		addVertex(dimX, 0, 0);// 3
		// addCoordTexture2D(1, 0);
		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		addNormal(0, 0, -1);
		addVertex(0, 0, dimZ);// 0
		// addCoordTexture2D(0, 0);

		addNormal(0, 0, -1);
		addVertex(0, dimY, dimZ);// 1
		// addCoordTexture2D(0, 1);

		addNormal(0, 0, -1);
		addVertex(dimX, dimY, dimZ);// 2
		// addCoordTexture2D(1, 1);

		addNormal(0, 0, -1);
		addVertex(dimX, 0, dimY);// 3
		// addCoordTexture2D(1, 0);
		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		addNormal(1, 0, 0);
		addVertex(0, 0, 0);// 0
		// addCoordTexture2D(0, 0);

		addNormal(1, 0, 0);
		addVertex(0, 0, dimZ);// 1
		// addCoordTexture2D(0, 1);

		addNormal(1, 0, 0);
		addVertex(0, dimY, dimZ);// 2
		// addCoordTexture2D(1, 1);

		addNormal(1, 0, 0);
		addVertex(0, dimY, 0);// 3
		// addCoordTexture2D(1, 0);
		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		addNormal(-1, 0, 0);
		addVertex(dimX, 0, 0);// 0
		// addCoordTexture2D(0, 0);

		addNormal(-1, 0, 0);
		addVertex(dimX, 0, dimZ);// 1
		// addCoordTexture2D(0, 1);

		addNormal(-1, 0, 0);
		addVertex(dimX, dimY, dimZ);// 2
		// addCoordTexture2D(1, 1);

		addNormal(-1, 0, 0);
		addVertex(dimX, dimY, 0);// 3
		// addCoordTexture2D(1, 0);
		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		addNormal(0, 1, 0);
		addVertex(0, 0, 0);// 0
		// addCoordTexture2D(0, 0);

		addNormal(0, 1, 0);
		addVertex(0, 0, dimZ);// 1
		// addCoordTexture2D(0, 1);

		addNormal(0, 1, 0);
		addVertex(dimX, 0, dimZ);// 2
		// addCoordTexture2D(1, 1);

		addNormal(0, 1, 0);
		addVertex(dimX, 0, 0);// 3
		// addCoordTexture2D(1, 0);
		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		addNormal(0, -1, 0);
		addVertex(0, dimY, 0);// 0
		// addCoordTexture2D(0, 0);

		addNormal(0, -1, 0);
		addVertex(0, dimY, dimZ);// 1
		// addCoordTexture2D(0, 1);

		addNormal(0, -1, 0);
		addVertex(dimX, dimY, dimZ);// 2
		// addCoordTexture2D(1, 1);

		addNormal(0, -1, 0);
		addVertex(dimX, dimY, 0);// 3
		// addCoordTexture2D(1, 0);

		addTri(i, i + 1, i + 2);
		addTri(i, i + 2, i + 3);
		i += 4;

		super.createVBO();

	}
	public void dessiner(Shader s) {
		tex.bindTexture();
		super.dessiner(s);
		
	}

}
