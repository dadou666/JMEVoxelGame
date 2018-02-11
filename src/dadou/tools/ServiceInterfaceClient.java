package dadou.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import dadou.Game;

public class ServiceInterfaceClient {
	public String hostService = "http://www.power-kube.com/DadouWebService/GameService";
	public String localhostService = "http://localhost:8080/DadouWebService/GameService";
	public String clef;
	public String utilisateur;
	public String mdp;
	public String libelleError;

	public ServiceInterfaceClient() throws IOException {

		try {
			BufferedReader br = this.excuteService(hostService, "creerUtilisateurAnonyme\n");
			String statut = br.readLine();
			if (statut.equals("error")) {
				this.libelleError = br.readLine();
				throw new Error(this.libelleError + " creerUtilisateurAnonyme ");
			}
			this.utilisateur = br.readLine();
			this.mdp =this.utilisateur;

			br = this.excuteService(hostService, "connexion\n" + this.utilisateur + "\n" + mdp + "\n");
			statut = br.readLine();
			if (statut.equals("error")) {
				this.libelleError = br.readLine();
				throw new Error(this.libelleError + " " + utilisateur + " " + mdp);
			}
			this.clef = br.readLine();
			br.readLine();
		} catch (java.net.UnknownHostException e) {
			Game.devMode = true;

		}

	}

	public ServiceInterfaceClient(String utilisateur, String mdp, boolean dev) throws IOException {

		this.utilisateur = utilisateur;
		this.mdp = mdp;
		try {
			BufferedReader br = this.excuteService(hostService, "connexion\n" + this.utilisateur + "\n" + mdp + "\n");
			String statut = br.readLine();
			if (statut.equals("error")) {
				this.libelleError = br.readLine();
				throw new Error(this.libelleError + " " + utilisateur + " " + mdp);
			}
			this.clef = br.readLine();
			String devMode = br.readLine();
			if (dev) {
				if (!devMode.equals("true")) {
					throw new Error("Utilisatueur n'a pas le mode dev");
				}

			}
			Game.devMode = devMode.equals("true");
		} catch (java.net.UnknownHostException e) {
			Game.devMode = true;

		}

	}

	public void error(String libelle) {
		this.libelleError = libelle;
		throw new Error(libelle);
	}

	static public String readAll(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String s = null;
		boolean empty = true;
		while ((s = reader.readLine()) != null) {
			if (!empty) {
				sb.append('\n');
			}
			sb.append(s);
			empty = false;

		}
		if (empty) {
			return null;
		}
		return sb.toString();
	}

	public BufferedReader execute(String service, String... args) throws IOException {
		this.libelleError = null;
		StringBuilder sb = new StringBuilder();
		sb.append(service);
		sb.append("\n");
		sb.append("utilisateur=" + utilisateur);
		sb.append("\n");
		sb.append("clef=" + clef);
		sb.append("\n");

		for (String s : args) {
			sb.append(s);
			sb.append("\n");
		}
		String param = sb.toString();
		BufferedReader br;

		br = this.excuteService(hostService, param);

		String statut = br.readLine();
		if (statut == null) {
			error("Pas de statut");
		}
		if (statut.equals("error")) {
			error(br.readLine());
		} else {
			clef = br.readLine();
		}
		return br;

	}

	public BufferedReader lireDonnee(String nom) {
		BufferedReader r;
		try {
			r = execute("lireDonnee", "nom=" + nom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error("pb reseau");
		}

		return r;
	}

	public void ecrireDonnee(String nom, String valeur) {
		try {
			execute("ecrireDonnee", "nom=" + nom, valeur);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Error("pb reseau");
		}

	}

	private BufferedReader excuteService(String targetURL, String entree) throws IOException {
		HttpURLConnection connection = null;

		// Create connection
		URL url = new URL(targetURL);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");

		connection.setRequestProperty("Content-Length", Integer.toString(entree.getBytes().length));

		connection.setUseCaches(false);
		connection.setDoOutput(true);

		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(entree);
		wr.close();

		// Get Response
		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuilder response = new StringBuilder(); // or StringBuffer if
														// not Java 5+
		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
			response.append('\r');
		}
		rd.close();
		return new BufferedReader(new StringReader(response.toString()));

	}
}
