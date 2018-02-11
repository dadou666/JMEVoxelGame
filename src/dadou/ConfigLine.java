package dadou;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import dadou.procedural.RegleDescription;

abstract public class ConfigLine {
	abstract public  void initialiser(String nom,Map<String,String> props);
	static public  <T extends ConfigLine>  Map<String,T>  lire(Class<T> cls,String source) throws InstantiationException, IllegalAccessException {
		BufferedReader br = new BufferedReader(new StringReader(source));
		Map<String,T> r= new HashMap<>();
		try {
			String line = br.readLine();
			
			while (line != null) {
				String valeurs[] = line.split(" ");
				if (valeurs.length >= 1) {
					String nom = valeurs[0];
					Map<String, String> props = new HashMap<>();
					for (String valeur : valeurs) {
						String tmp[] = valeur.split("=");
						if (tmp.length == 2) {
							props.put(tmp[0], tmp[1]);
						}

					}
					T t = cls.newInstance();
					t.initialiser(nom, props);
					r.put(nom, t);

				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}


}
