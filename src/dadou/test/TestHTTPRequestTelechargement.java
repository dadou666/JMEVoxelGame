package dadou.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import ihm.swing.SwingBuilder;

public class TestHTTPRequestTelechargement extends SwingWorker<Void, Void> implements PropertyChangeListener {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * System.out.println(excuteGET(
		 * "http://64778hpv116125.ikoula.com:8080/DadouWebService/GameService",
		 * "connexion\nDadou\nzzz895a"));
		 */
		TestHTTPRequestTelechargement p = new TestHTTPRequestTelechargement();
	
		p.ouvrirInterface();

	}

	JProgressBar progressBar;

	public void ouvrirInterface() {
		SwingBuilder sb = new SwingBuilder();
		progressBar = new JProgressBar(0, 100);
		sb.setSize(200, 30);
		sb.beginX(() -> {
			sb.add(progressBar);

		});
		sb.open("Telechargement");
		this.addPropertyChangeListener(this);
		this.execute();

	}

	public String excuteGET(String targetURL) {
		HttpURLConnection connection = null;
		try {
			// Create connection
			URL url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			int code = connection.getResponseCode();
			// Send request
			if (code == 404) {
				return null;
			}
			long n = (connection.getContentLengthLong());

			InputStream is = connection.getInputStream();
			FileOutputStream fos = new FileOutputStream("c:/tmp/testTrucV2.zip");
			int b;
			float total = n;
			float avancement = 0;
			while ((b = is.read()) >= 0) {
				avancement++;
				float a = (avancement / total) * 100.0f;
			//System.out.println(" a="+a);
				this.setProgress((int) a);

				fos.write(b);
			}
			fos.close();

			return "" + n;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		excuteGET("https://codeload.github.com/dadou666/DoctorCube/zip/V1");
		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if ("progress" == evt.getPropertyName()) {

			int progress = (Integer) evt.getNewValue();
		//	System.out.println(" progress=" + progress);
			progressBar.setValue(progress);

		}

	}
}
