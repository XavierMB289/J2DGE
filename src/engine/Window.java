package engine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import backends.AppPage;
import backends.ImageItem;
import coffeeDev.Credit;
import interfaces.Functions;
import interfaces.Keys;

public class Window extends JPanel implements Runnable, Keys, Functions{

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Debug Variables
	public final String[] args;
	private String DEBUG_LEVEL;
	
	//Frame Variables
	private JFrame frame = null;
	
	private Thread thread = null;
	
	public int WIDTH, HEIGHT;
	
	public Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT;
	
	public int H12, W12;
	
	//Imports
	private Credit c = null;
	public EventHandler EH = null;
	public AudioHandler AH = null;
	
	//Page Variables
	public ArrayList<AppPage> pages = null;
	public String currentPage = "credit";
	
	//Images
	public ArrayList<ImageItem> images = null;
	
	/**
	 * @author Xavier Bennett
	 * @param name This is the name of the created window
	 */
	public Window(String name, String[] args) {
		
		thread = new Thread(this);
		
		//Commands
		this.args = args;
		DEBUG_LEVEL = ArrayContains(args, "-debug=");
		DEBUG_LEVEL = DEBUG_LEVEL.equals(null) ? "0" : DEBUG_LEVEL.split("=")[1];
		
		//Imports
		c = new Credit(this);
		EH = new EventHandler();
		AH = new AudioHandler();
		
		pages = new ArrayList<>();
		
		pages.add(c);
		
		//Setting Up Images
		images = new ImageHandler().getAllImages("/img/");
		
		frame = new JFrame(name);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				keys.put(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keys.put(e.getKeyCode(), false);
				if(e.getKeyCode() == ExitKey) {
					if(currentPage.equals("credit") || currentPage.equals("mainMenu")) {
						stop();
					}
				}else if(e.getKeyCode() == ENTER || e.getKeyCode() == ENTER_ALT) {
					if(currentPage.equals("credit")) {
						currentPage = "mainMenu";
					}
				}
			}
			
		});
		
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
		TOP_RECT = new Rectangle(0, 0, WIDTH, (int)(HEIGHT/2));
		BOTTOM_RECT = new Rectangle(0, (int)(HEIGHT/2), WIDTH, (int)(HEIGHT/2));
		W12 = (int)Math.floor(WIDTH/12);
		H12 = (int)Math.floor(HEIGHT/12);
		
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
		//Debugging
		if(DEBUG_LEVEL.equals("1")) {
			String buttons = "";
			for(Map.Entry<Integer, Boolean> entry : keys.entrySet()) {
				if(entry.getValue()) {
					buttons += KeyEvent.getKeyText(entry.getKey());
				}
			}
			g2d.setColor(Color.RED);
			g2d.drawString(buttons, 10, 50);
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
	
	public void drawCenteredImage(Graphics2D g, Image i, Rectangle rect) {
		int iCX = i.getWidth(null)/2;
		int iCY = i.getHeight(null)/2;
		int rCX = rect.width/2;
		int rCY = rect.height/2;
		g.drawImage(i, rCX-iCX, rCY-iCY, null);
	}
	
	public static void addFont(int fontFormat, String fontNamePath) {
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(fontFormat, new File(Window.class.getResource(fontNamePath).getFile())));
		} catch (IOException|FontFormatException e) {
			e.printStackTrace();
		}
	}
	
	public Image getImage(String name) {
		for(ImageItem i : images) {
			if(i.ID.equals(name)) {
				return i.img;
			}
		}
		return null;
	}

}
