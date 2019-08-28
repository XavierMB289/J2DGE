package handler;

import java.awt.Graphics2D;
import java.util.ArrayList;

import backends.Entity;
import engine.Window;

public class EntityHandler {
	
	Window w;
	
	// Entity Variables
	private ArrayList<Entity> entity = null;
	private ArrayList<Entity> removeEnt = null;
	private ArrayList<Entity> addEnt = null;
	private int tick = 3;
	private double deltas = 0;

	public EntityHandler(Window w) {
		this.w = w;
		entity = new ArrayList<>();
		removeEnt = new ArrayList<>();
		addEnt = new ArrayList<>();
	}
	
	public void paint(Graphics2D g) {
		for(Entity e : entity) {
			if(w.getCurrentPage().equals(e.getID())) {
				e.paint(g);
			}
		}
	}
	
	public void update(double delta) {
		deltas += delta;
		if(--tick <= 0) {
			tick = 3;
			deltas = 0;
			for(Entity e : entity) {
				e.onTick(deltas);
			}
		}
		if(removeEnt.size() > 0) {
			for(Entity e : removeEnt) {
				entity.remove(e);
			}
			removeEnt = new ArrayList<>();
		}
		if(addEnt.size() > 0) {
			for(Entity e : addEnt) {
				entity.add(e);
			}
			addEnt = new ArrayList<>();
		}
	}
	
	public void removeEntity(Entity e) {
		if(entity.contains(e)) {
			removeEnt.add(e);
		}
	}
	
	public void addEntity(Entity e) {
		if(!entity.contains(e)) {
			addEnt.add(e);
		}
	}
	
	public ArrayList<Entity> getEntities(){
		return entity;
	}

}
