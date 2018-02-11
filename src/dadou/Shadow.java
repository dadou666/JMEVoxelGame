package dadou;

public class Shadow {
	public FBOShadowMap shadowMapDecor;
	public boolean calculerShadowMapDecor = false;
	public FBOShadowMap shadowMapObjet;
	public FBOShadowMapFusion shadowMapFusion;
	public FBOShadowMap courant;
	public int shadowTextureID() {
		//return shadowMapFusion.shadowTextureID;
		return shadowMapObjet.shadowTextureID;
	}

	public void supprimer() {
		shadowMapDecor.supprimer();
		shadowMapObjet.supprimer();
		shadowMapFusion.supprimer();

	}
	public FBOShadowMap	shadowMapDecor() {
		courant = shadowMapDecor;
		return shadowMapDecor;
		
	}
	public FBOShadowMap	shadowMapObjet() {
		courant = shadowMapObjet;
		return shadowMapObjet;
		
	}
	public Shadow(int width, int height, Game game) {
		shadowMapDecor = new FBOShadowMap();
		shadowMapDecor.init(width, height, game);
		shadowMapObjet = new FBOShadowMap();
		shadowMapObjet.init(width, height, game);
		courant = shadowMapObjet;
		shadowMapFusion = new FBOShadowMapFusion();
		shadowMapFusion.init(width, height, game);
	}
	public void fusionner() {
		shadowMapFusion.activer();
		shadowMapFusion.fusionner(shadowMapObjet, shadowMapDecor);
		shadowMapFusion.desactiver();
	}

}
