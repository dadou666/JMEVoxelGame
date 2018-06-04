package dadou;

import java.io.Serializable;

public class DecorInfo   implements Serializable{
	public int dim;
	public int nbCube;
	public int niveau;
	public int elementTaille;
	
	public void init(DecorDeBriqueDataElement d,int niveau,int elementTaille){
		dim = (int) Math.pow(2, niveau);
		;
		this.niveau = niveau;
		this.elementTaille = elementTaille;
		nbCube = dim * elementTaille;
		d.elementsDecor = new ElementDecor[dim][dim][dim];
	

		
	}
	public void initElementDecor(DecorDeBriqueDataElement d) {
		d.elementsDecor = new ElementDecor[dim][dim][dim];
	}

	private static final long serialVersionUID = -8973649883450822315L;

}
