package backends.objs;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.io.Serializable;

import engine.GameWindow;

public abstract class Overlay implements Serializable{
	
	private static final long serialVersionUID = 5638768653315988428L;
	
	protected GameWindow w;
	
	protected String ID = getClass().getSimpleName();
	
	protected float alpha = 0.5f;
	
	public Overlay(GameWindow w) {
		this.w = w;
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1);
	}
	
	public abstract void init();
	
	//Will need to be overridden in order to work
	public void paint(Graphics2D g) {
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		AlphaComposite orig = (AlphaComposite) g.getComposite();
		g.setComposite(alcom);
		paintOverlay(g);
		g.setComposite(orig);
	}
	
	public abstract void paintOverlay(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return ID;
	}
	
	public void setTransparency(float t) {
		alpha = t;
	}
}
