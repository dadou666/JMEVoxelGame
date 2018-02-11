package lanceur;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class Update extends SwingWorker<Void, Void> implements PropertyChangeListener {
	public String urlString;
	public String version;

	public String chemin;
	public JProgressBar progressBar;
	public LanceurJeux main;

	public void demarer() {
		this.addPropertyChangeListener(this);
		this.execute();
	}

	public void unzip() {
		System.out.println(" telechargement " + urlString);
		File f = new File(chemin);
		String parent = f.getParent();
		new UnZip(chemin, parent);

	}

	HttpURLConnection donnerHttpURLConnection(boolean retry) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection connection = null;

		do {
			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			int code = connection.getResponseCode();
			// Send request
			if (code == 404) {
				System.out.println("erreur 404");
				return null;
			}
			System.out.println("retour :" + code);

		} while (connection.getContentLengthLong() == -1);
		return connection;
	}

	public boolean telecharger() {
		HttpURLConnection connection = null;
		try {
			// Create connection

			connection = this.donnerHttpURLConnection(true);
			if (connection == null) {
				return false;
			}
			long n = (connection.getContentLengthLong());

			InputStream is = connection.getInputStream();
			FileOutputStream fos = new FileOutputStream(chemin);
			int b;
			float total = n;
			float avancement = 0;
			byte buffer[] = new byte[100000];
			int nb = 0;
			System.out.println(" " + this.urlString + " total=" + total);
			while ((nb = is.read(buffer)) >= 0) {
				avancement += nb;

				this.setProgress(Math.min((int) ((avancement / total) * 100.0f), 100));
				System.out.println(" " + this.urlString + " : " + (avancement / total));
				fos.write(buffer, 0, nb);
			}
			fos.close();
			if (main.updateEngine == this) {
				main.updateEngine = null;
			} else {
				if (main.updateJeux == this) {
					main.updateJeux = null;
				}
			}
			unzip();
			main.lancerJeux();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		if ("progress" == evt.getPropertyName()) {

			int progress = (Integer) evt.getNewValue();
			// System.out.println(" progress=" + progress);
			progressBar.setValue(progress);

		}

	}

	@Override
	protected Void doInBackground() throws Exception {
		// TODO Auto-generated method stub
		this.telecharger();
		return null;
	}

}
