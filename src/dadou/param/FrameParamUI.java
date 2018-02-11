package dadou.param;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ihm.swing.SwingBuilderDialog;

public class FrameParamUI extends JDialog implements ActionListener {
	JButton GO;
	JFrame parentFrame;
	SwingBuilderDialog builder = new SwingBuilderDialog();
	int width;
	public boolean center = false;
	public FrameParamUI() {
		
	}
	public FrameParamUI(JFrame f) {
		super(f);
		this.parentFrame = f;
	}
	public void begin(int width, int height) {
		this.width = width;
		builder.setSize(width, height);
		builder.beginY();
	}

	public void end(String titre) {
		builder.setSize(2 * width, 30);
		builder.add(GO = new JButton("GO"));
		builder.end();
		GO.addActionListener(this);
		if (center) {
			this.center();
		}
	
		builder.openIn(titre, this);
		
	}

	public JCheckBox checkBox(String libelle) {
		JCheckBox jCheckBox = new JCheckBox();
		builder.beginX(() -> {
			builder.add(new JLabel(libelle));

			builder.add(jCheckBox);

		});
		return jCheckBox;
	}
	public JButton button(String libelle) {
		JButton button = new JButton(libelle);
		builder.setSize(2 * width, 30);
		builder.beginX(() -> {
			

			builder.add(button);

		});
		builder.setSize( width, 30);
		return button;
		
	}
	
	public JCheckBox checkBox(String libelle,boolean value) {
		JCheckBox jCheckBox = new JCheckBox();
		jCheckBox.setSelected(value);
		builder.beginX(() -> {
			builder.add(new JLabel(libelle));

			builder.add(jCheckBox);

		});
		return jCheckBox;
	}

	public JTextField textField(String libelle) {
		JTextField jTextField = new JTextField();
		builder.beginX(() -> {
			builder.add(new JLabel(libelle));

			builder.add(jTextField);

		});
		return jTextField;
	}
	public JTextField textField(String libelle,String valeur) {
		JTextField jTextField = new JTextField();
		jTextField.setText(valeur);
		builder.beginX(() -> {
			builder.add(new JLabel(libelle));

			builder.add(jTextField);

		});
		return jTextField;
	}
	public <T> JComboBox<T> comboBox(String libelle, T[] values) {
		JComboBox<T> jComboBox = new JComboBox<>(values);
		builder.beginX(() -> {
			builder.add(new JLabel(libelle));

			builder.add(jComboBox);

		});
		return jComboBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.valider();

	}

	public void valider() {

	}

	private void center() {
		int x, y;
		x = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
		y = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
		this.setLocation(x, y);
	}

}
