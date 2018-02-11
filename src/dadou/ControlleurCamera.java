package dadou;

import dadou.tools.BrickEditor;

public class ControlleurCamera {
	public static ControlleurCamera controlleur = new ControlleurCameraEditeur();
	public boolean figerTranslationCamera = false;
	public void tireActif(BrickEditor be, boolean b) {

	}

	public boolean tireActif() {
		return false;
	}

	public void modifierCameraAvecSouris(BrickEditor be) {

	}

	public boolean faireSaut(BrickEditor be) {
		return false;

	}

	public boolean faireTire(BrickEditor be) {
		return false;

	}
	public boolean continuerTire(BrickEditor be) {
		return false;
		
	}

	public boolean cibleAuCentre(BrickEditor be) {
		return false;
	}

	public void gererAvancerReculer(BrickEditor be) {

	}

	public void figerRotationEtTranslationCamera(boolean b, BrickEditor be) {

	}

	public void figerTranslationCamera(boolean b, BrickEditor be) {
		figerTranslationCamera= b;
	

	}

	public Button selectButton() {
		return null;
	}
	public void finParcourirCameraPosition() {
		
	}
	public void chargerCameraPosition(CameraPosition cp) {
		
	}
}
