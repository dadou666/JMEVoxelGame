package dadou.parallele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ExecuteurVolatile {


	static private List<ExecuteurThread> runnables = new ArrayList<>();

	static public void initialiser(int numThread) {

	
		for (int i = 1; i < numThread; i++) {
			runnables.add(new ExecuteurThread(i, numThread));
		}
	

	}

	

	static public long executer(ListeTache<? extends Tache> taches) {

	
		//long debut = System.currentTimeMillis();
		int numThread = runnables.size()+1;
		for (ExecuteurThread et : runnables) {
			et.taches = taches;
			et.resultAvaillable = false;
			Thread t=(new Thread(et));
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
		
		}

		if (taches != null) {
			int taille = taches.taille;
			Tache [] ls =taches.taches;
			int idxTache = 0;
			while (idxTache < taille) {
				Tache tache = ls[idxTache];
				try {
					tache.calculer();

				} catch (Throwable t) {
					tache.erreur(t);
				}
				idxTache += numThread;

			}
		}
		for (ExecuteurThread et : runnables) {
			while (!et.resultAvaillable) {
			//	System.out.println("wait");
			}
		
		}

	//	long r = System.currentTimeMillis() - debut;
		return 0;

	}

}
