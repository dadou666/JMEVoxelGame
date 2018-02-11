package dadou;

import java.util.concurrent.TimeUnit;

import org.lwjgl.opengl.Display;

public class Fps {
	public long nombreImage = 0;
	public long duree;
	public double t;
	public long fps;

	public Fps() {

		t = TimeUnit.SECONDS.toNanos(1);
	}

	long nano;

	public void calculer(ActionFps a) throws Exception  {
		nano = System.nanoTime();
		nombreImage++;
		a.execute();
		duree += (System.nanoTime() - nano);
		if (nombreImage > 240) {
			fps = (long) (t * (double) nombreImage / (double) duree);
			// Log.print("fps="+fps);
			
			//Display.set
			nombreImage = 0;
			duree = 0;
		}

	}

	public void debut() {
		nano = System.nanoTime();
		nombreImage++;
	}

	public void fin() {
		duree += (System.nanoTime() - nano);
		if (nombreImage > 240) {
			fps = (long) (t * (double) nombreImage / (double) duree);
			// Log.print("fps="+fps);
			nombreImage = 0;
			duree = 0;
		}
	}

	public void reset() {
		duree = 0;
		nombreImage = 0;
	}

	public long getResult() {
		return fps;
	}

}
