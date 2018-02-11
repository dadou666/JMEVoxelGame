package dadou;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.event.GameEvent;
import dadou.event.GameEventLeaf;
import dadou.event.GameEventNode;
import dadou.event.GameEventTree;
import dadou.jeux.Trajectoire;
import dadou.tools.BrickEditor;
import dadou.tools.SerializeTool;

public class DecorDeBriqueData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4623066919610694546L;

	public Map<String, ModelClasse> models;
	public Map<String, ConstructionModelClasse> constructions = new HashMap<>();
	public Map<String, ModelEvent> events;
	public ImageEcran imageEcran;
	public String nomMondeDemarage;

	public String mondeCourant;

	transient public Map<String, Icone> icones;
	public List<GameEventLeaf> modules;

	public static DecorDeBriqueData charger() throws FileNotFoundException,
			ClassNotFoundException, IOException {
		return (DecorDeBriqueData) SerializeTool

		.load(BrickEditor.cheminRessources + "/base.bin");
	}

	public List<ModelClasse> donnerListModelClasse(String nomModele) {
		List<ModelClasse> r = new ArrayList<>();
		for (Map.Entry<String, ModelClasse> ne : this.models.entrySet()) {
			String n = ne.getKey();
			if (n != null && ne.getKey().startsWith(nomModele + "#")
					|| n.equals(nomModele)) {
				r.add(ne.getValue());
			}
		}
		return r;
	}

	public ModelEvent donnerModelEvent(String nomModele) {
		if (events == null) {
			events = new HashMap<>();
		}
		ModelEvent r = events.get(nomModele);
		if (r == null) {
			r = new ModelEvent();
			events.put(nomModele, r);

		}
		return r;

	}

	public ModelEvent ajouterModelEvent(String nomModele, ModelEvent me) {
		if (events == null) {
			events = new HashMap<>();
		}
		ModelEvent r = events.get(nomModele);
		if (r != null) {
			return r;
		}
		if (me == null) {
			me = new ModelEvent();
		}
		events.put(nomModele, me);
		return me;

	}

	public boolean existeNomModel(String nomModel) {
		String pattern = nomModel + "#";
		for (String n : this.models.keySet()) {
			if (n.startsWith(pattern) || n.equals(nomModel)) {
				return true;
			}
		}
		return false;
	}

	public Arbre<String> creerArbreModel() {

		Arbre<String> r = new Arbre<String>(null);
		Arbre<String> models = r.ajouter("models");
		Map<String, Arbre<String>> mapDecors = new HashMap<>();
		;
		boolean erreurKeyNull = false;

		for (Map.Entry<String, ModelClasse> e : this.models.entrySet()) {
			String key = e.getKey();
			if (!e.getValue().estElementDecor())
				if (key != null) {
					String cheminEtNom[] = key.split("\\#");
					String chemin[] = cheminEtNom[0].split("\\.");
					String nom = "";
					if (cheminEtNom.length == 2) {
						nom = cheminEtNom[1];

					}
					models.ajouter(chemin, nom);
					String nomModele = e.getValue().nomModele();
					ModelEvent me = null;
					if (this.events != null) {
						me = this.events.get(nomModele);
					}
					if (me == null) {
						me = new ModelEvent();
						this.ajouterModelEvent(nomModele, me);
					}

				} else {
					erreurKeyNull = true;
				}

		}
		for (Map.Entry<String, ModelClasse> e : this.models.entrySet()) {
			String key = e.getKey();
			if (e.getValue().estElementDecor())
				if (key != null) {
					String nomDecor = "decor[" + e.getValue().nomHabillage
							+ "]";
					Arbre<String> decors = mapDecors.get(nomDecor);
					if (decors == null) {
						decors = r.ajouter(nomDecor);
						mapDecors.put(nomDecor, decors);
					}

					String cheminEtNom[] = key.split("\\#");
					String chemin[] = cheminEtNom[0].split("\\.");
					String nom = "";
					if (cheminEtNom.length == 2) {
						nom = cheminEtNom[1];

					}
					decors.ajouter(chemin, nom);

				} else {
					erreurKeyNull = true;
				}

		}
		if (erreurKeyNull) {

			HashMap<String, ModelClasse> tmp = new HashMap<>();
			for (Map.Entry<String, ModelClasse> e : this.models.entrySet()) {
				String key = e.getKey();
				if (key != null) {

					tmp.put(e.getKey(), e.getValue());
				} else {

				}

			}
			this.models = tmp;
		}

		return r;

	}

	public List<String> nomModules() {
		List<String> r = new ArrayList<String>();
		if (modules != null) {
			for (GameEventLeaf module : modules) {
				r.add(module.nom);

			}
		}
		return r;
	}

	public void ajouterModule(GameEventLeaf module) {
		if (modules == null) {
			modules = new ArrayList<>();
		}
		modules.add(module);

	}

	public GameEventLeaf donnerModule(String nom) {
		if (modules != null) {
			for (GameEventLeaf module : modules) {
				if (module.nom.equals(nom)) {
					return module;
				}

			}
		}
		return null;
	}

	public void supprimerModule(String nom) {
		GameEventLeaf o = this.donnerModule(nom);
		if (o == null) {
			return;
		}
		modules.remove(o);

	}

	public Icone donnerIcone(String nom) {
		if (icones == null) {
			return null;
		}
		return icones.get(nom);
	}

	public void supprimerIcone(String nom) {
		icones.remove(nom);
	}

	public List<String> nomIcones() {
		List<String> r = new ArrayList<String>();
		r.addAll(icones.keySet());
		return r;
	}

	public void ajouterIcone(String nom, Icone icone) {
		if (icones == null) {
			icones = new HashMap<>();
		}
		icones.put(nom, icone);

	}

	public void initBuffer(BrickEditor be) throws CouleurErreur, Exception {
		Game game = be.game;
		HashMap<String, ModelClasse> newModels = new HashMap<>();
		for (Map.Entry<String, ModelClasse> e : models.entrySet()) {
			ModelClasse mc = e.getValue();
			try {
				Habillage h = be.donnerHabillage(mc.nomHabillage);
				if (h != null) {
					mc.initBuffer(game, h);
				} else {
					Log.print(" Habillage inconnu ", mc.nom);
				}

				if (mc.dx <= 0 || mc.dy < 0 || mc.dz <= 0) {
					throw new Error(" dx,dy,dz=" + mc.dx + "," + mc.dy + ","
							+ mc.dz);
				}
				if (h != null) {
					newModels.put(e.getKey(), e.getValue());
				}
			} catch (Throwable err) {
				err.printStackTrace();

			}

		}
		models = newModels;
		HashMap<String, ConstructionModelClasse> newConstructions = new HashMap<>();
		for (Map.Entry<String, ConstructionModelClasse> e : this.constructions
				.entrySet()) {
			ConstructionModelClasse cmc = e.getValue();
			try {
				for (ModelClasse mc : cmc.list) {
					mc.initBuffer(game, be.donnerHabillage(mc.nomHabillage));
				}
				newConstructions.put(e.getKey(), cmc);
			} catch (Throwable err) {
				err.printStackTrace();

			}
		}
		this.constructions = newConstructions;

	}

	public void demarer() {

	}

	public void arreter() {

	}

	public DecorDeBriqueData() {
		models = new HashMap<String, ModelClasse>();

	}

	public void sauvegarder() throws IOException {
		sauvegarder(BrickEditor.cheminRessources + "/base.bin");
	}

	public void sauvegarder(String fileName) throws IOException {

		// DecorDeBriqueData.sauvegarder(fileName);
		SerializeTool.save(this, fileName);

	}

	public void ajouter(ModelClasse model, String nom) {
		model.nom = nom;
		this.models.put(nom, model);

	}
    public void supprimerMoldeleClasse(String nomModele) {
    	Set<String> keys =new HashSet<>( models.keySet());
    	
    	for(String key:keys) {
    		if (key.startsWith(nomModele + "#" )){
    			models.remove(key);
    		}
    	}
    }
	public ModelClasse creerModeleClasse(String nomSprite, String nomModele,
			String habillage, int dimX, int dimY, int dimZ) {
		String nom = nomModele + "#" + nomSprite;
		if (models.get(nom) != null) {
		//	return models.get(nom) ;
		}
		ModelClasse model = new ModelClasse();
		model.nomModele = nomModele;
		model.nomHabillage = habillage;

		model.init(dimX, dimY, dimZ);

		ajouter(model, nom);
		return model;

	}

	public GameEventNode creerArbreTrajectoire(Arbre<Trajectoire> arbre) {
		if (arbre == null) {
			return null;
		}
		GameEventTree r = new GameEventTree(arbre.nom);
		if (arbre.valeur != null) {
			if (arbre.valeur.event == null) {
				arbre.valeur.event = new GameEventLeaf("continuer");
			}
			r.ajouter(arbre.valeur.event);
		}
		for (Arbre<Trajectoire> a : arbre.enfants()) {
			r.ajouter(creerArbreTrajectoire(a));
		}
		return r;

	}

	public void creerArbreTrajectoire(GameEventTree tree,
			Arbre<Trajectoire> arbre) {

		if (arbre.valeur != null) {
			if (arbre.valeur.event == null) {
				arbre.valeur.event = new GameEventLeaf("continuer");
			}
			tree.ajouter(arbre.valeur.event);
		}
		for (Arbre<Trajectoire> a : arbre.enfants()) {
			tree.ajouter(creerArbreTrajectoire(a));
		}

	}

	public Arbre<Object> creerArbreObjets(BrickEditor editor) {
		Arbre<Object> objets = new Arbre(null);
		objets.nom = "objets";

		if (editor.decor.DecorDeBriqueData.modelInstances != null)
			for (Map.Entry<String, ModelInstance> e : editor.decor.DecorDeBriqueData.modelInstances
					.entrySet()) {
				String id = e.getKey();
				ModelInstance mi = e.getValue();
				GameEventTree idTree = new GameEventTree(id);

				String nomModele = mi.modelClasse.nomModele();
				String[] nomModeleArray = nomModele.split("\\.");

				Arbre<Object> idArbre = objets.ajouter(nomModeleArray, id);
				idArbre.valeur = mi;
				/*
				 * if (mi.trajectoires != null && mi.trajectoires.valeur !=
				 * null) { GameEventTree trajectoires = new
				 * GameEventTree("trajectoires"); idTree.ajouter(trajectoires);
				 * this.creerArbreTrajectoire(trajectoires, mi.trajectoires); }
				 */
			}
		return objets;
	}

	public GameEventNode creerGameEventNode(BrickEditor editor)
			throws FileNotFoundException, ClassNotFoundException, IOException {

		GameEventTree r = new GameEventTree("Jeux");

		GameEventTree classes = new GameEventTree("classes");

		r.ajouter(classes);

		if (events != null) {
			for (Map.Entry<String, ModelEvent> e : events.entrySet()) {

				ModelEvent evenement = e.getValue();
				List<ModelClasse> l = this.donnerListModelClasse(e.getKey());
				if (!l.isEmpty()) {
					String chemin[] = e.getKey().split("\\.");
					String nom = chemin[chemin.length - 1];
					if (chemin.length >= 1) {
						chemin = Arrays.copyOfRange(chemin, 0,
								chemin.length - 1);
					} else {
						chemin = new String[0];
					}
					GameEventTree cls = new GameEventTree(nom);
					cls.type = "classe";
					if (chemin.length == 0) {
						classes.ajouter(cls);
					} else {
						classes.ajouter(chemin, cls);
					}
					if (evenement != null) {
						evenement.init();
						// evenement.ajouter(cls);
					}
				}

			}
		}

		return r;
	}

}
