package dadou.param;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import dadou.greffon.Brique;

public class CubeVariableParam extends ParamUI {
	JComboBox<Integer> taillesX;
	JComboBox<Integer> taillesY;
	JComboBox<Integer> taillesZ;
	Brique cv;
	public CubeVariableParam(JFrame frame,Brique cv) {
		super(frame);
		this.cv=cv;
		this.begin(100, 20);
		taillesX = this.comboBox("Taille (Rouge)", new Integer[] {0,1,2,3,4,5,6,7,8,9 });
		taillesY = this.comboBox("Taille (Vert)", new Integer[] {0,1,2,3,4,5,6,7,8,9 });
		taillesZ = this.comboBox("Tailles (Bleue)", new Integer[] {0,1,2,3,4,5,6,7,8,9 });
		this.end("Cube variable");
		
	}
	public void valider() {
		cv.vx= (Integer)taillesX.getSelectedItem();
		cv.vy= (Integer)taillesY.getSelectedItem();
		cv.vz= (Integer)taillesZ.getSelectedItem();
		this.parentFrame.setTitle(cv.toString());
		
	}

}
