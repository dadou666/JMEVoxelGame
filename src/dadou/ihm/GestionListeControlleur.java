package dadou.ihm;

import dadou.VoxelTexture3D.CouleurErreur;

public interface GestionListeControlleur {
  public abstract void execute(GestionListe gestionListe, boolean valider) throws CouleurErreur ;

}
