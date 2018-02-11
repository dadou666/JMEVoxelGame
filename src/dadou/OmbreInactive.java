package dadou;

import dadou.tools.BrickEditor;

public class OmbreInactive extends EtatOmbre {
	public void modifierModeRendu(BrickEditor be) {
		Game.rm=Game.RenderMode.Normal;
		
	}
	public Game.RenderMode  renderMode() {
		return Game.RenderMode.Normal;
	}
}
