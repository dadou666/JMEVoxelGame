package dadou;

public class VoxelShaderPlancheDepthArray extends VoxelShader {

	
	public VoxelShaderPlancheDepthArray() {

		String prefixe ="planche/texture_array/depth/";
	

		shaderVoxelX = new Shader(BriqueAvecTexture3D.class, prefixe+"voxel.frag",
				prefixe+"voxel.vert", null);
		shaderVoxelY = new Shader(BriqueAvecTexture3D.class, prefixe+"voxel.frag",
				prefixe+"voxel.vert", null);
		shaderVoxelZ = new Shader(BriqueAvecTexture3D.class,prefixe+ "voxel.frag",
				prefixe+"voxel.vert", null);
	}

}
