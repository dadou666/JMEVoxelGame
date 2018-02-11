package dadou;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementTableauImage extends ElementTableau {
	public int nbLigne;
	public List<String> noms = new ArrayList<>();
	

	public int nbLigne() {
		return nbLigne;
	}

	public void afficher(Graphics2D g, int x, int y, int d, int fontHeight, Map<String, Icone> icones, Font font) {
		Icone icone = null;

		int dim = (nbLigne-1) * fontHeight;
		int W= tableau.widget.tex.width;;
		int IW= dim*noms.size();
		int Q = Math.max((W-IW)/2, 0);
		
		for (String nomIcone : noms) {
			icone = icones.get(nomIcone);
			if (icone != null) {
				g.drawImage(icone.getImage().getScaledInstance(dim, dim, java.awt.Image.SCALE_DEFAULT), x+Q, y, null);
				x += dim + d;

			}
		}
	}
}
