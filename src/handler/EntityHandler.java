package handler;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import backends.Entity;
import engine.Window;
import online.EntityWrapper;

public class EntityHandler implements Serializable{
	
	private static final long serialVersionUID = -6427291011070466960L;

	Window w;
	
	// Entity Variables
	private ArrayList<Entity> entity = null;
	private ArrayList<Entity> removeEnt = null;
	private ArrayList<Entity> addEnt = null;

	public EntityHandler(Window w) {
		this.w = w;
		entity = new ArrayList<>();
		removeEnt = new ArrayList<>();
		addEnt = new ArrayList<>();
	}
	
	public void paint(Graphics2D g) {
		for(Entity e : entity) {
			e.paint(g);
		}
	}
	
	public void update(double delta) {
		for(Entity e : entity) {
			e.update(delta);
		}
		if(removeEnt.size() > 0) {
			for(Entity e : removeEnt) {
				if(entity.contains(e)){
					entity.remove(e);
				}
			}
			removeEnt = new ArrayList<>();
		}
		if(addEnt.size() > 0) {
			for(Entity e : addEnt) {
				if(!entity.contains(e)){
					entity.add(e);
				}
			}
			addEnt = new ArrayList<>();
		}
	}
	
	public void removeEntity(Entity e) {
		if(entity.contains(e)) {
			removeEnt.add(e);
			if(w.SERVER_ENABLED){
				w.eServer.addChange(e, EntityWrapper.REMOVE);
			}
			if(w.CLIENT_ENABLED){
				w.eClient.addChange(e, EntityWrapper.REMOVE);
			}
		}
	}
	
	public void addEntity(Entity e) {
		if(!entity.contains(e)) {
			addEnt.add(e);
			if(w.SERVER_ENABLED){
				w.eServer.addChange(e, EntityWrapper.ADD);
			}
			if(w.CLIENT_ENABLED){
				w.eClient.addChange(e, EntityWrapper.ADD);
			}
		}
	}
	
	public void changeEntity(Entity e){
		for(int i = 0; i < entity.size(); i++){
			if(entity.get(i).getID().equals(e.getID())){
				entity.set(i, e);
				return;
			}
		}
		addEntity(e);
	}
	
	public void resetEntities(){
		entity = new ArrayList<>();
	}
	
	public ArrayList<Entity> getEntities(){
		return entity;
	}

}
