package handler.event;

import java.awt.Graphics2D;

public abstract class BaseEvent{
	
	private final String ID;
	
	public boolean FINISHED = false;
	
	public BaseEvent(String id){
		this.ID = id;
	}
	
	public abstract void init();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update();
	
	public String getID(){
		return ID;
	}
	
}
