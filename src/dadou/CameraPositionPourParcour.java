package dadou;

import java.io.Serializable;

public class CameraPositionPourParcour implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6411051237509926699L;
	public CameraPosition cp;
	public int n;
	public CameraPositionPourParcour(CameraPosition cp,int n) {
		this.cp=cp;
		this.n=n;
	}

}
