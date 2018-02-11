package dadou.tools.construction;

import java.awt.Color;

public class AnnulerColler {
	Color[][][] colors;
	public int x,y,z,dx,dy,dz;
	
	public AnnulerColler(Color [][][] colors,int x,int y,int z,int dx,int dy,int dz) {
		this.colors =colors;
		this.x=x;
		this.y=y;
		this.z=z;
		this.dx=dx;
		this.dy=dy;
		this.dz=dz;
	}
}
