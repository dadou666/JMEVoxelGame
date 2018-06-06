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
	public static int width = 512;
	public static int height = 512;
	public static int widthRepeat = 4;
	public static int heightRepeat =2;
	public static int size =1;
	public Terrain red;
	public Terrain green;
	public Terrain blue;
	public int numIteration = 1000000;

	public void initTerrain() {
		int deltat = 1;
		int hauteur = 50;
		red = new Terrain(width, hauteur);
		blue = new Terrain(width, hauteur);
		green = new Terrain(width, hauteur);
		

	}

	public void modify(Terrain terrain) {
		terrain.modify();
		/*
		 * int h = 0; while (terrain.modify()) { if (terrain.h != h) {
		 * System.out.println(" h " + h); } h = terrain.h;
		 * 
		 * } float total = 0.0f; for (int i = 0; i < terrain.maxValue; i++) { float val
		 * = terrain.val(i); total += val; System.out.println(" terrain.count[" + i +
		 * "]=" + terrain.count[i] + " max " + terrain.val(i)); }
		 * System.out.println(" total=" + total + " - " + (terrain.n * terrain.n));
		 * terrain.test();
		 */

	}

	public TerrainCanvas() {
		this.initTerrain();
		modify(red);
		// modify(green);
		// modify(blue);
		green = red;
	//	red.simplifier();
		blue = red;
	}

	public void paint(Graphics g) {
		int total = 0;
		for (int uy = 0; uy < heightRepeat; uy++)
			for (int ux = 0; ux < widthRepeat; ux++) {
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < width; y++) {

						float fr = red.grille[x][y];
						fr = fr / (float) red.maxValue;
						float fg = green.grille[x][y];
						fg = fg / (float) red.maxValue;
						float fb = blue.grille[x][y];
						fb = fb / (float) red.maxValue;

						Color color = new Color((1.0f - fr), (1.0f - fg), (1.0f - fb));
						g.setColor(color);
						g.fillRect(ux*width*size+x * size, uy*height*size+y * size, size, size);
						if (color.getBlue() != 0 && color.getRed() != 0 && color.getGreen() != 0) {
							total++;
						}
					}
				}
			}
		System.out.println("total = " + total + "/" + (width * width));

	}

	public static void main(String[] a) {
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(30, 30, size * width * widthRepeat, size * height * heightRepeat);
		TerrainCanvas mc = new TerrainCanvas();
		window.getContentPane().add(mc);
		window.setVisible(true);
		window.addKeyListener(mc);

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		this.initTerrain();
		this.modify(red);
		green = red;
		//	red.simplifier();
			blue = red;
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
