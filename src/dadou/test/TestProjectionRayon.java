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

import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;

import dadou.BriqueAvecTexture3D;
import dadou.BriqueProjRayon;
import dadou.Button;

import dadou.DecorDeBriqueData;
import dadou.ElementDecor;
import dadou.ExplorerAuSolCameraControllerBis;
import dadou.ExplorerCameraControllerBis;
import dadou.FBO;
import dadou.Fps;
import dadou.Game;
import dadou.Key;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.Shader;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VitesseCamera;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class TestProjectionRayon {
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

	public static Color getColor(int x, int y, int z, Color colors[][][]) {
		try {
			return colors[x][y][z];
		} catch (IndexOutOfBoundsException u) {
			return Color.BLACK;
		}

	}

	public static Color calculerCouleur(int x, int y, int z, Color colors[][][]) {
		int nbVide = 0;
		int d[] = { 0, 1 };
		int r = 0;
		int g = 0;
		int b = 0;
		int nbNonVide = 0;
		for (int dx : d) {
			for (int dy : d) {
				for (int dz : d) {
					Color color = getColor(x + dx, y + dy, z + dz, colors);
					if (ElementDecor.estVide(color)) {
						nbVide++;
					} else {
						r += color.getRed();
						g += color.getGreen();
						b += color.getBlue();
						nbNonVide++;
					}
				}
			}
		}
		if (nbVide >= 4) {
			return Color.BLACK;
		}
		r = r / nbNonVide;
		g = g / nbNonVide;
		b = b / nbNonVide;
		return new Color(r, g, b);

	}

	public static Color[][][] reduire(Color colors[][][], int dx, int dy, int dz) {
		int ux = dx / 2;
		int uy = dy / 2;
		int uz = dz / 2;
		Color[][][] r = new Color[ux][uy][uz];
		for (int x = 0; x < ux; x++) {
			for (int y = 0; y < uy; y++) {
				for (int z = 0; z < uz; z++) {
					r[x][y][z] = calculerCouleur(2 * x, 2 * y, 2 * z, colors);
				}
			}
		}
		return r;

	}

	static Widget fps = null;
	static Widget info = null;

	public static Objet3D creerObjet(Vector3f size, VBOBriqueTexture3D vbo, VoxelTexture3D tex) {
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();
		BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
		translation.brique = brique;
		
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);
		System.out.println("size=" + size);
		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		// obj.ajouter(translation);
		return translation;

	}

	public static Objet3D creerObjet(Vector3f size, BriqueProjRayon brique) {
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();

		translation.brique = brique;
		brique.echelle = 1.0f;

		System.out.println("size=" + size);
		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		// obj.ajouter(translation);
		return translation;

	}

	public static boolean partition = false;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Game g = new Game();
		FBO fbo = new FBO();
		Shader shader = new Shader(Shader.class, "proj_rayon.frag", "proj_rayon.vert", null);
		String cheminRessources = OutilFichier.lireFichier("fichierRessource.txt").get(0);
		DecorDeBriqueData decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");

		/*
		 * for (ModelClasse mc : decorDeBriqueData.models.values()) {
		 * mc.initBuffer(g); }
		 */
		LWJGLCamera cl;
		// cl.getModelViewMatrix();
		System.out.println(decorDeBriqueData.models.keySet());
		int dx = 16;
		int dy = 16;
		int dz = 16;
		Color ls[] = new Color[] { Color.red, Color.yellow, Color.blue, Color.green, Color.gray,

		};
		int idx = 0;
		Color copie[][][] = new Color[dx][dy][dz];
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				for (int z = 0; z < dz; z++) {
					if ((2 * x) % 6 != 0 && (2 * y) % 6 != 0 && (2 * z) % 6 != 0) {
						copie[x][y][z] = ls[idx];
						idx++;
						if (idx >= ls.length) {
							idx = 0;
						}

					} else {
						copie[x][y][z] = Color.BLACK;
					}
				}

			}
		}
		ModelClasse mc = new ModelClasse();
		mc.copie = copie;
		mc.dx = dx;
		mc.dy = dy;
		mc.dz = dz;

		// ModelClasse mc = decorDeBriqueData.models.get("objet.arme");
		mc.initBuffer(g, null);

		LWJGLCamera cam = (LWJGLCamera) g.getCamera();

		cam.update();
		cam.apply();

		ExplorerCameraControllerBis scc = new ExplorerCameraControllerBis(cam);

		// brique.initShader = false;
		// brique.initShader();
		scc.speed = 0.5f;

		Objet3D obj2 = creerObjet(mc.size, mc.vbo, mc.tex);

		VitesseCamera vc = new VitesseCamera();

		// obj.translation(10, 0, 10);

		scc.translation.set(0, 0, -28);
		Vector3f ar = new Vector3f(0, 0, 1);
		scc.screenHeight = g.getHeight();
		scc.screenWidth = g.getWidth();
		// obj.rotation(ar, 1.6f);

		scc.avancer = new Key(Keyboard.KEY_Z);
		scc.reculer = new Key(Keyboard.KEY_S);
		Key escape = new Key(Keyboard.KEY_ESCAPE);
		Vector3f c = mc.size.mult(-0.0f);
		c.y = 0;
		System.out.println("c=" + c);
		BriqueProjRayon bpr = new BriqueProjRayon(mc.tex, shader);
		Objet3D obj3 = creerObjet(mc.size, bpr);
		boolean release = true;
		Vector3f axe = new Vector3f(1.0f, 1.0f, 0.0f);
		axe.normalizeLocal();
		Matrix4f mat = new Matrix4f();
		Quaternion q = new Quaternion();
		Fps fpsA = new Fps();
		Fps fpsB = new Fps();
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
				scc.modifieVitesseCamWithMouse(vc, g, !Keyboard.isKeyDown(Keyboard.KEY_SPACE));

			}
			scc.gererAvancerReculer(vc);
			scc.deplacerCamera(vc);
			vc.reset();

			obj2.rotation(c, axe, 0.01f);
			obj3.rotation(c, axe, 0.01f);
			g.fps.calculer(() -> {
				if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
					obj2.dessiner(cam);
				} else {
					obj3.dessiner(cam);
				}
			});
		
System.out.println("fps="+g.fps.getResult());
			// System.out.println("duree="+duree);

			Display.update();
			Display.sync(30);

		}

	}

}
