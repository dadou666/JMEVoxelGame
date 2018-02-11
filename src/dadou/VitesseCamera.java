package dadou;

public class VitesseCamera {
	public float rotationAxeGravite;
	public float rotationInclinaison;
	public float vitesseDeplacement;
	public float vitesseDeplacementX=1.0f;
	public float vitesseDeplacementZ=1.0f;
	public float vitesseLeft ;
	public float vitesseUp;
	public void reset() {
		rotationInclinaison = 0;
		rotationAxeGravite = 0;
		vitesseLeft = 0;
		vitesseUp = 0;
		vitesseDeplacement = 0;
		
	}


}
