package dadou;

import dadou.VoxelTexture3D.CouleurErreur;

public interface VoxelTexture3DEcouteur {
	public void ajouterBrique(int x,int y,int z) ;
	public void supprimerBrique(int x,int y,int z) throws CouleurErreur ;

}
