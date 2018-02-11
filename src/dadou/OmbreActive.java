package dadou;

import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class OmbreActive extends EtatOmbre {
	public void modifierModeRendu(BrickEditor be) throws InterruptedException, Exception {
		Game.rm = Game.RenderMode.Shadow;
		if (FBOShadowMap.shadowMap == null) {
			FBOShadowMap.shadowMap = new FBOShadowMap();
			FBOShadowMap.shadowMap.init(Game.shadowMapWidth, Game.shadowMapHeight, be.game);
		}
		be.modifierOmbre();
		

	}
	public Game.RenderMode  renderMode() {
		return Game.RenderMode.Shadow;
	}
}
