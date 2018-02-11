package dadou.son;

import java.util.HashMap;
import java.util.Map;

import dadou.ElementJeux;

public class Emeteurs {
	public Map<String, Emeteur> emeteurs = new HashMap<>();
	public ElementJeux elementJeux;
	public Emeteurs( ElementJeux elementJeux) {
		this.elementJeux = elementJeux;
	}
	public void volume(String nomSon, float volume) {
		emeteurs.get(nomSon).volume = volume;

	}

	public void volume(float volume) {
		for (Emeteur e : emeteurs.values()) {
			e.volume(volume);
		}

	}

	public void demarer(String nomSon,float volume) {
		this.chargerSon(nomSon).demarer(volume);
	}

	public Emeteur chargerSon(String fichier) {
		Emeteur r = this.emeteurs.get(fichier);
		if (r != null) {
			return r;

		}
		Emeteur emeteur = new Emeteur(fichier);
		
		emeteur.chargerSon(fichier);
		this.emeteurs.put(fichier, emeteur);
		emeteur.emeteurs = this;
		return emeteur;

	}

	public void pause(String nomSon) {
		this.emeteurs.get(nomSon).pauseSon();

	}

	public void continuer(String nomSon) {
		this.chargerSon(nomSon).continuer();
	}
	public void stopper(String nomSon) {
		Emeteur e = this.emeteurs.get(nomSon);
		if (e == null) {
			return;
		}
		e.stop();
		this.emeteurs.remove(nomSon);
		
	}
	public void stop() {
		for (Emeteur e : emeteurs.values()) {
			e.stop();
		}
	}
}
