package dadou.ihm;

public class ListWidgetX extends ListWidget {
	@Override
	public void computeLocation(int x,int y) {
		// TODO Auto-generated method stub
		for(Element e:elements){
			e.computeLocation(x, y);
			x+= e.getWidth();
		}
		
		
	}
	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		int width = 0;
		for(Element e:this.elements) {
			width += e.getWidth();
		}
		return width;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		int height = elements.get(0).getHeight();
		for(Element e:this.elements) {
			int tmp = e.getHeight();
			if (tmp > height) {
				height = tmp; 
			}
		}
		
		return height;
	}

	@Override
	public int getWidthForSpaceSize(int size) {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public int getHeightForSpaceSize(int size) {
		// TODO Auto-generated method stub
		return 0;
	}
}
