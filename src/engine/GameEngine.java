package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.WindowConstants;

import handler.EntityHandler;
import handler.GameFileHandler;
import handler.ScreenHandler;
import window.GameWindow;

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
	GameWindow window = null;
	
	//Loop Variables
	private boolean running;
	private int FPS;
	private int fpsCounter;
	private double delta;
	private long lastLoopTime;
	private Thread thread;
	
	//Key Variables
	private Map<Integer, Boolean> keyDown;
	
	//Config Variable
	Map<String, Object> config;
	
	//Paint Variables
	BufferedImage paintImg;
	
	/*
	 * Client Side variables init
	 * client side module init
	 */
	public void preInit() {
		gv = new GlobalVars();
		gv.preInit();
		fh = new GameFileHandler();
		sh = new ScreenHandler();
		eh = new EntityHandler();
		eh.preInit();
		
		window = new GameWindow();
		window.preInit();
		
		running = true;
		delta = 0;
		
		keyDown = new HashMap<Integer, Boolean>();
		
		config = new HashMap<String, Object>();
	}
	
	/*
	 * Search for and init pages
	 * setup Frame Variables
	 */
	public void init() {
		
		if(config.containsKey(GameConfigKeys.TITLE)) {
			window.getFrame().setTitle((String)config.get(GameConfigKeys.TITLE));
		}
		
		window.getFrame().addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {  }

			@Override
			public void keyPressed(KeyEvent e) {
				keyDown.put(e.getKeyCode(), true);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDown.put(e.getKeyCode(), false);
				
				if(config.containsKey(GameConfigKeys.EXIT_KEY)) {
					if(e.getKeyCode() == (int)config.get(GameConfigKeys.EXIT_KEY)) {
						stop();
					}
				}
			}
			
		});
		
		window.getFrame().setLayout(null);
		window.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		window.getFrame().setUndecorated(true);
		
		sh.init();
		
		sh.setScreens(this, getClass(), "MainMenu");
		
		window.init();
		
	}
	
	/*
	 * Server Module Setup
	 * close up loose ends
	 */
	public void postInit() {
		thread = new Thread(this);
		
		window.finalize();
		
		int w = MAX_WIDTH, h = MAX_HEIGHT;
		if(config.containsKey(GameConfigKeys.WIDTH)) {
			w = (int)config.get(GameConfigKeys.WIDTH);
			w = w < MAX_WIDTH ? w : MAX_WIDTH;
		}
		if(config.containsKey(GameConfigKeys.HEIGHT)) {
			h = (int)config.get(GameConfigKeys.HEIGHT);
			h = h < MAX_HEIGHT ? h : MAX_HEIGHT;
		}
		if(w > window.getFrame().getWidth()) {
			w = window.getFrame().getWidth();
		}
		if(h > window.getFrame().getHeight()) {
			h = window.getFrame().getHeight();
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
			
			g.setColor(Color.white);
			g.fillRect(0, 0, paintImg.getWidth(), paintImg.getHeight());
			g.setColor(Color.black);
			sh.getScreen().paint(g);
			eh.paint(g);
			
			g.dispose();
		}
		
		sh.getScreen().update(delta);
		eh.update(delta);
		window.paint(paintImg);
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
	
	public void addConfig(Map<String, Object> c) {
		config = c;
		if(config.containsKey(GameConfigKeys.FILEPATH)) {
			getScreenHandler().setFilepath((String)config.get(GameConfigKeys.FILEPATH));
		}
	}
	
	public void setFullscreen() {
		window.setFullscreen();
	}
	
	public void fullscreenExclusive() {
		window.fullscreenExclusive();
	}
	
	public void setWindowSize(int x, int y) {
		window.setWindowSize(new Dimension(x, y));
	}
	
	public void changeLocation(int addX, int addY) {
		window.changeLocation(addX, addY);
	}
	
	public synchronized void stop() {
		try {
			running = false;
			getScreenHandler().getScreen().onEngineStop();
			window.stop();
			thread.join();
		}catch(InterruptedException e) {
			System.err.println("Couldn't properly join the thread in GameEngine.stop");
		}
		System.exit(-1);
	}
	
	public int getFPS() {
		return FPS;
	}
	
	public GlobalVars getGlobalVars() {
		return gv;
	}
	
	public GameWindow getWindow() {
		return window;
	}
	
	public EntityHandler getEntityHandler() {
		return eh;
	}
	
	public GameFileHandler getFileHandler() {
		return fh;
	}
	
	public ScreenHandler getScreenHandler() {
		return sh;
	}
	
	public boolean keyIsDown(int key) {
		return keyDown.containsKey(key) ? keyDown.get(key) : false;
	}
	
}
