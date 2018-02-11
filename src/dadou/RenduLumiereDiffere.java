package dadou;

import java.io.Serializable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class RenduLumiereDiffere implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5669135946705474078L;
	transient public VBOTexture2D vbo;
	transient public Shader shader;
	public byte  [] data;
	transient public Texture2D texture;
	public int width;
	public int height;
	
	public RenduLumiereDiffere(int width,int height,byte data []) {
		this.width=width;
		this.height=height;
		this.data=data;
		
		
	}
	public void init(Shader shader) {
		
		this.shader = shader;
		vbo = new VBOTexture2D(shader);
		vbo.addVertex(-1, -1, 0.00f);
		vbo.addVertex(-1, 1, 0.0f);
		vbo.addVertex(1, 1, 0.0f);
		vbo.addVertex(1, -1, 0.0f);

		vbo.addCoordTexture2D(0, 0);
		vbo.addCoordTexture2D(0, 1);
		vbo.addCoordTexture2D(1, 1);
		vbo.addCoordTexture2D(1, 0);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		texture = new Texture2D(width,height);
		texture.updateRGBA(data);
		
	}
	
	public void dessiner(Shader widgetShader) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.texId);

		widgetShader.use();
		vbo.bind();
		widgetShader.glUniformiARB("texture", 0);
		widgetShader.glUniformfARB("discardColor", 0, 0, 0, 0);
		vbo.dessiner();
		vbo.unBindVAO();
	}

}
