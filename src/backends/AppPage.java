package backends;

import java.awt.Graphics2D;

public abstract class AppPage {
	
	protected String ID = getClass().getSimpleName();
	
	public AppPage() {
		ID = ID.substring(0, 1).toLowerCase() + ID.substring(1);
	}
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update();
	
	public String getID() {
		return ID;
	}
	
}
