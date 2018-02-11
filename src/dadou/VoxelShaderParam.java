package dadou;

public class VoxelShaderParam {
	public Habillage habillage;

	

	public void initUniform(Brique bt) {
		bt.vs().initUniform(bt,this);

	}
	public void initUniform(Brique bt,Shader shader) {
		

	}

}
