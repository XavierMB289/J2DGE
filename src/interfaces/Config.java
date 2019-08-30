package interfaces;

import java.awt.event.KeyEvent;

public interface Config {
	
	//Custom Keys
	public int EXIT = KeyEvent.VK_ESCAPE;
	public int UP = KeyEvent.VK_W;
	public int UP_ALT = KeyEvent.VK_UP;
	public int DOWN = KeyEvent.VK_S;
	public int DOWN_ALT = KeyEvent.VK_DOWN;
	public int LEFT = KeyEvent.VK_A;
	public int LEFT_ALT = KeyEvent.VK_LEFT;
	public int RIGHT = KeyEvent.VK_D;
	public int RIGHT_ALT = KeyEvent.VK_RIGHT;
	public int ENTER = KeyEvent.VK_J;
	public int ENTER_ALT = KeyEvent.VK_Z;
	public int ACTION = KeyEvent.VK_K;
	public int ACTION_ALT = KeyEvent.VK_X;
	
	//GameLoop Variables
	public final int LOOP_TYPE = 1;
	//Variable Timestep Variables
	public final double TARGET_FPS = 60;
	public final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;
}
