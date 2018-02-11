package dadou.tools.canon;

import com.jme.math.Vector3f;

import dadou.VoxelTexture3D.CouleurErreur;

public interface CallbackCanon {
	public void saveOldPos(Vector3f oldPos);
	public boolean process(Obus obus) throws CouleurErreur;
	public void finish() throws CouleurErreur;
}
