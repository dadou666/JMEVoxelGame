package dadou.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.swing.tree.DefaultMutableTreeNode;

import dadou.tools.BrickEditor;

public class GameEventTree extends GameEventNode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4960083591879847464L;
	public String type;

	public GameEventTree(String nom) {
		this.nom = nom;
	}

	public void ajouter(GameEventNode node) {

		this.children.put(node.nom, node);
		node.parent = this;

	}


	public Map<String, GameEventNode> children = new HashMap<>();

	public GameEventNode getGameEventNode(String name) {
		return children.get(name);

	}

	public GameEventLeaf getEventLeaf(String name) {
		GameEventNode r = children.get(name);
		if (r == null) {
			return null;

		}
		GameEventLeaf el = r.getEventLeaf();
		if (el.source == null) {
			return null;
		}
		return el;

	}
	

	public void ajouter(String [] chemin,GameEventNode node) {
		GameEventNode tmp = this;
		for(String nom:chemin) {
			GameEventNode nv = tmp.getGameEventNode(nom);
			if (nv == null) {
				GameEventTree get = new GameEventTree(nom);
				tmp.ajouter(get);
				tmp =get;
				
			} else {
				tmp=nv;
			}
	
			
			
		}
		tmp.ajouter(node);
		
		
	}



	public void createMutableNodeTree() {
		node = new DefaultMutableTreeNode(this);
		for (Map.Entry<String, GameEventNode> e : children.entrySet()) {
			e.getValue().createMutableNodeTree();
			node.add(e.getValue().node);
		}

	}

}
