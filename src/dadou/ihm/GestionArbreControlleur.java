package dadou.ihm;

import dadou.Arbre;

public interface GestionArbreControlleur<T> {
	
	public void valider(EditeurArbre<T> editeurArbre) ;
	public void annuler(EditeurArbre<T> editeurArbre) ;
	public boolean supprimer(EditeurArbre<T> editeurArbre,Arbre<T> nv) ;
	public boolean ajouter(EditeurArbre<T> editeurArbre,String nv) ;
}
