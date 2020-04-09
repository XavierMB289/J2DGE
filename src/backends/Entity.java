package backends;

import java.awt.Graphics2D;
import java.io.Serializable;

import engine.Window;

public abstract class Entity implements Serializable{
	
	private static final long serialVersionUID = -3124026621796435033L;
	
	protected Window w;
	
	protected String ID;
	
	protected int x, y;
	
	protected Vector2D movement;
	
	/**
	 * @author Xavier Bennett
	 * @param w
	 */
	public Entity(Window w) {
		this.w = w;
		ID = new Exception().getStackTrace()[2].getClassName().split("\\.")[1];
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1) + System.currentTimeMillis();
		ID = w.functions.md5(ID);
		ID = ID.length() > 10 ? ID.substring(0, 11) : ID;
	}
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return ID;
	}
	
	public void removeMe(Entity e) {
		w.EntityH.removeEntity(this);
	}
	
}
