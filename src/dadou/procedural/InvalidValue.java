package dadou.procedural;

public class InvalidValue extends Exception {
	public int x,y,z;
	public InvalidValue(int x,int y,int z) {
		this.x=x;
		this.y=y;
		this.z=z;
	}
	public String getMessage() {
		return "InvalidValue "+x+","+y+","+z;
	}

}
