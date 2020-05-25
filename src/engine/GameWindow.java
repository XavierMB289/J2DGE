package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

import backends.objs.AppPage;
import backends.objs.Clipping;
import backends.objs.ImageItem;
import backends.objs.Overlay;
import online.client.Client;
import online.client.EntityClient;
import online.server.EntityServer;
import online.server.Server;

public class GameWindow extends Collector implements Serializable {

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Immediate Frame Variables
	private GameLoop gameLoop;
	private GameFrame frame = null;
	private transient Thread thread = null;
	public transient BufferedImage paintImg = null;

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
		preInitPages();
		
		//Setup and create JFrame
		frame = new GameFrame(this);
		frame.setTitle(WINDOW_NAME);

		frame.addKeyListener(new KeyListener() {

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
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		
		setWindowSize(200, 200);
			
		sp = new StartingPanel(this);
		sp.init();
		getHandlers().getPageHandler().addPage(sp);
			
		//Setup Starting Window
		setVisible();

	}
	
	public GameFrame getFrame() {
		return frame;
	}
	
	public GameLoop getLoop() {
		return gameLoop;
	}

	/**
	 * @author Xavier Bennett
	 * @desc Creates a fullscreen window
	 */
	public void setFullscreen() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setFocusable(true);
		frame.requestFocus();
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
			frame.requestFocus();
		}else {
			setFullscreen();
		}
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
		frame.requestFocus();
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
	
	public void removeMouseListen(MouseListener l) {
		frame.removeMouseListener(l);
	}
	
	public void removeMouseMotionListen(MouseMotionListener l) {
		frame.removeMouseMotionListener(l);
	}

	public void removeMouseWheelListen(MouseWheelListener l) {
		frame.removeMouseWheelListener(l);
	}

	public void removeComp(Component c) {
		frame.remove(c);
	}
	
	public void removeAll() {
		frame.removeAll();
	}

	public void setVisible() {
		
		frame.setVisible(false);
		frame.validate();
		frame.pack();
		frame.setVisible(true);
		
		frame.init();
		
	}
	
	void windowSetup() {
		
		getHandlers().start(this);
		
		gameLoop = new GameLoop(this);

		thread = new Thread(gameLoop);

		// Setting Up Images
		images = getHandlers().getImageHandler().getAllImages("img/");
		
		//Audio Init
		audio = new ArrayList<>();
		
		postInit(this);
			
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
		try {
			getHandlers().getPageHandler().getCurrentAppPage().onChange();
			getLoop().RUNNING = false;
			frame.setVisible(false);
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	
	public void paint(Graphics gr) {
		try {
			if(paintImg != null){
				Graphics2D g = (Graphics2D) paintImg.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				if(getHandlers().getPageHandler().compareAppPage("start") != true) {
					g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
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
				g.dispose();
				
				gr.drawImage(paintImg, 0, 0, null);
			}
		}catch(NullPointerException e) {
			System.err.println("Null Pointer in "+e.getStackTrace()[0]);
		}catch(Exception e) {
			System.err.println(e.getStackTrace()[0]);
		}
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
	
}