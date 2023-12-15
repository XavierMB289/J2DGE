package handler;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import backend.obj.GameEntity;

public class EntityHandler {
	
	ArrayList<GameEntity> ents;
	
	public void preInit() {
		ents = new ArrayList<GameEntity>();
	}
	
	public void paint(Graphics2D g) {
		for(int i = 0; i < ents.size(); i++) { //Making sure that the Ents are painted in order
			ents.get(i).paint(g);
		}
	}
	
	public void update(double delta) {
		int index = 0;
		//Changed to a while loop for easier deletion
		while(index < ents.size()) {
			GameEntity ent = ents.get(index);
			ent.update(delta);
			
			if(ents.get(index).isDeleted()) {
				ents.remove(index);
			}
			index+=1;
		}
	}
	
	/**
	 * Adds an entity to the end of the entity list and initializes it
	 * @param e GameEntity to add
	 */
	public void addEntity(GameEntity e) {
		e.init();
		ents.add(e);
	}
	
	/**
	 * Adds an entity to the list at the given zIndex and initializes it
	 * @param e GameEntity to add
	 * @param zIndex where to add it
	 */
	public void addEntity(GameEntity e, int zIndex) {
		e.init();
		ents.add(zIndex, e);
	}
	
	/**
	 * Adds the given list to the end of the entities list via addEntity(GameEntity obj)
	 * @param e GameEntity list
	 */
	public void addEntities(GameEntity[] e) {
		for(GameEntity ge : e) {
			addEntity(ge);
		}
	}
	
	/**
	 * Gets the GameEntity at the specified index
	 * @param index Index to get
	 * @return GameEntity
	 */
	public GameEntity getEntity(int index) {
		return ents.get(index);
	}
	
	/**
	 * Gets a GameEntity based on the ID
	 * @param ID ID to search for
	 * @return GameEntity or null
	 */
	public GameEntity getEntity(String ID) {
		for(GameEntity ge : ents) {
			if(ge.getID().equals(ID)) {
				return ge;
			}
		}
		return null;
	}
	
	/**
	 * Gets ALL GameEntity that contain the given string in their ID
	 * @param ID ID to search for
	 * @return GameEntity[], can be length of 0
	 */
	public GameEntity[] getAllEntities(String ID) {
		GameEntity[] ret = new GameEntity[0];
		for(GameEntity ge : ents) {
			if(ge.getID().contains(ID)) {
				ret = Arrays.copyOf(ret, ret.length+1);
				ret[ret.length-1] = ge;
			}
		}
		return ret;
	}
	
	public GameEntity[] getAllEntities() {
		return ents.toArray(new GameEntity[ents.size()]);
	}
	
	/**
	 * Changes the index of an object to a different index
	 * @param current the index of the object to change
	 * @param next the index to change it to
	 */
	public void changeIndex(int current, int next) {
		GameEntity ent = ents.get(current);
		ents.remove(current);
		addEntity(ent, next);
	}
	
	/**
	 * Deletes all Entites that contain the given input in their ID
	 * @param input String to check for
	 */
	public void deleteAll(String input) {
		for(GameEntity ge : ents) {
			if(ge.getID().contains(input)) {
				ge.delete();
			}
		}
	}
	
	/**
	 * DELETES ALL ENTITIES
	 */
	public void deleteAll() {
		for(GameEntity ge : ents) {
			ge.delete();
		}
	}
	
	/**
	 * Returns the total number of entities
	 * @return the size of the entity list
	 */
	public int getTotalEntities() {
		return ents.size();
	}
	
}
