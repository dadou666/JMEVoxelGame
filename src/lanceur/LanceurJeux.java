package lanceur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import ihm.BuilderAction;
import ihm.swing.SwingBuilder;
import ihm.swing.SwingBuilderDialog;

public class LanceurJeux extends SwingBuilder implements ActionListener {
	String userString;
	JTextField user;
	JTextField mdp;

	JTextField email;
	JTextField nouveauEmail;
	JTextField nouveauMDP;
	BuilderAction validerAction;

	String hostService = "http://www.power-kube.com/DadouWebService/GameService";
	String localhostService = "http://localhost:8080/DadouWebService/GameService";
	JButton connect;
	JButton inscrire;
	JButton envoyerIdentifiant;
	JButton modifierMDP;
	JButton modifierEmail;
	JButton quitter;

	JButton valider;
	JButton annuler;

	JButton reDo;
	String urlEngine;
	String urlJeux;
	String versionEngine;
	String versionJeux;
	String gameName = "PowerKubeBeta";
	public Map<JButton, String> games = new HashMap<>();
	public Game game;
	volatile public Update updateEngine;
	volatile public Update updateJeux;
	public String clef;
	public String JVM;

	String listJars(String rep) {
		File files[] = new File("dadouGame\\" + rep).listFiles();
		boolean first = true;
		StringBuilder db = new StringBuilder();
		for (File f : files) {
			if (f.getName().endsWith(".jar")) {
				if (!first) {
					db.append(";");
				}
				first = false;
				db.append(rep + "\\" + f.getName());
			}

		}
		return db.toString();

	}

	public void lancerJeux() {
		if (updateEngine != null) {
			return;
		}
		if (updateJeux != null) {
			return;
		}
		try {
			SerializeTool.save(game, "dadouGame/config.bin");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Runtime rt = Runtime.getRuntime();
		String jarEngine = listJars("VoxelEngine");

		String args = gameName + " " + user.getText() + " " + this.mdp.getText() + " nono";
		File chemin = new File(".", "dadouGame/" + gameName);
		String classPath = jarEngine;
		System.out.println("chemin=" + chemin.getAbsolutePath());

		for (File f : chemin.listFiles()) {

			if (f.getName().endsWith(".jar")) {
				classPath = classPath + ";" + gameName + "\\" + f.getName();

			}

		}

		try {
			String commande = JVM + "    -Djava.library.path=.\\VoxelEngine  -cp " + classPath
					+ " dadou.tools.BrickEditor " + args;

			for (File f : (new File(".")).listFiles()) {
				System.out.println("" + f.getAbsolutePath());
			}
			System.out.println(" " + new File(".\\dadouGame").exists());
			System.out.println(commande);
			Process pr = rt.exec(commande, null, new File(".\\dadouGame"));
			InputStream is = pr.getErrorStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;

			System.exit(0);
			while ((line = rd.readLine()) != null) {
				System.out.println(line);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String excuteService(String targetURL, String entree) throws IOException {
		HttpURLConnection connection = null;

		// Create connection
		URL url = new URL(targetURL);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		connection.setRequestProperty("Content-Length", Integer.toString(entree.getBytes().length));
		connection.setRequestProperty("Content-Language", "en-US");

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
		return response.toString();

	}

	public String creerDemande(String... args) {
		StringBuilder sb = new StringBuilder();
		for (String s : args) {
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();

	}

	public LanceurJeux() {
		this.buildMenuUI();
		try {
			game = (Game) SerializeTool.load("dadouGame/config.bin");
		} catch (FileNotFoundException e) {

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void buildInscriptionUI() {
		this.beginX();
		this.space(10);
		this.beginY();
		this.space(10);
		this.setSize(240, 30);
		this.beginX();
		this.add(new JLabel("Email"));
		this.add(email = new JTextField());
		this.end();
		this.beginX();
		this.add(new JLabel("User"));
		this.add(user = new JTextField());
		this.end();
		this.beginX();
		this.add(valider = new JButton("Validate"));
		this.add(annuler = new JButton("Annuler"));
		valider.addActionListener(this);
		annuler.addActionListener(this);
		this.end();
		this.space(10);
		this.end();
		this.space(10);
		this.end();
		this.validerAction = () -> {
			validerInscription();
		};
		openIn("Register", frame);

	}
	public void buildModifierEmailUI() {
		this.beginX();
		this.space(10);
		this.beginY();
		this.space(10);
		this.setSize(240, 30);
		this.beginX();
		this.add(new JLabel("New email"));
		this.add(email = new JTextField());
		this.end();
		this.beginX(() -> {

			this.add(new JLabel("User "));
			this.add(user = new JTextField());
			if (userString != null) {
				user.setText(userString);

			}

		});
		this.beginX(() -> {

			this.add(new JLabel("Password "));

			this.add(mdp = new JPasswordField());
			if (u != null) {
				mdp.setText(u.mdp);
			}

		});
		this.beginX();
		this.add(valider = new JButton("Validate"));
		this.add(annuler = new JButton("Cancel"));
		valider.addActionListener(this);
		annuler.addActionListener(this);
		this.end();
		this.space(10);
		this.end();
		this.space(10);
		this.end();
		this.validerAction = () -> {
			modifierEmail();
		};
		openIn("Change email", frame);

	}
	public void buildMenuUI() {
		this.beginX();
		this.space(10);
		this.beginY(() -> {
			this.space(10);
			this.setSize(400, 30);

			this.add(connect = new JButton("Sign in"));
			this.add(inscrire = new JButton("Register"));
			this.add(this.envoyerIdentifiant = new JButton("Send user and password"));

			this.add(this.modifierEmail = new JButton("Change email"));
			this.add(this.modifierMDP = new JButton("Change password"));
			this.add(this.quitter = new JButton("Exit"));
			this.space(10);
		});
		this.space(10);
		this.end();
		connect.addActionListener(this);
		inscrire.addActionListener(this);
		envoyerIdentifiant.addActionListener(this);
		this.modifierEmail.addActionListener(this);
		this.modifierMDP.addActionListener(this);
		quitter.addActionListener(this);

		openIn("Menu", frame);

	}

	public void buildEnvoyerIdentifiantUI() {

		try {
			u = (Utilisateur) SerializeTool.load("dadouGame/u.bin");
			userString = u.user;
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
		this.beginX();
		this.space(10);
		this.beginY(() -> {
			this.space(10);
			this.setSize(200, 30);
			this.beginX(() -> {

				this.add(new JLabel("Email "));
				this.add(email = new JTextField());

			});

			this.beginX();
			this.add(valider = new JButton("Validate"));
			this.add(annuler = new JButton("Cancel"));
			valider.addActionListener(this);
			annuler.addActionListener(this);
			this.end();
			this.space(10);
		});
		this.space(10);
		this.end();
		validerAction = () -> {
			this.envoyerIdentifiant();
		};
		openIn("Send user and password ", frame);
	}

	Utilisateur u;

	public void buildConnectUI() {

		try {
			u = (Utilisateur) SerializeTool.load("dadouGame/u.bin");
			userString = u.user;
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
		this.beginX();
		this.space(10);
		this.beginY(() -> {
			this.space(10);
			this.setSize(200, 30);
			this.beginX(() -> {

				this.add(new JLabel("User "));
				this.add(user = new JTextField());
				if (userString != null) {
					user.setText(userString);

				}

			});
			this.beginX(() -> {

				this.add(new JLabel("Password "));

				this.add(mdp = new JPasswordField());
				if (u != null) {
					mdp.setText(u.mdp);
				}

			});
			this.beginX();
			this.add(valider = new JButton("Validate"));
			this.add(annuler = new JButton("Cancel"));
			valider.addActionListener(this);
			annuler.addActionListener(this);
			this.end();
			this.space(10);
		});
		this.space(10);
		this.end();
		validerAction = () -> {
			this.validerConnexion();
		};
		openIn("Sign in", frame);
	}

	public void buildModifierMotDePasseUI() {
		this.beginX();
		this.space(10);
		this.beginY(() -> {
			this.space(10);
			this.setSize(200, 30);
			this.beginX(() -> {

				this.add(new JLabel("User "));
				this.add(user = new JTextField());
				if (userString != null) {
					user.setText(userString);

				}

			});
			this.beginX(() -> {

				this.add(new JLabel("Password "));

				this.add(mdp = new JPasswordField());

			});
			this.beginX(() -> {

				this.add(new JLabel("New password "));

				this.add(this.nouveauMDP = new JPasswordField());

			});
			this.beginX();
			this.add(valider = new JButton("Validate"));
			this.add(annuler = new JButton("Cancel"));
			valider.addActionListener(this);
			annuler.addActionListener(this);
			this.end();
			this.space(10);
		});
		this.space(10);
		this.end();
		this.validerAction = () -> {
			this.validerModificationMotDePasse();
		};
		openIn("Change password", frame);
	}

	public static File logError = new File("err.txt");
	public static File logOut = new File("out.txt");

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		/*
		 * if (logError != null) { System.setErr(new PrintStream(logError)); }
		 * if (logOut != null) { System.setOut(new PrintStream(logOut)); }
		 */
		SwingBuilder.setLookAndFeel();
		LanceurJeux r = new LanceurJeux();
		r.JVM = ".\\dadouGame\\jre\\bin\\java.exe";

	}

	public void lancerJeuxAvecMiseAJour() {
		if (game == null) {
			game = new Game();
		}
		String ancienneVersionEngine = game.versionEngine;
		String ancienneVersionjeux = game.versionJeux;

		try {
			updateEngine = this.updateInputStream(urlEngine, ancienneVersionEngine, this.versionEngine);
			updateJeux = this.updateInputStream(urlJeux, ancienneVersionjeux, this.versionJeux);

			if (updateEngine != null || updateJeux != null) {
				this.beginY(() -> {
					if (updateEngine != null) {
						this.beginX(() -> {

							this.setSize(100, 20);
							this.add(new JLabel("Moteur " + updateEngine.version));
							this.setSize(200, 20);
							this.add(updateEngine.progressBar = new JProgressBar(0, 100));
						});
					}
					if (updateJeux != null) {
						this.beginX(() -> {

							this.setSize(100, 20);
							this.add(new JLabel("Jeux " + updateJeux.version));
							this.setSize(200, 20);
							this.add(updateJeux.progressBar = new JProgressBar(0, 100));
						});
					}

				});
				this.openIn("Telechargement PowerKube" + gameName + " ...", frame);
				File repDadouGame = new File("dadouGame");
				if (!repDadouGame.exists()) {
					repDadouGame.mkdir();
				}
				if (updateEngine != null) {
					File rep = new File("dadouGame/VoxelEngine");
					if (!rep.exists()) {
						rep.mkdir();
					}
					updateEngine.chemin = "dadouGame/VoxelEngine/engine_" + updateEngine.version + ".zip";
					updateEngine.demarer();
					game.versionEngine = updateEngine.version;

				}
				if (updateJeux != null) {

					updateJeux.chemin = "dadouGame/" + gameName + "/" + updateJeux.version + ".zip";
					File rep = new File("dadouGame/" + gameName);
					if (!rep.exists()) {
						rep.mkdir();
					}
					game.versionJeux = updateJeux.version;
					updateJeux.demarer();
				}

			} else {

				this.lancerJeux();

			}
			return;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == reDo) {
			this.buildConnectUI();
		}

		if (e.getSource() == this.inscrire) {

			this.buildInscriptionUI();

			return;

		}
		if (e.getSource() == quitter) {
			System.exit(0);
		}
		if (e.getSource() == this.envoyerIdentifiant) {
			this.buildEnvoyerIdentifiantUI();
			return;

		}
		if (e.getSource() == this.modifierMDP) {

			this.buildModifierMotDePasseUI();

		}

		if (e.getSource() == this.modifierEmail) {
			this.buildModifierEmailUI();

		}

		if (e.getSource() == connect) {

			this.buildConnectUI();

		}
		if (e.getSource() == valider) {
			validerAction.process();

		}
		if (e.getSource() == annuler) {
			this.buildMenuUI();

		}

	}
	public void modifierEmail() {
		
		String nouveauEmail = email.getText();
		String r;
		try {
			r = this.excuteService(this.hostService, this.creerDemande("modifierEmailUtilisateur",
					"utilisateur=" + this.user.getText(), "mdp=" + this.mdp.getText(), "email=" + nouveauEmail));
			BufferedReader br = new BufferedReader(new StringReader(r));
			String statut = br.readLine();
			if (statut.equals("error")) {
				UI.warning("erreur =>" + br.readLine(), this.frame);
			} else {
				UI.warning("A tempory password was send, change thee", this.frame);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void envoyerIdentifiant() {
		String email = this.email.getText();
		String r;
		try {
			r = this.excuteService(this.hostService,
					this.creerDemande("rappelerModePasseUtilisateur", "email=" + email));
			BufferedReader br = new BufferedReader(new StringReader(r));
			String statut = br.readLine();
			if (statut.equals("error")) {
				UI.warning("erreur =>" + br.readLine(), this.frame);
			} else {
				UI.warning("User and password send", this.frame);

			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public Update updateInputStream(String baseUrl, String ancienneVersion, String versionCourante) throws IOException {
		if (ancienneVersion != null && versionCourante.equals(ancienneVersion)) {
			return null;
		}

		Update update = new Update();
		update.urlString = baseUrl + versionCourante;
		update.version = versionCourante;
		update.main = this;
		return update;

	}

	public void validerModificationMotDePasse() {
		String nouveauMDP = this.nouveauMDP.getText();
		String r;
		try {
			r = this.excuteService(this.hostService,
					this.creerDemande("modifierModePasseUtilisateur", "utilisateur=" + this.user.getText(),
							"ancienMotDePasse=" + this.mdp.getText(), "nouveauMotDePasse=" + nouveauMDP));
			BufferedReader br = new BufferedReader(new StringReader(r));
			String statut = br.readLine();
			if (statut.equals("error")) {
				UI.warning("Error =>" + br.readLine(), this.frame);
			} else {
				UI.warning("Password was changed", this.frame);
			}
			this.userString = user.getText();
			this.buildConnectUI();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void validerConnexion() {
		String r = null;
		try {
			r = this.excuteService(this.hostService, "connexion\n" + this.user.getText() + "\n" + this.mdp.getText());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		BufferedReader br = new BufferedReader(new StringReader(r));
		try {
			String statut = br.readLine();
			if (statut.equals("success")) {

				clef = br.readLine();
				br.readLine();
				urlEngine = br.readLine();
				versionEngine = br.readLine();
				urlJeux = br.readLine();
				versionJeux = br.readLine();
				Utilisateur u = new Utilisateur();
				u.mdp = this.mdp.getText();
				u.user = this.user.getText();
				SerializeTool.save(u, "dadouGame/u.bin");
				this.lancerJeuxAvecMiseAJour();

				// this.openIn("Lanceur", this.frame);
			} else {
				this.beginY(() -> {

					try {
						this.add(new JLabel(br.readLine()));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					this.add(reDo = new JButton("Refaire"));
				});
				reDo.addActionListener(this);
				this.openIn("Erreur", this.frame);

			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void validerInscription() {
		try {
			String r = excuteService(hostService, creerDemande("ajouterUtilisateur", "email=" + email.getText(),
					"utilisateur=" + this.user.getText()));
			BufferedReader br = new BufferedReader(new StringReader(r));

			String statut = br.readLine();
			if (statut.equals("error")) {
				UI.warning("Error =>" + br.readLine(), this.frame);
			} else {
				UI.warning("email was send with a key , use them to change your temporary password for login",
						this.frame);
				this.userString = user.getText();

				this.buildModifierMotDePasseUI();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			UI.warning("Erreur ????", this.frame);
		}

	}

}
