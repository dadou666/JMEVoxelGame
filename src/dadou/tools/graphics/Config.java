package dadou.tools.graphics;

import java.awt.Color;

import org.lwjgl.input.Mouse;

import com.jme.renderer.Camera;

import dadou.Button;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.InputfieldList;
import dadou.ihm.Widget;
import dadou.tools.EtatBrickEditor;

public class Config extends EtatBrickEditor {
	 public ConfigValues config;
	
	


	public Config(Game g,ConfigValues config) {
		this.config = config;
	

	}



}
