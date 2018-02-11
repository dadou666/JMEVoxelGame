package ihm;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Component<T> extends Element<T> {

	public T component;

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return height;
	}

	public void computePosition(int x, int y) {
		this.x = x;
		this.y = y;
		// System.out.println( "component("+x+","+y+")");

	}

	public void build(UIBuilder<T> builder) {
		builder.buildFrom(this);
	}

}
