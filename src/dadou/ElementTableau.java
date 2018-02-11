package dadou;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map;

public class ElementTableau {
	public Tableau tableau;
	public int nbLigne(){
		return 1;
	}
	public void afficher(Graphics2D g,int x,int y ,int d,int fontHeight, Map<String, Icone> icones, Font font) {
		
	}
	public  boolean modifierTexture() {
		return false;
		
	}

}
