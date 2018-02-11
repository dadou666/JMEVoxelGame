package dadou.parallele;

public class GestionTraitementParallele {
	public Traitement courant;
	public Traitement dernier;
	private boolean enCours = false;
	public void ajouter(Traitement t){
		synchronized(this) {
			if (courant == null)  {
				courant =t;
			}
			t.suivant = dernier;
			dernier =t;
		}
	}
	public void demarer() {
		synchronized(this) {
			if (enCours) {
				return ;
			}
			Thread t = new Thread( new Runnable() {

				@Override
				public void run() {
					while(enCours) {
						executer();
						
					}
					
				}
				
			});
			t.start();
		}
		
	}
	private void executer() {
		Traitement t ;
		synchronized(this) {
			t=courant;
		}
		if (t == null) {
			return;
		}
		t.executer();
		t.estTermine = true;
		synchronized(this) {
			courant =  t.suivant;
		}
		
	}
	public void arreter() {
		enCours = false;
		
	}

}
