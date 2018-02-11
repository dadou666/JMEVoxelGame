package dadou.ihm;

import dadou.Game;
import dadou.Shader;

public class Space implements Element {
public int size;
public ListWidget parent;
@Override
public ListWidget getParent() {
	// TODO Auto-generated method stub
	return parent;
}
@Override
public void computeLocation(int x,int y) {
	// TODO Auto-generated method stub
	
}

@Override
public int getWidth() {
	// TODO Auto-generated method stub
	return parent.getWidthForSpaceSize(size);
}
@Override
public int getHeight() {
	// TODO Auto-generated method stub
	return parent.getHeightForSpaceSize(size);
}

@Override
public void buildVBO(Shader shader, int width, int height) {
	// TODO Auto-generated method stub
	
}
public void dessiner(Shader widgetShader) {
	
	
}
@Override
public Widget getWidgetAt(int x, int y) {
	// TODO Auto-generated method stub
	return null;
}
@Override
public void echelle(float echelle) {
	// TODO Auto-generated method stub
	
}

}
