package dadou.ihm;

import java.util.ArrayList;
import java.util.List;

import dadou.Game;
import dadou.Shader;

public abstract class ListWidget implements Element {

	public int x, y;
	public ListWidget parent;

	public List<Element> elements = new ArrayList<Element>();

	@Override
	public ListWidget getParent() {
		// TODO Auto-generated method stub
		return parent;
	}
	public void echelle(float echelle) {
		for(Element e:elements) {
			e.echelle(echelle);
		}
		
	}
	public void computeLocation() {
		this.computeLocation(0, 0);

	}
	public Widget getWidgetAt(int x,int y) {
		for(Element e:elements) {
			Widget r = e.getWidgetAt(x, y);
			if (r != null) {
				return r;
			}
		}
		return null;
	}
	public void buildVBO(Shader shader,int width,int height) {
		for(Element e:elements){
			e.buildVBO(shader,width, height);
		}
		
	}
	public void buildVBO(Shader shader) {
		this.buildVBO(shader,getWidth(),getHeight());
	}
	abstract public int getWidthForSpaceSize(int size);

	abstract public int getHeightForSpaceSize(int size);
	public void dessiner(Shader widgetShader) {
		for(Element element:elements) {
			element.dessiner(widgetShader);
		}
		
		
	}

}
