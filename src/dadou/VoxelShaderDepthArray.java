package dadou;

public class VoxelShaderDepthArray extends VoxelShader {

	public VoxelShaderDepthArray() {

		String prefixe = "texture_array/depth/";
		String geoX = null;
		String geoY = null;
		String geoZ = null;
	

			prefixe = "texture_array/depth/sp/";
			geoX = prefixe + "voxel.geo";
			geoY = prefixe + "voxel.geo";
			geoZ = prefixe + "voxel.geo";
		

	

			shaderVoxelX = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", geoX);
			shaderVoxelY = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", geoY);
			shaderVoxelZ = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", geoZ);

	

	}

}
