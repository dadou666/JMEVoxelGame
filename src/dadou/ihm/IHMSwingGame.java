package dadou.ihm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
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
import dadou.Fps;
import dadou.Game;
import dadou.ihm.IHM;
import dadou.ihm.Widget;

public class IHMSwingGame<T extends Comportement> {

	public static void main(String[] args) {

	}

	public void setToEffectiveScreenSize(JFrame frame, float width, float height) {
		float x, y;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame tmp = new JFrame();
		tmp.pack();
		Insets bounds = tmp.getInsets();

		// Calculate the height/length by subtracting the margins
		// (x,y) = ( (screenHeight-windowHeight)/2, (screenWidth -
		// windowWidth)/2 )

		width = width + (bounds.left);
		height = height + (bounds.top );
		System.out.println("bounds=" + bounds);
		// Now center the new rectangle inside the screen
		x = (float) ((screenSize.getHeight() - height) / 2.0);
		y = (float) ((screenSize.getWidth() - width) / 2.0);

		frame.setBounds((int) 0, (int)0, (int) width, (int) height);
	}

	public JFrame frame;
	public  Canvas canvas;
	private Thread gameThread;
	public Game game;
	private boolean running;
	private volatile boolean needValidation;
	private volatile boolean needUpdateViewport;
	public JPanel horizontalPanel;
	public JPanel verticalPanel;
	public int width = 600;
	public int height = 400;
	public T comportement;
	public JPanel canvasPanel ;
	
	public IHMSwingGame(T comportement, int canvasWidth, int canvasHeight,
			int screenWidth, int screenHeight) {
		frame = new JFrame();
		frame.setResizable(false);
		this.comportement = comportement;
		frame.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent arg0) {
			}

			public void windowIconified(WindowEvent arg0) {
			}

			public void windowDeiconified(WindowEvent arg0) {
			}

			public void windowDeactivated(WindowEvent arg0) {
			}

			public void windowClosing(WindowEvent arg0) {
			comportement.terminate(game);
			}

			public void windowClosed(WindowEvent arg0) {
				comportement.terminate(game);
			}

			public void windowActivated(WindowEvent arg0) {
			}
		});
		width = canvasWidth;
		height = canvasHeight;
	
		// frame.setBounds(100, 100, screenWidth, screenHeight);
		this.setToEffectiveScreenSize(frame, screenWidth, screenHeight);
	//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		needValidation = true;

		frame.getContentPane().setLayout(null);

		 canvasPanel = new JPanel();
		canvasPanel.setLayout(new BorderLayout(0, 0));
		frame.getContentPane().add(canvasPanel);

		game = new Game(canvasWidth, canvasHeight);
		canvas = game.canvas;
		canvas.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
				setNeedValidation();
			}

			public void componentResized(ComponentEvent e) {
				setNeedValidation();
			}

			public void componentMoved(ComponentEvent e) {
				setNeedValidation();
			}

			public void componentHidden(ComponentEvent e) {
				setNeedValidation();
			}
		});
		canvas.setIgnoreRepaint(true);
	
		canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
		canvas.setMinimumSize(new Dimension(canvasWidth, canvasHeight));
		canvas.setBounds(screenWidth - canvasWidth,
				screenHeight - canvasHeight, canvasWidth, canvasHeight);
		canvas.setVisible(true);
		canvasPanel.setBounds(screenWidth - canvasWidth - 1, 0, canvasWidth,
				canvasHeight);
		canvasPanel.add(canvas, BorderLayout.CENTER);

		startOpenGL();
	}

	private void setNeedValidation() {
		needValidation = true;
		needUpdateViewport = true;
	}

	private void startOpenGL() {
		System.out.println("StartOpenGL");

		gameThread = new Thread() {
			public void run() {
				try {
					Display.create();
					Display.setParent(canvas);

					Rectangle rect = canvas.getBounds();
					int w = (int) rect.getWidth();
					int h = (int) rect.getHeight();
				//	System.out.println(" width=" + w + " height=" + h);
					game.startCanvas(w, h);

					init();
					loop();

				} catch (Exception e) {
					e.printStackTrace();
				}

				if (Display.isCreated()) {
					Display.destroy();
				}
			}
		};
		gameThread.start();
	}

	public void init() throws Exception {
		comportement.init(game);
	}	public void loop() throws Exception {

		while (true) {
			game.clear();
			Game.fpsGlobal.calculer(() -> {
				try {
					comportement.loop(game);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		
			if (needUpdateViewport) {
				needUpdateViewport = false;

				Rectangle rect = canvas.getBounds();
				width = (int) rect.getWidth();
				height = (int) rect.getHeight();
				game.resize(width, height);
				comportement.resize(width, height);

			}
			Display.update();
			Display.sync(30);

		}
		// game.displayBackBuffer();
	}

	private void stopOpenGL() {
		System.out.println("StopOpenGL");

		running = false;
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void terminate() {
		frame.dispose();
		System.exit(0);
	}

}
