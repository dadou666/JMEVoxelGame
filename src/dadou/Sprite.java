package dadou;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Objet3D;
import dadou.Shader;
import dadou.Texture2D;
import dadou.VBOTexture2D;
import dadou.tools.BrickEditor;

public class Sprite {
	public Objet3D objet3D;
	public Texture2D tex;
	public Map<String, Texture2D> texCibles = new HashMap<>();

	public Sprite( int width, int height,float echelle) {

		tex = new Texture2D(width, height);
		Graphics2D g = tex.getGraphics2DForUpdate();

		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.yellow);
		int d = 5;
		g.fillOval(0, 0, width , height);
		tex.update();
		this.texCibles.put("soleil", tex);
		objet3D = new Objet3D();
		objet3D.brique = this.creerParticule(echelle);

	}

	public void initialiserCible(String nom) {
		Texture2D tex = (this.texCibles.get(nom));

		if (tex != null) {
			this.tex = tex;
		} else {
			tex = new Texture2D(32, 32);
			Graphics2D g = tex.getGraphics2DForUpdate();

			g.setColor(Color.black);
			g.fillRect(0, 0, 31, 31);
			try {
				g.setColor((Color) Color.class.getField(nom).get(null));
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				return;
			}

			g.setStroke(new BasicStroke(4));
			g.drawPolygon(new int[] { 4, 16, 27, 16 }, new int[] { 16, 4, 16, 27 }, 4);
			tex.update();
			this.texCibles.put(nom, tex);
			this.tex = tex;

		}

	}

	public VBOTexture2D creerParticule(float echelle) {
		VBOTexture2D vbo = new VBOTexture2D(Game.shaderParticule);

		vbo.addCoordTexture2D(0.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 0.0f);
		vbo.addCoordTexture2D(0.0f, 0.0f);

		vbo.addVertex(-0.5f*echelle, -0.5f*echelle, 0.0f);
		vbo.addVertex(0.5f*echelle, -0.5f*echelle, 0.0f);
		vbo.addVertex(0.5f*echelle, 0.5f*echelle, 0.0f);
		vbo.addVertex(-0.5f*echelle, 0.5f*echelle, 0.0f);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		return vbo;

	}

	public void dessiner(Vector3f pos, Camera cam) {
		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Game.shaderParticule.use();
		tex.bind();
		Game.shaderParticule.glUniformiARB("baseTexture", 0);
		Game.shaderParticule.glUniformfARB("up", up.x, up.y, up.z);
		Game.shaderParticule.glUniformfARB("left", left.x, left.y, left.z);
		Game.shaderParticule.glUniformfARB("size", 0.08f);
		objet3D.positionToZero();
		objet3D.translation(pos);
		objet3D.dessiner(cam);
	}

}
