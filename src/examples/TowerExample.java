package examples;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import engine.Window;
import objs.Tower;

public class TowerExample extends Window{
	
	private static final long serialVersionUID = -7864489152398171062L;
	
	int mx, my;
	Point towerPoint = new Point(50, 50);
	
	//Painting/updating variables
	Tower tower = null;

	public TowerExample() {
		super("Example Window");
		setWindowSize(new Dimension(100, 100));
		addKeyListen(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ESCAPE) {
					stop();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		addMouseMotionListen(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				mx = e.getX();
				my = e.getY();
			}
			
		});
		tower = new Tower();
		setVisible();		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.translate(50, 50);
		AffineTransform at = tower.rotateTowards(g2d, towerPoint, new Point(mx, my));
		g.drawLine(0, 0, 25, 0);
		g2d.setTransform(at);
	}
	
	@Override
	public void update() {
	}
	
	public static void main(String[] args) {
		new TowerExample();
	}

}