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

public class DialogCreationModelCoeur extends JDialog implements WindowListener, ActionListener {

	/**
	 * 
	 */


	JTextField nomModel;
	JTextField nomDossier;

	JComboBox<String> comboBox;
	JComboBox<String> nomKube;
	JComboBox<Integer> taille;
	JComboBox<Integer> profondeur;
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

	public DialogCreationModelCoeur(JFrame parent, ModelEditorSwing me, ModelEditorMenu menu) {
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
					sb.add(new JLabel("Tailles"));
					sb.add(taille = new JComboBox<>(new Integer[] {4, 9, 17, 24,32,40 }));

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Profondeurs"));
					sb.add(profondeur = new JComboBox<>(new Integer[] { 0, 2, 4,6 }));

				});
				sb.beginX(() -> {
					sb.add(new JLabel("Nom kube"));
					sb.add(this.nomKube = new JComboBox<>(new String[] {}));
					

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
		new DialogCreationModelCoeur(null, null, null);
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
				
					mc = new ModelClasse();
					
					String nomHabillage = (String) this.comboBox.getSelectedItem();
					Habillage hab = me.donnerHabillage((String) this.comboBox.getSelectedItem());
				

					if (this.comboBox.getSelectedItem() != null) {
						mc.nomHabillage = (String) this.comboBox.getSelectedItem();
					}
					mc.initCoeur(nomHabillage, hab, (String) this.nomKube.getSelectedItem(), (Integer)this.taille.getSelectedItem(),(Integer)this.profondeur.getSelectedItem());
			

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
