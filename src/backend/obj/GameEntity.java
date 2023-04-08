package backend.obj;

import java.awt.Graphics2D;

import engine.GameEngine;

public abstract class GameEntity {
	
	protected final String ID;
	protected GameEngine engine;
	protected boolean DELETED;
	
	public GameEntity(int idNum, GameEngine e) {
		ID = getClass().getSimpleName() + idNum;
		engine = e;
		DELETED = false;
	}
	
	public abstract void init();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	public String getID() {
		return this.getID();
	}
	
	public boolean isDeleted() {
		return this.DELETED;
	}
	
	public void delete() {
		DELETED = true;
	}
	
}
