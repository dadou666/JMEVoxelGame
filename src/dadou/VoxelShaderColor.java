package dadou;

public class VoxelShaderColor extends VoxelShader {
	public static boolean renduLumiereDiffere = true;

	public VoxelShaderColor() {

		String prefixe = "";
		if (renduLumiereDiffere) {
			prefixe = "differe/";
			// System.out.println(prefixe);
		}


			shaderVoxelX = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);
			shaderVoxelY = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);
			shaderVoxelZ = new Shader(BriqueAvecTexture3D.class, prefixe + "voxel.frag", prefixe + "voxel.vert", null);

	

	}

}
