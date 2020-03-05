package handler;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import backends.AppPage;
import handler.event.BaseEvent;

public class EventHandler implements Serializable{
	
	private static final long serialVersionUID = -2477938767670058066L;
	
	private ArrayList<BaseEvent> events;
	private ArrayList<BaseEvent> destroy;
	
	private int eventNum = 0;
	
	public EventHandler() {
		events = new ArrayList<>();
		destroy = new ArrayList<>();
	}
	
	public void addEvent(BaseEvent e) {
		events.add(e);
	}
	
	public void addDestroy(BaseEvent e) {
		if(events.contains(e)) {
			destroy.add(e);
		}
	}
	
	public void paintCurrentEvent(Graphics2D g) {
		if(events.size() > eventNum) {
			BaseEvent be = events.get(eventNum);
			if(!be.FINISHED){
				events.get(eventNum).paint(g);
			}
		}
	}
	
	public void updateCurrentEvent(double delta) {
		if(events.size() > eventNum) {
			BaseEvent be = events.get(eventNum);
			if(!be.FINISHED){
				events.get(eventNum).update();
			}else{
				addDestroy(be);
				eventNum++;
			}
		}
		//Destroying destroyables
		for(BaseEvent b : destroy) {
			events.remove(b);
		}
	}
	
}
