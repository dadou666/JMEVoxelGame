package dadou.parallele;

public class TestParallelisme {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ListeTache<TestTache> taches = new  ListeTache<>(400);
		for(int i=0; i< 100;i++) {
			TestTache tt = new TestTache();
			tt.set(i, -i, i);
			tt.idx = i;
			tt.value = -1;
			taches.ajouter(tt);
		}
		Executeur.initialiser(7);
		int total = 400000;
		long debut = System.currentTimeMillis();
		for(int count=0;count < total ;count++) {
			Executeur.executer(taches);
		}
		System.out.println(" duree="+(System.currentTimeMillis()-debut));
		for(int i=0;i< taches.taille ;i++) {
			if (!taches.tache(i).flag) {
				throw new Error("manque tache");
			}
		}
		
		 debut = System.currentTimeMillis();
		for(int count=0;count < total ;count++) {
			for(int i=0;i < taches.taille ;i++) {
				taches.tache(i).calculer();
			}
		}
		System.out.println("normal duree="+(System.currentTimeMillis()-debut));
		
	
		 debut = System.currentTimeMillis();
		for(int count=0;count < total ;count++) {
			Executeur.executer(taches);
		}
		System.out.println("paralele duree="+(System.currentTimeMillis()-debut));
		Executeur.executer(null);

	}

}
