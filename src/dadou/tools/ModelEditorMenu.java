package dadou.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sun.javafx.scene.control.skin.ToolBarSkin;

import dadou.Arbre;
import dadou.ConstructionModelClasse;
import dadou.Log;
import dadou.ModelClasse;
import dadou.greffon.GreffonForme;
import dadou.greffon.GreffonSelection;
import dadou.ihm.Action;
import dadou.param.ModelClassParam;

public class ModelEditorMenu implements MouseListener, ActionListener {

	JPopupMenu menuModel = new JPopupMenu();
	JPopupMenu menuDossier = new JPopupMenu();
	JMenuItem copier;
	JMenuItem ajouter;
	JMenuItem ajouterTexteModel;
	JMenuItem ajouterCoeurModel;
	JMenuItem coller;
	JMenuItem supprimer;
	JMenuItem animer;
	JMenuItem initialiser;
	JMenuItem informer;
	JMenuItem configurer;
	JMenuItem creerImage;
	
	String nomCopie;
	String nomModelClassCopie;

	ModelEditorSwing me;

	JTree tree;
	Map<JMenuItem, Action> greffonActions;

	public ModelEditorMenu(ModelEditorSwing me, JTree tree) {
		this.me = me;
		copier = new JMenuItem("Copier");
		creerImage = new JMenuItem("Creer image");
		
		menuModel.add(copier);
		menuModel.add(creerImage);
		copier.addActionListener(this);
		creerImage.addActionListener(this);
		ajouter = new JMenuItem("Ajouter Modele");
		ajouterTexteModel = new JMenuItem("Ajouter Modele texte");
		ajouterCoeurModel = new JMenuItem("Ajouter Modele coeur");
		coller = new JMenuItem("Coller");
		supprimer = new JMenuItem("Supprimer");
		animer = new JMenuItem("Animer construction");
		initialiser = new JMenuItem("Initialiser");
		informer = new JMenuItem("Informer");
		configurer = new JMenuItem("Configurer");
		
		menuDossier.add(ajouter);
		menuDossier.add(ajouterTexteModel);
		menuDossier.add(ajouterCoeurModel);
		menuDossier.add(coller);
		menuModel.add(supprimer);
		menuModel.add(configurer);
		menuModel.add(animer);
		menuModel.add(initialiser);
		menuModel.add(informer);
		JMenu menuForme = new JMenu("Formes");
		List<JMenuItem> greffonItems = new ArrayList<>();
		greffonActions = new HashMap<>();
		menuDossier.add(menuForme);
		coller.addActionListener(this);
		initialiser.addActionListener(this);
		informer.addActionListener(this);
		
		ajouter.addActionListener(this);
		ajouterTexteModel.addActionListener(this);
		ajouterCoeurModel.addActionListener(this);
		supprimer.addActionListener(this);
		animer.addActionListener(this);
		configurer.addActionListener(this);
		this.tree = tree;
		for (GreffonSelection gf : GreffonSelection.greffons) {
			JMenuItem item = new JMenuItem(gf.nom());

			greffonActions.put(item, () -> {
				GreffonSelection.courrant = gf;
				me.posDebutSelection = null;
				gf.initGreffon(me.window.frame);
				me.window.frame.setTitle(" greffon "+gf);
			
			});

			menuForme.add(item);
			greffonItems.add(item);
			item.addActionListener(this);

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node != null) {
			Arbre<String> arbre = (Arbre<String>) node.getUserObject();
			if (arbre.enfants().isEmpty() && !(arbre.donnerParent() == null)) {
				tree.setComponentPopupMenu(menuModel);

			} else {
				coller.setVisible(nomCopie != null);
				tree.setComponentPopupMenu(menuDossier);
			}

		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public Arbre<String> nettoyer(Arbre<String> p) {
		if (p.enfants().isEmpty()) {
			Arbre<String> parent = p.donnerParent();
			if (parent == null) {
				return null;
			}
			parent.supprimer(p.nom);
			return nettoyer(parent);
		}
		return p;

	}
	File defaultRep;
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Action a = this.greffonActions.get(e.getSource());
		if (a != null) {
			a.execute();
			return;
		}
		if (e.getSource() == this.ajouter) {
			new DialogCreationModel(me.window.frame, me, null);
			System.out.println("Ok");

		}
		if (e.getSource() == this.ajouterTexteModel) {
			new DialogCreationModelTexte(me.window.frame, me, null);
			System.out.println("Ok");

		}
		if (e.getSource() == this.ajouterCoeurModel) {
			new DialogCreationModelCoeur(me.window.frame, me, null);
			System.out.println("Ok");

		}
		if (e.getSource() == this.creerImage) {
			
			FileOption fo = new FileOption();
			fo.acceptFiles = false;
			fo.multiSelect = false;
			if (defaultRep != null) {
			fo.defaultDirectory =defaultRep.getAbsolutePath(); }
			File[] rep = UI.chooseFileOrDirectory(this.me.window.frame, fo);
			if (rep == null) {
				return;
			}
			String nom= UI.request("Nom image (png)", this.me.window.frame,this.me.mc.nomSprite3D()+".png");
			if (nom ==null) {
				return;
			}
			defaultRep = rep[0];
			this.me.fileNameEcran=rep[0].getAbsolutePath()+"\\"+nom;
			
		}
		if (e.getSource() == this.copier) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				this.nomCopie = arbre.donnerParent().chemin(".");
				nomModelClassCopie = nomCopie;

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					this.nomCopie += "#" + arbre.nom;

				}
				this.nomCopie = this.me.retirerCategorie(nomCopie);

			}
		}
		if (e.getSource() == supprimer) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				String nom = arbre.donnerParent().chemin(".");
				Arbre<String> parent = arbre.donnerParent();
				parent.supprimer(arbre.nom);

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					nom += "#" + arbre.nom;

				}
				nom = this.me.retirerCategorie(nom);
				if (this.nomCopie != null && this.nomCopie.equals(nom)) {
					this.nomCopie = null;
				}

				Log.print("supprimer ", nom);
				this.me.decorDeBriqueData.models.remove(nom);
				this.me.decorDeBriqueData.constructions.remove(nom);
				parent = this.nettoyer(parent);
				me.treeModel.reload(parent.node);

			}
		}

		if (e.getSource() == animer) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				String nom = arbre.donnerParent().chemin(".");

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					nom += "#" + arbre.nom;

				}
				nom = this.me.retirerCategorie(nom);
				if (this.nomCopie != null && this.nomCopie.equals(nom)) {
					this.nomCopie = null;
				}
				ModelClasse mc = this.me.decorDeBriqueData.models.get(nom);
				if (this.me.decorDeBriqueData.constructions == null) {
					this.me.decorDeBriqueData.constructions = new HashMap<>();
				}
				this.me.decorDeBriqueData.constructions.put(nom, new ConstructionModelClasse(mc));

			}
		}
		
		
		if (e.getSource() == configurer) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				String nom = arbre.donnerParent().chemin(".");

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					nom += "#" + arbre.nom;

				}
				nom = this.me.retirerCategorie(nom);
				if (this.nomCopie != null && this.nomCopie.equals(nom)) {
					this.nomCopie = null;
				}
				ModelClasse mc = this.me.decorDeBriqueData.models.get(nom);
				new ModelClassParam(mc);
					

			}
		}
		if (e.getSource() == initialiser) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				String nom = arbre.donnerParent().chemin(".");

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					nom += "#" + arbre.nom;

				}
				nom = this.me.retirerCategorie(nom);
				if (this.nomCopie != null && this.nomCopie.equals(nom)) {
					this.nomCopie = null;
				}
				ModelClasse mc = this.me.decorDeBriqueData.models.get(nom);
				String r = UI.request("echelle", this.me.window.frame);
				if (r == null) {
					return;
				}
				mc.echelle = Float.parseFloat(r);

			}
		}
		if (e.getSource() == informer) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node != null) {
				Arbre<String> arbre = (Arbre<String>) node.getUserObject();

				String nom = arbre.donnerParent().chemin(".");

				if (arbre.nom != null && !arbre.nom.isEmpty()) {
					nom += "#" + arbre.nom;

				}
				nom = this.me.retirerCategorie(nom);
				if (this.nomCopie != null && this.nomCopie.equals(nom)) {
					this.nomCopie = null;
				}
				ModelClasse mc = this.me.decorDeBriqueData.models.get(nom);

				System.out.println(" dim=(" + mc.dx + "," + mc.dy + "," + mc.dz + ")");
				System.out.println(" echelle=" + mc.echelle);
				System.out.println(" nomHabillage=" + mc.nomHabillage);
				System.out.println(" transparence=" + mc.transparence);
				System.out.println(" nom=" + nom);

			}
		}
		if (e.getSource() == coller) {
			new DialogCreationModel(me.window.frame, me, this);
		}

	}

}
