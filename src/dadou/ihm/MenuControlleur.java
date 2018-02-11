package dadou.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public abstract class MenuControlleur {

	public Widget widget;
	public char key;
	public String nom;
	public boolean selected;
	public boolean enabled=true;
	private String prefix;
	public MenuDeselection deselection;

	public MenuControlleur(String nom, int idx) {
		this.nom = nom;
		this.key = nom.charAt(idx);
		prefix = nom.substring(0, idx);

	}
	public void initDeselection() {
		deselection = new MenuDeselection(this);
	}

	abstract public void activer();
	public void setEnabled(boolean b) {
		if (b== enabled) {
			return;
		}
		enabled = b;
		this.updateText();
		
		
	}

	public void updateText() {

		Graphics2D g = widget.getGraphics2DForUpdate();

		g.setColor(Color.BLACK);

		g.fillRect(0, 0, widget.width, widget.height);

		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, widget.width, widget.height);
		if (selected) {
			g.setColor(Color.RED);
		} else {
			if (enabled) {
				g.setColor(Color.green);
			} else {
				g.setColor(Color.gray);
			}
		}
		int d = 5;
		g.fillRect(d, d, widget.width - 2 * d, widget.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(nom, d, widget.height - 2 * d);
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(prefix);
		int sz = fm.charWidth(key);
		int y = widget.height - 2 * d + 2;
		g.drawLine(d + width, y, d + width + sz, y);



		widget.update();
	}

}
