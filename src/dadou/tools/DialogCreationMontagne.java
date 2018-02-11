package dadou.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

import ihm.swing.SwingBuilder;
import ihm.swing.SwingBuilderDialog;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import com.jme.math.Vector3f;

import dadou.Arbre;
import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.Habillage;
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelClasseDecor;
import dadou.ModelEvent;
import dadou.NomTexture;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.greffon.Montagne;

public class DialogCreationMontagne extends JDialog implements WindowListener, ActionListener {

	/**
	 * 
	 */

	JTextField min;

	JComboBox<String> kubeSommet;
	JComboBox<String> kubeAutre;

	private static final long serialVersionUID = 1L;
	BrickEditorSwing bes;
	JButton valider;
	Vector<String> choix;
	Montagne montagne;

	public DialogCreationMontagne(Montagne montagne, JFrame parent, BrickEditorSwing bes) {
		super(parent);
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(120, 30);
		this.montagne = montagne;
		this.bes = bes;
		Habillage h = bes.comportement.donnerHabillage(bes.comportement.decor.DecorDeBriqueData.nomHabillage);
		choix = new Vector<String>();
		choix.addAll(h.valeurs.keySet());
		DecorDeBriqueDataElement tmp=bes.comportement.decor.DecorDeBriqueData;
		sb.beginY(() -> {
		

			sb.beginX(() -> {
				sb.add(new JLabel("Kube sommet"));
				sb.add(kubeSommet = new JComboBox<>(choix));
				if (tmp.nomKubeSommet!= null) {
					kubeSommet.setSelectedItem(tmp.nomKubeSommet);
				}

			});
			sb.beginX(() -> {
				sb.add(new JLabel("Kube autre"));
				sb.add(kubeAutre = new JComboBox<>(choix));
				if (tmp.nomKubeAutre != null) {
					kubeAutre.setSelectedItem(tmp.nomKubeAutre);
				}

			});
			sb.setSize(240, 30);
			sb.add(valider = new JButton("Valider"));
			valider.addActionListener(this);
		});
		sb.openIn("Dimension", this);
		this.addWindowListener(this);

	}

	static public void main(String[] args) {

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

		bes.comportement.decor.DecorDeBriqueData.nomKubeAutre = (String) this.kubeAutre.getSelectedItem();
		bes.comportement.decor.DecorDeBriqueData.nomKubeSommet = (String) this.kubeSommet.getSelectedItem();
		this.dispose();

	}
}
