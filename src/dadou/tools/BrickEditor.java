package dadou.tools;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.OpenGLException;

import com.jme.bounding.BoundingSphere;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Matrix4f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.lwjgl.LWJGLCamera;
import com.jme.util.geom.BufferUtils;

import dadou.AbstractJoueur;
import dadou.Arbre;
import dadou.Button;
import dadou.tools.camera.ArbreCameraPositions;
import dadou.tools.camera.GestionIcone;
import dadou.ChargeurDecor;
import dadou.ControlleurCamera;
import dadou.ControlleurCameraEditeur;
import dadou.CreerIcone;
import dadou.Kube;
import dadou.Log;
import dadou.Lumiere;
import dadou.Main;
import dadou.Deceleration;
import dadou.DecorDeBrique;
import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.ElementDecor;
import dadou.Espace;
import dadou.EspaceSelector;
import dadou.EtatOmbre;
import dadou.ExplorerAuSolCameraControllerBis;
import dadou.FBO;
import dadou.FBORenduLumiereDiffere;
import dadou.FBOShadowMap;
import dadou.Fps;
import dadou.Game;
import dadou.GameInfo;
import dadou.Grappin;
import dadou.Habillage;
import dadou.Joueur;
import dadou.Key;
import dadou.ModelClasse;
import dadou.ModelEvent;
import dadou.ModelInstance;
import dadou.MondeInterfacePrive;
import dadou.MondeInterfacePublic;
import dadou.Objet3D;
import dadou.ObjetMobile;
import dadou.ObjetMobilePourModelInstance;
import dadou.OcclusionVolume;
import dadou.OrientedBoundingBoxWithVBO;
import dadou.EffetEcran;
import dadou.SautCamera;
import dadou.SautMonde;
import dadou.SauvegardeModel;
import dadou.SauvegardeMonde;
import dadou.Shader;
import dadou.ShadowData;
import dadou.Skybox;
import dadou.Soleil;
import dadou.StopperBoucle;
import dadou.VBOTexture2D;
import dadou.VitesseCamera;
import dadou.VoxelShaderColor;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventNode;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheConstructeur;
import dadou.graphe.GrapheDebug;
import dadou.graphe.GrapheSommet;
import dadou.greffon.Boite;
import dadou.greffon.GreffonForme;
import dadou.greffon.ZoneGreffon;
import dadou.ihm.Action;
import dadou.ihm.Comportement;
import dadou.ihm.IHM;
import dadou.ihm.Menu;
import dadou.ihm.MenuControlleur;
import dadou.ihm.Widget;
import dadou.mutator.Orientation;
import dadou.parallele.GestionTraitementParallele;
import dadou.parallele.Parallele;
import dadou.param.GraphicsParam;
import dadou.tools.canon.CallbackCanonDestructionOuConstruction;
import dadou.tools.canon.Canon;
import dadou.tools.canon.CibleAffichageDistance;
import dadou.tools.canon.EtatCanon;
import dadou.tools.construction.AfficherBoxSelection;
import dadou.tools.construction.ArbreModelClasse;
import dadou.tools.construction.ChoixModelClasse;
import dadou.tools.construction.DialogDemandeSaisieChaine;
import dadou.tools.construction.Selection;
import dadou.tools.construction.SelectionGroupeModelClasse;
import dadou.tools.construction.SelectionModelClasse;
import dadou.tools.editionOM.DialogDemandeNomClone;
import dadou.tools.editionOM.EditeurOM;
import dadou.tools.graphics.Config;
import dadou.tools.graphics.Info;
import dadou.tools.monde.SelectionMonde;
import dadou.tools.texture.ChoixKube;
import dadou.tools.texture.ChoixTexture;

public class BrickEditor implements Comportement {

	public DecorDeBrique decor;
	public Espace espace;
	public String joueur;
	public Parallele<Runnable> parallele;
	public Runnable callbackModification = () -> {
	};
	public static Map<String, String> arguments = new HashMap<>();
	Map<String, Habillage> habillages = new HashMap<>();
	public ServiceInterfaceClient sic;
	public String version;
	private List<Action> actions = new ArrayList<>();
	static public Map<String, Skybox> skyboxMap = new HashMap<>();
	public GameEventLeaf workspace = new GameEventLeaf("workspace");
	public Map<String, ModelClasse> dynamicsModelClasse = new HashMap<>();
	public Map<String, ChoixModelClasse> choixModelClasses = new HashMap<>();

	public ExplorerAuSolCameraControllerBis scc;
	public Config config;
	public dadou.tools.construction.DialogDemandeNomClasse DialogDemandeNomClasse;

	public DialogDemandeNomClone DialogDemandeNomClone;
	public ChoixModelClasse choixModelClasse;
	public OcclusionVolume occlusionVolume = new OcclusionVolume();
	public Message message;
	public ChoixKube choixKube;
	public ChoixTexture choixTexture;

	public SelectionModelClasse selectionModel;
	public ArbreModelClasse arbreModelClasse;
	public ArbreCameraPositions arbreCameraPositions;
	public SelectionGroupeModelClasse selectionGroupeModel;

	public VitesseCamera vc = new VitesseCamera();
	public Deceleration deceleration;
	public Game game;

	public Menu menu;

	public EtatBrickEditor etat;

	public dadou.tools.canon.EtatCanon EtatCanon;

	public Selection selection;
	public SelectionMonde SelectionMonde;

	public GestionIcone gestionIcone;

	public EditeurOM editeurOM;

	public boolean grille = false;
	public FBO fbo;

	boolean release = true;

	public void ajouterAction(Action a) {
		synchronized (actions) {
			actions.add(a);
		}
	}

	public ModelClasse modelClassePourCube(Color color, String nomHabillage)
			throws CouleurErreur, Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(nomHabillage);

		sb.append("_");
		sb.append(color.getRed());
		sb.append("_");
		sb.append(color.getGreen());
		sb.append("_");
		sb.append(color.getBlue());
		String clef = sb.toString();
		ModelClasse mc = this.dynamicsModelClasse.get(clef);
		if (mc != null) {
			return mc;
		}
		mc = new ModelClasse();
		mc.init(1, 1, 1);
		mc.setColor(0, 0, 0, color);
		mc.initBuffer(game, this.donnerHabillage(nomHabillage));
		this.dynamicsModelClasse.put(clef, mc);
		return mc;

	}

	static public void lancer(String chemin, AbstractJoueur joueur) {
		try {
			AbstractJoueur.joueur = joueur;
			GraphicsParam.lancer(chemin);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public void main(String args[]) throws Exception {

		try {
			GraphicsParam.lancer(args[0]);
		} catch (LWJGLException e) {

		}

	}

	public VoxelTexture3D donnerHabillageVoxelTexture3D(String nomHabillage) {
		Habillage habillage = this.donnerHabillage(nomHabillage);
		if (habillage == null) {

			return null;
		}

		return habillage.creerVoxelTexture3D();

	}

	public Vector3f tmp = new Vector3f();
	Quaternion qTmp = new Quaternion();
	boolean debugTmpBox = true;
	OrientedBoundingBoxWithVBO tmpBox = new OrientedBoundingBoxWithVBO();

	public boolean estAccessibleDepuis(Vector3f debut, Vector3f fin, float sx,
			float sy, EspaceSelector es) {
		tmp.y = sx;
		tmp.z = sy;

		tmpBox.setXAxis(Vector3f.UNIT_X);
		tmpBox.setYAxis(Vector3f.UNIT_Y);
		tmpBox.setZAxis(Vector3f.UNIT_Z);
		tmpBox.setExtent(tmp);
		tmp.set(debut);
		tmp.addLocal(fin);
		tmp.multLocal(0.5f);

		tmpBox.getCenter().set(tmp);

		tmp.set(fin);
		tmp.subtractLocal(debut);
		tmp.multLocal(0.5f);

		tmpBox.getExtent().x = tmp.length();
		tmp.normalizeLocal();
		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		qTmp.multLocal(tmpBox.getXAxis());
		qTmp.multLocal(tmpBox.getYAxis());
		qTmp.multLocal(tmpBox.getZAxis());
		if (this.debugTmpBox) {
			tmpBox.createDebugBox(this.shaderBox, null);
			tmpBox.objDebugBox.positionToZero();
			tmpBox.objDebugBox.translation(tmpBox.getCenter());
			tmpBox.objDebugBox.setRotation(qTmp);

		}
		ObjetMobile om = espace.getOMFor(tmpBox, es);
		if (om != null) {
			return false;
		}
		return !decor.gestionCollision.verifierCollision(tmpBox);

	}

	public Habillage donnerHabillage(String nomHabillage) {
		if (nomHabillage == null) {
			return null;
		}
		Habillage h = this.habillages.get(nomHabillage);
		if (h == null) {
			try {
				Log.print(" chargement " + nomHabillage);
				h = (Habillage) SerializeTool.load(new File(
						this.cheminRessources, nomHabillage));
				this.habillages.put(nomHabillage, h);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return h;

	}

	public ZoneGreffon zg = new ZoneGreffon();

	public void initModelClasseZoneGreffon() {
		etat = selection;
		ModelClasse mc = zg.mc;
		selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0, mc);
		;

		if (mc.echelle == 0.0f) {
			mc.echelle = 1.0f;
		}

		selection.BoxSelectionAction = selection.BoxColler;
		selection.EtatBoxSelection = new AfficherBoxSelection(mc.dx, mc.dy,
				mc.dz, mc.echelle, mc);
		etat = selection;
	}

	public Color couleurPourHabillage(String nomHabillage) {
		Habillage h = this.habillages.get(nomHabillage);
		if (h == null) {
			return Color.BLACK;
		}
		if (menuTexture.selected) {
			ChoixTexture ct = h.donnerChoixTexture(game, true);
			return new Color(ct.choix.valeur.idx, ct.choix.valeur.idx,
					ct.choix.valeur.idx);
		}
		if (h.choixKube == null) {
			return Color.BLACK;
		}
		if (h.choixKube.choix == null) {
			return Color.BLACK;
		}
		return h.choixKube.choix.valeur;
	}

	public boolean contientSelectionKube(String nomHabillage) {
		Habillage h = this.habillages.get(nomHabillage);
		if (h == null) {
			return false;
		}

		if (h.choixKube == null) {
			return false;
		}
		if (h.choixKube.choix == null) {
			return false;
		}
		return true;
	}

	public boolean contientSelectionModel() {
		if (choixModelClasse == null) {
			return false;
		}
		if (choixModelClasse.choix == null) {
			return false;
		}
		return true;
	}

	public ModelClasse selectionModel() {

		if (choixModelClasse == null) {
			return null;
		}
		if (choixModelClasse.choix == null) {
			return null;
		}
		return choixModelClasse.choix.valeur;
	}

	public void demarerMonde() {
		this.mondeInterface.mapAffichage = new HashMap<>();
		if (mondeInterface.sauvegardeMonde != null) {
			mondeInterface.sauvegardeMonde.monde.i = this.mondeInterface.mondeInterface;
			mondeInterface.sauvegardeMonde
					.chargerDebut(mondeInterface.sauvegardeJoueur);

		} else {
			decor.DecorDeBriqueData.demarer(this.mondeInterface);

		}
		List<ObjetMobilePourModelInstance> list = new ArrayList<>();
		list.addAll(espace.mobiles.values());

		for (ObjetMobilePourModelInstance om : list) {

			// if (om.nom == null) {

			String nomModel = om.mc.nomModele();
			ModelEvent me = this.decorDeBriqueData.events.get(nomModel);
			if (me == null) {
				throw new Error(" " + nomModel);
			}
			om.me = me;

			if (om.model != null) {
				if (om.mei == null) {
					om.mei = om.evenement().creerModeleEventInterface(nomModel,
							this.mondeInterface);
					Map<String, String> params = null;
					params = om.model.params();
					om.evenement().demarer(om, params);
				}
			}

			// }
		}
		if (mondeInterface.sauvegardeMonde == null) {
			decor.DecorDeBriqueData.finaliser(this.mondeInterface);
		} else {
			mondeInterface.sauvegardeMonde.chargerElements(this);
			mondeInterface.sauvegardeMonde.chargerFin();
		}

	}

	public void boucler() {
		try {
			this.espace.boucler();
			this.decor.DecorDeBriqueData.boucler(this.mondeInterface);
			AbstractJoueur aj = this.mondeInterface.joueur.aj;
			if (aj != null) {
				aj.i = this.mondeInterface.mondeInterface;

				aj.boucler();

			}

		} catch (StopperBoucle e) {
			// TODO Auto-generated catch block
			mondeInterface.active = false;
			mondeInterface.mondeInterface.arreter();

		}

	}

	public Color kubeCourant() {
		String nomHabillage = decor.DecorDeBriqueData.nomHabillage;
		return couleurPourHabillage(nomHabillage);
	}

	public void initSelection() {
		selection = new Selection(this, 32, 32, 32);
		selection.BrickEditor = this;

	}

	long delay = 0;
	Vector3f worldPosition = new Vector3f();
	Quaternion q = new Quaternion();

	public void gererCamera() throws CouleurErreur {
		try {
			this.gererCamera(verouillerRotationEtTranslationCamera);
			// occlusionVolume.init(this.cam, scc.screenWidth, scc.screenHeight,
			// 1.0f);
			// occlusionVolume.compute(500.0f,7 , this.decor.gestionCollision);

		} catch (SautCamera e) {

		}

	}

	public Vector3f depCopy = new Vector3f();
	public ObjetMobile omHauteur;
	public BoundingSphere bsPourSol = new BoundingSphere();
	public int hauteurSautMax = 10;
	public int hauteur = 0;
	public float hauteurSaut = 0.0f;
	public boolean toucherSol = true;
	public float hauteurChute = 0;
	public boolean chute = false;
	public Key toucheSaut = new Key(Keyboard.KEY_RSHIFT);
	public Key toucheDemiTour = new Key(Keyboard.KEY_W);
	public Key flecheGauche = new Key(Keyboard.KEY_LEFT);
	public Key flecheDroite = new Key(Keyboard.KEY_RIGHT);
	public boolean sautForce = false;
	public int hauteurSautForce = hauteurSautMax;

	public void gererCamera(boolean verouillerCamera) throws CouleurErreur,
			SautCamera {
		if (verouillerCamera) {
			return;
		}
		ControlleurCamera cc = ControlleurCamera.controlleur;
		boolean sauter = cc.faireSaut(this) || sautForce;
		if (hauteurSautMax == 0 && !sautForce) {
			sauter = false;
		}
		if (toucherSol && sauter) {
			if (sautForce) {
				hauteur = hauteurSautForce;
			} else {
				hauteur = hauteurSautMax;

			}
			toucherSol = false;

		}
		if (!sauter) {
			hauteur = 0;
			hauteurSaut = 0.0f;
		}
		// System.out.println(" hasFocus="+this.swingEditor.canvasHasFocus());

		scc.save();
		// if (System.currentTimeMillis() -delay > 50) {
		vc.reset();

		cc.modifierCameraAvecSouris(this);
		if (toucherSol || !this.scc.sautSansDeplacement) {

			cc.gererAvancerReculer(this);
		}
		delay = System.currentTimeMillis();
		// }
		this.depCopy.set(scc.dep);
		scc.deplacerCamera(vc);
		float dimX = decor.DecorDeBriqueData.decorInfo.nbCube;
		float dimY = decor.DecorDeBriqueData.decorInfo.nbCube;
		float dimZ = decor.DecorDeBriqueData.decorInfo.nbCube;
		worldPosition.set(-dimX / 2.0f, -dimY / 2.0f, -dimZ / 2.0f);

		scc.calculerBox(cam, vc);
		// boolean collisionDecorCamera = verifieur.verifierCollision(game, q,
		// decor.tex, worldPosition, 1.0f, 0.01f);
		boolean restore = true;

		boolean collisionDecorCamera = this.collisionDecorCamera();

		boolean intersectSphereCam = espace.octree.box
				.intersects(scc.sphereCam);

		if (mondeInterface.active && (!intersectSphereCam)) {
			scc.restore();
			scc.calculerBox(cam, vc);
			this.chute = false;
			this.hauteurChute = 0.0f;
			this.decor.DecorDeBriqueData.sortieMonde(this.mondeInterface);
			return;

		}
		if (collisionDecorCamera) {

			scc.restore();

			scc.calculerBox(cam, vc);
			vc.vitesseDeplacementX = 0.0f;
			scc.deplacerCamera(vc);
			scc.calculerBox(cam, vc);

			if (this.collisionDecorCamera()) {
				scc.restore();
				scc.calculerBox(cam, vc);
				vc.vitesseDeplacementX = 1.0f;
				vc.vitesseDeplacementZ = 0.0f;
				scc.deplacerCamera(vc);
				scc.calculerBox(cam, vc);

				if (this.collisionDecorCamera()) {
					scc.restore();
					scc.calculerBox(cam, vc);
					restore = false;
				}
			}
			vc.vitesseDeplacementZ = 1.0f;
			vc.vitesseDeplacementX = 1.0f;

			// restore = false;

		}
		if (restore) {
			if (this.gererCollisionCameraAvecObjetMobile()) {
				scc.restore();
				scc.calculerBox(cam, vc);
				vc.vitesseDeplacementX = 0.0f;
				scc.deplacerCamera(vc);
				scc.calculerBox(cam, vc);

				if (this.gererCollisionCameraAvecObjetMobile()) {
					scc.restore();
					scc.calculerBox(cam, vc);
					vc.vitesseDeplacementX = 1.0f;
					vc.vitesseDeplacementZ = 0.0f;
					scc.deplacerCamera(vc);
					scc.calculerBox(cam, vc);

					if (this.gererCollisionCameraAvecObjetMobile()) {
						scc.restore();
						scc.calculerBox(cam, vc);

					}
				}

			}
			vc.vitesseDeplacementZ = 1.0f;
			vc.vitesseDeplacementX = 1.0f;

		}

		omHauteur = null;
		if (hauteur > 0) {
			float deltatSaut = 0.5f;
			scc.deplacerPositionCamera(0, deltatSaut, 0);
			scc.calculerBox(cam, vc);
			hauteur--;
			hauteurSaut += deltatSaut;

			if ((mondeInterface.active)) {
				AbstractJoueur aj = mondeInterface.joueur.aj;
				if (aj != null) {
					aj.i = this.mondeInterface.mondeInterface;
					aj.sautEnCours(sautForce, hauteurSaut);
				} else
					this.decor.DecorDeBriqueData.sautEnCours(
							this.mondeInterface, sautForce, hauteurSaut);

			}
			if (this.annulerDeplacementSiBesoin(0, deltatSaut, 0)) {
				hauteur = 0;
				hauteurSaut = 0.0f;
			}

		} else if (scc.auSol()) {
			float hauteur = 2.0f;
			float deltat = 1f;
			float deltatDescente = 0.75f;
			bsPourSol.setRadius(scc.sphereCam.radius);
			omHauteur = scc.donnerObjetMobilePourHauteur(bsPourSol, hauteur);
			boolean collisionHauteur = scc.collisionDecorPourHauteur(bsPourSol,
					hauteur);
			toucherSol = omHauteur != null || collisionHauteur;
			if (toucherSol) {
				sautForce = false;
			}
			if (omHauteur != null) {
				if (this.hauteurChute > 0.0f) {
					chute = true;
				}
				;
				if (!omHauteur.dep.equals(Vector3f.ZERO)) {

					scc.deplacerPositionCamera(omHauteur.dep);
					scc.calculerBox(cam, vc);

					this.annulerDeplacementSiBesoin(omHauteur.dep.x,
							omHauteur.dep.y, omHauteur.dep.z);
					omHauteur.dep.set(Vector3f.ZERO);
				} else if (scc.donnerObjetMobilePourHauteur(bsPourSol, hauteur
						- deltat) != null) {
					scc.deplacerPositionCamera(0, deltat, 0);
					scc.calculerBox(cam, vc);
					this.annulerDeplacementSiBesoin(0, deltat, 0);

				}
			} else {
				if (!collisionHauteur) {
					scc.deplacerPositionCamera(0, -deltatDescente, 0);
					scc.calculerBox(cam, vc);
					if (this.annulerDeplacementSiBesoin(0, -deltatDescente, 0)) {
						scc.restore();
						toucherSol = true;
						sautForce = false;
					} else {
						hauteurChute += deltatDescente;
					}

				} else {
					if (this.hauteurChute > 0.0f) {
						chute = true;
					}
					;
					if (scc.collisionDecorPourHauteur(bsPourSol, hauteur
							- deltat)) {
						scc.deplacerPositionCamera(0, deltat, 0);
						scc.calculerBox(cam, vc);
						this.annulerDeplacementSiBesoin(0, deltat, 0);
					}

				}

			}
		} else {
			toucherSol = false;
		}

		/*
		 * if (collisionDecorCamera) { scc.restore(); }
		 */

	}

	public boolean collisionDecorCamera() {
		if (this.decor.gestionCollision.verifierCollision(scc.boxCam)
				&& vc.vitesseDeplacement > 0) {
			return true;
		}
		if (this.decor.gestionCollision.verifierCollision(scc.sphereCam)) {
			return true;

		}
		return false;
	}

	public boolean annulerDeplacementSiBesoin(float dx, float dy, float dz)
			throws SautCamera {
		boolean collisionDecorCamera = this.collisionDecorCamera();
		if (collisionDecorCamera) {
			scc.deplacerPositionCamera(-dx, -dy, -dz);
			return true;

		} else {
			if (this.gererCollisionCameraAvecObjetMobile()) {
				scc.deplacerPositionCamera(-dx, -dy, -dz);
				return true;
			}

		}
		return false;

	}

	public boolean sautCamera = false;
	public boolean sautMonde = false;

	public boolean gererCollisionCameraAvecObjetMobile() throws SautCamera {
		ObjetMobile zoneDetection = espace
				.objetMobileCollisionAvecCamera(false);
		if (zoneDetection != null) {
			// System.out.println(" obj="+omColision.model.nomObjet);

			if (zoneDetection.evenement() != null && mondeInterface.active) {
				sautCamera = false;
				zoneDetection.evenement()
						.collisionCamera(zoneDetection, joueur);
				if (sautCamera) {
					sautCamera = false;
					throw new SautCamera();
				}
				if (sautMonde) {
					sautMonde = false;
					throw new SautMonde();
				}
			}

		}
		omColision = espace.objetMobileCollisionAvecCamera(true);
		if (omColision != null) {
			/*
			 * if (omColision.model.modelClasse.nom.equals("objet.act")) {
			 * Log.print(" obj=" , omColision.model.nomObjet); }
			 */

			if (mondeInterface.active) {
				sautCamera = false;
				omColision.evenement().collisionCamera(omColision, joueur);
				if (sautCamera) {
					sautCamera = false;
					throw new SautCamera();
				}
				if (sautMonde) {
					sautMonde = false;
					throw new SautMonde();
				}
			}
			return !omColision.annulerCollisionCamera;

		}
		return false;

	}

	public Key keyFlecheBas = new Key(Keyboard.KEY_DOWN);
	public Key keyFlecheHaut = new Key(Keyboard.KEY_UP);
	public Key keyB = new Key(Keyboard.KEY_B);
	public Key keyC = new Key(Keyboard.KEY_C);
	public Key keyN = new Key(Keyboard.KEY_N);
	public Key keyV = new Key(Keyboard.KEY_V);
	public Key keyA = new Key(Keyboard.KEY_A);
	public Key keyS = new Key(Keyboard.KEY_S);
	public Key keyW = new Key(Keyboard.KEY_W);
	public Key keyX = new Key(Keyboard.KEY_X);

	public Key keyPlus = new Key(Keyboard.KEY_ADD);
	public Key keyMoins = new Key(Keyboard.KEY_SUBTRACT);
	public Key keyReturn = new Key(Keyboard.KEY_RETURN);
	public Key keyRShift = new Key(Keyboard.KEY_RSHIFT);
	public Key keyLShift = new Key(Keyboard.KEY_LSHIFT);
	public Key keyRControl = new Key(Keyboard.KEY_RCONTROL);
	public Key key0 = new Key(Keyboard.KEY_0);
	public Key keySpace = new Key(Keyboard.KEY_SPACE);
	public Key keyEscape = new Key(Keyboard.KEY_ESCAPE);
	public Button b = new Button(2);

	MenuControlleur menuBox;
	MenuControlleur menuLigne;

	public MenuControlleur menuColler;

	public MenuControlleur menuCamera;

	public MenuControlleur menuCouleur;
	public MenuControlleur menuTexture;

	public MenuControlleur menuTrajectoire;

	public MenuControlleur menuDomage;
	public MenuControlleur menuAxeX;
	public MenuControlleur menuAxeY;
	public MenuControlleur menuAxeZ;
	public MenuControlleur menuFormeCube;
	public MenuControlleur menuFormeSphere;

	public static void log(String... strings) {
		for (String s : strings) {
			System.out.print(s);

		}
		System.out.println();
	}

	public int cameraSurX() {
		Vector3f dir = game.getCamera().getDirection();
		if (Math.abs(dir.x) >= Math.abs(dir.y)
				&& Math.abs(dir.x) >= Math.abs(dir.z)) {
			if (dir.x > 0) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;

	}

	public boolean cameraInverse() {
		int r;
		if ((r = cameraSurX()) != 0) {
			return r < 0;
		}
		if ((r = cameraSurY()) != 0) {
			return r < 0;
		}
		if ((r = cameraSurZ()) != 0) {
			return r > 0;
		}
		return false;
	}

	public int cameraSurY() {
		Vector3f dir = game.getCamera().getDirection();
		if (Math.abs(dir.y) >= Math.abs(dir.x)
				&& Math.abs(dir.y) >= Math.abs(dir.z)) {
			if (dir.y > 0) {
				return 1;

			} else {
				return -1;
			}

		}
		return 0;

	}

	public int cameraSurZ() {
		Vector3f dir = game.getCamera().getDirection();
		if (Math.abs(dir.z) >= Math.abs(dir.x)
				&& Math.abs(dir.z) >= Math.abs(dir.y)) {
			if (dir.z > 0) {
				return 1;
			} else {
				return -1;
			}

		}
		return 0;

	}

	public boolean verouillerRotationEtTranslationCamera = false;

	public void deselect() {

		menuColler.selected = false;

		menuTexture.selected = false;

		// menuLigne.selected = false;
	}

	public void activerBrouillard() {
		glEnable(GL_FOG);
		FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
		fogColor.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();

		// fogMode = GL_EXP;
		glFogi(GL_FOG_MODE, GL_EXP);
		glFog(GL_FOG_COLOR, fogColor);
		glFogf(GL_FOG_DENSITY, 0.020f);
		glHint(GL_FOG_HINT, GL_DONT_CARE);
		glFogf(GL_FOG_START, 50);
		glFogf(GL_FOG_END, 60);

	}

	public void modifierShadowMap(ShadowData sd) throws InterruptedException,
			Exception {
		if (Game.rm == Game.RenderMode.Normal) {
			return;
		}
		if (FBOShadowMap.shadowMap == null) {

			FBOShadowMap.shadowMap = new FBOShadowMap();
			FBOShadowMap.shadowMap.init(Game.shadowMapWidth,
					Game.shadowMapHeight, game);
			Game.modifierOmbre = 0;
		}
		if (Game.modifierOmbre > 1) {
			return;
		}
		FBOShadowMap sm = FBOShadowMap.shadowMap;
		if (sd.direction == null) {
			return;
		}

		// Log.print("Ombre... " + Game.modifierOmbre);
		FBOShadowMap.shadowMap.activer(sd);

		Game.rm = Game.RenderMode.Depth;
		decor.dessiner(sm.cam);

		sm.desactiver();

		Game.rm = Game.RenderMode.Shadow;
		Game.modifierOmbre++;

		game.getCamera().update();
		game.getCamera().apply();

	}

	public void creerMenuControlleur(List<MenuControlleur> list) {

		list.add(menuAxeX = new MenuControlleur("Axe x", 4) {

			@Override
			public void activer() {
				selection.axe = Selection.Axe.X;
				menuAxeX.selected = !menuAxeX.selected;
				menuAxeY.selected = false;
				menuAxeZ.selected = false;
			}

		});
		list.add(menuAxeY = new MenuControlleur("Axe y", 4) {

			@Override
			public void activer() {
				selection.axe = Selection.Axe.Y;
				menuAxeX.selected = false;
				menuAxeY.selected = !menuAxeY.selected;
				menuAxeZ.selected = false;
			}

		});
		list.add(menuAxeZ = new MenuControlleur("Axe z", 4) {

			@Override
			public void activer() {
				selection.axe = Selection.Axe.Z;
				menuAxeX.selected = false;
				menuAxeY.selected = false;
				menuAxeZ.selected = !menuAxeZ.selected;
			}

		});

		/*
		 * list.add(menuCopie = new MenuControlleur("copie", 1) {
		 * 
		 * @Override public void activer() { DialogDemandeNomClasse.old = etat;
		 * etat = DialogDemandeNomClasse; processMenuKey = false;
		 * DialogDemandeNomClasse.fermerAction = false;
		 * 
		 * deselect(); selected = true; }
		 * 
		 * });
		 */
		list.add(menuColler = new MenuControlleur("coller", 0) {

			@Override
			public void activer() {
				if (zg != null && zg.mc != null) {

					initModelClasseZoneGreffon();
					zg.mc = null;
					return;
				}
				BrickEditor.this.donnerChoixModelClasse();
				etat = choixModelClasse;
				etat.BrickEditor = BrickEditor.this;
				processMenuKey = false;

				/*
				 * selection.BoxSelectionAction = selection.BoxCopie;
				 * selection.setBoxSelection(true);
				 */
				deselect();
				selected = true;
			}

		});

		list.add(new MenuControlleur("sol", 2) {

			@Override
			public void activer() {
				scc.auSol = !scc.auSol;
				selected = scc.auSol;

			}

		});
		list.add(menuTexture = new MenuControlleur("texture", 0) {
			EtatBrickEditor old;

			@Override
			public void activer() {
				deselect();
				if (decor.DecorDeBriqueData.nomHabillage != null) {
					if (etat.getClass() == Selection.class
							|| etat.getClass() == EtatCanon.class) {
						selected = true;
						processMenuKey = false;
						actions.add(() -> {
							old = etat;
							try {

								ChoixTexture ct = BrickEditor.this
										.donnerHabillage(
												decor.DecorDeBriqueData.nomHabillage)
										.donnerChoixTexture(game, true);
								ct.initialiser(BrickEditor.this.cameraInverse());
								etat = ct;
								etat.BrickEditor = BrickEditor.this;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						});
						return;
					}
				}
			}
		});

		list.add(menuCouleur = new MenuControlleur("Kube", 1) {
			EtatBrickEditor old;

			@Override
			public void activer() {
				deselect();
				if (decor.DecorDeBriqueData.nomHabillage != null) {
					if (etat.getClass() == Selection.class
							|| etat.getClass() == EtatCanon.class) {
						selected = true;
						processMenuKey = false;
						old = etat;
						actions.add(() -> {

							try {
								FBO fbo = mondeInterface.donnerFBO(128, 128);
								etat = BrickEditor.this.donnerHabillage(
										decor.DecorDeBriqueData.nomHabillage)
										.donnerChoixKube(game, fbo, false);
								etat.BrickEditor = BrickEditor.this;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						});
						return;
					}

					return;
				}

			}

		});

		/*
		 * list.add(new MenuControlleur("couleur", 2) { EtatBrickEditor old;
		 * 
		 * @Override public void activer() { if (etat.getClass() ==
		 * Selection.class || etat.getClass() == EtatCanon.class) { old = etat;
		 * etat = rgbColorSelector;
		 * 
		 * selected = true; return; } if (etat.getClass() ==
		 * RGBColorSelector.class) { etat = old; selected = false;
		 * 
		 * }
		 * 
		 * }
		 * 
		 * });
		 */
		list.add(new MenuControlleur("canon", 2) {

			@Override
			public void activer() {
				if (etat.getClass() == Selection.class) {
					etat = EtatCanon;
					selected = true;
					return;
				}
				if (etat.getClass() == EtatCanon.class) {
					etat = selection;
					selected = false;

				}

			}

		});

		list.add(menuTrajectoire = new MenuControlleur("trajectoire", 3) {

			@Override
			public void activer() {
				editeurOM.setModel();
				etat = editeurOM;

			}

		});

		checkListMenuController(list);
		/*
		 * list.add(new MenuControlleur("sphere", 2) {
		 * 
		 * @Override public void activer() { afficherSphere = !afficherSphere;
		 * selected = afficherSphere;
		 * 
		 * }
		 * 
		 * });
		 */

	}

	public void checkListMenuController(List<MenuControlleur> list) {
		Map<Character, MenuControlleur> map = new HashMap<Character, MenuControlleur>();
		for (MenuControlleur mc : list) {
			MenuControlleur tmp = map.get(mc.key);
			if (tmp == null) {
				map.put(mc.key, mc);

			} else {
				throw new Error(" doublons " + mc.nom + " avec " + tmp.nom);
			}

		}

	}

	public MondeInterfacePrive mondeInterface;

	public boolean processMenuKey = true;

	public VBOTexture2D creerParticule(Shader shader) {
		VBOTexture2D vbo = new VBOTexture2D(shader);

		vbo.addCoordTexture2D(0.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 0.0f);
		vbo.addCoordTexture2D(0.0f, 0.0f);

		vbo.addVertex(-0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, 0.5f, 0.0f);
		vbo.addVertex(-0.5f, 0.5f, 0.0f);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		return vbo;

	}

	public Widget tailleObus;
	public static int elementTaille = 16;
	public static int niveau = 4;
	public static int niveauCollision = 10;
	public Info info;
	public String modelName;
	public String modelType;
	public float echelle = 1.0f;
	public ObjetMobile objetCourant;
	public BrickEditorSwing swingEditor;

	public void initCurrentModelName(ObjetMobile om) {

		if (verouillerRotationEtTranslationCamera) {
			return;
		}
		objetCourant = om;
		menuTrajectoire.setEnabled(objetCourant != null);
		if (objetCourant == null) {
			modelName = "";
		} else {
			if (objetCourant.model == null) {
				modelType = om.mc.nomModele();
				modelName = objetCourant.nom;
				echelle = objetCourant.echelle;
			} else {
				modelType = om.mc.nomModele();

				modelName = objetCourant.model.nomObjet;
				echelle = objetCourant.echelle;
			}
		}

	}

	ObjetMobile omColision;
	public EffetEcran rond;
	public Lumiere lumiereSelection;
	public Lumiere lumiereModification;

	public void afficherDecor() throws InterruptedException, Exception {

		debut = System.currentTimeMillis();
		if (chargeur.decor != null) {
			return;
		}
		decor.action.numTriAvantTest = config.config.numTriAvantTest;

		Game.fogDensity = config.config.brouillard;

		chargerModelView();
		decor.dessiner(cam);

		if (this.selection.placerLumiere
				&& decor.DecorDeBriqueData.lumieres != null) {
			if (lumiereModification == null) {

				Lumiere tmp = this.lumiereProche();

				lumiereSelection = tmp;
				for (Lumiere l : decor.DecorDeBriqueData.lumieres) {
					if (l == lumiereSelection) {
						l.color().set(1, 0, 0);
						l.dessiner(cam);
					} else {
						l.color().set(0, 1, 0);
						l.dessiner(cam);
					}

				}
			} else {
				lumiereModification.color().set(0, 0, 1);
				lumiereModification.dessiner(cam);
			}
		}
		// Game.etatOmbre.dessinerSoleil(cam);

		// this.decor.DecorDeBriqueData.dessinerGroupe(this,
		// arbreCameraPositions.nomGroupeEnCours, cam);

	}

	static public Vector3f tmpVect = new Vector3f();

	public Lumiere lumiereProche() {
		float dist = 9999999.0f;
		Lumiere r = null;
		if (decor.DecorDeBriqueData.lumieres != null) {
			for (Lumiere l : decor.DecorDeBriqueData.lumieres) {
				tmpVect.set(cam.getLocation());
				tmpVect.subtractLocal(l.pos);
				float tmp = tmpVect.lengthSquared();
				if (tmp < dist) {
					dist = tmp;
					r = l;
				}
			}
			return r;
		} else {
			return null;

		}

	}

	public boolean partition = true;

	public boolean pause = false;

	public void chargerMonde(String nomMonde) throws FileNotFoundException,
			ClassNotFoundException, IOException {
		if (this.decor != null && this.decor.DecorDeBriqueData != null) {
			Log.print(" Delete Decor");
			this.decor = null;
			// this.espace = null;
			FBOShadowMap.shadowMap = null;
		}
		DecorDeBriqueDataElement data = DecorDeBriqueDataElement
				.charger(cheminRessources + "/" + nomMonde);
		if (espace != null) {
			espace.reset();
			espace.vider();
		}
		mondeInterface.listMutator.clear();
		try {

			if (arbreCameraPositions != null) {
				arbreCameraPositions.creerListePositionCamera(null);
			}
			if (chargeur != null) {
				chargeur.init(this, data);
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public GrapheDebug grapheDebug;

	public Graphe creerGrapheMultiRacine(String nomGroupe, float speed,
			float rayon, boolean peutVoler, float hauteur) {
		List<String> l = decor.DecorDeBriqueData
				.donnerNomsPourGroupe(nomGroupe);
		List<GrapheSommet> listGS = new ArrayList<>();
		for (String nom : l) {
			GrapheSommet gs = new GrapheSommet();
			gs.nom = nom;
			gs.position = decor.DecorDeBriqueData.donnerCameraPosition(nom).translation;
			listGS.add(gs);
		}
		Log.print("creation graphe constructeur");
		GrapheConstructeur gc = new GrapheConstructeur(this, listGS, rayon,
				peutVoler, hauteur, false);
		Log.print("graphe constructeur calcul");
		return gc.calculer(speed);
	}

	public Graphe creerGrapheMultiRacineSimple(String nomGroupe, float speed,
			float rayon, boolean peutVoler, float hauteur) {
		List<String> l = decor.DecorDeBriqueData
				.donnerNomsPourGroupe(nomGroupe);
		List<GrapheSommet> listGS = new ArrayList<>();
		for (String nom : l) {
			GrapheSommet gs = new GrapheSommet();
			gs.nom = nom;
			gs.position = decor.DecorDeBriqueData.donnerCameraPosition(nom).translation;
			listGS.add(gs);
		}

		GrapheConstructeur gc = new GrapheConstructeur(this, listGS, rayon,
				peutVoler, hauteur, true);

		return gc.calculerGrapheSimple(speed);
	}

	public Soleil soleil = new Soleil();
	public Color couleurCourante;

	public void modifierOmbre() throws InterruptedException, Exception {

		if (Game.rm == Game.RenderMode.Normal) {
			return;
		}
		if (chargeur.decor != null) {
			return;
		}
		if (soleil == null) {
			return;
		}
		soleil.init(this);
		if (decor.DecorDeBriqueData.shadowData == null) {
			decor.DecorDeBriqueData.shadowData = new ShadowData();
			return;
		}

		soleil.init(decor.DecorDeBriqueData.shadowData);
		modifierShadowMap(decor.DecorDeBriqueData.shadowData);
		if (FBOShadowMap.shadowMap == null) {

			return;
		}
		// FBOShadowMap.shadowMap.dessinerSoleil(cam);

	}

	public void debugShadowCam() {
		ShadowData sd = decor.DecorDeBriqueData.shadowData;
		this.cam.getDirection().set(sd.direction);
		this.cam.getLocation().set(sd.location);
		this.cam.getLeft().set(sd.left);
		this.cam.getUp().set(sd.up);
		this.cam.update();
		this.cam.apply();

	}

	public static final FloatBuffer modelViewMatrix = BufferUtils
			.createFloatBuffer(16);
	private Matrix4f modelView = new Matrix4f();

	public void chargerModelView() {

		LWJGLCamera c = (LWJGLCamera) cam;
		c.getModelViewMatrix().fillFloatBuffer(modelViewMatrix);

	}

	boolean afficherIHM = true;

	public void loop(Game game) throws Exception {

		etat.modifier();
		if (this.swingEditor != null && this.decor != null) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					if (swingEditor != null && swingEditor.frame != null) {
						swingEditor.frame.setTitle(decor.DecorDeBriqueData.nom
								+ "(" + Game.fpsGlobal.getResult() + " FPS)");
					}
				}

			});

		}
		synchronized (actions) {
			for (Action action : actions) {
				action.execute();

			}
			actions.clear();
		}
		if (mondeInterface.active) {

			game.clear();

			if (!pause) {

				mondeInterface.loop();

				Game.etatOmbre.modifierModeRendu(this);

				if (mondeInterface.choixGenerique != null) {
					mondeInterface.choixGenerique.modifier();
				}
				for (CreerIcone ci : mondeInterface.creerIcones) {
					ci.creer(mondeInterface);
				}
				mondeInterface.creerIcones.clear();
				if (mondeInterface.tableau != null) {
					mondeInterface.tableau.modifierTexture();// pour le
																// rendu
																// des
																// ElementTableVue3D
					this.mondeInterface.brickEditor.scc.updateCamera();
				}

				if (fBORenduLumiereDiffere != null) {

					fBORenduLumiereDiffere.activer();
					fBORenduLumiereDiffere.initialiser(game.getCamera());
				}

				this.mondeInterface.brickEditor.scc.updateCamera();

				mondeInterface.dessiner();

				if (fBORenduLumiereDiffere != null) {
					fBORenduLumiereDiffere.desactiver();
				}
				// this.modifierOmbre();

			}

			if (fBORenduLumiereDiffere != null) {

				game.getCamera().update();
				game.getCamera().apply();

				fBORenduLumiereDiffere.dessinerFBO(rond);

			}
			if (chargeur.decor != null) {
				chargeur.dessiner(Game.shaderWidget);
			}

			return;

		}
		synchronized (lock) {
			if (resetEspace) {
				espace.reset();

				this.mondeInterface.joueur.emeteurs.stop();

				resetEspace = false;

			}
		}

		if (nbImage == 0.0f) {

			nbImage = nbImageMax;
			duree = 0;
		}

		boolean menuMouseButton = false;
		if (!etat.estSousEcran()) {
			if (processMenuKey) {
				menu.processKeyboard();
				menuMouseButton = menu.processMouseButton();
			}
		}

		game.clear();
		Game.etatOmbre.modifierModeRendu(this);
		if (fBORenduLumiereDiffere != null) {

			fBORenduLumiereDiffere.activer();

		}
		if (decor != null) {

			if (chargeur.decor == null) {
				try {
					if (!menuMouseButton) {
						etat.gerer();
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			if (Soleil.debug) {
				this.debugShadowCam();
			}

			fBORenduLumiereDiffere.initialiser(game.getCamera());

			game.getCamera().update();
			game.getCamera().apply();

			this.afficherDecor();
			if (this.grapheDebug != null) {
				this.grapheDebug.dessiner();
			}
			etat.dessiner(cam);
			if (this.keyEscape.isPressed()) {
				afficherIHM = !afficherIHM;
			}
			canon.dessiner(cam);
			if (!etat.estSousEcran()) {
				if (afficherIHM) {

					menu.ihm.dessiner(game.shaderWidget);
				}
			}
			if (afficherIHM) {
				info.dessiner();
			}

			if (scc.boxCam.debugBox != null) {
				Shader shader = shaderBox;
				shader.use();

				// selection.shader.glUniformfARB("size", 1);
				shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);
				shader.glUniformfARB("echelle", 1.0f);
				scc.boxCam.objDebugBox.dessiner(cam);
			}
		}

		// System.out.println(" tmpDuree="+tmpDuree);

		// shader.glUniformfARB("texture", 0);

		fps = game.fps.getResult();
		// Display.setTitle("" + fps);
		if (fBORenduLumiereDiffere != null) {
			fBORenduLumiereDiffere.desactiver();

			fBORenduLumiereDiffere.dessinerFBO(null);

		}
		if (chargeur.decor != null) {
			chargeur.charger(100);
			if (chargeur.decor == null) {
				this.processMenuKey = true;
				// Log.print("processMenuKey=true");
			}
			chargeur.dessiner(game.shaderWidget);

		}

	}

	public boolean contientDecor() {
		if (decor == null) {
			return false;
		}
		if (decor.action == null) {
			return false;
		}
		return true;
	}

	float nbImage = 0;
	float nbImageMax = 15;
	long debut = 0;
	public Camera cam;
	public String fileName = "c:\\tmp\\world.bin";
	long duree = 0;
	String listColorsFileName = "c:\\tmp\\colors.bin";
	List<Color> colors = new ArrayList<Color>();
	public Canon canon;
	public CallbackCanonDestructionOuConstruction callback;
	static public String cheminRessources;
	public DecorDeBriqueData decorDeBriqueData;

	public void chargerDecorDeBriqueData() throws FileNotFoundException,
			ClassNotFoundException, IOException {
		File file = new File(cheminRessources + "/base.bin");

		if (file.exists()) {
			decorDeBriqueData = (DecorDeBriqueData) SerializeTool
					.load(cheminRessources + "/base.bin");

		} else {

			decorDeBriqueData = new DecorDeBriqueData();
			decorDeBriqueData.sauvegarder(cheminRessources + "/base.bin");
		}
		if (Main.main != null) {
			Main.main.initialiser(decorDeBriqueData);
		}

	}

	public void sauvergarder() throws IOException {

		decorDeBriqueData.sauvegarder(cheminRessources + "/base.bin");
		SelectionMonde.sauvegarder();

	}

	public void chargerRessource() throws FileNotFoundException,
			ClassNotFoundException, IOException {

		this.chargerDecorDeBriqueData();

		List<String> ls = DecorDeBriqueDataElement.lesMondes(cheminRessources);
		if (ls.size() == 0) {
			String nomFichier = cheminRessources + "/defaut.wld";
			this.decorDeBriqueData.mondeCourant = "/defaut.wld";
			DecorDeBriqueDataElement.creer(BrickEditor.niveau,
					BrickEditor.elementTaille, nomFichier);

		}
		if (this.decorDeBriqueData.mondeCourant == null) {
			this.decorDeBriqueData.mondeCourant = ls.get(0);
		}

	}

	public String cheminRessources() {
		return cheminRessources;
	}

	Object lock = new Object();
	private boolean resetEspace = false;

	public void resetEspace() {
		synchronized (lock) {
			resetEspace = true;
		}

	}

	public VoxelTexture3D texEspace;

	public void initMenuSelection() {

		selection.BoxSelectionAction = null;
		selection.setBoxSelection(false);
		this.deselect();

	}

	public Shader shaderBox;
	public ChargeurDecor chargeur;

	public void afficherMessage(String msg) {
		message.afficherMessage(msg);
	}

	FBORenduLumiereDiffere fBORenduLumiereDiffere;

	public void creerChoixModelClasse(String nom, List<String> noms) {
		ChoixModelClasse nvChoixModelClasse = this.choixModelClasses.get(nom);

		try {
			nvChoixModelClasse = new ChoixModelClasse(this,
					this.mondeInterface.donnerFBO(128, 128), noms);
			this.choixModelClasses.put(nom, nvChoixModelClasse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void creerChoixModelClasse(String nom, List<String> noms, int dim,
			int dx, int dy, float distCoeff) {
		ChoixModelClasse nvChoixModelClasse = this.choixModelClasses.get(nom);

		try {
			nvChoixModelClasse = new ChoixModelClasse(this,
					this.mondeInterface.donnerFBO(dim, dim), noms, dx, dy,
					distCoeff);

			this.choixModelClasses.put(nom, nvChoixModelClasse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void donnerChoixModelClasse(String nom) {

		this.choixModelClasse = this.choixModelClasses.get(nom);

	}

	public void donnerChoixModelClasse() {
		String nm = decorDeBriqueData.mondeCourant;
		this.choixModelClasse = this.choixModelClasses.get(nm);
		if (this.choixModelClasse == null) {
			try {
				this.choixModelClasse = new ChoixModelClasse(this,
						this.mondeInterface.donnerFBO(128, 128),
						decor.DecorDeBriqueData.nomHabillage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.choixModelClasses.put(nm, this.choixModelClasse);
		}

	}

	public void supprimerChoixModelClasse() {
		String nm = decorDeBriqueData.mondeCourant;
		ChoixModelClasse cmc = this.choixModelClasses.get(nm);

		this.choixModelClasses.put(nm, null);

	}

	public boolean modeEditeur = false;

	public void modifierTailleObus() {
		setText(tailleObus, "" + canon.tailleObus);
	}

	public void init(Game game) throws Exception {
		// System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords",
		// "true");
		// Mouse.setClipMouseCoordinatesToWindow(false);
		parallele = new Parallele<>();
		parallele.start(4);
		if (VoxelShaderColor.renduLumiereDiffere) {
			fBORenduLumiereDiffere = new FBORenduLumiereDiffere();
			fBORenduLumiereDiffere.game = game;
			fBORenduLumiereDiffere.init(game.getWidth(), game.getHeight());
			Game.screenPixelCount = game.getWidth() * game.getHeight();
			// fBORenduLumiereDiffere.init(1024, 1024);

		}
		this.activerBrouillard();
		this.game = game;
		Game.fps = new Fps();
		Game.fpsGlobal = new Fps();
		shaderBox = new Shader(Shader.class, "base.frag", "base.vert", null);
		info = new Info(this);
		IHM ihm = game.nouvelleIHM();
		ihm.setSize(200, 45);
		ihm.beginX();
		List<MenuControlleur> list = new ArrayList<MenuControlleur>();
		this.creerMenuControlleur(list);

		menu = new Menu(ihm, 128, 32, list, false);

		DialogDemandeNomClasse = new dadou.tools.construction.DialogDemandeNomClasse(
				game);
		DialogDemandeNomClasse.BrickEditor = this;
		DialogDemandeNomClone = new DialogDemandeNomClone(game);
		DialogDemandeNomClone.BrickEditor = this;

		ihm.beginY();
		ihm.setSize(256, 32);
		// Widget fps = ihm.widget();
		tailleObus = ihm.widget();
		ihm.ajouterModel(() -> {
			modifierTailleObus();
		});

		setText(tailleObus, "0");

		canon = new Canon(this, 15, 0.5f, 200);
		callback = new CallbackCanonDestructionOuConstruction(canon, this);
		canon.CallbackCanon = callback;
		EtatCanon = new dadou.tools.canon.EtatCanon();
		EtatCanon.canon = canon;
		EtatCanon.BrickEditor = this;

		// shaderSphere.use();
		// shaderSphere.glUniformfARB("color", 1.0f, 1.0f, 0.0f, 0.0f);
		ihm.space(600);
		ihm.end();
		ihm.space(400);
		ihm.end();
		fbo = new FBO();
		fbo.init(this.game.getWidth(), this.game.getHeight());

		SelectionMonde = new SelectionMonde(this);

		gestionIcone = new GestionIcone(this);

		editeurOM = new EditeurOM(this);

		this.decorDeBriqueData.initBuffer(this);

		cam = game.getCamera();
		scc = new ExplorerAuSolCameraControllerBis(this, cam);
		mondeInterface = new MondeInterfacePrive(this);
		
		mondeInterface.creerMondeInterfacePublic();
		mondeInterface.chargerSauvegarde();
		if (mondeInterface.joueur.aj == null) {
			mondeInterface.creerJoueur();
			mondeInterface.joueur.aj.i = mondeInterface.mondeInterface;
			mondeInterface.joueur.aj.initialiser();
		}

		espace = new Espace(this, BrickEditor.niveau, BrickEditor.elementTaille);
		// decor = new DecorDeBrique(this, SelectionMonde.charger());

		this.chargeur = new ChargeurDecor();
		String nomMonde = mondeInterface.joueur.aj.nomMonde;
		if (nomMonde != null) {
			SelectionMonde.nom = nomMonde;
			this.mondeInterface.nomMonde = nomMonde;
		} else {
			mondeInterface.joueur.aj.nomMonde = SelectionMonde.nom;
			this.mondeInterface.nomMonde = SelectionMonde.nom;
		}
		if (this.mondeInterface.sauvegardeMonde != null) {
			this.chargeur
					.init(this,
							DecorDeBriqueDataElement
									.charger(BrickEditor.cheminRessources
											+ "/"
											+ this.mondeInterface.sauvegardeJoueur.mondeEnCours));
		} else {
			this.chargeur.init(this, SelectionMonde.charger());
		}
		chargeur.creerIHM();
		decor = null;

		selectionModel = new SelectionModelClasse(this);
		this.arbreModelClasse = new ArbreModelClasse(this);
		this.arbreCameraPositions = new ArbreCameraPositions(this);
		selectionGroupeModel = new SelectionGroupeModelClasse(this);
		message = new Message(this);

		initSelection();

		etat = selection;

		cam.update();
		cam.apply();

		// brique.initShader = false;
		// brique.initShader();
		scc.screenWidth = game.getWidth();
		scc.screenHeight = game.getHeight();
		scc.speed = 0.5f;
		deceleration = new Deceleration();
		deceleration.vitesse = scc.speed;

		if ((new File(listColorsFileName)).exists()) {
			colors = (List<Color>) SerializeTool.load(listColorsFileName);

		}

		Display.setVSyncEnabled(true);
		delay = System.currentTimeMillis();
	}

	public void initMondeInterfacePublic() throws FileNotFoundException,
			ClassNotFoundException, IOException {

		mondeInterface.active = true;
		mondeInterface.gestionTraitementParalle = new GestionTraitementParallele();
		mondeInterface.gestionTraitementParalle.demarer();

	}

	static public void startHeadless(AbstractJoueur j)
			throws FileNotFoundException, ClassNotFoundException, IOException,
			CouleurErreur {
		BrickEditor be = new BrickEditor();
		
		j.initialiser();
		be.chargerRessource();

		be.mondeInterface = new MondeInterfacePrive(be);
		be.mondeInterface.joueur.aj = j;
		be.initMondeInterfacePublic();
		be.mondeInterface.creerMondeInterfacePublic();

		be.espace = new Espace(be, BrickEditor.niveau,
				BrickEditor.elementTaille);

		be.chargeur = new ChargeurDecor();

		be.chargeur.init(
				be,
				DecorDeBriqueDataElement.charger(BrickEditor.cheminRessources
						+ "/" + j.nomMonde));

		while (!be.mondeInterface.exit) {
			if (be.chargeur.decor != null) {
			//	Log.print(" chargement decor ... ");
				be.chargeur.charger(100);
				if (be.chargeur.decor == null)  {
					be.demarerMonde();
				}
			} else {
				//Log.print("  loop ... ");
				be.mondeInterface.loop();
			}
		}

	}

	public void start() throws Exception {
		game = new Game();

		this.init(game);
		this.initMondeInterfacePublic();
		while (!game.isClosed()) {
			try {
				game.clear();
				Game.fpsGlobal.calculer(() -> {

					this.loop(game);

				});
			} catch (OpenGLException e) {
				e.printStackTrace();
				System.exit(0);

			}
			Display.update();
			Display.sync(30);

		}

	}

	public float fps;

	public void setText(Widget w, String txt) {
		Graphics2D g = w.getGraphics2DForUpdate();
		g.setColor(Color.GREEN);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, w.width - 2 * d, w.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(txt, d, w.height - d);
		// g.dispose();
		w.update();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		scc.screenWidth = width;
		scc.screenHeight = height;
		if (fBORenduLumiereDiffere != null) {
			fBORenduLumiereDiffere.init(width, height);
			Game.screenPixelCount = width * height;
			// fBORenduLumiereDiffere.init(1024, 1024);
		}

	}

	@Override
	public void terminate(Game game) {
		// TODO Auto-generated method stub

	}

}
