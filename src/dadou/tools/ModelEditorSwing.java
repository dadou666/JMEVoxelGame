package dadou.tools;

import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.glFogf;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;

import dadou.Arbre;
import dadou.BriqueAvecTexture3D;
import dadou.Button;
import dadou.DecorDeBriqueData;
import dadou.FBO;
import dadou.FBORenduLumiereDiffere;
import dadou.FBOShadowMap;
import dadou.Game;
import dadou.Habillage;
import dadou.Icone;
import dadou.Key;
import dadou.Log;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.Shader;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.graphe.GrapheLigne;
import dadou.greffon.Brique;
import dadou.greffon.Cercle;
import dadou.greffon.GreffonSelection;
import dadou.greffon.SphereRemplissage;
import dadou.greffon.TournerY;
import dadou.ihm.Comportement;
import dadou.ihm.IHM;
import dadou.ihm.IHMSwingGame;
import dadou.ihm.Widget;
import dadou.tools.texture.ChoixKube;
import dadou.tools.texture.ChoixTexture;

public class ModelEditorSwing implements Comportement, TreeSelectionListener {
	public JTree tree;
	public int screenHeight;
	public int screenWidth;
	public DecorDeBriqueData decorDeBriqueData;
	volatile public String nouveauModelClasse = null;
	public Quaternion rotationX = new Quaternion();
	public Quaternion rotationY = new Quaternion();
	public float theta = 0.0f;
	public float phi = 0.0f;
	public float distance = 0.0f;
	Quaternion tmp = new Quaternion();
	public Vector3f size = new Vector3f();
	public Vector3f pos = new Vector3f();
	public Vector3f posDebutSelection;

	public ModelEditorMenu menu;
	Widget axeX;
	Widget axeY;
	Widget axeZ;
	String fileNameEcran;
	Map<String, Habillage> habillages = new HashMap<>();

	Compilable compilable;
	ScriptEngine engine;

	Widget axeEnCours;
	public float selDist = 18.0f;
	public Vector2f screenPos = new Vector2f();

	public Button b = new Button(1);
	public Vector3f oldPos = new Vector3f();

	public void gererMouseWheel() {
		if (mc == null) {
			return;
		}
		int wheel = Mouse.getDWheel();
		float OldSelDist = selDist;
		if (wheel < 0) {
			selDist -= 1.0f;
		}
		if (wheel > 0) {
			selDist += 1.0f;
		}
		int x = Mouse.getX();
		int y = Mouse.getY();

		screenPos.set(x, y);
		Camera cam = this.window.game.getCamera();
		oldPos.set(pos);

		pos.set(cam.getWorldCoordinates(screenPos, 1));
		pos.subtractLocal(cam.getWorldCoordinates(screenPos, 0));
		pos.normalizeLocal();
		pos.multLocal(selDist);
		pos.addLocal(cam.getLocation());
		int px = (int) (pos.x - size.x);
		int py = (int) (pos.y - size.y);
		int pz = (int) (pos.z - size.z);

		// int ox = Math.min(Math.max(0, px), mc.dx - 1);
		// int oy = Math.min(Math.max(0, py), mc.dy - 1);
		// int oz = Math.min(Math.max(0, pz), mc.dz - 1);
		pos.set(px, py, pz);
		// if (ox != px || oy != py || oz != pz) {
		// selDist = OldSelDist;
		// }
		pos.addLocal(size);

	}

	public void initCamera(Camera cam) {

		rotationX.fromAngleNormalAxis(theta, Vector3f.UNIT_X);

		rotationY.fromAngleNormalAxis(phi, Vector3f.UNIT_Y);

		tmp.set(rotationY);
		tmp.multLocal(rotationX);
		cam.setAxes(tmp);
		cam.setLocation(cam.getDirection().mult(-distance));

		cam.update();
		cam.apply();

	}

	public Habillage donnerHabillage(String nomHabillage) {
		if (nomHabillage == null) {
			return null;
		}
		Habillage h = this.habillages.get(nomHabillage);
		if (h == null) {
			try {
				h = (Habillage) SerializeTool.load(new File(this.cheminRessources, nomHabillage));
				this.habillages.put(nomHabillage, h);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return h;

	}

	public IHMSwingGame window;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {

					ModelEditorSwing a = new ModelEditorSwing();
					a.cheminRessources = args[0];
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					int width = (int) screenSize.getWidth();
					int height = (int) screenSize.getHeight() - 100;
					a.screenWidth = width;
					a.screenHeight = height;
					a.window = new IHMSwingGame(a, width - 200, height, a.screenWidth, a.screenHeight);

					a.init(a.window);
					a.window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					a.window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	String cheminRessources = null;

	public void charger() throws FileNotFoundException, ClassNotFoundException, IOException {

		Log.print("charger : ", cheminRessources + "/base.bin");

		decorDeBriqueData = (DecorDeBriqueData) SerializeTool.load(cheminRessources + "/base.bin");

	}

	public VoxelTexture3D donnerHabillageVoxelTexture3D(String nomHabillage) {
		Habillage habillage = this.donnerHabillage(nomHabillage);
		if (habillage == null) {
			if (nomHabillage != null) {
				System.err.println(" habillage non trouve " + nomHabillage);
			}
			return null;
		}

		return habillage.creerVoxelTexture3D();

	}

	Widget selectionAxe;
	IHM ihm;



	public void init(IHMSwingGame w) throws FileNotFoundException, ClassNotFoundException, IOException {
		tree = new JTree();
		tree.setModel(null);

		JScrollPane treeView = new JScrollPane(tree);

		tree.setBounds(0, 0, screenWidth - w.width, screenHeight);
		treeView.setBounds(0, 0, screenWidth - w.width, screenHeight);
		// treeView.setLayout(null);
		w.frame.getContentPane().add(treeView);
		tree.addTreeSelectionListener(this);
		GreffonSelection.greffons.add(new Brique());
		GreffonSelection.greffons.add(new SphereRemplissage(true));
		GreffonSelection.greffons.add(new SphereRemplissage(false));
		GreffonSelection.greffons.add(new Cercle());
		GreffonSelection.greffons.add(new TournerY());
		menu = new ModelEditorMenu(this, tree);
		tree.addMouseListener(menu);

	}

	Shader shader;
	public Vector3f dir = new Vector3f();
	public Vector3f array[] = new Vector3f[] { new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f() };

	public void dessinerSelection(Vector3f pos, float d, Vector3f axe, Vector3f u, Vector3f v) {
		if (mc == null) {
			return;
		}
		array[0].set(u);
		array[1].set(v);

		array[2].set(u);
		array[2].addLocal(v);

		shader.use();

		int x = (int) (pos.x - size.x);
		int y = (int) (pos.y - size.y);
		int z = (int) (pos.z - size.z);
		boolean horsZone = (x < 0 || x >= mc.dx) || (y < 0 || y >= mc.dy) || (z < 0) || (z >= mc.dz);
		for (Vector3f p : array) {
			if (horsZone) {
				shader.glUniformfARB("color", 0.0f, 1.0f, 0.0f, 0.0f);
			} else {
				shader.glUniformfARB("color", 0.0f, 0.0f, 0.0f, 0.0f);
			}
			GL11.glLineWidth(3.0f);
			GL11.glBegin(GL11.GL_LINES);

			GL11.glVertex3f(pos.x + p.x, pos.y + p.y, pos.z + p.z);

			GL11.glVertex3f(pos.x + p.x + d * axe.x, pos.y + p.y + d * axe.y, pos.z + p.z + d * axe.z);
			GL11.glEnd();

		}

		if (this.posDebutSelection != null) {

		}

	}

	public float val(float d) {
		if (d >= 0.0f) {
			return d + 1;
		}
		return d-1;
	}

	public Vector3f UNIT_X = new Vector3f(1.0f, 0.0f, 0.0f);
	public Vector3f UNIT_Y = new Vector3f(0.0f, 1.0f, 0.0f);
	public Vector3f UNIT_Z = new Vector3f(0.0f, 0.0f, 1.0f);
	public Vector3f tmpV = new Vector3f();

	public void dessinerSelection() {
		float dx = 1.0f;
		float dy = 1.0f;
		float dz = 1.0f;
		if (posDebutSelection != null) {
			dx = val(pos.x - posDebutSelection.x);
			dy = val(pos.y - posDebutSelection.y);
			dz = val(pos.z - posDebutSelection.z);

			UNIT_X.x = dx;
			UNIT_Y.y = dy;
			UNIT_Z.z = dz;
			tmpV.set(posDebutSelection);
			
			if (dx < 0) {
				tmpV.x=tmpV.x+1.0f;
			}
			if (dy < 0) {
				tmpV.y=tmpV.y+1.0f;
			}
			if (dz < 0) {
				tmpV.z=tmpV.z+1.0f;
			}
			this.dessinerSelection(tmpV, dx, Vector3f.UNIT_X, UNIT_Y, UNIT_Z);
			this.dessinerSelection(tmpV, dy, Vector3f.UNIT_Y, UNIT_X, UNIT_Z);
			this.dessinerSelection(tmpV, dz, Vector3f.UNIT_Z, UNIT_Y, UNIT_X);

		}

		UNIT_X.x = 1.0f;
		UNIT_Y.y = 1.0f;
		UNIT_Z.z = 1.0f;
		this.dessinerSelection(pos, 1.0f, Vector3f.UNIT_X, UNIT_Y, UNIT_Z);
		this.dessinerSelection(pos, 1.0f, Vector3f.UNIT_Y, UNIT_X, UNIT_Z);
		this.dessinerSelection(pos, 1.0f, Vector3f.UNIT_Z, UNIT_Y, UNIT_X);

	}

	Widget currentColorWidget;

	public void dessinerAxes() {

		shader.use();

		shader.glUniformfARB("color", 1.0f, 0.0f, 0.0f, 0.0f);

		GL11.glLineWidth(3.0f);
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3f(size.x, size.y, size.z);

		GL11.glVertex3f(size.x + 1.0f, size.y, size.z);
		GL11.glEnd();

		shader.glUniformfARB("color", 0.0f, 1.0f, 0.0f, 0.0f);

		GL11.glLineWidth(3.0f);
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3f(size.x, size.y, size.z);

		GL11.glVertex3f(size.x, size.y + 1.0f, size.z);
		GL11.glEnd();

		shader.glUniformfARB("color", 0.0f, 0.0f, 1.0f, 0.0f);

		GL11.glLineWidth(3.0f);
		GL11.glBegin(GL11.GL_LINES);

		GL11.glVertex3f(size.x, size.y, size.z);

		GL11.glVertex3f(size.x, size.y, size.z + 1.0f);
		GL11.glEnd();
	}

	Key plus;
	Key moins;
	Button button = new Button(0);
	public DefaultTreeModel treeModel;

	public void modifierWidgetAxe() {
		if (axeEnCours != axeX) {
			Widget.drawText(axeX, "Axe X", Color.GRAY, Color.RED, Color.RED);
		} else {

			Widget.drawText(axeX, "Axe X", Color.GRAY, Color.RED, Color.GRAY);
			this.dir = Vector3f.UNIT_X;
		}
		if (axeEnCours != axeY) {
			Widget.drawText(axeY, "Axe Y", Color.GRAY, Color.GREEN, Color.GREEN);
		} else {
			Widget.drawText(axeY, "Axe Y", Color.GRAY, Color.GREEN, Color.GRAY);
			this.dir = Vector3f.UNIT_Y;
		}
		if (axeEnCours != axeZ) {
			Widget.drawText(axeZ, "Axe Z", Color.GRAY, Color.BLUE, Color.BLUE);
		} else {
			Widget.drawText(axeZ, "Axe Z", Color.GRAY, Color.BLUE, Color.GRAY);
			this.dir = Vector3f.UNIT_Z;
		}
	}

	Widget widgetSelection;
	FBO fbo;
	FBORenduLumiereDiffere fboLumiere;

	@Override
	public void init(Game game) {
		ScriptEngineManager factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
		fbo = new FBO();
		fbo.init(128, 128);
		fboLumiere = new FBORenduLumiereDiffere();

		fboLumiere.init(game.getWidth(), game.getHeight());

		compilable = (Compilable) engine;
		entree = new Key(Keyboard.KEY_RETURN);
		echap = new Key(Keyboard.KEY_ESCAPE);
		Game.rm = Game.RenderMode.Normal;
		try {
			this.charger();
			ihm = window.game.nouvelleIHM();
			int height = window.height / 20;

	
			ihm.beginY();
			ihm.beginX(() -> {

				ihm.setSize(100, 30);

				this.widgetSelection = ihm.widget();
				ihm.space(window.width);

			});
			ihm.space(window.height);
			ihm.end();
			Widget.drawText(widgetSelection, "0,0,0", Color.RED, Color.BLUE, Color.CYAN);

			// this.modifierWidgetAxe();
			plus = new Key(Keyboard.KEY_ADD);
			moins = new Key(Keyboard.KEY_SUBTRACT);
			Arbre<String> arbre = this.decorDeBriqueData.creerArbreModel();
			arbre.createMutableNodeTree();
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					treeModel = new DefaultTreeModel(arbre.node);
					tree.setModel(treeModel);

				}

			});
		

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shader = new Shader(Shader.class, "base_sans_vbo.frag", "base_sans_vbo.vert", null);
		FBOShadowMap.shadowMap = new FBOShadowMap();
		FBOShadowMap.shadowMap.init(1, 1, game);
	}

	public Objet3D obj;
	public ModelClasse mc;
	public ChoixGenerique etat;
	public ChoixGenerique etatSelection;

	public boolean verifierPosition(float sign) {
		if (pos.x + sign * dir.x < 0) {
			return false;
		}
		if (pos.y + sign * dir.y < 0) {
			return false;
		}
		if (pos.z + sign * dir.z < 0) {
			return false;
		}
		if (pos.x + sign * dir.x >= mc.dx) {
			return false;
		}
		if (pos.y + sign * dir.y >= mc.dy) {
			return false;
		}
		if (pos.z + sign * dir.z >= mc.dz) {
			return false;
		}
		return true;
	}

	Key entree;
	Key echap;
	

	public Color couleurCourante() {
	 return this.couleurCourante;
	
	}

	public int cameraSurX() {
		Vector3f dir = window.game.getCamera().getDirection();
		if (Math.abs(dir.x) >= Math.abs(dir.y) && Math.abs(dir.x) >= Math.abs(dir.z)) {
			if (dir.x > 0) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;

	}

	public int cameraSurY() {
		Vector3f dir = window.game.getCamera().getDirection();
		if (Math.abs(dir.y) >= Math.abs(dir.x) && Math.abs(dir.y) >= Math.abs(dir.z)) {
			if (dir.y > 0) {
				return 1;

			} else {
				return -1;
			}

		}
		return 0;

	}

	public int cameraSurZ() {
		Vector3f dir = window.game.getCamera().getDirection();
		if (Math.abs(dir.z) >= Math.abs(dir.x) && Math.abs(dir.z) >= Math.abs(dir.y)) {
			if (dir.z > 0) {
				return 1;
			} else {
				return -1;
			}

		}
		return 0;

	}

	public boolean cameraInverse() {
		int r;
		if ((r = cameraSurX()) != 0) {
			return r < 0;
		}
		if ((r = cameraSurY()) != 0) {
			return r < 0;
		}
		if ((r = cameraSurZ()) != 0) {
			return r > 0;
		}
		return false;
	}
Color couleurCourante;
	public void calculerCouleurCourante() {
		if (etatSelection == null) {
			return;
		}
		if (etatSelection instanceof ChoixKube) {
			ChoixKube ck = (ChoixKube) etatSelection;
			this.couleurCourante = ck.choix.valeur;
			return;
		}
		ChoixTexture ct = (ChoixTexture) etatSelection;
		if (ct.choix == null) {
			return;
		}
		int x = (int) (pos.x - size.x);
		int y = (int) (pos.y - size.y);
		int z = (int) (pos.z - size.z);
		boolean horsZone = (x < 0 || x >= mc.dx) || (y < 0 || y >= mc.dy) || (z < 0) || (z >= mc.dz);
		if (horsZone) {
			return;
		}
		Color c = Color.BLACK;
		if (mc.copie != null) {
			c = mc.copie[x][y][z];
		}
		if (this.cameraSurX() != 0) {
			c = new Color(ct.choix.valeur.idx, c.getGreen(), c.getBlue());

		} else if (this.cameraSurY() != 0) {
			c = new Color(c.getRed(), ct.choix.valeur.idx, c.getBlue());

		} else if (this.cameraSurZ() != 0) {
			c = new Color(c.getRed(), c.getGreen(), ct.choix.valeur.idx);

		} else {
			c = mc.copie[x][y][z];
		}
		this.couleurCourante = c;

	}

	@Override
	public void loop(Game game) {
		// TODO Auto-generated method stubi
		glFogf(GL_FOG_DENSITY, 0.00001f);
		if (Keyboard.isKeyDown(Keyboard.KEY_U) && mc != null && mc.nomHabillage != null) {
			try {
				etat = this.donnerHabillage(mc.nomHabillage).donnerChoixKube(game, fbo, mc.estModelPlanche);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_T) && mc != null && mc.nomHabillage != null) {
			try {
				ChoixTexture ct = this.donnerHabillage(mc.nomHabillage).donnerChoixTexture(game, true);
				ct.afficherNon = true;
				etat = ct;
				ct.initialiser(this.cameraInverse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (etat != null) {
			try {
				etat.modifier();
				etat.gerer();

				if (etat.choix != null) {
					
					posDebutSelection= null;
					GreffonSelection.courrant=null;
					etatSelection = etat;
					etat = null;
				} else

				if (etat.quitter) {
					GreffonSelection.courrant=null;
					etat = null;
					etatSelection = null;
					posDebutSelection= null;
				}

			} catch (CouleurErreur e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.theta += 0.01f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.theta -= 0.01f;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.phi += 0.01f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.phi -= 0.01f;
		}

		this.gererMouseWheel();

		if (b.isPressed()) {
			this.gererClickSouris(game);

		}
		if (plus.isPressed()) {
			if (this.verifierPosition(+1)) {
				this.pos.addLocal(this.dir);
			}
		}
		if (moins.isPressed()) {
			if (this.verifierPosition(-1)) {
				this.pos.subtractLocal(this.dir);
			}
		}

		Widget.drawText(widgetSelection,
				"(" + (int) (pos.x - size.x) + "," + (int) (pos.y - size.y) + "," + (int) (pos.z - size.z) + ")",
				Color.blue, Color.GREEN, Color.GREEN);
		ihm.dessiner(game.shaderWidget);


		if (this.nouveauModelClasse != null) {
			mc = this.decorDeBriqueData.models.get(this.nouveauModelClasse);
			nouveauModelClasse = null;
			if (mc != null) {

				pos.set(0, 0, 0);
				try {
					obj = this.creerObjet(mc, game);
					this.size.set(mc.size);
					this.size.multLocal(-0.5f);
				} catch (CouleurErreur e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				this.distance = 1.5f * mc.size.length();
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			this.distance -= 0.5f;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			this.distance += 0.5f;
		}
		if (echap.isPressed() && executeScript != null) {
			CompiledScript script;
			Bindings bindings;
			try {
				script = compilable.compile(executeScript);
				bindings = engine.createBindings();
				bindings.put("$", this);
				script.eval(bindings);
				if (mc != null) {
					mc.vbo.delete();
					try {
						mc.initBuffer(window.game, true, null);
						this.obj = this.creerObjet(mc, window.game);
					} catch (CouleurErreur e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// executeScript = null;

		}

		Camera cam = game.getCamera();
		this.initCamera(game.getCamera());
		Vector3f dir = cam.getDirection();
		Vector3f pos = cam.getLocation();
		// System.out.println(" pos=" + pos + "dir=" + dir);

		game.setLight0Pos(pos.x, pos.y, pos.z);
		game.setLight1Pos(0, 0, 0);
		game.setLightDirection(GL11.GL_LIGHT0, dir.x, dir.y, dir.z);
		if (etat != null) {
			etat.dessiner(cam);
		}
		fboLumiere.activer(0.8f,0.8f,0.8f);
		
		// fboLumiere.resetFBO(0, 0, 1);
		if (obj != null) {
			Game.rm = Game.RenderMode.Normal;
			obj.dessiner(game.getCamera());
	if (fileNameEcran == null) {		this.dessinerSelection(); }
		}

		if (mc != null && mc.nomHabillage == null) {
			ihm.dessiner(game.shaderWidget);
		}
		dessinerAxes();
		fboLumiere.desactiver();
		Game.obscurite = 1.0f;
		fboLumiere.dessinerFBO(true, null);
		
		if (fileNameEcran != null) {
			Icone ie = new Icone(fboLumiere.width, fboLumiere.height,fboLumiere. getContent());
			ie.save(fileNameEcran);
			fileNameEcran = null;
		}

		

	}

	public void gererClickSouris(Game game) {
		int x = (int) (pos.x - size.x);
		int y = (int) (pos.y - size.y);
		int z = (int) (pos.z - size.z);

		boolean horsZone = (x < 0 || x >= mc.dx) || (y < 0 || y >= mc.dy) || (z < 0) || (z >= mc.dz);
		if (horsZone) {
			return;
		}
		if (posDebutSelection == null ) {
			posDebutSelection = pos.clone();
			return;
		}
		int debx = (int) (posDebutSelection.x - size.x);
		int deby = (int) (posDebutSelection.y - size.y);
		int debz = (int) (posDebutSelection.z - size.z);
		this.calculerCouleurCourante();
		Color c = this.couleurCourante();
		if (c == null) {
			posDebutSelection = null;
			return;
		}
		if (mc.copie == null) {
			mc.init(mc.dx, mc.dy, mc.dz);
		}
		int minX=Math.min(x, debx);
		int minY=Math.min(y, deby);
		int minZ=Math.min(z, debz);
		int maxX=Math.max(x, debx);
		int maxY=Math.max(y, deby);
		int maxZ=Math.max(z, debz);
		if (GreffonSelection.courrant != null) {
			GreffonSelection.courrant.exec(minX, minY, minZ, maxX-minX+1,maxY-minY+1,maxZ-minZ+1, mc.copie, c);
		} else {

			for(int ux=minX;ux<=maxX;ux++) {
				for(int uy=minY;uy<=maxY;uy++) {
					for(int uz=minZ;uz<=maxZ;uz++) {
						mc.copie[ux][uy][uz] = c;	
					}
				}	
			}
		
			
		}
		try {
			if (mc.vbo != null) {
				mc.vbo.delete();
			}
			mc.initBuffer(game, true, this.donnerHabillage(mc.nomHabillage));
			obj = this.creerObjet(mc, game);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		posDebutSelection = null;
	}

	public void effacer() {

	}

	public void vider() {
		if (mc == null || this.currentColorWidget == null) {
			return;
		}
		int x = (int) pos.x;
		int y = (int) pos.y;
		int z = (int) pos.z;
		
		Color o = mc.copie[x][y][z];
		for (int ux = 0; ux < mc.dx; ux++)

		{
			for (int uy = 0; uy < mc.dy; uy++)

			{
				for (int uz = 0; uz < mc.dz; uz++)

				{

					mc.copie[ux][uy][uz] = Color.BLACK;

				}

			}

		}
	}






	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	public Objet3D creerObjet(ModelClasse mc, Game g) throws CouleurErreur, Exception {
		if (mc.size == null) {
			mc.size = new Vector3f(mc.dx, mc.dy, mc.dz);
		}
		Vector3f size = mc.size.clone();
		if (mc.vbo == null) {
			mc.initBuffer(g, true, this.donnerHabillage(mc.nomHabillage));
		}
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();
		if (mc.vbo != null) {
			VBOBriqueTexture3D vbo = mc.vbo;
			VoxelTexture3D tex = mc.tex;

			BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
			translation.brique = brique;
			brique.echelle = 1.0f;
			brique.tpos.set(0, 0, 0);
		}
		System.out.println("size=" + size);
		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		obj.ajouter(translation);
		return obj;

	}

	public String retirerCategorie(String nom) {
		int idx;
		if (nom.startsWith("models.")) {
			idx = nom.indexOf(".") + 1;
		} else {
			idx = nom.indexOf("].") + 2;
		}
		return nom.substring(idx);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node != null) {
			Arbre<String> arbre = (Arbre<String>) node.getUserObject();
			if (arbre.enfants().isEmpty() && arbre.donnerParent() != null) {

				this.nouveauModelClasse = arbre.donnerParent().chemin(".");
				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					this.nouveauModelClasse += "#" + arbre.nom;
				}

				this.nouveauModelClasse = this.retirerCategorie(nouveauModelClasse);
				Log.print(this.nouveauModelClasse);

			}

		}
	}

	public String script = "";
	volatile public String executeScript = null;

	@Override
	public void terminate(Game game) {
		// TODO Auto-generated method stub
		try {
			Log.print("sauvegarde : ", cheminRessources + "/base.bin");
			SerializeTool.save(decorDeBriqueData, cheminRessources + "/base.bin");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
