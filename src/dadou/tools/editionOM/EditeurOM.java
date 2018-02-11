package dadou.tools.editionOM;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Arbre;
import dadou.Button;
import dadou.JeuxException;
import dadou.ModelEvent;
import dadou.ModelInstance;
import dadou.ObjetMobile;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.EditeurArbre;
import dadou.ihm.GestionArbreControlleur;
import dadou.ihm.IHM;
import dadou.ihm.Menu;
import dadou.ihm.MenuControlleur;
import dadou.ihm.MenuDeselection;
import dadou.ihm.Widget;
import dadou.jeux.Chemin;
import dadou.jeux.Trajectoire;
import dadou.jeux.TrajectoireContext;
import dadou.tools.*;
import dadou.tools.construction.BoxColler;
import dadou.tools.construction.Selection;

public class EditeurOM extends EtatBrickEditor implements GestionArbreControlleur<Trajectoire> {

	public EditeurArbre<Trajectoire> editeurArbre;
	public IHM ihmMenu;
	public IHM ihmEditeur;
	public IHM cheminTrajectoire;
	public Map<ObjetMobile, TrajectoireContext> tcs = new HashMap<>();

	public enum Axe {
		X, Y, Z
	};

	public Axe axe;
	public Menu menu;
	public Widget widgetChemin;
	public Widget widgetPositionChemin;
	public MenuControlleur trajectoire;
	public MenuControlleur position;
	public MenuControlleur rotation;
	public MenuControlleur cloner;
	public MenuControlleur supprimer;

	public MenuControlleur axeX;
	public MenuControlleur axeY;
	public MenuControlleur axeZ;

	public MenuControlleur menuPlus;
	public MenuControlleur menuMoins;

	public MenuControlleur quitter;
	public MenuControlleur editer;

	public MenuControlleur avancer;
	public MenuControlleur reculer;
	public MenuControlleur stopper;
	public MenuControlleur initialiserDebut;
	public MenuControlleur initialiserFin;
	public MenuControlleur annuler;
	public MenuControlleur parametre;
	public List<MenuDeselection> deselections = new ArrayList<>();
	public Button button;

	public void setTrajectoireContext(TrajectoireContext tc) {
		this.tcs.put(BrickEditor.objetCourant, tc);
	}

	public TrajectoireContext getTrajectoireContext() {

		TrajectoireContext tc = this.tcs.get(BrickEditor.objetCourant);
		if (tc == null) {
			Chemin chemin = new Chemin();
			Arbre<Trajectoire> arbre = editeurArbre.arbre;
			if (arbre.valeur == null) {
				arbre.valeur = new Trajectoire();
			}
			tc = new TrajectoireContext(arbre, chemin, BrickEditor.objetCourant);
			tcs.put(BrickEditor.objetCourant, tc);
		}
		return tc;
	}

	public EditeurOM(BrickEditor be) {

		cheminTrajectoire = be.game.nouvelleIHM();
		cheminTrajectoire.beginY();
		cheminTrajectoire.space(800);
		cheminTrajectoire.beginX();
		cheminTrajectoire.setSize(400, 40);
		widgetChemin = cheminTrajectoire.widget();
		Widget.drawText(widgetChemin, "/", Color.BLUE, Color.GREEN, Color.gray);
		widgetPositionChemin = cheminTrajectoire.widget();

		cheminTrajectoire.end();
		cheminTrajectoire.end();

		ihmMenu = be.game.nouvelleIHM();
		button = new Button(0);
		ihmMenu.beginY();
		ihmMenu.beginX();
		List<MenuControlleur> list = new ArrayList<MenuControlleur>();
		this.creerMenuControlleur(list);
		menu = new Menu(ihmMenu, 128, 32, list, false);
		ihmMenu.space(700);
		axe = Axe.X;
		axeX.selected = true;
		axeX.updateText();
		this.BrickEditor = be;
		ihmMenu.end();

		ihmMenu.space(400);
		ihmMenu.end();
		ihmEditeur = be.game.nouvelleIHM();
		ihmEditeur.setSize(128, 32);
		ihmEditeur.beginX();
		ihmEditeur.space(800);
		ihmEditeur.beginY();
		ihmEditeur.space(400);

		editeurArbre = new EditeurArbre<Trajectoire>(this, null, ihmEditeur, 128, 32, 5);

		ihmEditeur.end();
		ihmEditeur.end();

	}

	public void deselectionAxe() {
		axeX.selected = false;
		axeY.selected = false;
		axeZ.selected = false;

	}

	public void deselectionEtatObjet() {
		avancer.selected = false;
		reculer.selected = false;
		stopper.selected = false;
		initialiserDebut.selected = false;
		initialiserFin.selected = false;

	}

	public void initialiserDebut() {
		TrajectoireContext tc = getTrajectoireContext();
		if (tc != null) {
			tc.trajectoire = BrickEditor.objetCourant.model.trajectoires;
			tc.idxDeplacement = 0;
			BrickEditor.objetCourant.reset();
		}
		Widget.drawText(widgetPositionChemin, tc.position(), Color.BLUE, Color.GREEN, Color.gray);
	}

	public void creerMenuControlleur(List<MenuControlleur> list) {
		list.add(quitter = new MenuControlleur("quitter", 0) {

			@Override
			public void activer() {
				// quitter.selected = true;
				/*
				 * if (tc != null) { tc.reset(); tc = null; }
				 */
				BrickEditor.etat = BrickEditor.selection;

			}

		});

		list.add(trajectoire = new MenuControlleur("trajectoire", 3) {

			@Override
			public void activer() {
				// TODO Auto-generated method stub
				position.selected = false;
				trajectoire.selected = true;
				rotation.selected = false;

			}

		});

		trajectoire.selected = true;
		list.add(position = new MenuControlleur("position", 0) {

			@Override
			public void activer() {
				// TODO Auto-generated method stub
				trajectoire.selected = false;
				position.selected = true;
				rotation.selected = false;
			}

		});

		list.add(rotation = new MenuControlleur("rotation", 2) {

			@Override
			public void activer() {
				// TODO Auto-generated method stub
				trajectoire.selected = false;
				position.selected = false;
				rotation.selected = true;
			}

		});
		list.add(cloner = new MenuControlleur("cloner", 0) {

			@Override
			public void activer() {
				// TODO Auto-generated method stub
				ModelInstance mi = SerializeTool.clone(BrickEditor.objetCourant.model);
				mi.modelClasse = BrickEditor.objetCourant.model.modelClasse;
				BrickEditor.selection.BoxColler.modelInstance = mi;
				BrickEditor.etat = BrickEditor.DialogDemandeNomClone;
				BrickEditor.DialogDemandeNomClone.old = BrickEditor.editeurOM;
				BrickEditor.processMenuKey = false;
			}

		});

		list.add(null);
		list.add(supprimer = new MenuControlleur("$supprimer", 0) {

			@Override
			public void activer() {
				// TODO Auto-generated method stub
				BrickEditor.espace.supprimer(BrickEditor.objetCourant);
				BrickEditor.menuTrajectoire.setEnabled(false);
				BrickEditor.decor.DecorDeBriqueData.modelInstances.remove(BrickEditor.objetCourant.donnerNom());
				BrickEditor.objetCourant = null;
				BrickEditor.swingEditor.reloadTree();
				BrickEditor.etat = BrickEditor.selection;
			}

		});
		list.add(null);
		list.add(axeX = new MenuControlleur("Axe x", 4) {

			@Override
			public void activer() {
				deselectionAxe();
				axeX.selected = true;
				axe = Axe.X;

			}

		});
		list.add(axeY = new MenuControlleur("Axe y", 4) {

			@Override
			public void activer() {
				deselectionAxe();
				selected = true;
				axe = Axe.Y;

			}

		});
		list.add(axeZ = new MenuControlleur("Axe z", 4) {

			@Override
			public void activer() {
				deselectionAxe();
				selected = true;
				axe = Axe.Z;

			}

		});
		list.add(null);

		list.add(editer = new MenuControlleur("editer", 0) {

			@Override
			public void activer() {
				editer.selected = true;

			}

		});

		list.add(null);
		list.add(avancer = new MenuControlleur("avancer", 0) {

			@Override
			public void activer() {
				if (reculer.selected) {
					return;
				}
				if (initialiserFin.selected) {
					return;
				}
				deselectionEtatObjet();
				TrajectoireContext tc = getTrajectoireContext();
				tc.stop = false;

				selected = true;

			}

		});
		list.add(reculer = new MenuControlleur("reculer", 0) {

			@Override
			public void activer() {
				if (avancer.selected) {
					return;
				}
				if (initialiserDebut.selected) {
					return;
				}
				TrajectoireContext tc = getTrajectoireContext();
				tc.stop = false;
				deselectionEtatObjet();
				selected = true;

			}

		});
		list.add(stopper = new MenuControlleur("stopper", 0) {

			@Override
			public void activer() {
				deselectionEtatObjet();
				selected = true;

			}

		});
		list.add(initialiserDebut = new MenuControlleur("init debut", 5) {

			@Override
			public void activer() {
				deselectionEtatObjet();
				selected = true;
				initialiserDebut();

			}

		});
		list.add(initialiserFin = new MenuControlleur("init fin", 5) {

			@Override
			public void activer() {
				deselectionEtatObjet();
				selected = true;
				TrajectoireContext tc = getTrajectoireContext();
				if (tc != null) {
					tc.initialiserFin();
				}
				Widget.drawText(widgetPositionChemin, tc.position(), Color.BLUE, Color.GREEN, Color.gray);

			}

		});
		list.add(null);
		list.add(annuler = new MenuControlleur("annuler", 1) {

			@Override
			public void activer() {

				deselection.activer();
				int size = editeurArbre.arbre.valeur.nbDeplacement();
				editeurArbre.arbre.valeur.annuler(BrickEditor.objetCourant);
				TrajectoireContext tc = getTrajectoireContext();
				if (size != editeurArbre.arbre.valeur.nbDeplacement()) {
					tc.idxDeplacement = editeurArbre.arbre.valeur.nbDeplacement() - 1;

				}

			}

		});
		list.add(menuMoins = new MenuControlleur("-", 0) {

			@Override
			public void activer() {
				if (rotation.selected) {
					gererRotation(-1.0f);
				} else {
					if (axe == Axe.X) {
						deplacer(-1, 0, 0);
					}
					if (axe == Axe.Y) {
						deplacer(0, -1, 0);
					}
					if (axe == Axe.Z) {
						deplacer(0, 0, -1);
					}
				}
				deselection.activer();

			}

		});
		list.add(menuPlus = new MenuControlleur("+", 0) {

			@Override
			public void activer() {
				if (rotation.selected) {
					gererRotation(1.0f);
				} else {
					if (axe == Axe.X) {
						deplacer(1, 0, 0);

					}
					if (axe == Axe.Y) {
						deplacer(0, 1, 0);

					}
					if (axe == Axe.Z) {
						deplacer(0, 0, 1);

					}
				}
				deselection.activer();

			}

		});
		menuMoins.initDeselection();
		menuPlus.initDeselection();
		annuler.initDeselection();

	}

	public void deplacer(int dx, int dy, int dz) {
		// System.out.println(" dx=" + dx + " dy=" + dy + " dz=" + dz);
		if (position.selected) {

			this.initialiserDebut();

			BrickEditor.objetCourant.model.x += dx;
			BrickEditor.objetCourant.model.y += dy;
			BrickEditor.objetCourant.model.z += dz;
			BrickEditor.objetCourant.move(dx, dy, dz);

		} else {

			editeurArbre.arbre.valeur.ajouter(dx, dy, dz);
			TrajectoireContext tc = getTrajectoireContext();
			tc.idxDeplacement = editeurArbre.arbre.valeur.nbDeplacement() - 1;
			Widget.drawText(widgetPositionChemin, tc.position(), Color.BLUE, Color.GREEN, Color.gray);
			// tc.n =
			// editeurArbre.arbre.valeur.deplacements.get(tc.idxDeplacement).distance;
			BrickEditor.objetCourant.move(dx, dy, dz);
		}
	}

	public void setModel() {

		Arbre<Trajectoire> tmp = this.BrickEditor.objetCourant.model.trajectoires;
		if (tmp == null) {
			tmp = new Arbre<Trajectoire>(null, new Trajectoire());
			this.BrickEditor.objetCourant.model.trajectoires = tmp;
		}
		editeurArbre.setModel(tmp);
		Widget.drawText(widgetPositionChemin, this.getTrajectoireContext().position(), Color.BLUE, Color.GREEN,
				Color.gray);

	}

	@Override
	public void valider(EditeurArbre<Trajectoire> editeurArbre) {
		// TODO Auto-generated method stub
		editer.selected = false;
		editer.updateText();

		Chemin chemin = new Chemin();
		Arbre<Trajectoire> arbre = editeurArbre.arbre;
		if (arbre.valeur == null) {
			arbre.valeur = new Trajectoire();
		}
		Arbre<Trajectoire> parent = arbre.donnerParent();
		String cheminChaine = "";
		while (parent != null) {
			cheminChaine += "/" + arbre.nom;
			// System.out.println("cheminChaine="+cheminChaine);
			chemin.map.put(parent, arbre);
			arbre = parent;
			if (arbre.valeur == null) {
				arbre.valeur = new Trajectoire();
			}
			parent = arbre.donnerParent();

		}
		Widget.drawText(widgetChemin, cheminChaine, Color.BLUE, Color.GREEN, Color.gray);
		this.initialiserDebut();
		TrajectoireContext tc = this.getTrajectoireContext();
		tc.chemin = chemin;
		this.setTrajectoireContext(tc);
		BrickEditor.swingEditor.reloadTree();

	}

	public boolean menuActif(TrajectoireContext tc) {
		if (tc == null) {
			return false;
		}
		/*
		 * if (editeurArbre.arbre.nombreEnfant() > 0) { return false; }
		 */

		if (!tc.estFini()) {
			return false;
		}
		return true;
	}

	public void gererRotation(float sign) {

		Quaternion q=null;
		if (axe == Axe.X) {
			q = new Quaternion();
			q.fromAngleAxis(sign * (float) (Math.PI / 2.0f), Vector3f.UNIT_X);

		}
		if (axe == Axe.Y) {
			q = new Quaternion();
			q.fromAngleAxis(sign * (float) (Math.PI / 2.0f), Vector3f.UNIT_Y);

		}
		if (axe == Axe.Z) {
			q = new Quaternion();
			q.fromAngleAxis(sign * (float) (Math.PI / 2.0f), Vector3f.UNIT_Z);

		}
		if (BrickEditor.objetCourant.model.rotation == null) {
			BrickEditor.objetCourant.model.rotation = new Quaternion();
			BrickEditor.objetCourant.model.rotation.loadIdentity();
		}
		q.multLocal(BrickEditor.objetCourant.model.rotation);//.multLocal(q);
		BrickEditor.objetCourant.model.rotation=q;
		BrickEditor.objetCourant.calculerPositionInitial();

	}

	public void gerer() throws CouleurErreur {
		TrajectoireContext tc = getTrajectoireContext();
		annuler.deselection.gerer();
		menuMoins.deselection.gerer();
		menuPlus.deselection.gerer();
		boolean b = menuActif(tc);
		menuMoins.setEnabled(b);
		menuPlus.setEnabled(b);
		annuler.setEnabled(b);

		try {

			if (tc != null) {
				if (avancer.selected) {

					tc.inverse = false;
					if (!tc.executer()) {

						avancer.selected = false;
						avancer.updateText();
						initialiserFin.selected = true;
						initialiserFin.updateText();

					}
					Widget.drawText(widgetPositionChemin, tc.position(), Color.BLUE, Color.GREEN, Color.gray);

				}

				if (reculer.selected) {
					tc.inverse = true;
					if (!tc.executer()) {
						reculer.selected = false;
						reculer.updateText();
						initialiserDebut.selected = true;
						initialiserDebut.updateText();

					}
					Widget.drawText(widgetPositionChemin, tc.position(), Color.BLUE, Color.GREEN, Color.gray);

				}

			}
		} catch (JeuxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!editer.selected) {
			BrickEditor.gererCamera();
			menu.processKeyboard();
		} else {
			editeurArbre.processkeyboard();
			editeurArbre.updateSelection();
			editeurArbre.processMouseButton(button);

		}

	}

	public void dessiner(Camera cam) {
		cheminTrajectoire.dessiner(BrickEditor.game.shaderWidget);
		ihmMenu.dessiner(BrickEditor.game.shaderWidget);
		if (editer.selected) {
			ihmEditeur.dessiner(BrickEditor.game.shaderWidget);
		}

	}

	public boolean estSousEcran() {
		return true;
	}

	@Override
	public void annuler(EditeurArbre<Trajectoire> editeurArbre) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean supprimer(EditeurArbre<Trajectoire> editeurArbre, Arbre<Trajectoire> nv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ajouter(EditeurArbre<Trajectoire> editeurArbre, String nv) {
		// TODO Auto-generated method stub
		return false;
	}
}
