package dadou.procedural;

public class DescriptionKube {
	public int dx;
	public int dy;
	public int dz;
	public String nomHabillage;
	public String toString() {
		return nomHabillage+" ("+dx+","+dy+","+dz+")";
	}
	
	public DescriptionKube verifier(String nom,DescriptionKube dk) throws DescriptionKubeInvalide {
		if (dk ==null) {
			return this;
		}
		if (dk.dx!= dx) {
			throw new DescriptionKubeInvalide(nom, this, dk);
		}
		if (dk.dy!= dy) {
			throw new DescriptionKubeInvalide(nom, this, dk);
		}
		if (dk.dz!= dz) {
			throw new DescriptionKubeInvalide(nom, this, dk);
		}
		if (!dk.nomHabillage.equals(nomHabillage)) {
			throw new DescriptionKubeInvalide(nom, this, dk);
		}
		return this;
		
	}

}
