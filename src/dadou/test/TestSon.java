package dadou.test;

import java.io.File;
import java.io.IOException;

import com.jcraft.jorbis.FuncFloor;
import com.jcraft.jorbis.FuncMapping;
import com.jcraft.jorbis.FuncResidue;
import com.jcraft.jorbis.FuncTime;
import com.jcraft.jorbis.Mapping0;

import dadou.Log;
import dadou.son.OggEmeteur;

public class TestSon {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		// OggEmeteur oe = new OggEmeteur(new
		// File("C:/Users/DAVID/Documents/GitHub/PowerKube/aide.ogg"));

		OggEmeteur oe1 = new OggEmeteur(new File("C:/Users/DAVID/Documents/GitHub/PowerKube/cell.ogg"));
		new Thread(oe1).start();
		/*
		 * OggEmeteur oe2 = new OggEmeteur(new
		 * File("C:/Users/DAVID/Documents/GitHub/PowerKube/arret_2.ogg")); new
		 * Thread(oe2).start();
		 */
		int n = 10;
		int m = 0;
		while (m < 30000) {
			m++;
			// System.out.println("m="+m);
			while (oe1.enCours()) {
				Thread.sleep(10);

			}
			Log.print("demarer");
			oe1.demarer(0.5f);
			if (n < 0) {
				// System.gc();
				n = 10;
			}
			n--;

		}
	
	//	System.out.println("l=" + FuncMapping.mapping_P.length);
		oe1.stop();
		oe1 = null;

		System.gc();

		
 /* while(true) {
		  
		//System.out.println("AttenteFuncFloor"); 
		}*/
		
	}

}
