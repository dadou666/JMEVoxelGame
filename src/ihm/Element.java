package ihm;

import javax.swing.JFrame;

public abstract class Element<T> {
	public Composite<T> parent;
	public int x=0;
	public int y=0;
	public int width=0;
	public int height=0;
	public void computePosition(int x,int y) {
		
	}
	public void computePosition() {
		width = this.getWidth();
	    height = this.getHeight();
		this.computePosition(0, 0);
		
	}
	
	public void build(UIBuilder<T> builder) {
		
	}
	abstract public int getWidth() ;
	abstract public int getHeight() ;
}
