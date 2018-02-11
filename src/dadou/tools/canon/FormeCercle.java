package dadou.tools.canon;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class FormeCercle extends FormeCible {

	@Override
	public void forme(Graphics2D g) {
		g.setStroke(new BasicStroke(4));
		g.drawOval(4, 4, 27, 27);

	}

}
