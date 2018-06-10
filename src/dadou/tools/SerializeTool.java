/*
 * SerializeTool.java
 *
 * Created on 10 f�vrier 2007, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dadou.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 *
 * @author David
 */
public class SerializeTool {

	/** Creates a new instance of SerializeTool */
	public SerializeTool() {
	}

	static public byte[] toBytes(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		(new ObjectOutputStream(bos)).writeObject(obj);
		return bos.toByteArray();
	}

	static public <T> T toObject(Class<T> clazz, byte[] bytes) throws IOException, ClassNotFoundException {

		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

		return (T) (new ObjectInputStream(bis)).readObject();
	}

	static public void save(Object obj, String fileName) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		try {
			oos.writeObject(obj);
		} finally {
			oos.close();
		}

	}

	static public void save(Object obj, File file) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		try {
			oos.writeObject(obj);
		} finally {
			oos.close();
		}

	}

	static public void saveAsStream(String fileName, List<Object> objects) {

		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			for (Object o : objects)
				oos.writeObject(o);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static public ObjectInputStream readAsStream(String fileName) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);
		return ois;
	}

	static public Object load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
		ObjectInputStream ois = new ObjectInputStream(fis);

		try {
			return ois.readObject();
		} finally {
			ois.close();
		}
	}

	static public Object load(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			return ois.readObject();
		} finally {
			ois.close();
		}
	}

	static public Object load(URL url) throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(url.openStream());
		try {
			return ois.readObject();
		} finally {
			ois.close();
		}
	}

	static public <U extends Serializable> U clone(U obj) {
		ByteArrayInputStream bis;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			bis = new ByteArrayInputStream(bos.toByteArray());

			ObjectInputStream ois = new ObjectInputStream(bis);

			return (U) ois.readObject();

		} catch (IOException ex) {
			ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
