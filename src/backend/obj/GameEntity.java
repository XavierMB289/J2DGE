package backend.obj;

import java.awt.Graphics2D;

import engine.GameEngine;

public abstract class GameEntity {
	
	protected final String ID;
	protected GameEngine engine;
	protected boolean DELETED;
	
	/**
	 * Creates a GameEntity.
	 * @param idNum Appends this on the end of the SimpleName
	 * @param e GameEngine object
	 */
	public GameEntity(int idNum, GameEngine e) {
		ID = getClass().getSimpleName() + idNum;
		engine = e;
		DELETED = false;
	}
	
	public abstract void init();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return ID;
	}
	
	/**
	 * Check if the has been deleted
	 * @return this.DELETED
	 */
	public boolean isDeleted() {
		return this.DELETED;
	}
	
	/**
	 * Makes the Entity be deleted on the end of the current update
	 * this.DELETED = true;
	 */
	public void delete() {
		DELETED = true;
	}
	
}
