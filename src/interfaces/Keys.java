package interfaces;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public interface Keys {
	
	public Map<Integer, Boolean> keys = new HashMap<>();
	
	public int ExitKey = KeyEvent.VK_ESCAPE;
	
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
	
	public default boolean keyIsDown(int key) {
		return keys.containsKey(key) ? keys.get(key) : false;
	}
	
}
