package dadou.procedural;

public class ModelClasseInexistant extends Exception {
	public String nom;
	public String getMessage() {
		return "ModelClasse ["+nom+"] inexistant";
	}
	public ModelClasseInexistant(String nom) {
		this.nom = nom;
	}

}
