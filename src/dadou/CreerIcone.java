package dadou;

public class CreerIcone {
	String nomClasse; String nomIcone; String nomCouleurFond; float theta; float phi;
	public CreerIcone(String nomClasse, String nomIcone, String nomCouleurFond, float theta, float phi) {
		this.nomClasse = nomClasse;
		this.nomIcone = nomIcone;
		this.nomCouleurFond = nomCouleurFond;
		this.theta=theta;
		this.phi=phi;
		
	}
	public CreerIcone() {
		// TODO Auto-generated constructor stub
	}
	public void creer(MondeInterfacePrive i ) {
		i.creerIcone(nomClasse, nomIcone, nomCouleurFond, theta, phi);
		
	}

}
