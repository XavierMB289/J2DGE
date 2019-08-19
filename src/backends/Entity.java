package backends;

import java.awt.Graphics2D;
import java.io.Serializable;

import engine.Window;

public abstract class Entity implements Serializable{
	
	private static final long serialVersionUID = -3124026621796435033L;
	
	protected Window w;
	
	protected String ID;
	
	public Entity(Window w, String pageName) {
		this.w = w;
		ID = pageName;
	}
	
	public abstract void paint(Graphics2D g);
	
	public abstract void onTick();
	
	public String getID() {
		return ID;
	}
	
	public void removeMe(Entity e) {
		w.removeEnt.add(e);
	}
	
}
