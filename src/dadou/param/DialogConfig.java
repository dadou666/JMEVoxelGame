package dadou.param;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import dadou.EtatOmbre;
import dadou.Game;
import dadou.tools.BrickEditorSwing;
import dadou.tools.graphics.ConfigValues;
import ihm.swing.SwingBuilderDialog;

public class DialogConfig extends ParamUI implements WindowListener {
	JTextField profondeur;
	JTextField brouillard;
	JTextField numTriAvantTest;
	JTextField numObjetMobileAvantTest;
	JTextField skybox;
	JCheckBox ombreActive;
	// JComboBox<String> shadowMode;
	BrickEditorSwing bes;

	public DialogConfig(JFrame parent, BrickEditorSwing bes) {
		super(parent);
		this.bes = bes;
		this.begin(120, 30);
		ConfigValues config = bes.comportement.config.config;

		profondeur = this.textField("Profondeur", "" + config.profondeur);
		brouillard = this.textField("Brouillard", "" + config.brouillard);
		numTriAvantTest = this.textField("NumTriAvantTest", "" + config.numTriAvantTest);
		skybox = this.textField("Skybox", "" + this.bes.comportement.decor.DecorDeBriqueData.skyBox);
		this.ombreActive = this.checkBox("Ombre active", true);
		this.end("Configurer affichage");

		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public void valider() {
		ConfigValues config = bes.comportement.config.config;
		config.profondeur = Float.parseFloat(profondeur.getText());
		Game.profondeur =  config.profondeur * config.profondeur;
		Game.fogDensity = Float.parseFloat(brouillard.getText());
		config.brouillard = Game.fogDensity;
		config.numTriAvantTest = Integer.parseInt(numTriAvantTest.getText());
		bes.comportement.decor.DecorDeBriqueData.configValues = config;
		bes.comportement.decor.DecorDeBriqueData.skyBox = skybox.getText();
		bes.comportement.decor.action.nomSkyBox = skybox.getText();
		if (this.ombreActive.isSelected()) {
			Game.etatOmbre = EtatOmbre.OmbreActive;
		} else {
			Game.etatOmbre = EtatOmbre.OmbreInactive;

		}

	

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
