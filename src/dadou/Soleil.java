package dadou;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.tools.BrickEditor;

public class Soleil {
	public Vector3f position = new Vector3f();
	public static boolean debug= false;
	public float theta;
	public Vector3f camPos ;
	public Vector3f camDir ;

	public float phi= FastMath.PI/4.0f;
	public float distance;
	public float vitesse=0.00005f;
	static public float coeff = FastMath.sqrt(3);
	public Objet3D source;
	public void creerSource() {
		float dim=0.5f;
		VBOTexture2D vbo = new VBOTexture2D(Game.shaderSoleil);
		vbo.addVertex(-dim, -dim, 0.00f);
		vbo.addVertex(-dim, dim, 0.0f);
		vbo.addVertex(dim, dim, 0.0f);
		vbo.addVertex(dim, -dim, 0.0f);

		vbo.addCoordTexture2D(0, 0);
		vbo.addCoordTexture2D(0, 1);
		vbo.addCoordTexture2D(1, 1);
		vbo.addCoordTexture2D(1, 0);
		vbo.addTri(0, 1, 2);
		vbo.addTri(0, 2, 3);
		vbo.createVBO();
		source = new Objet3D();
		source.brique = vbo;
		source.translation(position);
	}

	public void init(ShadowData sd) {
		sd.init(position, distance, phi, theta);
		camPos = sd.location;
		camDir =sd.direction;
	}

	public void init(BrickEditor be) {
		float dim = be.decor.DecorDeBriqueData.decorInfo.nbCube;
		position.set(0,0.75f*dim, 0);
		//position.set(0,0.5f*dim, 0);
		theta = FastMath.PI / 5.0f;
		//phi+=vitesse;
		distance =  1.25f*dim;
		//distance =  dim;
	
	}
	public void dessinerLumiere(Camera cam) {
		if (camPos == null) {
			return;
		}
		if (source == null) {
			this.creerSource();
		}
		source.positionToZero();
	//	source.translation(camPos);
		Vector3f d =camDir;
		float u=070.0f;
	//	source.translation(u*d.x, u*d.y, u*d.z);
	
		Game.shaderSoleil.use();
		Game.shaderSoleil.glUniformfARB("size", 300);
		Game.shaderSoleil.glUniformfARB("position", camPos.x+u*d.x, camPos.y+u*d.y,camPos.z+u*d.z);
		Game.shaderSoleil.glUniformmat4ARB("modelView", BrickEditor.modelViewMatrix);
		source.dessiner(cam);
		
	}
	
	
	

}
