package dadou;

public class ConstructionModelClasseEnCours {
	public ConstructionModelClasse cmc;
	public Object args;
	public boolean inverser = false;
	public boolean demarer = true;
	public int i = 0;
	static public int periodeMax = 10;
	public int periode = 0;

	public void process(ObjetMobile om) {
		if (periode < periodeMax) {
			periode++;
			return;
		}
		periode = 0;
		if (inverser) {
			i--;
		} else {
			i++;
		}
		if (i >= cmc.list.size()) {
			om.construction = null;
			if (demarer) {
				om.evenement().demarer(om, args);
			}
	
			return;
		}
		if (i < 0) {
			om.construction = null;
			if (demarer) {
				
				om.evenement().demarer(om, args);
			}
		
		}

	}

	public BriqueAvecTexture3D donnerBrique() {
		return cmc.list.get(i).brique;
	}

	public ConstructionModelClasseEnCours(ConstructionModelClasse cmc,
			boolean inverser) {
		this.cmc = cmc;
		this.inverser = inverser;
		if (this.inverser) {
			i = cmc.list.size() - 1;
		}
		demarer = true;
	}

}
