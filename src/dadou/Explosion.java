package dadou;

import java.awt.Color;
import java.awt.Graphics2D;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

public class Explosion {
	Texture2D tex ;
	SphereGenerateur generateur;
	Shader shaderParticule;
	VBOTexture2D vbo ;
	Objet3D obj;
	Vector3f center;
	public float distance;
	public float distanceMax;
	public Explosion(Shader shaderParticule,int n,Shader shader) {
		this.shaderParticule = shaderParticule;
		generateur = new SphereGenerateur(1.0f, n);
		 tex = new Texture2D(32,32);
			Graphics2D g = tex.getGraphics2DForUpdate();
			g.setColor(Color.GREEN);
			g.fillOval(1, 1, 30, 30);
			
			tex.update();
			obj = new Objet3D();
			this.creerParticule();
			obj.brique = vbo;
		
	}
	public void creerParticule() {
		vbo = new VBOTexture2D(shaderParticule);

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
	public void init(Vector3f center,float distanceMax)  {
		this.center =center;
		this.distanceMax =distanceMax;
	}
	public boolean process(float speed) {
		distance+= speed;
		return distance < distanceMax;
			
		
	}
	public void dessiner(Camera cam) {
		Vector3f up = cam.getUp();
		Vector3f left =cam.getLeft();
		shaderParticule.use();
		tex.bind();
		shaderParticule.glUniformfARB("up", up.x, up.y, up.z);
		shaderParticule.glUniformfARB("left", left.x, left.y, left.z);
		shaderParticule.glUniformfARB("size", 0.01f);
		for(Vector3f p:this.generateur.points) {
			obj.positionToZero();
			obj.translation(center);
			obj.translation(p.x*distance,p.y*distance,p.z*distance);
			obj.dessiner(cam);
			
			
		}
		
	}
}
