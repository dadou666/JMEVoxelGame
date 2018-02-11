package dadou.tools.construction;

import java.awt.Color;

import dadou.ElementDecor;
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelInstance;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.greffon.GreffonForme;

public class BoxRemplissage extends BoxSelectionAction {

	
	
	@Override
	public void action(int x, int y, int z, int dx, int dy, int dz) throws CouleurErreur {
		// TODO Auto-generated method stub
		if (!editor.zg.modifierValeur) {
			if (GreffonForme.courrant != null && GreffonForme.courrant.modifierValeur()) {
			editor.zg.modifierValeur = true;
			
			editor.selection.modifierValeurGreffon = true;
			editor.selection.EtatBoxSelection = new FinBoxSelection();
			return; }
		}
		editor.selection.modifierValeurGreffon = false;
		editor.zg.modifierValeur = false;
		if (GreffonForme.courrant != null) {
			editor.zg.mc = null;
			int mx = editor.zg.dx();
			int my = editor.zg.dy();
			int mz = editor.zg.dz();
			x= editor.zg.x();
			y= editor.zg.y();
			z= editor.zg.z();
			//Log.print(" greffonForme " + x + " " + y + " " + z + " => " + dx + " " + dy + " " + dz);
			Color source[][][] = new Color[mx][my][mz];
			Color cible[][][] = new Color[mx][my][mz];
			try {
				for (int ux = 0; ux < mx; ux++) {
					for (int uy = 0; uy < my; uy++) {
						for (int uz = 0; uz < mz; uz++) {
							source[ux][uy][uz] = editor.decor.DecorDeBriqueData.lireCouleur(x + ux, y + uy, z + uz);

						}

					}
				}
			} catch (java.lang.ArrayIndexOutOfBoundsException e) {
				Log.print(" error = " + x + " " + y + " " + z);

			}
			editor.callbackModification.run();
			GreffonForme.courrant.exec(mx, my, mz, source, cible, editor.kubeCourant());
			for (int ux = 0; ux < mx; ux++) {
				for (int uy = 0; uy < my; uy++) {
					for (int uz = 0; uz < mz; uz++) {
						Color color = cible[ux][uy][uz];
						int sx = x + ux;
						int sy = y + uy;
						int sz = z + uz;
						if (color != null) {
							if (ElementDecor.estVide(color)) {
								editor.decor.removeBrique(sx, sy, sz);
							} else {
								editor.decor.addBrique(sx, sy, sz);
							}
							editor.decor.ecrireCouleur(sx, sy, sz, color);
						}

					}

				}
			}

		} else {

			for (int sx = x; sx < dx; sx++) {
				for (int sy = y; sy < dy; sy++) {
					for (int sz = z; sz < dz; sz++) {
						Color color = editor.kubeCourant();

						if (ElementDecor.estVide(color)) {
							editor.decor.removeBrique(sx, sy, sz);
						} else {
							editor.decor.addBrique(sx, sy, sz);
						}
						editor.decor.ecrireCouleur(sx, sy, sz, color);

					}

				}
			}
		}
		// editor.decor.gestionCollision.verifierOctree();
		editor.decor.reconstuire();
	}

	@Override
	public String getMode() {
		// TODO Auto-generated method stub
		return "Remplisage";
	}

}
