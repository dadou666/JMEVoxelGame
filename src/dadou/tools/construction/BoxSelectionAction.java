package dadou.tools.construction;

import java.awt.Color;

import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;

public abstract class BoxSelectionAction {
	public 	BrickEditor editor;
abstract public void action(int x, int y, int z, int dx, int dy, int dz) throws CouleurErreur;
abstract public String getMode();
}
