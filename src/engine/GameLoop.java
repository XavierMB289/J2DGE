package engine;

import java.io.Serializable;

import coffeeDev.Credit;

public class GameLoop implements Serializable, Runnable{

	private static final long serialVersionUID = -5758060187282871973L;
	
	GameWindow w;
	
	private Credit c = null;
	
	public int FPS;
	private int fpsCounter;
	
	//Variable Timestep Variables
	private long lastLoopTime;
	private long lastFpsTime = 0;
	
	//Original Loop Variables
	private double delta;

	public GameLoop(GameWindow w) {
		this.w = w;
	}
	
	public void init() {
		c = new Credit(w);
		c.init();
		w.getHandlers().getPageHandler().addPage(c);
		w.getHandlers().getPageHandler().setAllToCurrent("credit", true);
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		lastLoopTime = System.nanoTime();
		
		if(w.LOOP_TYPE == 1){
			delta = 0;
		}
		
		while (w.getFrame().isVisible()) {
			if(w.LOOP_TYPE == 0) {
				variableTimestep();
			}else if(w.LOOP_TYPE == 1) {
				originalLoop();
			}else {
				System.err.println("Invalid LOOP_TYPE in interfaces.Config");
				System.exit(-1);
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void variableTimestep() {
		long now = System.nanoTime();
		long updateLen = now - lastLoopTime;
		lastLoopTime = now;
		double delta = updateLen / ((double)w.OPTIMAL_TIME);
		
		lastFpsTime += updateLen;
		fpsCounter++;
		
		if(lastFpsTime >= 1000000000) {
			FPS = fpsCounter;
			lastFpsTime = 0;
			fpsCounter = 0;
		}
		
		w.update(delta);
		
		w.getFrame().repaint();
		
		try {
			long temp = ((lastLoopTime-System.nanoTime() + (int)w.OPTIMAL_TIME) / 1000000);
			if(temp > 0) {
				Thread.sleep(temp);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	private void originalLoop() {
		long now = System.nanoTime();
		delta = delta + ((now - lastLoopTime) / w.OPTIMAL_TIME);
		lastLoopTime = now;
		while(delta >= 1) {
			fpsCounter++;
			w.getFrame().repaint();
			w.update(delta);
			delta--;
		}
		FPS = fpsCounter;
		fpsCounter = 0;
	}

}
