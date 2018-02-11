package dadou.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.List;

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
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.NomTexture;

public class DialogCreationModel extends JDialog implements WindowListener, ActionListener {

	/**
	 * 
	 */

	JTextField dimX;
	JTextField dimY;
	JTextField dimZ;
	JTextField nomModel;
	JTextField nomDossier;
	JCheckBox estPlanche;
	JComboBox<String> comboBox;
	private static final long serialVersionUID = 1L;
	ModelEditorSwing me;
	ModelEditorMenu menu;
	JButton valider;

	public void initComboBox() {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		File[] files = (new File(this.me.cheminRessources).listFiles());
		model.addElement(null);
		for (File f : files) {
			if (f.getName().endsWith(".hab")) {
				model.addElement(f.getName());
			}
		}
		comboBox.setModel(model);

	}

	public DialogCreationModel(JFrame parent, ModelEditorSwing me, ModelEditorMenu menu) {
		super(parent);
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(120, 30);
		this.me = me;
		this.menu = menu;

	
		sb.beginY(() -> {
			sb.beginX(() -> {
				sb.add(new JLabel("Nom dossier"));
				sb.add(nomDossier = new JTextField());

			});
			sb.beginX(() -> {
				sb.add(new JLabel("Nom model"));
				sb.add(nomModel = new JTextField());
		
				if (menu != null) {
					ModelClasse mcCopie = this.me.decorDeBriqueData.models

							.get(menu.nomCopie);
					nomModel.setText(mcCopie.nomSprite3D());
				}
			
			});
			sb.beginX(() -> {
				sb.add(new JLabel("Nom habillage"));
				sb.add(this.comboBox = new JComboBox<>());
				this.initComboBox();

			});
			if (menu == null) {
				sb.beginX(() -> {
					sb.add(new JLabel("Dim X"));
					sb.add(dimX = new JTextField());

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Dim Y"));
					sb.add(dimY = new JTextField());

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Dim Z"));
					sb.add(dimZ = new JTextField());

				});
				sb.beginX(() -> {
					sb.add(this.estPlanche = new JCheckBox("Planche"));

				});
			}
			sb.setSize(240, 30);
			sb.add(valider = new JButton("Valider"));
			valider.addActionListener(this);
		});
		sb.openIn("Dimension", this);
		this.addWindowListener(this);

	}

	static public void main(String[] args) {
		new DialogCreationModel(null, null, null);
		System.out.println("Ok");
	}

	public int getDimX() {
		return Integer.parseInt(this.dimX.getText());
	}

	public int getDimY() {
		return Integer.parseInt(this.dimY.getText());
	}

	public int getDimZ() {
		return Integer.parseInt(this.dimZ.getText());
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
		if (me != null) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) me.tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();
				String nomDossier = this.nomDossier.getText();
				Arbre<String> nouveauArbre = null;
				if (!nomDossier.trim().isEmpty()) {
					nouveauArbre = arbre.ajouter(nomDossier.split("\\."), this.nomModel.getText());
				} else {

					nouveauArbre = arbre.ajouter(this.nomModel.getText());
				}
				if (nouveauArbre == null) {
					UI.warning("Model existant", this);
					return;
				}
				me.treeModel.reload(arbre.node);
				String nomClasse = me.retirerCategorie(nouveauArbre.donnerParent().chemin("."));

				String nomModele = nomClasse + "#" + nouveauArbre.nom;

				Log.print(" ajout ", nomModele);

				ModelClasse mc = null;
				if (menu == null) {
					mc = new ModelClasse();
					mc.init(this.getDimX(), this.getDimY(), this.getDimZ());
					if (this.comboBox.getSelectedItem() != null) {
						mc.nomHabillage = (String) this.comboBox.getSelectedItem();
					}
					mc.estModelPlanche = this.estPlanche.isSelected();

				} else {
					ModelClasse mcCopie = this.me.decorDeBriqueData.models.get(menu.nomCopie);
					mc = mcCopie.cloner();
					if (me.decorDeBriqueData.events != null) {
						ModelEvent evt = me.decorDeBriqueData.events.get(nomClasse);
						if (evt == null) {
							ModelEvent evtCopie = me.decorDeBriqueData.events.get(menu.nomModelClassCopie);
							if (evtCopie != null) {
								evt = SerializeTool.clone(evtCopie);
								me.decorDeBriqueData.events.put(menu.nomModelClassCopie, evt);
							}

						}

					}

				}

				mc.nomModele = nomClasse;

				me.decorDeBriqueData.ajouter(mc, nomModele);

			}
		}
	}

}
