package dadou.procedural;

public class DescriptionKubeInvalide extends Exception {
	public String nomModelClasse;
	public DescriptionKube dk;
	public DescriptionKube dkAtendu;
	public DescriptionKubeInvalide(String nomModelClasse,DescriptionKube dk ,DescriptionKube dkAttendu) {
	this.nomModelClasse = nomModelClasse;
	this.dk =dk;
	this.dkAtendu=dkAttendu;
	
	}
	public String getMessage(){
		return " description "+dk+" pour "+nomModelClasse+" attendu "+this.dkAtendu;
	}

}
