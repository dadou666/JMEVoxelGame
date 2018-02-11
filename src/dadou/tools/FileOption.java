package dadou.tools;
/*
 * FileFilter.java
 *
 * Created on 3 f�vrier 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author David
 */
public class FileOption extends javax.swing.filechooser.FileFilter {
    public String extension ;
    public String description ;
    public boolean acceptFiles ;
    public boolean acceptDirectories ;
    public boolean multiSelect ;
    public String defaultDirectory = null;
    /** Creates a new instance of FileFilter */
    public FileOption() {
        acceptFiles = true ;
        acceptDirectories = true ;
    multiSelect = false;
    }
public int fileSectionModel() {
    if (acceptFiles  && acceptDirectories) return    JFileChooser.FILES_AND_DIRECTORIES;
    if (acceptFiles) return JFileChooser.FILES_ONLY;
    if (acceptDirectories) return JFileChooser.DIRECTORIES_ONLY ;
    throw new Error("File Option Error");
}
    public boolean accept(File f) {
        if (extension==null) return true;
        if (f.isDirectory()) return true;
   
       return f.getName().endsWith(extension);
      
    }

    public String getDescription() {
        if (description==null) return "";
        return description;
    }
    
}
