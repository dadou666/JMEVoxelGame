package dadou.mutator;

import java.util.ArrayList;
import java.util.List;

import dadou.ModelClasse;



public class Animation   {
	public List<ModelClasse> etapes = new ArrayList<>();
	public String nomModel;
	
	public Animation(String nomModel,List<ModelClasse> etapes) {
		this.etapes = etapes;
		this.nomModel = nomModel;
	}



}
