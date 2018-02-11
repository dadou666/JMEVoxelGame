package dadou;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.jme.math.Vector3f;

public class LumiereBuffer {
	public static int nombreMax = 8;
	public static LumiereBuffer defaut = new LumiereBuffer();
	public static LumiereBuffer courant;
	static public Object mark = new Object();
	static public Object markSource = new Object();

	public FloatBuffer buffer = BufferUtils.createFloatBuffer(nombreMax * 4);
	public Lumiere elements[] = new Lumiere[nombreMax];
	public String[] noms = new String[nombreMax];
	public int nombre = 0;
public	LumiereBuffer() {
		for(int i=0;i < nombreMax;i++) {
			noms[i]="lumieres["+i+"]";
		}
	}

	public void resetBuffer() {
		buffer.clear();
		nombre = 0;
		mark = new Object();
	}

	public static LumiereBuffer valeur() {
		if (courant == null) {
			return defaut;
		}
		return courant;
	}

	public void ajouter(Lumiere l) {
		if (l.mark == mark) {
			return;
		}
		l.mark = mark;
		/*
		 * buffer.put(l.pos.x); buffer.put(l.pos.y); buffer.put(l.pos.z);
		 * buffer.put(l.rayon);
		 */

		elements[nombre] = l;
		nombre++;
	}

	public void ajouterSansMark(Lumiere l) {

		buffer.put(l.pos.x);
		buffer.put(l.pos.y);
		buffer.put(l.pos.z);
		buffer.put(l.rayon);
		elements[nombre] = l;
		nombre++;
	}

}
