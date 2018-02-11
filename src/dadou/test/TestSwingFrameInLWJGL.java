package dadou.test;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.jme.system.DisplaySystem;

import dadou.Button;
import dadou.Game;
import dadou.ihm.Comportement;
import dadou.ihm.IHM;
import dadou.ihm.IHMSwingGame;
import dadou.ihm.Widget;

public class TestSwingFrameInLWJGL implements Comportement {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					TestSwingFrameInLWJGL a = new TestSwingFrameInLWJGL();
					IHMSwingGame window = new IHMSwingGame(a, 600, 400, 800, 600);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void setText(Widget w, String txt) {
		Widget.drawText(w, txt, Color.BLUE, Color.RED,Color.white,20);
	}

	IHM ihm1;
	IHM ihm2;
	Button button;
	Map<Widget, String> msg;

	@Override
	public void init(Game game) {
		// TODO Auto-generated method stub
		msg = new HashMap<Widget, String>();

		 button = new Button(0);
		Game g = game;
		IHM ihm = g.nouvelleIHM();
		Widget wHautGauche;
		ihm.setSize(230, 32);
		ihm.beginX();
		ihm.beginY();
		wHautGauche = ihm.widget();
		setText(wHautGauche, "Haut gauche");
		msg.put(wHautGauche, "Haut gauche");
		ihm.space(400);
		Widget wBasGauche = ihm.widget();
		setText(wBasGauche, "Bas gauche");
		msg.put(wBasGauche, "Bas gauche");
		ihm.end();
		ihm.space(700);
		ihm.end();

		ihm1 = ihm;
		ihm = g.nouvelleIHM();
		ihm.setSize(230, 32);
		ihm.beginX();
		ihm.space(700);
		ihm.beginY();
		Widget wHautDroite = ihm.widget();
		setText(wHautDroite, "Haut droite");
		msg.put(wHautDroite, "Haut droite");
		ihm.space(400);
		Widget wBasDroite = ihm.widget();
		setText(wBasDroite, "Bas droite");
		msg.put(wBasDroite, "Bas droite");
		ihm.end();
		ihm.end();

		ihm2 = ihm;
		ihm.ajouterModel(() -> {
			setText(wBasDroite, msg.get(wBasDroite));
			setText(wBasGauche, msg.get(wBasGauche));
			setText(wHautDroite, msg.get(wHautDroite));
			setText(wHautGauche, msg.get(wHautGauche));

		});

	}

	Widget wUpdate;
	int count = 0;

	@Override
	public void loop(Game game) {
		// TODO Auto-generated method stub
		ihm1.dessiner(game.shaderWidget);
		ihm2.dessiner(game.shaderWidget);
		if (button.isPressed()) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			System.out.println("Button x=" + x + " y=" + y);
			Widget sel = ihm1.getWidgetAt(x, y);
			if (sel != null) {
				wUpdate = sel;

				System.out.println(msg.get(sel));
			}
			sel = ihm2.getWidgetAt(x, y);
			if (sel != null) {
				wUpdate = sel;

				System.out.println(msg.get(sel));
			}
		}
		count++;
		if (wUpdate != null) {
			msg.put(wUpdate, "[" + count + "]");

		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		ihm1.screenHeight = height;
		
		ihm1.screenWidth = width;
		ihm2.screenHeight = height;
		ihm2.screenWidth = width;

		
	}

	@Override
	public void terminate(Game game) {
		// TODO Auto-generated method stub
		
	}

}
