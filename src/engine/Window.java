package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import backends.AppPage;
import backends.Entity;
import backends.Functions;
import backends.ImageItem;
import coffeeDev.Credit;
import interfaces.Config;

public class Window extends JPanel implements Runnable, Config, Serializable{

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Debug Variables
	public final String[] args;
	private String DEBUG_LEVEL;
	
	//Frame Variables
	private JFrame frame = null;
	
	private transient Thread thread = null;
	
	public int WIDTH=-1, HEIGHT=-1, HALF_W=-1, HALF_H=-1;
	
	public Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT;
	private Rectangle DEBUG_RECT;
	
	public Point ORIGINAL_MIDDLE;
	
	public double H12, W12;
	
	//Imports
	private Credit c = null;
	public EventHandler EH = null;
	public AudioHandler AH = null;
	public ImageHandler IH = null;
	public TransitionHandler TH = null;
	public FileHandler FH = null;
	public Functions functions = null;
	
	//Page Variables
	public ArrayList<AppPage> pages = null;
	public String currentPage = "credit";
	
	//Entity Variables
	public ArrayList<Entity> entity = null;
	public ArrayList<Entity> removeEnt = null;
	private int tick = 3;
	
	//Key Variables
	public Map<Integer, Boolean> keys = new HashMap<>();
	
	//Images
	public ArrayList<ImageItem> images = null;
	
	/**
	 * @author Xavier Bennett
	 * @param name This is the name of the created window
	 */
	public Window(String name, String[] args) {
		
		thread = new Thread(this);
		
		//Imports
		EH = new EventHandler();
		AH = new AudioHandler();
		IH = new ImageHandler();
		TH = new TransitionHandler(this);
		FH = new FileHandler(this);
		functions = new Functions();
		c = new Credit(this);
		
		//Commands
		this.args = args;
		if(args != null && args.length > 0) {
			DEBUG_LEVEL = functions.ArrayContains(args, "-debug=");
			DEBUG_LEVEL = DEBUG_LEVEL.equals(null) ? "0" : DEBUG_LEVEL.split("=")[1];
		}else {
			DEBUG_LEVEL = "0";
		}
		
		pages = new ArrayList<>();
		
		//Setting Up Entities
		entity = new ArrayList<>();
		removeEnt = new ArrayList<>();
		
		//Setting Up Images
		if(FH.fileExists("/img/")) {
			images = IH.getAllImages("/img/");
		}
		
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
				if(e.getKeyCode() == EXIT) {
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
	
	public void setLocation(int x, int y) {
		frame.setLocation(x, y);
	}
	
	public void addMouseListen(MouseListener l) {
		frame.addMouseListener(l);
		frame.validate();
	}
	
	public void addMouseMotionListen(MouseMotionListener l) {
		frame.addMouseMotionListener(l);
		frame.validate();
	}
	
	public void addMouseWheelListen(MouseWheelListener l) {
		frame.addMouseWheelListener(l);
		frame.validate();
	}
	
	public void addComp(Component c) {
		frame.add(c);
		frame.validate();
	}
	
	public void setMenuBar(JMenuBar j) {
		frame.setJMenuBar(j);
		frame.validate();
	}
	
	@SuppressWarnings("rawtypes")
	public void setVisible() {
		
		frame.validate();
		frame.pack();
		frame.setVisible(true);
		if(WIDTH == -1) {
			WIDTH = frame.getWidth();
			HALF_W = WIDTH/2;
		}
		if(HEIGHT == -1) {
			HEIGHT = frame.getHeight();
			HALF_H = HEIGHT/2;
		}
		WINDOW_RECT = new Rectangle(WIDTH, HEIGHT);
		TOP_RECT = new Rectangle(0, 0, WIDTH, (int)(HEIGHT/2));
		BOTTOM_RECT = new Rectangle(0, (int)(HEIGHT/2), WIDTH, (int)(HEIGHT/2));
		W12 = (int)Math.floor(WIDTH/12);
		H12 = (int)Math.floor(HEIGHT/12);
		DEBUG_RECT = new Rectangle(0, 0, (int)W12, (int)H12*2);
		ORIGINAL_MIDDLE = new Point(frame.getX(), frame.getY());
		
		c.init();
		pages.add(c);
		
		//Adding Pages
		String[] dirList = FH.getFilesFromDir(getClass(), "pages/");
		for(String pn : dirList) {
			try {
				if(!pn.isEmpty() && !pn.matches(".*\\d.*")) {
					Class<?> c = Class.forName("pages."+pn.split("\\.")[0]);
					Constructor con = c.getConstructor(new Class[] {Window.class});
					Object o = con.newInstance(this);
					if(o instanceof AppPage) {
						pages.add((AppPage) o);
						System.out.println("Page "+((AppPage)o).getID()+" has been successfully added.");
					}
				}
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		if(!TH.transitioning) {
			for(AppPage p : pages) {
				if(p.getID().equals(currentPage)) {
					p.paint(g2d);
				}
			}
			if(entity.size() > 0) {
				for(Entity e : entity) {
					if(e.getID().equals(currentPage)) {
						e.paint(g2d);
					}
				}
			}
		}else {
			g2d.drawImage(TH.getTransition().getImage(), 0, 0, null);
		}
		//Debugging
		if(DEBUG_LEVEL.equals("1")) {
			String buttons = "";
			for(Map.Entry<Integer, Boolean> entry : keys.entrySet()) {
				if(entry.getValue()) {
					buttons += KeyEvent.getKeyText(entry.getKey());
				}
			}
			g2d.setFont(new Font("Arial", Font.PLAIN, 24));
			functions.debugText(g2d, buttons, DEBUG_RECT);
		}
		this.update();
	}

	public void update() {
		try {
			if(!TH.transitioning) {
				for(AppPage p : pages) {
					if(p.getID().equals(currentPage)) {
						p.update();
					}
				}
				if(entity.size() > 0) {
					if(tick <= 0) {
						tick = 3;
						
						for(Entity e : entity) {
							if(e.getID().equals(currentPage)) {
								e.onTick();
							}
						}
						if(removeEnt.size() > 0) {
							for(Entity e : removeEnt) {
								entity.remove(e);
							}
							removeEnt = new ArrayList<>();
						}
					}else {
						tick--;
					}
				}
			}
		}catch(ConcurrentModificationException e) {
			System.err.println("CME in engine.Window.update");
		}
	}
	
	public void resetCheck() {
		if(thread == null) {
			thread = new Thread(this);
		}
	}
	
	public boolean keyIsDown(int key) {
		return keys.containsKey(key) ? keys.get(key) : false;
	}
	
	public ImageIcon getImage(String name) {
		for(ImageItem i : images) {
			if(i.ID.equals(name)) {
				return i.img;
			}
		}
		return null;
	}
	
	public AppPage getPage(String id) {
		for(AppPage p : pages) {
			if(p.getID().equals(id)) {
				return p;
			}
		}
		return null;
	}

}
