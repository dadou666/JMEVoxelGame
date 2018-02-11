package dadou;

import java.util.ArrayList;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.BoundingVolume;

public class ListeEvtDetection implements OctreeVisitor<OctreeValeur>{
	public List<ElementJeux> oldList= new ArrayList<>();
	public List<ElementJeux> newList=new ArrayList<>();
	public BoundingSphere bs= new BoundingSphere();

	public  Object IN = new Object();
	public  Object OUT= new Object();
	public ElementJeux objetMobile;
	public ListeEvtDetection(ElementJeux ej) {
		this.objetMobile = ej;
	}
	public void ajouter(ElementJeux om) {
		if (om == this.objetMobile) {
			return;
		}
	
			newList.add(om);
	
	}
	
	public void rayon(float r) {
		bs.setRadius(r);
	}
	
	public void init() {
		bs.setCenter(objetMobile.getBoundingVolume().getCenter());
	}
	public void entreeZoneDetection(ElementJeux om) {
		objetMobile.entreeZoneDetection(objetMobile, om);
	}
	
	public void sortieZoneDetection(ElementJeux om) {
		objetMobile.sortieZoneDetection(objetMobile, om);
		
	}
	public void dansZoneDetection(ElementJeux om) {
		
		
	}
	public void creerEvenement() {
		
		for(ElementJeux om:this.oldList) {
			
			om.mark = OUT;
			
		}
		
		for(ElementJeux om:this.newList) {
		
			if (om.mark != OUT) {
				
				entreeZoneDetection( om);
			} else {
				dansZoneDetection(om);
			}
			om.mark = IN;
			
		}
		
		for(ElementJeux om:this.oldList ) {
		
			if (om.mark == OUT) {
				sortieZoneDetection( om);
				
			}
			om.mark = null;
		
			
			
			
			
		}

		List<ElementJeux> tmpList;
		
		tmpList = newList;
	
		this.newList = this.oldList;
		this.oldList = tmpList;
		this.newList.clear();
		
		
		
		
		
		
	}

	@Override
	public BoundingVolume getBoudingVolume() {
		// TODO Auto-generated method stub
		return bs;
	}

	@Override
	public void execute(Octree<OctreeValeur> oct) {
		ObjetMobile om = oct.value.om;
		while(om != null) {
			if (om.getBox().intersects(bs)) {
				this.ajouter(om);
			}
			om =om.suivant;
			
		}
		Joueur joueur = oct.value.joueur;
		while(joueur != null) {
			if (joueur.getBoundingVolume().intersects(bs)) {
				this.ajouter(joueur);
			}
			joueur =joueur.suivant;
			
		}
		
	}

}
