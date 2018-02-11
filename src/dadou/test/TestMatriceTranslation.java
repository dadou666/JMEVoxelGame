package dadou.test;

import com.jme.math.Matrix4f;
import com.jme.math.Vector3f;

public class TestMatriceTranslation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector3f translation = new Vector3f();
		translation.set(1,2,3);
		Matrix4f mat = new Matrix4f();
		mat.loadIdentity();
		mat.setTranslation(translation);
		Vector3f a = new Vector3f();
		a.set(4,5,6);
		System.out.println( mat.mult(a));
		
		
	}

}
