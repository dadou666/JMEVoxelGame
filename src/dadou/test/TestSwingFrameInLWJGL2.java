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

public class TestSwingFrameInLWJGL2 implements Comportement {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					TestSwingFrameInLWJGL2 a = new TestSwingFrameInLWJGL2();
					IHMSwingGame window = new IHMSwingGame(a, 1600, 900, 1800, 900);
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
	Info i;

	@Override
	public void init(Game game) {
		// TODO Auto-generated method stub
		// i=new Info(game);
		ihm1 = game.nouvelleIHM();
		ihm1.beginX();
		ihm1.space(600);
		ihm1.beginY();
		ihm1.space(600);
		ihm1.setSize(150,300);
		Widget w = ihm1.widget();
	
		ihm1.end();
		ihm1.end();
		setText(w, "Marche pas");
	}

	Widget wUpdate;
	int count = 0;

	@Override
	public void loop(Game game) {
		// TODO Auto-generated method stub
	//i.dessiner();
		ihm1.dessiner(game.shaderWidget);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		//i.ihm.screenHeight = height;
		//i.ihm.screenWidth = width;
		/*ihm1.screenHeight = height;
		ihm1.screenWidth = width;
		ihm2.screenHeight = height;
		ihm2.screenWidth = width;*/

		
	}

	@Override
	public void terminate(Game game) {
		// TODO Auto-generated method stub
		
	}
	
}
