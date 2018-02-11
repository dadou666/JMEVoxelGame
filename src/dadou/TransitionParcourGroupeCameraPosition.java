package dadou;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class TransitionParcourGroupeCameraPosition {
	CameraPosition debut;
	CameraPosition fin;
	CameraPosition current;
	MondeInterfacePrive i;
	
	public float t;
	public float distance;
	public Vector3f dep = new Vector3f();
	public float speed ;

	public TransitionParcourGroupeCameraPosition(MondeInterfacePrive i, CameraPosition fin) {
		current = new CameraPosition();
		this.i=i;
		debut = i.brickEditor.scc.creerCameraPosition();
		this.speed = 0.1f;
		dep.set(fin.translation);
		dep.subtractLocal(debut.translation);
		distance = dep.length();
		t=0.0f;
		this.fin = fin;
		current.translation.set(debut.translation);
		current.rotationX.slerp(debut.rotationX, fin.rotationX, t/distance);
		current.rotationY.slerp(debut.rotationY, fin.rotationY,t/distance);
		dep.normalizeLocal();
		dep.multLocal(speed);

	}
	
	public boolean process() {
		t+=speed;
		current.translation.addLocal(dep);
		boolean r =t >= distance;
		if (r) {
			current.translation.set(fin.translation);
			current.rotationX.set(fin.rotationX);
			current.rotationY.set(fin.rotationY);
		} else {
			
			current.rotationX.slerp(debut.rotationX, fin.rotationX, t/distance);
			current.rotationY.slerp(debut.rotationY, fin.rotationY,t/distance);
			
		}
		i.brickEditor.scc.charger(current);
		return r;
		
		
	}
	
	
	
}
