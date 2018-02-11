package dadou.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.sun.javafx.scene.control.skin.ToolBarSkin;

import dadou.Arbre;
import dadou.Game;
import dadou.Habillage;
import dadou.ModelClasse;
import dadou.NomTexture;
import dadou.NomTexture.PlusDeTexture;
import dadou.texture.textureImport;
import dadou.ihm.Action;
import dadou.param.CaracteresParam;

public class HabillageEditorMenu implements MouseListener, ActionListener {
	JMenuItem ajouter = new JMenuItem("Ajouter");
	JMenuItem copier = new JMenuItem("Copier");
	JMenuItem supprimer = new JMenuItem("Supprimer");
	JMenuItem modifier = new JMenuItem("Modifier");
	JMenuItem modeRectangle = new JMenuItem("Mode rectangle");
	JMenuItem modePixel = new JMenuItem("Mode pixel");
	JMenuItem ajouterCaracteres = new JMenuItem("Ajouter caractères");

	JMenuItem effacer = new JMenuItem("Effacer");
	JMenuItem importer = new JMenuItem("Importer");
	JMenuItem generer = new JMenuItem("Generer textures couleurs");
	JMenuItem initialiser = new JMenuItem("Initialiser");

	JMenuItem modeBrique = new JMenuItem("Mode brique");
	JMenuItem modePlanche = new JMenuItem("Mode planche");
	JMenuItem blancEnNoire = new JMenuItem("Blanc en noire");

	List<JMenuItem> listItems = new ArrayList<>();

	JPopupMenu menuModel = new JPopupMenu();
	ArbreMenuAction ama;
	public HabillageEditorSwing habillageEditorSwing;

	public HabillageEditorMenu(HabillageEditorSwing habillageEditorSwing) {
		this.habillageEditorSwing = habillageEditorSwing;
		this.ajouterMenu(ajouter);
		this.ajouterMenu(supprimer);
		this.ajouterMenu(modifier);
		this.ajouterMenu(modeRectangle);
		this.ajouterMenu(modePixel);
		this.ajouterMenu(ajouterCaracteres);
		this.ajouterMenu(importer);
		this.ajouterMenu(generer);
		this.ajouterMenu(effacer);
		this.ajouterMenu(modeBrique);
		this.ajouterMenu(modePlanche);
		this.ajouterMenu(blancEnNoire);
		this.ajouterMenu(copier);
		this.ajouterMenu(initialiser);
	}

	public void ajouterMenu(JMenuItem item) {
		this.menuModel.add(item);
		item.addActionListener(this);
		listItems.add(item);
	}

	public void cacher() {
		for (JMenuItem item : listItems) {
			item.setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (ama == null) {
			return;
		}
		ama.menuAction.get(e.getSource()).execute();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		this.habillageEditorSwing.tree.setComponentPopupMenu(null);
		TreePath path = habillageEditorSwing.tree.getSelectionModel()
				.getSelectionPath();
		if (path == null) {
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path
				.getLastPathComponent();
		Arbre<Object> tmp = (Arbre<Object>) node.getUserObject();
		if (tmp.valeur == null) {
			return;
		}
		ama = (ArbreMenuAction) tmp.valeur;
		this.cacher();
		for (Map.Entry<JMenuItem, Action> entry : ama.menuAction.entrySet()) {
			entry.getKey().setVisible(true);
		}
		this.habillageEditorSwing.tree.setComponentPopupMenu(menuModel);
	}

	ArbreMenuAction actionsPourValeurs(Arbre<Object> arbre, Game g,
			Habillage h, String nomHabillage) {

		return ArbreMenuAction.creer().ajouter(
				ajouter,
				() -> {
					JFrame frame = this.habillageEditorSwing.window.frame;
					String nom = UI.request("Nom ", frame);
					if (h.valeurs.get(nom) != null) {
						UI.warning("Nom existant", frame);
					} else {
						h.valeurs.put(nom, new Color(0, 0, 0));
						Arbre<Object> nv = arbre.ajouter(nom);
						nv.valeur = this.actionsPourValeur(h, g, nom, arbre);
						try {
							SerializeTool.save(h, new File(
									this.habillageEditorSwing.cheminRessources,
									nomHabillage));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.habillageEditorSwing.treeModel.reload(arbre.node);
					}
				});

	}

	ArbreMenuAction actionsPourTextures(Habillage a, Game g,
			Arbre<Object> arbreTextures) {
		return ArbreMenuAction
				.creer()
				.ajouter(
						ajouter,
						() -> {
							String nom = UI.request("Nom texture ?",
									this.habillageEditorSwing.window.frame);
							if (nom == null) {
								return;
							}
							try {
								NomTexture nt = a.creerNomTexture(nom);
								if (nt == null) {
									UI.warning(
											"Nom existant",
											this.habillageEditorSwing.window.frame);
									return;
								}
								Color color = new Color(nt.idx, nt.idx, nt.idx);
								a.valeurs.put(nom, color);
								Arbre<Object> nv = arbreTextures.donnerParent()
										.enfant("valeurs").ajouter(nom);
								nv.valeur = this.actionsPourValeur(
										a,
										g,
										nom,
										arbreTextures.donnerParent().enfant(
												"valeurs"));
								try {
									SerializeTool
											.save(a,
													new File(
															this.habillageEditorSwing.cheminRessources,
															a.nomHabillage));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								this.habillageEditorSwing.treeModel
										.reload(arbreTextures.donnerParent()
												.enfant("valeurs").node);
								Arbre<Object> arbreTexture = arbreTextures
										.ajouter(nt.nom);
								arbreTexture.valeur = this.actionsPourTexture(
										a,
										this.habillageEditorSwing.window.game,
										nt, arbreTextures);
								this.habillageEditorSwing.treeModel
										.reload(arbreTextures.node);

							} catch (PlusDeTexture e) {
								UI.warning("Plus de texture disponnible",
										this.habillageEditorSwing.window.frame);

							}

						})
				.ajouter(
						importer,
						() -> {
							FileOption fo = new FileOption();
							fo.acceptFiles = true;
							fo.acceptDirectories = false;
							fo.extension = ".jpg";
							fo.multiSelect = false;
							File[] files = UI.chooseFileOrDirectory(
									this.habillageEditorSwing.window.frame, fo);
							if (files == null) {
								return;
							}

							try {
								String nom = UI.request("Nom base ?",
										this.habillageEditorSwing.window.frame);
								if (nom == null) {
									return;
								}
								BufferedImage img = ImageIO.read(files[0]);
								Point p = this.testDim(img, a);
								if (p == null) {
									UI.warning(
											"Image incompatible requis "
													+ a.dim + "x" + a.dim,
											this.habillageEditorSwing.window.frame);
									return;
								}

								this.habillageEditorSwing.setAction(() -> {
									for (int ix = 0; ix < p.x; ix++) {

										for (int iy = 0; iy < p.y; iy++) {
											NomTexture nt;
											try {
												String nm = nom;
												if (p.x > 1 || p.y > 1) {
													nm = nom + "_" + ix + "_"
															+ iy;
												}
												nt = a.creerNomTexture(nm);
											} catch (Exception e1) {
												return;
											}
											HabillageIHM ai = habillageEditorSwing
													.creerHabillageIHM(a,
															nt.idx);
											ai.mettreAjourTexture();
											Arbre<Object> tmp = arbreTextures
													.ajouter(nt.nom);
											tmp.valeur = this
													.actionsPourTexture(
															a,
															this.habillageEditorSwing.window.game,
															nt, arbreTextures);
											int dim = a.dim;
											for (int x = 0; x < dim; x++) {
												for (int y = 0; y < dim; y++) {
													int clr = img.getRGB(x + ix
															* dim, y + iy
															* dim);
													int red = (clr & 0x00ff0000) >> 16;
													int green = (clr & 0x0000ff00) >> 8;
													int blue = clr & 0x000000ff;
													Color c = new Color(red,
															green, blue);
													a.SetBlock(x,
															dim - 1 - y,
															nt.idx, c);

													try {
														ai.voxelTexture3D
																.updateBlock(x,
																		dim-1-y,
																		nt.idx,
																		c);
													} catch (Exception e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}
												}
											}
										}
									}
									SwingUtilities.invokeLater(() -> {
										this.habillageEditorSwing.treeModel
												.reload(arbreTextures.node);
									});

								});

							} catch (IOException e) {
							}
						})
				.ajouter(
						ajouterCaracteres,
						() -> {

							new CaracteresParam(
									this.habillageEditorSwing.window.frame, a,
									this, arbreTextures);

						});

	}

	public void creerCaracteres(Habillage a, Arbre<Object> arbreTextures,
			CaracteresParam cp) throws PlusDeTexture {
		String caracteres = cp.caracteres();
		if (caracteres == null) {
			return;
		}
		Arbre<Object> arbreValeurs = arbreTextures.donnerParent().enfant(
				"valeurs");
		for (int i = 0; i < caracteres.length(); i++) {
			String nom = "" + caracteres.charAt(i);
			if (cp.inverserX()) {
				nom = "ix_" + nom;
			}
			NomTexture nt = a.creerNomTexture(nom);
			if (nt != null) {

				BufferedImage bi = new BufferedImage(a.dim, a.dim,
						BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = bi.createGraphics();
				Color cf = cp.couleurFond();
				g.setColor(cp.couleurFond());
				g.fillRect(0, 0, a.dim, a.dim);
				if (cp.bordure()) {
					float red = cf.getRed();
					float green = cf.getGreen();
					float blue = cf.getBlue();
					float tx = 0.85f;
					red = tx * red;
					blue = tx * blue;
					green = tx * green;

					Color c = new Color((int) red, (int) green, (int) blue);
					g.setColor(c);
					int b = 3;
					g.fillRect(b, b, a.dim - 2 * b, a.dim - 2 * b);

				}

				Font font = Font.decode("arial-22");
				g.setFont(font);
				FontMetrics fm = g.getFontMetrics();

				int sz = fm.charWidth(caracteres.charAt(i));
				int px = (a.dim - sz) / 2;
				int py = fm.getHeight();
				g.setColor(cp.couleurCaractere());
				g.drawString("" + caracteres.charAt(i), px, py);
				g.dispose();
				HabillageIHM ai = habillageEditorSwing.creerHabillageIHM(a,
						nt.idx);
				if (cp.creerKube()) {
					a.valeurs.put(nt.nom, new Color(nt.idx, nt.idx, nt.idx));

					Arbre<Object> nv = arbreValeurs.ajouter(nt.nom);
					nv.valeur = this.actionsPourValeur(a,
							this.habillageEditorSwing.window.game, nt.nom,
							arbreValeurs);

				}
				ai.mettreAjourTexture();
				Arbre<Object> tmp = arbreTextures.ajouter(nt.nom);
				tmp.valeur = this.actionsPourTexture(a,
						this.habillageEditorSwing.window.game, nt,
						arbreTextures);
				for (int x = 0; x < a.dim; x++) {
					for (int y = 0; y < a.dim; y++) {
						int clr = bi.getRGB(x, y);
						int red = (clr & 0x00ff0000) >> 16;
						int green = (clr & 0x0000ff00) >> 8;
						int blue = clr & 0x000000ff;
						Color c = new Color(red, green, blue);
						int ux;
						if (cp.inverserX()) {
							ux = x;
						} else {
							ux = a.dim - x - 1;
						}
						a.SetBlock(ux, a.dim - y - 1, nt.idx, c);

						try {
							ai.voxelTexture3D.updateBlock(ux, a.dim - y - 1,
									nt.idx, c);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

		}
		SwingUtilities.invokeLater(() -> {
			this.habillageEditorSwing.treeModel.reload(arbreTextures.node);
			this.habillageEditorSwing.treeModel.reload(arbreValeurs.node);
		});

	}

	Point testDim(BufferedImage img, Habillage a) {

		int nx = img.getWidth() / a.dim;
		if (nx * a.dim != img.getWidth()) {
			return null;
		}

		int ny = img.getHeight() / a.dim;
		if (ny * a.dim != img.getHeight()) {
			return null;
		}
		return new Point(nx, ny);

	}

	ArbreMenuAction actionsPourTexture(Habillage a, Game game, NomTexture nt,
			Arbre<Object> textures) {
		return ArbreMenuAction
				.creer()
				.selection(
						() -> {

							this.habillageEditorSwing.setAction(() -> {
								HabillageIHM ai = habillageEditorSwing
										.creerHabillageIHM(a, nt.idx);
								ai.modeIHM = true;
							});

						})
				.ajouter(
						this.modePixel,
						() -> {
							this.habillageEditorSwing.ihms.get(a).modeRectangle = false;
						})
				.ajouter(this.modeRectangle, () -> {
					this.habillageEditorSwing.ihms.get(a).modeRectangle = true;
				})
				.ajouter(
						supprimer,
						() -> {
							a.noms[nt.idx] = null;
							textures.supprimer(nt.nom);
							this.habillageEditorSwing.setAction(() -> {
								HabillageIHM ai = habillageEditorSwing
										.creerHabillageIHM(a, nt.idx);
								ai.modeIHM = false;
							});
							this.habillageEditorSwing.treeModel
									.reload(textures.node);
						})
				.ajouter(
						effacer,
						() -> {
							for (int x = 0; x < a.dim; x++) {
								for (int y = 0; y < a.dim; y++) {
									a.SetBlock(x, y, nt.idx, Color.BLACK);
								}
							}
							this.habillageEditorSwing.setAction(() -> {
								try {
									HabillageIHM ai = habillageEditorSwing
											.creerHabillageIHM(a, nt.idx);
									ai.mettreAjourTexture();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							});

						})
				.ajouter(
						blancEnNoire,
						() -> {
							for (int x = 0; x < a.dim; x++) {
								for (int y = 0; y < a.dim; y++) {
									Color c = a.GetBlock(x, y, nt.idx);
									int whiteLimit = 238;
									if (c.getBlue() >= whiteLimit
											&& c.getRed() >= whiteLimit
											&& c.getGreen() >= whiteLimit) {
										a.SetBlock(x, y, nt.idx, Color.BLACK);
									}
								}
							}
							this.habillageEditorSwing.setAction(() -> {
								try {
									HabillageIHM ai = habillageEditorSwing
											.creerHabillageIHM(a, nt.idx);
									ai.mettreAjourTexture();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							});

						})
				.ajouter(
						copier,
						() -> {
							String nomCopie = UI.request("Nom copie",
									this.habillageEditorSwing.window.frame);
							if (nomCopie == null) {
								return;
							}
							try {
								NomTexture ntCopie = a
										.creerNomTexture(nomCopie);
								for (int x = 0; x < a.dim; x++) {
									for (int y = 0; y < a.dim; y++) {
										a.SetBlock(x, y, ntCopie.idx,
												a.GetBlock(x, y, nt.idx));
									}
								}
								this.habillageEditorSwing.setAction(() -> {
									try {
										HabillageIHM ai = habillageEditorSwing
												.creerHabillageIHM(a,
														ntCopie.idx);
										ai.mettreAjourTexture();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});
								SwingUtilities.invokeLater(() -> {
									// textures.ajouter(nomCopie);
									Arbre<Object> arbreTexture = textures
											.ajouter(ntCopie.nom);
									arbreTexture.valeur = this
											.actionsPourTexture(
													a,
													this.habillageEditorSwing.window.game,
													ntCopie, textures);
									this.habillageEditorSwing.treeModel
											.reload(textures.node);
								});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						});
	}

	public void modifierValeur(Habillage a, String nom, Color c) {
		this.habillageEditorSwing.setAction(() -> {
			HabillageIHM h = this.habillageEditorSwing.creerHabillageIHM(a, 0);
			h.habillage.valeurs.put(nom, c);
			try {
				h.mc.vbo.tex.updateBlock(0, 0, 0, c);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			h.modeIHM = false;
		});

	}

	public void modelePlanche(Habillage a, String nom) {
		this.habillageEditorSwing.setAction(() -> {
			HabillageIHM h = this.habillageEditorSwing.creerHabillageIHM(a, 0);
			Color c = h.habillage.valeurs.get(nom);
			Color copie[][][] = new Color[1][1][1];
			copie[0][0][0] = c;
			h.mc.brique.delete();
			h.mc = new ModelClasse();
			h.mc.copie = copie;
			h.mc.dx = 1;
			h.mc.dy = 1;
			h.mc.dz = 1;
			h.mc.estModelPlanche = true;

			h.modeIHM = false;
			try {
				h.obj = h.creerObjet(h.mc, h.game);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void modeleBrique(Habillage a, String nom) {
		this.habillageEditorSwing.setAction(() -> {
			HabillageIHM h = this.habillageEditorSwing.creerHabillageIHM(a, 0);
			Color c = h.habillage.valeurs.get(nom);
			Color copie[][][] = new Color[1][1][1];
			copie[0][0][0] = c;
			h.mc.brique.delete();
			h.mc = new ModelClasse();
			h.mc.copie = copie;
			h.mc.dx = 1;
			h.mc.dy = 1;
			h.mc.dz = 1;
			h.mc.estModelPlanche = false;

			h.modeIHM = false;
			try {
				h.obj = h.creerObjet(h.mc, h.game);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	ArbreMenuAction actionsPourValeur(Habillage a, Game game, String nomValeur,
			Arbre<Object> valeurs) {
		return ArbreMenuAction
				.creer()
				.selection(() -> {

					Color c = a.valeurs.get(nomValeur);
					this.modifierValeur(a, nomValeur, c);

				})
				.ajouter(
						modifier,
						() -> {
							Color color = a.valeurs.get(nomValeur);

							DialogModificationValeurHabillage dialog = new DialogModificationValeurHabillage(
									this.habillageEditorSwing.window.frame,
									nomValeur, a.noms(), a.nom(color.getRed()),
									a.nom(color.getGreen()), a.nom(color
											.getBlue()));
							dialog.action = () -> {
								Color c = new Color(dialog.donnerFaceX(),
										dialog.donnerFaceY(), dialog
												.donnerFaceZ());
								this.modifierValeur(a, nomValeur, c);
							};

						}).ajouter(supprimer, () -> {
					a.valeurs.remove(nomValeur);
					valeurs.supprimer(nomValeur);
					this.habillageEditorSwing.treeModel.reload(valeurs.node);
				}).ajouter(modeBrique, () -> {
					this.modeleBrique(a, nomValeur);
				}).ajouter(modePlanche, () -> {
					this.modelePlanche(a, nomValeur);
				});

	}

	ArbreMenuAction actionsPourHabillages(Arbre<Object> arbre) {
		return ArbreMenuAction.creer().ajouter(ajouter, () -> {
			JFrame frame = this.habillageEditorSwing.window.frame;

			new DialogCreationHabillage(frame, this, arbre);

		});

	}

	ArbreMenuAction actionsPourHabillage(Arbre<Object> arbre, Habillage h) {
		return ArbreMenuAction
				.creer()
				.ajouter(generer, () -> {

					this.habillageEditorSwing.setAction(() -> {
						try {
							this.genererCouleur(arbre, h);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});

				})
				.ajouter(
						initialiser,
						() -> {
							FileOption fo = new FileOption();
							fo.acceptFiles = true;
							fo.acceptDirectories = false;
							fo.extension = ".tx";
							fo.multiSelect = false;
							File[] files = UI.chooseFileOrDirectory(
									this.habillageEditorSwing.window.frame, fo);
							if (files == null) {
								return;
							}
							try {
								textureImport.chargerTextures(h, arbre.nom,
										files[0].getName());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							SwingUtilities.invokeLater(() -> {
								this.habillageEditorSwing.treeModel
										.reload(arbre.node);
							});

						});

	}

	public void genererCouleur(Arbre<Object> arbre, Habillage h)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, PlusDeTexture {
		String colorNames[] = new String[] { "RED", "BLUE", "GREEN", "CYAN",
				"YELLOW", "PINK", "DARK_GRAY", "GRAY", "ORANGE", "LIGHT_GRAY",
				"MAGENTA", "WHITE"

		};
		Arbre<Object> arbreTextures = arbre.enfant("textures");
		Arbre<Object> arbreValeurs = arbre.enfant("valeurs");
		int iNom = 0;
		for (String nomColor : colorNames) {
			for (float cf : new float[] { 0.60f, 0.80f, 1.0f }) {
				String nom = nomColor + iNom;
				iNom++;
				Color c = (Color) Color.class.getField(nomColor).get(null);
				NomTexture nt = h.creerNomTexture(nom);
				int marge = 2;
				if (nt != null) {
					for (int x = 0; x < h.dim; x++) {
						for (int y = 0; y < h.dim; y++) {

							h.SetBlock(x, y, nt.idx, color(c, cf));

						}
					}
					HabillageIHM ai = habillageEditorSwing.creerHabillageIHM(h,
							nt.idx);
					ai.mettreAjourTexture();
					Arbre<Object> tmp = arbreTextures.ajouter(nt.nom);

					tmp.valeur = this.actionsPourTexture(h,
							this.habillageEditorSwing.window.game, nt,
							arbreTextures);

					h.valeurs.put(nom, new Color(nt.idx, nt.idx, nt.idx));
				}
				Arbre<Object> nv = arbreValeurs.ajouter(nom);
				nv.valeur = this.actionsPourValeur(h,
						this.habillageEditorSwing.window.game, nom, arbre);

			}
		}

		iNom = 0;
		for (String nomColor : colorNames) {
			for (float cf : new float[] { 0.60f, 0.80f, 1.0f }) {
				String nom = "G" + nomColor + iNom;
				iNom++;
				Color c = (Color) Color.class.getField(nomColor).get(null);
				NomTexture nt = h.creerNomTexture(nom);
				int marge = h.dim / 8;
				if (nt != null) {
					for (int x = 0; x < h.dim; x++) {
						for (int y = 0; y < h.dim; y++) {
							if (x >= marge && x < h.dim - marge && y >= marge
									&& y < h.dim - marge) {
								h.SetBlock(x, y, nt.idx, Color.BLACK);
							} else {
								h.SetBlock(x, y, nt.idx, color(c, cf));
							}

						}
					}
					HabillageIHM ai = habillageEditorSwing.creerHabillageIHM(h,
							nt.idx);
					ai.mettreAjourTexture();
					Arbre<Object> tmp = arbreTextures.ajouter(nt.nom);

					tmp.valeur = this.actionsPourTexture(h,
							this.habillageEditorSwing.window.game, nt,
							arbreTextures);

					h.valeurs.put(nom, new Color(nt.idx, nt.idx, nt.idx));
				}
				Arbre<Object> nv = arbreValeurs.ajouter(nom);
				nv.valeur = this.actionsPourValeur(h,
						this.habillageEditorSwing.window.game, nom, arbre);

			}
		}
		SwingUtilities.invokeLater(() -> {
			this.habillageEditorSwing.treeModel.reload(arbre.node);
		});

	}

	public void genererCouleurSur4x4x4(Arbre<Object> arbre, Habillage h)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException, PlusDeTexture {
		int n = 3;
		Arbre<Object> arbreTextures = arbre.enfant("textures");
		Arbre<Object> arbreValeurs = arbre.enfant("valeurs");
		for (int r = 0; r < n; r++) {
			for (int g = 0; g < n; g++) {
				for (int b = 0; b < n; b++) {

					String nom = "" + r + "_" + g + "_" + b;
					Color c = new Color(r * 64, g * 64, b * 64);
					NomTexture nt = h.creerNomTexture(nom);
					int marge = 2;
					if (nt != null) {
						for (int x = 0; x < h.dim; x++) {
							for (int y = 0; y < h.dim; y++) {

								h.SetBlock(x, y, nt.idx, c);

							}
						}
						HabillageIHM ai = habillageEditorSwing
								.creerHabillageIHM(h, nt.idx);
						ai.mettreAjourTexture();
						Arbre<Object> tmp = arbreTextures.ajouter(nt.nom);

						tmp.valeur = this.actionsPourTexture(h,
								this.habillageEditorSwing.window.game, nt,
								arbreTextures);

						h.valeurs.put(nom, new Color(nt.idx, nt.idx, nt.idx));

						Arbre<Object> nv = arbreValeurs.ajouter(nom);

						nv.valeur = this.actionsPourValeur(h,
								this.habillageEditorSwing.window.game, nom,
								arbre);
					}
				}
			}
		}

		SwingUtilities.invokeLater(() -> {
			this.habillageEditorSwing.treeModel.reload(arbre.node);
		});

	}

	public Color color(Color c, float tx) {
		float b = c.getBlue();
		float r = c.getRed();
		float g = c.getGreen();
		b = (b / 255.0f) * tx;
		r = (r / 255.0f) * tx;
		g = (g / 255.0f) * tx;
		return new Color(r, g, b);

	}

	public boolean estSurLeBord(Habillage h, int x, int y, int marge) {
		if (x < marge) {
			return true;
		}
		if (y < marge) {
			return true;
		}
		if (x > h.dim - 1 - marge) {
			return true;
		}
		if (y > h.dim - 1 - marge) {
			return true;
		}
		return false;
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

}
