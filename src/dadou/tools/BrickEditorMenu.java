package dadou.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import dadou.Arbre;
import dadou.CameraPosition;
import dadou.DecorDeBriqueDataElement;
import dadou.EtatOmbre;
import dadou.FBOShadowMap;
import dadou.Game;
import dadou.GroupeCameraPosition;
import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.ModelInstance;
import dadou.NomTexture.PlusDeTexture;
import dadou.ShadowData;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.texture.photo;
import dadou.texture.photoHabillage;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventNode;
import dadou.event.GameEventTree;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheDebug;
import dadou.greffon.GreffonForme;
import dadou.ihm.Action;
import dadou.param.DialogConfig;

public class BrickEditorMenu implements MouseListener, ActionListener {
	JMenuItem copier = new JMenuItem("Copier");
	JMenuItem copierAvecNom = new JMenuItem("Copier avec nom");
	JMenuItem coller = new JMenuItem("Coller");
	JMenuItem ajouter = new JMenuItem("Ajouter dans monde");
	JMenuItem sauvegarder = new JMenuItem("Sauvegarder");
	JMenuItem demarerArreter = new JMenuItem("Demarer");
	JMenuItem creerMonde = new JMenuItem("Creer monde");

	JMenuItem selectionner = new JMenuItem("Selectionner groupe (R shift pour position , ESPACE pour trajectoire)");
	JMenuItem importerPhotos = new JMenuItem("Importer photos");
	JMenuItem configurer = new JMenuItem("Configurer affichage");

	JMenuItem initialiserMondeDemarage = new JMenuItem("Initialiser monde demarage");
	JMenuItem imageEcran = new JMenuItem("Creer image écran");
	JMenuItem imageEcranDansFichier = new JMenuItem("Creer image écran dans fichier");
	JMenuItem afficherGraphe = new JMenuItem("Afficher graphe");
	JMenuItem afficherGrapheSimple = new JMenuItem("Afficher graphe simple");
	JMenu menuLumiere = new JMenu("Lumieres");
	JMenuItem modifierLumiere = new JMenuItem("Modifier");
	JMenuItem selectionnerLumiere = new JMenuItem("Selectionner");
	JMenuItem charger = new JMenuItem("Charger monde");
	JMenuItem canon = new JMenuItem("Canon");
	JMenuItem supprimer = new JMenuItem("Supprimer");
	JMenuItem modifierDonnees = new JMenuItem("Modifier donnees");
	JMenuItem ajouterDonnees = new JMenuItem("Ajouter donnees");
	JMenuItem supprimerDonnees = new JMenuItem("Supprimer donnees");
	JMenu menuForme = new JMenu("Formes");
	List<JMenuItem> greffonItems = new ArrayList<>();

	JPopupMenu menu = new JPopupMenu();
	BrickEditorSwing bes;
	Map<JMenuItem, Action> actions = new HashMap<>();
	Map<JMenuItem, Action> greffonActions = new HashMap<>();

	public BrickEditorMenu(BrickEditorSwing bes) {
		menu.add(copier);
		menu.add(copierAvecNom);
		menu.add(coller);
		menu.add(supprimer);

		menu.add(ajouter);
		menu.add(configurer);
		menu.add(importerPhotos);
		menu.add(charger);
		menu.add(creerMonde);

		menu.add(modifierDonnees);
		menu.add(ajouterDonnees);
		menu.add(supprimerDonnees);
		menu.add(selectionner);

		menu.add(initialiserMondeDemarage);
		menu.add(imageEcran);
		menu.add(imageEcranDansFichier);
		menu.add(canon);
		menu.add(sauvegarder);
		menu.add(menuForme);
		menu.add(menuLumiere);
		menu.add(demarerArreter);

		menu.add(afficherGraphe);
		menu.add(afficherGrapheSimple);
		importerPhotos.addActionListener(this);
		sauvegarder.addActionListener(this);
		demarerArreter.addActionListener(this);
		modifierLumiere.addActionListener(this);
		selectionnerLumiere.addActionListener(this);
		afficherGraphe.addActionListener(this);
		afficherGrapheSimple.addActionListener(this);
		menuLumiere.add(modifierLumiere);
		menuLumiere.add(selectionnerLumiere);
		for (GreffonForme gf : GreffonForme.greffons) {
			JMenuItem item = new JMenuItem(gf.nom());

			greffonActions.put(item, () -> {
				gf.init(this.bes);
				this.bes.comportement.ajouterAction(() -> {

					this.bes.comportement.etat = this.bes.comportement.selection;
					this.bes.comportement.selection.BoxSelectionAction = this.bes.comportement.selection.BoxRemplissage;
					this.bes.comportement.selection.setBoxSelection(true);
					GreffonForme.courrant = gf;
					
					bes.comportement.zg.modifierValeur = false;
					bes.comportement.selection.modifierValeurGreffon = false;
					bes.comportement.selection.placerLumiere = false;
					this.bes.comportement.processMenuKey = true;
				});
			});

			menuForme.add(item);
			greffonItems.add(item);
			item.addActionListener(this);

		}

		canon.addActionListener(this);
		imageEcran.addActionListener(this);
		imageEcranDansFichier.addActionListener(this);
		initialiserMondeDemarage.addActionListener(this);
		modifierDonnees.addActionListener(this);
		selectionner.addActionListener(this);
		this.supprimerDonnees.addActionListener(this);
		this.ajouterDonnees.addActionListener(this);

		ajouter.addActionListener(this);

		copier.addActionListener(this);
		copierAvecNom.addActionListener(this);
		coller.addActionListener(this);

		supprimer.addActionListener(this);
		configurer.addActionListener(this);

		creerMonde.addActionListener(this);
		charger.addActionListener(this);

		this.bes = bes;
		this.bes.tree.addMouseListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Action a = this.actions.get(arg0.getSource());
		if (a != null) {
			a.execute();
			return;
		}

		if (arg0.getSource() == supprimer) {
			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

			GameEventTree get = (GameEventTree) node.getUserObject();
			String chemin = get.donnerChemin("classes");

			if (chemin == null) {
				System.out.println("Pas d'objet");
			} else {
				ModelEvent me = bes.comportement.decorDeBriqueData.events.get(chemin);
				if (me == null) {
					System.out.println(" aucun model event pour " + chemin);
					return;
				}

				if (bes.comportement.decorDeBriqueData.existeNomModel(chemin)) {
					System.out.println(" Il existe des models pour " + chemin);
					return;
				}

				bes.comportement.decorDeBriqueData.events.remove(chemin);
				bes.reloadTree();

			}

		}
		if (arg0.getSource() == coller) {
			if (bes.copies == null) {
				System.out.println(" aucune copie ");
				return;
			}
			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			GameEventTree get = (GameEventTree) node.getUserObject();
			String chemin = get.donnerChemin("classes");
			if (chemin == null && !get.nom.equals("classes")) {
				System.out.println("Pas d'emplacement pour la copie d'objet");
			} else {

				if (chemin != null && bes.comportement.decorDeBriqueData.models.get(chemin) != null) {
					System.out.println("Copie dans objet impossible");
					return;
				}

				String nomCopie = UI.request("Nouveau nom", bes.canvasPanel);
				if (nomCopie == null) {
					return;
				}
				String nomComplet = null;
				if (chemin != null) {
					nomComplet = chemin + "." + nomCopie;
				} else {
					nomComplet = nomCopie;
				}
				if (bes.comportement.decorDeBriqueData.models.get(nomComplet) != null) {
					System.out.println("nom existant " + nomComplet);
					return;

				}

				for (ModelClasse copie : bes.copies) {
					ModelClasse clone = SerializeTool.clone(copie);
					clone.nomModele = nomComplet;

					String nomSprite3D = clone.nomSprite3D();
					if (nomSprite3D == null) {
						clone.nom = nomComplet;
					} else {
						clone.nom = nomComplet + "#" + nomSprite3D;
					}
					// System.out.println(" mc.nom="+clone.nom);
					// System.out.println(" mc.nomModele="+clone.nomModele);
					bes.comportement.decorDeBriqueData.ajouter(clone, clone.nom);

				}
				bes.reloadTree();

			}

		}
		if (arg0.getSource() == this.demarerArreter) {
			BrickEditor comportement = bes.comportement;
			if (comportement.mondeInterface.active) {
				comportement.pause = false;
				demarerArreter.setText("Demarer");
				bes.arreter();
				return;
			}
			bes.save();
			demarerArreter.setText("Arreter");

			comportement.ajouterAction(() -> {
				comportement.arbreCameraPositions.creerListePositionCamera(null);
				comportement.mondeInterface.parcourGroupeCameraPosition = null;
				comportement.mondeInterface.mondeInterface = null;
				comportement.mondeInterface.active = true;
			});
		}
		if (arg0.getSource() == charger) {

			List<String> l = DecorDeBriqueDataElement.lesMondes(bes.comportement.cheminRessources);
			String choix = (String) UI.choice(l, "Selectionner monde", bes.frame);
			if (choix == null) {
				return;
			}
			bes.comportement.decor.DecorDeBriqueData.cameraPosition = bes.comportement.scc.creerCameraPosition();
			bes.comportement.ajouterAction(() -> {
				try {
					bes.comportement.chargerMonde(choix);
					bes.comportement.SelectionMonde.nom = choix;
					bes.comportement.decorDeBriqueData.mondeCourant = choix;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

		}

		if (arg0.getSource() == imageEcran) {

			bes.comportement.ajouterAction(() -> {
				try {
					bes.comportement.fbo.creerImageEcranPourDecor(bes.comportement, null);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

		}
		if (arg0.getSource() == importerPhotos) {
			bes.comportement.ajouterAction(() -> {
				try {
					Map<String, photo> photos = photo.photos();

					for (photo p : photos.values()) {
						try {
							photoHabillage mc = p.creer(this.bes.comportement.decorDeBriqueData);

							mc.mc.initBuffer(bes.comportement.game, mc.habillage);

						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
						bes.reloadTree();
						
				

				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}
		if (arg0.getSource() == imageEcranDansFichier) {
			FileOption fo = new FileOption();
			fo.acceptFiles = false;
			fo.multiSelect = false;
			File[] rep = UI.chooseFileOrDirectory(this.bes.frame, fo);
			if (rep == null) {
				return;
			}
			String nom = UI.request("Nom image (png)", this.bes.frame);
			if (nom == null) {
				return;
			}
			bes.comportement.ajouterAction(() -> {
				try {
					bes.comportement.fbo.creerImageEcranPourDecor(bes.comportement,
							rep[0].getAbsolutePath() + "\\" + nom);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

		}
		if (arg0.getSource() == canon) {

			bes.comportement.ajouterAction(() -> {
				try {
					bes.comportement.etat = bes.comportement.EtatCanon;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

		}
		if (arg0.getSource() == sauvegarder) {
			this.bes.save();
		}
		if (arg0.getSource() == ajouter) {
			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			if (node.getUserObject() instanceof GameEventTree) {
				GameEventTree get = (GameEventTree) node.getUserObject();
				if (get.type.equals("classe")) {
					new DialogAjouterModel(this.bes.frame, this.bes, get.donnerChemin("classes"));
					return;
				}
			}

		}
		if (arg0.getSource() == copier) {
			// tree.getSelectionModel()

			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			GameEventTree get = (GameEventTree) node.getUserObject();
			if (get.nom.equals("classes")) {
				int idx = 0;
				String nomModelTmp = "__" + idx;

				while (this.bes.comportement.decorDeBriqueData.models.get(nomModelTmp) != null) {
					idx++;
					nomModelTmp = "__" + idx;
				}
				String nomModel = nomModelTmp;
				this.bes.comportement.ajouterAction(() -> {
					BrickEditor be = this.bes.comportement;
					be.DialogDemandeNomClasse.nomModel = nomModel;
					be.DialogDemandeNomClasse.supprimerEspaces = true;
					be.selection.BoxSelectionAction = be.selection.BoxCopie;
					be.selection.setBoxSelection(true);
					be.etat = be.selection;
					be.processMenuKey = true;
				});

				return;
			}

		}
		if (arg0.getSource() == copierAvecNom) {
			// tree.getSelectionModel()

			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			GameEventTree get = (GameEventTree) node.getUserObject();
			if (get.nom.equals("classes")) {
				int idx = 0;
				String nomModel = UI.request("Nom model", this.bes.frame);

				if (this.bes.comportement.decorDeBriqueData.models.get(nomModel) != null) {
					UI.warning("Nom model existant", this.bes.frame);
				}

				this.bes.comportement.ajouterAction(() -> {
					BrickEditor be = this.bes.comportement;
					be.DialogDemandeNomClasse.nomModel = nomModel;
					be.DialogDemandeNomClasse.supprimerEspaces = false;
					be.selection.BoxSelectionAction = be.selection.BoxCopie;
					be.selection.setBoxSelection(true);
					be.etat = be.selection;
					be.processMenuKey = true;
				});

				return;
			}

		}
		if (arg0.getSource() == selectionnerLumiere) {
			this.bes.comportement.ajouterAction(() -> {
				BrickEditor be = this.bes.comportement;
				be.lumiereModification = null;
				be.selection.BoxSelectionAction = null;
				be.selection.setBoxSelection(false);
				be.selection.placerLumiere = true;
				be.etat = be.selection;
				be.processMenuKey = true;
			});

		}
		if (arg0.getSource() == modifierLumiere) {
			this.bes.comportement.ajouterAction(() -> {
				BrickEditor be = this.bes.comportement;
				be.lumiereModification = be.lumiereSelection;
				be.selection.BoxSelectionAction = null;
				be.selection.setBoxSelection(false);
				be.selection.placerLumiere = false;
				be.etat = be.selection;
				be.selection.placerLumiere = true;
				be.processMenuKey = true;
			});

		}
		if (arg0.getSource() == copier) {
			// tree.getSelectionModel()

			TreePath path = bes.tree.getSelectionModel().getSelectionPath();
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
			GameEventTree get = (GameEventTree) node.getUserObject();
			String chemin = get.donnerChemin("classes");

			if (chemin == null) {
				System.out.println("Pas d'objet");
			} else {
				bes.copies = bes.comportement.decorDeBriqueData.donnerListModelClasse(chemin);

			}

		}
		if (arg0.getSource() == configurer) {
			new DialogConfig(bes.frame, bes);
		}

		if (arg0.getSource() == creerMonde) {

			DialogCreationMonde dialog = new DialogCreationMonde(bes.frame, bes.comportement.cheminRessources);
			dialog.action = () -> {
				String choix = bes.comportement.SelectionMonde.nom;

				bes.comportement.ajouterAction(() -> {
					try {
						String nom = dialog.nomMonde.getText();
						if (nom.endsWith(".wld")) {
							String nomFichier = BrickEditor.cheminRessources + "/" + nom;
							if (new File(nomFichier).exists()) {
								UI.warning("Monde existant", bes.frame);
								return;

							}
							try {
								
								DecorDeBriqueDataElement data = DecorDeBriqueDataElement.creer(dialog.niveau(),
										BrickEditor.elementTaille, nomFichier);
								data.nomHabillage = dialog.nomHabillage();
								SerializeTool.save(data, nomFichier);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				});

			};
			return;
		}
		if (arg0.getSource() == this.initialiserMondeDemarage) {
			bes.comportement.decorDeBriqueData.nomMondeDemarage = bes.comportement.SelectionMonde.nom;
			return;
		}
		if (arg0.getSource() == creerMonde) {
			String nom = UI.request("Nom nouveau monde", bes.frame);
			if (nom == null) {
				return;
			}

			if (nom.endsWith(".wld")) {
				String nomFichier = bes.comportement.cheminRessources + "/" + nom;
				if (new File(nomFichier).exists()) {
					UI.warning("Monde existant", bes.frame);
					return;

				}
				try {
					DecorDeBriqueDataElement.creer(BrickEditor.niveau, BrickEditor.elementTaille, nomFichier);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void modifierDonnneesClasse() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		GameEventTree get = (GameEventTree) node.getUserObject();
		String chemin = get.donnerChemin("classes");
		ModelEvent me = bes.comportement.decorDeBriqueData.events.get(chemin);
		new DialogModifierDonnees(this.bes.frame, bes, me.donnees());
		return;
	}

	public void modifierDonnneesObjet() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Arbre<Object> get = (Arbre<Object>) node.getUserObject();
		ModelInstance mi = (ModelInstance) get.valeur;
		new DialogModifierDonnees(this.bes.frame, bes, mi.params());
		return;
	}

	public void supprimerDonneesClasse() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		GameEventTree get = (GameEventTree) node.getUserObject();
		String chemin = get.donnerChemin("classes");
		ModelEvent me = bes.comportement.decorDeBriqueData.events.get(chemin);
		List<String> choix = new ArrayList<>();
		choix.addAll(me.donnees().keySet());
		String nom = (String) UI.choice(choix, "Supprimer donnee", bes.frame);
		if (nom == null) {
			return;
		}
		me.donnees().remove(nom);

	}

	public void supprimerDonneesObjet() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Arbre<Object> get = (Arbre<Object>) node.getUserObject();
		ModelInstance mi = (ModelInstance) get.valeur;
		List<String> choix = new ArrayList<>();
		choix.addAll(mi.params().keySet());
		String nom = (String) UI.choice(choix, "Supprimer donnee", bes.frame);
		if (nom == null) {
			return;
		}
		mi.params().remove(nom);

	}

	public void ajouterDonneesClasse() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		GameEventTree get = (GameEventTree) node.getUserObject();
		String chemin = get.donnerChemin("classes");
		ModelEvent me = bes.comportement.decorDeBriqueData.events.get(chemin);
		String nom = UI.request("Nom donnee", this.bes.frame);
		if (nom == null) {
			return;
		}
		if (me.donnees().get(nom) != null) {
			UI.warning("Donnee existante", bes.frame);
			return;
		}
		me.donnees().put(nom, "");
		return;

	}

	public void ajouterDonneesObjet() {
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Arbre<Object> get = (Arbre<Object>) node.getUserObject();
		ModelInstance mi = (ModelInstance) get.valeur;
		String nom = UI.request("Nom donnee", this.bes.frame);
		if (nom == null) {
			return;
		}
		if (mi.params().get(nom) != null) {
			UI.warning("Donnee existante", bes.frame);
			return;
		}
		mi.params().put(nom, "");
		return;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void setVisible(boolean b, JMenuItem... items) {
		for (JMenuItem item : items) {
			item.setVisible(b);
		}
	}

	public void setVisible(boolean b, List<JMenuItem> items) {
		for (JMenuItem item : items) {
			item.setVisible(b);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		actions = new HashMap<>();
		TreePath path = bes.tree.getSelectionModel().getSelectionPath();
		if (path == null) {
			return;
		}

		this.initialiserMondeDemarage.setVisible(false);
		menuForme.setVisible(false);
		setVisible(false, importerPhotos, configurer, charger, modifierLumiere, selectionnerLumiere, imageEcran,
				imageEcranDansFichier, creerMonde, initialiserMondeDemarage, canon, sauvegarder);
		setVisible(false, this.greffonItems);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (node != null && node.getUserObject() != null && node.getUserObject() instanceof GameEventNode) {

			GameEventNode gen = (GameEventNode) node.getUserObject();
			boolean estJeux = gen.nom.equals("Jeux");
			menuForme.setVisible(estJeux && !bes.comportement.mondeInterface.active);
			setVisible(estJeux && !bes.comportement.mondeInterface.active, importerPhotos, configurer, charger,
					modifierLumiere, selectionnerLumiere, imageEcran, imageEcranDansFichier, creerMonde,
					initialiserMondeDemarage, canon, sauvegarder);
			setVisible(estJeux, demarerArreter);
			setVisible(estJeux && !bes.comportement.mondeInterface.active, this.greffonItems);
			if (estJeux && !bes.comportement.mondeInterface.active) {
				actions = this.greffonActions;
			}
			charger.setText("Charger monde");

			boolean estClasses = gen.nom.equals("classes");
			ajouter.setVisible(false);
			copier.setVisible(estClasses);
			copierAvecNom.setVisible(estClasses);

			coller.setVisible(false);
			supprimer.setVisible(false);

			modifierDonnees.setVisible(false);
			selectionner.setVisible(false);
			afficherGraphe.setVisible(false);
			afficherGrapheSimple.setVisible(false);
			ajouterDonnees.setVisible(false);
			this.supprimerDonnees.setVisible(false);
			if (!bes.comportement.mondeInterface.active) {
				if (gen instanceof GameEventTree) {
					GameEventTree get = (GameEventTree) gen;
					if (get.type != null && get.type.equals("classe")) {
						ajouter.setText("Ajouter sprite 3D");
						ajouter.setVisible(true);
						copier.setVisible(true);
						copierAvecNom.setVisible(true);
						supprimer.setVisible(true);
						String chemin = get.donnerChemin("classes");
						ModelEvent me = bes.comportement.decorDeBriqueData.events.get(chemin);
						modifierDonnees.setVisible(me.donnees().size() > 0);
						this.actions.put(this.modifierDonnees, () -> {
							this.modifierDonnneesClasse();

						});
						ajouterDonnees.setVisible(true);
						this.actions.put(this.ajouterDonnees, () -> {
							this.ajouterDonneesClasse();

						});
						supprimerDonnees.setVisible(me.donnees().size() > 0);
						this.actions.put(supprimerDonnees, () -> {
							this.supprimerDonneesClasse();

						});

					} else {
						String chemin = get.donnerChemin("classes");
						if ((get.nom.equals("classes") || chemin != null) && bes.copies != null) {
							coller.setVisible(true);
						}

					}
					if (get.type != null && get.type.equals("module")) {
						ajouter.setText("Ajouter module");
						ajouter.setVisible(true);
					}
				}
			}

			bes.tree.setComponentPopupMenu(menu);

		}
		if (node != null && node.getUserObject() != null && node.getUserObject() instanceof Arbre) {
			this.setVisible(false, configurer, charger, creerMonde, ajouter, copier, copierAvecNom, coller, supprimer,
					selectionner, modifierDonnees, ajouterDonnees, supprimerDonnees);
			Arbre arbre = (Arbre) node.getUserObject();
			List<String> chemin = arbre.cheminListe(null);
			if (chemin.size() >= 1 && chemin.get(0).equals("camera")) {

				String cheminChaine = this.nomCameraPosition(chemin);
				Object[] p = bes.tree.getSelectionPath().getPath();

				CameraPosition cp = this.bes.comportement.decor.DecorDeBriqueData.donnerCameraPosition(cheminChaine);
				if (cp == null) {
					String nomGroupe = this.nomGroupeCameraPosition(chemin);
					List<String> lst = this.bes.comportement.decor.DecorDeBriqueData.donnerNomsPourGroupe(nomGroupe);

					if (lst == null) {
						ajouter.setVisible(true);
						ajouter.setText("Ajouter groupe");
						actions.put(ajouter, () -> {
							this.ajouterGroupe(nomGroupe, chemin);
						});
					} else {
						selectionner.setVisible(true);
						actions.put(selectionner, () -> {
							this.selectionnerGroupe(nomGroupe, chemin);
						});
						afficherGraphe.setVisible(true);
						actions.put(afficherGraphe, () -> {
							this.afficherGraphe(nomGroupe, chemin);
						});
						afficherGrapheSimple.setVisible(true);
						actions.put(afficherGrapheSimple, () -> {
							this.afficherGrapheSimple(nomGroupe, chemin);
						});
					}

					supprimer.setVisible(lst != null);
					supprimer.setText("Supprimer");
					actions.put(supprimer, () -> {
						this.supprimerGroupe(nomGroupe, chemin);
					});

				} else {
					charger.setVisible(true);
					charger.setText("Charger position");
					actions.put(charger, () -> {
						this.chargerPosition(cp);
					});
					supprimer.setVisible(true);
					supprimer.setText("Supprimer");
					actions.put(supprimer, () -> {
						this.supprimerPosition(cp, cheminChaine, chemin);
					});
				}

			}
			if (chemin.size() >= 1 && chemin.get(0).equals("objets")) {
				if (arbre.valeur != null && arbre.valeur instanceof ModelInstance) {
					ModelInstance mi = (ModelInstance) arbre.valeur;
					modifierDonnees.setVisible(mi.params().size() >= 1);
					ajouterDonnees.setVisible(true);
					this.supprimerDonnees.setVisible(mi.params().size() >= 1);

					this.actions.put(this.modifierDonnees, () -> {
						this.modifierDonnneesObjet();

					});

					this.actions.put(this.ajouterDonnees, () -> {
						this.ajouterDonneesObjet();

					});

					this.actions.put(supprimerDonnees, () -> {
						this.supprimerDonneesObjet();

					});
				}
			}

			bes.tree.setComponentPopupMenu(menu);
		}

	}

	public void supprimerPosition(CameraPosition cp, String nomPosition, List<String> chemin) {
		List<String> cheminParent = chemin.subList(0, chemin.size() - 1);
		this.bes.comportement.ajouterAction(() -> {
			this.bes.comportement.decor.DecorDeBriqueData.supprimerCameraPosition(nomPosition);
			this.bes.chemin = cheminParent;
			this.bes.reloadTree();
			String tmp = this.bes.comportement.arbreCameraPositions.nomGroupeEnCours;
			if (tmp != null && tmp.equals(cp.groupe)) {
				this.bes.comportement.arbreCameraPositions.creerListePositionCamera(tmp);
			}
		});
	}

	public void supprimerGroupe(String groupe, List<String> cheminListe) {
		List<String> cheminParent = cheminListe.subList(0, cheminListe.size() - 1);
		this.bes.comportement.ajouterAction(() -> {
			this.bes.comportement.decor.DecorDeBriqueData.supprimerGroupeCameraPosition(groupe);
			this.bes.chemin = cheminParent;
			this.bes.reloadTree();
			String tmp = this.bes.comportement.arbreCameraPositions.nomGroupeEnCours;
			if (tmp != null && tmp.equals(groupe)) {
				this.bes.comportement.arbreCameraPositions.creerListePositionCamera(null);
			}
		});
	}

	public void chargerPosition(CameraPosition cp) {

		this.bes.comportement.ajouterAction(() -> {
			this.bes.comportement.arbreCameraPositions.nomGroupeEnCours = cp.groupe;

			this.bes.comportement.scc.charger(cp);
			this.bes.comportement.decor.DecorDeBriqueData.cameraPosition = cp;

		});
	}

	public void selectionnerGroupe(String nomGroupe, List<String> chemin) {
		this.bes.comportement.ajouterAction(() -> {
			this.bes.chemin = chemin;
			this.bes.comportement.arbreCameraPositions.nomGroupeEnCours = nomGroupe;

			this.bes.comportement.arbreCameraPositions.creerListePositionCamera(nomGroupe);

		});

	}

	public void afficherGraphe(String nomGroupe, List<String> chemin) {
		this.bes.comportement.ajouterAction(() -> {
			this.bes.chemin = chemin;
			Graphe g = this.bes.comportement.creerGrapheMultiRacine(nomGroupe, 3, 1, false, 5.0f);
			// this.bes.comportement.arbreCameraPositions.nomGroupeEnCours =
			// nomGroupe;
			this.bes.comportement.grapheDebug = new GrapheDebug(this.bes.comportement, g);
			// this.bes.comportement.arbreCameraPositions.creerListePositionCamera(nomGroupe);

		});

	}

	public void afficherGrapheSimple(String nomGroupe, List<String> chemin) {
		this.bes.comportement.ajouterAction(() -> {
			this.bes.chemin = chemin;
			Graphe g = this.bes.comportement.creerGrapheMultiRacineSimple(nomGroupe, 3, 1, false, 5.0f);
			// this.bes.comportement.arbreCameraPositions.nomGroupeEnCours =
			// nomGroupe;
			this.bes.comportement.grapheDebug = new GrapheDebug(this.bes.comportement, g);
			// this.bes.comportement.arbreCameraPositions.creerListePositionCamera(nomGroupe);

		});

	}

	public void ajouterGroupe(String baseGroupe, List<String> cheminList) {
		String chemin = UI.request("Nom groupe", bes.frame);
		if (chemin == null) {
			return;
		}
		String array[] = chemin.split("\\#");
		for (int i = 0; i < array.length - 1; i++) {
			cheminList.add(array[i]);
		}

		String tmp = baseGroupe + "#" + chemin;
		if (baseGroupe.equals("")) {
			tmp = chemin;
		}
		String nouveauGroupe = tmp;

		List<String> lst = this.bes.comportement.decor.DecorDeBriqueData.donnerNomsPourGroupe(nouveauGroupe);
		if (lst != null) {
			return;
		}
		this.bes.comportement.ajouterAction(() -> {
			this.bes.comportement.arbreCameraPositions.nomGroupeEnCours = nouveauGroupe;
			this.bes.comportement.decor.DecorDeBriqueData.ajouterGroupeCameraPosition(nouveauGroupe);
			this.bes.chemin = cheminList;
			this.bes.reloadTree();

		});

	}

	public String nomGroupeCameraPosition(List<String> chemin) {
		StringBuilder sb = new StringBuilder();
		boolean premier = true;
		for (int i = 1; i < chemin.size(); i++) {
			if (!premier) {
				sb.append("#");
			}
			premier = false;
			sb.append(chemin.get(i));
		}

		return sb.toString();

	}

	public String nomCameraPosition(List<String> chemin) {
		StringBuilder sb = new StringBuilder();
		boolean premier = true;
		for (int i = 1; i < chemin.size() - 1; i++) {
			if (!premier) {
				sb.append("#");
			}
			premier = false;
			sb.append(chemin.get(i));
		}
		sb.append("$");
		sb.append(chemin.get(chemin.size() - 1));
		return sb.toString();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
