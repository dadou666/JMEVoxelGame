package ihm.swing;

import java.awt.Color;
import java.awt.Insets;

import ihm.Component;
import ihm.UIBuilder;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class SwingBuilder extends UIBuilder<JComponent> {
	public JFrame frame;

	public SwingBuilder() {

	}

	static public void setLookAndFeel() {

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("nimbusBase", Color.DARK_GRAY);
					UIManager.put("nimbusBlueGrey", Color.LIGHT_GRAY);
					UIManager.put("control", Color.LIGHT_GRAY);
				}
			}
		} catch (Exception e) {

		}
	}

	public void open(String title) {
		if (composite.parent != null) {
			return;
		}
		composite.computePosition();

		frame = new JFrame();
		frame.pack();
		Insets i = frame.getInsets();
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(composite.width + i.left + i.right, composite.height
				+ i.top + i.bottom);
		frame.setLayout(null);

		composite.build(this);
		// frame.pack();
		frame.setVisible(true);
		composite = null;

	}

	public void openIn(String title, JFrame oldFrame) {
		if (composite.parent != null) {
			return;
		}
		composite.computePosition();

		frame = new JFrame();
		frame.pack();
		Insets i = frame.getInsets();
		frame = oldFrame;
		frame.setResizable(false);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(composite.width + i.left + i.right, composite.height
				+ i.top + i.bottom);
		frame.setLayout(null);
		frame.getContentPane().removeAll();

		composite.build(this);
		// frame.pack();
		frame.setVisible(true);

	}

	@Override
	public void buildFrom(Component<JComponent> component) {
		// TODO Auto-generated method stub
		component.component.setSize(component.width, component.height);
		component.component.setLocation(component.x, component.y);
		frame.getContentPane().add(component.component);
	}

}
