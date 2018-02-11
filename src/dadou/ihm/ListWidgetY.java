package dadou.ihm;

public class ListWidgetY extends ListWidget {
	@Override
	public void computeLocation(int x,int y) {
		// TODO Auto-generated method stub
		for(Element e:elements){
			e.computeLocation(x, y);
			y+= e.getHeight();
		}
		
		
	}
	@Override
	public int getWidth() {
		int width = elements.get(0).getWidth();
		for(Element e:this.elements) {
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
		int height = 0;
		for(Element e:this.elements) {
			height += e.getHeight();
		}
		return height;
	}

	@Override
	public int getWidthForSpaceSize(int size) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeightForSpaceSize(int size) {
		// TODO Auto-generated method stub
		return size;
	}

}
