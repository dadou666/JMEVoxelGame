package dadou.test.avl;

import java.io.*;
/*
 * Un filtre d'extension est defini par 2 choses : sa description(ce que l'on
 * voit dans la barre des extensions)  et les fichiers qu'il permet de laisser 
 * apparaitre ou non (accept). On precise qu'un repertoire est un fichier "special".
 *
 *
 * Titre : Extension personalisé
 * Auteur: Pengo
 * Date  : 2003
 *
 */

public class FiltreExtension extends javax.swing.filechooser.FileFilter
{
	String extension, description;
	
	//constructeur
	public FiltreExtension(String ext, String desc)
	{
		extension = ext;
		description = desc;
	}
	
	public boolean accept(File f)
	{
		if(f.getName().endsWith(extension))
	 		return true;
		else 
			if(f.isDirectory())
				return true;
			else
		    	return false;
	}
	
	public String getDescription()
	{
		return description+"(*" + extension + ")";
	}
}