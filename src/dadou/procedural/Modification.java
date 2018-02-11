package dadou.procedural;

public class Modification {
	public int x;
	public int y;
	public int z;
	public String type;
	public Modification() {
		
	}
	public Modification(int x,int y,int z,MondeGenere mg) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.type =mg.contenu[x][y][z];
		
	}
	public String nom() {
		return "" + x + "," + y + "," + z;
	}
}
