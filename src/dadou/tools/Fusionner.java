package dadou.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;

public class Fusionner {

	public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
/*	String fileName = "c:\\tmp\\defaut.wld";
		String fileNameAdd = "c:\\tmp\\worldA.bin";
		File rep = new File("c:/tmp/");
		DecorDeBriqueData  DecorDeBriqueData= new DecorDeBriqueData(256);
		DecorDeBriqueData.mondes = new ArrayList<>();
		for(File file:rep.listFiles()) {
			String absolutePath = file.getAbsolutePath();
			if (absolutePath.endsWith(".wld")) {
			DecorDeBriqueDataElement	DecorDeBriqueDataElement = (dadou.DecorDeBriqueDataElement) SerializeTool
					.load(absolutePath);
			DecorDeBriqueData.mondes.add(DecorDeBriqueDataElement);
			if (DecorDeBriqueData.bytes == null) {
				DecorDeBriqueData.bytes = DecorDeBriqueDataElement.bytes;
				DecorDeBriqueData.elementsDecor =DecorDeBriqueDataElement.elementsDecor;
			}
			
		}
			
		} */
		
	
	/*	DecorDeBriqueData	DecorDeBriqueDataAdd = (dadou.DecorDeBriqueData) SerializeTool
				.load(fileNameAdd);
		for(DecorDeBriqueDataElement elt:DecorDeBriqueDataAdd.mondes) {
			
			
			elt.nom="Add"+elt.nom;
			
		}
		DecorDeBriqueData.mondes.addAll(DecorDeBriqueDataAdd.mondes);*/
	//	SerializeTool.save(DecorDeBriqueData, "c:/tmp/world.bin"); 
	}

}
