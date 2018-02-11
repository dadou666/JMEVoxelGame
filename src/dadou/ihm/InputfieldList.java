package dadou.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import dadou.test.TestIHMInputField;

public class InputfieldList {
	public Map<Widget,StringBuilder> models;

	public Widget selection;

	public InputfieldList(){
		models =new HashMap<Widget,StringBuilder>();
	}
	public  void updateText(Color color) {
		StringBuilder sb = models.get(selection);
		Graphics2D g = selection.getGraphics2DForUpdate();
		g.setColor(color);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, selection.width, selection.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, selection.width - 2 * d, selection.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(sb.toString(), d,selection.height - 2*d);
		//g.dispose();

		selection.update();
	}
	public void setInputField(Widget w,String value) {
	
			StringBuilder	sb = new StringBuilder();
		sb.append(value);
		models.put(w, sb);
		selection =w;
		this.updateText(Color.GRAY);
		selection = null;
	}
	
	
	public boolean select(Widget w) {
		StringBuilder sb = models.get(w);
		if (sb == null) {
			return false;
		}
		if (selection != null) {
			this.updateText(Color.GRAY);
		}
		selection = w;
		this.updateText(Color.YELLOW);
		return true;
	}
	public void processKeyboard() {
	
		if (selection == null) {
			return;
		}
		StringBuilder sb= this.models.get(selection);
		if (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
			
				int event = Keyboard.getEventKey();
				char c= Keyboard.getEventCharacter();
				if (Character.isAlphabetic(c) || Character.isDigit(c) || c=='.' || c=='#') {
					
					sb.append(c);
				
					}
				if (event == Keyboard.KEY_SPACE){
					sb.append(" ");
					
				}
				if (event == Keyboard.KEY_BACK) {
					if (sb.length() > 0) {
					sb.deleteCharAt(sb.length()-1);}
				}
				this.updateText(Color.YELLOW);
				
			}
		}
	}

}
