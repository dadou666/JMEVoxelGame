package dadou;

public class CreerIconeAvecDistance extends CreerIcone {
	float distance;
public	CreerIconeAvecDistance(String nomClasse, String nomIcone, String nomCouleurFond,float distance, float theta, float phi) {
	this.nomClasse = nomClasse;
	this.nomIcone = nomIcone;
	this.nomCouleurFond = nomCouleurFond;
	this.theta=theta;
	this.phi=phi;
	this.distance =distance;
		
	}
	public void creer(MondeInterfacePrive i ) {
		i.creerIcone(nomClasse, nomIcone, nomCouleurFond,distance, theta, phi);
		
	}

}
