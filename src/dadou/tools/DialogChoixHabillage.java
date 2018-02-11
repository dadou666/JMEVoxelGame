package dadou.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import ihm.swing.SwingBuilder;
import ihm.swing.SwingBuilderDialog;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jme.math.Vector3f;

import dadou.Arbre;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.ihm.Action;

public class DialogChoixHabillage extends JDialog implements WindowListener,ActionListener {

	/**
	 * 
	 */

	JComboBox<String> cbNomHabillage;
	

	public Action action;
	public JButton valider;
	public void initHabillages(String cheminRessource) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel();
		for(File f:new File(cheminRessource).listFiles()) {
			if (f.getName().endsWith(".hab")) {
				model.addElement(f.getName());
			}
			
		}
		this.cbNomHabillage.setModel(model);
		
		
	}
	public DialogChoixHabillage(JFrame parent, String cheminRessource) {
		super(parent);
	
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(120, 30);
		

		sb.beginY(() -> {
			 sb.add( cbNomHabillage = new JComboBox<>());
			 this.initHabillages(cheminRessource);
			 sb.add(valider = new JButton());
			 valider.addActionListener(this);
			 

		});
		sb.openIn("Dimension", this);
		this.addWindowListener(this);

	}


	public String nomHabillage() {
		return cbNomHabillage.getItemAt(cbNomHabillage.getSelectedIndex());
	}



	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

		

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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (action != null) {
			action.execute();
		}
		
	}

}
