package dadou.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.AWTGLCanvas;

import com.jme.input.InputSystem;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;
import com.jme.math.Vector2f;
import com.jme.system.lwjgl.LWJGLStandardCursor;

import dadou.Arbre;
import dadou.ControlleurCamera;
import dadou.ControlleurCameraEditeur;
import dadou.ControlleurCameraStandard;
import dadou.DecorDeBriqueDataElement;
import dadou.Key;
import dadou.Main;
import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.Tableau;
import dadou.VoxelShaderColor;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventNode;
import dadou.event.GameEventTree;
import dadou.greffon.Boite;
import dadou.greffon.CylindreX;
import dadou.greffon.CylindreY;
import dadou.greffon.CylindreZ;
import dadou.greffon.GreffonForme;
import dadou.greffon.Montagne;
import dadou.greffon.Sphere;

import dadou.greffon.TunelX;
import dadou.greffon.TunelY;
import dadou.greffon.TunelZ;
import dadou.ihm.Comportement;
import dadou.ihm.IHMSwingGame;

import dadou.son.Emeteurs;
import dadou.test.TestSwingFrameInLWJGL;
import dadou.tools.monde.SelectionMonde;

public class BrickEditorSwing extends IHMSwingGame<BrickEditor>
		implements TreeSelectionListener, ActionListener, FocusListener, WindowListener {
	public int screenWidth;
	public int screenHeight;

	public int ihmWidth;

	public BrickEditorSwing(BrickEditor comportement, int canvasWidth, int canvasHeight, int screenWidth,
			int screenHeight) {
		super(comportement, canvasWidth, canvasHeight, screenWidth, screenHeight);
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;

	}

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		GreffonForme.greffons.add(new Boite());
		GreffonForme.greffons.add(new Sphere());
		GreffonForme.greffons.add(new Montagne());
		GreffonForme.greffons.add(new TunelZ());
		GreffonForme.greffons.add(new TunelX());
		GreffonForme.greffons.add(new TunelY());
		GreffonForme.greffons.add(new CylindreX());
		GreffonForme.greffons.add(new CylindreY());
		GreffonForme.greffons.add(new CylindreZ());
		

		try {
			Main.main = (Main) Class.forName("monde.Main").newInstance();
		} catch (Throwable e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					BrickEditor a = new BrickEditor();
					a.modeEditeur = true;
					VoxelShaderColor.renduLumiereDiffere = true;

					a.cheminRessources = args[0];
					System.out.println(" cheminRessources =" + a.cheminRessources);
					//a.sic = new ServiceInterfaceClient(args[1], args[2], true);
				

					a.chargerRessource();
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					int width = (int) screenSize.getWidth();
					int height = (int) screenSize.getHeight() - 100;
					int ihmWidth = 300;

					BrickEditorSwing window = new BrickEditorSwing(a, width - ihmWidth, height, width, height);

					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	GameEventLeaf leaf;
	GameEventNode root;
	JTree tree;

	JTextArea output;
	public PrintStream ps;

	public void chemin(Arbre<String> arbre, List<Object> result, int idx, List<String> chemin) {
		if (arbre == null) {
			return;
		}
		result.add(arbre.node);
		if (idx == chemin.size()) {
			return;
		}

		this.chemin(arbre.enfant(chemin.get(idx)), result, idx + 1, chemin);

	}

	public List<String> chemin;

	public void reloadTree() {

		EventQueue.invokeLater(new Runnable() {

			public void run() {
				GameEventNode e;
				try {
					// save();
					e = comportement.decorDeBriqueData.creerGameEventNode(comportement);
					e.createMutableNodeTree();

					Arbre<String> arbre = comportement.decor.DecorDeBriqueData.creerArbreCameraPosition();
					Arbre<Object> objets = comportement.decorDeBriqueData.creerArbreObjets(comportement);
					arbre.createMutableNodeTree();
					objets.createMutableNodeTree();
					e.node.add(arbre.node);
					e.node.add(objets.node);
					TreePath tp = null;
					if (chemin != null) {
						List<Object> lst = new ArrayList<>();

						lst.add(e.node);
						// lst.add(arbre.node);
						chemin(arbre, lst, 1, chemin);
						Object path[] = new Object[lst.size()];
						for (int i = 0; i < lst.size(); i++) {
							path[i] = lst.get(i);
						}

						tp = new TreePath(path);

					}

					root = e;

					tree.setModel(new DefaultTreeModel(e.node));
					if (tp != null) {
						tree.expandPath(tp);
					}

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

	}

	public void init() throws Exception {
		super.init();
		this.frame.setTitle(comportement.SelectionMonde.nom);
		this.comportement.swingEditor = this;
		int ihmWidth = this.screenWidth - this.width;
		tree = new JTree();
		tree.setModel(null);
		JScrollPane treeView = new JScrollPane(tree);

		this.frame.getContentPane().setLayout(null);

		tree.setBounds(0, 0, ihmWidth, screenHeight);
		treeView.setBounds(0, 0, ihmWidth, screenHeight);
		// treeView.setLayout(null);
		this.frame.getContentPane().add(treeView);

		tree.addTreeSelectionListener(this);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setFocusable(true);
		// AWTGLCanvas glCanvas;

		// canvas.set
		this.frame.addWindowListener(this);
		// this.frame.addMouseListener(this);
		// Mouse.setClipMouseCoordinatesToWindow(true);
		// this.frame.addMouseMotionListener(this);
		this.frame.repaint();
		// MouseInput.setProvider(InputSystem.INPUT_SYSTEM_AWT);
		MouseInput.get().setCursorVisible(true);

		// MouseInput.get().addListener(this);
		new BrickEditorMenu(this);

		/*
		 * JPopupMenu menu = new JPopupMenu(); copier = new JMenuItem("Copier");
		 * coller = new JMenuItem("Coller"); supprimer = new
		 * JMenuItem("Supprimer"); activerPrint = new JMenuItem("Activer print"
		 * ); desactiverPrint = new JMenuItem("Desactiver print");
		 * menu.add(copier); menu.add(coller); menu.add(supprimer);
		 * menu.add(activerPrint); menu.add(desactiverPrint);
		 * tree.setComponentPopupMenu(menu); copier.addActionListener(this);
		 * coller.addActionListener(this); activerPrint.addActionListener(this);
		 * desactiverPrint.addActionListener(this);
		 * supprimer.addActionListener(this);
		 */
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean canvasHasFocus() {
		return canvas.hasFocus();
	}

	public void selectModelInstance(final String monde, final String nomClasse, final String nom) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					TreePath tp = new TreePath(root.getGameEventNodeForPath("monde", "objets", nomClasse, nom));
					tree.setSelectionPath(tp);
				} catch (Exception e) {

				}
			}
		});

	}

	public void arreter() {

		ControlleurCamera.controlleur = new ControlleurCameraEditeur();
		comportement.mondeInterface.listMutator.clear();
		comportement.mondeInterface.grapheDebug = null;
		comportement.pause = false;
		comportement.mondeInterface.zoneDetections = new HashMap<>();
		comportement.espace.reset();
		comportement.scc.tremblement = false;
		comportement.verouillerRotationEtTranslationCamera = false;
		ControlleurCamera.controlleur.figerRotationEtTranslationCamera(false, comportement);

		Emeteurs em = comportement.mondeInterface.joueur(null).emeteurs;
		if (em != null) {
			em.stop();
			comportement.mondeInterface.joueur(null).emeteurs = null;
		}
		comportement.mondeInterface.active = false;
		comportement.mondeInterface.data = new HashMap<>();

		comportement.decor.DecorDeBriqueData.arreter(comportement.mondeInterface);
		comportement.decorDeBriqueData.arreter();
		try {
			comportement.decor.initialiserModelInstances(comportement);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comportement.ajouterAction(() -> {
		
			comportement.mondeInterface.tableau = null;
			comportement.mondeInterface.tableaux = new HashMap<>();
			comportement.mondeInterface.rechargerMonde = false;
			comportement.mondeInterface.ajouterKubes.clear();
			try {
				comportement.SelectionMonde.nom = comportement.decorDeBriqueData.mondeCourant;
				comportement.chargerMonde(comportement.SelectionMonde.nom);
				comportement.processMenuKey = true;
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

		});

	}

	List<ModelClasse> copies;

	public void initPrint(GameEventNode gen, boolean r) {
		if (gen instanceof GameEventLeaf) {
			((GameEventLeaf) gen).print = r;
			return;

		}
		for (GameEventNode a : ((GameEventTree) gen).children.values()) {

			this.initPrint(a, r);

		}
	}

	public boolean contientPrint(GameEventNode gen) {
		if (gen instanceof GameEventLeaf) {
			return ((GameEventLeaf) gen).print;

		}
		for (GameEventNode a : ((GameEventTree) gen).children.values()) {

			if (this.contientPrint(a)) {
				return true;
			}

		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void save() {
		try {
			if (comportement.decor == null) {
				return;
			}
			comportement.decor.DecorDeBriqueData.cameraPosition = comportement.scc.creerCameraPosition();
			comportement.sauvergarder();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		if (this.comportement.mondeInterface.active) {
			return;
		}
		this.comportement.swingEditor = null;
		if (UI.confirm("Sauvegarder le jeux ??", this.frame)) {
			save();
		}
		//

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	Vector2f pos = new Vector2f();

}
