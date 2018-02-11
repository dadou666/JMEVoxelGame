package dadou.event;

import java.io.Serializable;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import dadou.MondeInterfacePublic;
import dadou.tools.BrickEditor;

public class GameEventLeaf extends GameEventNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4250587726373996873L;
	public String source;

	public String getSource() {

		return source;
	}

	public void setSource(String source) {
		// System.out.println(" source="+source+" ["+nom+"]");
		this.source = source;
	}

	public transient CompiledScript script;
	public transient Bindings bindings;
	public transient MondeInterfacePublic i;
	public static transient boolean trace = false;
	public transient boolean print = true;

	public GameEventLeaf(String nom) {
		this.nom = nom;
	}

	public GameEventLeaf getEventLeaf() {
		return null;
	}

	public void setVar(String nom, Object obj) {
		if (bindings == null) {
			return;
		}
		bindings.put(nom, obj);
	}

	public void print(Object... args) {
		if (!print) {
			return;
		}
		System.out.println(this.donnerChemin());
		for (Object o : args) {
			System.out.print(o);
		}
		System.out.println();
	}
	public boolean error = false;
	public void exec() {
		error = false;
		try {
			if (bindings == null) {
				return;
			}
			if (script == null) {
				return;
			}
			if (trace) {
				System.out.println(" exec :" + this.donnerChemin());
			}
			bindings.put("$", i);
			bindings.put("jeux", i.jeux);
			bindings.put("$$", this);
			script.eval(bindings);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			error = true;
			System.out.println("\n Script :" + this.donnerChemin() + "\n ");
			System.err.println("\n Script :" + this.donnerChemin() + "\n ");
			e.printStackTrace();

		}

	}

	public boolean compileScript(Compilable compilable, ScriptEngine engine, BrickEditor be) {
		i = be.mondeInterface.mondeInterface;
		if (source != null && source.trim().isEmpty()) {
			source = null;
		}
		if (source != null) {

			try {
				script = compilable.compile(source);
				bindings = engine.createBindings();
			} catch (ScriptException e) {
				// TODO Auto-generated catch block
				System.out.println(" nom =" + this.donnerChemin());
				System.out.println(e.getMessage());
				return false;
			}
		}
		return true;
	}
}
