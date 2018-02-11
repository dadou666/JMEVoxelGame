package dadou;

import java.awt.Color;

import dadou.VoxelTexture3D.CouleurErreur;

abstract public class Face extends Partitionneur {
	public VoxelTexture3D tex;
	public ModelClasse mc;
	public boolean etat[][];
	
	public boolean estPlanche = false;
	
	public boolean estVide(Color c) {
		return ElementDecor.estVide(c);
	}
	public Color getBlockColor(int x, int y, int z) throws CouleurErreur {
		Color r;
		if (mc == null) {
			r= tex.getBlockColor(x, y, z);
		} else {
		r= mc.copie[x][y][z];
		}
		if (r == null) {
			return Color.BLACK;
		}
		return r;
	}
	public void init() {
		this.rs.clear();
		if (etat == null) {
			etat = new boolean[dx()][dy()];
	}
		for(int x=0;x < dx();x++) {
			for(int y=0;y < dy();y++) {
				etat[x][y]= false;
			}
		}
	}
	@Override
	abstract	public boolean estPlein(int x, int y);
	@Override
	public boolean estTraite(int x, int y) {
		// TODO Auto-generated method stub
		return etat[x][y];
	}
	@Override
	public void traiter(int x, int y) {
		// TODO Auto-generated method stub
		etat[x][y]=true;
		
	}
	@Override
abstract	public int dx() ;
	@Override
abstract	public int dy() ;
}
