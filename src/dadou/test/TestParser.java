package dadou.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import dadou.data.Obj;
import dadou.data.Parser;
import dadou.data.ReadFail;
import dadou.data.SyntaxError;

public class TestParser {

	@Test
	public void testReadFloat() throws ReadFail, SyntaxError {
		Parser p = new Parser("45.05");
		System.out.println(" " + p.readReal());
	}

	@Test
	public void testReadInt() throws ReadFail {
		Parser p = new Parser("45");
		System.out.println(" " + p.readInt());
	}

	@Test
	public void testReadString() throws ReadFail, SyntaxError {
		Parser p = new Parser("'45' '678' \"910\"");
		System.out.println(" " + p.readStr());

	}

	@Test
	public void testReadSymbol() throws ReadFail, SyntaxError {
		Parser p = new Parser(" toto ");
		System.out.println(" <" + p.readSymbol() + ">");

	}

	@Test
	public void testReadInstance() throws SyntaxError {
		Parser p = new Parser(" toto { x 10 y 45 } ");
		try {
			System.out.println(" <" + p.readInstance() + ">");
		} catch (SyntaxError se) {
			System.out.println(se);
		}

	}
	
	@Test
	public void testReadCollection() throws SyntaxError {
		Parser p = new Parser(" [ 45 5.5 'hello'  ] ");
		try {
			System.out.println(" <" + p.readCollection() + ">");
		} catch (SyntaxError se) {
			System.out.println(se);
			se.printStackTrace();
		}

	}
	
	@Test
	public void testReadObj() throws SyntaxError {
		Parser p = new Parser(" [ 45 5.5 'hello' print('hello')   pack { ls [ ]  m 'hello'}  mock { parent \"pa'tot\" }  ] ");
		try {
			System.out.println(" <" + p.readObj() + ">");
		} catch (SyntaxError se) {
			System.out.println(se);
			se.printStackTrace();
		}

	}
	
	@Test
	public void testReadFunctor() throws SyntaxError {
		Parser p = new Parser(" equal( 45 85 )");
		try {
			System.out.println(" <" + p.readFunctor() + ">");
		} catch (SyntaxError se) {
			System.out.println(se);
			se.printStackTrace();
		}

	}
	@Test
	public void testCreateObject() {
		Parser p= new Parser(" dadou.test.nono {  u 666  momo 'hello'  h 5.5 valeurs [ 'hello' 'nini'  ] toto dadou.test.toto { m 6 jojo dadou.test.jojo('hello') }} ");
		try {
			Object obj = p.readInstance().createObject();
			System.out.println(obj);
		} catch (SyntaxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	@Test 
	public void testReadAll() {
		
		Parser p= new Parser("@a tot { a 45 } momo { lo @a jojo nini.momo }  ");
		
		try {
			List<Obj> ls = p.readAll();
			System.out.println(ls);
		} catch (SyntaxError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
