package dadou.procedural;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dadou.Log;

public class Monde {
	public Map<String, Zone> zones = new HashMap<String, Zone>();
	public Piece estDansPiece[][];

	public Piece depart;

	public Map<String, Integer> occupations;
	public int dx, dy;
	public static int maxPiece = 25;
	public static int minPiece = 12;
	public static int taillePorte = 20;
	public static int marge = 3;
	public static int margePosition = 5;
	public static int tailleZonePorte = 150;
	int occupation = 0;
	int totalPiece = 0;
	String zonePorte = "0";
	Random r;

	public Monde(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public List<Piece> pieces() {
		ArrayList<Piece> r = new ArrayList<Piece>();
		for (Zone zone : this.zones.values()) {
			r.addAll(zone.pieces);
		}
		return r;

	}

	public void reset() {

		zones = new HashMap<String, Zone>();
		zonePorte = "0";
		this.compteurPorte = 0;
		occupation = 0;

	}

	public float occupation() {
		return (float) occupation / (float) (dx * dy);

	}

	public float occupation(Color color) {
		return (float) occupations.get(color) / (float) (dx * dy);

	}

	public boolean estDansPiece(int x, int y) {
		if (x < 0) {
			return true;
		}
		if (y < 0) {
			return true;
		}
		if (x >= dx) {
			return true;
		}
		if (y >= dy) {
			return true;
		}

		return estDansPiece[x][y] != null;
	}

	public int nombreDePositionLibrePourPorte(Piece piece) {
		Piece test = new Piece();
		int cx = piece.cx();
		int cy = piece.cy();
		;
		// int tailleZonePorte = Math.max(Math.abs(piece.dx),
		// Math.abs(piece.dy)) + 20;

		test.x = cx - tailleZonePorte;
		test.y = cy - tailleZonePorte;
		test.dx = 2 * tailleZonePorte;
		test.dy = 2 * tailleZonePorte;
		int n = 0;
		for (int x = test.minX(); x < test.maxX(); x++) {
			for (int y = test.minY(); y < test.maxY(); y++) {
				if (!this.estDansPiece(x, y)) {
					n++;
				}
			}

		}
		return n;

	}

	public List<Piece> chercherPiecesPourPorte(String zone) {
		List<Piece> ls = new ArrayList<Piece>();
		ls.addAll(this.zones.get(zone).pieces);

		for (int i = 0; i < ls.size(); i++) {
			Piece tmp = ls.get(i);
			tmp.n = this.nombreDePositionLibrePourPorte(tmp);
		}

		Comparator<Piece> comp = new Comparator<Piece>() {

			@Override
			public int compare(Piece arg0, Piece arg1) {
				// TODO Auto-generated method stub
				return Integer.compare(arg0.n, arg1.n);
			}

		};
		Collections.sort(ls, comp);
		Collections.reverse(ls);

		return ls;

	}

	public void listeIntersection(Piece p, List<Piece> rs) {
		for (Zone zone : this.zones.values()) {
			for (Piece q : zone.pieces) {
				if (p.intersection(q)) {
					rs.add(q);
				}
			}

		}
	}

	public boolean tester(String a, String b, List<Piece> rs) {
		// boolean lierA;
		for (Piece p : rs) {
			if (p.color != a && p.color != b) {
				return false;
			}
		}
		return true;
	}

	HashSet<Piece> piecesPourVerification = new HashSet<Piece>();

	public boolean verifier(Piece base, Piece piece, int numIntersectionMax) {
		int n = 0;
		if (piece.maxX() - piece.minX() < minPiece) {
			return false;
		}
		if (piece.maxY() - piece.minY() < minPiece) {
			return false;
		}
		if (piece.maxX() == piece.minX()) {
			return false;
		}
		if (piece.maxY() == piece.minY()) {
			return false;
		}

		Piece pieceAvecMarge = new Piece();
		pieceAvecMarge.x = piece.minX() - marge;
		pieceAvecMarge.y = piece.minY() - marge;
		pieceAvecMarge.dx = piece.maxX() - piece.minX() + 2 * marge;
		pieceAvecMarge.dy = piece.maxY() - piece.minY() + 2 * marge;
		piecesPourVerification.clear();

		for (String color : zones.keySet()) {
			for (Piece p : zones.get(color).pieces) {

				if (p.intersection(pieceAvecMarge)) {

					if (!p.color.equals(base.color) && p != base) {
						return false;
					}
					if (p != base) {
						n++;
					}
				}
				if (p.contient(piece) || piece.contient(p)) {
					return false;
				}
				if (n > numIntersectionMax) {
					return false;
				}
			}
		}
		return true;
	}

	public int random(int a, int b) {
		if (a == b) {
			return a;
		}
		int max = Math.max(a, b);
		int min = Math.min(a, b);
		return r.nextInt(max - min) + min;

	}

	public void ajouter(Piece piece) {
		if (piece.color == null) {
			throw new Error("color null");
		}
		for (int x = piece.minX(); x < piece.maxX(); x++) {
			for (int y = piece.minY(); y < piece.maxY(); y++) {
				if (!this.estDansPiece(x, y)) {
					occupation++;
					Integer i = this.occupations.get(piece.color);
					if (i == null) {
						i = 0;
					}
					this.occupations.put(piece.color, i + 1);
				}
				estDansPiece[x][y] = piece;
			}
		}
		totalPiece++;

		Zone zone = this.zones.get(piece.color);
		if (zone == null) {
			zone = new Zone();
			this.zones.put(piece.color, zone);

		}
		List<Piece> ls = zone.pieces;
		if (ls.isEmpty()) {
			zone.entree = piece;
		}
		ls.add(piece);

	}

	public List<Separation> separations = new ArrayList<Separation>();

	public Separation creerPorte(Piece piece, Piece parent) {
		for (Zone zone : this.zones.values()) {
			for (Piece p : zone.pieces) {
				if (p.intersection(piece) && p != parent) {
					return null;
				}
			}
		}
		Piece a = new Piece();
		Piece b = new Piece();
		a.color = "porte";
		b.color = "porte";

		if (Math.abs(piece.dx) > Math.abs(piece.dy)) {
			a.x = piece.minX();
			a.dx = Math.abs(piece.dx) / 2;

			b.x = a.x + a.dx;
			b.dx = Math.abs(piece.dx) - a.dx;

			a.y = piece.minY();
			b.y = piece.minY();
			a.dy = Math.abs(piece.dy);
			b.dy = Math.abs(piece.dy);
		} else {
			a.y = piece.minY();
			a.dy = Math.abs(piece.dy) / 2;

			b.y = a.y + a.dy;
			b.dy = Math.abs(piece.dy) - a.dy;

			a.x = piece.minX();
			b.x = piece.minX();
			a.dx = Math.abs(piece.dx);
			b.dx = Math.abs(piece.dx);

		}
		Piece test = a;
		if (a.intersection(parent)) {
			test = b;
			b = a;
			a = test;
		}
		for (Zone zone : this.zones.values()) {
			for (Piece p : zone.pieces) {
				if (p.intersection(test)) {
					return null;
				}
			}
		}
		Separation sep = new Separation();
		sep.a = a;
		sep.b = b;
		sep.p = parent;
		return sep;

	}

	public Piece generate(Piece piece, int nbEssai, int numIntersectionMax) {

		while (nbEssai > 0) {

			int pieceX = random(piece.minX() + margePosition, piece.maxX()
					- margePosition);
			int pieceY = random(piece.minY() + margePosition, piece.maxY()
					- margePosition);

			int pieceDx = random(Math.min(dx - pieceX, maxPiece),
					-Math.min(pieceX, maxPiece));
			int pieceDy = random(Math.min(dy - pieceY, maxPiece),
					-Math.min(pieceY, maxPiece));
			Piece p = new Piece();
			p.x = pieceX;
			p.y = pieceY;
			p.dx = pieceDx;
			p.dy = pieceDy;
			p.color = piece.color;
			nbEssai--;

			if (this.verifier(piece, p, numIntersectionMax)) {

				this.ajouter(p);
				return p;

			}

		}
		return null;

	}

	public static int nombreDePorteMax = 6;

	int compteurPorte = 0;

	public Piece generatePorte(int nbEssai) {
		List<Piece> ls = this.chercherPiecesPourPorte(zonePorte);
		for (Piece p : ls) {
			Piece r = this.generatePorte(p, nbEssai);
			if (r != null) {
				return r;
			}

		}
		return null;
	}

	public Piece generatePorte(Piece piece, int nbEssai) {
		Separation sep = null;

		int nbEssaiSave = nbEssai;
		// System.out.println(" generation porte");
		do {

			int pieceX = random(piece.minX() + margePosition, piece.maxX()
					- margePosition);
			int pieceY = random(piece.minY() + margePosition, piece.maxY()
					- margePosition);

			int pieceDx = taillePorte;
			int pieceDy = taillePorte;
			Piece p = new Piece();
			p.x = pieceX;
			p.y = pieceY;
			p.dx = pieceDx;
			p.dy = pieceDy;
			p.color = piece.color;
			nbEssai--;

			if (this.verifier(piece, p, 0) && p.maxX() < dx && p.maxY() < dy) {

				sep = this.creerPorte(p, piece);
				if (sep != null) {
					// System.out.println(" generation separation");
					this.ajouter(sep.a);
					this.ajouter(sep.b);
					piece = sep.a;
					this.separations.add(sep);
				}

			}

		} while (nbEssai > 0 && sep == null);
		if (sep == null) {
			return null;
		}
		nbEssai = nbEssaiSave;
		compteurPorte++;
		String color = "" + compteurPorte;
		// piece.color = color;
		while (nbEssai > 0) {

			int pieceX = random(piece.minX() + marge, piece.maxX() - marge);
			int pieceY = random(piece.minY() + marge, piece.maxY() - marge);

			int pieceDx = random(Math.min(dx - pieceX, maxPiece),
					-Math.min(pieceX, maxPiece));
			int pieceDy = random(Math.min(dy - pieceY, maxPiece),
					-Math.min(pieceY, maxPiece));
			Piece p = new Piece();
			p.x = pieceX;
			p.y = pieceY;
			p.dx = pieceDx;
			p.dy = pieceDy;
			p.color = color;
			nbEssai--;

			if (this.verifier(piece, p, 0)) {

				this.ajouter(p);
				zonePorte = color;
				
				// piece.color = "porte";
				return p;

			}

		}
		compteurPorte--;
		this.zones.get("porte").pieces.remove(sep.a);
		this.zones.get("porte").pieces.remove(sep.b);
		this.separations.remove(sep);

		return null;

	}

	public void initPieces() {
		r = new Random();

		zones = new HashMap<String, Zone>();
		occupations = new HashMap<String, Integer>();
		this.separations.clear();

		while (!this.ajouterPieceInit("0")) {
		}

	}

	public boolean ajouterPieceInit(String a) {
		estDansPiece = new Piece[dx][dy];
		for (int x = 0; x < dx; x++) {
			for (int y = 0; y < dy; y++) {
				estDansPiece[x][y] = null;
			}
		}
		int pieceDx = 40;
		random(minPiece, maxPiece);
		int pieceDy = 15;
		random(minPiece, maxPiece);

		int x = r.nextInt(dx - pieceDx);
		int y = r.nextInt(dy - pieceDy);

		Piece pieceA = new Piece();
		pieceA.x = x;
		pieceA.y = y;
		pieceA.dx = pieceDx / 2;
		pieceA.dy = pieceDy;
		pieceA.color = a;

		for (Zone zone : zones.values()) {
			for (Piece p : zone.pieces) {
				if (p.intersection(pieceA)) {
					return false;
				}
			}
		}
		ajouter(pieceA);

		return true;
	}

	public boolean ajouterPieceInit(int x, int y, String a) {
		int pieceDx = random(minPiece, maxPiece);
		int pieceDy = random(minPiece, maxPiece);

		Piece pieceA = new Piece();
		pieceA.x = x;
		pieceA.y = y;
		pieceA.dx = pieceDx / 2;
		pieceA.dy = pieceDy;
		pieceA.color = a;

		for (Zone zone : zones.values()) {
			for (Piece p : zone.pieces) {
				if (p.intersection(pieceA)) {
					return false;
				}
			}
		}
		ajouter(pieceA);

		return true;
	}

	int idxColor = 0;

	public Piece first(String color) {

		List<Piece> ls = this.zones.get(color).pieces;

		return ls.get(r.nextInt(ls.size()));

	}

	public boolean generate(String color, int cheminTaille) {

		Piece p = first(color);
		// System.out.println(" color ="+color);
		int numIntersectionMax = 1;

		while (cheminTaille > 0) {

			Piece nv;

			nv = this.generate(p, 1000, numIntersectionMax);

			if (nv == null) {
				numIntersectionMax++;

				if (numIntersectionMax >= 4) {

					return true;
				}
			} else {
				// System.out.println(" gen " + nv.color);
				p = nv;
				cheminTaille--;

				numIntersectionMax--;
				if (numIntersectionMax <= 0) {
					numIntersectionMax = 1;
				}

			}

		}

		return false;

	}

	public boolean generate(Piece p, int cheminTaille) {

		//
		int numIntersectionMax = 1;
		String color = p.color;
		int nbTest = 4;
		while (cheminTaille > 0) {

			Piece nv;

			nv = this.generate(p, 1000, numIntersectionMax);

			if (nv == null) {
				numIntersectionMax++;

				if (numIntersectionMax >= 4) {
					p = this.first(color);
					nbTest--;
					if (nbTest < 0) {

						return false;
					}
					numIntersectionMax = 1;

				}
			} else {

				p = nv;
				cheminTaille--;

				numIntersectionMax--;
				if (numIntersectionMax <= 0) {
					numIntersectionMax = 1;
				}

			}

		}

		return false;

	}

	public void generate() {
		do {

			do {
				this.reset();
				this.initPieces();

				System.out.println(" genereation ");
				Piece p = null;

				do {
					String oldZonePorte = zonePorte;

					p = this.generatePorte(1000);
					if (p != null) {
						this.generate(p, 30);
						System.out.println(" creation porte avant "
								+ oldZonePorte + " "
								+ this.zones.get(oldZonePorte).pieces.size());
						System.out
								.println(" creation porte apres " + zonePorte);
					}

				} while (p != null && this.compteurPorte < nombreDePorteMax);
			} while (this.compteurPorte != nombreDePorteMax);
		} while (!this.estValide());
		do {
			ArrayList<String> keys = new ArrayList<String>();
			keys.clear();
			keys.addAll(zones.keySet());
			keys.remove("porte");
			for (String k : keys) {

				this.generate(k, 3);
			}

			System.out.println(" occupation=" + this.occupation());
		} while (this.occupation() <= 0.80f);

		for (Separation sep : this.separations) {
			sep.b.color = sep.p.color;
			this.zones.get("porte").pieces.remove(sep.b);
			Zone zone = this.zones.get(sep.p.color);
			sep.a.objet =sep.b.color;
			zone.pieces.add(sep.b);
			zone.sortie = sep.b;

		}
		for (String key : this.zones.keySet()) {
			Zone zone = this.zones.get(key);
			if (!key.equals("porte")) {
				zone.creerGraphhe();
				System.out.println(" key=" + key + " - ");
				zone.calculerHauteur(this);

			}

		}
		

	}


	public boolean estValide() {
		for (String clef : zones.keySet()) {
			if (!clef.equals("porte") && !clef.equals("0")) {

				Zone zone = zones.get(clef);
				int size = zone.pieces.size();
				if (size <= 5) {
					Log.print(" fail " + clef);
					return false;
				}
			}
		}
		return true;

	}



	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean estDebut = true;
		for (String key : this.zones.keySet()) {
			Zone zone = this.zones.get(key);
			for (Piece p : zone.pieces) {
				if (!estDebut) {
					sb.append("\n");
				}
				estDebut = false;
				sb.append(p.toString());

			}
		}
		return sb.toString();

	}

}
