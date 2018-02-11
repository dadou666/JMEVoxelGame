package dadou.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
import dadou.NomTexture;
import dadou.ihm.Action;

public class DialogModificationValeurHabillage extends JDialog implements WindowListener, ActionListener {

	/**
	 * 
	 */

	JComboBox<NomTexture> fX;

	JComboBox<NomTexture> fY;

	JComboBox<NomTexture> fZ;

	public void initComboBox(JComboBox<NomTexture> cb, List<NomTexture> nomTextures) {
		DefaultComboBoxModel<NomTexture> model = new DefaultComboBoxModel<NomTexture>();
		NomTexture ntVide = new NomTexture();
		ntVide.idx = 0;
		ntVide.nom = null;
		model.addElement(ntVide);
		for (NomTexture nt : nomTextures) {
			model.addElement(nt);
		}
		cb.setModel(model);

	}

	JTextField nomHabillage;
	JButton valider;

	public int donnerFaceX() {
		int r= fX.getItemAt(fX.getSelectedIndex()).idx;
		return r;
	}

	public int donnerFaceY() {
		return fY.getItemAt(fY.getSelectedIndex()).idx;
	}

	public int donnerFaceZ() {
		return fZ.getItemAt(fZ.getSelectedIndex()).idx;
	}

	private static final long serialVersionUID = 1L;

	public Action action;

	
	int idxNom(List<NomTexture> nomTextures,String nom) {
		if (nom == null) {
			return 0;
		}
		for(int i=0;i < nomTextures.size() ;i++) {
			if (nomTextures.get(i).nom.equals(nom)) {
				return i+1;
			}
		}
		return 0;
	}
	public DialogModificationValeurHabillage(JFrame parent, String nom, List<NomTexture> nomTextures,String nomX,String nomY,String nomZ) {
		super(parent);

		SwingBuilderDialog sb = new SwingBuilderDialog();

		sb.beginY(() -> {
			sb.setSize(240, 30);
			sb.beginX(() -> {
				sb.add(new JLabel(nom));

			});
			sb.setSize(120, 30);
			sb.beginX(() -> {
				sb.add(new JLabel("Face X"));
				sb.add(fX = new JComboBox<>());

				this.initComboBox(fX, nomTextures);
				fX.setSelectedIndex(this.idxNom(nomTextures, nomX));
			});
			sb.beginX(() -> {
				sb.add(new JLabel("Face Y"));
				sb.add(fY = new JComboBox<>());
				this.initComboBox(fY, nomTextures);
				fY.setSelectedIndex(this.idxNom(nomTextures, nomY));
			});
			sb.beginX(() -> {
				sb.add(new JLabel("Face Z"));
				sb.add(fZ = new JComboBox<>());
				this.initComboBox(fZ, nomTextures);
				fZ.setSelectedIndex(this.idxNom(nomTextures, nomZ));
			});
			sb.setSize(240, 30);
			sb.beginX(() -> {
				sb.add(valider = new JButton("Valider"));

				valider.addActionListener(this);

			});

		});
		sb.openIn("Dimension", this);
		this.addWindowListener(this);

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
