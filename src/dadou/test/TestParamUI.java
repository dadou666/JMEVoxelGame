package dadou.test;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import dadou.Log;
import dadou.param.ParamUI;

public class TestParamUI extends ParamUI {
	JCheckBox check;
	JComboBox<String> values;
	JTextField text;
	public TestParamUI(){
	this.begin(100, 20);
	check=this.checkBox("Check");
	values=this.comboBox("Choix", new String[] {
		"choix1",
		"choix2"
	});
	text=this.textField("texte");
	center = true;
	this.end("Test param UI");
	}
	public void valider(){
		Log.print("check=",check.isSelected(),"\n");
		Log.print("values=",values.getSelectedItem(),"\n");
		Log.print("text=",text.getText(),"\n");
		
	}
	public static void main(String args[]) {
		new TestParamUI();
	}
}
