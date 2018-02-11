package dadou;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VBOTexture3D {

	List<VBOTexture3DElement> elements = new ArrayList<VBOTexture3DElement>();
	List<VBOTriangle> triangles = new ArrayList<VBOTriangle>();
	float[] vertexArray;
	float[] normalArray;
	float[] coord3DTexture;
	public int nbTriangle;

	private int vaoId = 0;
	private int vertex_vboId = 0;
	private int normal_vboId = 0;
	private int coordTexture3D_vboId = 0;

	private int indice_vboId = 0;
	private int reverseIndice_vboId = 0;
	int idxVertexArray = 0;
	int idxNormalArray = 0;
	int idxCoord3DTexture = 0;

	int[] indiceArray;

	Shader shader;

	public VBOTexture3D(Shader shader) {

		this.shader = shader;

	}

	public VBOTexture3DElement getVBOTexture3DElement(int idx) {
		VBOTexture3DElement elt;
		if (elements.size() == idx) {
			elt = new VBOTexture3DElement();
			elements.add(elt);
			return elt;

		}
		elt = elements.get(idx);
		if (elt == null) {
			elt = new VBOTexture3DElement();
			elements.set(idx, elt);
		}
		return elt;

	}

	public void addVertex(float x, float y, float z) {
		VBOTexture3DElement elt = this.getVBOTexture3DElement(idxVertexArray);
		idxVertexArray++;
		elt.position.set(x, y, z);

	}

	public void addNormal(float x, float y, float z) {
		VBOTexture3DElement elt = this.getVBOTexture3DElement(idxNormalArray);
		elt.normal.set(x, y, z);
		idxNormalArray++;

	}

	public void addCoordTexture3D(float x, float y, float z) {
		VBOTexture3DElement elt = this.getVBOTexture3DElement(idxCoord3DTexture);
		elt.positionTex.set(x, y, z);
		idxCoord3DTexture++;

	}

	public void addTri(int a, int b, int c) {
		VBOTriangle tri = new VBOTriangle();
		tri.a = a;
		tri.b = b;
		tri.c = c;
		this.triangles.add(tri);

	}

	int attribute_coord3d_texture = 0;

	int attribute_vertex = 0;

	int attribute_normal = 0;

	public int getIdx(String nameAttribute) {
		int attribute = GL20.glGetAttribLocation(shader.program, nameAttribute);
		if (attribute == -1) {
			throw new Error(" pas trouvé " + nameAttribute + " => " + shader.vert);
		}
		return attribute;
	}

	public void createArrays() {

		vertexArray = new float[elements.size() * 3];
		normalArray = new float[elements.size() * 3];
		coord3DTexture = new float[elements.size() * 3];

		for (int i = 0; i < elements.size(); i++) {
			VBOTexture3DElement elt = elements.get(i);
			vertexArray[3 * i] = elt.position.x;
			vertexArray[3 * i + 1] = elt.position.y;
			vertexArray[3 * i + 2] = elt.position.z;

			coord3DTexture[3 * i] = elt.positionTex.x;
			coord3DTexture[3 * i + 1] = elt.positionTex.y;
			coord3DTexture[3 * i + 2] = elt.positionTex.z;

			normalArray[3 * i] = elt.normal.x;
			normalArray[3 * i + 1] = elt.normal.y;
			normalArray[3 * i + 2] = elt.normal.z;

		}
		indiceArray = new int[triangles.size() * 3];
		for (int i = 0; i < triangles.size(); i++) {
			VBOTriangle tri = triangles.get(i);
			indiceArray[3 * i] = tri.a;
			indiceArray[3 * i + 1] = tri.b;
			indiceArray[3 * i + 2] = tri.c;

		}

	}

	public void createVBO() {
		this.createArrays();
		if (this.indiceArray.length == 0) {
			this.nbTriangle = 0;
			return ;
		}

		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(this.vertexArray.length);
		verticesBuffer.put(vertexArray);
		verticesBuffer.flip();

		FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(this.normalArray.length);
		normalsBuffer.put(normalArray);
		normalsBuffer.flip();

		FloatBuffer coordTexture3DBuffer = BufferUtils.createFloatBuffer(this.coord3DTexture.length);
		coordTexture3DBuffer.put(coord3DTexture);
		coordTexture3DBuffer.flip();

		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(this.indiceArray.length);
		indicesBuffer.put(indiceArray);
		indicesBuffer.flip();

		// GL11.glEnable(GL15.GL_ARRAY_BUFFER_BINDING);
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		attribute_coord3d_texture = getIdx("coordTexture3D");
		attribute_vertex = getIdx("vertex");

		attribute_normal = getIdx("normal");

		this.vertex_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertex_vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(attribute_vertex, 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.normal_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normal_vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(attribute_normal, 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		this.coordTexture3D_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, coordTexture3D_vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, coordTexture3DBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribute_coord3d_texture, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind)
		this.indice_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indice_vboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		/*
		 * this.reverseIndice_vboId = GL15.glGenBuffers();
		 * GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, reverseIndice_vboId);
		 * GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, reverseIndicesBuffer,
		 * GL15.GL_STATIC_DRAW); // Deselect (bind to 0) the VBO
		 * GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		 */

		this.elements = null;
		this.triangles = null;
		this.coord3DTexture = null;
		nbTriangle = indiceArray.length;
		this.indiceArray = null;

		this.vertexArray = null;
		OpenGLTools.exitOnGLError("createVBO");

	}

	public void bind() {
	
		if (nbTriangle == 0) {
			throw new Error(" nbTriangle =0");
		}
		if (this.delete) {
			throw new Error(" delete ");
		}
		OpenGLTools.exitOnGLError("bind");
		
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(this.attribute_coord3d_texture);

		GL20.glEnableVertexAttribArray(this.attribute_vertex);
		GL20.glEnableVertexAttribArray(this.attribute_normal);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indice_vboId);

		OpenGLTools.exitOnGLError("bind");
	}

	public void unBindVAO() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(attribute_coord3d_texture);

		GL20.glDisableVertexAttribArray(attribute_vertex);
		GL20.glDisableVertexAttribArray(attribute_normal);
		GL30.glBindVertexArray(0);
		OpenGLTools.exitOnGLError("unbind");

	}

	boolean delete = false;
	boolean error = false;

	public void dessiner(Shader shader) {
		if (error) {
			return;
		}
		if (delete) {
			Log.print("Deleted...");
		}

		shader.use();
		OpenGLTools.exitOnGLError("shaderUse");
		// this.getMatrix(shader);
		shader.glUniformiARB("texture", 0);

		try {
			GL11.glDrawElements(GL11.GL_TRIANGLES, nbTriangle, GL11.GL_UNSIGNED_INT, 0);
		} catch(Throwable t) {
			t.printStackTrace();
		}
		//OpenGLTools.exitOnGLError("glDrawElements " + nbTriangle);
	}

	public void dessiner() {

		shader.use();
		GL11.glDrawElements(GL11.GL_TRIANGLES, nbTriangle, GL11.GL_UNSIGNED_INT, 0);
		OpenGLTools.exitOnGLError("glDrawElements " + nbTriangle);
	}

	public void delete() {

		GL20.glDisableVertexAttribArray(0);

		// Delete the vertex VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.vertex_vboId);

		// Delete the index VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.indice_vboId);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(this.normal_vboId);
		// Delete the VAO
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoId);
		OpenGLTools.exitOnGLError("delete ");

	}
	public void finalize() {
	
	
		//	Log.print(" delete "+this);
		this.delete = true;
		this.delete();
	
	}

}
