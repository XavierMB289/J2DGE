package backends.objs;

import java.awt.Graphics2D;
import java.io.Serializable;

import engine.GameWindow;

public abstract class AppPage implements Serializable{
	
	private static final long serialVersionUID = 5638768653315988428L;
	
	protected GameWindow w;
	
	protected String ID = getClass().getSimpleName();
	
	/**
	 * @author Xavier Bennett
	 * @param win engine.Window
	 * Setup for ID so that it can be properly displayed by engine.Window
	 */
	public AppPage(GameWindow win) {
		w = win;
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1);
	}
	
	/**
	 * @author Xavier Bennett
	 * Initializing the variables for the page.
	 */
	public abstract void init();
	
	/**
	 * @author Xavier Bennett
	 * On AppPage change, this happens...
	 */
	public abstract void onChange();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return ID;
	}
}
