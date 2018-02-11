package dadou.param;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import dadou.AbstractJoueur;
import dadou.ControlleurCamera;
import dadou.ControlleurCameraStandard;
import dadou.DecorDeBriqueDataElement;
import dadou.EtatOmbre;
import dadou.Game;
import dadou.Key;
import dadou.Log;
import dadou.Main;
import dadou.OpenGLTools;
import dadou.VoxelShaderColor;
import dadou.tools.BrickEditor;
import dadou.tools.DialogCreationMonde;
import dadou.tools.SerializeTool;
import dadou.tools.ServiceInterfaceClient;
import dadou.tools.UI;
import ihm.swing.SwingBuilderDialog;

public class GraphicsParam extends FrameParamUI implements ActionListener {


	public static GraphicsParam gp;
	private static final long serialVersionUID = 1L;
	JComboBox<String> res;

	JCheckBox pleineEcran;
	JCheckBox ombreActive;
	JCheckBox useMipmap;
	JCheckBox useQuerty;
	JComboBox<Integer> profondeurs;
	JComboBox<Integer> shadowRes;
	JButton createNewWorld;
	JButton GO;
	public DisplayMode displayMode;
	public Map<String, DisplayMode> mapDisplayMode = new HashMap<>();

	static public class DisplayModeSorter implements Comparator<DisplayMode> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(DisplayMode a, DisplayMode b) {
			// Width
			if (a.getWidth() != b.getWidth())
				return (a.getWidth() > b.getWidth()) ? 1 : -1;
			// Height
			if (a.getHeight() != b.getHeight())
				return (a.getHeight() > b.getHeight()) ? 1 : -1;
			// Bit depth
			if (a.getBitsPerPixel() != b.getBitsPerPixel())
				return (a.getBitsPerPixel() > b.getBitsPerPixel()) ? 1 : -1;
			// Refresh rate
			if (a.getFrequency() != b.getFrequency())
				return (a.getFrequency() > b.getFrequency()) ? 1 : -1;
			// All fields are equal
			return 0;
		}
	}
	
	public static void lancer(String chemin) throws Exception {
		BrickEditor.cheminRessources = chemin;
		
		GraphicsParam gp = new GraphicsParam();
		gp.begin(150, 30);

		DisplayMode modes[] = Display.getAvailableDisplayModes();
		String[] resolutions = new String[modes.length];

		Arrays.sort(modes, new GraphicsParam.DisplayModeSorter());
		for (int i = 0; i < resolutions.length; i++) {
			DisplayMode dm = modes[i];
			String res = "" + dm.getWidth() + "x" + dm.getHeight();

			gp.mapDisplayMode.put(res, dm);
			resolutions[i] = res;

		}
		gp.res = gp.comboBox("Resolution", resolutions);
		gp.pleineEcran = gp.checkBox("Fullscreen");
		gp.ombreActive = gp.checkBox("Shadow ", true);
		gp.useQuerty = gp.checkBox("Use qwerty keyboard", false);
		gp.useMipmap = gp.checkBox("Use mipmap", true);
		gp.profondeurs = gp.comboBox("Depth view", new Integer[] { 200, 25, 50,
				75, 100, 125, 150, 175, 300, 400, 900

		});
		gp.shadowRes = gp.comboBox("Shadow res", new Integer[] { 2000, 3000,
				4000, 5000, 6000, 7000 });
		gp.center = true;
		String editeur = BrickEditor.arguments.get("editeur");
		if (editeur != null && editeur.equals("true")) {
			gp.createNewWorld = gp.button("Create world");
			gp.createNewWorld.addActionListener(gp);
		}
		gp.end("Start");

		gp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public GraphicsParam() throws LWJGLException {

	}

	@Override
	public void valider() {

		/*
		 * String shadowSize = (String) this.shadowMapRes.getSelectedItem();
		 * Game.shadowMapSize = Integer.parseInt(shadowSize);
		 */
		this.displayMode = this.mapDisplayMode.get(res.getSelectedItem());
		if (this.ombreActive.isSelected()) {
			Game.etatOmbre = EtatOmbre.OmbreActive;
			Game.shadowMapWidth = (int) this.shadowRes.getSelectedItem();
			Game.shadowMapHeight = (int) this.shadowRes.getSelectedItem();
		} else {
			Game.etatOmbre = EtatOmbre.OmbreInactive;
			Game.shadowMapWidth = 10;
			Game.shadowMapHeight = 10;

		}
		Game.profondeur = (Integer) profondeurs.getSelectedItem();
		Game.profondeur = Game.profondeur * Game.profondeur;
		Game.pleineEcran = this.pleineEcran.isSelected();

		ControlleurCamera.controlleur = new ControlleurCameraStandard();
		try {
			this.demarer();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
		this.setVisible(false);

	}

	public void demarer() throws Exception {

		final BrickEditor be = new BrickEditor();
		if (Game.logError != null) {
			System.setErr(new PrintStream(Game.logError));
		}
		if (Game.logOut != null) {
			System.setOut(new PrintStream(Game.logOut));
		}
		be.modeEditeur = false;
		VoxelShaderColor.renduLumiereDiffere = true;

		be.chargerRessource();

		GraphicsParam.gp = this;
		if (this.useQuerty.isSelected()) {
			ControlleurCameraStandard.avancer = new Key(Keyboard.KEY_W);
			ControlleurCameraStandard.reculer = new Key(Keyboard.KEY_S);
			ControlleurCameraStandard.gauche = new Key(Keyboard.KEY_A);
			ControlleurCameraStandard.droite = new Key(Keyboard.KEY_D);

		}

		OpenGLTools.useMipmap = this.useMipmap.isSelected();


		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					(be).start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (this.createNewWorld == e.getSource()) {

			DialogCreationMonde dialog = new DialogCreationMonde(null,
					BrickEditor.cheminRessources);
			dialog.action = () -> {

				try {
					String nom = dialog.nomMonde();
					if (nom.endsWith(".wld")) {
						String nomFichier = BrickEditor.cheminRessources + "/"
								+ nom;
						if (new File(nomFichier).exists()) {

							return;

						}
						try {

							DecorDeBriqueDataElement data = DecorDeBriqueDataElement
									.creer(dialog.niveau(),
											BrickEditor.elementTaille,
											nomFichier);
							data.nomHabillage = dialog.nomHabillage();
							SerializeTool.save(data, nomFichier);
							valider();
						} catch (IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}

					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			};
			return;
		}
		this.valider();

	}

}
