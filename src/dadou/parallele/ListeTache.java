package dadou.parallele;


public class ListeTache<T extends Tache> {
	
	Tache taches[];
	int taille;

	public ListeTache(int capacity) {
		taches = new Tache[capacity];
		taille = 0;

	}

	public void vider() {
		synchronized (this) {
			taille = 0;
		}
	}

	public void ajouter(T tache) {
		
		if (taille >= taches.length) {
			throw new Error(" capacity");
			
		}
		taches[taille] = tache;
		taille++;

	}

	public T tache(int i) {
		if (i >= taille) {
			return null;
		}
		return (T)taches[i];

	}

}
