package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serializable;

import javax.swing.JPanel;

public class CustomPanel extends JPanel implements Serializable, Runnable{

	private static final long serialVersionUID = -5758060187282871973L;
	
	Window w;
	
	public int FPS;
	private int fpsCounter;
	
	//Variable Timestep Variables
	private long lastLoopTime;
	private long lastFpsTime = 0;
	
	//Original Loop Variables
	private double delta;

	public CustomPanel(Window w) {
		this.w = w;
	}
	
	@SuppressWarnings("static-access")
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
		
		repaint();
		
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
			update(1);
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
		}
	}
	
	public void update(double delta) {
		w.update(delta);
	}

}
