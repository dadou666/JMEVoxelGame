package dadou;

public class Deceleration {
	public float duree;
	public float t;
	public float vitesse;
	public boolean actif = false;
	public float calculerVitesse() {
		if (t >= duree ) {
			return 0.0f;
			
		}
		return (1-t/duree)*vitesse;
	}
	
	public void incrementer(float deltat) {
		t+=deltat;
		if (t >= duree) {
			actif = false;
		}
	}
		

}
