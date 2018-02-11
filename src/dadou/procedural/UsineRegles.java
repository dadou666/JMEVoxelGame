package dadou.procedural;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dadou.Log;
import dadou.VoxelTexture3D.CouleurErreur;

public class UsineRegles {

	static public Regles regles;
	static public String readRessourceAsString(Class cls, String name) throws Exception {
		StringBuilder source = new StringBuilder();
		Exception exception = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(cls.getResourceAsStream(name), "UTF-8"));

			Exception innerExc = null;
			try {
				String line;
				while ((line = reader.readLine()) != null)
					source.append(line).append('\n');
			} catch (Exception exc) {
				exception = exc;
			} finally {
				try {
					reader.close();
				} catch (Exception exc) {
					if (innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}

			if (innerExc != null)
				throw innerExc;
		} catch (Exception exc) {
			exception = exc;
		} finally {
			if (exception != null)
				throw exception;
		}

		return source.toString();
	}




	

}
