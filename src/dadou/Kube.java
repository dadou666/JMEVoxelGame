package dadou;

import java.awt.Color;

import com.jme.math.Vector3f;

public class Kube {
	public int x;
	public int y;
	public int z;

	public Color color;
	public Vector3f position;

	public boolean estVide() {
		return color.equals(Color.BLACK);
	}
}
