package dadou.tools;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import ihm.swing.SwingBuilder;
import ihm.swing.SwingBuilderDialog;

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

public class DialogCreationHabillage extends JDialog implements WindowListener {

	/**
	 * 
	 */

	JTextField dim;

	JTextField nomHabillage;

	private static final long serialVersionUID = 1L;
	
	HabillageEditorMenu menu;
	Arbre<Object> arbre;

	public DialogCreationHabillage(JFrame parent, HabillageEditorMenu menu,	Arbre<Object> arbre) {
		super(parent);
		this.arbre =arbre;
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(120, 30);
		
		this.menu = menu;
		sb.beginY(() -> {
			sb.beginX(() -> {
				sb.add(new JLabel("Nom habillage"));
				sb.add(nomHabillage = new JTextField());

			});

			sb.beginX(() -> {
				sb.add(new JLabel("Dim"));
				sb.add(dim = new JTextField());

			});

		});
		sb.openIn("Dimension", this);
		this.addWindowListener(this);

	}

	static public void main(String[] args) {
		new DialogCreationHabillage(null, null,null);
		System.out.println("Ok");
	}

	public int getDim() {
		return Integer.parseInt(this.dim.getText());
	}



	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		Habillage h  = new Habillage(this.getDim());
		String nom =nomHabillage.getText()+".hab";
		if (arbre.enfant(nom) != null) {
			UI.warning("Nom existant", this.menu.habillageEditorSwing.window.frame);
		} else {
			File f=new File(this.menu.habillageEditorSwing.cheminRessources,nom);
			try {
				h.nomHabillage = nom;
				SerializeTool.save(h, f);
				Arbre<Object> o=arbre.ajouter(nom);
				o.valeur = this.menu.actionsPourHabillage(o, h);
				
				this.menu.habillageEditorSwing.creerArbreHabillage(h, o,nom);
				this.menu.habillageEditorSwing.treeModel.reload(arbre.node);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
