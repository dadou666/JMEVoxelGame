package dadou.procedural;

import java.util.ArrayList;
import java.util.List;

import dadou.Log;

public class Regle {

	public List<String> PX;
	public List<String> MX;
	public List<String> PY;
	public List<String> MY;
	public List<String> PZ;
	public List<String> MZ;
	public RegleDescription rd;
	public float totalPoid = 0.0f;
	public int nombreTotal = 0;
	public int nbEchec = 0;
	public String nom;

	public Regle(RegleDescription rd) {
		totalPoid = rd.poid;
	}

public float coeffTri() {
	return (totalPoid*(nbEchec+1));///(float)(nombreTotal+1);
}

	public static boolean comparer(String a, String b) {
		if (a == b) {
			return true;
		}
		if (a == null) {
			return false;
		}
		if (b == null) {
			return false;
		}
		return a.equals(b);
	}



	public boolean nombreSupperieurMax() {
		if (this.rd.nombreMax == 0) {
			return false;
		}
		return this.nombreTotal > this.rd.nombreMax-1;
	}

	public float nombreElement() {
		return this.totalPoid / rd.poid;
	}

	public boolean estSemblable(Regle r) {
		if (!comparer(r.rd.PX, rd.PX)) {
			return false;
		}
		if (!comparer(r.rd.PY, rd.PY)) {
			return false;
		}
		if (!comparer(r.rd.PZ, rd.PZ)) {
			return false;
		}
		if (!comparer(r.rd.MX, rd.MX)) {
			return false;
		}
		if (!comparer(r.rd.MY, rd.MY)) {
			return false;
		}
		if (!comparer(r.rd.MZ, rd.MZ)) {
			return false;
		}
		return true;
	}

	public void PX(String... noms) {
		if (PX == null) {
			PX = new ArrayList<>();
		}
		for (String nom : noms)
			PX.add(nom);
	}

	public void PY(String... noms) {
		if (PY == null) {
			PY = new ArrayList<>();
		}
		for (String nom : noms) {
			PY.add(nom);
		}
	}

	public void PZ(String... noms) {
		if (PZ == null) {
			PZ = new ArrayList<>();
		}
		for (String nom : noms) {
			PZ.add(nom);
		}

	}

	public void MY(String... noms) {
		if (MY == null) {
			MY = new ArrayList<>();
		}
		for (String nom : noms) {
			MY.add(nom);
		}

	}

	public void MZ(String... noms) {
		if (MZ == null) {
			MZ = new ArrayList<>();
		}
		for (String nom : noms) {
			MZ.add(nom);
		}

	}

	public void MX(String... noms) {
		if (MX == null) {
			MX = new ArrayList<>();
		}
		for (String nom : noms) {
			MX.add(nom);
		}

	}

	public int nombre() {
		return (int) (totalPoid / rd.poid);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PX=" + PX + "\n");
		sb.append("PY=" + PY + "\n");
		sb.append("PZ=" + PZ + "\n");
		sb.append("MX=" + MX + "\n");
		sb.append("MY=" + MY + "\n");
		sb.append("MZ=" + MZ + "\n");
		return sb.toString();

	}

	public void compare(Regle r) {
		System.out.println("poid=" + (r.rd.poid == rd.poid));
		System.out.println("PX:" + ("" + PX).equals("" + r.PX));
		System.out.println("PY:" + ("" + PY).equals("" + r.PY));
		System.out.println("PZ:" + ("" + PZ).equals("" + r.PZ));
		System.out.println("MX:" + ("" + MX).equals("" + r.MX));
		System.out.println("MY:" + ("" + MY).equals("" + r.MY));
		System.out.println("MZ:" + ("" + MZ).equals("" + r.MZ));

	}

	public void ajouterPoid() {
		totalPoid += rd.poid;

		this.nombreTotal++;
	}

	public void supprimerPoid() {
		totalPoid -= rd.poid;
	//	rd.poid+=rd.poidBase;
		this.nbEchec++;
		// Log.print("nb Echec ="+nom+" : "+nbEchec);
		this.nombreTotal--;

	}

	public boolean estType(String type) {
		if (type == null) {
			return false;
		}
		return this.rd.types.contains(type);
	}

	public int nbConnexion() {
		return rd.nbConnexion();

	}

}
