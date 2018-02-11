package dadou;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

abstract public class Partitionneur {
	public abstract boolean estPlein(int x, int y);

	public abstract boolean estTraite(int x, int y);

	public abstract void traiter(int x, int y);

	public abstract int dx();

	public abstract int dy();

	public List<Rectangle> rs = new ArrayList<>();

	public boolean verifier(int x, int y, int max) {

		for (int i = 0; i < max; i++) {
			if (estTraite(x + i, y)) {
				return false;
			}
			if (!estPlein(x+i, y)) {
				return false;
			}

		}
		for (int i = 0; i < max; i++) {
			traiter(x + i, y);
		}
		return true;
	}
	public void creerRectangle(int px,int dx,int y) {
		if (dx== 0) {
			return;
		}
		int dy = 1;
		while (y + dy < dy() && verifier(px, y + dy, dx)) {
			dy++;
		}
		Rectangle r = new Rectangle(px, y, dx, dy);
		this.rs.add(r);
	}
	public void calculer() {
		int dx = 0;
		Integer px = 0;
		for (int y = 0; y < dy(); y++) {
			px=null;
			dx = 0;
			for (int x = 0; x < dx(); x++) {
				if (!estTraite(x, y)) {
					
					traiter(x, y);
					if (estPlein(x, y)) {
						if (px == null) {
							px=x;
						}
						
						if (x == dx()-1) {
							dx=x-px+1;
							this.creerRectangle(px, dx, y);	
						}

					} else {
						  if (px != null) {
							    dx=x-px;
								this.creerRectangle(px, dx, y);
								px=null;
						  }
						
							
						

					}

				} else {
					 if (px != null) {
						    dx=x-px;
							this.creerRectangle(px, dx, y);
							px=null;
					  }
				  
				}

			}

		}

	}

}
