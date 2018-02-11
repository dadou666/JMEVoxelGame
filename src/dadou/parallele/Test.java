package dadou.parallele;

public class Test implements Runnable {
	int n;
	float v;
	
	

	/**
	 * @param args
	 * @throws StartException 
	 */
	public static void main(String[] args) throws StartException {
		// TODO Auto-generated method stub
		Parallele p = new Parallele();
		p.start(4);
	

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while (n > 0) {
			n--;
			v=v*v;
		}
		
	}

}
