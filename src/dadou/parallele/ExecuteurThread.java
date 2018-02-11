package dadou.parallele;

import java.util.List;



public class ExecuteurThread implements Runnable {

	public volatile boolean resultAvaillable = false;
	public volatile ListeTache<? extends Tache> taches;
	public int idx;
	public int numThread;
	public ExecuteurThread(int idx,int numThread) {
		this.idx = idx;
		this.numThread = numThread;
	}
	public void run() {
		
			//System.out.println("run");
		
		
		
			if (taches != null) {
				int taille = taches.taille;
				Tache [] ls =taches.taches;
				int idxTache = idx;
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
			resultAvaillable = true;
			
			
			
	
		
	}
	

}
