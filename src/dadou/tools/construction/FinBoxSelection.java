package dadou.tools.construction;

import java.awt.Color;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

import dadou.Kube;
import dadou.Log;
import dadou.ModelClasse;
import dadou.ModelClasseDecor;
import dadou.Objet3D;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.greffon.GreffonForme;
import dadou.greffon.GreffonSelection;
import dadou.greffon.ZoneGreffon;

public class FinBoxSelection extends EtatBoxSelection {
	public int px;
	public int py;
	public int pz;

	public int wx;
	public int wy;
	public int wz;
	public float dx = 1;
	public float dy = 1;
	public float dz = 1;
	public Objet3D objetColler;


	public VoxelDessinerLigneRec VoxelDessinerLigne;

	public void previsualisationForme(Selection selection) {
		// int minX=Math.min( cube.x, cube.x+(int)dx);
		// int minX=Math.min( cube.x, cube.x+(int)dx);
		// int minX=Math.min( cube.x, cube.x+(int)dx);
		ZoneGreffon zg = selection.BrickEditor.zg;
	
		if (!zg.modifierValeur) {
			if (zg.mc == null) {
				zg.mc = new ModelClasseDecor();
				zg.mc.init(zg.dx(), zg.dy(), zg.dz(), null);
			} else {
				if (zg.mc.vbo != null) {
					zg.mc.vbo.delete();
					zg.mc.tex.delete();
				}
				zg.mc.copie = null;
				zg.mc.init(zg.dx(), zg.dy(), zg.dz(), null);

			}
		}
		ModelClasse mc=zg.mc;
		// Log.print(" dx="+mc.dx+" dy="+mc.dy+" dz="+mc.dz);
		Color source[][][] = new Color[mc.dx][mc.dy][mc.dz];

		try {

			for (int ux = 0; ux < mc.dx; ux++) {
				for (int uy = 0; uy < mc.dy; uy++) {
					for (int uz = 0; uz < mc.dz; uz++) {
						source[ux][uy][uz] = selection.BrickEditor.decor.DecorDeBriqueData.lireCouleur(zg.x() + ux,
								zg.y() + uy, zg.z() + uz);

					}

				}
			}
			GreffonForme.courrant.exec(mc.dx, mc.dy, mc.dz, source, mc.copie, selection.BrickEditor.kubeCourant());
			mc.nomHabillage = selection.BrickEditor.decor.DecorDeBriqueData.nomHabillage;
			mc.initBuffer(selection.BrickEditor.game, true,
					selection.BrickEditor.donnerHabillage(selection.BrickEditor.decor.DecorDeBriqueData.nomHabillage));
			if (objetColler == null) {
				objetColler = new Objet3D();
			}
			objetColler.positionToZero();
			int dim = selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
			int dx2 = dim / 2;
			int dy2 = dim / 2;
			int dz2 = dim / 2;
			Vector3f pos = new Vector3f(zg.x() - dx2, zg.y() - dy2, zg.z() - dz2);

			objetColler.translation(pos);// selection.camSelection.getTranslationGlobal());

			objetColler.brique = mc.brique;

		} catch (CouleurErreur ce) {
			Log.print(" error = " + x + " " + y + " " + z);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	Kube cube = new Kube();

	public void dessiner(Selection selection, Camera cam) {
		selection.shader.use();
		selection.shader.glUniformfARB("dx", dx);
		selection.shader.glUniformfARB("dy", dy);
		selection.shader.glUniformfARB("dz", dz);
		// selection.shader.glUniformfARB("size", 1);
		// selection.shader.glUniformfARB("color", 1.0f, 0.0f, 1.0f, 0.0f);
		selection.camSelection.dessiner(cam);
		selection.shader.use();
		selection.shader.glUniformfARB("dx", 1.0f);
		selection.shader.glUniformfARB("dy", 1.0f);
		selection.shader.glUniformfARB("dz", 1.0f);
		// selection.shader.glUniformfARB("size", 1);
		// selection.shader.glUniformfARB("color", 1.0f, 1.0f, 0.0f, 0.0f);
		if (GreffonForme.courrant != null && selection.BrickEditor.zg != null) {
			this.previsualisationForme(selection);
		} else {
			objetColler = null;
		}
		selection.boxCamSelection.dessiner(cam);
		if (objetColler != null) {
			objetColler.dessiner(cam);
		}

	}

	public void setPosition(Selection selection, Vector3f pos) {
		selection.boxCamSelection.positionToZero();
	
		ZoneGreffon zg = selection.BrickEditor.zg;
		int dim = selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int dx2 = dim / 2;
		int dy2 = dim / 2;
		int dz2 = dim / 2;
		int x = (int) (pos.x + dx2) - (dx2);
		int y = (int) (pos.y + dy2) - (dy2);
		int z = (int) (pos.z + dz2) - (dz2);
		selection.boxCamSelection.translation(x, y, z);
		int px = (int) (pos.x + dim / 2);
		int py = (int) (pos.y + dim / 2);
		int pz = (int) (pos.z + dim / 2);
		// System.out.println( " px="+px+" py="+py+" pz="+pz);
		int tmpX = Math.max(Math.min(px, dim - 1), 0);
		int tmpY = Math.max(Math.min(py, dim - 1), 0);
		int tmpZ = Math.max(Math.min(pz, dim - 1), 0);
		zg.fx = this.px;
		zg.fy = this.py;
		zg.fz = this.pz;
		zg.lx = tmpX;
		zg.ly = tmpY;
		zg.lz = tmpZ;
		boolean r = tmpX != px || tmpY != py || tmpZ != pz;
		if (r) {
			return;
		}
		dx = tmpX - this.px + 1;
		dy = tmpY - this.py + 1;
		dz = tmpZ - this.pz + 1;
		int qx = 0;
		int qy = 0;
		int qz = 0;
		if (dx <= 0) {
			qx++;
			dx -= 2;
		}
		if (dy <= 0) {
			qy++;
			dy -= 2;
		}
		if (dz <= 0) {
			qz++;
			dz -= 2;
		}
		selection.BrickEditor.info.infoText = " " + (x + dx2) + "," + (y + dy2) + "," + (z + dz2) + "->" + (int) dx
				+ "," + (int) dy + "," + (int) (dz);
		selection.camSelection.positionToZero();
		selection.camSelection.translation(wx + qx, wy + qy, wz + qz);

	}

	public void setBlock(Selection selection, Vector3f pos, Color color) throws CouleurErreur {

		selection.BrickEditor.decor.getCube(pos, cube);
		int dim = selection.BrickEditor.decor.DecorDeBriqueData.decorInfo.nbCube;
		int px = (int) (pos.x + dim / 2);
		int py = (int) (pos.y + dim / 2);
		int pz = (int) (pos.z + dim / 2);
		// System.out.println( " px="+px+" py="+py+" pz="+pz);
		int tmpX = Math.max(Math.min(px, dim - 1), 0);
		int tmpY = Math.max(Math.min(py, dim - 1), 0);
		int tmpZ = Math.max(Math.min(pz, dim - 1), 0);
		dx = tmpX - this.px + 1;
		dy = tmpY - this.py + 1;
		dz = tmpZ - this.pz + 1;

		if (dx <= 0) {
			this.px++;
			dx -= 2;
		}
		if (dy <= 0) {
			this.py++;
			dy -= 2;
		}
		if (dz <= 0) {
			this.pz++;
			dz -= 2;
		}
		boolean r = tmpX != px || tmpY != py || tmpZ != pz;
		if (r) {
			return;
		}
		// System.out.println( " updateBlock ");
		int minX;
		int minY;
		int minZ;
		int maxX;
		int maxY;
		int maxZ;
		if (dx >= 0) {
			minX = this.px;
			maxX = (int) (this.px + dx);
		} else {
			minX = (int) (this.px + dx);
			maxX = this.px;

		}
		if (dy >= 0) {
			minY = this.py;
			maxY = (int) (this.py + dy);
		} else {
			minY = (int) (this.py + dy);
			maxY = this.py;

		}
		if (dz >= 0) {
			minZ = this.pz;
			maxZ = (int) (this.pz + dz);
		} else {
			minZ = (int) (this.pz + dz);
			maxZ = this.pz;

		}
		selection.EtatBoxSelection = null;
		selection.BoxSelectionAction.action(minX, minY, minZ, maxX, maxY, maxZ);
		if (selection.EtatBoxSelection == null) {
			selection.EtatBoxSelection = new DebutBoxSelection();
		}
	}
}
