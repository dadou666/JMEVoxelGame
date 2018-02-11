package dadou;

import java.awt.Color;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingSphere;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.collision.GestionCollision;
import dadou.event.GameEventLeaf;
import dadou.exploration.DirectionExploration;
import dadou.exploration.GestionExploration;
import dadou.exploration.GestionGrapheExploration;
import dadou.exploration.GrapheExploration;
import dadou.exploration.OctreeExploration;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheConstructeur;
import dadou.graphe.GrapheDebug;
import dadou.graphe.GrapheElement;
import dadou.graphe.GrapheReduction;
import dadou.graphe.GrapheSommet;
import dadou.greffon.Boite;
import dadou.greffon.CylindreX;
import dadou.greffon.CylindreY;
import dadou.greffon.CylindreZ;
import dadou.greffon.GreffonForme;
import dadou.greffon.Montagne;
import dadou.greffon.Sphere;
import dadou.greffon.TunelX;
import dadou.greffon.TunelY;
import dadou.greffon.TunelZ;
import dadou.ihm.Action;
import dadou.ihm.Widget;
import dadou.jeux.DeplacementObjet;
import dadou.jeux.Trajectoire;
import dadou.jeux.TrajectoireContext;
import dadou.menu.MenuJeux;
import dadou.mutator.Animation;
import dadou.mutator.AnimationEtape;
import dadou.mutator.Decomposer;
import dadou.mutator.DecomposerRecomposer;
import dadou.mutator.Echelle;
import dadou.mutator.Exploser;
import dadou.mutator.Mouvement;
import dadou.mutator.Mutator;
import dadou.mutator.Orientation;
import dadou.mutator.ParcourCameraPositions;
import dadou.mutator.ParcourGraphe;
import dadou.mutator.Rotation;
import dadou.mutator.RotationAvecLimite;
import dadou.parallele.AddException;
import dadou.parallele.BeginException;
import dadou.parallele.EndException;
import dadou.parallele.Parallele;
import dadou.parallele.StartException;
import dadou.parallele.Traitement;
import dadou.physique.PhysiqueMonde;
import dadou.physique.PhysiqueMutator;
import dadou.son.Emeteurs;
import dadou.son.Son;
import dadou.texture.annotation;
import dadou.texture.photo;
import dadou.texture.photoHabillage;
import dadou.tools.BrickEditor;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;
import dadou.tools.ServiceInterfaceClient;
import dadou.tools.canon.CibleAffichageDistance;
import dadou.tools.canon.GestionAnimationSprite;
import dadou.tools.canon.ParamAnimationSprite;
import dadou.tools.canon.ParamExplosionSphere;
import dadou.tools.construction.AfficherBoxSelection;
import dadou.tools.construction.Selection;
import dadou.tools.texture.ChoixKube;

/**
 * @author BESNARD DAVID
 *
 */
public class MondeInterfacePublic {
	public Object jeux;
	private MondeInterfacePrive mondeInterfacePrive;

	public MondeInterfacePublic(MondeInterfacePrive mondeInterfacePrive) {
		this.mondeInterfacePrive = mondeInterfacePrive;

	}

	public CameraPosition donnerCameraPosition() {
		return this.mondeInterfacePrive.brickEditor.scc.creerCameraPosition();

	}

	public void exit() {
		this.mondeInterfacePrive.exit = true;
	}

	public Vector3f donnerDirectionCamera() {
		return this.mondeInterfacePrive.brickEditor.scc.cam.getDirection();
	}

	public void annulerChute() {
		this.mondeInterfacePrive.brickEditor.hauteurChute = 0.0f;
	}

	public void initGestionAnimationSprite(String nomHabillage) {
		this.mondeInterfacePrive.gestionAnimationSprite = new GestionAnimationSprite(
				this.mondeInterfacePrive.brickEditor
						.donnerHabillage(nomHabillage));
		this.mondeInterfacePrive.joueur(null).canon.gestionExplosion = this.mondeInterfacePrive.gestionAnimationSprite;
	}

	public void initGestionExplosionSphere() {
		this.mondeInterfacePrive.joueur(null).canon.gestionExplosion = mondeInterfacePrive.gestionExplosionSphere;
	}

	public Parallele<Runnable> parallele() {
		return this.mondeInterfacePrive.brickEditor.parallele;
	}

	public void initParamAnimationSprite(String nom, float echelle,
			String args[]) {
		GestionAnimationSprite gas = this.mondeInterfacePrive.gestionAnimationSprite;
		int etapes[] = new int[args.length];
		ParamAnimationSprite pas = gas.donnerParamAnimationSprite(nom);
		for (int i = 0; i < args.length; i++) {
			etapes[i] = gas.h.donnerNomTextures().get(args[i]).idx;
		}
		pas.etapes = etapes;
		pas.echelle = echelle;

	}

	public Graphe creerGrapheMultiRacineSimple(String nomGroupe, float speed,
			float rayon, boolean peutVoler, float hauteur) {
		return this.mondeInterfacePrive.brickEditor
				.creerGrapheMultiRacineSimple(nomGroupe, speed, rayon,
						peutVoler, hauteur);

	}

	public int historiqueIndex() throws IOException {
		return this.mondeInterfacePrive.historiqueIndex();

	}
	public Traitement ajouter(Runnable t) {
		return this.mondeInterfacePrive.gestionTraitementParalle.ajouter(t);
	}
	public void liberer(Traitement t) {
		this.mondeInterfacePrive.gestionTraitementParalle.liberer(t);
	}

	public void choisirKube() {
		String nomHabillage = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.nomHabillage;
		Game g = this.mondeInterfacePrive.brickEditor.game;
		FBO fbo = mondeInterfacePrive.donnerFBO(128, 128);
		try {
			this.mondeInterfacePrive.choixGenerique = this.mondeInterfacePrive.brickEditor
					.donnerHabillage(nomHabillage).donnerChoixKube(g, fbo,
							false);
			this.mondeInterfacePrive.choixGenerique.BrickEditor = this.mondeInterfacePrive.brickEditor;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void creerImageEcran() {
		BrickEditor be = this.mondeInterfacePrive.brickEditor;
		be.fbo.creerImageEcranPourDecor(be, null);

	}

	public void creerChoixModel(String nom, List<String> noms) {
		this.mondeInterfacePrive.brickEditor.creerChoixModelClasse(nom, noms);
	}

	public void creerChoixModel(String nom, List<String> noms, int dim, int dx,
			int dy, float distCoeff) {
		this.mondeInterfacePrive.brickEditor.creerChoixModelClasse(nom, noms,
				dim, dx, dy, distCoeff);
	}

	public void chosirModel(String nom) {
		this.mondeInterfacePrive.brickEditor.donnerChoixModelClasse(nom);
		this.mondeInterfacePrive.choixGenerique = this.mondeInterfacePrive.brickEditor.choixModelClasse;
		this.mondeInterfacePrive.choixGenerique.BrickEditor = this.mondeInterfacePrive.brickEditor;

	}

	public void chosirModel() {
		this.mondeInterfacePrive.brickEditor.donnerChoixModelClasse();
		this.mondeInterfacePrive.choixGenerique = this.mondeInterfacePrive.brickEditor.choixModelClasse;
		this.mondeInterfacePrive.choixGenerique.BrickEditor = this.mondeInterfacePrive.brickEditor;

	}

	public void supprimerChoixModel() {
		this.mondeInterfacePrive.brickEditor.supprimerChoixModelClasse();
	}

	public void initialiserMenuJeux(MenuJeux menuJeux, int largeur,
			int hauteur, int hauteurLigne) {
		this.mondeInterfacePrive.menuJeux = menuJeux;
		menuJeux.initialiser(this, largeur, hauteur, hauteurLigne);
	}

	public void initGreffonBoite() {
		GreffonForme.courrant = new Boite();

	}

	public void initCylindreX() {
		GreffonForme.courrant = new CylindreX();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initCylindreY() {
		GreffonForme.courrant = new CylindreY();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initCylindreZ() {
		GreffonForme.courrant = new CylindreZ();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initTunelX() {
		GreffonForme.courrant = new TunelX();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initTunelY() {
		GreffonForme.courrant = new TunelY();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initTunelZ() {
		GreffonForme.courrant = new TunelZ();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initSphere() {

		GreffonForme.courrant = new Sphere();
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public void initMontagne() {
		this.initMontagne(false);

	}

	public void initMontagne(boolean utiliseKubeSelection) {
		Montagne m = new Montagne();
		m.utiliseKubeSelection = utiliseKubeSelection;
		m.bes = this.mondeInterfacePrive.brickEditor;
		GreffonForme.courrant = m;
		this.mondeInterfacePrive.brickEditor.zg.modifierValeur = false;
		this.mondeInterfacePrive.brickEditor.selection.modifierValeurGreffon = false;

	}

	public boolean choixEnCours() {
		return this.mondeInterfacePrive.choixGenerique != null;
	}

	public void afficherFaceX(String nomSprite3D, boolean v) {
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.get(nomSprite3D).vbo.afficherX = v;
	}

	public void afficherFaceY(String nomSprite3D, boolean v) {
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.get(nomSprite3D).vbo.afficherY = v;

	}

	public void afficherFaceZ(String nomSprite3D, boolean v) {
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.get(nomSprite3D).vbo.afficherZ = v;

	}

	public Color donnerChoixKube() {
		String nomHabillage = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.nomHabillage;

		return this.mondeInterfacePrive.brickEditor
				.couleurPourHabillage(nomHabillage);

	}

	public boolean contientChoixKube() {
		String nomHabillage = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.nomHabillage;

		return this.mondeInterfacePrive.brickEditor
				.contientSelectionKube(nomHabillage);

	}

	public boolean contientChoixModel() {
		return this.mondeInterfacePrive.brickEditor.contientSelectionModel();
	}

	public void supprimerModel(ModelClasse mc) {
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.remove(mc.nom);
	}

	public ModelClasse selectionModel() {
		return this.mondeInterfacePrive.brickEditor.selectionModel();
	}

	public <U extends Serializable> U clone(U obj) {
		ByteArrayInputStream bis;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bis = new ByteArrayInputStream(bos.toByteArray());

			ObjectInputStream ois = new ClassLoaderObjectInputStream(
					this.mondeInterfacePrive.classLoader, bis);

			return (U) ois.readObject();

		} catch (IOException ex) {
			ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public void decomposer(String nomObjet, float distance)
			throws CouleurErreur, Exception {
		ObjetMobilePourModelInstance om = mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (distance == 0.0f) {
			om.dmc = null;
			return;
		}
		if (om.dmc == null) {
			DecompositionModelClasseCtx ctx = new DecompositionModelClasseCtx();
			ctx.dmc = om.mc.decompositionCube(
					this.mondeInterfacePrive.brickEditor, 1);
			om.dmc = ctx;
		}
		om.dmc.distance = distance;

	}

	public Vector3f donnerElementDecorCenterForCam() {
		return this.mondeInterfacePrive.donnerElementDecorCenterForCam();
	}

	public void restaurerModeleInstance(String nomObjet) {
		String nom = "$$" + this.mondeInterfacePrive.idxClone + "$$";
		this.mondeInterfacePrive.idxClone++;
		ModelInstance mi = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.modelInstances
				.get(nomObjet);
		ObjetMobilePourModelInstance nv = mondeInterfacePrive.brickEditor.espace
				.ajouter(mi, nom);
		nv.nom = nom;

		String nomModele = nv.mc.nomModele();
		nv.me = new ModelEvent();

		nv.mei = nv.evenement().creerModeleEventInterface(nomModele,
				this.mondeInterfacePrive);
		if (nv.construction == null) {
			nv.evenement().demarer(nv, null);
		}

	}

	public void restaurer(String nomObjet) {
		ObjetMobilePourModelInstance om = mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om.nom != null) {
			return;
		}

		ModeleEventInterface mei = om.mei;
		ModelEvent me = om.me;
		Emeteurs e = om.emeteurs;
		Map<String, ListeEvtDetection> map = this.mondeInterfacePrive.brickEditor.espace.mapListeEvtDetection;
		ListeEvtDetection o = map.get(nomObjet);

		mondeInterfacePrive.brickEditor.espace.supprimer(om);
		float speedRotate = om.speedRotate;
		float speedTranslate = om.speedTranslate;

		ModelInstance mi = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.modelInstances
				.get(nomObjet);
		ObjetMobilePourModelInstance nv = mondeInterfacePrive.brickEditor.espace
				.ajouter(mi);

		nv.mei = mei;
		nv.me = me;
		nv.speedRotate = speedRotate;
		nv.speedTranslate = speedTranslate;
		nv.emeteurs = e;
		o.objetMobile = nv;
		map.put(nomObjet, o);

	}

	public void chargerMonde(String nomMonde,
			Class<? extends MondeEventInterface> meiClass)
			throws FileNotFoundException, ClassNotFoundException, IOException,
			CouleurErreur {
		try {
			MondeEventInterface mei = null;
			if (meiClass != null) {
				mei = meiClass.newInstance();
			}
			this.chargerMondeAvecInstance(nomMonde, mei);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean chargerMondeAvecInstance(String nomMonde,
			MondeEventInterface mei) throws FileNotFoundException,
			ClassNotFoundException, IOException, CouleurErreur {
		if (this.mondeInterfacePrive.sauvegardeJoueur == null) {
			this.mondeInterfacePrive.sauvegardeJoueur = new SauvegardeJoueur();

		}
		this.mondeInterfacePrive.sauvegardeMonde = this.mondeInterfacePrive.sauvegardeJoueur.mondes
				.get(nomMonde);

		if (this.mondeInterfacePrive.sauvegardeMonde != null) {
			this.mondeInterfacePrive.mei = this.mondeInterfacePrive.sauvegardeMonde.monde;

		} else {
			this.mondeInterfacePrive.sauvegardeMonde = new SauvegardeMonde();
			if (mei == null) {

				this.mondeInterfacePrive.mei = null;
			} else {

				this.mondeInterfacePrive.sauvegardeMonde.monde = mei;
				this.mondeInterfacePrive.mei = mei;

			}
		}

		mondeInterfacePrive.brickEditor.chargerMonde(nomMonde);
		// mondeInterfacePrive.brickEditor.sautMonde = true;
		this.mondeInterfacePrive.nomMonde = nomMonde;
		mondeInterfacePrive.data = new HashMap<>();

		mondeInterfacePrive.joueur.canon.reset();
		return this.mondeInterfacePrive.mei == mei;

	}

	public void estTransparent(String nomObjet, float transparence) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.transparence = transparence;

	}

	public String nomMonde() {
		if (mondeInterfacePrive.nomMonde == null) {
			return this.mondeInterfacePrive.brickEditor.decorDeBriqueData.nomMondeDemarage;
		}
		return this.mondeInterfacePrive.nomMonde;

	}

	public boolean partition() {
		return this.mondeInterfacePrive.brickEditor.partition;
	}

	public void selectionKube(boolean b) {
		if (b) {
			this.mondeInterfacePrive.selection = this.mondeInterfacePrive.brickEditor.selection;
			this.mondeInterfacePrive.selection.BoxSelectionAction = this.mondeInterfacePrive.brickEditor.selection.BoxRemplissage;

			this.mondeInterfacePrive.selection.setBoxSelection(true);
		} else {
			this.mondeInterfacePrive.selection = null;
		}

	}

	public void copier() {
		this.copier(true);

	}

	public String copier(boolean estDecor) {
		int idx = 0;
		String nomModelTmp = "__" + idx;
		BrickEditor be = this.mondeInterfacePrive.brickEditor;
		while (be.decorDeBriqueData.models.get(nomModelTmp) != null) {
			idx++;
			nomModelTmp = "__" + idx;
		}

		be.DialogDemandeNomClasse.nomModel = nomModelTmp;
		GreffonForme.courrant = null;
		if (be.zg.mc != null) {

			be.decorDeBriqueData.ajouter(be.zg.mc, nomModelTmp);
			be.zg.mc = null;
			selectionKube(false);
			forceControlleurCameraStandard();
			return null;
		}
		be.DialogDemandeNomClasse.supprimerEspaces = true;
		be.selection.BoxCopie.estDecor = estDecor;
		be.selection.BoxSelectionAction = be.selection.BoxCopie;
		be.selection.setBoxSelection(true);
		be.etat = be.selection;
		be.processMenuKey = true;
		this.mondeInterfacePrive.selection = this.mondeInterfacePrive.brickEditor.selection;
		return nomModelTmp;

	}

	public void callbackModification(Runnable r) {
		this.mondeInterfacePrive.brickEditor.callbackModification = r;
	}

	public void chargerSauvegarde() {
		this.mondeInterfacePrive.brickEditor.ajouterAction(() -> {
			this.mondeInterfacePrive.chargerSauvegarde();
		});
	}

	public void chargerHistorique() throws IOException {
		this.mondeInterfacePrive.chargerHistorique();

	}

	public List<String> importerPhotos() throws InstantiationException,
			IllegalAccessException {

		Map<String, photo> photos = photo.photos();
		List<String> ls = new ArrayList<>();
		annotation.annotations();
		Parallele<photo> parallele = new Parallele<>();
		try {
			parallele.start(6);
			parallele.begin();
			for (photo p : photos.values()) {
				parallele.add(p);

			}
			parallele.end();

		} catch (StartException | BeginException | AddException | EndException
				| InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (photo p : parallele.list) {
			try {
				photoHabillage mc = p
						.creer(this.mondeInterfacePrive.brickEditor.decorDeBriqueData);

				mc.mc.initBuffer(this.mondeInterfacePrive.brickEditor.game,
						mc.habillage);
				ls.add(mc.nom);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ls;
	}

	public void coler(ModeleEventInterface mei) {
		BrickEditor be = this.mondeInterfacePrive.brickEditor;
		ModelClasse mc = be.choixModelClasse.choix.valeur;
		this.coler(mei, mc);

	}

	public void coler(ModeleEventInterface mei, String nomModel) {
		this.coler(mei, nomModel, 1.0f);

	}

	public void coler(ModeleEventInterface mei, String nomModel, float echelle) {
		BrickEditor be = this.mondeInterfacePrive.brickEditor;
		ModelClasse mc = be.decorDeBriqueData.models.get(nomModel);
		this.coler(mei, mc, echelle);

	}

	public Map<String, String> arguments() {
		return BrickEditor.arguments;
	}

	public void coler(ModeleEventInterface mei, ModelClasse mc, float echelle) {
		BrickEditor be = this.mondeInterfacePrive.brickEditor;

		be.selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0, mc);
		;
		String nomObjet = null;
		do {
			nomObjet = "$" + this.mondeInterfacePrive.idxClone + "$";

			this.mondeInterfacePrive.idxClone++;
		} while (be.espace.mobiles.get(nomObjet) != null);
		be.selection.BoxColler.modelInstance.nomObjet = nomObjet;
		be.selection.BoxColler.mei = mei;
		if (mei != null) {
			mei.i = this;
		}

		mc.echelle = echelle;

		be.selection.BoxSelectionAction = be.selection.BoxColler;
		be.selection.EtatBoxSelection = new AfficherBoxSelection(mc.dx, mc.dy,
				mc.dz, mc.echelle, mc);
		this.mondeInterfacePrive.selection = be.selection;
	}

	public void coler(ModeleEventInterface mei, ModelClasse mc) {
		this.coler(mei, mc, 1.0f);

	}

	public void modifierOmbre() throws InterruptedException, Exception {
		Game.modifierOmbre = 0;
		this.mondeInterfacePrive.brickEditor.modifierOmbre();
	}

	public void ajouterKube(int x, int y, int z, Color color) {
		Kube kube = new Kube();
		kube.x = x;
		kube.y = y;
		kube.z = z;
		kube.color = color;
		this.mondeInterfacePrive.ajouterKubes.add(kube);

	}

	public void ajouterSelectionKube(Color color) {
		Kube kube = new Kube();
		kube.x = this.mondeInterfacePrive.selectionKube.x;
		kube.y = this.mondeInterfacePrive.selectionKube.y;
		kube.z = this.mondeInterfacePrive.selectionKube.z;
		kube.color = color;
		this.mondeInterfacePrive.ajouterKubes.add(kube);

	}

	public void validerKubes() throws CouleurErreur {
		for (Kube k : this.mondeInterfacePrive.ajouterKubes) {
			if (ElementDecor.estVide(k.color)) {
				this.mondeInterfacePrive.brickEditor.decor.removeBrique(k.x,
						k.y, k.z);
			} else {
				this.mondeInterfacePrive.brickEditor.decor.addBrique(k.x, k.y,
						k.z);
			}
			this.mondeInterfacePrive.brickEditor.decor.ecrireCouleur(k.x, k.y,
					k.z, k.color);
		}
		this.mondeInterfacePrive.brickEditor.decor.reconstuire();
		this.mondeInterfacePrive.ajouterKubes.clear();
		this.mondeInterfacePrive.rechargerMonde = true;

	}

	public void ombreInactive(String nomObjet) {
		ObjetMobilePourModelInstance om = mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		om.etatOmbre = EtatOmbre.OmbreInactive;
	}

	public void ombreActive(String nomObjet) {
		ObjetMobilePourModelInstance om = mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		om.etatOmbre = EtatOmbre.OmbreActive;
	}

	public List<String> listNomObjets(String nomClasse) {
		ArrayList<String> list = new ArrayList<String>();

		for (Map.Entry<String, ObjetMobilePourModelInstance> e : this.mondeInterfacePrive.brickEditor.espace.mobiles
				.entrySet()) {
			if (e.getValue().mc.nomModele().equals(nomClasse)) {
				list.add(e.getKey());
			}
		}
		return list;
	}

	public <T extends ModeleEventInterface> void listModeleEvent(Class<T> cls,
			List<T> ls) {
		ArrayList<String> list = new ArrayList<String>();

		for (Map.Entry<String, ObjetMobilePourModelInstance> e : this.mondeInterfacePrive.brickEditor.espace.mobiles
				.entrySet()) {
			if (e.getValue().mei.getClass() == cls) {
				ls.add((T) e.getValue().mei);
			}
		}

	}

	public void activerTremblement() {
		this.mondeInterfacePrive.brickEditor.scc.tremblement = true;
	}

	public void desactiverTremblement() {
		this.mondeInterfacePrive.brickEditor.scc.tremblement = false;
	}

	public List<ObjetMobile> listObjets(String nomClasse) {
		ArrayList<ObjetMobile> list = new ArrayList<>();

		for (Map.Entry<String, ObjetMobilePourModelInstance> e : this.mondeInterfacePrive.brickEditor.espace.mobiles
				.entrySet()) {
			if (e.getValue().mc.nomModele().equals(nomClasse)) {
				list.add(e.getValue());
			}
		}
		return list;
	}

	public void figerCamera(boolean b) {
		this.mondeInterfacePrive.figerCamera(b);

	}

	public void figerCameraTranslation(boolean b) {
		this.mondeInterfacePrive.figerTranslationCamera(b);

	}

	public void initExplosionSphere(String nom, Color color, float distance,
			float vitesse) {
		ParamExplosionSphere pes = this.mondeInterfacePrive.joueur(null).canon.gestionExplosionSphere
				.donnerParamExplosionSphere(nom, color);
		pes.distance = distance;
		pes.vitesse = vitesse;
	}

	public void afficherDonneeHorizontal(int idx, String nomVar, String libelle) {
		this.mondeInterfacePrive.afficherDonneeHorizontal(idx, nomVar, libelle);
	}

	public void afficherDonneeVertical(int idx, String nomVar, String libelle) {
		this.mondeInterfacePrive.afficherDonneeVertical(idx, nomVar, libelle);
	}

	public void modifierBar(int idx, int niveau, int niveauMax) {
		this.mondeInterfacePrive.modifierBar(idx, niveau, niveauMax);
	}

	public void afficherBar(int idx, int limit, String contour, String bar) {
		this.mondeInterfacePrive.afficherBar(idx, limit, contour, bar);
	}

	public void afficherBar(int idx, boolean r) {
		this.mondeInterfacePrive.afficherBar(idx, r);
	}

	public boolean appuyerTouche(int event) {
		return Keyboard.isKeyDown(event);
	}

	public void deceleration(String nomJoueur, float duree) {
		mondeInterfacePrive.brickEditor.deceleration.duree = duree;

	}

	public void couleurSelection(int idx, String couleur) {
		this.mondeInterfacePrive.gameInfoHorizontalEnCours.arrayGameInfo[idx].boxColor = couleur;
		this.mondeInterfacePrive.gameInfoHorizontalEnCours.arrayGameInfo[idx].change = true;
	}

	public ProjectileDef creerProjectileDef() {
		return this.mondeInterfacePrive.creerProjectileDef();
	}

	public boolean initMutator(ObjetMobilePourModelInstance om, Mutator m) {
		return this.mondeInterfacePrive.initMutator(om, m);

	}

	public void afficherGameInfoHorizontal(String nom, boolean b) {
		this.mondeInterfacePrive.mapGameInfoHorizontal.get(nom)
				.afficherGameInfo(b);
	}

	public void afficherGameInfoVertical(String nom, boolean b) {
		this.mondeInterfacePrive.mapGameInfoVertical.get(nom).afficherGameInfo(
				b);
	}

	public void creerAffichageDonneeHorizontal(String nom, int nombreDeDonnees,
			int nombreDeBar) {
		this.mondeInterfacePrive.creerAffichageDonneeHorizontal(nom,
				nombreDeDonnees, nombreDeBar);

	}

	public void creerAffichageDonneeHorizontal(String nom, int nombreDeDonnees,
			int nombreDeBar, int largeurInfo) {
		this.mondeInterfacePrive.creerAffichageDonneeHorizontal(nom,
				nombreDeDonnees, nombreDeBar, largeurInfo);

	}

	public void creerAffichageDonneeVertical(String nom, int nombreDeDonnees,
			int space) {
		this.mondeInterfacePrive.creerAffichageDonneeVertical(nom,
				nombreDeDonnees, space);

	}

	public void activerAffichageDonneeHorizontal(String nom) {
		this.mondeInterfacePrive.activerAffichageDonneeHorizontal(nom);
	}

	public void activerAffichageDonneeVertical(String nom) {
		this.mondeInterfacePrive.activerAffichageDonneeVertical(nom);
	}

	public int nombreDonneePourAffichageHorizontal(String nom) {
		return this.mondeInterfacePrive.mapGameInfoHorizontal.get(nom).arrayGameInfo.length;
	}

	public int nombreDonneePourAffichageVertical(String nom) {
		return this.mondeInterfacePrive.mapGameInfoVertical.get(nom).arrayGameInfo.length;
	}

	public boolean parcourirDepuisLeDebut(String nomObjet,
			String nomGroupeCameraPosition) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.mondeInterfacePrive.brickEditor, obj,
				nomGroupeCameraPosition, true);
		return this.initMutator(obj, pcp);

	}

	public String nomPremierePosition(String nomGroupe) {
		List<String> noms = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerNomsPourGroupe(nomGroupe);
		return noms.get(0);
	}

	public String nomDernierePosition(String nomGroupe) {
		List<String> noms = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerNomsPourGroupe(nomGroupe);
		return noms.get(noms.size() - 1);
	}

	public boolean parcourirDepuisLaFin(String nomObjet,
			String nomGroupeCameraPosition) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.mondeInterfacePrive.brickEditor, obj,
				nomGroupeCameraPosition, false);
		return this.initMutator(obj, pcp);

	}

	public boolean parcourirDepuisLaFin(String nomObjet, List<String> lst) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.mondeInterfacePrive.brickEditor, obj, lst, false);
		return this.initMutator(obj, pcp);

	}

	public boolean parcourirDepuisLeDebut(String nomObjet, List<String> lst) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.mondeInterfacePrive.brickEditor, obj, lst, true);
		return this.initMutator(obj, pcp);

	}

	public void debug(String nomObjet) {

	}

	public String creerEtParcourirDepuisLeDebut(String nomClasse,
			float echelle, String debut, List<String> noms, Object args)
			throws CouleurErreur {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive
				.creerEtParcourirDepuisLeDebut(nomClasse, echelle, debut, noms,
						args);
		if (om == null) {
			return null;
		}
		return om.donnerNom();
	}

	public String creerEtParcourirDepuisLaFin(String nomClasse, float echelle,
			String debut, List<String> noms, Object args) throws CouleurErreur {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive
				.creerEtParcourirDepuisLaFin(nomClasse, echelle, debut, noms,
						args);
		if (om == null) {
			return null;
		}
		return om.donnerNom();
	}

	public boolean rotationAvecLimite(String nomObjet, float nx, float ny,
			float nz, float limite) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		RotationAvecLimite rot;

		rot = new RotationAvecLimite(this.mondeInterfacePrive.brickEditor, obj,
				null, limite, null);
		rot.axe.set(nx, ny, nz);
		rot.axe.normalizeLocal();
		return this.initMutator(obj, rot);

	}

	public boolean rotationAvecLimite(String nomObjet, float nx, float ny,
			float nz, float limite, Vector3f origin) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		RotationAvecLimite rot;

		rot = new RotationAvecLimite(this.mondeInterfacePrive.brickEditor, obj,
				null, limite, origin);
		rot.axe.set(nx, ny, nz);
		rot.axe.normalizeLocal();
		return this.initMutator(obj, rot);

	}

	public boolean rotation(String nomObjet, float nx, float ny, float nz) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Rotation rot;

		rot = new Rotation(this.mondeInterfacePrive.brickEditor, obj, null);
		rot.axe.set(nx, ny, nz);
		rot.axe.normalizeLocal();
		return this.initMutator(obj, rot);

	}

	public void rayonDetection(String nomObjet, float rayon) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		Map<String, ListeEvtDetection> map = this.mondeInterfacePrive.brickEditor.espace.mapListeEvtDetection;
		ListeEvtDetection o = map.get(nomObjet);
		if (o == null) {
			o = new ListeEvtDetection(obj);
			map.put(nomObjet, o);
		}
		o.rayon(rayon);

	}

	public boolean existeGroupeCameraPositions(String nomPosition) {
		List<String> lst = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerNomsPourGroupe(nomPosition);
		return lst != null;
	}

	public void effacerTableau() {
		this.mondeInterfacePrive.tableauModification.list = new ArrayList<>();
	}

	public void initialiserTableau(String nom, int w, int h,
			Color couleurTransparente) {
		if (this.mondeInterfacePrive.brickEditor.game == null) {
			return;
		}
		Tableau tab = new Tableau();
		tab.echelle = 0.0f;

		tab.initialiser(this.mondeInterfacePrive.brickEditor, w, h);
		if (couleurTransparente != null) {
			Quaternion q = new Quaternion();
			q.x = ((float) couleurTransparente.getRed()) / 255.0f;
			q.y = ((float) couleurTransparente.getGreen()) / 255.0f;
			q.z = ((float) couleurTransparente.getBlue()) / 255.0f;
			q.w = ((float) couleurTransparente.getAlpha()) / 255.0f;
			tab.widget.couleurTransparente = q;
		}
		this.mondeInterfacePrive.tableaux.put(nom, tab);
	}

	public void vitesseActivationTableau(float v) {
		if (this.mondeInterfacePrive.tableauModification != null) {
			this.mondeInterfacePrive.tableauModification.vitesseActivation = v;
		}
	}

	public GestionExploration creerGestionExploration(ObjetMobile om) {
		GestionExploration ge = new GestionExploration(
				this.mondeInterfacePrive,
				this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.decorInfo.niveau + 4,
				1);
		ge.demarer(this, om);
		return ge;
	}

	public GestionExploration creerGestionExploration(ObjetMobile om,
			OctreeExploration octree) {
		GestionExploration r = new GestionExploration(this.mondeInterfacePrive,
				octree);
		r.demarer(this, om);
		return r;
	}

	public OctreeExploration octreeExploration() {
		int niveau = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.decorInfo.niveau + 4;
		return new OctreeExploration(new Vector3f(0, 0, 0), niveau, 1);

	}

	public GestionGrapheExploration creerGestionGrapheExploration(
			ObjetMobile om, int numStep, List<Class> classes) {
		int niveau = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.decorInfo.niveau + 4;
		Octree<GrapheExploration> octree = new Octree<>(new Vector3f(0, 0, 0),
				niveau, 1);
		GestionGrapheExploration ge = new GestionGrapheExploration();
		ge.octree = octree;
		ge.init(this.mondeInterfacePrive.brickEditor, om, numStep, classes);
		return ge;

	}

	public void init(GestionGrapheExploration gge) {
		gge.brickEditor = this.mondeInterfacePrive.brickEditor;
	}

	public GestionGrapheExploration creerGestionGrapheExploration(Vector3f pos,
			float rayon, int numStep, List<Class> classes) {
		int niveau = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.decorInfo.niveau + 4;
		Octree<GrapheExploration> octree = new Octree<>(new Vector3f(0, 0, 0),
				niveau, 1);
		GestionGrapheExploration ge = new GestionGrapheExploration();
		ge.octree = octree;
		ge.init(this.mondeInterfacePrive.brickEditor, pos, rayon, numStep,
				classes);
		return ge;

	}

	public void activerTableauAvecTransition(String nom) {
		if (nom == null) {

			if (this.mondeInterfacePrive.tableau != null) {
				this.mondeInterfacePrive.tableau.activer = false;
				this.mondeInterfacePrive.tableau.transition = null;

			}

			return;
		}

		if (this.mondeInterfacePrive.tableau != null) {
			this.mondeInterfacePrive.tableau.activer = false;
			this.mondeInterfacePrive.tableau.transition = this.mondeInterfacePrive.tableaux
					.get(nom);
			this.mondeInterfacePrive.tableau.transition.activer = true;

			Tableau transition = this.mondeInterfacePrive.tableau.transition;
			if (transition != null) {
				transition.transition = null;
				transition.echelle(0.0f);
			}
			return;
		}

		this.mondeInterfacePrive.tableau = this.mondeInterfacePrive.tableaux
				.get(nom);
		this.mondeInterfacePrive.tableau.activer = true;
		this.mondeInterfacePrive.tableau.echelle(0.0f);
	}

	public boolean devMode() {
		return Game.devMode;
	}

	public void tableauTransparent(boolean b) {
		this.mondeInterfacePrive.tableauTransparent = b;
	}

	public void activerTableau(String nom) {
		// System.out.println(" activer tableau="+nom);
		if (nom == null) {
			if (this.mondeInterfacePrive.tableau != null) {
				this.mondeInterfacePrive.tableau.echelle = 0.0f;
				this.mondeInterfacePrive.tableau.activer = false;

			}
			this.mondeInterfacePrive.tableau = null;
			return;
		}
		if (this.mondeInterfacePrive.tableau != null) {
			this.mondeInterfacePrive.tableau.echelle = 0.0f;
			this.mondeInterfacePrive.tableau.activer = false;

		}
		this.mondeInterfacePrive.tableau = this.mondeInterfacePrive.tableaux
				.get(nom);
		this.mondeInterfacePrive.tableau.echelle = 1.0f;
		this.mondeInterfacePrive.tableau.activer = true;

	}

	public void modifierTableau(String nom) {
		this.mondeInterfacePrive.tableauModification = this.mondeInterfacePrive.tableaux
				.get(nom);

	}

	public int largeurEcran() {
		return this.mondeInterfacePrive.brickEditor.game.getWidth();
	}

	public int hauteurEcran() {
		return this.mondeInterfacePrive.brickEditor.game.getHeight();
	}

	public void ajouterMessage(String msg) {
		ElementTableauText elt = new ElementTableauText();
		elt.text = msg;
		elt.nomCouleur = couleurMessage;
		elt.tableau = this.mondeInterfacePrive.tableauModification;
		elt.centrer = this.centrerMessage;
		this.mondeInterfacePrive.tableauModification.list.add(elt);
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public String couleurMessage;

	public void couleurMessage(String couleur) {
		this.couleurMessage = couleur;
	}

	public boolean centrerMessage = false;

	public void centrerMessage(boolean v) {
		this.centrerMessage = v;
	}

	public void indexVue(int i) {

		this.mondeInterfacePrive.tableauModification.idxVue(i);
	}

	public void nombreElement(int n) {
		this.mondeInterfacePrive.tableauModification.nombreElement(n);

	}

	public void arreter() {
		BrickEditor brickEditor = this.mondeInterfacePrive.brickEditor;
		if (brickEditor.swingEditor == null) {
			System.exit(0);
		}

		brickEditor.swingEditor.arreter();

	}

	public int eventKeyValue(String name) {
		try {

			Field f = Keyboard.class.getField(name);

			return f.getInt(null);
		} catch (NoSuchFieldException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	public String donneePourModel(String nomModel, String nomDonnee) {
		ModelEvent me = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.events
				.get(nomModel);
		if (me == null) {
			return null;
		}
		String s = me.donnees().get(nomDonnee);
		return s;

	}

	public String ressource(Class cls, String name) {
		StringBuilder source = new StringBuilder();
		Exception exception = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					cls.getResourceAsStream(name), "UTF-8"));

			Exception innerExc = null;
			try {
				String line;
				while ((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch (Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch (Exception exc) {
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		} catch (Exception exc) {
			exc.printStackTrace();
			return null;
		}

		return source.toString();
	}

	public Object xmlDecode(String source) {
		XMLDecoder d = new XMLDecoder(new ByteArrayInputStream(
				source.getBytes()), null, null,
				this.mondeInterfacePrive.classLoader);
		Object result = d.readObject();
		d.close();
		return result;

	}

	public String xmlEncode(Object obj) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLEncoder e = new XMLEncoder(os);
		e.writeObject(obj);
		e.close();
		return new String(os.toByteArray());
	}

	public int donnerIndexVue() {
		return this.mondeInterfacePrive.tableauModification.idxVue;
	}

	public void ajouterImages(int nbLigne, String... noms) {
		ElementTableauImage elt = new ElementTableauImage();
		for (String nom : noms) {
			elt.noms.add(nom);
		}
		elt.nbLigne = nbLigne;

		elt.tableau = this.mondeInterfacePrive.tableauModification;
		this.mondeInterfacePrive.tableauModification.list.add(elt);
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public void pause(boolean pause) {
		this.mondeInterfacePrive.pause = pause;

	}

	public void connecter(String user, String mdp) throws IOException {
		this.mondeInterfacePrive.connecter(user, mdp);
	}

	public ServiceInterfaceClient creerUtilisateurAnonyme() throws IOException {

		return this.mondeInterfacePrive.creerUtilisateurAnonyme();
	}

	public ServiceInterfaceClient donnerServiceInterfaceClient() {
		return this.mondeInterfacePrive.brickEditor.sic;
	}

	public void ajouterVue3D(int nbLigne, float inclinaison, float vitesse,
			String nomModel, String couleurFond) {
		Color color;
		try {
			color = (Color) Color.class.getField(couleurFond).get(null);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		ElementTableauVue3D elt = new ElementTableauVue3D(
				this.mondeInterfacePrive.brickEditor, nomModel, vitesse,
				inclinaison, nbLigne, color);

		elt.nbLigne = nbLigne;

		elt.tableau = this.mondeInterfacePrive.tableauModification;
		this.mondeInterfacePrive.tableauModification.list.add(elt);
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public Icone creerIconeEcran(int width, int height) {
		FBO fbo = this.mondeInterfacePrive.donnerFBO(width, height);
		return fbo.creerIconeEcran(this.mondeInterfacePrive.brickEditor);

	}

	public void modifierImages(int idx, int nbLigne, String... noms) {
		ElementTableauImage elt = new ElementTableauImage();
		for (String nom : noms) {
			elt.noms.add(nom);
		}
		elt.tableau = this.mondeInterfacePrive.tableauModification;
		elt.nbLigne = nbLigne;
		this.mondeInterfacePrive.tableauModification.list.set(idx, elt);
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public void modifierMessage(String msg, int idx) {
		ElementTableauText elt = new ElementTableauText();
		elt.text = msg;
		elt.nomCouleur = couleurMessage;
		elt.tableau = this.mondeInterfacePrive.tableauModification;
		elt.centrer = this.centrerMessage;
		this.mondeInterfacePrive.tableauModification.list.set(idx, elt);
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public void effacerMessages() {
		this.mondeInterfacePrive.tableauModification.list = new ArrayList<>();
		this.mondeInterfacePrive.tableauModification.modifier();

	}

	public void tireActif(boolean b) {

		ControlleurCamera.controlleur.tireActif(
				this.mondeInterfacePrive.brickEditor, b);

	}

	public void continuerApresImpactSurObjetMobile(boolean b) {
		this.mondeInterfacePrive.joueur.canon.continuerApresImpactSurObjetMobile = b;
	}

	public void controlleurCameraStandard() {
		if (ControlleurCamera.controlleur != null
				&& ControlleurCamera.controlleur instanceof ControlleurCameraEditeur) {
			ControlleurCamera.controlleur = new ControlleurCameraStandard();
		}
	}

	public void controlleurCameraDadou() {
		if (ControlleurCamera.controlleur != null
				&& ControlleurCamera.controlleur instanceof ControlleurCameraEditeur) {
			ControlleurCamera.controlleur = new ControlleurCameraDadou();
		}
	}

	public void forceControlleurCameraStandard() {

		ControlleurCamera.controlleur = new ControlleurCameraStandard();

	}

	public void forceControlleurCameraDadou() {

		ControlleurCamera.controlleur = new ControlleurCameraDadou();

	}

	public void faireSauter(String nomJoueur, int hauteur) {
		this.mondeInterfacePrive.brickEditor.sautForce = true;
		this.mondeInterfacePrive.brickEditor.hauteurSautForce = hauteur;

	}

	public void initialiserSkybox(String nomBase) {
		this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.skyBox = nomBase;
		this.mondeInterfacePrive.brickEditor.decor.action.nomSkyBox = nomBase;
	}

	public void activerGrappin(boolean b) {
		if (b) {
			Grappin.grappin = new Grappin(this.mondeInterfacePrive.brickEditor);
		} else {
			Grappin.grappin = null;
		}
	}

	public void toucheClavierPourSeRetourner(String event) {
		if (event == null) {
			this.mondeInterfacePrive.brickEditor.toucheDemiTour = null;
		} else {
			this.mondeInterfacePrive.brickEditor.toucheDemiTour = new Key(
					Keyboard.getKeyIndex(event));
		}
	}

	public void debugBox(String nomObjet) {
		if (nomObjet == null) {
			this.mondeInterfacePrive.debugTmpBox = true;
			return;
		}
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		Vector3f size = new Vector3f();
		size.set(obj.mc.size);
		size.multLocal(0.5f);
		obj.box.createDebugBox(this.mondeInterfacePrive.brickEditor.shaderBox,
				size);

	}

	public boolean estAccessibleDepuis(Vector3f debut, Vector3f fin, float sx,
			float sy) {
		return mondeInterfacePrive.brickEditor.estAccessibleDepuis(debut, fin,
				sx, sy, (ObjetMobile om) -> {
					return (om.zoneDetection == null) && om.estVisible;

				});

	}

	public boolean estAccessibleDepuis(Vector3f debut, Vector3f fin, float sx,
			float sy, EspaceSelector es) {
		return mondeInterfacePrive.brickEditor.estAccessibleDepuis(debut, fin,
				sx, sy, es);

	}

	public List<String> groupeCameraPosition(String groupe) {

		return this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerNomsPourGroupe(groupe);
	}

	public List<String> groupeCameraPositionCopie(String groupe) {
		List<String> lst = new ArrayList<String>();
		lst.addAll(this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerNomsPourGroupe(groupe));
		return lst;
	}

	public void modelClassePourTire(String nomJoueur, String modelClasse) {
		this.mondeInterfacePrive.modelClassePourTire(nomJoueur, modelClasse);

	}

	public void echellePourTire(String nomJoueur, float echelle) {
		this.mondeInterfacePrive.echellePourTire(nomJoueur, echelle);

	}

	public CameraPosition donnnerCameraPosition(String nom) {
		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(nom);
		return cp;
	}

	public String creerObjet(String nomClasse, String nomPosition,
			float echelle, Object args) {

		return this.mondeInterfacePrive.creerObjet(nomClasse, nomPosition,
				echelle, args,null);
	}
	public String creerObjet(String nomClasse, String nomPosition,
			float echelle, Object args,ModeleEventInterface mei) {

		return this.mondeInterfacePrive.creerObjet(nomClasse, nomPosition,
				echelle, args,mei);
	}
	public String creerObjet(String nomClasse, Vector3f pos, float echelle,
			Object args, boolean useBoundingSphere) {
		return this.mondeInterfacePrive.creerObjet(nomClasse, pos, echelle,
				args, useBoundingSphere);

	}

	public <T extends ModeleEventInterface> T creerObjet(String nomClasse,
			Vector3f pos, float echelle, Object args,
			boolean useBoundingSphere, Class<T> meiClass) {
		return this.mondeInterfacePrive.creerObjet(nomClasse, pos, echelle,
				args, useBoundingSphere, meiClass);

	}

	public void creerObjet(String nomClasse, Vector3f pos, float echelle,
			Object args, boolean useBoundingSphere, ModeleEventInterface mei) {
		mei.i = this;
		this.mondeInterfacePrive.creerObjet(nomClasse, pos, echelle, args,
				useBoundingSphere, mei);
	}

	public void supprimer(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj == null) {
			return;
		}

		this.mondeInterfacePrive.brickEditor.espace.supprimer(obj);
		if (obj.mutator != null) {
			obj.mutator.fini();
		}
		obj.mutator = null;

	}

	public String nomMondeDemarage() {
		return this.mondeInterfacePrive.brickEditor.decorDeBriqueData.nomMondeDemarage;
	}

	public void stopperMutator(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.mutator = null;
	}

	public void exploser(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Exploser exploser = new Exploser(obj,
				this.mondeInterfacePrive.gestionExplosionSphere,
				this.mondeInterfacePrive.brickEditor.espace, true, true);
		exploser.be = this.mondeInterfacePrive.brickEditor;
		this.initMutator(obj, exploser);

	}

	public void decomposer(String nomObjet, float distance, float vitesse,
			int taille, boolean appliquerTransparence) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Decomposer decomposer = new Decomposer(obj,
				this.mondeInterfacePrive.brickEditor.espace, distance, vitesse,
				taille, appliquerTransparence);
		decomposer.be = this.mondeInterfacePrive.brickEditor;
		this.initMutator(obj, decomposer);
	}

	public void decomposer(String nomObjet, int taille) throws CouleurErreur,
			Exception {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		DecompositionModelClasse dmc = new DecompositionModelClasse(obj.mc,
				this.mondeInterfacePrive.brickEditor, taille);
		DecompositionModelClasseCtx dmcCtx = new DecompositionModelClasseCtx();
		dmcCtx.map = new HashMap<>();
		dmcCtx.dmc = dmc;

		dmcCtx.initialiser(obj.echelle, obj.getTranslationGlobal(),
				obj.getRotationLocal());
		obj.dmc = dmcCtx;

	}

	public void initPhysiqueMonde() {

		this.mondeInterfacePrive.pm = new PhysiqueMonde(
				this.mondeInterfacePrive.brickEditor);
	}

	public void removePhysiqueMonde() {
		this.mondeInterfacePrive.pm = null;
	}

	public PhysiqueMutator decomposerAvecPhysique(String nomObjet,
			Vector3f boxExtent, int elementTaille, float impulse,
			int numStepTransparence, boolean remove) throws CouleurErreur,
			Exception {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		BoundingBox box = new BoundingBox();
		box.xExtent = boxExtent.x;
		box.yExtent = boxExtent.y;
		box.zExtent = boxExtent.z;
		box.getCenter().set(obj.box.getCenter());

		this.mondeInterfacePrive.pm.ajouterDecor(box);
		DecompositionModelClasse dmc = obj.mc.decompositionCube(
				this.mondeInterfacePrive.brickEditor, elementTaille);
		dmc.initialiser(0.0f, obj.echelle, obj.getTranslationGlobal(),
				obj.getRotationLocal());
		PhysiqueMutator pm = mondeInterfacePrive.pm.creerDecompositionPhysique(
				obj, dmc, impulse, numStepTransparence, remove);
		obj.listPO = pm.objets;
		pm.obj = obj;
		this.initMutator(obj, pm);
		return pm;

	}

	public void decomposerRecomposer(String nomObjet, float distance,
			float vitesse, int taille, boolean appliquerTransparence) {
		this.decomposerRecomposer(nomObjet, distance, vitesse, taille,
				appliquerTransparence, null);
	}

	public void decomposerRecomposer(String nomObjet, float distance,
			float vitesse, int taille, boolean appliquerTransparence,
			Vector3f axeRotation) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		DecomposerRecomposer decomposer = new DecomposerRecomposer(obj,
				this.mondeInterfacePrive.brickEditor.espace, distance, vitesse,
				taille, appliquerTransparence, axeRotation);
		decomposer.be = this.mondeInterfacePrive.brickEditor;
		this.initMutator(obj, decomposer);
	}

	public void exploser(String nomObjet, String nomExplosion) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Exploser exploser = new Exploser(obj,
				this.mondeInterfacePrive.gestionExplosionSphere,
				this.mondeInterfacePrive.brickEditor.espace, true, true);
		exploser.be = this.mondeInterfacePrive.brickEditor;
		exploser.nomExplosion = nomExplosion;
		this.initMutator(obj, exploser);

	}

	public void exploser(String nomObjet, String nomExplosion,
			boolean supprimer, boolean reduire) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Exploser exploser = new Exploser(obj,
				this.mondeInterfacePrive.gestionExplosionSphere,
				this.mondeInterfacePrive.brickEditor.espace, supprimer, reduire);
		exploser.be = this.mondeInterfacePrive.brickEditor;
		exploser.nomExplosion = nomExplosion;
		this.initMutator(obj, exploser);

	}

	public void traceEvent(boolean trace) {
		GameEventLeaf.trace = trace;
	}

	public void vitesseRotation(String nomObjet, float s) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.speedRotate = s;
	}

	public void vitesseEchelle(String nomObjet, float s) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.speedEchelle = s;
	}

	public void vitesseTranslation(String nomObjet, float s) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.speedTranslate = s;
	}

	public boolean deplacerVersJoueur(String nomObjet, String nomJoueur) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();
		Vector3f location = this.mondeInterfacePrive.brickEditor.game
				.getCamera().getLocation();

		Mouvement mvt = new Mouvement(this.mondeInterfacePrive.brickEditor,
				obj, location);
		return this.initMutator(obj, mvt);

	}

	public boolean enPause(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj.mutator != null && obj.mutator.alive) {
			return obj.mutator.pause;
		}
		return false;
	}

	public boolean enMouvement(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		return (obj.mutator != null && obj.mutator.alive);
	}

	public void pause(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj.mutator != null) {
			obj.mutator.pause = true;
		}

	}

	public void redemarer(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj.mutator != null) {
			obj.mutator.redemarer();
		}

	}

	public boolean orienterEtDeplacerVersJoueur(String nomObjet,
			String nomJoueur) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();
		Vector3f location = this.mondeInterfacePrive.brickEditor.game
				.getCamera().getLocation();

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, location, true, true);

		mvt.controler = this.mondeInterfacePrive;
		return this.initMutator(obj, mvt);

	}

	public boolean orienterEtDeplacerVersPosition(String nomObjet, Vector3f pos) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, pos, true, true);

		mvt.controler = this.mondeInterfacePrive;
		return this.initMutator(obj, mvt);

	}

	public boolean orienterVersJoueur(String nomObjet, boolean axeZ) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();
		Vector3f location = this.mondeInterfacePrive.brickEditor.game
				.getCamera().getLocation();

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, location, false, axeZ);
		return this.initMutator(obj, mvt);

	}

	public boolean orienterVersPosition(String nomObjet, Vector3f pos) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, pos, false, true);
		return this.initMutator(obj, mvt);

	}
	public boolean orienterAvecAxeEtAngle(String nomObjet, Vector3f axe,float angle) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, axe, angle);
		return this.initMutator(obj, mvt);

	}

	public Map<String, String> paramsObjet(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj.model != null) {
			return obj.model.params();
		}
		return null;

	}

	public Graphe creerGraphe(String nomGroupe, String nomDebut, float speed,
			float rayon) {
		return this.creerGraphe(this.groupeCameraPosition(nomGroupe), nomDebut,
				speed, rayon);

	}

	public Graphe creerGrapheMultiRacine(String nomGroupe, float speed,
			float rayon, boolean peutVoler, float hauteur) {

		return this.mondeInterfacePrive.brickEditor.creerGrapheMultiRacine(
				nomGroupe, speed, rayon, peutVoler, hauteur);
	}

	public Object getData(String name) {
		if (this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.data == null) {
			return null;
		}
		return this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.data
				.get(name);
	}

	public Graphe creerGraphe(List<String> lst, String nomDebut, float speed,
			float rayon) {
		GrapheReduction gr = new GrapheReduction(nomDebut, lst,
				this.mondeInterfacePrive.brickEditor, speed, rayon);
		gr.executerExploration();
		if (gr.newList.size() > 0) {
			while (gr.executerExploration()) {

			}

			while (gr.newList.size() > 0) {
				gr.executerTest();
			}
			gr.preparerFermeture();
			while (gr.executerFermeture()) {

			}
			/*
			 * for (GrapheFermeture gf : gr.fermetures) { System.out.println(" "
			 * + gf.elt.nom() + "=>" + gf.fermer+" "+gf.idx+" size="
			 * +gf.fermetureRef); }
			 */
			gr.graphe.supprimerLienInutile();
			gr.graphe.creerLienInverse();
			return gr.graphe;
		}
		return null;
	}

	public boolean orienterVers(String nomObjet, String nomPosition) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerCameraPosition(nomPosition);
		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, cp.translation, false, true);
		return this.initMutator(obj, mvt);

	}

	public boolean orienterVersObjet(String nomObjet, String nomObjetCible) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ObjetMobilePourModelInstance objCible = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjetCible);

		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();
		if (obj == null) {
			return false;
		}

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, objCible.box.getCenter(), false, true);
		return this.initMutator(obj, mvt);

	}

	public void testCollisionAvecAutreObjetMobile(String nomObjet, boolean b) {
		this.mondeInterfacePrive.brickEditor.espace.mobiles.get(nomObjet).testCollisionAvecAutreObjetMobile = b;

	}

	public void testCollision(String nomObjet, boolean b) {
		this.mondeInterfacePrive.brickEditor.espace.mobiles.get(nomObjet).testCollision = b;

	}

	public void debugGraphe(Graphe graphe) {
		this.mondeInterfacePrive.grapheDebug = new GrapheDebug(
				this.mondeInterfacePrive.brickEditor, graphe);
	}

	public void debugGraphe(GrapheDebug gd) {
		this.mondeInterfacePrive.grapheDebug = gd;

	}

	public boolean orienterEtDeplacerVers(String nomObjet, String nomPosition) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerCameraPosition(nomPosition);
		if (cp == null) {
			throw new Error(" position inconnu " + nomPosition);
		}
		// Vector3f location =
		// this.mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();

		Orientation mvt = new Orientation(this.mondeInterfacePrive.brickEditor,
				obj, cp.translation, true, true);
		mvt.controler = this.mondeInterfacePrive;
		return this.initMutator(obj, mvt);

	}

	public String creerObjetEtDeplacerDans(String nomClasse, Graphe graphe,
			float echelle, Integer choisirPosProcheJoueur, Object args) {
		ObjetMobilePourModelInstance o = this.mondeInterfacePrive
				.creerObjetEtDeplacerDans(nomClasse, graphe, echelle,
						choisirPosProcheJoueur, args,null);
		if (o == null) {
			return null;
		}
		return o.donnerNom();

	}
	public String creerObjetEtDeplacerDans(String nomClasse, Graphe graphe,
			float echelle, Integer choisirPosProcheJoueur, Object args,ModeleEventInterface mei) {
		ObjetMobilePourModelInstance o = this.mondeInterfacePrive
				.creerObjetEtDeplacerDans(nomClasse, graphe, echelle,
						choisirPosProcheJoueur, args,mei);
		if (o == null) {
			return null;
		}
		return o.donnerNom();

	}
	public String donnerElementDecor(Vector3f pos) {
		return this.mondeInterfacePrive.donnerElementDecor(pos);
	}

	public String donnerElementDecorCameraPos() {
		return this.mondeInterfacePrive.donnerElementDecorCameraPos();
	}

	public Kube getKube(Vector3f pos) throws CouleurErreur {
		return this.mondeInterfacePrive.getKube(pos);

	}

	public void initialiserCouleurTire(String nom) {
		this.mondeInterfacePrive.joueur.canon.initialiserProjectile(nom);

	}

	public void initialiserCouleurCible(String nom, String forme) {
		if (this.mondeInterfacePrive.brickEditor.game == null) {
			return;
		}

		this.mondeInterfacePrive.cible.initialiserCible(nom, forme);
	}

	public void initialiserCouleurObjectif(String nom, float transparence) {

		this.mondeInterfacePrive.cad.nom = nom;
		this.mondeInterfacePrive.cad.cible.transparence = transparence;
	}

	public void initialiserCouleurObjectifPourObjetMobile(String nomObjet,
			String nom, float transparence, float distanceAffichage, float size) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (obj.cad == null) {
			obj.cad = new CibleAffichageDistance(size);
		}
		obj.cad.nom = nom;
		obj.cad.cible.transparence = transparence;
		obj.cad.cible.size = size;
		obj.cad.distanceAffichage = distanceAffichage;
	}

	public void initialiserValeurCible(String nomObjet, int valeur) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.cad.valeur = valeur;
	}

	public void supprimerObjectifPourObjetMobile(String nomObjet) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		obj.cad = null;
	}

	public void tireTraverseObjetTransparent(boolean b) {
		this.mondeInterfacePrive.joueur.canon.traverserObjetTransparent = b;
	}

	public void initObjectif(String nomObjet) {
		if (nomObjet == null) {
			this.mondeInterfacePrive.objectif = null;
			return;
		}
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		this.mondeInterfacePrive.objectif = obj;

	}

	public void ecrireDonnee(String nom, String donnee)
			throws FileNotFoundException {

		// SerializeTool.save(obj,
		// mondeInterfacePrive.brickEditor.cheminRessources + "/" + nom);
		BrickEditor be = this.mondeInterfacePrive.brickEditor;
		be.sic.ecrireDonnee(nom, donnee);

	}

	public String serviceError() {
		return this.mondeInterfacePrive.brickEditor.sic.libelleError;
	}

	public String lireDonee(String nom) throws IOException {

		try {
			BrickEditor be = this.mondeInterfacePrive.brickEditor;
			BufferedReader br = be.sic.lireDonnee(nom);
			return ServiceInterfaceClient.readAll(br);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	public void initialiserVitesseJoueur(float vitesse) {
		this.mondeInterfacePrive.brickEditor.scc.speed = vitesse;
	}

	public boolean deplacerVers(String nomObjet, String nomPosition) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerCameraPosition(nomPosition);

		Mouvement mvt = new Mouvement(this.mondeInterfacePrive.brickEditor,
				obj, cp.translation);
		return this.initMutator(obj, mvt);

	}

	public boolean deplacerVers(String nomObjet, Vector3f pos) {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		Mouvement mvt = new Mouvement(this.mondeInterfacePrive.brickEditor,
				obj, pos);
		return this.initMutator(obj, mvt);

	}

	public void voler() {
		this.mondeInterfacePrive.voler();

	}

	public int tailleZoneDetection(String nom) {
		return this.mondeInterfacePrive.zoneDetections.get(nom).zoneDetection
				.taille();
	}

	public String elementZoneDetection(String nom, int idx) {
		return this.mondeInterfacePrive.zoneDetections.get(nom).zoneDetection
				.element(idx).donnerNom();
	}

	public void vitesseTire(float vitesse) {
		this.mondeInterfacePrive.vitesseTire(vitesse);
	}

	public void marcher() {
		this.mondeInterfacePrive.marcher();
	}

	public void sautSansDeplacement(boolean b) {
		if (this.mondeInterfacePrive.brickEditor.scc == null) {
			return;
		}
		this.mondeInterfacePrive.brickEditor.scc.sautSansDeplacement = b;

	}

	public List<String> lesMondes() {
		return DecorDeBriqueDataElement.lesMondes(BrickEditor.cheminRessources);
	}

	public void cacherObjet(String nomObjet) {

		this.mondeInterfacePrive.cacherObjet(nomObjet);

	}

	public boolean afficherObjet(String nomObjet) {
		return this.mondeInterfacePrive.afficherObjet(nomObjet);

	}

	public String typeObjet(String nomObjet) throws JeuxException {
		ObjetMobilePourModelInstance obj = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		return obj.mc.nom;
	}

	public void distanceTire(int distance) {
		this.mondeInterfacePrive.distanceTire(distance);

	}

	public boolean estVisible(String nomObjet, float angle, float h) {
		Vector3f pos = this.mondeInterfacePrive.brickEditor.cam.getLocation();
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return false;
		}
		return om.estVisible(pos, angle, h);

	}

	public boolean estVisiblePourObjet(String nomObjet, String nomAutreObjet,
			float angle, float h) {

		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		ObjetMobilePourModelInstance omAutre = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomAutreObjet);
		if (om == null) {
			return false;
		}
		if (omAutre == null) {
			return false;
		}
		return om.estVisible(omAutre.box.getCenter(), angle, h);

	}

	public boolean estVisible(String nomObjet) {
		return this.mondeInterfacePrive.estVisible(nomObjet);
	}

	public int nbObjetMobileInvisible() {
		return this.mondeInterfacePrive.brickEditor.decor.action.nbInvisible;
	}

	public float fps() {
		return Game.fps.getResult();
	}

	public float fpsGlobal() {
		return Game.fpsGlobal.getResult();
	}

	public float fpsMesure() {
		return Game.fpsMesure.getResult();
	}

	public void vitesse(String nomObjet, int factor) {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om != null) {
			om.factorSpeed = factor;
		}

	}

	public boolean existePositionCamera(String nomPosition) {
		return this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerCameraPosition(nomPosition) != null;
	}

	public void camera(String nomPosition) {
		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.donnerCameraPosition(nomPosition);
		if (this.mondeInterfacePrive.brickEditor.scc.charger(cp)) {
			this.mondeInterfacePrive.brickEditor.sautCamera = true;
		}
		ControlleurCamera.controlleur.chargerCameraPosition(cp);
	}

	public void arreterParcourirGroupeCameraPosition() {
		this.mondeInterfacePrive.parcourGroupeCameraPosition = null;
	}

	public void parcourirGroupeCameraPosition(String nomGroupe) {
		if (nomGroupe == null) {
			this.mondeInterfacePrive.parcourGroupeCameraPosition = null;
		} else {
			this.mondeInterfacePrive.parcourGroupeCameraPosition = new ParcourGroupeCameraPosition(
					mondeInterfacePrive, nomGroupe);
		}
	}

	public void parcourirGroupeCameraPosition(
			List<CameraPositionPourParcour> list) {

		this.mondeInterfacePrive.parcourGroupeCameraPosition = new ParcourGroupeCameraPosition(
				mondeInterfacePrive, list);

	}

	public int indexParcourirGroupeCameraPosition() {
		if (this.mondeInterfacePrive.parcourGroupeCameraPosition == null) {
			return -1;
		}
		return this.mondeInterfacePrive.parcourGroupeCameraPosition.idx;

	}

	public void camera(CameraPosition cp) {
		if (this.mondeInterfacePrive.brickEditor.scc == null) {
			return;
		}
		if (this.mondeInterfacePrive.parcourGroupeCameraPosition != null) {
			return;
		}
		if (this.mondeInterfacePrive.brickEditor.scc.charger(cp)) {
			this.mondeInterfacePrive.brickEditor.sautCamera = true;
		}
		ControlleurCamera.controlleur.chargerCameraPosition(cp);
	}

	public String cloner(String nomObjet) throws CouleurErreur {
		return mondeInterfacePrive.cloner(nomObjet);

	}

	public void initialiserDimensionIcone(int width, int height) {

		if (this.mondeInterfacePrive.brickEditor.game == null) {
			return;
		}
		this.mondeInterfacePrive.iconeFBO = this.mondeInterfacePrive.donnerFBO(
				width, height);
	}

	public void creerIcone(String nomClasse, String nomIcone,
			String nomCouleurFond, float theta, float phi) {
		this.mondeInterfacePrive.creerIcones.add(new CreerIcone(nomClasse,
				nomIcone, nomCouleurFond, theta, phi));

	}

	public void creerIcone(String nomClasse, String nomIcone,
			String nomCouleurFond, float distance, float theta, float phi) {
		this.mondeInterfacePrive.creerIcones.add(new CreerIconeAvecDistance(
				nomClasse, nomIcone, nomCouleurFond, distance, theta, phi));

	}

	public void ajouterIcone(String nom, Icone icone) {
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData.ajouterIcone(
				nom, icone);
	}

	public void actionAvecRendu(Action a) {
		this.mondeInterfacePrive.brickEditor.ajouterAction(a);
	}

	public void echelle(String nomObjet, float echelle) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		Echelle mutator = new Echelle(om, echelle);
		mutator.be = this.mondeInterfacePrive.brickEditor;
		this.initMutator(om, mutator);

	}

	public float donnerEchelle(String nomObjet) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		return om.model.modelClasse.echelle;

	}

	public void couleurFactor(String nomObjet, float factor) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		om.couleurFactor = factor;

	}

	public int entier(float f) {
		return (int) f;
	}

	public void arreter(String nomObjet) throws JeuxException {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om.tc == null) {
			return;
		}
		om.tc.stop = true;

	}

	public Object lire(String nom) {
		return this.mondeInterfacePrive.data.get(nom);
	}

	public void ecrire(String nom, Object value) {
		this.mondeInterfacePrive.data.put(nom, value);
	}

	public void remonter(String nomObjet) {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		TrajectoireContext tc = om.tc;
		if (tc == null) {
			return;
		}
		if (!tc.stop) {
			return;
		}

		tc.inverse = !tc.inverse;
		tc.stop = false;

	}

	public void creerTrajectoire(String nomObjet, DeplacementObjet... deps) {
		Trajectoire t = new Trajectoire();
		for (DeplacementObjet dep : deps) {
			t.ajouter(dep);
		}
		Arbre<Trajectoire> at = new Arbre<Trajectoire>(null, t);
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		TrajectoireContext tc = new TrajectoireContext(at, null, om);

		this.mondeInterfacePrive.ajouterTrajectoireContext(tc);
	}

	public void bouger(String nomObjet) {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		TrajectoireContext tc = om.tc;
		if (tc == null) {

			tc = new TrajectoireContext(om.model.trajectoires, null, om);
			this.mondeInterfacePrive.ajouterTrajectoireContext(tc);

			return;
		}

		if (tc.stop) {

			if (tc.executerEvenement) {
				tc.executerEvenement("bouger");
				return;
			}
			tc.stop = false;

		}

	}

	public <T> T cast(Object obj, Class<? extends T> clazz) {
		if (clazz.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}

		return null;
	}

	public void tirerDepuisObjet(String nomObjet, Vector3f position,
			ProjectileDef pd, Object args) {

		this.mondeInterfacePrive.tirerDepuisObjet(nomObjet, position, pd, args);
	}

	public void tirerDepuisObjet(String nomObjet, Vector3f position,
			ProjectileDef pd, Object args, ModeleEventInterface mei) {

		this.mondeInterfacePrive.tirerDepuisObjet(nomObjet, position, pd, args,
				mei);
	}

	public void tirerDepuisObjetAvecDirection(String nomObjet,
			Vector3f position, ProjectileDef pd, Object args) {

		this.mondeInterfacePrive.tirerDepuisObjetAvecDirection(nomObjet,
				position, pd, args);
	}

	public int nombreObjetDansZoneDetectionJoueur() {
		Joueur j = this.mondeInterfacePrive.joueur;
		if (j.list == null) {
			return 0;
		}
		return j.list.oldList.size();
	}

	public String lireChaineDansFichier(String chemin) {

		String base = BrickEditor.cheminRessources + "_sauvegarde/";
		return OutilFichier.lireChaineDansFichier(base + chemin);

	}

	public boolean ecrireChaineDansFichier(String chemin, String valeur) {

		String base = BrickEditor.cheminRessources + "_sauvegarde/";
		if (!new File(base).exists()) {
			new File(base).mkdir();
		}

		return OutilFichier.ecrireChaineDansFichier(base + chemin, valeur);

	}

	public boolean ajouterChaineDansFichier(String chemin, String valeur) {

		String base = BrickEditor.cheminRessources + "_sauvegarde/";
		if (!new File(base).exists()) {
			new File(base).mkdir();
		}

		try {
			OutilFichier.ajouterDansFichier(base + chemin, valeur);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean collisionAvecDecor(float distance, float dim) {
		ExplorerAuSolCameraControllerBis scc = this.mondeInterfacePrive.brickEditor.scc;
		scc.calculerBoxTest(distance, dim);
		return this.mondeInterfacePrive.brickEditor.decor.gestionCollision
				.verifierCollision(scc.boxCamTest);

	}

	public void collisionAvecObjetMobiles(Vector3f centre, float rayon,
			List<ObjetMobile> ls) {
		ExplorerAuSolCameraControllerBis scc = this.mondeInterfacePrive.brickEditor.scc;
		scc.sphereCamTest.getCenter().set(centre);
		scc.sphereCamTest.setRadius(rayon);

		this.mondeInterfacePrive.brickEditor.decor.espace.processAll(
				scc.sphereCamTest, (ObjetMobile om) -> {
					ls.add(om);

				});

	}

	public String lireRessourceChaineDansFichier(String chemin) {

		String base = this.mondeInterfacePrive.brickEditor.cheminRessources;
		return OutilFichier.lireChaineDansFichier(base + chemin);

	}

	public void ecrireObjet(String chemin, Object s)
			throws FileNotFoundException, IOException {
		String base = this.mondeInterfacePrive.brickEditor.cheminRessources
				+ "_sauvegarde/";
		if (!new File(base).exists()) {
			new File(base).mkdir();
		}
		SerializeTool.save(s, base + chemin);
	}

	public void profondeur(float profondeur) {
		Game.profondeur = profondeur * profondeur;
	}

	public <T> T lireObjet(Class<T> cls, String chemin)
			throws FileNotFoundException, ClassNotFoundException, IOException {
		String base = BrickEditor.cheminRessources + "_sauvegarde/";

		FileInputStream bis;

		bis = new FileInputStream(base + chemin);

		ObjectInputStream ois = new ClassLoaderObjectInputStream(
				this.mondeInterfacePrive.classLoader, bis);

		return (T) ois.readObject();

	}

	public void sauvergarder() throws IOException {

		this.mondeInterfacePrive.brickEditor.decorDeBriqueData
				.sauvegarder(BrickEditor.cheminRessources + "/base.bin");
		int historiqueIndex = this.historiqueIndex() + 1;
		String repHistorique = BrickEditor.cheminRessources + "/_historique/"
				+ historiqueIndex;
		File fRep = new File(repHistorique);
		fRep.mkdirs();
		this.mondeInterfacePrive.brickEditor.decorDeBriqueData
				.sauvegarder(repHistorique + "/base.bin");
		String mondeDemarage = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.nomMondeDemarage;
		if (this.mondeInterfacePrive.nomMonde != null) {
			mondeDemarage = this.mondeInterfacePrive.nomMonde;
		}
		this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.cameraPosition = this
				.donnerCameraPosition();
		this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.sauvegarder(BrickEditor.cheminRessources + "/" + mondeDemarage);
		this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData
				.sauvegarder(repHistorique + "/" + mondeDemarage);
		this.mondeInterfacePrive.sauvegarder(historiqueIndex);

	}

	public void vider(String nsol) {
		Habillage hab = this.mondeInterfacePrive.brickEditor
				.donnerHabillage(this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.nomHabillage);
		Color sol = hab.valeurs.get(nsol);
		DecorDeBrique decor = this.mondeInterfacePrive.brickEditor.decor;
		this.mondeInterfacePrive.brickEditor.espace.vider();
		int nbCube = decor.DecorDeBriqueData.decorInfo.nbCube;
		for (int x = 0; x < nbCube; x++) {
			for (int y = 0; y < nbCube; y++) {
				for (int z = 0; z < nbCube; z++) {
					try {
						Color old = decor.lireCouleur(x, y, z);
						if (!ElementDecor.estVide(old)) {
							if (y == 0 && sol != null) {
								decor.ecrireCouleur(x, y, z, sol);
							} else {
								decor.ecrireCouleur(x, y, z, Color.BLACK);
							}
							decor.modifierBrique(x, y, z);
						} else {
							if (sol != null && y == 0) {
								decor.ecrireCouleur(x, y, z, sol);
								decor.modifierBrique(x, y, z);

							}

						}
					} catch (Throwable e) {
						Log.print("" + x + "," + y + "," + z);
						e.printStackTrace();
						return;
					}
				}
			}

		}
		try {
			decor.reconstuire();
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String selectionInfoText() {
		if (this.mondeInterfacePrive.selection != null) {
			return this.mondeInterfacePrive.brickEditor.info.infoText;
		}
		return "";
	}

	public String objetDansZoneDetectionJoueur(String nomJoueur, int i) {
		Joueur j = this.mondeInterfacePrive.joueur;
		if (j.list == null) {
			return null;
		}
		return j.list.oldList.get(i).donnerNom();
	}

	public void rayonPourZoneDetectionJoueur(String nomJoueur, float rayon) {
		Joueur j = this.mondeInterfacePrive.joueur;
		if (rayon == 0.0f) {
			j.list = null;
		} else {
			if (j.list == null) {
				j.list = new ListeEvtDetection(j);

			}
			j.list.rayon(rayon);
		}
	}

	public boolean sonActif = true;

	public void demarerSon(String nomObjet, String nomSon, float volume) {
		if (!sonActif) {
			return;
		}
		Emeteurs e;

		float coeff = 1.0f;

		Joueur j = this.mondeInterfacePrive.joueur(nomObjet);
		if (j != null) {

			if (j.emeteurs == null) {
				j.emeteurs = new Emeteurs(j);
			}
			e = j.emeteurs;

		} else {

			ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
					.get(nomObjet);

			if (om.emeteurs == null) {
				om.emeteurs = new Emeteurs(om);

			}
			e = om.emeteurs;

			om.calculerDistanceCamera(this.mondeInterfacePrive.brickEditor.game
					.getCamera());
			coeff = 1 - (om.distanceCamera / this.mondeInterfacePrive.joueur.listSon.rayonCarre);
			if (coeff <= 0) {
				return;
			}

		}
		String chemin = mondeInterfacePrive.brickEditor.cheminRessources + "/"
				+ nomSon;
		e.chargerSon(chemin);
		e.volume(chemin, volume);
		e.demarer(chemin, coeff);

	}

	public void stoperSon(String nomObjet, String nomSon) {
		if (!sonActif) {
			return;
		}
		Emeteurs e;

		float coeff = 1.0f;
		Joueur j = this.mondeInterfacePrive.joueur(nomObjet);
		if (j != null) {

			if (j.emeteurs == null) {
				return;
			}
			e = j.emeteurs;

		} else {

			ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
					.get(nomObjet);

			if (om.emeteurs == null) {
				return;

			}
			e = om.emeteurs;

		}

		if (nomSon != null) {
			String chemin = BrickEditor.cheminRessources + "/" + nomSon;
			e.stopper(chemin);

		} else {
			e.stop();
		}

	}

	public void initEffetEcran(float rayon, float red, float green, float blue,
			boolean estDivision) {
		if (this.mondeInterfacePrive.brickEditor.rond == null) {
			this.mondeInterfacePrive.brickEditor.rond = new EffetEcran();
		}
		this.mondeInterfacePrive.brickEditor.rond.blue = blue;
		this.mondeInterfacePrive.brickEditor.rond.red = red;
		this.mondeInterfacePrive.brickEditor.rond.green = green;
		this.mondeInterfacePrive.brickEditor.rond.rayon = rayon;
		this.mondeInterfacePrive.brickEditor.rond.estDivision = estDivision;

	}

	public void resetRond() {
		this.mondeInterfacePrive.brickEditor.rond = null;
	}

	public void pauseSon(String nomObjet, String nomSon) {
		Emeteurs e;
		ElementJeux j = this.mondeInterfacePrive.joueur(nomObjet);
		if (j == null) {
			j = this.mondeInterfacePrive.brickEditor.espace.mobiles
					.get(nomObjet);

		}
		if (j.emeteurs == null) {
			j.emeteurs = new Emeteurs(j);
		}
		e = j.emeteurs;
		String chemin = mondeInterfacePrive.brickEditor.cheminRessources + "/"
				+ nomSon;
		e.pause(chemin);

	}

	public void sonPourImpactTire(String nomJoueur, String nomSon, float volume) {
		String chemin = mondeInterfacePrive.brickEditor.cheminRessources + "/"
				+ nomSon;
		Joueur j = this.mondeInterfacePrive.joueur(nomJoueur);
		j.sonImpact = new Son();
		j.sonImpact.nom = chemin;
		j.sonImpact.volume = volume;

	}

	public void continuerSon(String nomObjet, String nomSon) {
		Emeteurs e;
		ElementJeux j = this.mondeInterfacePrive.joueur(nomObjet);
		if (j == null) {
			j = this.mondeInterfacePrive.brickEditor.espace.mobiles
					.get(nomObjet);

		}
		if (j.emeteurs == null) {
			j.emeteurs = new Emeteurs(j);
		}
		e = j.emeteurs;
		String chemin = mondeInterfacePrive.brickEditor.cheminRessources + "/"
				+ nomSon;
		e.continuer(chemin);

	}

	public void rayonPourZoneDetectionSonJoueur(float rayon) {
		Joueur j = this.mondeInterfacePrive.joueur;
		if (rayon == 0.0f) {

			j.listSon = null;
			j.sonImpact = null;
		} else {
			if (j.listSon == null) {
				j.listSon = new ListeEvtDetectionPourSon(j);

			}
			j.listSon.rayon(rayon);
			j.canon.rayonPourSon = rayon;
		}
	}

	public void hauteurSaut(int hauteurSaut) {
		this.mondeInterfacePrive.brickEditor.hauteurSautMax = hauteurSaut;

	}

	public String nomModele(String nomObjet) {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return null;
		}
		return om.mc.nomModele();

	}

	public String nomModeleClasse(String nomObjet) {
		ObjetMobile om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return null;
		}
		return om.mc.nom;

	}

	public Animation creerAnimation(String model, String... noms) {
		List<ModelClasse> etapes = new ArrayList<>();
		for (String nom : noms) {

			String nomModelClasse = model + "#" + nom;
			if (nom.isEmpty()) {
				nomModelClasse = model;
			}
			ModelClasse mc = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
					.get(nomModelClasse);
			if (mc == null) {
				throw new Error(" nomModelClasse inconnu " + nomModelClasse);
			}
			etapes.add(mc);

		}
		return new Animation(model, etapes);

	}

	public Animation creerAnimation(String model, List<String> noms) {
		List<ModelClasse> etapes = new ArrayList<>();
		for (String nom : noms) {

			String nomModelClasse = model + "#" + nom;
			if (nom.isEmpty()) {
				nomModelClasse = model;
			}
			ModelClasse mc = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
					.get(nomModelClasse);
			if (mc == null) {
				throw new Error(" nomModelClasse inconnu " + nomModelClasse);
			}
			etapes.add(mc);

		}
		return new Animation(model, etapes);

	}

	public void lancerAnimation(String nomObjet, Animation animation,
			int periode, boolean loop) {

		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		if (!om.mc.nomModele().equals(animation.nomModel)) {
			throw new Error(" modele incompatible pour animation "
					+ om.mc.nomModele() + " et " + animation.nomModel);
		}
		if (om.animation != null) {
			// throw new Error(" animation en cours");
		}

		om.animation = new AnimationEtape(animation, periode, loop);

	}

	public void deconstruire(String nomObjet) {
		this.mondeInterfacePrive.deconstruire(nomObjet);
	}

	public void construire(String nomObjet) {
		this.mondeInterfacePrive.construire(nomObjet);
	}

	public void modifierModelClasse(String nomObjet, String nomSprite) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		String nomModelClasseComplet = om.mc.nomModele() + "#" + nomSprite;
		ModelClasse mc = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.get(nomModelClasseComplet);
		if (mc == null) {
			throw new Error(" pas trouvé " + nomModelClasseComplet);
		}
		if (mc.dx == om.mc.dx && mc.dy == om.mc.dy && mc.dz == om.mc.dz) {
			om.mc = mc;
			return;
		}
		om.mc = mc;

		om.dim.set(mc.size);
		om.dim.multLocal(om.echelle);
		om.box.getExtent().set(om.dim.x / 2.0f, om.dim.y / 2.0f,
				om.dim.z / 2.0f);
		om.positionToZero();
		om.translation(om.box.getCenter());
		om.updateTranslationFromBox();

	}

	public void ajouterModelClasseSecondaire(String nomObjet, String nomSprite) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		String nomModelClasseComplet = om.mc.nomModele() + "#" + nomSprite;
		ModelClasse mc = this.mondeInterfacePrive.brickEditor.decorDeBriqueData.models
				.get(nomModelClasseComplet);
		if (mc == null) {
			throw new Error(" pas trouvé " + nomModelClasseComplet);
		}
		om.mcMap.put(nomSprite, mc);
	}

	public void camera(float distance, float phi, float theta,
			Vector3f position, CameraPosition cp) {

		this.mondeInterfacePrive.brickEditor.scc.initCameraPosition(distance,
				phi, theta, position, cp);

	}

	public void arreterAnimation(String nomObjet) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		om.animation = null;
	}

	public boolean compareCharacter(String s, char c) {
		if (s.length() != 1) {
			return false;
		}
		return s.charAt(0) == c;

	}

	public boolean initialiserPosition(String nomObjet, String positionCamera) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		CameraPosition cp = this.mondeInterfacePrive.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(positionCamera);

		om.detachFromOctree();
		this.mondeInterfacePrive.tmp.set(om.getTranslationGlobal());

		this.mondeInterfacePrive.qTmp.loadIdentity();
		om.initTranslationRotationEchelle(cp.translation,
				this.mondeInterfacePrive.qTmp, om.echelle);
		ObjetMobile another = this.mondeInterfacePrive.brickEditor.espace
				.getOMFor(om.box);
		if (another != null) {
			om.initTranslationRotationEchelle(this.mondeInterfacePrive.tmp,
					this.mondeInterfacePrive.qTmp, om.echelle);
			om.updateOctree();
			return false;

		}
		om.updateOctree();
		return true;

	}

	public boolean initialiserPosition(String nomObjet, Vector3f pos) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);

		// om.detachFromOctree();
		this.mondeInterfacePrive.tmp.set(om.box.getCenter());
		om.box.getCenter().set(pos);
		ObjetMobile another = this.mondeInterfacePrive.brickEditor.espace
				.getOMFor(om.box);
		if (another != null && another != om) {
			om.box.getCenter().set(this.mondeInterfacePrive.tmp);
			// om.updateOctree();
			return false;

		}
		om.box.getCenter().set(this.mondeInterfacePrive.tmp);
		Vector3f old = this.mondeInterfacePrive.tmp;
		om.move(pos.x - old.x, pos.y - old.y, pos.z - old.z);

		return true;

	}

	public void brouillard(float b) {
		Game.fogDensity = b;
		this.mondeInterfacePrive.brickEditor.config.config.brouillard = b;
	}

	public Vector3f position(String nomObjet) {
		if (nomObjet == null) {
			return mondeInterfacePrive.brickEditor.scc.boxCam.getCenter();
		}
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om != null) {
			return om.box.getCenter();

		}
		return null;

	}

	public void initialiserEtat(String nomObjet, Object o) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		om.etat = o;

	}

	public Object donnerEtat(String nomObjet) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return null;
		}
		return om.etat;
	}

	public ModeleEventInterface donnerModeleEventInterface(String nomObjet) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return null;
		}
		return om.mei;
	}

	public ObjetMobilePourModelInstance donnerObjetMobile(String nomObjet) {
		ObjetMobilePourModelInstance om = this.mondeInterfacePrive.brickEditor.espace.mobiles
				.get(nomObjet);
		return om;
	}

	public void creerModelClasse(String nomModele, String nomHabillage,
			Color couleurs[][][], int dx, int dy, int dz) throws CouleurErreur,
			Exception {
		this.mondeInterfacePrive.creerModelClasse(nomModele, nomHabillage,
				couleurs, dx, dy, dz);

	}

	public List<BoundingBox> listeBoxCollision(Vector3f p, float rayon) {
		return this.mondeInterfacePrive.listeBoxCollision(p, rayon);
	}
}
