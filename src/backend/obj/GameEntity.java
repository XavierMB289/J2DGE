package backend.obj;

import java.awt.Graphics2D;

import backend.Vector2D;
import engine.GameEngine;

public abstract class GameEntity {
	
	protected final String ID;
	protected GameEngine engine;
	protected boolean DELETED;
	protected Vector2D coords;
	
	/**
	 * Creates a GameEntity.
	 * @param idNum Appends this on the end of the SimpleName
	 * @param e GameEngine object
	 */
	public GameEntity(int idNum, GameEngine e) {
		ID = getClass().getSimpleName() + "_" + idNum;
		engine = e;
		DELETED = false;
		coords = new Vector2D();
	}
	
	public abstract void init();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	/**
	 * Overrides toString method for use in
	 * multi-user applications
	 */
	@Override
	public abstract String toString();
	
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
	
	/**
	 * Sets the Coords variable for this entity
	 * @param x X Coord
	 * @param y Y Coord
	 */
	public void setCoords(int x, int y) {
		coords.set(x, y);
	}
	
	/**
	 * Gets the X Coord for this entity
	 * @return X Coord
	 */
	public double getX() {
		return coords.getX();
	}
	
	/**
	 * Gets the Y Coord for this entity
	 * @return Y Coord
	 */
	public double getY() {
		return coords.getY();
	}
	
}
