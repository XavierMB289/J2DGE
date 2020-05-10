package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serializable;

import javax.swing.JPanel;

import coffeeDev.Credit;

public class CustomPanel extends JPanel implements Serializable, Runnable{

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

	public CustomPanel(GameWindow w) {
		this.w = w;
	}
	
	public void init() {
		this.setBounds(0, 0, w.getWIDTH(), w.getHEIGHT());
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
		
		update(delta);
		
		this.repaint();
		
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
			this.repaint();
			update(delta);
			delta--;
		}
		FPS = fpsCounter;
		fpsCounter = 0;
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		try {
			w.paint(g2d);
		}catch(NullPointerException e) {
			System.err.println("Null Pointer in "+e.getStackTrace()[0]);
		}catch(Exception e) {
			System.err.println(e.getStackTrace()[0]);
		}
	}
	
	public void update(double delta) {
		w.update(delta);
	}

}
