package dadou.procedural;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Piece implements Serializable{
	public int x, y;
	public int dx, dy;
	public String color;
	public String objet;
	int n;
	
	public int h=0;
	transient public List<Piece> proches= new ArrayList<Piece>();

	public String toString() {
		String s = "" + x + ";" + y + ";" + dx + ";" + dy + ";" + color;
	
		return s;
	}

	public int maxX() {
		return Math.max(x, x + dx);
	}

	public 	int maxY() {
		return Math.max(y, y + dy);
	}

	public int minX() {
		return Math.min(x, x + dx);
	}

	public int minY() {
		return Math.min(y, y + dy);
	}

	public int cx() {
		return minX() + Math.abs(dx) / 2;

	}

	public int cy() {
		return minY() + Math.abs(dy) / 2;
	}

	public boolean contient(Piece piece) {
		if (minX() >= piece.minX() && maxX() <= piece.maxX()
				&& minY() >= piece.minY() && maxY() <= piece.maxY()) {
			return true;
		}
		return false;
	}

	public boolean intersection(Piece piece) {

		if (piece.minX() > maxX()) {
			return false;
		}
		if (minX() >= piece.maxX()) {
			return false;
		}

		if (piece.minY() > maxY()) {
			return false;
		}
		if (minY() >= piece.maxY()) {
			return false;
		}
		return true;

	}

}
