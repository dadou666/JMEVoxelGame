package dadou.parallele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import dadou.Log;

public class Parallele<T extends Runnable> {

	public List<T> list;
	private int idx;
	private Object lock = new Object();
	private long start;

	private enum Etat {
		Start, Begin, Add, End, Stop
	};

	private Etat etat;

	public Parallele() {
		etat = Etat.Start;
		endWait = new Semaphore(0);
	}

	private Semaphore endWait;

	public void stop() throws StopException {
		synchronized (lock) {
			if (etat != Etat.Begin) {
				throw new StopException();
			}
			etat = Etat.Stop;
		}
	}

	public void start(int numThread) throws StartException {
		synchronized (lock) {
			if (etat != Etat.Start) {
				throw new StartException();
			}
			etat = Etat.Begin;
			list = new ArrayList<>();
			while (numThread > 0) {
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						while (etat != Etat.Stop) {
							int u = -1;
							synchronized (lock) {
								if (idx < list.size()) {
									u = idx;
									idx++;
								}
							}
							if (u != -1) {
							//	Log.print(" run " + u);
								try {
									list.get(u).run();
								} catch (Throwable t) {
									
								}
								endWait.release();
							}

						}

					}
				});
				t.start();
				numThread--;
			}

		}

	}

	public void add(T r) throws AddException {
		synchronized (lock) {
			if (etat != Etat.Add) {
				throw new AddException();
			}
		//	Log.print(" add " + r);
			list.add(r);

		}

	}

	public void begin() throws BeginException {
		synchronized (lock) {
			if (Etat.Begin != etat) {
				throw new BeginException();
			}

			list.clear();
			idx = 0;
			etat = Etat.Add;
			start = System.currentTimeMillis();

		}

	}

	public long end() throws EndException, InterruptedException {
		synchronized (lock) {
			if (Etat.Add != etat) {
				throw new EndException();
			}
			etat = Etat.End;

			if (list.isEmpty()) {
				etat = Etat.Begin;
			}

		}

		for (int i=0;i < list.size();i++) {
	
 
			endWait.acquire();
		}

		synchronized (lock) {

			etat = Etat.Begin;
			
			return System.currentTimeMillis() - start;

		}

	}

}
