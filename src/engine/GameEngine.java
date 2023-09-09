package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import handler.EntityHandler;
import handler.GameFileHandler;
import handler.ScreenHandler;
import window.AppWindow;

public class GameEngine implements Runnable{
	
	//final variables
	final int MAX_WIDTH = 1280;
	final int MAX_HEIGHT = 720;
	
	//backend
	GlobalVars gv = null;
	
	//Handlers
	GameFileHandler fh = null;
	ScreenHandler sh = null;
	EntityHandler eh = null;
	
	//Window
	AppWindow appWindow = null;
	
	//Loop Variables
	private boolean running;
	private int FPS;
	private int fpsCounter;
	private double delta;
	private long lastLoopTime;
	private Thread thread;
	
	//Config Variable
	Map<String, Object> config;
	
	//Paint Variables
	BufferedImage paintImg;
	Color bgColor;
	
	/*
	 * PreInitializes all the methods and variables used
	 */
	public void preInit() {
		gv = new GlobalVars();
		gv.preInit();
		fh = new GameFileHandler();
		sh = new ScreenHandler();
		eh = new EntityHandler();
		eh.preInit();
		
		appWindow = new AppWindow();
		appWindow.preInit();
		
		running = true;
		delta = 0;
		
		config = new HashMap<String, Object>();
		
		bgColor = Color.white;
	}
	
	/*
	 * Initializes Methods and searches for the first Screen/Overlay (MainMenu)
	 * Recommended Adding the Listeners before calling this.
	 */
	public void init() {
		
		if(config.containsKey(GameConfigKeys.TITLE)) {
			appWindow.setTitle((String)config.get(GameConfigKeys.TITLE));
		}
		
		appWindow.init();
		
		sh.init();
		
		sh.setOverlay(this, getClass(), "MainMenuOverlay");
		sh.setScreens(this, getClass(), "MainMenu");
		
	}
	
	/*
	 * Closes up ALL Init procedures
	 */
	public void postInit() {
		thread = new Thread(this);
		
		appWindow.postInit();
		
		int w = MAX_WIDTH, h = MAX_HEIGHT;
		if(config.containsKey(GameConfigKeys.WIDTH)) {
			w = (int)config.get(GameConfigKeys.WIDTH);
			w = w < MAX_WIDTH ? w : MAX_WIDTH;
		}
		if(config.containsKey(GameConfigKeys.HEIGHT)) {
			h = (int)config.get(GameConfigKeys.HEIGHT);
			h = h < MAX_HEIGHT ? h : MAX_HEIGHT;
		}
		System.out.println(w+" "+h);
		paintImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		thread.start();
	}
	
	public void update(double delta) {
		
		if(paintImg != null) {
			Graphics2D g = (Graphics2D) paintImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			g.setColor(bgColor);
			g.fillRect(0, 0, paintImg.getWidth(), paintImg.getHeight());
			g.setColor(Color.black);
			sh.getScreen().paint(g);
			eh.paint(g);
			sh.getOverlay().paint(g);
			
			g.dispose();
		}
		
		sh.getScreen().update(delta);
		eh.update(delta);
		sh.getOverlay().update(delta);
		appWindow.paint(paintImg);
	}
	
	@Override
	public void run() {
		lastLoopTime = System.nanoTime();
		
		double optimalTime = (1000000000 / 60);
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now - lastLoopTime) / optimalTime);
			lastLoopTime = now;
			while(delta >= 1) {
				fpsCounter++;
				update(delta);
				delta--;
			}
			FPS = fpsCounter;
			fpsCounter = 0;
		}
	}
	
	/**
	 * Adds the config to the GameEngine Object
	 * @param c A Map using {@link GameConfigKeys} as the keys
	 */
	public void addConfig(Map<String, Object> c) {
		config = c;
		if(config.containsKey(GameConfigKeys.SCREEN_FILEPATH)) {
			getScreenHandler().setFilepath((String)config.get(GameConfigKeys.SCREEN_FILEPATH));
		}
	}
	
	/**
	 * Sets the background color of the app/game
	 * @param c Color to set the bgColor to
	 */
	public void setBGColor(Color c) {
		bgColor = c;
	}
	
	/**
	 * Adds a MouseListener to the JFrame
	 * @param l Custom MouseListener
	 */
	public void addMouseListener(MouseListener l) {
		appWindow.addMouseListener(l);
	}
	
	/**
	 * Adds a KeyListener to the Jframe
	 * @param l Custom KeyListener
	 */
	public void addKeyListener(KeyListener l) {
		appWindow.addKeyListener(l);
	}
	
	/**
	 * Sets the window to borderless fullscreen window mode
	 */
	public void setFullscreen() {
		appWindow.setFullscreen();
	}
	
	/**
	 * Sets the window in fullscreen exclusive mode
	 */
	public void fullscreenExclusive() {
		appWindow.setFullscreenExclusive();
	}
	
	/**
	 * Sets the window size of the window
	 * @param x the width of the window
	 * @param y the height of the window
	 */
	public void setWindowSize(int x, int y) {
		appWindow.setWindowSize(new Dimension(x, y));
	}
	
	/**
	 * Moves the window on the screen
	 * @param addX amount to add to the x position
	 * @param addY amount to add to the y position
	 */
	public void changeLocation(int addX, int addY) {
		appWindow.changeLocation(addX, addY);
	}
	
	/**
	 * STOPS ALL PROCESSES AND CLOSES THE APP
	 */
	public synchronized void stop() {
		running = false;
		getScreenHandler().getScreen().onEngineStop();
		appWindow.stop();
		System.exit(-1);
	}
	
	/**
	 * Gets the FPS for Debugging purposes
	 * @return int FPS
	 */
	public int getFPS() {
		return FPS;
	}
	
	/**
	 * Gets the GlobalVars
	 * Needed for transferring/holding variables between screens
	 * @return {@link GlobalVars}
	 */
	public GlobalVars getGlobalVars() {
		return gv;
	}
	
	/**
	 * Gets the EntityHandler
	 * Needed for Handling Entities on screen via z-Indexing
	 * @return {@link EntityHandler}
	 */
	public EntityHandler getEntityHandler() {
		return eh;
	}
	
	/**
	 * Gets the GameFileHandler
	 * Needed for savefile manipulation and file getting
	 * @return {@link GameFileHandler}
	 */
	public GameFileHandler getFileHandler() {
		return fh;
	}
	
	/**
	 * Gets the ScreenHandler Object
	 * Needed to change which screen you're looking at
	 * @return {@link ScreenHandler}
	 */
	public ScreenHandler getScreenHandler() {
		return sh;
	}
	
}
