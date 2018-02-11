package dadou.parallele;

public class GestionTraitementParallele {
	public Traitement courant;
	public Traitement dernier;
	public Traitement libre;
	private boolean enCours = false;

	public Traitement ajouter(Runnable t) {
		synchronized (this) {
			Traitement tmp = null;
			if (libre != null) {
				tmp = libre;
				libre = libre.suivant;
			}
			if (tmp == null) {
				tmp = new Traitement();
			}
			tmp.runnable = t;
			tmp.estTermine = false;
			if (courant == null) {
				courant = tmp;
				dernier = tmp;
			} else {
				dernier.suivant = tmp;
				
				dernier = tmp;
			}
			return tmp;
		}
	}

	public void demarer() {
		synchronized (this) {
			if (enCours) {
				return;
			}
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					while (enCours) {
						executer();

					}

				}

			});
			enCours = true;
			t.start();
			
		}

	}

	private void executer() {
		Traitement t;
		synchronized (this) {
			t = courant;
		}
		if (t == null) {
			return;
		}
		t.runnable.run();
		t.estTermine = true;

		synchronized (this) {

			courant = t.suivant;
			if (courant == null) {
				dernier = null;
			}
		}

	}

	public void liberer(Traitement t) {
		synchronized (this) {
			t.suivant = libre;
			libre = t;
		}
	}

	public void arreter() {
		enCours = false;

	}

}
