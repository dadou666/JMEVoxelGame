package dadou.tools.canon;

import java.awt.Color;
import java.awt.Graphics2D;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Game;
import dadou.Objet3D;
import dadou.Shader;
import dadou.SphereGenerateur;
import dadou.Texture2D;
import dadou.VBOTexture2D;

public class ExplosionSphere{
	Texture2D tex;
	SphereGenerateur generateur;
	ExplosionSphere suivant;

	VBOTexture2D vbo;
	float size = 0.2f;
	Objet3D obj;
	Vector3f center = new Vector3f();
	float speed;

	public float distance;
	public float distanceMax;

	public ExplosionSphere( int n) {

		generateur = new SphereGenerateur(1.0f, n);
	
		obj = new Objet3D();
		this.creerParticule();
		obj.brique = vbo;

	}

	public void creerParticule() {
		vbo = new VBOTexture2D(Game.shaderParticule);

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

	}

	public void init(Vector3f center, float distanceMax, float speed) {
		this.center.set(center);
		this.speed = speed;
		this.distanceMax = distanceMax;
		this.distance = 0.0f;
	}

	public boolean process() {
		distance += speed;
		return distance < distanceMax;

	}

	public void dessiner(Camera cam) {
		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Game.shaderParticule.use();
		tex.bind();
		Game.shaderParticule.glUniformfARB("up", up.x, up.y, up.z);
		Game.shaderParticule.glUniformfARB("left", left.x, left.y, left.z);
		Game.shaderParticule.glUniformfARB("size", (1 - distance / distanceMax)
				* size);
		for (Vector3f p : this.generateur.points) {
			obj.positionToZero();
			obj.translation(center);
			obj.translation(p.x * distance, p.y * distance, p.z * distance);
			obj.dessiner(cam);

		}

	}
}
