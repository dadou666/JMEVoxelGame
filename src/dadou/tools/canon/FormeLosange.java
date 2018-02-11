package dadou.tools.canon;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class FormeLosange extends FormeCible {

	@Override
	public void forme(Graphics2D g) {
		g.setStroke(new BasicStroke(4));
		g.drawPolygon(new int[] { 4, 16, 27 ,16}, new int[] { 16, 4, 16 ,27 }, 4);

	}

}
