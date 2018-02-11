package dadou.param;

import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

import dadou.Arbre;
import dadou.Habillage;
import dadou.greffon.Cercle;
import dadou.tools.HabillageEditorMenu;

public class CaracteresParam extends ParamUI {
	JTextField caracteres;
	JCheckBox creerKube;
	JCheckBox inverserX;
	JComboBox<String> couleurFond;
	JComboBox<String> couleurCaractere;
	JCheckBox bordure;
	HabillageEditorMenu hem;
	Arbre<Object> arbreTextures;
	Habillage a;

	public CaracteresParam(JFrame parent, Habillage a, HabillageEditorMenu hem, Arbre<Object> arbreTextures) {
		super(parent);
		this.hem = hem;
		this.a = a;
		this.arbreTextures = arbreTextures;
		String couleurs[] = new String[] { "RED", "YELLOW", "BLUE", "GREEN", "PINK", "MAGENTA", "BROWN", "GRAY",
				"ORANGE", "DARK_GRAY", "LIGHT_GRAY"

		};

		this.begin(100, 30);
		caracteres = this.textField("Caracteres");
		creerKube = this.checkBox("Creer kube");
		inverserX = this.checkBox("Inverser X");
		couleurFond = this.comboBox("Couleur fond", couleurs);
		couleurCaractere = this.comboBox("Couleur caractere", couleurs);
		bordure = this.checkBox("Bordure");

		this.end("Caracteres");

	}
	public boolean inverserX() {
		return inverserX.isSelected();
	}

	public void valider() {

		hem.habillageEditorSwing.setAction(() -> {
			try {
				hem.creerCaracteres(a, arbreTextures, this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public boolean creerKube() {
		return this.creerKube.isSelected();
	}

	public boolean bordure() {
		return this.bordure.isSelected();

	}

	public String caracteres() {
		return caracteres.getText();
	}

	public Color couleurFond() {
		String c = (String) this.couleurFond.getSelectedItem();
		try {
			return (Color) Color.class.getField(c).get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Color.WHITE;

	}

	public Color couleurCaractere() {
		String c = (String) this.couleurCaractere.getSelectedItem();
		try {
			return (Color) Color.class.getField(c).get(null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Color.DARK_GRAY;

	}
}
