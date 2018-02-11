package dadou;

import java.io.Serializable;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class ShadowData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Vector3f location = new Vector3f();
	public Vector3f direction = new Vector3f();
	public Vector3f left = new Vector3f();
	public Vector3f up = new Vector3f();
	public ShadowData(Camera cam) {
		location.set(cam.getLocation());
		direction.set(cam.getDirection());
		left.set(cam.getLeft());
		up.set(cam.getUp());
		
	}
	public ShadowData() {
	
		
	}
	public void init(Vector3f p,float dist,float phi,float theta) {
		float ny=FastMath.sin(theta);
		float nz=FastMath.cos(theta)*FastMath.cos(phi);
		float nx=FastMath.cos(theta)*FastMath.sin(phi);
		direction.set(-nx,-ny,-nz);
		float my=FastMath.sin(theta+FastMath.PI/2.0f);
		float mz=FastMath.cos(theta+FastMath.PI/2.0f)*FastMath.cos(phi);
		float mx=FastMath.cos(theta+FastMath.PI/2.0f)*FastMath.sin(phi);
		up.set(mx,my,mz);
		up.cross(direction,left);
		location.set(p);
		
		location.addLocal(FastMath.sin(phi)*dist, 0, FastMath.cos(phi)*dist);
		
		
		
	}

}
