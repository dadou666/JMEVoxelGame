package dadou;



public class ListeEvtDetectionPourSon extends ListeEvtDetection {

	public ListeEvtDetectionPourSon(ElementJeux ej) {
		super(ej);
		// TODO Auto-generated constructor stub
	}
	float rayonCarre ;
	public void rayon(float r) {
		bs.setRadius(r);
		rayonCarre =r*r;
	}
	
	public void calculerDistanceCamera(MondeInterfacePrive i) {
		for(ElementJeux ej:this.newList) {
			ej.calculerDistanceCamera(i.brickEditor.game.getCamera());
		}
		
		
	}
	
	public void entreeZoneDetection(ElementJeux om) {
		try {
			float gain =  1-om.distanceCamera/rayonCarre;
			om.emeteurs.volume(gain);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void dansZoneDetection(ElementJeux om) {
    	try {
    		float gain =  1-om.distanceCamera/rayonCarre;
			om.emeteurs.volume(gain);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void sortieZoneDetection(ElementJeux om) {
		try {
			
			om.emeteurs.volume(0.0f);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void ajouter(ElementJeux om) {
		if (om == this.objetMobile) {
			return;
		}
		if (om.emeteurs == null) {
			return;
		}
		
	
			newList.add(om);
	
	}
}
