package dadou;

import com.jme.renderer.Camera;

public class VBOBriqueTexture3D implements Element3D {
	public boolean afficherX= true;
	public boolean afficherY= true;
	public boolean afficherZ= true;
	VBOTexture3D X;

	VBOTexture3D Y;
	VBOTexture3D Z;
	public VoxelTexture3D tex;
	public int numTri = 0;
	VoxelShaderParam vsp;
	public Poid PX;
	public Poid PY;
	public Poid PZ;
	public BriqueAvecTexture3D creerBriqueAvecTexture3D(VoxelTexture3D tex)  {
		return null;
	}
	@Override
	public void dessiner(Camera cam) {
		// TODO Auto-generated method stub

	}
	
	public VoxelShader voxelShader() {
		return null;
	}

	@Override
	public void initFromShadowMap(Objet3D obj, Camera cam) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	//	X.delete();
	//	Y.delete();
	//	Z.delete();

	}
	

}
