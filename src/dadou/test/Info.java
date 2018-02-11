package dadou.test;

import java.awt.Color;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;

import dadou.Game;
import dadou.OpenGLTools;
import dadou.ihm.IHM;
import dadou.ihm.Widget;
import dadou.tools.BrickEditor;

public class Info {
	Widget trie;
	Widget cache;
	Widget ignore;
	Widget total;
	Widget fps;
	Widget fpsGlobal;
	Widget fpsMesure;
	Widget boxWorld;
	Widget modelName;
	Widget info;
	Widget nbObjet;
	public String infoText = "";
	Game g;
	public IHM ihm;

	public void modifierWidgets() {
		Widget.drawText(trie, "trie" , Color.RED, Color.white, Color.GREEN);
	/*	Widget.drawText(cache, "cache" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ignore, "ignore" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(total, "total" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fps, "fps", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fpsGlobal, "fpsGlobal" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fpsMesure, "fpsMesure" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(boxWorld, "boxWorld" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(modelName, "modelName" , Color.RED, Color.white, Color.GREEN);
		Widget.drawText(info,
				"info",
				Color.RED, Color.white, Color.GREEN);
		Widget.drawText(nbObjet, "nbObjet" , Color.RED,
				Color.white, Color.GREEN);*/

	}

	public void dessiner() {
	
		ihm.dessiner(g.shaderWidget);
		//OpenGLTools.exitOnGLError("info");

	}

	public Info(Game g) {
		this.g=g;
		
		ihm = g.nouvelleIHM();
		ihm.beginX();
		ihm.space(700);
		
		ihm.beginY();
		ihm.setSize(200, 30);
		/*ihm.ajouterModel(() -> {
			modifierWidgets();
		});*/
		trie = ihm.widget();
		Widget.drawText(trie, "Trie", Color.RED, Color.white, Color.GREEN);
		//modifierWidgets();
	/*	cache = ihm.widget();
		ignore = ihm.widget();
		total = ihm.widget();
		fps = ihm.widget();
		fpsGlobal = ihm.widget();
		fpsMesure = ihm.widget();
		boxWorld = ihm.widget();
		modelName = ihm.widget();
		info = ihm.widget();
		nbObjet = ihm.widget();*/
		ihm.space(850);
		ihm.end();

		ihm.beginY();
		ihm.setSize(115, 30);
		Widget.drawText(ihm.widget(), "Trie", Color.RED, Color.white, Color.GREEN);
		/*Widget.drawText(ihm.widget(), "Cache", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Ignore", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Total", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPS", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPSGLOBAL", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPSMESURE", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "BxWld", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "MdlName", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Info", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "NbObjet", Color.RED, Color.white, Color.GREEN);*/
		ihm.space(850);
		ihm.end();
		
		
		ihm.space(100);
		ihm.end();
		

	}

}
