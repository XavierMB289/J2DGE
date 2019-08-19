package backends;

import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class AppPage implements Serializable{
	
	private static final long serialVersionUID = 5638768653315988428L;
	
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
