package dadou;

public class Poid {
	
	short [][] valeurs;
	public float total = 0.0f;
	public Poid(int n,int m) {
		valeurs= new short[n][m];
		for(int u=0;u < n;u++) {
			for(int v=0;v < m;v++) {
				valeurs[u][v]=0;
				
			}
		}
	}
	
	public void ajouter(int u,int v) {
		short a = valeurs[u][v];
		if (a == 0) {
			total+=1;
			valeurs[u][v]=1;
		}
	}

}
