package dadou.tools.graphics;

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
	Widget modelType;
	Widget modelName;
	Widget info;
	Widget nbObjet;
	public String infoText = "";
	BrickEditor b;
	IHM ihm;

	public void modifierWidgets() {
		Widget.drawText(trie, "" + b.decor.action.totalTrie, Color.RED, Color.white, Color.GREEN);
		Widget.drawText(cache, "" + b.decor.action.totalOctreeCache, Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ignore, "" + b.decor.action.totalOctreeIngnore, Color.RED, Color.white, Color.GREEN);
		Widget.drawText(total, "" + b.decor.action.totalOctreeDansCamera, Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fps, "" + Game.fps.getResult(), Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fpsGlobal, "" + Game.fpsGlobal.getResult(), Color.RED, Color.white, Color.GREEN);
		Widget.drawText(fpsMesure, "" + Game.fpsMesure.getResult(), Color.RED, Color.white, Color.GREEN);
		Widget.drawText(boxWorld, "" + b.decor.action.worldBoxVisible, Color.RED, Color.white, Color.GREEN);
		Widget.drawText(modelName, "" + b.modelName + "{" + b.echelle + "}", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(modelType, "" + b.modelType , Color.RED, Color.white, Color.GREEN);
		int dim =b.selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		
		Widget.drawText(info,
				"" + infoText,
				Color.RED, Color.white, Color.GREEN);
		Widget.drawText(nbObjet, "" + b.decor.action.nbInvisible + ":" + b.decor.action.nbVisible, Color.RED,
				Color.white, Color.GREEN);

	}

	public void dessiner() {
	
		ihm.dessiner(b.game.shaderWidget);
		//OpenGLTools.exitOnGLError("info");

	}

	public Info(BrickEditor b) {
		this.b = b;
		ihm = b.game.nouvelleIHM();
		ihm.beginX();
		ihm.space(700);
		
		ihm.beginY();
		ihm.setSize(350, 32);
		ihm.ajouterModel(() -> {
			modifierWidgets();
		});
		trie = ihm.widget();
	
		cache = ihm.widget();
		ignore = ihm.widget();
		total = ihm.widget();
		fps = ihm.widget();
		fpsGlobal = ihm.widget();
		fpsMesure = ihm.widget();
		boxWorld = ihm.widget();
		modelName = ihm.widget();
		modelType =ihm.widget();
		info = ihm.widget();
		nbObjet = ihm.widget();
		ihm.space(850);
		ihm.end();

		ihm.beginY();
		ihm.setSize(115, 32);
		Widget.drawText(ihm.widget(), "Trie", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Cache", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Ignore", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Total", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPS", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPSGLOBAL", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "FPSMESURE", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "BxWld", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "MdlName", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "MdlType", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "Info", Color.RED, Color.white, Color.GREEN);
		Widget.drawText(ihm.widget(), "NbObjet", Color.RED, Color.white, Color.GREEN);
		ihm.space(850);
		ihm.end();
		
		
	//	ihm.space(100);
		ihm.end();

	}

}
