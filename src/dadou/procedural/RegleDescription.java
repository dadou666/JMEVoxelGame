package dadou.procedural;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dadou.Log;

public class RegleDescription {
	public String nom;
	public List<String> nomModels = new ArrayList<>();
	public int idxNomModel = 0;
	public String PX;// ="*";
	public String MX;// ="*";
	public String PY;// ="*";
	public String MY;// ="*";
	public String PZ;// ="*";
	public String MZ;// ="*";
	public float poid = 1.0f;
	public float poidBase = 1.0f;
	public List<String> types = new ArrayList<>();
	public Float poidMax;
	public int nombreMax = 0;

	public void initialiser(Map<String, String> props) {
		PX = props.get("PX");
		PY = props.get("PY");
		PZ = props.get("PZ");

		MX = props.get("MX");
		MY = props.get("MY");
		MZ = props.get("MZ");
		String spoid = props.get("poid");
		if (spoid != null) {
			poidBase = Float.parseFloat(spoid);
			poid = poidBase;
		}
		String nomModel = props.get("model");
		if (nomModel != null) {
			String tmp[] = nomModel.split(";");
			for (String s : tmp) {
				nomModels.add(s);
			}
		} else {
			nomModels.add(nom);
		}
		String s = props.get("type");
		if (s != null) {
			String array[] = s.split(";");
			for (String t : array) {
				types.add(t);

			}
		}
		String smax = props.get("nombreMax");
		if (smax != null) {
			this.nombreMax = Integer.parseInt(smax);
		
		}

	}

	public String nomModel() {
		if (nomModels.size() > 1) {
		//	Log.print(" Multi modele " + idxNomModel);
		}
		if (idxNomModel == nomModels.size()) {
			idxNomModel = 0;
		}
		String r = nomModels.get(idxNomModel);
		idxNomModel++;
		return r;

	}

	public int nbConnexion() {
		int r = 0;
		if (this.MX != null && this.MX.charAt(0) != '#') {
			r++;
		}
		if (this.PX != null && this.PX.charAt(0) != '#') {
			r++;
		}
		if (this.MY != null && this.MY.charAt(0) != '#') {
			r++;
		}
		if (this.PY != null && this.PY.charAt(0) != '#') {
			r++;
		}
		if (this.MZ != null && this.MZ.charAt(0) != '#') {
			r++;
		}
		if (this.PZ != null && this.PZ.charAt(0) != '#') {
			r++;
		}
		return r;

	}

}
