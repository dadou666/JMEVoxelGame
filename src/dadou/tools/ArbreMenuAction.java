package dadou.tools;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import dadou.ihm.Action;

public class ArbreMenuAction {
	public Map<JMenuItem, Action> menuAction = new HashMap<>();
	public Action selectionAcion;
	public ArbreMenuAction selection(Action action) {
		this.selectionAcion =action;
		return this;
	}
	public static ArbreMenuAction creer() {
		return new ArbreMenuAction();
	}
	public ArbreMenuAction ajouter(JMenuItem mi,Action a) {
		menuAction.put(mi,a);
		return this;
	}

}
