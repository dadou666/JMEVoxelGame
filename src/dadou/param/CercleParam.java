package dadou.param;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import dadou.greffon.Cercle;

public class CercleParam extends ParamUI {
	JComboBox<String> axe;
	JComboBox<Integer> rayonExterne;
	JComboBox<Integer> rayonInterne;
	Cercle cercle;
	public CercleParam(JFrame parent,Cercle cercle) 
	{
		super(parent);
		this.cercle = cercle;
		this.begin(100, 30);
		axe = this.comboBox("Axe", new String[] {"Rouge","Vert","Bleue"});
		rayonExterne = this.comboBox("Rayon Externe", new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 });
		rayonInterne = this.comboBox("Rayon Interne", new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22 });
		this.end("Cercle");
	}
	public void valider() {
		cercle.axe = (String) this.axe.getSelectedItem();
		cercle.rayonExterne = (Integer)rayonExterne.getSelectedItem();
		cercle.rayonInterne = (Integer)rayonInterne.getSelectedItem();
		this.parentFrame.setTitle(cercle.toString());
	}
	
}
