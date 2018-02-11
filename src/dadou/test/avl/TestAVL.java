package dadou.test.avl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Test;

public class TestAVL {
	static int nb = 1500;
	static int nbElt = 2000;

	@Test
	public void testAVL() {
		List<noeud> list = new ArrayList<>();
		List<noeud> trier = new ArrayList<>();
		Random r = new Random();
		arbre a = new arbre();
		float max = 100.0f;
		for (int i = 0; i < 8000; i++) {
			float dist = max * r.nextFloat();
			noeud noeud = new noeud();
			noeud.valeur = dist;
			a.ajouter_noeud(noeud);
		}
		a.parcourir((n) -> {
			trier.add(n);
		});

		for (int i = 1; i < trier.size(); i++) {
			//System.out.println("i=" + i);
			assertTrue(trier.get(i - 1).valeur <= trier.get(i).valeur);
		}

	}

	@Test
	public void testTRIE() {
		long t = 0;
		for (int u = 0; u <= nb; u++) {
			List<noeud> list = new ArrayList<>();

			Random r = new Random();

			float max = 100.0f;
			for (int i = 0; i < nbElt; i++) {
				float dist = max * r.nextFloat();
				noeud noeud = new noeud();
				noeud.valeur = dist;
				list.add(noeud);
			}
			long m = System.nanoTime();
			Collections.sort(list, (noeud na, noeud nb) -> {
				if (na.valeur == nb.valeur) {
					return 0;
				}
				if (na.valeur > nb.valeur) {
					return 1;
				}
				return -1;

			});
			m = System.nanoTime() - m;
			t += m;
		}
		System.out.println(" t=" + t);
		/*
		 * for(int i=1;i< list.size();i++) { System.out.println("i="+i);
		 * assertTrue(list.get(i-1).valeur <= list.get(i).valeur); }
		 */

	}

	@Test
	public void testPERF() {
		long t = 0;
		for (int u = 0; u <= nb; u++) {
			List<noeud> list = new ArrayList<>();

			Random r = new Random();

			float max = 100.0f;
			for (int i = 0; i < nbElt; i++) {
				float dist = max * r.nextFloat();
				noeud noeud = new noeud();
				noeud.valeur = dist;
				list.add(noeud);
			}
			long m = System.nanoTime();
			Collections.sort(list, (noeud na, noeud nb) -> {
				if (na.valeur == nb.valeur) {
					return 0;
				}
				if (na.valeur > nb.valeur) {
					return 1;
				}
				return -1;

			});
			m = System.nanoTime() - m;
			t += m;
		}
		System.out.println(" t=" + t);
		t = System.nanoTime();
		for (int u = 0; u <= nb; u++) {

			Random r = new Random();
			arbre a = new arbre();
			float max = 100.0f;
			for (int i = 0; i < nbElt; i++) {
				float dist = max * r.nextFloat();
				noeud noeud = new noeud();
				noeud.valeur = dist;
				a.ajouter_noeud(noeud);
			}
		}
		t = System.nanoTime() - t;
		System.out.println("t=" + t);

	}

}
