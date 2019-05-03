package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import backends.AppPage;
import backends.ImageItem;
import coffeeDev.Credit;

public class Window extends JPanel implements Runnable{

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Frame Variables
	private JFrame frame = null;
	
	private Thread thread = null;
	
	public int WIDTH, HEIGHT;
	
	public Rectangle WINDOW_RECT;
	
	//Imports
	public Credit c = null;
	
	//Page Variables
	public ArrayList<AppPage> pages = null;
	public String currentPage = "credit";
	
	//Images
	ArrayList<ImageItem> images = null;
	
	/**
	 * @author Xavier Bennett
	 * @param name This is the name of the created window
	 */
	public Window(String name) {
		
		thread = new Thread(this);
		
		//Imports
		c = new Credit(this);
		
		pages = new ArrayList<>();
		
		pages.add(c);
		
		//Setting Up Images
		images = new ImageHandler().getAllImages("/img/");
		
		frame = new JFrame(name);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		
	}
	
	/**
	 * @author Xavier Bennett
	 * Creates a fullscreen window
	 */
	public void setFullscreen() {
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setFocusable(true);
		this.requestFocus();
	}
	
	/**
	 * @author Xavier Bennett
	 * @param minimumSize creates a window with these dimensions in the middle of the screen...
	 */
	public void setWindowSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		this.requestFocus();
	}
	
	/**
	 * @author Xavier Bennett
	 * @param addX the value to add to the X position of the window
	 * @param addY the value to add to the Y position of the window
	 */
	public void changeLocation(int addX, int addY) {
		frame.setLocation(frame.getX() + addX, frame.getY() + addY);
	}
	
	public void addKeyListen(KeyListener l) {
		frame.addKeyListener(l);
	}
	
	public void addMouseListen(MouseListener l) {
		frame.addMouseListener(l);
	}
	
	public void addMouseMotionListen(MouseMotionListener l) {
		frame.addMouseMotionListener(l);
	}
	
	public void addMouseWheelListen(MouseWheelListener l) {
		frame.addMouseWheelListener(l);
	}
	
	public void addComp(Component c) {
		frame.add(c);
	}
	
	public void setMenuBar(JMenuBar j) {
		frame.setJMenuBar(j);
	}
	
	public void setVisible() {
		frame.validate();
		frame.pack();
		frame.setVisible(true);
		WIDTH = frame.getWidth();
		HEIGHT = frame.getHeight();
		WINDOW_RECT = new Rectangle(WIDTH, HEIGHT);
		
		//inits
		c.init();
		
		thread.start();
	}
	
	public synchronized void stop() {
		frame.setVisible(false);
		System.exit(-1);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;//60 times per second
		double delta = 0;
		while(frame.isVisible()) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				this.repaint();
				delta--;
			}
		}
		stop();
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		for(AppPage p : pages) {
			if(p.getID().equals(currentPage)) {
				p.paint(g2d);
			}
		}
		this.update();
	}

	public void update() {
		for(AppPage p : pages) {
			if(p.getID().equals(currentPage)) {
				p.update();
			}
		}
	}
	
	public void drawCenteredString(Graphics2D g, String text, Rectangle rect) {
		Font font = g.getFont();
		
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}

}
