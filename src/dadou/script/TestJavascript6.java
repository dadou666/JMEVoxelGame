package dadou.script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestJavascript6 {

	public static void main(String[] args) throws ScriptException {
		// TODO Auto-generated method stub
		ScriptEngineManager factory = new ScriptEngineManager();

		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.eval("((x,y)=>{x+y})(4,5)");
	}

}
