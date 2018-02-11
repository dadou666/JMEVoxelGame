package dadou.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class OutilFichier {
	abstract public void callback(String line);

	public static List<String> lireFichier(String fileName) {
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			String line = reader.readLine();
			List<String> result = new ArrayList<String>();
			while (line != null) {

				result.add(line);
				line = reader.readLine();

			}
			return result;
		} catch (Throwable t) {
			return null;
		}

	}

	public static String lireChaineDansFichier(String fileName) {
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			StringBuilder sb = new StringBuilder();
			String line=null;
			boolean first = true;
			while ((line = reader.readLine())!=null) {
				if (!first) {
					sb.append("\n");	
				}
				first =false;
				sb.append(line);
				


			}
			reader.close();
			return sb.toString();
		} catch (Throwable t) {
			return null;
		}

	}

	public static void ajouterDansFichier(String fileName, List<String> liste) throws IOException {
		File file = new File(fileName);
		BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
		for (int k = 0; k < liste.size(); k++) {
			if (k > 0) {
				output.write("\n");
			}
			output.write(liste.get(k));

		}
		output.close();

	}

	public static void ajouterDansFichier(String fileName, String line) throws IOException {
		File file = new File(fileName);
		BufferedWriter output = new BufferedWriter(new FileWriter(file, true));

		output.write(line);
	//	output.write("\n");

		output.close();

	}

	public static void ecrireDansFichier(String fileName, List<String> liste) throws IOException {
		File file = new File(fileName);
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		for (int k = 0; k < liste.size(); k++) {
			if (k > 0) {
				output.write("\n");
			}
			output.write(liste.get(k));

		}
		output.close();

	}
	public static boolean ecrireChaineDansFichier(String fileName,String s) {
		File file = new File(fileName);
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(s);
			output.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	
			



	}

	public static void lireFichier(String fileName, OutilFichier callback) throws Throwable {
		FileReader file = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(file);
		String line = reader.readLine();

		while (line != null) {

			callback.callback(line);
			line = reader.readLine();

		}

	}
}
