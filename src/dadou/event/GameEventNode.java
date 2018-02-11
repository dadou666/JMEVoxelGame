package dadou.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.tree.DefaultMutableTreeNode;

import dadou.tools.BrickEditor;

public class GameEventNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8004811259635023987L;
	transient public GameEventTree parent;
	public String nom;
	transient public DefaultMutableTreeNode node;

	public String donnerChemin() {

		if (parent == null) {
			return nom;
		}
		return parent.donnerChemin() + "/" + nom;

	}

	public GameEventLeaf getEventLeaf() {
		return null;
	}

	public void ajouter(GameEventNode node) {

	}

	public DefaultMutableTreeNode[] getGameEventNodeForPath(String... path) {
		DefaultMutableTreeNode[] r = new DefaultMutableTreeNode[path.length + 1];
		GameEventNode tmp = this;
		r[0] = this.node;
		for (int idx = 0; idx < path.length; idx++) {
			tmp = tmp.getGameEventNode(path[idx]);
			if (tmp == null) {
				return null;
			}
			r[idx + 1] = tmp.node;

		}
		return r;

	}

	public GameEventNode getGameEventNode(String name) {
		return null;

	}



	public boolean compileScript(Compilable compilable, ScriptEngine engine,
			BrickEditor be) {
		return true;

	}

	public void donnerChemin(List<String> l) {
	
		if (parent != null) {
			parent.donnerChemin(l);
		}
	
		l.add(nom);

	}

	public String donnerChemin(String nomBase) {
		ArrayList<String> list= new ArrayList<>();
		this.donnerChemin(list);
		int idx = list.indexOf(nomBase);
		if (idx == -1) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for(int k=idx+1;k < list.size();k++) {
			if (isFirst){
				sb.append(list.get(k));
				isFirst = false;
			} else {
				sb.append(".");
				sb.append(list.get(k));
			}
			
			
		}
		if (isFirst) {
			return null;
		}
		return sb.toString();
		
	}

	public void createMutableNodeTree() {

		node = new DefaultMutableTreeNode(this);

	}

	public String toString() {
		return this.nom;
	}
}
