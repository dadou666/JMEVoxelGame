package dadou;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VBOVisibilite {
	float [] vertexArray;

	
	private int vaoId = 0;
	private int vertex_vboId = 0;



	private int indice_vboId = 0;
	int idxVertexArray=0;


	int idxIndice=0;
	int []indiceArray;
	Shader shader;
	
	public VBOVisibilite(	Shader shader,int nbVertex,int nbIndice) {
		vertexArray = new float[nbVertex*3];


		indiceArray = new int[nbIndice];
		this.shader=shader;
		
	}
	
	public void addVertex(float x,float y,float z){
		vertexArray[idxVertexArray++] =x;
		vertexArray[idxVertexArray++] =y;
		vertexArray[idxVertexArray++] =z;
		
		
	}

	

	

	public void addTri(int a,int b,int c) {
		indiceArray[idxIndice++]=a;
		indiceArray[idxIndice++]=b;
		indiceArray[idxIndice++]=c;
		
	}


	 int attribute_vertex = 0;
	
	 
	 public int getIdx(String nameAttribute){
		 int attribute = GL20.glGetAttribLocation(shader.program, nameAttribute);
			if (attribute == -1 ) {
				throw new Error(" pas trouvé "+nameAttribute);
			} 
			return attribute;
	 }
	public void createVBO() {
	
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(this.vertexArray.length);
		verticesBuffer.put(vertexArray);
		verticesBuffer.flip();
	
		
		

	
		
		IntBuffer indicesBuffer = BufferUtils.createIntBuffer(this.indiceArray.length);
		indicesBuffer.put(indiceArray);
		indicesBuffer.flip();
		
		
		
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
	
		attribute_vertex = getIdx("vertex");
	
	
		
		
		this.vertex_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertex_vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
		// Put the VBO in the attributes list at index 0
		GL20.glVertexAttribPointer(attribute_vertex, 3, GL11.GL_FLOAT, false, 0, 0);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	
		
	
		

		
		
		
		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);
		
		// Create a new VBO for the indices and select it (bind)
		this.indice_vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indice_vboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		
		
		
	}
	
	public void bind() {
	
		GL30.glBindVertexArray(vaoId);
		
	
		GL20.glEnableVertexAttribArray(this.attribute_vertex);

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indice_vboId);
		
	}
	public void unBindVAO() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	

		GL20.glDisableVertexAttribArray(attribute_vertex);
	
		GL30.glBindVertexArray(0);
		
		
	}
	public void dessiner(Shader shader) {
		
		shader.use();

		GL11.glDrawElements(GL11.GL_TRIANGLES, this.indiceArray.length,
				GL11.GL_UNSIGNED_INT, 0);
	}
	public void dessiner() {
		
		shader.use();
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.indiceArray.length,
				GL11.GL_UNSIGNED_INT, 0);
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
}
