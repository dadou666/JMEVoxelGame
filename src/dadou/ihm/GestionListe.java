package dadou.ihm;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import dadou.Arbre;
import dadou.VoxelTexture3D.CouleurErreur;

public class GestionListe {
	WidgetList WidgetList;
	public InputfieldList inputFieldList = new InputfieldList();
	Widget Valider;
	Widget Annuler;
	Widget Ajouter;
	Widget Supprimer;
	Widget nom;
	public List<String> model;
	public List<String> listeAjouter;
	public List<String> listeSupprimer;
	public String selection;
	int delay=-1;
	final int maxDelay = 7;
	public boolean annuler=false;
	public void init() {
		listeAjouter = new ArrayList<>();
		listeSupprimer = new ArrayList<>();
		model = new ArrayList<>();
	}
	public GestionListeControlleur glc;
	public void setModel(List<String> list) {
		this.init();
		model.addAll(list);
		WidgetList.setModel(model);
		
	}

	public void updateText(String text, Widget selection, Color color) {
		if (selection == null) {
			return;
		}
		Graphics2D g = selection.getGraphics2DForUpdate();
		g.setColor(color);
		Font font = Font.decode("arial-22");
		g.fillRect(0, 0, selection.width, selection.height);
		g.setColor(Color.RED);
		int d = 5;
		g.fillRect(d, d, selection.width - 2 * d, selection.height - 2 * d);
		g.setFont(font);
		g.setColor(Color.BLUE);
		g.drawString(text, 2 * d, selection.height - 2 * d);
	//	g.dispose();

		selection.update();
	}

	public GestionListe(GestionListeControlleur glc,List<String> list, boolean editer, IHM ihm, int width,
			int height, int nbLigne) {
	this.glc=glc;
		ihm.beginY();
		this.init();
		this.model = new ArrayList<>();
		this.model.addAll(list);
		WidgetList = new WidgetList(ihm, width, height, nbLigne, true);
		WidgetList.setModel(model);
		Valider = ihm.widget();
		Annuler = ihm.widget();
		
if (editer) {
		Supprimer = ihm.widget();
	
		nom = ihm.widget();
		inputFieldList.setInputField(nom, "");
		Ajouter = ihm.widget();
}
		initButton();

		ihm.end();

	}
	public GestionListe(GestionListeControlleur glc,List<String> list,GestionListConfig config, IHM ihm, int width,
			int height, int nbLigne) {
	this.glc=glc;
		ihm.beginY();
		this.init();
		this.model = new ArrayList<>();
		this.model.addAll(list);
		WidgetList = new WidgetList(ihm, width, height, nbLigne, true);
		WidgetList.setModel(model);
		Valider = ihm.widget();
		Annuler = ihm.widget();
		
if (config.supprimer) {
		Supprimer = ihm.widget();
}
if (config.ajouter) {
		nom = ihm.widget();
		inputFieldList.setInputField(nom, "");
		Ajouter = ihm.widget();
}
		initButton();

		ihm.end();

	}
	public void initButton() {
		this.updateText("Supprimer", Supprimer, Color.BLACK);
		this.updateText("Valider", Valider, Color.BLACK);
		this.updateText("Ajouter", Ajouter, Color.BLACK);
		this.updateText("Annuler", Annuler, Color.BLACK);

	}
	
	public void processkeyboard() {
		inputFieldList.processKeyboard();
	}
	public void updateSelection() {
		if (delay == 0) {
			this.initButton();
			delay = -1;
		}
		if (delay > 0) {
			delay--;
		}
	}
	public ListElement getSelection() {
		
		return WidgetList.getSelection();
		
	
	}
	
	public boolean processClick(Widget w) throws CouleurErreur {
	
		if (w == Valider){
			this.initButton();
			this.updateText("Valider", Valider, Color.BLUE);
			delay = maxDelay;
			glc.execute(this, true);
		
			return true;
		}
		if (w == Annuler) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Annuler", Annuler, Color.BLUE);
			glc.execute(this, annuler);
		
			return true;
		}
		if (w == Ajouter) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Ajouter", Ajouter, Color.BLUE);
			String value = inputFieldList.models.get(nom).toString();
			if (value.trim().equals("")) {
				return true;
			}
		
			inputFieldList.setInputField(nom, "");
			this.listeAjouter.add(value.trim());
			this.model.add(value.trim());
			this.WidgetList.setModel(model);
			
			return true;

		}
		
		if (w == Supprimer) {
			delay = maxDelay;
			this.initButton();
			this.updateText("Supprimer", Supprimer, Color.BLUE);
			ListElement e = WidgetList.getSelection();
			if (e != null) {
			model.remove(e.idx);
			this.listeSupprimer.add(e.value);
			WidgetList.setModel(model);
			return true;
			}
			
		}
		if (WidgetList.processClick(w)) {
			return true;
		}
		return inputFieldList.select(w);

	}
}
