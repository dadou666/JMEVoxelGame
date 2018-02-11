package dadou.tools;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Toolkit;
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

public class DialogCreationMonde extends JDialog implements WindowListener,ActionListener {

	/**
	 * 
	 */

	JComboBox<String> cbNomHabillage;
	JTextField nomMonde;
	JComboBox<String> cbTaille;
	
	

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
	public String nomMonde() {
		return nomMonde.getText();
	}
	public DialogCreationMonde(Frame parent, String cheminRessource) {
		super(parent);
	
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(120, 30);
		

		sb.beginY(() -> {
			 sb.add( cbNomHabillage = new JComboBox<>());
			 this.initHabillages(cheminRessource);
			 sb.add( nomMonde = new JTextField());
			 sb.add(cbTaille = new JComboBox<>(new String[] {"512","256","128" }));
			 sb.add(valider = new JButton("OK"));
			 valider.addActionListener(this);
			 

		});
		this.center();
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
	public int niveau() {
		String s= (String) this.cbTaille.getSelectedItem();
		if (s == null) {
			return 4;
		}
		if (s.equals("512")) {
			return 5;
		}
		if (s.equals("256")) {
			return 4;
		}
		if (s.equals("128")) {
			return 3;
		}
		return 4;
		
	}
	private void center() {
		int x, y;
		x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
		y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
		this.setLocation(x, y);
	}

}
