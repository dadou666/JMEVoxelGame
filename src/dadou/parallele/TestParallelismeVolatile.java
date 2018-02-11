package dadou.parallele;

import java.io.IOException;

public class TestParallelismeVolatile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int n = 400000;
		System.out.println("taper entree pour commence");
		System.in.read();
		System.out.println("debut");
		ListeTache<TestTache> taches = new ListeTache<>(n);
		for (int i = 0; i < n; i++) {
			TestTache tt = new TestTache();
			tt.set(i, -i, i);
			tt.idx =i;
			tt.value = -1;
			taches.ajouter(tt);
		}
		
	
		
		System.out.println("processors="
				+ Runtime.getRuntime().availableProcessors());
		
	
		ExecuteurVolatile.initialiser(8);
		int total = 1;
		long debut = System.currentTimeMillis();
		for (int count = 0; count < total; count++) {
			ExecuteurVolatile.executer(taches);
		}
		System.out.println(" duree=" + (System.currentTimeMillis() - debut));
		for (int i = 0; i < taches.taille; i++) {
			if (taches.tache(i).value != i) {
				throw new Error("manque tache");
			}
		}

		debut = System.currentTimeMillis();
		for (int count = 0; count < total; count++) {
			for (int i = 0; i < taches.taille; i++) {
				taches.tache(i).calculer();
			}
		}
		System.out.println("normal duree="
				+ (System.currentTimeMillis() - debut));

		for (int i = 0; i < taches.taille; i++) {
			if (!taches.tache(i).flag) {
				throw new Error("manque tache");
			}
		}
		debut = System.currentTimeMillis();
		for (int count = 0; count < total; count++) {
			ExecuteurVolatile.executer(taches);
		}
		System.out.println("paralele duree="
				+ (System.currentTimeMillis() - debut));
		ExecuteurVolatile.executer(null);

	}

}
