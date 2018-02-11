package dadou.tools;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.ModelInstance;
import dadou.tools.construction.AfficherBoxSelection;
import ihm.swing.SwingBuilderDialog;

public class DialogModifierDonnees extends JDialog implements WindowListener,ListSelectionListener,CaretListener {
	BrickEditorSwing bes;
	
	JList<DonneeText> listeDonnee;
	JTextArea valeur;
	Map<String,String> donnees;
	public DialogModifierDonnees(JFrame parent, BrickEditorSwing bes, 	Map<String,String> donnees) {
		super(parent);
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(200, 30);
		this.donnees=donnees;
		this.bes = bes;
		DefaultListModel<DonneeText> m = new DefaultListModel<>();
		for(Map.Entry<String, String> e:donnees.entrySet()) {
			DonneeText dt = new DonneeText();
			dt.nom = e.getKey();
			dt.valeur =e.getValue();
			m.addElement(dt);
			
		}
		

		sb.beginX(() -> {
	
			listeDonnee = new JList<>();
			sb.setSize(150, 600);
			sb.add(listeDonnee);
			
			listeDonnee.setModel(m);
			valeur = new JTextArea();
			valeur.addCaretListener(this);
			listeDonnee.addListSelectionListener(this);
			
			JScrollPane valeurScrollPane = new JScrollPane(valeur);
			sb.setSize(400, 600);
			sb.add(valeurScrollPane);
		});
		sb.openIn("Gerer donnees", this);
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
		for(int i=0;i < this.listeDonnee.getModel().getSize();i++) {
			DonneeText dt = this.listeDonnee.getModel().getElementAt(i);
			donnees.put(dt.nom,dt.valeur);
			
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

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		DonneeText dt =this.listeDonnee.getSelectedValue();
		if (dt == null) {
			this.valeur.setText("");
		} else {
			this.valeur.setText(dt.valeur);
		}
		
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO Auto-generated method stub
		DonneeText dt =this.listeDonnee.getSelectedValue();
		if (dt == null) {
			return;
		} else {
			dt.valeur = this.valeur.getText();
		}
		
	}

}
