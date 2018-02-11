package dadou.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestJavascript {
	public void print(Object ...args){
		for(Object o:args) {
		System.out.print(o);
		}
		System.out.println();
	}

	public static void main(String[] args) throws ScriptException {
		// TODO Auto-generated method stub
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");

		
		engine.put("m",new TestJavascript());
		engine.eval("  m.print('x=',4,' y=',5) ");
	}

}
