package dadou.test;

import dadou.Partitionneur;

public class PartitionneurSimple extends Partitionneur {
	int dx;
	int dy;
    public	boolean [][] donnees;
    public	boolean [][] etats;
	public PartitionneurSimple(int dx,int dy) {
		donnees = new boolean[dx][dy];
		etats = new boolean[dx][dy];
		this.dx=dx;
		this.dy=dy;
		
		
	}
	public void initialiser() {
		for(int x=0;x < dx;x++){
			for(int y=0;y < dy;y++) {
				etats[x][y]=false;
				donnees[x][y]= false;
				
			}
		}
	}
	
	@Override
	public boolean estPlein(int x, int y) {
		// TODO Auto-generated method stub
		return donnees[x][y];
	}

	@Override
	public boolean estTraite(int x, int y) {
		// TODO Auto-generated method stub
		return etats[x][y];
	}

	@Override
	public void traiter(int x, int y) {
		// TODO Auto-generated method stub
		etats[x][y] = true;

	}

	@Override
	public int dx() {
		// TODO Auto-generated method stub
		return dx;
	}

	@Override
	public int dy() {
		// TODO Auto-generated method stub
		return dy;
	}

}
