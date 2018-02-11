package dadou.procedural;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dadou.CameraPosition;
import dadou.DecorDeBriqueData;
import dadou.DecorDeBriqueDataElement;
import dadou.ElementDecor;
import dadou.Log;
import dadou.ModelClasse;
import dadou.VoxelTexture3D.CouleurErreur;
import dadou.tools.BrickEditor;
import dadou.tools.SerializeTool;
import dadou.tools.graphics.ConfigValues;

public class Regles {
	public Map<String, Regle> regles;
	public Map<String, RegleDescription> regleDescriptions = new HashMap<>();

	

	public RegleDescription regleDescription(String nom) {
		RegleDescription r = regleDescriptions.get(nom);
		if (r == null) {
			r = new RegleDescription();
			r.nom = nom;
			regleDescriptions.put(nom, r);
		}
		return r;
	}
	public Regles(String source) {
		this.lire(source);
		this.genererRegles();

	}



	public Regles() {

	}

	public boolean comparer(String a, String b) {
		if (a == null || b == null) {
			return false;

		}
		return a.equals(b);
	}

	public String nomModel(String nom) {
		Regle r = this.regles.get(nom);
		return r.rd.nomModel();
	}

	public void genererRegles() {
		regles = new HashMap<String, Regle>();
		for (Map.Entry<String, RegleDescription> rd1 : this.regleDescriptions.entrySet()) {
			Regle r = new Regle(rd1.getValue());
			r.rd = rd1.getValue();
			r.nom =rd1.getKey();
		
		

			regles.put(rd1.getKey(), r);
			for (Map.Entry<String, RegleDescription> rd2 : this.regleDescriptions.entrySet()) {

				if (comparer(rd1.getValue().PX, rd2.getValue().MX)) {
					r.PX(rd2.getKey());

				}
				if (comparer(rd1.getValue().MX, rd2.getValue().PX)) {
					r.MX(rd2.getKey());

				}
				if (comparer(rd1.getValue().PZ, rd2.getValue().MZ)) {
					r.PZ(rd2.getKey());

				}
				if (comparer(rd1.getValue().MZ, rd2.getValue().PZ)) {
					r.MZ(rd2.getKey());

				}
				if (comparer(rd1.getValue().PY, rd2.getValue().MY)) {
					r.PY(rd2.getKey());

				}
				if (comparer(rd1.getValue().MY, rd2.getValue().PY)) {
					r.MY(rd2.getKey());

				}

			}

		}

	

	}


	public int nbKube = 0;

	
	


	public void lire(String source) {
		BufferedReader br = new BufferedReader(new StringReader(source));
		try {
			String line = br.readLine();
			while (line != null) {
				String valeurs[] = line.split(" ");
				if (valeurs.length > 1) {
					String nom = valeurs[0];
					Map<String, String> props = new HashMap<>();
					for (String valeur : valeurs) {
						String tmp[] = valeur.split("=");
						if (tmp.length == 2) {
							props.put(tmp[0], tmp[1]);
						}

					}
					RegleDescription rd = this.regleDescription(nom);
					rd.initialiser(props);

				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
