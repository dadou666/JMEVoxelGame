package dadou.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import dadou.Habillage;

public class HabillageChargeur implements Runnable {
	public Habillage h;
	public File f;

	@Override
	public void run() {
		try {
			 h = (Habillage) SerializeTool.load(f);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
