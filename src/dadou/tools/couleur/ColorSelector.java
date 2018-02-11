package dadou.tools.couleur;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class ColorSelector {
	public Widget plus;
	public Widget moins;
	public Widget label;
	public int value;

	void setText(Widget w, String txt) {
		Graphics2D g = w.getGraphics2DForUpdate();
		g.setColor(Color.GREEN);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, w.width - 2 * d, w.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(txt, 10, w.height - 10);
		g.dispose();
		w.update();
	}

	public ColorSelector(IHM ihm, Color color, int width, int height) {
		ihm.beginX();
		int marge = 2;
		ihm.setSize(width, height);
		Widget w = ihm.widget();
		Graphics2D g = w.getGraphics2DForUpdate();
		g.setColor(color);
		g.fillRect(0, 0, w.width, w.height);
		w.update();
		ihm.setSize(width / 4, height / 2);
		;
		ihm.beginY();
		plus = ihm.widget();

		g = plus.getGraphics2DForUpdate();
		g.setColor(color);
		g.setStroke(new BasicStroke(3));
		g.drawLine(plus.width / 2, 0, plus.width / 2, plus.height);
		g.drawLine(marge, plus.height / 2, plus.width - marge, plus.height / 2);
		plus.update();

		moins = ihm.widget();

		g = moins.getGraphics2DForUpdate();
		g.setColor(color);
		g.setStroke(new BasicStroke(3));

		g.drawLine(marge, moins.height / 2, moins.width - marge,
				moins.height / 2);
		moins.update();

		ihm.end();
		ihm.setSize(width / 2, height);
		label = ihm.widget();

		ihm.end();
		setText(label, "" + value);

	}

	public void setColorValue(int value) {
		value = Math.max(Math.min(value, 255), 0);
		setText(label, "" + value);
		this.value = value;
	}

	public boolean action(Widget w) {
		if (w == plus) {
			value += 10;
			value = Math.min(value, 255);
			setText(label, "" + value);
			return true;
		}
		if (w == moins) {
			value -= 10;
			value = Math.max(0, value);
			setText(label, "" + value);
			return true;
		}
		return false;
	}

}
