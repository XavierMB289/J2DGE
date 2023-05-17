package backend.obj;

import java.awt.Graphics2D;

import engine.GameEngine;

public abstract class GameScreen {
	
	protected final String ID;
	protected GameEngine engine;
	
	public GameScreen(GameEngine e) {
		ID = getClass().getSimpleName();
		engine = e;
	}
	
	/*
	 * Used to initialize variables for the page and load resources used
	 */
	public abstract void init();
	
	public abstract void paint(Graphics2D g);
	
	public abstract void update(double delta);
	
	/**
	 * Calls this function from the shown screen ONLY
	 * Must be Overridden to do anything
	 */
	public void onEngineStop() {}
	
	public String getID() {
		return ID;
	}
	
}
