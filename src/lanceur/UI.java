/*
 * UI.java
 *
 * Created on 10 f�vrier 2007, 12:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package lanceur;

import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class UI {

    /**
     * Creates a new instance of UI
     */
    public UI() {
    }

    static public File[] chooseFileOrDirectory(Component c, FileOption fo) {
        JFileChooser fc;
        if (fo.defaultDirectory == null) {
            fc = new JFileChooser();
        } else {
            fc = new JFileChooser(fo.defaultDirectory);
        }

        fc.setFileSelectionMode(fo.fileSectionModel());
        fc.setFileFilter(fo);
        fc.setMultiSelectionEnabled(fo.multiSelect);


        int returnVal = fc.showOpenDialog(c);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (fo.multiSelect) {
                return fc.getSelectedFiles();
            } else {
                return new File[]{fc.getSelectedFile()};
            }
        }

        return null;
    }

    static public URL[] convertToURLs(File files[]) {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                urls[i] = files[i].toURI().toURL();
            } catch (MalformedURLException ex) {
            }
        }
        return urls;
    }

    static public String request(String msg, Component c) {
        return (String) JOptionPane.showInputDialog(
                c,
                msg,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");
    }

    static public boolean confirm(String msg, Component c) {
        return JOptionPane.showConfirmDialog(
                c,
                msg,
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null) == 0;
    }

    static public void warning(String msg, Component c) {
        JOptionPane.showMessageDialog(c,
                msg,
                "",
                JOptionPane.ERROR_MESSAGE);
    }

    static public <T> Object choice(T[] objects, String msg, Component c) {

        Object[] choices = new Object[objects.length];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = objects[i];
        }
        return JOptionPane.showInputDialog(
                c,
                msg,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                null);
    }

    static public <T> Object choice(List<T> objects, String msg, Component c) {

        Object[] choices = new Object[objects.size()];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = objects.get(i);
        }
        return JOptionPane.showInputDialog(
                c,
                msg,
                "",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                null);
    }
}
