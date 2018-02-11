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
import dadou.Habillage;
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelClasseDecor;
import dadou.ModelEvent;
import dadou.NomTexture;
import dadou.VoxelTexture3D.CouleurErreur;

public class DialogCreationModelTexte extends JDialog implements WindowListener, ActionListener {

	/**
	 * 
	 */

	JTextField texte;
	JComboBox<String> orientation;
	JTextField nomModel;
	JTextField nomDossier;
	JCheckBox estPlanche;
	JCheckBox estDecor;
	JComboBox<String> comboBox;
	JComboBox<String> nomKube;
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

	public DialogCreationModelTexte(JFrame parent, ModelEditorSwing me, ModelEditorMenu menu) {
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

			});
			sb.beginX(() -> {
				sb.add(new JLabel("Nom habillage"));
				sb.add(this.comboBox = new JComboBox<>());
				this.initComboBox();
				this.comboBox.addActionListener(this);

			});
			if (menu == null) {
				sb.beginX(() -> {
					sb.add(new JLabel("Texte"));
					sb.add(texte = new JTextField());

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Orientation"));
					sb.add(orientation = new JComboBox<>(new String[] { "X", "Y", "Z" }));

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Nom kube"));
					sb.add(this.nomKube = new JComboBox<>(new String[] {}));
					

				});
				sb.beginX(() -> {
					sb.add(this.estPlanche = new JCheckBox("Planche"));

				});
				sb.beginX(() -> {
					sb.add(this.estDecor = new JCheckBox("Decor"));

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
		new DialogCreationModelTexte(null, null, null);
		System.out.println("Ok");
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
		if (e.getSource() == this.comboBox) {
			Habillage hab = me.donnerHabillage((String) this.comboBox.getSelectedItem());

			Vector<String> ls = new Vector<>();
			ls.addAll(hab.valeurs.keySet());
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(ls);
			this.nomKube.setModel(model);
			return;

		}
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
					if (this.estDecor.isSelected()) {
					mc= new ModelClasseDecor();	
					} else {
					mc = new ModelClasse();
					}
					String nomHabillage = (String) this.comboBox.getSelectedItem();
					Habillage hab = me.donnerHabillage((String) this.comboBox.getSelectedItem());
					try {
						String o = (String) orientation.getSelectedItem();
						if (o.equals("X")) {
							mc.initBufferFromTexteX(nomHabillage, hab, texte.getText(),
									(String) this.nomKube.getSelectedItem());
						}
						if (o.equals("Y")) {
							mc.initBufferFromTexteX(nomHabillage, hab, texte.getText(),
									(String) this.nomKube.getSelectedItem());
						}
						if (o.equals("Z")) {
							mc.initBufferFromTexteX(nomHabillage, hab, texte.getText(),
									(String) this.nomKube.getSelectedItem());
						}
					} catch (CouleurErreur e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

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
