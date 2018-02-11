package dadou;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class DadouClassLoader extends ClassLoader {
	public static String base = "./bin/";

	public static void loop(String name) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(System.in);
		InputStreamReader isr = new InputStreamReader(bis);

		BufferedReader br = new BufferedReader(isr);
		while (true) {
			String line = br.readLine();
			if (line == null || line.isEmpty()) {
				ClassLoader cl = new DadouClassLoader(getSystemClassLoader());
				try {
					cl.loadClass(name).getMethod("exec").invoke(null, new Object[] {});
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	public DadouClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class loadClass(String name) throws ClassNotFoundException {

		String chemin = base + name.replace(".", "/");
		File file = new File(chemin + ".class");

		if (!file.exists())
			return super.loadClass(name);

		FileInputStream input;
		try {
			input = new FileInputStream(file);
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();

			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}

			input.close();

			byte[] classData = buffer.toByteArray();

			return defineClass(name, classData, 0, classData.length);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated ccklatch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
