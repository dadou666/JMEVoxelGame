package dadou;

import dadou.VoxelTexture3D.CouleurErreur;

abstract public class VoxelLigneRec  {
	int i[];
	int j[];
	public void execute() throws CouleurErreur {
		dessiner(i[0],i[1],i[2],j[0],j[1],j[2]);
	}
	public void init(int i[],int j[]) {
		this.i=i;
		this.j=j;
	}
	public 	abstract void process(int x, int y, int z) throws CouleurErreur;
	public void dessiner(int x0,int y0,int z0,int x1,int y1,int z1) throws CouleurErreur {
		if (x0==x1 && y0==y1 && z0==z1) {
			this.process(x0, y0, z0);
			return;
		}
		
		int dx=(x1-x0)/2;
		int dy=(y1-y0)/2;
		int dz=(z1-z0)/2;
		int x=x0+dx;
		int y=y0+dy;
		int z=z0+dz;

		this.process(x, y, z);
		if (x==x0 && y==y0 && z==z0) {
			this.process(x1, y1, z1);
			return;
		}
		if (x==x1 && y==y1 && z==z1) {
			this.process(x0, y0, z0);
			return;
		}
		this.dessiner(x0, y0, z0, x, y, z);
		this.dessiner(x, y, z, x1, y1, z1);		
		
		
		
	}

}
