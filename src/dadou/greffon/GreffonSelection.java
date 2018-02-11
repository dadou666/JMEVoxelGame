package dadou.greffon;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public abstract class GreffonSelection {
	public static GreffonSelection courrant;
	public static List<GreffonSelection> greffons = new ArrayList<>();
	
	abstract public void exec(int x,int y,int z,int dx, int dy, int dz, Color cible[][][], Color color);
	abstract public String nom();
	public void initGreffon(JFrame parent) {
		
	}
}
