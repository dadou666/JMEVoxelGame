package dadou.tools;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import dadou.Log;
import dadou.texture.annotation;
import dadou.texture.photo;
import dadou.tools.BrickEditor;
import ihm.swing.SwingBuilder;

public class ImageAnnotation implements WindowListener, MouseListener {
	int idx;
	List<String> list = new ArrayList<String>();
	Map<String, annotation> annotations;
	Map<String, photo> photos;
	JButton suivant = new JButton("Suivant");
	JButton precedent = new JButton("Precedent");
	JTextField annotationTextField = new JTextField();
	JComboBox<String> couleurs= new JComboBox<>(new String[]{"RED","YELLOW","GREEN","BLUE","PINK","MANGENTA"  });

	JComboBox<Integer> fontSize= new JComboBox<>(new Integer[]{10,15,20,25,30,35 });
	ImagePanel ip;

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException {
		ImageAnnotation ia = new ImageAnnotation();
		BrickEditor.cheminRessources = "H:/GitHub/DiaporamaMariageMathildaEtAlexis/";
		ia.run();

	}

	public void run() throws InstantiationException, IllegalAccessException {
		SwingBuilder sb = new SwingBuilder();

		annotations = annotation.annotations();
		if (annotations == null) {
			annotations = new HashMap<>();
		}
		photos = photo.photos();
		Log.print("  " + photos);

		for (String fichier : photos.keySet()) {
			list.add(fichier);
		}
		ip = new ImagePanel();
		ip.addMouseListener(this);
		sb.beginY(() -> {
			sb.beginX(() -> {
				sb.setSize(150, 30);

				sb.add(precedent);
				suivant.addActionListener((e) -> {
					this.modifierAnnotationCourante();
					idx++;
					recupererAnnotationCourante();

				});
				sb.add(suivant);
				precedent.addActionListener((e) -> {
					this.modifierAnnotationCourante();
					idx--;
					recupererAnnotationCourante();

				});
				sb.add(couleurs);
				sb.add(fontSize);
			});
			sb.setSize(600, 500);

			sb.add(ip);
			sb.setSize(600, 30);
			sb.add(annotationTextField);

		});
		sb.open("Image annotation");
		sb.frame.addWindowListener(this);

		recupererAnnotationCourante();

	}

	String cheminRelatif(String cheminAbsolu) {
		/*
		 * int size = BrickEditor.cheminRessources.length(); return
		 * cheminAbsolu.substring(size + 1);
		 */
		return cheminAbsolu;

	}

	annotation annotationCourante() {
		return annotation(list.get(idx));
	}

	annotation annotation(String cheminAbsolu) {
		String chemin = this.cheminRelatif(cheminAbsolu);
		if (annotations == null) {
			return null;
		}
		annotation a = annotations.get(chemin);
		if (a == null) {
			a = new annotation();
			a.fichier = chemin;
			annotations.put(chemin, a);
			return a;
		}
		return a;
	}

	public void modifierAnnotation(String cheminAbsolu, String txt) {
		Log.print(" cheminAbsolu=" + cheminAbsolu);
		String chemin = this.cheminRelatif(cheminAbsolu);
		annotation a = annotation(chemin);

		a.annotation = txt;

	}

	public void modifierAnnotationCourante() {
		String tx = annotationTextField.getText();
		String nomFichier = list.get(idx);
		this.modifierAnnotation(nomFichier, tx);
	}

	public void recupererAnnotationCourante() {
		String nomFichier = list.get(idx);
		annotation a = annotation(nomFichier);
		annotationTextField.setText(a.annotation);

		ip.charger(new File(BrickEditor.cheminRessources + list.get(idx)),
				photos.get(nomFichier), a);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		try {
			Log.print(" enregistrer ");
			annotation.enregistrerAnnotations(this.annotations);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent evt) {
		// TODO Auto-generated method stub
		this.modifierAnnotationCourante();
		annotation a = this.annotationCourante();
		photo p = this.photos.get(a.fichier);
		float x = evt.getX();
		float y = evt.getY();
		float w = p.largeur * p.tailleCube;
		float h = p.largeur * p.tailleCube;
		float W = ip.getWidth();
		float H = ip.getHeight();

		x = x * w / W;
		y = y * h / H;
		a.x = (int) x;
		a.y = (int) y;
		a.taille = (int) this.fontSize.getSelectedItem();
		a.color =(String) couleurs.getSelectedItem();

		this.recupererAnnotationCourante();

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
