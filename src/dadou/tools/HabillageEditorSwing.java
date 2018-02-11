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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
import dadou.FBORenduLumiereDiffere;
import dadou.FBOShadowMap;
import dadou.Game;
import dadou.Habillage;
import dadou.Key;
import dadou.Log;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.Objet3D;
import dadou.Shader;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VoxelShader;
import dadou.VoxelShaderColor;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.graphe.GrapheLigne;
import dadou.ihm.Action;
import dadou.ihm.Comportement;
import dadou.ihm.IHM;
import dadou.ihm.IHMSwingGame;
import dadou.ihm.Widget;
import dadou.parallele.Parallele;
import dadou.parallele.StartException;

public class HabillageEditorSwing implements Comportement,
		TreeSelectionListener {
	public JTree tree;
	public int screenHeight;
	public int screenWidth;

	public Quaternion rotationX = new Quaternion();
	public Quaternion rotationY = new Quaternion();
	public float theta = 0.0f;
	public float phi = 0.0f;
	public float distance = 2.5f;
	Quaternion tmp = new Quaternion();
	public Vector3f size = new Vector3f();
	public Vector3f pos = new Vector3f();
	public HabillageEditorMenu menu;
	public Map<Habillage, HabillageIHM> ihms = new HashMap<>();
	public HabillageIHM ihm;
	private Action action;
	private Object lock = new Object();

	public void setAction(Action a) {
		synchronized (lock) {
			this.action = a;
		}
	}

	public Button b = new Button(2);

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

	public IHMSwingGame window;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					VoxelShaderColor.renduLumiereDiffere = true;
					HabillageEditorSwing a = new HabillageEditorSwing();
					a.cheminRessources = args[0];
					BrickEditor.cheminRessources = args[0];
					Dimension screenSize = Toolkit.getDefaultToolkit()
							.getScreenSize();
					int width = (int) screenSize.getWidth();
					int height = (int) screenSize.getHeight() - 100;
					a.screenWidth = width;
					a.screenHeight = height;
					a.window = new IHMSwingGame(a, width - 200, height,
							a.screenWidth, a.screenHeight);

					a.init(a.window);

					a.window.frame
							.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					a.window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public HabillageIHM creerHabillageIHM(Habillage a, int idx) {
		HabillageIHM ai = (this.ihms.get(a));

		if (ai != null) {
			this.ihm = ai;
			ai.idxTexture = idx;
			ai.mettreAjourTexture();
			return ai;
		}
		ai = new HabillageIHM(a, window.game);
		ai.idxTexture = idx;
		ai.mettreAjourTexture();
		this.ihms.put(a, ai);
		this.ihm = ai;
		return ai;

	}

	public String cheminRessources;

	public void creerArbreHabillage(Habillage h, Arbre<Object> arbre, String nom) {
		Arbre<Object> textures = arbre.ajouter("textures");
		textures.affichage = "textures[" + h.noms().size() + "]";
		textures.valeur = this.menu.actionsPourTextures(h, this.window.game, textures);

		this.setAction(() -> {

			HabillageIHM a = this.creerHabillageIHM(h, 0);
			
			a.nom = nom;
		});

		for (NomTexture nt : h.noms()) {
			Arbre<Object> arbreTexture = textures.ajouter(nt.nom);
			arbreTexture.valeur = this.menu.actionsPourTexture(h,
					this.window.game, nt, textures);

		}
		Arbre<Object> valeurs = arbre.ajouter("valeurs");
		valeurs.valeur = menu.actionsPourValeurs(valeurs, this.window.game, h,
				nom);
		for (Map.Entry<String, Color> entry : h.valeurs.entrySet()) {
			Arbre<Object> arbreValeur = valeurs.ajouter(entry.getKey());
			arbreValeur.valeur = menu.actionsPourValeur(h, this.window.game,
					entry.getKey(), valeurs);
		}
	}

	public void init(IHMSwingGame w) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		tree = new JTree();
		tree.setModel(null);

		JScrollPane treeView = new JScrollPane(tree);

		tree.setBounds(0, 0, screenWidth - w.width, screenHeight);
		treeView.setBounds(0, 0, screenWidth - w.width, screenHeight);
		// treeView.setLayout(null);
		w.frame.getContentPane().add(treeView);
		tree.addTreeSelectionListener(this);

		// tree.addMouseListener(menu);
		// w.frame.addWindowListener(this);

		menu = new HabillageEditorMenu(this);
		tree.addMouseListener(menu);
		Arbre<Object> arbre = new Arbre<>(null);
		arbre.nom = "habillages";
		arbre.valeur = menu.actionsPourHabillages(arbre);

		SwingUtilities.invokeLater(() -> {
			Parallele<HabillageChargeur> p = new Parallele<>();
			try {
				p.start(4);
				p.begin();
				for (File f : new File(cheminRessources).listFiles()) {
					if (f.getName().endsWith(".hab")) {
						HabillageChargeur hc = new HabillageChargeur();
						hc.f = f;
						p.add(hc);
					}
				}
				p.end();
				p.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (HabillageChargeur hc : p.list) {

				Arbre<Object> habillage = arbre.ajouter(hc.f.getName());

				Habillage h = hc.h;
				h.nomHabillage =hc.f.getName();
				// Log.print("h ",h);
				habillage.valeur = this.menu.actionsPourHabillage(habillage, h);

				this.creerArbreHabillage(h, habillage, hc.f.getName());

			}
			arbre.createMutableNodeTree();
			treeModel = new DefaultTreeModel(arbre.node);
			tree.setModel(treeModel);
		});

	}

	Shader shader;
	public Vector3f dir = new Vector3f();

	Button button = new Button(0);
	public DefaultTreeModel treeModel;
	public String pourEcriture;

	FBORenduLumiereDiffere fbo;

	@Override
	public void init(Game game) {
		fbo = new FBORenduLumiereDiffere();
		float fx = 1.0f / (float) game.getWidth();
		float fy = 1.0f / (float) game.getHeight();
		fbo.init(game.getWidth(), game.getHeight());

		Color copie[][][] = new Color[1][1][1];
		copie[0][0][0] = Color.RED;
		Game.rm = Game.RenderMode.Normal;
		ModelClasse mc = new ModelClasse();
		mc.copie = copie;
		mc.dx = 1;
		mc.dy = 1;
		mc.dz = 1;
		FBOShadowMap.shadowMap = new FBOShadowMap();
		FBOShadowMap.shadowMap.init(1, 1, game);

	}

	public Objet3D obj;
	public ModelClasse mc;

	@Override
	public void loop(Game game) {
		synchronized (lock) {

			if (action != null) {
				action.execute();
				action = null;
			}
		}
		// TODO Auto-generated method stub
		glFogf(GL_FOG_DENSITY, 0.00001f);
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

		fbo.activer();
		GL11.glClearColor(0.7f, 0.7f, 0.7f, 0.0f);
		Camera cam = game.getCamera();
		this.initCamera(game.getCamera());
		Vector3f dir = cam.getDirection();
		Vector3f pos = cam.getLocation();
		// System.out.println(" pos=" + pos + "dir=" + dir);

		game.setLight0Pos(pos.x, pos.y, pos.z);
		game.setLight1Pos(0, 0, 0);
		game.setLightDirection(GL11.GL_LIGHT0, dir.x, dir.y, dir.z);
		if (ihm != null) {
			ihm.dessiner();
		}
		fbo.desactiver();
		fbo.dessinerFBO(true, null);

	}

	public void effacer() {

	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		if (node != null) {
			if (node.getUserObject() == null) {
				return;
			}
			Arbre<Object> arbre = (Arbre<Object>) node.getUserObject();
			if (arbre.valeur == null) {
				return;
			}
			ArbreMenuAction ama = (ArbreMenuAction) arbre.valeur;
			if (ama.selectionAcion != null) {
				ama.selectionAcion.execute();
			}

		}

	}

	@Override
	public void terminate(Game g) {
		// TODO Auto-generated method stub
		for (Map.Entry<Habillage,HabillageIHM> et : this.ihms.entrySet()) {
			try {
				String nom = et.getKey().nomHabillage;
				if (nom == null) {
					Log.print(" nom = null");
					nom = "null.hab";
				}
					SerializeTool.save(et.getValue().habillage, new File(
							BrickEditor.cheminRessources, nom));
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

}
