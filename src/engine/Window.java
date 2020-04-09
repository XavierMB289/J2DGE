package engine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
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

import achievement.Trophy;
import achievement.TrophyCallback;
import backends.AppPage;
import backends.Clipping;
import backends.Functions;
import backends.ImageItem;
import backends.Overlay;
import debug.Debug;
import handler.AudioHandler;
import handler.EntityHandler;
import handler.EventHandler;
import handler.FileHandler;
import handler.ImageHandler;
import handler.TransitionHandler;
import interfaces.Config;
import interfaces.EngineChanges;
import objs.progress.ProgressionCheck;
import online.client.Client;
import online.client.EntityClient;
import online.server.EntityServer;
import online.server.Server;

public class Window extends EngineChanges implements Config, Serializable {

	private static final long serialVersionUID = 5651871526801520822L;
	
	//Private JPanel
	private CustomPanel customPanel;

	// Frame Variables
	private JFrame frame = null;

	private transient Thread thread = null;

	public int WIDTH = -1, HEIGHT = -1, HALF_W = -1, HALF_H = -1;
	public double H12, W12;

	//Window Graphics Variables
	public Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT, LEFT_RECT, RIGHT_RECT;
	
	//Window Location Variables
	public Point SCREEN_CENTER;

	// Imports
	public Debug debug = null;
	public EventHandler EventH = null;
	public AudioHandler AudioH = null;
	public ImageHandler ImageH = null;
	public TransitionHandler TransH = null;
	public FileHandler FileH = null;
	public EntityHandler EntityH = null;
	public Functions functions = null;
	public Trophy trophy = null;

	// Page and Overlay Variables
	private Map<String, AppPage> pages = new HashMap<>();
	private String currentPage = "start";
	private Map<String, Overlay> overlays = new HashMap<>();
	private String currentOverlay = "";

	// Key Variables
	private Map<Integer, Boolean> keys = new HashMap<>();
	private Map<Integer, Boolean> keyUp = new HashMap<>();
	
	//Global Variables
	Map<String, Object> globals;

	// Images
	private ArrayList<ImageItem> images = null;
	
	//Audio
	private ArrayList<Clipping> audio = null;
	
	//ProgressionChecks
	public ProgressionCheck pageCheck;
	public ProgressionCheck imageCheck;
	
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
	public Window(String[] args) {
		
		customPanel = new CustomPanel(this);

		thread = new Thread(customPanel);
		
		globals = new HashMap<>();
		
		pageCheck = new ProgressionCheck();
		imageCheck = new ProgressionCheck();

		// Imports
		debug = new Debug(this);
		EventH = new EventHandler();
		AudioH = new AudioHandler();
		ImageH = new ImageHandler(this);
		TransH = new TransitionHandler(this);
		FileH = new FileHandler(this);
		EntityH = new EntityHandler(this);
		functions = new Functions();
		
		//Commands
		if (args != null && args.length > 0) {
			debug.init(args);
		}

		pages = new HashMap<>();
		overlays = new HashMap<>();

		// Setting Up Images
		images = ImageH.getAllImages("img/");
		
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
				if(!TransH.transitioning) {
					keyUp.put(e.getKeyCode(), true);
				}

				if (e.getKeyCode() == EXIT) {
					if (currentPage.equals("credit") || currentPage.equals("mainMenu")) {
						stop();
					}
				} else if (e.getKeyCode() == ENTER || e.getKeyCode() == ENTER_ALT) {
					if (currentPage.equals("credit")) {
						setCurrentPage("mainMenu", true);
					}
				}
			}

		});
		
		frame.setLayout(null);
		frame.add(customPanel);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
		
		if(FORCE_FULLSCREEN) {
			fullscreenExclusive(0);
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

	public void setCurrentPage(String pageId, boolean init) {
		if(pages.get(pageId) != null) {
			//Only if there is a new page to go to, make sure to "exit" old page
			if(init) {
				if(getCurrentAppPage() != null) {
					getCurrentAppPage().onChange();
				}
				AppPage temp = pages.get(pageId);
				temp.init();
			}
			currentPage = pageId;
		} else {
			System.err.println("The page "+currentPage+" was not properly loaded in engine.Window");
			System.exit(-1);
		}
	}
	
	public void addPage(AppPage p) {
		if(!pages.containsValue(p)) {
			pages.put(p.getID(), p);
		}
	}
	
	public String getCurrentPage() {
		return currentPage;
	}
	
	public AppPage getCurrentAppPage() {
		return pages.get(currentPage);
	}
	
	public AppPage getLoadedPage(String page) {
		return pages.containsKey(page) ? pages.get(page) : null;
	}
	
	public Overlay getLoadedOverlay(String over) {
		return pages.containsKey(over) ? overlays.get(over) : null;
	}
	
	public void setCurrentOverlay(String id) {
		currentOverlay = id;
		if(overlays.get(currentOverlay) != null) {
			overlays.get(currentOverlay).init();
		}
	}
	
	public String getCurrentOverlay() {
		return currentOverlay;
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
		SCREEN_CENTER = frame.getLocationOnScreen();
		SCREEN_CENTER = new Point(SCREEN_CENTER.x+HALF_W, SCREEN_CENTER.y+HALF_H);
		
		debug.setupRect();
		trophy = new Trophy(this);
			
		if(thread.getState().equals(Thread.State.NEW)) {
			
			if(sp != null) {
				sp.init();
				pages.put("start", sp);
			}
			
			Clipping[] bgm = AudioH.loadClippings("audio/bgm/");
			Clipping[] sprite = AudioH.loadClippings("audio/sprite/");
			
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
		String[] pageList = FileH.getFilesFromDir(getClass(), "pages/");
		String[] overlayList = FileH.getFilesFromDir(getClass(), "overlays/");
		int totalFiles = pageList.length + overlayList.length;
		int tempNum = 0;
		try {
			for (String pn : pageList) {
				tempNum++;
				if (!pn.isEmpty() && !pn.matches(".*\\d.*")) {
					Class<?> c = Class.forName("pages." + pn.split("\\.")[0]);
					Constructor<?> con = c.getConstructor(new Class[] { Window.class });
					Object o = con.newInstance(this);
					if (o instanceof AppPage) {
						AppPage ap = (AppPage) o;
						if(pages.containsKey(ap.getID())) {
							System.out.println("The AppPage, "+ap.getID()+" was already there...");
						}else {
							System.out.println("The AppPage, "+ap.getID()+" has been added.");
							pages.put(ap.getID(), ap);
						}
					}
				}
				pageCheck.setProgress((int)Math.floor((tempNum / totalFiles) * 100));
			}
			for (String pn : overlayList) {
				tempNum++;
				if (!pn.isEmpty() && !pn.matches(".*\\d.*")) {
					Class<?> c = Class.forName("overlays." + pn.split("\\.")[0]);
					Constructor<?> con = c.getConstructor(new Class[] { Window.class });
					Object o = con.newInstance(this);
					if (o instanceof Overlay) {
						Overlay ap = (Overlay) o;
						if(overlays.containsKey(ap.getID())) {
							System.out.println("The Overlay, "+ap.getID()+" was already there...");
						}else {
							System.out.println("The Overlay, "+ap.getID()+" has been added.");
							overlays.put(ap.getID(), ap);
						}
					}
				}
				pageCheck.setProgress((int)Math.floor((tempNum / totalFiles) * 100));
			}
			pageCheck.setProgress(100);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void stop() {
		getCurrentAppPage().onChange();
		frame.setVisible(false);
		System.exit(-1);
	}

	private void paintPage(Graphics2D g2d) {
		AppPage ap = pages.get(currentPage);
		if(ap != null) {
			ap.paint(g2d);
			EntityH.paint(g2d);
			Overlay o = overlays.get(currentOverlay);
			if(o != null) {
				o.paint(g2d);
			}
		}
	}
	
	public void paint(Graphics2D g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		if (!TransH.transitioning) {
			paintPage(g);
		} else {
			g.drawImage(TransH.getTransition().getImage(), 0, 0, null);
		}
		if(trophy != null) {
			trophy.paint(g);
		}
		debug.paint(g);
	}

	private void updatePage(double delta) {
		AppPage ap = pages.get(currentPage);
		if(ap != null) {
			ap.update(delta);
			EntityH.update(delta);
			if(!currentOverlay.equals("")) {
				Overlay o = overlays.get(currentOverlay);
				if(o != null) {
					o.update(delta);
				}
			}
		}
	}
	
	public void update(double delta) {
		if (!TransH.transitioning) {
			updatePage(delta);
		}
		if(trophy != null) {
			trophy.update();
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
	
	public void addTrophy(String title, String desc) {
		trophy.addTrophy(title, desc);
	}
	
	public void addTrophy(String title, String desc, TrophyCallback call) {
		trophy.addTrophy(title, desc, call);
	}
	
	public Object getGlobal(String key) {
		return globals.containsKey(key) ? globals.get(key) : -1;
	}
	
	public void setGlobal(String key, Object value) {
		globals.put(key, value);
	}
	
	public Map<String, Object> globalDump(){
		return globals;
	}
	
	public void resetKeyUp() {
		for(Map.Entry<Integer, Boolean> entry : keyUp.entrySet()) {
			keyUp.put(entry.getKey(), false);
		}
	}
}