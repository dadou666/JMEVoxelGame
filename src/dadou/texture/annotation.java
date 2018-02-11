package dadou.texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import dadou.ConfigLine;
import dadou.Log;
import dadou.tools.BrickEditor;
import dadou.tools.OutilFichier;
import dadou.tools.SerializeTool;

public class annotation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2935577776386038674L;
	public String fichier;
	public String annotation;
	public int x = 0;
	public int y = 0;
	public String color;
	public int taille;
	private static Map<String, annotation> annotations;

	public String toString() {
		return fichier + " annotation=" + annotation;

	}

	public static Map<String, annotation> annotations()
		{
		if (annotations == null)
			if (new File(BrickEditor.cheminRessources + "/annotation.bin")
					.exists()) {
				annotations = new HashMap<>();
				try {
					annotations = (Map<String, annotation>) SerializeTool
							.load(BrickEditor.cheminRessources
									+ "/annotation.bin");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return annotations;

	}

	public static void enregistrerAnnotations(Map<String, annotation> map)
			throws IOException {
		SerializeTool.save(map, BrickEditor.cheminRessources
				+ "/annotation.bin");
	}

}
