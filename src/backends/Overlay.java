package backends;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import engine.Window;

public abstract class Overlay implements Serializable{
	
	private static final long serialVersionUID = 5638768653315988428L;
	
	protected Window w;
	
	protected String ID = getClass().getSimpleName();
	
	protected Color bg;
	
	public Overlay(Window w, Color bg) {
		this.w = w;
		this.bg = bg;
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1);
	}
	
	public abstract void init();
	
	//Will need to be overridden in order to work
	public void paint(Graphics2D g) {
		g.setColor(bg);
		g.fillRect(0, 0, w.WIDTH, w.HEIGHT);
	}
	
	public abstract void update();
	
	public String getID() {
		return ID;
	}
}
