package dadou;

import java.awt.Color;

import dadou.VoxelTexture3D.CouleurErreur;

public class FaceZ extends Face{
	public int minX;
	public int minY;
	public int dx;
	public int dy;
	public int ux;
	public int uy;
	public int uz;
	public float getMinX() {
		return minX;
	}

	public float getMinY() {
		return minY;
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxY() {
		return maxY;
	}

	public int maxX;
	public int maxY;
	public boolean estVide = true;
	
	
	
	public boolean calculer( int px, int py, int pz, int dimX,int dimY, Poid p) throws CouleurErreur {
		minX=dimX;
		minY=dimY;
		maxX=0;
		maxY=0;
		estVide=true;
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y <dimY; y++) {
				int cx = px + x;
				int cy = py + y;
				if (mc==null &&!estVide(this.getBlockColor(cx, cy, pz))) {
					p.ajouter(x, y);
					
				}
				boolean r = this.testCase( cx, cy, pz);
				if (r){
					estVide=false;
					if (x> maxX){
						maxX=x;
					}
					if (y > maxY){
						maxY=y;
					}
					
					if (x<minX){
						minX=x;
					}
					if (y < minY){
						minY=y;
					}
				}
				
			}

		}
		maxX++;
		maxY++;
		//System.out.println("Z maxX="+maxX+" maxY="+maxY+" minX="+minX+" minY="+minY+" estVide="+estVide+" pz="+pz);
		return estVide;
	}
	public void calculerPartition( int px, int py, int pz, int dimX,int dimY, Poid p) throws CouleurErreur {
	
		this.ux=px;
		this.uy=py;
		this.uz=pz;
		this.dx=dimX;
		this.dy=dimY;
		
		this.init();
		this.calculer();
		for (int x = 0; x < dimX; x++) {
			for (int y = 0; y <dimY; y++) {
				int cx = px + x;
				int cy = py + y;
				if (mc==null &&!estVide(this.getBlockColor(cx, cy, pz))) {
					p.ajouter(x, y);
					
				}}}
	}
	
	
	public boolean estVide(Color c) {
		if (!estPlanche)  {
			return super.estVide(c);
		}
		return c.getBlue() == 0;
	}
	public boolean testCase( int x,int y,int z) throws CouleurErreur{
		if (estPlanche) {
			return !estVide(getBlockColor(x, y, z));
		}
		int d = tex.dimZ;
		if (this.mc != null) {
			d = mc.dz;
		}
		if (z == 0) {
			return !cmp(getBlockColor(x, y, z),Color.BLACK);
		}
		if (z==d) {
			return !cmp(getBlockColor(x, y, z-1),Color.BLACK);
		}
		Color a = getBlockColor(x, y, z-1);
		Color b = getBlockColor(x, y, z);
		if (cmp(a,Color.BLACK) && cmp(b,Color.BLACK)){
			return false;
		}
		if (cmp(a,Color.BLACK)) {
			return true;
		}
		if (cmp(b,Color.BLACK)){
			return true;
		}
		return a.getAlpha() !=b.getAlpha();
		
	}
	public boolean cmp(Color a,Color b) {
		if (a.getBlue() !=  b.getBlue()) {
			return false;
		}
		if (a.getRed() !=  b.getRed()) {
			return false;
		}
		if (a.getGreen() !=  b.getGreen()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean estPlein(int x, int y) {
		// TODO Auto-generated method stub
		try {
			return this.testCase(ux+x, uy+y, uz);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
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
