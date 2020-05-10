package handler;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import backends.objs.EntityBase;
import engine.GameWindow;
import online.EntityWrapper;

public class EntityHandler implements Serializable{
	
	private static final long serialVersionUID = -6427291011070466960L;

	GameWindow w;
	
	// Entity Variables
	private ArrayList<EntityBase> entity = null;
	private ArrayList<EntityBase> removeEnt = null;
	private ArrayList<EntityBase> addEnt = null;

	public EntityHandler(GameWindow w) {
		this.w = w;
		entity = new ArrayList<>();
		removeEnt = new ArrayList<>();
		addEnt = new ArrayList<>();
	}
	
	public void paint(Graphics2D g) {
		for(EntityBase e : entity) {
			e.paint(g);
		}
	}
	
	public void update(double delta) {
		for(EntityBase e : entity) {
			e.update(delta);
		}
		if(removeEnt.size() > 0) {
			for(EntityBase e : removeEnt) {
				if(entity.contains(e)){
					entity.remove(e);
				}
			}
			removeEnt = new ArrayList<>();
		}
		if(addEnt.size() > 0) {
			for(EntityBase e : addEnt) {
				if(!entity.contains(e)){
					entity.add(e);
				}
			}
			addEnt = new ArrayList<>();
		}
	}
	
	public void removeEntity(EntityBase e) {
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
	
	public void addEntity(EntityBase e) {
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
	
	public void changeEntity(EntityBase e){
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
	
	public ArrayList<EntityBase> getEntities(){
		return entity;
	}

}
