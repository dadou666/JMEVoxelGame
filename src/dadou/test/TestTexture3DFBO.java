package dadou.test;

import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.glFogf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.BriqueAvecTexture3D;
import dadou.Button;

import dadou.DecorDeBriqueData;
import dadou.ElementDecor;
import dadou.ExplorerAuSolCameraControllerBis;
import dadou.ExplorerCameraControllerBis;
import dadou.FBO;
import dadou.Game;
import dadou.Icone;
import dadou.Key;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VitesseCamera;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class TestTexture3DFBO {
	public static void setText(Widget w, String txt) {
		Graphics2D g = w.getGraphics2DForUpdate();
		g.setColor(Color.GREEN);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, w.width - 2 * d, w.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(txt, d, w.height - 1);
		g.dispose();

		w.update();
	}

	static Widget fps = null;
	static Widget info = null;

	public static Objet3D creerObjet(Vector3f size,
			VBOBriqueTexture3D vbo, VoxelTexture3D tex) {
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();
		BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
		translation.brique = brique;
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);
		System.out.println("size=" + size);
		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		obj.ajouter(translation);
		return obj;

	}

	public static boolean partition = false;
	public FBO fbo;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Game g = new Game();
		FBO fbo = new FBO();
		String cheminRessources = OutilFichier.lireFichier(
				"fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool
				.load(cheminRessources + "/base.bin");

		/*
		 * for (ModelClasse mc : decorDeBriqueData.models.values()) {
		 * mc.initBuffer(g); }
		 */
		System.out.println(decorDeBriqueData.models.keySet());

		ModelClasse mc = decorDeBriqueData.models.get("tete.r");
		mc.initBuffer(g, null);
		fbo = new FBO();
		fbo.init(128, 128);
		Camera cam = g.getCamera();

		cam.update();
		cam.apply();

		ExplorerCameraControllerBis scc = new ExplorerCameraControllerBis(cam);

		// brique.initShader = false;
		// brique.initShader();
		scc.speed = 0.5f;

		Objet3D obj2 = creerObjet(mc.size, mc.vbo, mc.tex);

		VitesseCamera vc = new VitesseCamera();

		// obj.translation(10, 0, 10);

		scc.translation.set(0, 0, 0);
		Vector3f ar = new Vector3f(0, 0, 1);
		scc.screenHeight = g.getHeight();
		scc.screenWidth = g.getWidth();
		// obj.rotation(ar, 1.6f);

		scc.avancer = new Key(Keyboard.KEY_Z);
		scc.reculer = new Key(Keyboard.KEY_S);
		Key escape = new Key(Keyboard.KEY_ESCAPE);
		Vector3f c = mc.size.mult(0.5f);
		float frame = 0.0f;
		long total = 0;
		long totalMax = 40;
		long dureeTotal = 0;
		IHM ihm = g.nouvelleIHM();
		ihm.setSize(200, 40);

		ihm.beginX(() -> {
			ihm.beginY(() -> {
				fps = ihm.widget();
				info = ihm.widget();
				ihm.space(400);
			});
			ihm.space(200);

		});

		boolean release = true;
		double t = TimeUnit.SECONDS.toNanos(1);
		// c.addLocal(10,0,10);
		while (!g.isClosed() && !escape.isDown()) {
			// System.out.println(" frame "+k);
			// k++;
			// g.checkEscape();
			GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
			g.clear();

			glFogf(GL_FOG_DENSITY, 0.00001f);
			// obj.translation(-dim / 2, -dim / 2, -dim / 2);
			// obj.rotation(axe2, 0.01f);
			// obj.rotation(axe, 0.01f);

			Vector3f dir = cam.getDirection();
			Vector3f pos = cam.getLocation();
			// System.out.println(" pos=" + pos + "dir=" + dir);

			g.setLight0Pos(pos.x, pos.y, pos.z);
			g.setLight1Pos(0, 0, 0);
			g.setLightDirection(GL11.GL_LIGHT0, dir.x, dir.y, dir.z);
			scc.save();
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				scc.modifieVitesseCamWithMouse(vc, g,
						!Keyboard.isKeyDown(Keyboard.KEY_SPACE));

			}
			scc.gererAvancerReculer(vc);
			scc.deplacerCamera(vc);
			vc.reset();

			// obj.rotation(Vector3f.UNIT_Y, 0.01f);
			// obj.rotation(c, Vector3f.UNIT_Y, 0.01f);
			// obj2.rotation(Vector3f.UNIT_Y, 0.01f);
			long debut = System.nanoTime();
			if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
				if (release) {
					g.fps.reset();
					release = false;
					fbo.activer();
					// GL11.glViewport(0, 0, fbo.width, fbo.height);
					// obj2.dessiner(cam);
					Icone icone = fbo.creerIconne(mc, 3.14f / 4, -3.14f / 3.0f,
							25, cam,Color.BLUE);
					// Icone icone= new
					// Icone(fbo.width,fbo.height,fbo.getContent());
					icone.save("c:/tmp/img.png");
					fbo.desactiver();
				}
			} else {
				release = true;
			}
			g.fps.calculer(() -> {

				obj2.dessiner(cam);

			});

			total++;

			Widget.drawText(fps, "" + g.fps.getResult(), Color.RED, Color.BLUE,
					Color.GRAY);
			Widget.drawText(info, "" + partition, Color.RED, Color.BLUE,
					Color.GRAY);
			ihm.dessiner(g.shaderWidget);

			// System.out.println("duree="+duree);

			Display.update();
			Display.sync(30);

		}

	}

}
