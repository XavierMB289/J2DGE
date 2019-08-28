package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.Serializable;

import javax.swing.JPanel;

public class CustomPanel extends JPanel implements Serializable, Runnable{

	private static final long serialVersionUID = -5758060187282871973L;
	
	Window w;

	public CustomPanel(Window w) {
		this.w = w;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;// 60 times per second
		double delta = 0;
		while (w.getFrame().isVisible()) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;
			while (delta >= 1)// Make sure update is only happening 60 times a second
			{
				// handles all of the logic restricted time
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
		w.paint(g2d);
		this.update();
	}
	
	public void update() {
		w.update();
	}

}
