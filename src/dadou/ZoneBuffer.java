package dadou;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.jme.math.Vector3f;

public class ZoneBuffer {
	public static int nombreMax = 8;
	public static ZoneBuffer defaut = new ZoneBuffer();
	public static ZoneBuffer courant;
	static public Object mark = new Object();
	static public Object markSource = new Object();
	static public Vector3f colorZone = new Vector3f(0.0f,0.0f,0.0f);

	public FloatBuffer buffer = BufferUtils.createFloatBuffer(nombreMax * 4);
	public Zone elements[] = new Zone[nombreMax];
	public String[] noms = new String[nombreMax];
	public int nombre = 0;
public	ZoneBuffer() {
		for(int i=0;i < nombreMax;i++) {
			noms[i]="zones["+i+"]";
		}
	}

	public void resetBuffer() {
		buffer.clear();
		nombre = 0;
		mark = new Object();
	}

	public static ZoneBuffer valeur() {
		if (courant == null) {
			return defaut;
		}
		return courant;
	}

	public void ajouter(Zone l) {
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

	public void ajouterSansMark(Zone l) {

		buffer.put(l.pos.x);
		buffer.put(l.pos.y);
		buffer.put(l.pos.z);
		buffer.put(l.rayon);
		elements[nombre] = l;
		nombre++;
	}

}
