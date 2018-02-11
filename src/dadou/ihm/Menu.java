package dadou.ihm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import dadou.Button;
import dadou.Game;

public class Menu {
	public IHM ihm;
	public Map<Character, MenuControlleur> controlleurs;
	public Map<Widget,MenuControlleur> controlleursForWidget;
	public Button button = new Button(0);

	public Menu(IHM ihm, int width, int height, List<MenuControlleur> controlleurs,boolean sensX) {
		this.ihm = ihm;
		this.controlleurs = new HashMap<Character, MenuControlleur>();
		controlleursForWidget = new HashMap<>();
	if (sensX) {
		ihm.beginX(); } else {
			ihm.beginY();
		}
		ihm.setSize(width, height);
		for (MenuControlleur mc : controlleurs) {
			if (mc == null) {
				ihm.space(height);
			} else {
			mc.widget = ihm.widget();
			mc.updateText();
			controlleursForWidget.put(mc.widget, mc)
;			this.controlleurs.put(mc.key, mc); }

		}
		ihm.end();

	}
	public boolean processMouseButton() {
		if (button.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			Widget w = ihm.getWidgetAt(x, y);
			MenuControlleur mc = this.controlleursForWidget.get(w);
			if (mc == null || !mc.enabled) {
				return false;
			}
			mc.activer();
			for (MenuControlleur t : this.controlleurs.values())

				t.updateText();
			
			return true;
		}
		return false;
		
	}

	public void processKeyboard() {
		if (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {

				int event = Keyboard.getEventKey();
				char c = Keyboard.getEventCharacter();
				MenuControlleur mc = this.controlleurs.get(c);
			
				if (mc == null || !mc.enabled) {
					return;
				}
			
				mc.activer();
				for (MenuControlleur t : this.controlleurs.values())

					t.updateText();
			}

		}

	}

}
