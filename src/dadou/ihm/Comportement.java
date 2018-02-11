package dadou.ihm;

import dadou.Game;

public interface Comportement {
	public void init(Game game) throws Exception;
	public void loop(Game game) throws Exception;
	public void resize(int width,int height) ;
	
	public void terminate(Game game);

}
