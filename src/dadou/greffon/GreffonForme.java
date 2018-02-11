package dadou.greffon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import dadou.tools.BrickEditorSwing;

abstract public class GreffonForme {
	public static List<GreffonForme> greffons = new ArrayList<>();
	

	public static GreffonForme courrant;
	public void init(BrickEditorSwing bes)  {
		
		
	}
	public boolean modifierValeur() {
		return false;
	}
	abstract public void exec(int dx,int dy,int dz,Color source[][][],Color cible[][][], Color color);
	abstract public String nom();

}
