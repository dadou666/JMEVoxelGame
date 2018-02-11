package ihm;

import java.util.ArrayList;
import java.util.List;

public class CompositeY<T> extends Composite<T> {

	public void space(int value) {
		Space<T> space = new Space<T>();
		space.width = 0;
		space.height = value;
		this.add(space);
	}

	public void computePosition(int x, int y) {
		this.x = x;
		this.y = y;
		for (Element<T> e : elements) {
			e.x = x;
			e.y = y;
			e.computePosition(x, y);
			y += e.height;
		}

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		width = 0;
		for (Element<T> e : elements) {
			int tmp = e.getWidth();
			if (tmp > width) {
				width = tmp;
			}
		}
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		height = 0;
		for (Element<T> e : elements) {
			height += e.getHeight();
		}
		return height;
	}
}
