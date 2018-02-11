package dadou.tools.canon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Game;
import dadou.Objet3D;
import dadou.Shader;
import dadou.Texture2D;
import dadou.VBOTexture2D;
import dadou.tools.BrickEditor;

public class Cible {
	public Objet3D objet3D;
	public Texture2D tex;
	public Map<String, Texture2D> texCibles = new HashMap<>();
	public float transparence = 1.0f;
	public boolean devant = false;
	public float size = 0.08f;

	public Cible() {

		tex = new Texture2D(32, 32);
		Graphics2D g = tex.getGraphics2DForUpdate();
		size = 0.08f;
		g.setColor(Color.black);
		g.fillRect(0, 0, 31, 31);
		g.setColor(Color.cyan);
		g.setStroke(new BasicStroke(2));
		g.drawPolygon(new int[] { 4, 16, 27 }, new int[] { 30, 20, 30 }, 3);
		tex.update();
		this.texCibles.put("cyan", tex);
		objet3D = new Objet3D();
		vbo = this.creerParticule(Game.shaderParticule);
		vboDevant = this.creerParticule(Game.shaderParticuleDevant);

	}

	public void initialiserCibleAvecValeur(String nom, int val) {
		if (tex == null) {
			tex = new Texture2D(32, 32);
		}
		Graphics2D g = tex.getGraphics2DForUpdate();

		g.setColor(Color.black);
		g.fillRect(0, 0, 31, 31);
		try {
			g.setColor((Color) Color.class.getField(nom).get(null));
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			return;
		}
		FormeCible fc;

		fc = FormeCible.formes().get("cercle");
		int d = g.getFontMetrics().stringWidth("" + val) / 2;
		g.drawString("" + val, 16 - d, 20);

		fc.forme(g);
		tex.update();

	}

	public void initialiserCible(String nom, String forme) {

		String nomCible;
		if (forme == null) {
			nomCible = nom;
		} else {
			nomCible = nom + "$" + forme;
		}
		Texture2D tex = (this.texCibles.get(nomCible));

		if (tex != null) {
			this.tex = tex;
		} else {
			tex = new Texture2D(32, 32);
			Graphics2D g = tex.getGraphics2DForUpdate();

			g.setColor(Color.black);
			g.fillRect(0, 0, 31, 31);
			try {
				g.setColor((Color) Color.class.getField(nom).get(null));
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				return;
			}
			FormeCible fc;
			if (forme == null) {
				fc = FormeCible.formes().get("losange");
			} else {
				fc = FormeCible.formes().get(forme);
			}
			fc.forme(g);
			tex.update();
			this.texCibles.put(nomCible, tex);
			this.tex = tex;

		}

	}
	VBOTexture2D vbo;
	VBOTexture2D vboDevant;
	public VBOTexture2D creerParticule(Shader shader) {
		VBOTexture2D vbo = new VBOTexture2D(shader);

		vbo.addCoordTexture2D(0.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 0.0f);
		vbo.addCoordTexture2D(0.0f, 0.0f);

		vbo.addVertex(-0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, 0.5f, 0.0f);
		vbo.addVertex(-0.5f, 0.5f, 0.0f);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		return vbo;

	}

	public void dessiner(Vector3f pos, Camera cam) {
		if (transparence < 1.0f) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Shader shader = Game.shaderParticuleDevant;
		if (!devant) {
			shader = Game.shaderParticule;
		}
		shader.use();
		tex.bind();
		shader.glUniformfARB("transparence", transparence);
		shader.glUniformiARB("baseTexture", 0);
		shader.glUniformfARB("up", up.x, up.y, up.z);
		shader.glUniformfARB("left", left.x, left.y, left.z);
		shader.glUniformfARB("size", size);
	
		objet3D.positionToZero();
		objet3D.translation(pos);
		if (devant) {
			objet3D.brique = vboDevant;
		} else {
			objet3D.brique = vbo;
		}
		objet3D.dessiner(cam);
		if (transparence < 1.0f) {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

}
