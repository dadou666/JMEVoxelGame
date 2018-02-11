package dadou.graphe;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

import dadou.EspaceSelector;
import dadou.Log;
import dadou.ObjetMobile;
import dadou.OrientedBoundingBoxWithVBO;
import dadou.mutator.Orientation;

public class GrapheArc implements Runnable {
	public int a;
	public int b;
	public float distance;
	public boolean utilisable = true;
	public GrapheConstructeur gc;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		utilisable = estAccessibleDepuis(gc.pos(a), gc.pos(b), gc.rayon / 2.0f, gc.rayon / 2.0f, null);
	//Log.print("arc"+a+"_"+b+" ="+utilisable);
	}

	public Vector3f tmp = new Vector3f();
	Quaternion qTmp = new Quaternion();
	boolean debugTmpBox = false;
	OrientedBoundingBoxWithVBO tmpBox = new OrientedBoundingBoxWithVBO();

	public boolean estAccessibleDepuis(Vector3f debut, Vector3f fin, float sx, float sy, EspaceSelector es) {
		tmp.y = sx;
		tmp.z = sy;

		tmpBox.setXAxis(Vector3f.UNIT_X);
		tmpBox.setYAxis(Vector3f.UNIT_Y);
		tmpBox.setZAxis(Vector3f.UNIT_Z);
		tmpBox.setExtent(tmp);
		tmp.set(debut);
		tmp.addLocal(fin);
		tmp.multLocal(0.5f);

		tmpBox.getCenter().set(tmp);

		tmp.set(fin);
		tmp.subtractLocal(debut);
		tmp.multLocal(0.5f);

		tmpBox.getExtent().x = tmp.length();
		tmp.normalizeLocal();
		Orientation.calculerQuaternionPolaireDirZ(tmp, qTmp);
		qTmp.multLocal(tmpBox.getXAxis());
		qTmp.multLocal(tmpBox.getYAxis());
		qTmp.multLocal(tmpBox.getZAxis());

		return !gc.be.decor.gestionCollision.verifierCollision(tmpBox);

	}

}
