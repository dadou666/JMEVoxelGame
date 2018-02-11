package dadou.parallele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Executeur {
	static private ListeTache<? extends Tache> taches;
	static private int numThread;
	static private List<Semaphore> attentesThread;
	static private Semaphore attenteExecuter;

	static public void initialiser(int _numThread) {
		attentesThread = new ArrayList<>();
		attenteExecuter = new Semaphore(0);
		numThread = _numThread;
		for (int i = 0; i < _numThread; i++) {
			attentesThread.add(new Semaphore(0));
			final int u = i;
			Thread t=(new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					calculThread(u);
				}

			}));
			t.setPriority(Thread.MAX_PRIORITY);
			t.start();
		}

	}

	static public void calculThread(int i) {
		do {
			attentesThread.get(i).acquireUninterruptibly();
			if (taches != null) {
				int idxTache = i;
				while (idxTache < taches.taille) {
					Tache tache = taches.tache(idxTache);
					try {
						tache.calculer();

					} catch (Throwable t) {
						tache.erreur(t);
					}
					idxTache += numThread;

				}
			}
			attenteExecuter.release();

		} while (taches != null);

	}

	static private long executer() {

		long debut = System.currentTimeMillis();
		for (int i = 0; i < numThread; i++) {
			attentesThread.get(i).release();
		}
		
		attenteExecuter.acquireUninterruptibly(numThread);
		long r = System.currentTimeMillis() - debut;
		return r;
	}

	synchronized static public long executer(ListeTache<? extends Tache> _taches) {

		taches = _taches;
		if (taches == null) {
			return executer();
		} else
			synchronized (taches) {
				return executer();
			}
	}

}
