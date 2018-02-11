package dadou.son;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Emeteur {
	public String nom;
	public String fichier;
	public OggEmeteur player;
	public Emeteurs emeteurs;
	public float volume;


	public Emeteur(String nom) {
		this.nom = nom;
		// player = new BasicPlayer();

	}

	public void pauseSon() {
		player.pause();
	}

	public void continuer() {
		this.player.continuer();
	}

	public void demarer(float d) {
		if (this.player == null) {
			this.chargerSon(fichier);
		}
		this.player.demarer(d * volume);
	}

	public void chargerSon(String fichier) {
		try {
			this.fichier = fichier;
			this.player = new OggEmeteur(new File(fichier));
			this.player.emeteur = this;

			new Thread(this.player).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void volume(float d) {
		if (player ==null) {
			return;
		}
		player.volume(d * volume);
	}

	public void stop() {
		if (player == null) {
			return;
		}
		player.stop();
		player = null;
	}

	public void finSon() {
		if (this.emeteurs.elementJeux == null) {
			return;
		}
		this.emeteurs.elementJeux.finSon(nom);

	}
}
