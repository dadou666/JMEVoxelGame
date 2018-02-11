package dadou.ihm;

import dadou.Game;
import dadou.Shader;

public interface Element {
	public ListWidget getParent();

	public void computeLocation(int x, int y);

	public void buildVBO(Shader shader,int width, int height);

	public int getWidth();

	public int getHeight();
	public void dessiner(Shader widgetShader) ;
	public Widget getWidgetAt(int x,int y);
	public void echelle(float echelle);
}
