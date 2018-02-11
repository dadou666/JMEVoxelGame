package ihm;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

abstract public class Composite<T> extends Element<T> {
	public List<Element<T>> elements = new ArrayList<Element<T>>();

	public void add(Element<T> element) {
		element.parent = this;
		elements.add(element);

	}

	abstract public int getWidth();

	abstract public int getHeight();

	public void space(int value) {

	}

	public void build(UIBuilder<T> builder) {

		builder.beginBuildComposite(this);
		for (Element<T> e : elements) {
			e.build(builder);
		}
		builder.endBuildComposite(this);
	}
}
