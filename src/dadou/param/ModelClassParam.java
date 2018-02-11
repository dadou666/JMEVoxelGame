package dadou.param;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import dadou.ModelClasse;

public class ModelClassParam extends ParamUI {
	JTextField transparence;
	JCheckBox modifierOmbre;
	ModelClasse mc;

	public ModelClassParam(ModelClasse mc) {
		this.mc = mc;
		this.begin(100, 30);
		transparence = this.textField("Transparence");
		modifierOmbre = this.checkBox("Modifier ombre", mc.modifierOmbre);

		this.end("ModelClass");
	}

	public void valider() {
		if (!transparence.getText().trim().isEmpty()) {
			mc.transparence = Float.parseFloat(transparence.getText());
		}
		mc.modifierOmbre = this.modifierOmbre.isSelected();

	}

}
