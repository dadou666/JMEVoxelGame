package dadou.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WidgetList {
	Map<Widget, ListElement> values;
	Widget widgets[];
	Widget up;
	Widget down;

	List<String> model = new ArrayList<String>();
	int viewIdx = 0;
	Widget selection;

	public ListElement getSelection() {
		return values.get(selection);
	}
	public String getSelectionValue() {
		ListElement le = values.get(selection);
		if (le == null) {
			return null;
		}
		return le.value;
	}
	public void updateText(Widget w, Color color) {
		ListElement E = values.get(w);
		String sb = "";
		if (E != null) {
			sb = values.get(w).value;
		}
		Graphics2D g = w.getGraphics2DForUpdate();
		if (E == null) {
			g.setColor(Color.BLACK);

			g.fillRect(0, 0, w.width, w.height);
			return;

		}
		g.setColor(color);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, w.width, w.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, w.width - 2 * d, w.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		if (sb != null) {
			g.drawString(sb, d, w.height - 2 * d);
		}
		g.dispose();

		w.update();
	}

	public void initScrollWidget(int width, int height) {
		Graphics2D g = down.getGraphics2DForUpdate();
		g.setColor(Color.BLUE);
		Polygon polygon = new Polygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(width / 2, height);
		polygon.addPoint(width, 0);
		g.fillPolygon(polygon);
		g.dispose();

		down.update();

		g = up.getGraphics2DForUpdate();
		g.setColor(Color.BLUE);
		polygon = new Polygon();
		polygon.addPoint(0, height);
		polygon.addPoint(width / 2, 0);
		polygon.addPoint(width, height);
		g.fillPolygon(polygon);
		g.dispose();

		up.update();

	}

	public WidgetList(IHM ihm, int width, int height, int size, boolean scroll) {
		ihm.beginY();
		ihm.setSize(width, height);
		if (scroll) {
			up = ihm.widget();
		}
		widgets = new Widget[size];
		for (int i = 0; i < size; i++) {
			widgets[i] = ihm.widget();

		}
		values = new HashMap<Widget, ListElement>();
		if (scroll) {
			down = ihm.widget();
			this.initScrollWidget(width, height);
		}
		ihm.end();

	}

	public void setViewIdx(int idx) {
		/*
		 * if (idx >= model.size()) { return; }
		 */
		this.viewIdx = idx;
		for (int i = 0; i < widgets.length; i++) {
			Widget w = widgets[i];
			int li = idx + i;
			if (li < model.size()) {
				values.put(w, new ListElement(li, model.get(li)));
			} else {
				values.put(w, null);
			}
			if (w == selection) {
				this.updateText(w, Color.YELLOW);
			} else {
				this.updateText(w, Color.GRAY);
			}
		}

	}

	public void setModel(List<String> model) {
		this.model = model;
		this.setViewIdx(0);

	}
	
	public void setModel(String ...values) {
		this.model = new ArrayList<String>();
		for(String value:values){
			this.model.add(value);
		}
		this.setViewIdx(0);

	}

	public boolean processClick(Widget w) {
		if (w == up) {
			if (viewIdx == 0) {
				return true;
			}
			this.setViewIdx(this.viewIdx - 1);
			return true;
		}
		if (w == down) {
			if (viewIdx + 1 >= model.size()) {
				return true;
			}
			this.setViewIdx(this.viewIdx + 1);
			return true;
		}
		ListElement elt = values.get(w);
		if (elt == null) {
			return false;
		}
		if (selection != null) {
			this.updateText(selection, Color.GRAY);
		}
		this.updateText(w, Color.YELLOW);
		selection = w;
		return true;

	}

}
