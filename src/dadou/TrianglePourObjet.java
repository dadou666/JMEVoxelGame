package dadou;

import java.io.Serializable;

import com.jme.math.Vector3f;

public class TrianglePourObjet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 763654938358899972L;
	public Vector3f a;
	public Vector3f b;
	public Vector3f c;
	public Vector3f normal;
	public Vector3f u= new Vector3f();
	public Vector3f v= new Vector3f();

	public TrianglePourObjet(Vector3f a, Vector3f b, Vector3f c) {
		this.a = new Vector3f(a);
		this.b = new Vector3f(b);
		this.c = new Vector3f(c);
		u.set(a);
		u.subtractLocal(b);
		
		v.set(a);
		v.subtractLocal(c);
		
		normal = u.cross(v);
		

	}

}
