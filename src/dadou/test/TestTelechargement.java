package dadou.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;



public class TestTelechargement {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		URL url = new URL(
				"https://codeload.github.com/dadou666/DoctorCube/zip/V1");
		InputStream is = url.openStream();
		
		String r=TestHTTPRequest.excuteGET("http://localhost:8080/DadouWebService/GameService",
				 "taille\nhttps://codeload.github.com/dadou666/DoctorCube/zip/V1");
	
		BufferedReader br = new BufferedReader(new StringReader(r));
		String statut = br.readLine();
		r = br.readLine();
		System.out.println(" total="+ r);
		
		FileOutputStream fos = new FileOutputStream("c:/tmp/testTrucV2.zip");
		int b;
		float total =Integer.parseInt(r);
		float avancement =0;
		while ((b = is.read()) >= 0) {
			avancement++;
			System.out.println(""+(avancement/total));
			
			fos.write(b);
		}
		fos.close();

	}
	



}
