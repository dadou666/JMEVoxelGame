package dadou.tools.canon;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Game;
import dadou.Habillage;
import dadou.Objet3D;
import dadou.VBOTexture2D;

public class SpriteAnimation {
	public SpriteAnimation suivant;
	float echelle;
	Objet3D obj;
	Habillage h;
	int etapes[];
	int idx;
	VBOTexture2D vbo;
	public SpriteAnimation(VBOTexture2D vbo,Habillage h) {
		this.vbo = vbo;
		this.h=h;
		obj = new Objet3D();
		obj.brique = vbo;
	}
	
	public void dessiner(Camera cam) {
		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Game.shaderSprite.use();

		Game.shaderSprite.glUniformfARB("up", up.x, up.y, up.z);
		Game.shaderSprite.glUniformfARB("left", left.x, left.y, left.z);
		h.creerVoxelTexture3D().bindTexture();
		
		Game.shaderSprite.glUniformfARB("size", echelle);
		Game.shaderSprite.glUniformiARB("habillage", 0);
		Game.shaderSprite.glUniformfARB("idx", ((float)etapes[idx])/255.0f);
		obj.dessiner(cam);
	}
	public boolean process() {
		idx++;
		if (idx >= etapes.length) {
			return false;
		}
		return true;
	}
	public void init(Vector3f center, float echelle,int etapes[]) {
		idx = 0;
		this.etapes = etapes;
		this.echelle = echelle;
		obj.positionToZero();
		obj.translation(center);
		
	}
}
