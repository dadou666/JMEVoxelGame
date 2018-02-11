package dadou.tools;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;

import dadou.ModelClasse;
import dadou.ModelInstance;
import dadou.tools.construction.AfficherBoxSelection;
import ihm.swing.SwingBuilderDialog;

public class DialogAjouterModel extends JDialog implements WindowListener {
	BrickEditorSwing bes;
	JTextField nomModel;
	JList<ModelClasse> listeType;

	public DialogAjouterModel(JFrame parent, BrickEditorSwing bes, String nomClasse) {
		super(parent);
		SwingBuilderDialog sb = new SwingBuilderDialog();
		sb.setSize(200, 30);
		this.bes = bes;
		List<ModelClasse> l = bes.comportement.decorDeBriqueData.donnerListModelClasse(nomClasse);
		DefaultListModel<ModelClasse> m = new DefaultListModel<>();
		sb.beginY(() -> {
			nomModel = new JTextField();
			nomModel.setText(this.nomParDefaut(nomClasse));
			listeType = new JList<>();
			sb.add(nomModel);
			sb.setSize(200, 300);
			sb.add(listeType);
			for (ModelClasse mc : l) {
				m.addElement(mc);

			}
			listeType.setModel(m);
		});
		sb.openIn("Ajouter", this);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	public String nomParDefaut(String nomClasse) {
		int i=0;
		while( bes.comportement.decor.DecorDeBriqueData.donnerModelInstance("@"+i) != null) {
		i++;
		}
		return "@"+i;
		
		
		
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		ModelClasse model = this.listeType.getSelectedValue();
		if (model == null) {
			return;
		}
		bes.comportement.arbreModelClasse.saisie = this.nomModel.getText();
		bes.comportement.selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0, model);
		;

		if (model.echelle == 0.0f) {
			model.echelle = 1.0f;
		}
		bes.comportement.selection.BoxSelectionAction = bes.comportement.selection.BoxColler;
		bes.comportement.selection.EtatBoxSelection = new AfficherBoxSelection(model.dx, model.dy, model.dz,
				model.echelle, null);
		bes.comportement.etat = bes.comportement.selection;
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
