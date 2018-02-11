package dadou.tools.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import dadou.BriqueAvecTexture3D;
import dadou.Button;
import dadou.FBO;
import dadou.Game;
import dadou.Habillage;
import dadou.Icone;
import dadou.Log;
import dadou.ModelClasse;
import dadou.Objet3D;
import dadou.ObjetMobile;
import dadou.VBOBriqueTexture3D;
import dadou.VBOMinimunPourBriqueAvecTexture3D;
import dadou.VoxelTexture3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.ChoixGenerique;
import dadou.tools.ElementChoix;
import dadou.tools.EtatBrickEditor;

public class ChoixKube extends ChoixGenerique<Color> {

	public ChoixKube(Habillage h, Game game, FBO fbo, boolean estModelPlanche) throws Exception {
		List<ElementChoix<Color>> r = new ArrayList<>();
		try {
			
			List<String> valeurs = new ArrayList<>();
			for(String valeur:h.valeurs.keySet()) {
				if (valeur != null) {
					valeurs.add(valeur);
				}
			}
			
			java.util.Collections.sort(valeurs);
			Log.print(valeurs);
			ElementChoix<Color> ec = new ElementChoix<>();
			ec.valeur = Color.BLACK;
			r.add(ec);
			for (String nom : valeurs) {
				ec = new ElementChoix<>();
				ec.valeur = h.valeurs.get(nom);
				ModelClasse mc = this.creerModelClasse(ec.valeur);
				mc.estModelPlanche =estModelPlanche;
				Objet3D om = this.creerObjet(mc, game, h);
				ec.a = 3.14f / 4.0f;
				ec.obj = om;
				ec.distance = mc.size.length()*1.5f;
				r.add(ec);

			}
			this.liste.addAll(r);
			init(4, 4, fbo, game);
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<ElementChoix<Color>> calculerListe(Habillage h, Game game) {
		List<ElementChoix<Color>> r = new ArrayList<>();
		try {
			
			List<String> valeurs = new ArrayList<>();
			valeurs.add(null);
			valeurs.addAll(h.valeurs.keySet());
			java.util.Collections.sort(valeurs);
			Log.print(valeurs);
			ElementChoix<Color> ec = new ElementChoix<>();
			ec.valeur = Color.BLACK;
			r.add(ec);
			for (String nom : valeurs) {
				ec = new ElementChoix<>();
				ec.valeur = h.valeurs.get(nom);
				ModelClasse mc = this.creerModelClasse(ec.valeur);
				Objet3D om = this.creerObjet(mc, game, h);

				ec.obj = om;
				r.add(ec);

			}
		} catch (CouleurErreur e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	public ModelClasse creerModelClasse(Color color) throws CouleurErreur, Exception {

		Color copie[][][] = new Color[1][1][1];
		copie[0][0][0] = color;
		ModelClasse mc = new ModelClasse();
		mc.copie = copie;
		mc.dx = 1;
		mc.dy = 1;
		mc.dz = 1;
		// ModelClasse mc = decorDeBriqueData.models.get("objet.arme");

		return mc;

	}

	public Objet3D creerObjet(ModelClasse mc, Game g, Habillage h) throws CouleurErreur, Exception {

		if (mc.vbo == null) {
			mc.initBuffer(g, true, h);
		}
		Vector3f size = mc.size.clone();
		VBOBriqueTexture3D vbo = mc.vbo;
		VoxelTexture3D tex = mc.tex;
		Objet3D obj = new Objet3D();
		Objet3D translation = new Objet3D();

		BriqueAvecTexture3D brique = vbo.creerBriqueAvecTexture3D(tex);
		translation.brique = brique;
		brique.echelle = 1.0f;
		brique.tpos.set(0, 0, 0);

		Vector3f t = size.mult(-0.5f);
		translation.translation(t);
		obj.ajouter(translation);

		return obj;

	}

	public void gerer() throws CouleurErreur {
		super.gerer();
		if (quitter && BrickEditor != null) {
			BrickEditor.etat = BrickEditor.selection;
			BrickEditor.processMenuKey =true;
			BrickEditor.menuCouleur.selected = false;
			BrickEditor.menuCouleur.updateText();
			return;
			
		}
		if (BrickEditor != null && choix != null) {
			BrickEditor.processMenuKey =true;
			BrickEditor.etat = BrickEditor.selection;
			BrickEditor.menuCouleur.selected = false;
			BrickEditor.menuCouleur.updateText();
		}
	}

}
