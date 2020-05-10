package backends.objs;

import java.awt.Graphics2D;
import java.io.Serializable;

import engine.GameWindow;

public abstract class EntityBase implements Serializable{
	
	private static final long serialVersionUID = -3124026621796435033L;
	
	protected GameWindow w;
	
	protected String ID;
	
	protected int x, y;
	
	protected Vector2D movement;
	
	/**
	 * @author Xavier Bennett
	 * @param w
	 */
	public EntityBase(GameWindow w) {
		this.w = w;
		ID = new Exception().getStackTrace()[2].getClassName().split("\\.")[1];
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1) + System.currentTimeMillis();
		ID = w.getFunctions().md5(ID);
		ID = ID.length() > 10 ? ID.substring(0, 11) : ID;
	}
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return ID;
	}
	
	public void removeMe(EntityBase e) {
		w.getHandlers().getEntityHandler().removeEntity(this);
	}
	
}
