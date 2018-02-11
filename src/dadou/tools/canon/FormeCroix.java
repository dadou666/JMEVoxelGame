package dadou.tools.canon;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class FormeCroix extends FormeCible {

	@Override
	public void forme(Graphics2D g) {
		g.setStroke(new BasicStroke(4));
		g.drawLine(4, 4, 27, 27);
		g.drawLine(4, 27, 27, 4);
	
	}

}
