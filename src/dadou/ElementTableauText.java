package dadou;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Map;

public class ElementTableauText extends ElementTableau {
	public String text;
	public String nomCouleur;
	public boolean centrer=false;

	public void afficher(Graphics2D g, int x, int y,int d,int fontHeight, Map<String, Icone> icones, Font font) {
		try {
			if (nomCouleur != null) {
			g.setColor((Color) Color.class.getDeclaredField(nomCouleur).get(null)); }
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int wt=0;
		if (centrer) {
			FontMetrics fm = g.getFontMetrics();
			int W= tableau.widget.tex.width;;
			wt= Math.max((W-fm.stringWidth(text))/2,0);
			
		}
		g.drawString(text, x+wt, y);
	}
}
