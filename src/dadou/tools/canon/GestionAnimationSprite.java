package dadou.tools.canon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Game;
import dadou.Habillage;
import dadou.Shader;
import dadou.Texture2D;
import dadou.VBOTexture2D;

public class GestionAnimationSprite extends GestionExplosion {
	public Map<String, ParamAnimationSprite> mapParamAnimationSprite = new HashMap<>();
	SpriteAnimation enCours;
	SpriteAnimation libre;
	VBOTexture2D vbo;
	public Habillage h;

	public GestionAnimationSprite(Habillage h) {
		vbo = new VBOTexture2D(Game.shaderSprite);

		vbo.addCoordTexture2D(0.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 1.0f);
		vbo.addCoordTexture2D(1.0f, 0.0f);
		vbo.addCoordTexture2D(0.0f, 0.0f);

		vbo.addVertex(-0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, -0.5f, 0.0f);
		vbo.addVertex(0.5f, 0.5f, 0.0f);
		vbo.addVertex(-0.5f, 0.5f, 0.0f);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		this.h = h;
		h.creerVoxelTexture3D(true);

	}

	public ParamAnimationSprite donnerParamAnimationSprite(String nom) {
		ParamAnimationSprite pas = mapParamAnimationSprite.get(nom);
		if (pas == null) {
			pas = new ParamAnimationSprite();
			mapParamAnimationSprite.put(nom, pas);
		}
		return pas;
	}

	public void dessiner(Camera cam) {
		SpriteAnimation tmp = enCours;
		enCours = null;
		SpriteAnimation u = null;
		SpriteAnimation dernier = null;
		while (tmp != null) {
			u = tmp.suivant;
			tmp.dessiner(cam);
			if (tmp.process()) {
				if (enCours == null) {
					enCours = tmp;
					dernier = tmp;
				} else {
					dernier.suivant = tmp;
					dernier = tmp;
				}

			} else {
				tmp.suivant = libre;
				libre = tmp;
			}
			tmp = u;
		}
		if (dernier != null) {
			dernier.suivant = null;
		}

	}

	public void explosion(String color, Vector3f center) {
		SpriteAnimation tmp;
		if (libre == null) {
			tmp = new SpriteAnimation(vbo,h);

		} else {
			tmp = libre;
			libre = libre.suivant;

		}
		ParamAnimationSprite pas = this.mapParamAnimationSprite.get(color);
		tmp.init(center, pas.echelle, pas.etapes);

		tmp.suivant = enCours;
		enCours = tmp;
	}

}
