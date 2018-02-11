package dadou.parseur;

import static org.junit.Assert.*;

import org.junit.Test;

import dadou.parseur.Sys.DuplicationRegle;

public class TestParseur {

	@Test
	public void testRegleEntier() throws DuplicationRegle {
		Parseur system = new Parseur(null,null);

		Integer i = (Integer) system.parse("entier", "45");
		System.out.println(" i=" + i);

		i = (Integer) system.parse("entier", "-45");
		System.out.println(" i=" + i);

		i = (Integer) system.parse("entier", "45 ");
		System.out.println(" i=" + i);

		i = (Integer) system.parse("entier", "-45A");
		System.out.println(" i=" + i);

		i = (Integer) system.parse("entier", "45A ");
		System.out.println(" i=" + i);
		i = (Integer) system.parse("entier", "45{ ");
		System.out.println(" i=" + i);

	}

	@Test
	public void testRegleReel() throws DuplicationRegle {
		Parseur system = new Parseur(null,null);
		Float i = (Float) system.parse("reel", "45");
		System.out.println(" i=" + i);

		i = (Float) system.parse("reel", "45.456");

		System.out.println(" i=" + i);

		i = (Float) system.parse("reel", "-45.456");

		System.out.println(" i=" + i);

		i = (Float) system.parse("reel", "-45..456");

		System.out.println(" i=" + i);

		i = (Float) system.parse("reel", "45..456");

		System.out.println(" i=" + i);

	}

	@Test
	public void testChaine() throws DuplicationRegle {
		Parseur system = new Parseur(null,null);
		String s = (String) system.parse("chaine", "'hello'");
		System.out.println(" s=" + s);
		s = (String) system.parse("chaine", "'hello ");
		System.out.println(" s=" + s);
	}

	@Test
	public void testSymbol() throws DuplicationRegle {
		Parseur system = new Parseur(null,null);
		String s = (String) system.parse("symbol", "hello");
		System.out.println(" symbol=" + s);
		
		s = (String) system.parse("symbol", " hello ");
		System.out.println(" symbol=" + s);

	}

}
