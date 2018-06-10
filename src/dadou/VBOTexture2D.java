package dadou;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.graphe.GrapheLigne;
import dadou.ihm.Action;

public class VBOTexture2D implements Element3D {

	List<VBOTexture2DElement> elements = new ArrayList<VBOTexture2DElement>();
	List<VBOTriangle> triangles = new ArrayList<VBOTriangle>();
	public boolean useVBO = false;
	float[] vertexArray;
	float[] coord2DTexture;
	float[] normalArray;
	public boolean normalActif = true;

	private int vaoId = 0;
	private int vertex_vboId = 0;
	private int coordTexture2D_vboId = 0;

	private int indice_vboId = 0;
	private int normal_vboId = 0;
	int idxVertexArray = 0;
	int idxCoord2DTexture = 0;
	int idxNormalArray = 0;
	int idxIndice = 0;
	int attribute_normal = 0;
	int[] indiceArray;
	Shader shader;

	public VBOTexture2D(Shader shader) {

		this.shader = shader;

	}

	public void addVertex(float x, float y, float z) {

		VBOTexture2DElement elt = this.getVBOTexture2DElement(idxVertexArray);
		idxVertexArray++;
		elt.position.set(x, y, z);

	}
	public int addVertex(Vector3f p) {

		VBOTexture2DElement elt = this.getVBOTexture2DElement(idxVertexArray);
		int r = idxVertexArray;
		idxVertexArray++;
		elt.position.set(p.x,p.y, p.z);
		return r;

	}

	public int addCoordTexture2D(float x, float y) {
		VBOTexture2DElement elt = this
				.getVBOTexture2DElement(idxCoord2DTexture);
		int r = idxCoord2DTexture;
		idxCoord2DTexture++;
		elt.positionTex.set(x, y);
		return r;

	}

	public void addNormal(float x, float y, float z) {
		if (!this.normalActif) {
			return;
		}
		VBOTexture2DElement elt = this.getVBOTexture2DElement(idxNormalArray);
		elt.normal.set(x, y, z);
		idxNormalArray++;

	}

	public void addTri(int a, int b, int c) {
		VBOTriangle tri = new VBOTriangle();
		tri.a = a;
		tri.b = b;
		tri.c = c;
		this.triangles.add(tri);

	}

	public int getIdx(String nameAttribute) {
		int attribute = GL20.glGetAttribLocation(shader.program, nameAttribute);
		if (attribute == -1) {
			throw new Error(" pas trouvé " + nameAttribute);
		}
		return attribute;
	}

	public void createArrays() {
		vertexArray = new float[elements.size() * 3];
		if (idxNormalArray > 0) {
			normalArray = new float[elements.size() * 3];
		}
		if (idxCoord2DTexture > 0) {
			coord2DTexture = new float[elements.size() * 2];
		}
		for (int i = 0; i < elements.size(); i++) {
			VBOTexture2DElement elt = elements.get(i);
			vertexArray[3 * i] = elt.position.x;
			vertexArray[3 * i + 1] = elt.position.y;
			vertexArray[3 * i + 2] = elt.position.z;

			if (coord2DTexture != null) {
				coord2DTexture[2 * i] = elt.positionTex.x;

				coord2DTexture[2 * i + 1] = elt.positionTex.y;
			}
			if (normalArray != null) {
				normalArray[3 * i] = elt.normal.x;
				normalArray[3 * i + 1] = elt.normal.y;
				normalArray[3 * i + 2] = elt.normal.z;
			}

		}
		indiceArray = new int[triangles.size() * 3];
		for (int i = 0; i < triangles.size(); i++) {
			VBOTriangle tri = triangles.get(i);
			indiceArray[3 * i] = tri.a;
			indiceArray[3 * i + 1] = tri.b;
			indiceArray[3 * i + 2] = tri.c;

		}

	}

	public VBOTexture2DElement getVBOTexture2DElement(int idx) {
		VBOTexture2DElement elt;
		if (elements.size() == idx) {
			elt = new VBOTexture2DElement();
			elements.add(elt);
			return elt;

		}
		elt = elements.get(idx);
		if (elt == null) {
			elt = new VBOTexture2DElement();
			elements.set(idx, elt);
		}
		return elt;

	}

	int attribute_coord3d_texture = 0;

	int attribute_vertex = 0;

	public void createVBO() {
		this.createArrays();
		FloatBuffer verticesBuffer = BufferUtils
				.createFloatBuffer(this.vertexArray.length);
		verticesBuffer.put(vertexArray);
		verticesBuffer.flip();
		FloatBuffer coordTexture3DBuffer = null;
		if (coord2DTexture != null) {
			coordTexture3DBuffer = BufferUtils
					.createFloatBuffer(this.coord2DTexture.length);
			coordTexture3DBuffer.put(coord2DTexture);
			coordTexture3DBuffer.flip();
		}
		FloatBuffer normalsBuffer = null;
		if (normalArray != null) {
			normalsBuffer = BufferUtils
					.createFloatBuffer(this.normalArray.length);
			normalsBuffer.put(normalArray);
			normalsBuffer.flip();

		}
		IntBuffer indicesBuffer = BufferUtils
				.createIntBuffer(this.indiceArray.length);
		indicesBuffer.put(indiceArray);
		indicesBuffer.flip();

		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		attribute_vertex = getIdx("vertex");
		if (normalsBuffer != null) {
			attribute_normal = getIdx("normal");
		}
		if (coordTexture3DBuffer != null) {
			attribute_coord3d_texture = getIdx("coordTexture2D");
		}
		this.vertex_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertex_vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer,
				GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(attribute_vertex, 3, GL11.GL_FLOAT, false,
				0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		if (coordTexture3DBuffer != null) {
			this.coordTexture2D_vboId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, coordTexture2D_vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, coordTexture3DBuffer,
					GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(attribute_coord3d_texture, 2,
					GL11.GL_FLOAT, false, 0, 0);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
		if (normalsBuffer != null) {
			this.normal_vboId = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normal_vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer,
					GL15.GL_STATIC_DRAW);
			// Put the VBO in the attributes list at index 0
			GL20.glVertexAttribPointer(attribute_normal, 3, GL11.GL_FLOAT,
					false, 0, 0);
			// Deselect (bind to 0) the VBO
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind)
		this.indice_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indice_vboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer,
				GL15.GL_STATIC_DRAW);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		useVBO = true;
		this.triangles = null;
		this.coord2DTexture = null;
		this.elements = null;
		this.vertexArray = null;

	}

	public void bind() {

		GL30.glBindVertexArray(vaoId);
		if (idxCoord2DTexture > 0) {
			GL20.glEnableVertexAttribArray(this.attribute_coord3d_texture);
		}
		if (idxNormalArray > 0) {
			GL20.glEnableVertexAttribArray(this.attribute_normal);

		}
		GL20.glEnableVertexAttribArray(this.attribute_vertex);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indice_vboId);

	}

	public void unBindVAO() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(attribute_coord3d_texture);

		GL20.glDisableVertexAttribArray(attribute_vertex);
		GL30.glBindVertexArray(0);

	}

	public boolean drawLine = false;
	public int widthLine = 1;
	Action action;
	
	public void initShader(Shader s) {
		if (action != null) {
			action.execute();
		}
		
	}
	public void dessiner(Shader s) {
		
		s.use();
		this.initShader(s);
		if (drawLine) {
			if (useVBO) {
				GL11.glLineWidth(widthLine);
				GL11.glDrawElements(GL11.GL_LINE_LOOP, this.indiceArray.length,
						GL11.GL_UNSIGNED_INT, 0);
			} else {
				GL11.glPushMatrix();
				GL11.glLineWidth(widthLine);
				for (VBOTriangle tri : this.triangles) {

					Vector3f a = this.elements.get(tri.a).position;
					Vector3f b = this.elements.get(tri.b).position;
					Vector3f c = this.elements.get(tri.c).position;
					
					GL11.glBegin(GL11.GL_LINE_LOOP);

					GL11.glVertex3f(a.x, a.y, a.z);
					GL11.glVertex3f(b.x, b.y, b.z);
					GL11.glVertex3f(c.x, c.y, c.z);

					GL11.glEnd();

				}
				GL11.glPopMatrix();

			}
		} else {
			GL11.glDrawElements(GL11.GL_TRIANGLES, this.indiceArray.length,
					GL11.GL_UNSIGNED_INT, 0);
		}
	}
	public void dessiner() {

	this.dessiner(shader);
	}

	public void delete() {

		GL20.glDisableVertexAttribArray(0);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vertex_vboId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.indice_vboId);

		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);

	}
	public void finalize() {
		this.delete();
	}

	@Override
	public void dessiner(Camera cam) {
		// TODO Auto-generated method stub
		this.bind();
		this.dessiner();

	}



	static public VBOTexture2D oldcreateBox(Shader shader, Vector3f e) {

		VBOTexture2D worldBox;
		worldBox = new VBOTexture2D(shader);
		int i = 0;

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, -e.y, e.z);// 1

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, e.y, e.z);// 2

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, e.y, -e.z);// 3

		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, -e.y, e.z);// 1

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, e.y, e.z);// 2

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, e.y, -e.z);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

	//	worldBox.createVBO();

		// shader.use();
		// shader.glUniformfARB("size", dim);
		// shader.glUniformfARB("color", 1, 0, 0, 0);
		return worldBox;
	}

	static public VBOTexture2D createBox(Shader shader, Vector3f e) {

		VBOTexture2D worldBox;
		worldBox = new VBOTexture2D(shader);
		int i = 0;

		// Vector3f e = this.getExtent();
		// worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(-e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(-e.x, e.y, -e.z);// 1

		// worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(e.x, e.y, -e.z);// 2

		// worldBox.addNormal(0, 0, -1);
		worldBox.addVertex(e.x, -e.y, -e.z);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(-e.x, -e.y, e.z);// 0

		// worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(-e.x, e.y, e.z);// 1

		// worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(e.x, e.y, e.z);// 2

		// worldBox.addNormal(0, 0, 1);
		worldBox.addVertex(e.x, -e.y, e.z);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, -e.y, e.z);// 1

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, e.y, e.z);// 2

		// worldBox.addNormal(-1, 0, 0);
		worldBox.addVertex(-e.x, e.y, -e.z);// 3

		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, -e.y, e.z);// 1

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, e.y, e.z);// 2

		// worldBox.addNormal(1, 0, 0);
		worldBox.addVertex(e.x, e.y, -e.z);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(-e.x, -e.y, -e.z);// 0

		// worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(-e.x, -e.y, e.z);// 1

		// worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(e.x, -e.y, e.z);// 2

		// worldBox.addNormal(0, -1, 0);
		worldBox.addVertex(e.x, -e.y, -e.z);// 3
		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		// worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(-e.x, e.y, -e.z);// 0

		// worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(-e.x, e.y, e.z);// 1

		// worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(e.x, e.y, e.z);// 2

		// worldBox.addNormal(0, 1, 0);
		worldBox.addVertex(e.x, e.y, -e.z);// 3

		worldBox.addTri(i, i + 1, i + 2);
		worldBox.addTri(i, i + 2, i + 3);
		i += 4;

		//worldBox.createVBO();

		// shader.use();
		// shader.glUniformfARB("size", dim);
		// shader.glUniformfARB("color", 1, 0, 0, 0);
		return worldBox;
	}

	@Override
	public void initFromShadowMap(Objet3D obj, Camera cam) {
		// TODO Auto-generated method stub
		
	}



}
