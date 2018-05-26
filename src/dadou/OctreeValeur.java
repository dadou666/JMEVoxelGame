package dadou;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class OctreeValeur {
	public ElementDecor ed;

	public ObjetMobile om;
	public Joueur joueur;

	public LumiereBuffer lumiereBuffer = new LumiereBuffer();
	public ZoneBuffer zoneBuffer = new ZoneBuffer();

	public int occquery;

	public void initQuery() {
		IntBuffer queries = BufferUtils.createIntBuffer(1);
		GL15.glGenQueries(queries);
		occquery = queries.get();
	}

	public void ajouterLumiere(Lumiere l) {
		if (lumiereBuffer.nombre == LumiereBuffer.nombreMax) {
			return;
		}

		lumiereBuffer.ajouterSansMark(l);

	}

	public void ajouterZone(Zone l) {
		if (zoneBuffer.nombre == ZoneBuffer.nombreMax) {
			return;
		}

		zoneBuffer.ajouterSansMark(l);

	}

	public boolean contientElementDecor() {
		if (ed == null) {
			return false;
		}
		if (ed.pt != null) {
			return true;
		}
		return ( ed.brique != null && ed.brique.vbo.numTri > 0);
	}

	public boolean estVide() {
		if (this.contientElementDecor()) {
			return false;
		}
		if (om == null) {
			return true;
		}
		return om.zoneDetection == null;
	}
}
