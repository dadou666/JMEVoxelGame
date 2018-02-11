package dadou.tools.canon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Game;
import dadou.Grappin;
import dadou.Objet3D;
import dadou.Shader;
import dadou.Texture2D;
import dadou.VBOTexture2D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.event.GameEventLeaf;
import dadou.son.Son;
import dadou.tools.BrickEditor;

public class Canon {
	public GestionExplosionSphere gestionExplosionSphere;
	public GestionAnimationSprite gestionAnimationSprite;
	public GestionExplosion gestionExplosion;
	public float vitesse;
	public float distance;
	public boolean traverserObjetTransparent=false;
	public boolean continuerApresImpactSurObjetMobile = false;

	Obus enCours;
	Obus libre;
	public float rayonPourSon;

	int limit;
	int total = 0;
	Texture2D tex;
	Map<String, Texture2D> texProjectiles = new HashMap<>();
	public int tailleObus = 1;
	public CallbackCanon CallbackCanon;
	public String nomProjectile;
	public BrickEditor editor;

	public Canon(BrickEditor editor,  int limit,
			float vitesse, float distance) {

		this.limit = limit;
		this.editor = editor;
		tex = new Texture2D(32, 32);
		Graphics2D g = tex.getGraphics2DForUpdate();
		g.setColor(Color.CYAN);
		g.fillOval(1, 1, 30, 30);
		g.setColor(Color.BLACK);
		int d = 5;
		g.fillOval(1 + d, 1 + d, 30 - 2 * d, 30 - 2 * d);
		tex.update();
		this.texProjectiles.put("cyan", tex);
		this.distance = distance;
		this.vitesse = vitesse;

	}

	public void initialiserProjectile(String nom) {
		Texture2D tex = (this.texProjectiles.get(nom));
		this.nomProjectile = nom;
		if (tex != null) {

			this.tex = tex;
		} else {
			tex = new Texture2D(32, 32);
			Graphics2D g = tex.getGraphics2DForUpdate();
			try {
				g.setColor((Color) Color.class.getField(nom).get(null));
			} catch (IllegalArgumentException | IllegalAccessException
					| NoSuchFieldException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			g.fillOval(1, 1, 30, 30);
			g.setColor(Color.BLACK);
			int d = 5;
			g.fillOval(1 + d, 1 + d, 30 - 2 * d, 30 - 2 * d);
			tex.update();
			this.texProjectiles.put(nom, tex);
			this.tex = tex;

		}

	}

	public boolean tirer(Son son, Vector3f position, Vector3f direction) {
		Obus o = null;
		if (libre == null) {
			if (total == limit) {
				return false;
			}
			o = new Obus();
			o.objet3D = new Objet3D();
			o.objet3D.brique = this.creerParticule();
			total++;

		} else {
			o = libre;

			libre = o.suivant;
		}
		o.sonImpact(son);
		o.direction.set(direction);
		o.direction.multLocal(vitesse);
		o.directionNormal.set(direction);
		o.distance = 0.0f;
		o.objet3D.positionToZero();
		o.objet3D.translation(position);
		o.origine.set(position);
		o.oldPos.set(position);
		o.suivant = enCours;
		o.collisions.clear();

		o.nomProjectile = nomProjectile;
		if (o.nomProjectile == null) {
			o.nomProjectile = "WHITE";
		}
	
		enCours = o;
		return true;

	}

	public void saveOldPos(Vector3f oldPos) {
		if (CallbackCanon == null) {
			return;
		}
		CallbackCanon.saveOldPos(oldPos);
	}

	public boolean CallbackCanon(Obus obus) throws CouleurErreur {
		if (CallbackCanon == null) {
			return false;
		}
		return CallbackCanon.process(obus);
	}

	public void reset() {
		enCours = null;
		libre = null;
	}

	public void dessiner(Camera cam) throws CouleurErreur {
		Obus obus = enCours;

		enCours = null;
		Obus dernier = null;
		Obus u = null;
		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Game.shaderParticule.use();
		tex.bind();
		Game.shaderParticule.glUniformfARB("up", up.x, up.y, up.z);
		Game.shaderParticule.glUniformfARB("left", left.x, left.y, left.z);
		Game.shaderParticule.glUniformfARB("size", 1);
		while (obus != null) {
			u = obus.suivant;
			if (obus.distance > 4) {
				obus.objet3D.dessiner(cam);
			}
			obus.distance += vitesse;
			obus.oldPos.set(obus.objet3D.getTranslationGlobal());
			obus.objet3D.translation(obus.direction);
			boolean b = CallbackCanon(obus);
			if (obus.distance > distance || b) {
				total--;
				if (gestionExplosion != null) {

					gestionExplosion.explosion(obus.nomProjectile, obus.oldPos);
					if (obus.distance <= distance) {
					
						obus.demarerSonImpact(cam.getLocation(),
								this.rayonPourSon);
						
					} else {
						
						if (editor.mondeInterface.active
								
								&& !b) {
							editor.decor.DecorDeBriqueData.finTireSansCollision(this.editor.mondeInterface);
							
						}
					}

				}
				obus.suivant = libre;
				libre = obus;
			} else {
				if (enCours == null) {
					enCours = obus;
					dernier = obus;
				} else {
					dernier.suivant = obus;
					dernier = obus;

				}

			}
			obus = u;

		}
		if (dernier != null) {
			dernier.suivant = null;
		}

		if (CallbackCanon != null) {
			CallbackCanon.finish();
		}
		if (gestionExplosion != null) {
			gestionExplosion.dessiner(cam);
		}

	}

	public VBOTexture2D creerParticule() {
		VBOTexture2D vbo = new VBOTexture2D(Game.shaderParticule);

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
		return vbo;

	}

}
