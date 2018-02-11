package dadou.tools.canon;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Shader;

public class Laser {
	public Vector3f debut = new Vector3f();
	public Vector3f fin = new Vector3f();
	public Vector3f dir = new Vector3f();
	public float distance;
	public Vector3f colorVect = new Vector3f();
	
	public void setColor(Color color) {
		colorVect.set(color.getRed(), color.getGreen(), color.getBlue());
		colorVect.divideLocal(255.0f);
		
	}
	public Laser suivant;
	public void dessiner(Shader shader,Camera cam) {
		GL11.glPushMatrix();
		shader.use();
		
		shader.glUniformfARB("color", colorVect.x, colorVect.y,colorVect.z,1.0f);

		GL11.glLineWidth(4.5f);
//		 GL11.glPointSize(13.0f);
		Vector3f left = cam.getLeft();
		GL11.glBegin(GL11.GL_LINES);
		

		GL11.glVertex3f(debut.x, debut.y, debut.z);
		//GL11.glVertex3f(debut.x+left.x*3, debut.y+left.y*3, debut.z+left.z*3);
		GL11.glVertex3f(fin.x, fin.y, fin.z);
		//GL11.glVertex3f(fin.x+left.x*3, fin.y+left.y*3, fin.z+left.z*3);
		GL11.glEnd();
		GL11.glPopMatrix();
		
	}
	public boolean process(float speed,float distanceMax) {
		distance += speed;
		
		fin.addLocal(dir);
	//	debut.addLocal(dir);
//	System.out.println("fin="+fin.subtract(debut));
		
		return distance < distanceMax;
		
	}
	

}
