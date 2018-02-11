package dadou;

import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.glFogf;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingVolume;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.input.MouseInput;
import com.jme.math.Plane;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventNode;
import dadou.graphe.Graphe;
import dadou.graphe.GrapheDebug;
import dadou.graphe.GrapheElement;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.jeux.Trajectoire;
import dadou.jeux.TrajectoireContext;
import dadou.menu.MenuJeux;
import dadou.mutator.Exploser;
import dadou.mutator.Mouvement;
import dadou.mutator.Mutator;
import dadou.mutator.MutatorControler;
import dadou.mutator.Orientation;
import dadou.mutator.ParcourCameraPositions;
import dadou.mutator.ParcourGraphe;
import dadou.mutator.Rotation;
import dadou.physique.PhysiqueMonde;
import dadou.physique.PhysiqueMutator;
import dadou.tools.BrickEditor;
import dadou.tools.ChoixGenerique;
import dadou.tools.ServiceInterfaceClient;
import dadou.tools.canon.CallbackCanonActionJeux;
import dadou.tools.canon.CallbackCanonSphereActionJeux;
import dadou.tools.canon.Canon;
import dadou.tools.canon.Cible;
import dadou.tools.canon.CibleAffichageDistance;
import dadou.tools.canon.GestionAnimationSprite;
import dadou.tools.canon.GestionExplosionSphere;
import dadou.tools.construction.Selection;
import lanceur.SerializeTool;

public class MondeInterfacePrive implements MutatorControler {
	public boolean rechargerMonde = false;
	public String nomMonde;
	public Map<String, Object> data = new HashMap<>();
	public boolean exit = false;

	public MondeInterfacePublic mondeInterface;
	public PhysiqueMonde pm;
	public MondeEventInterface mei;
	public SauvegardeJoueur sauvegardeJoueur;
	public SauvegardeMonde sauvegardeMonde;
	public BrickEditor brickEditor;
	public ObjetMobile objectif;
	public Map<String, GameInfosHorizontal> mapGameInfoHorizontal = new HashMap<>();
	public Map<String, GameInfosVertical> mapGameInfoVertical = new HashMap<>();
	public GameInfosHorizontal gameInfoHorizontalEnCours;
	public GameInfosVertical gameInfoVerticalEnCours;
	public DadouClassLoader classLoader;
	public List<CreerIcone> creerIcones = new ArrayList<>();
	public SelectionKube selectionKube;
	public List<Kube> ajouterKubes = new ArrayList<>();
	public Map<String, ConstructionModelClasse> constructions = new HashMap<>();
	public Map<String, ModelClasse> modelClasses = new HashMap<String, ModelClasse>();
	public boolean activerTableauDirectement = true;
	public boolean pause = false;
	public MenuJeux menuJeux;
	public MenuJeux menuJeuxCourant;

	public ChoixGenerique choixGenerique;
	public Map<String, MondeEventInterface> mondeEventInterfaces = new HashMap<>();
	public Map<String, Class<? extends ModeleEventInterface>> modeleEventInterfaceClasses = new HashMap<>();

	public void chargerClasse() {

		if (this.brickEditor.decorDeBriqueData.events != null)
			for (Map.Entry<String, ModelEvent> e : this.brickEditor.decorDeBriqueData.events
					.entrySet()) {
				String cn = "monde." + e.getKey();
				try {
					if (classLoader != null) {
						modeleEventInterfaceClasses.put(cn,
								classLoader.loadClass(cn));
					} else {
						modeleEventInterfaceClasses.put(cn,
								(Class<? extends ModeleEventInterface>) Class
										.forName(cn));
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					// System.out.println("classe "+cn+" non trouvée ");
					// e1.printStackTrace();
				}

			}

	}

	Vector3f positionObjectif = null;

	public void connecter(String user, String mdp) throws IOException {
		this.brickEditor.sic = new ServiceInterfaceClient(user, mdp, false);
	}

	public ServiceInterfaceClient creerUtilisateurAnonyme() throws IOException {
		this.brickEditor.sic = new ServiceInterfaceClient();
		return this.brickEditor.sic;

	}

	public ServiceInterfaceClient donnerServiceInterfaceClient() {
		return this.brickEditor.sic;
	}

	public void calculerPositionEcranObjectif() {
		if (objectif == null) {
			return;
		}

		Camera cam = this.brickEditor.cam;
		this.cad.calculerPositionEcranObjectif(objectif, cam);

	}

	public int historiqueIndex() throws IOException {

		Path histo = Paths.get(BrickEditor.cheminRessources + "/_historique/");
		if (!Files.exists(histo)) {

			Files.createDirectory(histo);

			return 0;
		}
		int max = 0;
		DirectoryStream<Path> dirs = Files.newDirectoryStream(histo);
		for (Path name : dirs) {
			if (Files.exists(name)) {
				int i = Integer.parseInt(name.getFileName().toString());
				if (i > max) {
					max = i;
				}
			}
		}
		dirs.close();

		return max;

	}

	public void sauvegarder(int historiqueIndex) {
		File repertoire = new File(BrickEditor.cheminRessources
				+ "/_sauvegarde");
		if (!repertoire.exists()) {
			repertoire.mkdir();

		}
		String fichierSauvegardeCourant = BrickEditor.cheminRessources
				+ "/_sauvegarde/current.sav";
		if (this.sauvegardeJoueur == null) {
			this.sauvegardeJoueur = new SauvegardeJoueur();
			this.sauvegardeJoueur.joueur = this.joueur.aj;

		}
		if (this.sauvegardeJoueur.joueur == null) {
			this.sauvegardeJoueur.joueur = this.joueur.aj;
		}
		this.sauvegardeJoueur.sauvegarder(this);
		try {

			String fHistorique = BrickEditor.cheminRessources + "/_historique/"
					+ historiqueIndex + "/current.sav";
			SerializeTool.save(this.sauvegardeJoueur, fHistorique);
			SerializeTool.save(this.sauvegardeJoueur, fichierSauvegardeCourant);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void chargerHistorique() throws IOException {
		String fichierSauvegarde = BrickEditor.cheminRessources
				+ "/_sauvegarde/current.sav";
		int idxHistorique = this.historiqueIndex();
		if (idxHistorique == 0) {
			return;
		}
		String fichierHistorique = BrickEditor.cheminRessources
				+ "/_historique/" + idxHistorique + "/current.sav";
		Path histo = Paths.get(BrickEditor.cheminRessources + "_historique/"
				+ idxHistorique);
		if (!Files.exists(histo)) {
			Log.print(" rep inexistant bug ???");
			return;
		}

		try {
			Files.copy(Paths.get(fichierHistorique),
					Paths.get(fichierSauvegarde),
					StandardCopyOption.REPLACE_EXISTING);

			DirectoryStream<Path> dirs = Files.newDirectoryStream(histo);
			for (Path name : dirs) {

				if (!name.endsWith("current.sav")) {
					Log.print(" name=" + name);
					Path dest = Paths.get(BrickEditor.cheminRessources, name
							.getFileName().toString());
					Log.print(" dest=" + dest);
					Files.copy(name, dest, StandardCopyOption.REPLACE_EXISTING);

				}
				Files.deleteIfExists(name);
			}
			dirs.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Files.deleteIfExists(histo);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void chargerSauvegarde() {
		String fichierSauvegarde = BrickEditor.cheminRessources
				+ "/_sauvegarde/current.sav";
		if (new File(fichierSauvegarde).exists()) {
			try {

				sauvegardeJoueur = (SauvegardeJoueur) SerializeTool
						.load(fichierSauvegarde);

				this.joueur.aj = sauvegardeJoueur.joueur;
				this.joueur.aj.i = this.mondeInterface;

				this.mondeInterface.chargerMonde(sauvegardeJoueur.mondeEnCours,
						null);

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CouleurErreur e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void enregisterSauvegarde() {

	}

	public void chargerMondeEventInterfaceClasses() {
		if (classLoader == null) {
			return;
		}
		mondeEventInterfaces = new HashMap<>();
		List<String> l = DecorDeBriqueDataElement
				.lesMondes(BrickEditor.cheminRessources);
		for (String s : l) {
			String className = "monde." + s.replace(".wld", "");
			try {
				Class clazz = null;
				if (classLoader == null) {
					clazz = Class.forName(className);
				} else {
					clazz = classLoader.loadClass(className);
				}
				MondeEventInterface mei = (MondeEventInterface) clazz
						.newInstance();
				mei.i = this.mondeInterface;
				mondeEventInterfaces.put(className, mei);

			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				System.out.println("Création instance erreur " + className);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				// System.out.println("Classe "+className+" non trouvée ");
			}

		}

	}

	public void creerIcone(String nomClasse, String nomIcone,
			String nomCouleurFond, float theta, float phi) {
		ModelClasse mc = this.brickEditor.decorDeBriqueData.models
				.get(nomClasse);

		Color couleurFond;
		try {
			couleurFond = (Color) Color.class.getField(nomCouleurFond)
					.get(null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			couleurFond = Color.WHITE;
			e.printStackTrace();
		}
		Icone icone = this.iconeFBO.creerIconne(mc, theta, phi,
				mc.size.length(), this.brickEditor.game.getCamera(),
				couleurFond);

		this.brickEditor.decorDeBriqueData.ajouterIcone(nomIcone, icone);

	}

	public void creerIcone(String nomClasse, String nomIcone,
			String nomCouleurFond, float distance, float theta, float phi) {
		ModelClasse mc = this.brickEditor.decorDeBriqueData.models
				.get(nomClasse);

		Color couleurFond;
		try {
			couleurFond = (Color) Color.class.getField(nomCouleurFond)
					.get(null);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			couleurFond = Color.WHITE;
			e.printStackTrace();
		}
		Icone icone = this.iconeFBO.creerIconne(mc, theta, phi, distance,
				this.brickEditor.game.getCamera(), couleurFond);

		this.brickEditor.decorDeBriqueData.ajouterIcone(nomIcone, icone);

	}

	public Map<String, Tableau> tableaux = new HashMap<>();
	public Tableau tableau;
	public Tableau tableauModification;
	public ParcourGroupeCameraPosition parcourGroupeCameraPosition;

	public Map<String, GameInfo> mapAffichage = new HashMap<>();
	public boolean active = false;
	public Joueur joueur;

	public Map<String, ObjetMobile> zoneDetections = new HashMap<>();
	public String debugNomObjet;

	public List<TrajectoireContext> TrajectoireContextPourAjout = new ArrayList<>();
	public int idxTrajectoireContextPourAjout = 0;

	public List<Mutator> listMutator = new ArrayList<Mutator>();
	public List<Mutator> listMutatorTmp = new ArrayList<Mutator>();

	public void ajouterTrajectoireContext(TrajectoireContext tc) {
		if (idxTrajectoireContextPourAjout == TrajectoireContextPourAjout
				.size()) {
			TrajectoireContextPourAjout.add(tc);
		} else {
			TrajectoireContextPourAjout.set(idxTrajectoireContextPourAjout, tc);
		}
		idxTrajectoireContextPourAjout++;
	}

	public Vector2f screenPos = new Vector2f();
	public Vector3f selPos = new Vector3f();
	public boolean reconstruire = false;

	public Joueur joueur(String nom) {
		if (nom == null) {
			return joueur;
		}
		return null;
	}

	public int idxClone = 0;
	int nbMutator = 0;

	public void processMutatorList() {
		List<Mutator> swap = listMutator;
		listMutator = listMutatorTmp;
		listMutatorTmp = swap;
		if (pm != null) {
			pm.totalMutator =0;
		}
		for (Mutator m : listMutatorTmp) {
			if (m instanceof PhysiqueMutator) {
				pm.totalMutator++;	
			}
			if (debugNomObjet != null
					&& m.obj.donnerNom().equals(debugNomObjet)) {
				System.out.println("Debug : " + debugNomObjet);
			}
			if (m.obj.octreeLeaf != null && m.estActif()
					&& (m.pause || m.process())) {
				listMutator.add(m);
			} else {
				m.fini();
			}
		}
		listMutatorTmp.clear();
		if (nbMutator != this.listMutator.size()) {
			// Log.print(" nbMutator=" + this.listMutator.size());
			nbMutator = this.listMutator.size();
		}
	}

	public void creerMondeInterfacePublic() {
		this.mondeInterface = new MondeInterfacePublic(this);
		/*
		 * this.classLoader = new DadouClassLoader(
		 * ClassLoader.getSystemClassLoader());
		 */
		this.chargerMondeEventInterfaceClasses();

		this.chargerClasse();
		this.data = new HashMap<>();
		AbstractJoueur aj = joueur.aj;
		if (aj != null) {
			aj.i = this.mondeInterface;
			aj.demarer();
		}

	}

	public String cheminPour(ObjetMobile om, Arbre<Trajectoire> src) {
		if (src == null) {
			return om.model.nomObjet;
		}
		return src.valeur.chemin;

	}

	public void modifierBar(int idx, int niveau, int niveauMax) {
		this.gameInfoHorizontalEnCours.arrayGameBar[idx].modifier(niveau,
				niveauMax);

	}

	public void afficherBar(int idx, boolean b) {
		GameBar gb = this.gameInfoHorizontalEnCours.arrayGameBar[idx];

		gb.widget.show = b;
	}

	public void afficherBar(int idx, int limit, String contour, String bar) {
		GameBar gb = this.gameInfoHorizontalEnCours.arrayGameBar[idx];
		gb.limit = limit;

		gb.widget.show = true;
		try {
			gb.contour = (Color) Color.class.getField(contour).get(null);
			gb.bar = (Color) Color.class.getField(bar).get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void afficherDonneeHorizontal(int idx, String nomVar, String libelle) {
		this.gameInfoHorizontalEnCours.afficherDonnee(idx, nomVar, libelle);

	}

	public void afficherDonneeVertical(int idx, String nomVar, String libelle) {
		if (this.brickEditor.game == null) {
			return;
		}
		this.gameInfoVerticalEnCours.afficherDonnee(idx, nomVar, libelle);

	}

	public ModelInstance verifierNomObjet(String nom) throws JeuxException {
		DecorDeBriqueDataElement decor = brickEditor.decor.DecorDeBriqueData;
		ModelInstance mi = decor.modelInstances.get(nom);
		if (mi == null) {
			throw new JeuxException(" nomObjet=", nom, "inconnu");
		}
		return mi;
	}

	public ModelClasse verifierNomType(String nom) throws JeuxException {
		DecorDeBriqueData decor = brickEditor.decorDeBriqueData;
		ModelClasse mi = decor.models.get(nom);
		if (mi == null) {
			throw new JeuxException(" nomObjet=", nom, "inconnu");
		}
		return mi;
	}

	public void creerAffichageDonneeHorizontal(String nom, int nombreDeDonnees,
			int nombreDeBar) {
		GameInfosHorizontal g = new GameInfosHorizontal(this, nombreDeDonnees,
				nombreDeBar, 100);
		this.mapGameInfoHorizontal.put(nom, g);

	}

	public void creerAffichageDonneeHorizontal(String nom, int nombreDeDonnees,
			int nombreDeBar, int largeurInfo) {
		if (this.brickEditor.game == null) {
			return;
		}
		GameInfosHorizontal g = new GameInfosHorizontal(this, nombreDeDonnees,
				nombreDeBar, largeurInfo);
		this.mapGameInfoHorizontal.put(nom, g);

	}

	public void creerAffichageDonneeVertical(String nom, int nombreDeDonnees,
			int space) {
		if (this.brickEditor.game == null) {
			return;
		}
		GameInfosVertical g = new GameInfosVertical(this, nombreDeDonnees,
				space);
		this.mapGameInfoVertical.put(nom, g);

	}

	public void activerAffichageDonneeHorizontal(String nom) {
		this.gameInfoHorizontalEnCours = this.mapGameInfoHorizontal.get(nom);
	}

	public void activerAffichageDonneeVertical(String nom) {
		this.gameInfoVerticalEnCours = this.mapGameInfoVertical.get(nom);
	}

	public boolean afficherObjet(String nomObjet) {

		ObjetMobilePourModelInstance om = this.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return false;
		}
		if (om.estVisible) {
			return true;
		}

		om.estVisible = true;
		return true;

	}

	public boolean estVisible(String nomObjet) {
		ObjetMobilePourModelInstance om = this.brickEditor.espace.mobiles
				.get(nomObjet);

		return om.estVisible;

	}

	Quaternion qTmp = new Quaternion();
	Vector3f vTmp = new Vector3f();
	Vector3f vTmp2 = new Vector3f();
	Vector3f vTmp3 = new Vector3f();

	public ObjetMobilePourModelInstance creerObjet(String nomClasse,
			Vector3f pos, Quaternion q, float echelle, Object args,
			ObjetMobile emetteur, boolean useBoundingSphere) {

		return this.creerObjet(nomClasse, pos, q, echelle, args, emetteur,
				useBoundingSphere, (ModeleEventInterface) null);

	}

	public <T extends ModeleEventInterface> T creerObjet(String nomClasse,
			Vector3f pos, Quaternion q, float echelle, Object args,
			ObjetMobile emetteur, boolean useBoundingSphere, Class<T> meiClass) {
		T r = null;
		try {
			r = meiClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.creerObjet(nomClasse, pos, q, echelle, args, emetteur,
				useBoundingSphere, r);
		return r;

	}

	public void deconstruire(String nomObjet) {
		ObjetMobilePourModelInstance om = this.brickEditor.espace.mobiles
				.get(nomObjet);
		ConstructionModelClasse cmc = this.brickEditor.decorDeBriqueData.constructions
				.get(om.mc.nom);
		if (cmc != null) {
			om.construction = new ConstructionModelClasseEnCours(cmc, true);

		}
	}

	public void construire(String nomObjet) {
		ObjetMobilePourModelInstance om = this.brickEditor.espace.mobiles
				.get(nomObjet);
		ConstructionModelClasse cmc = this.brickEditor.decorDeBriqueData.constructions
				.get(om.mc.nom);
		if (cmc != null) {
			om.construction = new ConstructionModelClasseEnCours(cmc, false);

		}
	}

	public ObjetMobilePourModelInstance creerObjet(String nomClasse,
			Vector3f pos, Quaternion q, float echelle, Object args,
			ObjetMobile emetteur, boolean useBoundingSphere,
			ModeleEventInterface mei) {
		Espace espace = this.brickEditor.espace;
		ModelClasse mc = this.brickEditor.decorDeBriqueData.models
				.get(nomClasse);
		if (mc == null) {
			mc = this.modelClasses.get(nomClasse);
		}
		if (mc == null) {
			throw new Error(" pas de model classe " + nomClasse);
		}
		ObjetMobilePourModelInstance obj = new ObjetMobilePourModelInstance(
				this.brickEditor, mc, useBoundingSphere);
	
		mei.om = obj; 
		obj.emetteur = emetteur;
		ConstructionModelClasse cmc = this.brickEditor.decorDeBriqueData.constructions
				.get(nomClasse);
		if (cmc != null) {
			obj.construction = new ConstructionModelClasseEnCours(cmc, false);
			obj.construction.args = args;
			obj.construction.demarer = true;
		}
		String nom = "$$" + this.idxClone + "$$";
		obj.nom = nom;

		obj.initTranslationRotationEchelle(pos, q, echelle);
		ObjetMobile om = espace.getOMFor(obj.box);
		if (om != null && om != emetteur
				&& om.testCollisionAvecAutreObjetMobile) {
			return null;
		}
		if (mei != null
				&& mei.testCollisionAvecDecor
				&& this.brickEditor.decor.gestionCollision
						.verifierCollision(obj.box)) {
			return null;
		}
		espace.mobiles.put(nom, obj);
		String nomModele = mc.nomModele();
		obj.me = this.brickEditor.decorDeBriqueData.events.get(nomModele);
		if (mei == null) {
			obj.mei = obj.evenement()
					.creerModeleEventInterface(nomModele, this);
		} else {

			obj.mei = mei;
			mei.i = this.mondeInterface;
			mei.me = obj.me;

		}
		obj.mei.om = obj;
		obj.testCollisionAvecDecor = mei.testCollisionAvecDecor;
		if (obj.construction == null) {
			obj.evenement().demarer(obj, args);
		}

		this.idxClone++;
		obj.updateOctree();
		return obj;

	}

	public String creerObjet(String nomClasse, String nomPosition,
			float echelle, Object args,ModeleEventInterface mei) {

		CameraPosition cp = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(nomPosition);

		qTmp.set(cp.rotationY);
		qTmp.multLocal(cp.rotationX);
		qTmp.loadIdentity();

		ObjetMobilePourModelInstance obj = this.creerObjet(nomClasse,
				cp.translation, qTmp, echelle, args, null, false,mei);
		if (obj == null) {
			return null;
		}
		return obj.donnerNom();

	}

	public String creerObjet(String nomClasse, Vector3f pos, float echelle,
			Object args, boolean useBoundingSphere) {

		qTmp.loadIdentity();

		ObjetMobilePourModelInstance obj = this.creerObjet(nomClasse, pos,
				qTmp, echelle, args, null, useBoundingSphere);
		if (obj == null) {
			return null;
		}
		return obj.donnerNom();

	}

	public <T extends ModeleEventInterface> T creerObjet(String nomClasse,
			Vector3f pos, float echelle, Object args,
			boolean useBoundingSphere, Class<T> meiClass) {
		qTmp.loadIdentity();

		T r = this.creerObjet(nomClasse, pos, qTmp, echelle, args, null,
				useBoundingSphere, meiClass);
		return r;
	}

	public void creerObjet(String nomClasse, Vector3f pos, float echelle,
			Object args, boolean useBoundingSphere, ModeleEventInterface mei) {
		qTmp.loadIdentity();
		this.creerObjet(nomClasse, pos, qTmp, echelle, args, null,
				useBoundingSphere, mei);

	}

	public Kube getKube(Vector3f pos) throws CouleurErreur {
		Kube r = new Kube();
		this.brickEditor.decor.getCube(pos, r);
		return r;

	}

	public String cloner(String nomObjet) throws CouleurErreur {
		Espace espace = this.brickEditor.espace;
		ObjetMobilePourModelInstance om = espace.mobiles.get(nomObjet);

		ObjetMobilePourModelInstance clone = new ObjetMobilePourModelInstance(
				this.brickEditor, om.model);

		String nomClone = "$$" + this.idxClone + "$$";
		clone.nom = nomClone;

		espace.mobiles.put(nomClone, clone);
		this.idxClone++;
		return nomClone;

	}

	public Vector3f tmp = new Vector3f();
	public Vector3f tmp2 = new Vector3f();

	public ProjectileDef creerProjectileDef() {
		return new ProjectileDef();
	}

	public ObjetMobilePourModelInstance creerEtParcourirDepuisLeDebut(
			String nomClasse, float echelle, String debut, List<String> noms,
			Object args) throws CouleurErreur {

		CameraPosition cp = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(debut);
		CameraPosition cpDest = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(noms.get(1));
		tmp.set(cpDest.translation);
		tmp.subtractLocal(cp.translation);
		tmp.normalizeLocal();

		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		ObjetMobilePourModelInstance obj = this.creerObjet(nomClasse,
				cp.translation, qTmp, echelle, args, null, false);
		if (obj == null) {
			return null;
		}

		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.brickEditor, obj, noms, true);
		this.initMutator(obj, pcp);
		return obj;
	}

	public ObjetMobilePourModelInstance creerEtParcourirDepuisLaFin(
			String nomClasse, float echelle, String debut, List<String> noms,
			Object args) throws CouleurErreur {

		CameraPosition cp = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(debut);
		CameraPosition cpDest = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(noms.get(noms.size() - 1));
		tmp.set(cpDest.translation);
		tmp.subtractLocal(cp.translation);
		tmp.normalizeLocal();

		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		ObjetMobilePourModelInstance obj = this.creerObjet(nomClasse,
				cp.translation, qTmp, echelle, args, null, false);
		if (obj == null) {
			return null;
		}
		ParcourCameraPositions pcp = new ParcourCameraPositions(
				this.brickEditor, obj, noms, false);
		this.initMutator(obj, pcp);
		return obj;
	}

	public Vector3f donnerElementDecorCenter(Vector3f pos) {
		Octree<OctreeValeur> oct = this.brickEditor.decor.octree
				.getForLeafPos(pos);
		if (oct != null) {
			return oct.box.getCenter();
		}
		return null;
	}

	public String donnerElementDecor(Vector3f pos) {
		Octree<OctreeValeur> oct = this.brickEditor.decor.octree
				.getForLeafPos(pos);
		if (oct != null && oct.value.contientElementDecor()) {
			ElementDecor ed = oct.value.ed;
			return "" + ed.x + "," + ed.y + "," + ed.z;

		}
		return null;
	}

	public Vector3f donnerElementDecorCenterForCam() {

		return this.donnerElementDecorCenter(this.brickEditor.scc.translation);
	}

	public String donnerElementDecorCameraPos() {
		return this.donnerElementDecor(this.brickEditor.scc.translation);
	}

	public ObjetMobilePourModelInstance creerObjetEtDeplacerDans(
			String nomClasse, Graphe graphe, float echelle,
			Integer nbPlusProche, Object args,ModeleEventInterface mei) {

		GrapheElement racine = null;

		if (nbPlusProche == null) {
			racine = graphe.racine();
		} else {
			racine = graphe.racineProche(this.brickEditor.scc.translation,
					this.brickEditor.decor.DecorDeBriqueData.cameraPositions,
					nbPlusProche);

		}
		if (racine == null) {

			return null;
		} else {
			// Log.print("racine =" + racine);
		}
		String nomPosition = racine.nom();
		CameraPosition cp = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(nomPosition);

		GrapheElement suivant = racine.tmp;
		CameraPosition cpDest = this.brickEditor.decor.DecorDeBriqueData.cameraPositions
				.get(suivant.nom());
		tmp.set(cpDest.translation);
		tmp.subtractLocal(cp.translation);
		tmp.normalizeLocal();

		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		ObjetMobilePourModelInstance obj = this.creerObjet(nomClasse,
				cp.translation, qTmp, echelle, args, null, false, mei);
		if (obj == null) {
			return null;
		}
		ParcourGraphe mvt = new ParcourGraphe(this.brickEditor, obj, graphe,
				suivant.nom(), racine);

		suivant.reservation = obj;
		this.initMutator(obj, mvt);
		return obj;
	}

	OrientedBoundingBoxWithVBO tmpBox = new OrientedBoundingBoxWithVBO();
	boolean debugTmpBox = false;

	public String tirerDepuisObjet(String nomObjet, Vector3f position,
			ProjectileDef pd, Object args) {
		Espace espace = this.brickEditor.espace;
		ObjetMobilePourModelInstance om = espace.mobiles.get(nomObjet);
		Vector3f pos = om.box.getCenter();
		tmp.set(position);
		tmp.subtractLocal(pos);
		tmp.normalizeLocal();
		tmp2.set(pos);
		float distanceDebut = pd.distanceDebut;
		tmp2.addLocal(tmp.x * distanceDebut, tmp.y * distanceDebut, tmp.z
				* distanceDebut);
		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		ObjetMobilePourModelInstance omProjectile = creerObjet(pd.nomClasse,
				tmp2, qTmp, pd.echelle, args, om, false);

		if (omProjectile == null) {
			return null;
		}

		omProjectile.updateBoxForMouvement();
		omProjectile.speedTranslate = pd.vitesse;
		Mouvement mvt = new Mouvement(brickEditor, omProjectile, tmp,
				pd.distance);
		this.initMutator(omProjectile, mvt);
		return omProjectile.donnerNom();

	}

	public String tirerDepuisObjet(String nomObjet, Vector3f position,
			ProjectileDef pd, Object args, ModeleEventInterface mei) {
		Espace espace = this.brickEditor.espace;
		ObjetMobilePourModelInstance om = espace.mobiles.get(nomObjet);
		Vector3f pos = om.box.getCenter();
		tmp.set(position);
		tmp.subtractLocal(pos);
		tmp.normalizeLocal();
		tmp2.set(pos);
		float distanceDebut = pd.distanceDebut;
		tmp2.addLocal(tmp.x * distanceDebut, tmp.y * distanceDebut, tmp.z
				* distanceDebut);
		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		ObjetMobilePourModelInstance omProjectile = creerObjet(pd.nomClasse,
				tmp2, qTmp, pd.echelle, args, om, false, mei);

		if (omProjectile == null) {
			return null;
		}

		omProjectile.updateBoxForMouvement();
		omProjectile.speedTranslate = pd.vitesse;

		Mouvement mvt = new Mouvement(brickEditor, omProjectile, tmp,
				pd.distance);
		this.initMutator(omProjectile, mvt);
		return omProjectile.donnerNom();

	}

	public String tirerDepuisObjetAvecDirection(String nomObjet,
			Vector3f direction, ProjectileDef pd, Object args) {
		Espace espace = this.brickEditor.espace;
		ObjetMobilePourModelInstance om = espace.mobiles.get(nomObjet);
		Vector3f pos = om.box.getCenter();
		tmp.set(pos);
		qTmp.loadIdentity();
		ObjetMobilePourModelInstance omProjectile = creerObjet(pd.nomClasse,
				tmp, qTmp, pd.echelle, args, om, false);

		if (omProjectile == null) {
			return null;
		}

		omProjectile.updateBoxForMouvement();
		omProjectile.speedTranslate = pd.vitesse;
		Mouvement mvt = new Mouvement(brickEditor, omProjectile, direction,
				pd.distance);
		this.initMutator(omProjectile, mvt);

		return omProjectile.donnerNom();

	}

	public Vector3f depart = new Vector3f();

	public void gererSouris() {
		if (brickEditor.game == null) {
			return;
		}
		int x = Mouse.getX();
		int y = Mouse.getY();
		screenPos.set(x, y);
		if (ControlleurCamera.controlleur.cibleAuCentre(this.brickEditor)) {
			screenPos.set(brickEditor.game.getWidth() / 2,
					brickEditor.game.getHeight() / 2);
		}
		Camera cam = brickEditor.game.getCamera();

		selPos.set(cam.getWorldCoordinates(screenPos, 1));
		if (ControlleurCamera.controlleur.faireTire(brickEditor)) {
			depart.set(cam.getWorldCoordinates(screenPos, 0.05f));
			selPos.subtractLocal(depart);
			selPos.normalizeLocal();

			// joueurs.get(nomJoueur).canon.tirer(brickEditor.game.getCamera().getLocation(),
			// selPos);
			joueur.tirer(depart, selPos);

		}
		selPos.set(cam.getWorldCoordinates(screenPos, 0.1f));
		// if (selectionKube != null && selectionKube.color == null)
		if (this.tableau == null
				&& (selection == null || choixGenerique != null)) {
			cible.devant = true;
			if (this.parcourGroupeCameraPosition == null) {
				cible.dessiner(selPos, cam);
			}
		}
	}

	Cible cible;

	public CibleAffichageDistance cad;
	GestionExplosionSphere gestionExplosionSphere;
	GestionAnimationSprite gestionAnimationSprite;

	public void modelClassePourTire(String nomJoueur, String modelClasse) {
		this.joueur.mcPourTirer = modelClasse;

	}

	public void echellePourTire(String nomJoueur, float echelle) {
		this.joueur.echelle = echelle;

	}

	public void creerJoueur() {
		try {
			if (AbstractJoueur.joueur != null) {
				joueur.aj = AbstractJoueur.joueur;
			} else {
				Class cls = null;
				if (this.classLoader != null) {
					cls = this.classLoader.loadClass("monde.joueur");
				} else {
					cls = Class.forName("monde.joueur");
				}
				joueur.aj = (AbstractJoueur) cls.newInstance();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MondeInterfacePrive(BrickEditor editor) {
		this.brickEditor = editor;
		joueur = new Joueur();
		if (editor.game != null) {
			cible = new Cible();

			cad = new CibleAffichageDistance(0.08f);

			gestionExplosionSphere = new GestionExplosionSphere(2);

			Canon canon = new Canon(editor, 15, 4.0f, 200);

			canon.gestionExplosionSphere = gestionExplosionSphere;
			canon.gestionExplosion = gestionExplosionSphere;

			canon.CallbackCanon = new CallbackCanonSphereActionJeux(
					this.brickEditor, canon);
			joueur.canon = canon;
			joueur.i = this;
		}

	}

	public void initJoueurs() {
		if (this.brickEditor.scc != null) {
			joueur.setBox(this.brickEditor.scc.boxCam);
		}
	}

	public void cacherObjet(String nomObjet) {

		ObjetMobilePourModelInstance om = this.brickEditor.espace.mobiles
				.get(nomObjet);
		if (om == null) {
			return;
		}
		if (!om.estVisible) {
			return;
		}

		om.estVisible = false;

	}

	public void voler() {
		brickEditor.scc.auSol = false;
	}

	public void marcher() {
		if (brickEditor.scc != null) {
			brickEditor.scc.auSol = true;
		}

	}

	public void distanceTire(int distance) {

		Canon canon = joueur.canon;
		if (canon == null) {
			return;
		}
		canon.distance = distance;

	}

	public void vitesseTire(float vitesse) {

		Canon canon = joueur.canon;
		if (canon == null) {
			return;
		}
		canon.vitesse = vitesse;

	}

	ObjetMobile dernierObjetMobileAction;

	public boolean testObjetMobileSortie(ObjetMobile om) {
		if (om == null) {
			return dernierObjetMobileAction != null;

		}
		if (dernierObjetMobileAction == null) {
			return false;
		}
		if (dernierObjetMobileAction.mutator != null
				&& dernierObjetMobileAction.mutator.getClass() == Exploser.class) {
			return false;
		}
		return dernierObjetMobileAction != om;

	}

	public boolean testObjetMobileEntree(ObjetMobile cu) {
		if (cu == null) {
			return false;

		}
		if (dernierObjetMobileAction == null) {
			return true;
		}
		if (dernierObjetMobileAction.mutator != null
				&& dernierObjetMobileAction.mutator.getClass() == Exploser.class) {
			return false;
		}
		return dernierObjetMobileAction != cu;

	}

	public GrapheDebug grapheDebug;

	public void processKeyboard() {
		if (this.brickEditor.game == null) {
			return;
		}
		if (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {

				int event = Keyboard.getEventKey();

				char c = Keyboard.getEventCharacter();
				if (event == Keyboard.KEY_ESCAPE) {
					if (this.menuJeuxCourant == null) {
						this.menuJeuxCourant = this.menuJeux;
						if (this.menuJeux != null) {
							this.menuJeuxCourant.activer(mondeInterface);
							;
						}

						return;
					}
				}
				if (this.menuJeuxCourant != null) {
					this.menuJeuxCourant = this.menuJeuxCourant.gerer(
							mondeInterface, event);
					if (this.menuJeuxCourant == null) {
						mondeInterface.activerTableauAvecTransition(null);
					}
					return;
				}
				AbstractJoueur aj = this.brickEditor.mondeInterface.joueur.aj;
				if (aj != null) {
					aj.i = this.mondeInterface;
					aj.appuyerSurClavier(c, event);
				} else {
					this.brickEditor.decor.DecorDeBriqueData.appuyerSurClavier(
							this, c, event);
				}

			}
		}

	}

	Map<String, Color> selectionColors = new HashMap<>();

	public Color selectionColor(String nom) {
		Color color = selectionColors.get(nom);
		if (color == null) {
			try {
				color = (Color) Color.class.getField(nom).get(null);
				selectionColors.put(nom, color);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return color;

	}

	public Map<String, FBO> mapFBO = new HashMap<>();
	FBO iconeFBO;

	public FBO donnerFBO(int width, int height) {
		String key = "" + width + "X" + height;
		FBO fbo = this.mapFBO.get(key);
		if (fbo == null) {
			fbo = new FBO();
			fbo.init(width, height);
			this.mapFBO.put(key, fbo);

		}
		return fbo;
	}

	Tableau old;
	Selection selection;

	public void loop() throws CouleurErreur, FileNotFoundException,
			ClassNotFoundException, IOException {
		Camera cam = brickEditor.cam;

		if (brickEditor.chargeur.decor != null) {
			/*
			 * if (brickEditor.decor != null) { brickEditor.decor.dessiner(cam);
			 * }
			 */
			brickEditor.chargeur.charger(100);

			if (brickEditor.chargeur.decor == null) {

				if (this.mondeInterface == null) {
					creerMondeInterfacePublic();

					this.brickEditor.decorDeBriqueData.demarer();
					this.debugTmpBox = false;
				}

				if (this.brickEditor.mondeInterface.sauvegardeJoueur != null) {
					this.brickEditor.mondeInterface.sauvegardeJoueur
							.chargerDebut(this.brickEditor.mondeInterface);
				}
				this.brickEditor.demarerMonde();
				if (this.brickEditor.mondeInterface.sauvegardeJoueur != null) {
					this.brickEditor.mondeInterface.sauvegardeJoueur
							.chargerFin(this.brickEditor.mondeInterface);
				}

			}

			return;
		}

		if (this.mondeInterface == null) {
			creerMondeInterfacePublic();

			if (this.brickEditor.decorDeBriqueData.nomMondeDemarage != null) {

				this.mondeInterface.chargerMonde(
						this.brickEditor.decorDeBriqueData.nomMondeDemarage,
						null);
				nomMonde = this.brickEditor.decorDeBriqueData.nomMondeDemarage;
				return;
			}

			this.brickEditor.decorDeBriqueData.demarer();
			this.brickEditor.demarerMonde();

		}

		processKeyboard();
		if (brickEditor.chargeur.decor != null) {
			return;
		}
		for (ObjetMobile om : this.zoneDetections.values()) {
			om.zoneDetection.reset();
		}
		this.brickEditor.espace.creerEvenementDetection(this);

		DecorDeBrique decor = brickEditor.decor;
		if (selection != null) {
			selection.gerer();
		}
		if (choixGenerique != null) {

			choixGenerique.gerer();

			if (choixGenerique.choix != null || choixGenerique.quitter) {
				choixGenerique = null;
			} else {
				choixGenerique.dessiner(cam);
			}

		}
		if (!pause) {

			if (this.active) {
				
				this.processMutatorList();
				if (this.pm != null) {
					this.pm.step();
				}
			}
			this.brickEditor.espace.gererTrajectoires();

			// decor.reconstuire();
			if (choixGenerique == null && this.brickEditor.game != null)
				try {
					if (this.parcourGroupeCameraPosition != null) {
						this.parcourGroupeCameraPosition.parcourir();
					} else {

						this.brickEditor
								.gererCamera(this.brickEditor.verouillerRotationEtTranslationCamera);
						this.brickEditor.occlusionVolume.init(
								this.brickEditor.cam,
								brickEditor.scc.screenWidth,
								brickEditor.scc.screenHeight, 1.0f);

					}

				} catch (SautCamera e1) {
					// TODO Auto-generated catch block
					this.brickEditor.hauteurChute = 0.0f;
					this.brickEditor.chute = false;
				} catch (SautMonde saut) {
					return;
				}
			this.joueur.octreeRoot = this.brickEditor.espace.octree;
			this.joueur.updateOctree();
			this.gererEntreeSortie();
			if (this.brickEditor.chute) {
				this.brickEditor.chute = false;
				AbstractJoueur aj = joueur.aj;
				if (aj != null) {
					aj.i = this.mondeInterface;
					aj.tomber(this.brickEditor.hauteurChute);
				} else {
					this.brickEditor.decor.DecorDeBriqueData.tomber(this,
							this.brickEditor.hauteurChute);
				}
				this.brickEditor.hauteurChute = 0.0f;
			}
			if (this.active) {
				this.brickEditor.boucler();
			}

			if (ControlleurCamera.controlleur.selectButton() != null
					&& ControlleurCamera.controlleur.selectButton().isPressed()) {
				int x = Mouse.getX();
				int y = Mouse.getY();

				if (this.gameInfoHorizontalEnCours != null) {
					Widget dernierSelection = this.gameInfoHorizontalEnCours.ihmInfo
							.getWidgetAt(x, y);
					if (dernierSelection != null) {
						selectionnerInfoJeux(dernierSelection.idxObject);

					} else {
						selectionnerInfoJeux(null);
					}

				} else {
					selectionnerInfoJeux(null);
				}

			}

			decor.espace.creerEvenementDetectionSon(this);

			for (int k = 0; k < this.idxTrajectoireContextPourAjout; k++) {
				TrajectoireContext tc = this.TrajectoireContextPourAjout.get(k);
				// System.out.println(" nom="+tc.om.donnerNom());
				this.brickEditor.espace.trajectoiresContexts.put(
						tc.om.donnerNom(), tc);

			}

			this.idxTrajectoireContextPourAjout = 0;
		}
		this.gererSouris();

		if (selectionKube != null) {
			selectionKube.gererMouseWheel();

		}

	}

	public void selectionnerInfoJeux(Integer idx) {
		AbstractJoueur aj = joueur.aj;
		if (aj != null) {
			aj.i = this.mondeInterface;
			aj.selectionnerInfoJeux(idx);
			return;
		}
		this.brickEditor.decor.DecorDeBriqueData
				.selectionnerInfoJeux(this, idx);

	}

	public void figerCamera(boolean b) {
		if (this.brickEditor.verouillerRotationEtTranslationCamera == b) {
			return;
		}
		this.brickEditor.verouillerRotationEtTranslationCamera = b;
		ControlleurCamera.controlleur.figerRotationEtTranslationCamera(b,
				this.brickEditor);

	}

	public void figerTranslationCamera(boolean b) {
		ControlleurCamera.controlleur.figerTranslationCamera(b,
				this.brickEditor);
	}

	public void dessiner() throws InterruptedException, Exception {

		if (brickEditor.chargeur.decor != null) {
			return;
		}
		Camera cam = brickEditor.game.getCamera();

		DecorDeBrique decor = this.brickEditor.decor;
		decor.action.numTriAvantTest = brickEditor.config.config.numTriAvantTest;

		Game.fogDensity = brickEditor.config.config.brouillard;

		if (selection != null) {

			selection.dessiner(brickEditor.cam);
		}

		if (this.gameInfoHorizontalEnCours != null) {

			this.gameInfoHorizontalEnCours.dessiner();
		}
		if (this.gameInfoVerticalEnCours != null) {
			gameInfoVerticalEnCours.dessiner();
		}
		if (tableau != old) {

			old = tableau;

		}

		OpenGLTools.exitOnGLError("tableauDessiner");
		if (grapheDebug != null) {
			grapheDebug.dessiner();
		}
		/*
		 * if (this.debugTmpBox) { if (tmpBox.debugBox == null) { return; }
		 * Shader shader = tmpBox.debugBox.shader; shader.use();
		 * 
		 * // selection.shader.glUniformfARB("size", 1);
		 * shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);
		 * 
		 * shader.glUniformfARB("echelle", 1.0f);
		 * this.tmpBox.objDebugBox.dessiner(cam); }
		 */

		brickEditor.chargerModelView();
		if (selectionKube == null || selectionKube.color == null)

			try {
				joueur.canon.dessiner(cam);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		this.calculerPositionEcranObjectif();
		brickEditor.decor.dessiner(cam);

		cad.dessiner(cam);

		if (this.tableau != null) {
			if (tableauTransparent) {
				this.tableau.dessinerAvecTransparence(Game.shaderWidget);
			} else {
				this.tableau.dessiner(Game.shaderWidget);
			}
			tableau.gererActivation(this);

		}

	}

	public boolean tableauTransparent = true;

	public void gererEntreeSortie() {
		ObjetMobile om = this.brickEditor.omHauteur;
		if (om != null && om.mutator != null
				&& om.mutator.getClass() == Exploser.class) {
			return;

		}
		if (testObjetMobileSortie(om)) {

			try {
				// System.out.println("Sortie");
				if (dernierObjetMobileAction.evenement() != null) {

					dernierObjetMobileAction.evenement().sortie(
							dernierObjetMobileAction);
				}

				dernierObjetMobileAction = null;

			} catch (Throwable t) {
				t.printStackTrace();
			}

		}

		if (testObjetMobileEntree(om)) {
			dernierObjetMobileAction = om;

			try {
				// System.out.println("Entree");
				// om.mouvementEcouteur = brickEditor;
				if (om.evenement() != null) {
					om.evenement().entree(om, null);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Override
	public boolean initMutator(ObjetMobilePourModelInstance om, Mutator m) {

		if (om.mutator != null) {
			Mutator tmp = om.mutator.interrompre(m);
			if (tmp == om.mutator) {

				return tmp.interruption;
			}
		}

		om.mutator = m;
		listMutator.add(m);
		return true;

	}

	public void creerModelClasse(String nomModele, String nomHabillage,
			Color couleurs[][][], int dx, int dy, int dz) throws CouleurErreur,
			Exception {
		ModelClasse mc = this.modelClasses.get(nomModele);
		if (mc != null) {
			return;
		}
		mc = new ModelClasse();
		mc.copie = couleurs;
		mc.nomHabillage = nomHabillage;
		mc.initBuffer(this.brickEditor.game,
				this.brickEditor.donnerHabillage(nomHabillage));

		this.modelClasses.put(nomModele, mc);

	}

	public List<BoundingBox> listeBoxCollision(Vector3f p, float rayon) {
		OctreeDivision od = new OctreeDivision();
		ArrayList<BoundingBox> r = new ArrayList<>();
		this.brickEditor.decor.gestionCollision.listeBoxCollision(od, p, rayon,
				r);
		return r;

	}

}
