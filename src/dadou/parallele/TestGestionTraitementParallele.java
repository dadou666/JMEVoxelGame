package dadou.parallele;

import dadou.Log;




public class TestGestionTraitementParallele {

	public static void main(String[] args) {
		GestionTraitementParallele gestion = new GestionTraitementParallele();
		Runnable t1= ()-> {
			for(int i = 0 ;i < 100000;i++) {
				System.out.println(" traitement t1 "+i);
			}
		
		};
		Runnable t2= ()-> {
			for(int i = 0 ;i < 100000;i++) {
				System.out.println(" traitement t2 "+i);
			}
		
		};
		gestion.demarer();
		Traitement tr1=gestion.ajouter(t1);
		Traitement tr2=gestion.ajouter(t2);
		
		while (!tr1.estTermine && !tr2.estTermine) {
			Log.print(" hello");
		
			
		}
	
		gestion.arreter();
	}

}
