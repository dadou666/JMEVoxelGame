package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;

class TerrainCanvas extends JComponent implements KeyListener {
	public static int width = 256;
	public static int height = 256;
	public static int size = 1;
	public Terrain red;
	public Terrain green;
	public Terrain blue;
	public int numIteration = 1000000;

	public void initTerrain() {
		int deltat = 1;
		int hauteur = 255;
		red = new Terrain(width, deltat, hauteur);
		blue = new Terrain(width, deltat, hauteur);
		green = new Terrain(width,deltat, hauteur);

	}

	public void modify(Terrain terrain) {
		terrain.modify();
		/*int h = 0;
		while (terrain.modify()) {
			if (terrain.h != h) {
				System.out.println(" h " + h);
			}
			h = terrain.h;

		}
		float total = 0.0f;
		for (int i = 0; i < terrain.maxValue; i++) {
			float val = terrain.val(i);
			total += val;
			System.out.println(" terrain.count[" + i + "]=" + terrain.count[i] + " max " + terrain.val(i));
		}
		System.out.println(" total=" + total + " - " + (terrain.n * terrain.n));
		terrain.test();*/

	}

	public TerrainCanvas() {
		this.initTerrain();
		modify(red);
		modify(green);
		modify(blue);
		//green = red;
		//blue=red;
	}

	public void paint(Graphics g) {
		int total = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < width; y++) {
				Color color = new Color(255 - red.grille[x][y], 255 - green.grille[x][y], 255 - blue.grille[x][y]);
				g.setColor(color);
				g.fillRect(x * size, y * size, size, size);
				if (color.getBlue() != 0 && color.getRed() != 0 && color.getGreen() != 0) {
					total++;
				}
			}
		}
		System.out.println("total = " + total + "/" + (width * width));

	}

	public static void main(String[] a) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, size * width, size * height);
		TerrainCanvas mc = new TerrainCanvas();
		window.getContentPane().add(mc);
		window.setVisible(true);
		window.addKeyListener(mc);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
