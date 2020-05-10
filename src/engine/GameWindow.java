package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import backends.objs.AppPage;
import backends.objs.Clipping;
import backends.objs.ImageItem;
import backends.objs.Overlay;
import backends.objs.Vector2D;
import online.client.Client;
import online.client.EntityClient;
import online.server.EntityServer;
import online.server.Server;

public class GameWindow extends Collector implements Serializable {

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Immediate Frame Variables
	private CustomPanel customPanel;
	private JFrame frame = null;
	private transient Thread thread = null;
	
	//"Programming" Variables
	private int WIDTH = -1, HEIGHT = -1, HALF_W = -1, HALF_H = -1;
	private double H12, W12;
	private Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT, LEFT_RECT, RIGHT_RECT;
	private Vector2D SCREEN_CENTER;

	// Key Variables
	private Map<Integer, Boolean> keys = new HashMap<>();
	private Map<Integer, Boolean> keyUp = new HashMap<>();

	// Images
	private ArrayList<ImageItem> images = null;
	
	//Audio
	private ArrayList<Clipping> audio = null;
	
	//Starting Panel
	StartingPanel sp;
	
	//Server and Client starting Variables
	public Server server;
	public Client client;
	public EntityServer eServer;
	public EntityClient eClient;

	/**
	 * @author Xavier Bennett
	 * @param name This is the name of the created window
	 */
	public GameWindow(String[] args) {
		start(this, args);
		
		customPanel = new CustomPanel(this);

		thread = new Thread(customPanel);

		// Setting Up Images
		images = getHandlers().getImageHandler().getAllImages("img/");
		
		//Audio Init
		audio = new ArrayList<>();
		
		//Setup and create JFrame
		frame = new JFrame(WINDOW_NAME);

		customPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				keys.put(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keys.put(e.getKeyCode(), false);
				if(!getHandlers().getTransHandler().transitioning) {
					keyUp.put(e.getKeyCode(), true);
				}

				if (e.getKeyCode() == EXIT) {
					if (getHandlers().getPageHandler().compareAppPage("credit") || getHandlers().getPageHandler().compareAppPage("mainMenu")) {
						stop();
					}
				} else if (e.getKeyCode() == ENTER || e.getKeyCode() == ENTER_ALT) {
					if (getHandlers().getPageHandler().compareAppPage("credit")) {
						getHandlers().getPageHandler().setCurrentPage("mainMenu", true);
					}
				}
			}

		});
		
		frame.setLayout(null);
		frame.add(customPanel);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		
		if(FORCE_FULLSCREEN) {
			fullscreenExclusive(MAIN_SCREEN);
			customPanel.init();
		}else {
			sp = new StartingPanel(this);
			sp.preInit();
			
			//Setup Starting Window
			setWindowSize(200, 200);
			setVisible();
		}

	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public CustomPanel getPanel() {
		return customPanel;
	}

	/**
	 * @author Xavier Bennett
	 * @desc Creates a fullscreen window
	 */
	public void setFullscreen() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setFocusable(true);
		customPanel.requestFocus();
	}
	
	/**
	 * @author Xavier Bennett
	 * @desc Fullscreen Exclusive Mode with multi-monitor system
	 */
	public void fullscreenExclusive(int screenNum) {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getScreenDevices()[screenNum];
		if(device.isFullScreenSupported()) {
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			device.setFullScreenWindow(frame);
			frame.setFocusable(true);
			customPanel.requestFocus();
		}else {
			setFullscreen();
		}
		windowSetup();
	}

	/**
	 * @author Xavier Bennett
	 * @param minimumSize creates a window with these dimensions in the middle of
	 *                    the screen...
	 */
	public void setWindowSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		customPanel.requestFocus();
	}

	public void setWindowSize(int x, int y) {
		setWindowSize(new Dimension(x, y));
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
		customPanel.addMouseListener(l);
		frame.validate();
	}

	public void addMouseMotionListen(MouseMotionListener l) {
		customPanel.addMouseMotionListener(l);
		frame.validate();
	}

	public void addMouseWheelListen(MouseWheelListener l) {
		customPanel.addMouseWheelListener(l);
		frame.validate();
	}

	public void addComp(Component c) {
		customPanel.add(c);
		frame.validate();
	}

	public void setMenuBar(JMenuBar j) {
		frame.setJMenuBar(j);
		frame.validate();
	}
	
	public void removeMouseListen(MouseListener l) {
		customPanel.removeMouseListener(l);
		frame.validate();
	}
	
	public void removeMouseMotionListen(MouseMotionListener l) {
		customPanel.removeMouseMotionListener(l);
		frame.validate();
	}

	public void removeMouseWheelListen(MouseWheelListener l) {
		customPanel.removeMouseWheelListener(l);
		frame.validate();
	}

	public void removeComp(Component c) {
		customPanel.remove(c);
		frame.validate();
	}

	public void setVisible() {

		frame.validate();
		frame.pack();
		frame.setVisible(true);

		windowSetup();
		
	}
	
	private void windowSetup() {
		WIDTH = frame.getWidth();
		HALF_W = WIDTH / 2;
			
		HEIGHT = frame.getHeight();
		HALF_H = HEIGHT / 2;

		WINDOW_RECT = new Rectangle(WIDTH, HEIGHT);
		TOP_RECT = new Rectangle(0, 0, WIDTH, HALF_H);
		BOTTOM_RECT = new Rectangle(0, HALF_H, WIDTH, HALF_H);
		LEFT_RECT = new Rectangle(0, 0, HALF_W, HEIGHT);
		RIGHT_RECT = new Rectangle(HALF_W, 0, HALF_W, HEIGHT);
		W12 = (int) Math.floor(WIDTH / 12);
		H12 = (int) Math.floor(HEIGHT / 12);
		
		//Setting middle of screen
		SCREEN_CENTER = new Vector2D(frame.getLocationOnScreen().x+HALF_W, frame.getLocationOnScreen().y+HALF_H);
		
		postInit(this);
			
		if(thread.getState().equals(Thread.State.NEW)) {
			
			if(sp != null) {
				sp.init();
				getHandlers().getPageHandler().addPage(sp);
			}
			
			Clipping[] bgm = getHandlers().getAudioHandler().loadClippings("audio/bgm/");
			Clipping[] sprite = getHandlers().getAudioHandler().loadClippings("audio/sprite/");
			
			for(Clipping clip : bgm) {
				audio.add(clip);
			}
			for(Clipping clip : sprite) {
				audio.add(clip);
			}
		
			thread.start();
		}
	}
	
	public void loadAssets() {
		// Adding Pages
		String[] pageList = getHandlers().getFileHandler().getFilesFromDir(getClass(), "pages/");
		String[] overlayList = getHandlers().getFileHandler().getFilesFromDir(getClass(), "overlays/");
		try {
			for (String pn : pageList) {
				if (!pn.isEmpty() && !pn.matches(".*\\d.*")) {
					Class<?> c = Class.forName("pages." + pn.split("\\.")[0]);
					Constructor<?> con = c.getConstructor(new Class[] { GameWindow.class });
					Object o = con.newInstance(this);
					if (o instanceof AppPage) {
						AppPage ap = (AppPage) o;
						if(getHandlers().getPageHandler().containsAppPage(ap)) {
							System.out.println("The AppPage, "+ap.getID()+" was already there...");
						}else {
							System.out.println("The AppPage, "+ap.getID()+" has been added.");
							getHandlers().getPageHandler().addPage(ap);
						}
					}
				}
			}
			for (String pn : overlayList) {
				if (!pn.isEmpty() && !pn.matches(".*\\d.*")) {
					Class<?> c = Class.forName("overlays." + pn.split("\\.")[0]);
					Constructor<?> con = c.getConstructor(new Class[] { GameWindow.class });
					Object o = con.newInstance(this);
					if (o instanceof Overlay) {
						Overlay ap = (Overlay) o;
						if(getHandlers().getPageHandler().containsOverlay(ap)) {
							System.out.println("The Overlay, "+ap.getID()+" was already there...");
						}else {
							System.out.println("The Overlay, "+ap.getID()+" has been added.");
							getHandlers().getPageHandler().addOverlay(ap);
						}
					}
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		getHandlers().getPageHandler().getCurrentAppPage().onChange();
		frame.setVisible(false);
		System.exit(-1);
	}

	private void paintPage(Graphics2D g2d) {
		AppPage ap = getHandlers().getPageHandler().getCurrentLoadedPage();
		if(ap != null) {
			ap.paint(g2d);
			getHandlers().getEntityHandler().paint(g2d);
			Overlay o = getHandlers().getPageHandler().getCurrentOverlay();
			if(o != null) {
				o.paint(g2d);
			}
		}
	}
	
	public void paint(Graphics2D g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		if (!getHandlers().getTransHandler().transitioning) {
			paintPage(g);
		} else {
			g.drawImage(getHandlers().getTransHandler().getTransition().getImage(), 0, 0, null);
		}
		if(getTrophy() != null) {
			getTrophy().paint(g);
		}
		getDebug().paint(g);
	}

	private void updatePage(double delta) {
		AppPage ap = getHandlers().getPageHandler().getCurrentAppPage();
		if(ap != null) {
			ap.update(delta);
			getHandlers().getEntityHandler().update(delta);
			if(getHandlers().getPageHandler().getCurrentLoadedOverlay() != null) {
				Overlay o = getHandlers().getPageHandler().getCurrentOverlay();
				if(o != null) {
					o.update(delta);
				}
			}
		}
	}
	
	public void update(double delta) {
		if (!getHandlers().getTransHandler().transitioning) {
			updatePage(delta);
		}
		if(getTrophy() != null) {
			getTrophy().update();
		}
	}

	public boolean keyIsDown(int key) {
		return keys.containsKey(key) ? keys.get(key) : false;
	}
	
	public boolean keyIsUp(int key) {
		if(keyUp.containsKey(key)) {
			boolean ret = keyUp.get(key);
			keyUp.put(key, false);
			return ret;
		}
		return false;
	}
	
	public Map<Integer, Boolean> getKeys(){
		return keys;
	}

	public ImageIcon getImage(String name) {
		if(images != null && images.size() > 0) {
			for (ImageItem i : images) {
				if (i.ID.equals(name) || i.ID.contains(name)) {
					return i.img;
				}
			}
		}
		return null;
	}
	
	public void resetKeyUp() {
		for(Map.Entry<Integer, Boolean> entry : keyUp.entrySet()) {
			keyUp.put(entry.getKey(), false);
		}
	}
	
	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getHALF_W() {
		return HALF_W;
	}

	public int getHALF_H() {
		return HALF_H;
	}

	public double getH12() {
		return H12;
	}

	public double getW12() {
		return W12;
	}

	public Rectangle getWINDOW_RECT() {
		return WINDOW_RECT;
	}

	public Rectangle getTOP_RECT() {
		return TOP_RECT;
	}

	public Rectangle getBOTTOM_RECT() {
		return BOTTOM_RECT;
	}

	public Rectangle getLEFT_RECT() {
		return LEFT_RECT;
	}

	public Rectangle getRIGHT_RECT() {
		return RIGHT_RECT;
	}

	public Vector2D getSCREEN_CENTER() {
		return SCREEN_CENTER;
	}
}