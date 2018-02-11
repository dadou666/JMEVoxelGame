package dadou.tools.construction;

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

import dadou.BriqueAvecTexture3D;
import dadou.Button;
import dadou.FBO;
import dadou.Game;
import dadou.Habillage;
import dadou.Icone;
import dadou.ModelClasse;
import dadou.ModelInstance;
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

public class ChoixModelClasse extends ChoixGenerique<ModelClasse> {
	String nomHabillage;
	dadou.tools.BrickEditor be;
	public float distCoeff=1.5f;

	public ChoixModelClasse(dadou.tools.BrickEditor be, FBO fbo, String nomHabillage) throws Exception {
		this.nomHabillage = nomHabillage;

		this.be = be;

		this.modifierListe();
		this.init(3, 5, fbo, be.game);

	}
	
	public ChoixModelClasse(dadou.tools.BrickEditor be, FBO fbo, List<String> noms) throws Exception {
		

		this.be = be;

		this.modifierListe(noms);
		this.init(3, 5, fbo, be.game);

	}
	public ChoixModelClasse(dadou.tools.BrickEditor be, FBO fbo, List<String> noms,int dx,int dy,float distCoeff) throws Exception {
		

		this.be = be;
		this.distCoeff = distCoeff;

		this.modifierListe(noms);
		this.init(dx, dy, fbo, be.game);

	}
	public boolean modifierListe =  false;

	public void modifierListe() throws CouleurErreur, Exception {
		Habillage h = be.donnerHabillage(nomHabillage);
		this.liste.clear();
	
		modifierListe =  false;
	

		List<ElementChoix<ModelClasse>> valeurs = new ArrayList<>();

		for (ModelClasse mc : be.decorDeBriqueData.models.values()) {
			if (mc.estElementDecor() && cmp(nomHabillage, mc.nomHabillage) && mc.vbo != null) {
				ElementChoix<ModelClasse> ec = new ElementChoix<>();
				Objet3D om = this.creerObjet(mc, be.game, h);
				ec.obj = om;
				ec.a = 3.14f / 4.0f;
				ec.distance = mc.size.length() * distCoeff;
				ec.valeur = mc;
				valeurs.add(ec);
			}
		}
		this.liste.addAll(valeurs);

	}
	public void modifierListe(List<String> noms) throws CouleurErreur, Exception {
	
		this.liste.clear();
	
		modifierListe =  false;
	

		List<ElementChoix<ModelClasse>> valeurs = new ArrayList<>();

		for (String nom:noms) {
			ModelClasse mc = be.decorDeBriqueData.models.get(nom);
			
			
			if ( mc.vbo != null) {
				Habillage h = be.donnerHabillage(mc.nomHabillage);
				ElementChoix<ModelClasse> ec = new ElementChoix<>();
				Objet3D om = this.creerObjet(mc, be.game, h);
				ec.obj = om;
				ec.a = 3.14f / 4.0f;
				ec.distance = mc.size.length() * distCoeff;
				ec.valeur = mc;
				valeurs.add(ec);
			}
		}
		this.liste.addAll(valeurs);

	}
	public void modifier() {
		if (this.modifierListe) {
			try {
				this.modifierListe();
				this.modifierTableau();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.modifier();
	}
	public boolean cmp(String a, String b) {
		if (a == b) {
			return true;
		}
		if (a == null) {
			return false;
		}
		if (b == null) {
			return false;
		}
		return a.equals(b);
	}

	public FBO fbo;
	int marge = 6;

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

		if (choix != null) {

			if (BrickEditor != null) {
				ModelClasse mc = choix.valeur;
				if (mc != null) {
					BrickEditor.etat = BrickEditor.selection;
					
					BrickEditor.selection.BoxColler.modelInstance = new ModelInstance(0, 0, 0, mc);
					;

					if (mc.echelle == 0.0f) {
						mc.echelle = 1.0f;
					}
				
					BrickEditor.selection.BoxSelectionAction = BrickEditor.selection.BoxColler;
					BrickEditor.selection.EtatBoxSelection = new AfficherBoxSelection(mc.dx, mc.dy, mc.dz, mc.echelle, mc);
					BrickEditor.etat = BrickEditor.selection;

				} else {
					BrickEditor.menuColler.selected = false;
					BrickEditor.menuColler.updateText();
				}
				BrickEditor.processMenuKey = true;
				BrickEditor.etat = BrickEditor.selection;

			}
			return;
		}
		if (quitter && BrickEditor != null) {
			BrickEditor.processMenuKey = true;
			BrickEditor.etat = BrickEditor.selection;
			BrickEditor.menuColler.updateText();
			return;
		}
	}

}
