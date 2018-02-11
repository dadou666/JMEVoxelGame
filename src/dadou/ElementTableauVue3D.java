package dadou;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class ElementTableauVue3D extends ElementTableau {
	public Objet3D objet3D;
	public float inclinaison;
	public float vitesse;
	public float angleY = 0.0f;
	public BrickEditor brickEditor;

	public int nbLigne;
	public Color couleurFond;
	public float distance;
	public Icone icone;
	public ModelClasse mc;

	public ElementTableauVue3D(BrickEditor brickEditor, String nomModel, float vitesse, float inclinaison, int nbLigne,
			Color couleurFond) {
		this.brickEditor = brickEditor;
		ModelClasse mc = brickEditor.decorDeBriqueData.models.get(nomModel);
		objet3D = new Objet3D();
		this.mc = mc;
		this.vitesse = vitesse;
		this.inclinaison = inclinaison;
		this.distance = mc.size.length();

		BriqueAvecTexture3D brique = mc.vbo.creerBriqueAvecTexture3D(mc.tex);
		objet3D.brique = brique;
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);
		Vector3f size = new Vector3f();
		size.set(mc.size);
		Vector3f t = size.mult(-0.5f);
		objet3D.translation(t);
		this.couleurFond = couleurFond;
	}

	public void afficher(Graphics2D g, int x, int y, int d, int fontHeight, Map<String, Icone> icones, Font font) {

		int dim = (nbLigne - 1) * fontHeight;
		this.creerIcone(dim);

		int W = tableau.widget.tex.width;
		;
		int IW = dim;
		int Q = Math.max((W - IW) / 2, 0);

		g.drawImage(icone.getImage(), x + Q, y, null);

	}

	public int nbLigne() {
		return nbLigne;
	}

	public void creerIcone(int dim) {
		FBO fbo = this.brickEditor.mondeInterface.donnerFBO(dim, dim);
		this.angleY += vitesse;
		// icone= fbo.creerIconne(mc, inclinaison, this.angleY,
		// mc.size.length(), this.brickEditor.game.getCamera(), couleurFond);
		fbo.render(() -> {
			Game.RenderMode old = Game.rm;
			Game.rm = Game.RenderMode.Normal;
			Camera cam = this.brickEditor.game.cam;
			Quaternion rotationX = new Quaternion();
			angleY += vitesse;
			rotationX.fromAngleNormalAxis(inclinaison, Vector3f.UNIT_X);
			Quaternion rotationY = new Quaternion();
			rotationY.fromAngleNormalAxis(this.angleY, Vector3f.UNIT_Y);
			Quaternion tmp = new Quaternion();
			tmp.set(rotationY);
			tmp.multLocal(rotationX);
			cam.setAxes(tmp);
			cam.setLocation(cam.getDirection().mult(-distance));

			cam.update();
			cam.apply();
			GL11.glViewport(0, 0, dim, dim);
			fbo.red = (float) couleurFond.getRed() / 255.0f;
			fbo.green = (float) couleurFond.getGreen() / 255.0f;
			fbo.blue = (float) couleurFond.getBlue() / 255.0f;

			glClear(GL_DEPTH_BUFFER_BIT);

			objet3D.dessiner(cam);
			Game.rm = old;
		});
		icone = new Icone(dim, dim, fbo.getContent());
		// GL11.glViewport(0, 0, brickEditor.game.getWidth(),
		// brickEditor.game.getHeight());

	}

	public boolean modifierTexture() {
		return true;

	}

}
