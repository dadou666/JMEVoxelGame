package dadou.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class TestPartition extends JFrame implements MouseListener, KeyListener {

	int dx = 4;
	int dy = 4;
	int cx = 40;
	int cy = 40;
	List<Point> points = new ArrayList<>();
	PartitionneurSimple part;

	TestPartition() {
		
		super();
		part = new PartitionneurSimple(dx, dy);
		part.initialiser();
		setSize(dx * cx+2*cx, dy * cy+2*cy);
		this.addMouseListener(this);
		setVisible(true);
	
		this.addKeyListener(this);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TestPartition();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = new Point((e.getX()) / cx, (e.getY()) / cy);
		points.add(p);
		System.out.println(points);
		part.donnees[p.x-1][p.y-1] = true;
		this.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		g.clearRect(cx, cy, (dx)*cx, (dy)*cy);
		g.draw3DRect(cx,cy, (dx)*cx, (dy)*cy, true);
		if (part.rs.isEmpty()) {
			for (Point p : points) {
				g.draw3DRect(p.x * cx, p.y * cy, cx, cy, true);
			}
		} else {
			
			for (Rectangle r : part.rs) {
				g.setColor(Color.GREEN);
				g.fill3DRect((r.x+1) * cx, (r.y+1) * cy, r.width * cx, r.height * cy,
						true);
				g.setColor(Color.RED);
				g.draw3DRect((r.x+1) * cx, (r.y+1) * cy, r.width * cx, r.height * cy,
						true);
				
			}

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("KEY PRESSED");
		if (part.rs.isEmpty()) {
			part.calculer();
			System.out.println(part.rs);
		} else {
			part.rs = new ArrayList<>();
			part.initialiser();
			points = new ArrayList<Point>();
		}
		this.repaint();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
