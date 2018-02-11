package ihm;

import java.util.ArrayList;
import java.util.List;

public class CompositeX<T> extends Composite<T> {
	public void space(int value) {
		Space space = new Space();
		space.width= value;
		space.height=0;
		this.add(space);
	}

	public void computePosition(int x, int y) {
		this.x = x;
		this.y = y;
		for (Element<T> e : elements) {
			e.x = x;
			e.y = y;
			e.computePosition(x, y);
			x += e.width;
		}

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		 width = 0;
		 
		for(Element<T> e:elements) {
			width+=e.getWidth();
		}
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		 height = 0;
		for(Element<T> e:elements) {
			int tmp = e.getHeight();
			if (tmp > height) {
				height=tmp;
			}
		}
		return height;
		
	}

}
