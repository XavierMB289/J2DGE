package engine;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import handler.EntityHandler;
import handler.GameFileHandler;
import handler.KeyboardHandler;
import handler.ScreenHandler;
import window.AppWindow;

public class GameEngine implements Runnable{
	
	//final variables
	final int MAX_WIDTH = 1280;
	final int MAX_HEIGHT = 720;
	final int TARGET_FPS = 60;
	
	//backend
	GlobalVars gv = null;
	
	//Handlers
	GameFileHandler fh = null;
	ScreenHandler sh = null;
	EntityHandler eh = null;
	KeyboardHandler kh = null;
	
	//Window
	AppWindow appWindow = null;
	
	//Loop Variables
	private boolean running;
	private int FPS;
	private Thread thread;
	
	//Config Variable
	Map<String, Object> config;
	
	//Paint Variables
	BufferedImage[] paintImg;
	int imgIndex; //Double Buffering
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
		kh = new KeyboardHandler();
		
		appWindow = new AppWindow();
		appWindow.preInit();
		
		running = true;
		FPS = 60;
		
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
		
		kh.init();
		appWindow.addKeyListener(kh);
	}
	
	/*
	 * Closes up ALL Init procedures
	 */
	public void postInit() {
		thread = new Thread(this);
		
		int w = MAX_WIDTH, h = MAX_HEIGHT;
		if(config.containsKey(GameConfigKeys.WIDTH)) {
			w = (int)config.get(GameConfigKeys.WIDTH);
			w = w < MAX_WIDTH ? w : MAX_WIDTH;
		}
		if(config.containsKey(GameConfigKeys.HEIGHT)) {
			h = (int)config.get(GameConfigKeys.HEIGHT);
			h = h < MAX_HEIGHT ? h : MAX_HEIGHT;
		}
		System.out.println("Panel: "+w+" "+h);
		paintImg = new BufferedImage[2];
		paintImg[0] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		paintImg[1] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		imgIndex = 0;
		
		appWindow.postInit(w, h);
		System.out.println("Frame: "+appWindow.getResWidth()+" "+appWindow.getResHeight());
		
		thread.start();
	}
	
	public void update(double delta) {
		
		sh.getScreen().update(delta);
		eh.update(delta);
		sh.getOverlay().update(delta);
	}
	
	private void paint() {
		BufferedImage selectedImg = paintImg[imgIndex];
		if(paintImg != null) {
			Graphics2D g = (Graphics2D) selectedImg.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			
			g.setColor(bgColor);
			g.fillRect(0, 0, selectedImg.getWidth(), selectedImg.getHeight());
			g.setColor(Color.black);
			sh.getScreen().paint(g);
			eh.paint(g);
			sh.getOverlay().paint(g);
			
			g.dispose();
			
			appWindow.paint(selectedImg);
		}
		imgIndex = imgIndex == 1 ? 0 : 1;
	}
	
	@Override
	public void run() {
		long lastLoopTime = System.nanoTime();
		final long optimalTime = (1000000000 / TARGET_FPS);
		long lastFPSTime = 0;
		int fpsCounter = 0;
		
		while(running) {
			long now = System.nanoTime();
			long updateLen = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLen / ((double)optimalTime);
			
			lastFPSTime += updateLen;
			if(lastFPSTime >= 1000000000) {
				lastFPSTime = 0;
				FPS = fpsCounter;
				fpsCounter = 0;
			}
			
			if(delta > 1) {
				update(delta);
			}
			
			paint();
			fpsCounter+=1;
			
			long sleep = (lastLoopTime - System.nanoTime() + optimalTime)/1000000;
			if(sleep > 0) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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
	 * Adds a MouseMotionListener to the JFrame
	 * @param l Custom MouseMotionListener
	 */
	public void addMouseMotionListener(MouseMotionListener l) {
		appWindow.addMouseMotionListener(l);
	}
	
	/**
	 * WARNING: Use other Functions. Use at own risk
	 * @param comp Component to add.
	 */
	public void addGeneric(Component comp) {
		appWindow.addGeneric(comp);
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
	 * Gets the Width of the Image to be stretched and painted
	 * @return int Width
	 */
	public int getWidth() {
		//Uses index 0. They are both the same size.
		return paintImg[0].getWidth();
	}
	
	/**
	 * Gets the Resolution Width
	 * @return int Resolution Width
	 */
	public int getResWidth() {
		return appWindow.getResWidth();
	}
	
	/**
	 * Gets the Height of the Image to be stretched and painted
	 * @return int Height
	 */
	public int getHeight() {
		//Uses index 0. They are both the same size.
		return paintImg[0].getHeight();
	}
	
	/**
	 * Gets the Resolution Height
	 * @return int Resolution Height
	 */
	public int getResHeight() {
		return appWindow.getResHeight();
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
	
	/**
	 * Runs the isKeyDown function from KeyboardHandler
	 * @param c Character to check for
	 * @return boolean
	 */
	public boolean keyDown(char c) {
		return kh.isKeyDown(c);
	}
	
	/**
	 * Runs the isKeyUp function from KeyboardHandler
	 * Single Use
	 * @param c Character to check for
	 * @return boolean
	 */
	public boolean keyUp(char c) {
		return kh.isKeyUp(c);
	}
	
}
