package ihm.swing;

import java.awt.Insets;

import ihm.Component;
import ihm.UIBuilder;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;

public  class SwingBuilderDialog  extends  UIBuilder<JComponent>{
	public JDialog frame;

	 public SwingBuilderDialog() {
		// TODO Auto-generated constructor stub
	
		
	}

	public void open(String title) {
		if (composite.parent != null) {
			return;
		}
		composite.computePosition();
		
		frame = new JDialog();
		frame.pack();
		Insets i = frame.getInsets();
		frame = new JDialog();
		frame.setResizable(false);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setSize(composite.width+i.left+i.right, composite.height+i.top+i.bottom);
		frame.setLayout(null);

	
		composite.build(this);
		//frame.pack();
		frame.setVisible(true);
		
		
		
	}
	
	
	public void openIn(String title,JDialog oldFrame) {
		if (composite.parent != null) {
			return;
		}
		composite.computePosition();
		
		frame = new JDialog();
		frame.pack();
		Insets i = frame.getInsets();
		frame = oldFrame;
		frame.setResizable(false);
		frame.setTitle(title);
	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		frame.setSize(composite.width+i.left+i.right, composite.height+i.top+i.bottom);
		frame.setLayout(null);
		frame.getContentPane().removeAll();
	
		composite.build(this);
		//frame.pack();
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
