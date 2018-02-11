package dadou.tools.canon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Shader;
import dadou.Texture2D;

public class GestionExplosionSphere extends GestionExplosion{
	public Map<String, ParamExplosionSphere> mapParamExplosionSphere = new HashMap<String, ParamExplosionSphere>();
	ExplosionSphere enCours;
	ExplosionSphere libre;
	public int n;

	public GestionExplosionSphere(int n) {
		this.n = n;

	}

	public ParamExplosionSphere donnerParamExplosionSphere(String color, Color c) {
		ParamExplosionSphere p;
		p = this.mapParamExplosionSphere.get(color);
		if (p != null) {
			return p;
		} 
			p = new ParamExplosionSphere();
			p.distance = 1.5f;
			p.vitesse = 0.05f;

			p.tex = new Texture2D(32, 32);
			Graphics2D g = p.tex.getGraphics2DForUpdate();
			g.setColor(c);
			g.fillOval(1, 1, 30, 30);

			p.tex.update();

			mapParamExplosionSphere.put(color, p);
		
		return p;

	}

	public ParamExplosionSphere donnerParamExplosionSphere(String color) {
		ParamExplosionSphere p;
		p = this.mapParamExplosionSphere.get(color);
		if (p != null) {
			return p;
		} else {

			Color c = Color.WHITE;
			try {
				c = (Color) Color.class.getField(color).get(null);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.donnerParamExplosionSphere(color, c);
		}
		

	}

	public void dessiner(Camera cam) {
		ExplosionSphere tmp = enCours;
		enCours = null;
		ExplosionSphere u = null;
		ExplosionSphere dernier = null;
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
		ExplosionSphere tmp;
		if (libre == null) {
			tmp = new ExplosionSphere(n);

		} else {
			tmp = libre;
			libre = libre.suivant;

		}
		ParamExplosionSphere pes = this.donnerParamExplosionSphere(color);
		tmp.init(center, pes.distance, pes.vitesse);
		tmp.tex = pes.tex;
		tmp.suivant = enCours;
		enCours = tmp;
	}

}
