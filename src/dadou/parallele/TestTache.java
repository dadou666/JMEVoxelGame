package dadou.parallele;

import com.jme.math.Vector3f;

public class TestTache implements Tache {
	public float x,y,z;
	public float r;
	public boolean flag = false;
	public int idx;
	int value;
	public void set(float x,float y ,float z) {
		this.x=x;
		this.y=y;
		this.z=z;
		flag = false;
		
	}

	@Override
	public void calculer() {
		// TODO Auto-generated method stub
		for(int i=0;i < 100000;i++) {
		r  = (float) Math.sqrt(x*x+y*y+z*z);  }
		flag =true;
		value = idx;
	}

	@Override
	public void erreur(Throwable t) {
		// TODO Auto-generated method stub

	}

}
