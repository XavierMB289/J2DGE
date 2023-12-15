package handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyboardHandler implements KeyListener {
	
	private Map<Integer, Key> keys;
	
	public void init() {
		keys = new HashMap<Integer, Key>();
	}

	@Override
	public void keyTyped(KeyEvent e) {  }

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keys.containsKey(keyCode)) {
			Key key = keys.get(keyCode);
			key.setPressed(true);
			key.setIsReleased(false);
		}else {
			Key key = new Key();
			key.setPressed(true);
			keys.put(keyCode, key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keys.containsKey(keyCode)) {
			Key key = keys.get(keyCode);
			key.setPressed(false);
			key.setIsReleased(true);
		}else {
			Key key = new Key();
			key.setIsReleased(true);
			keys.put(keyCode, key);
		}
	}
	
	/**
	 * Checks if the appropriate key is PRESSED
	 * @param c Character to check for
	 * @return boolean representing if "key is pressed"
	 */
	public boolean isKeyDown(char c) {
		return keys.containsKey((int)c)?keys.get((int)c).isPressed():false;
	}
	
	/**
	 * Checks if the appropriate key is RELEASED
	 * Single Check (sets to false when checked)
	 * @param c Character to check for
	 * @return boolean representing if "key is released"
	 */
	public boolean isKeyUp(char c) {
		return keys.containsKey((int)c)?keys.get((int)c).isReleased():false;
	}

}


class Key{
	
	private boolean isPressed;
	private boolean isReleased;
	
	public boolean isPressed() {
		return isPressed;
	}
	
	void setPressed(boolean val) {
		isPressed = val;
	}
	
	public boolean isReleased() {
		boolean tempBool = isReleased;
		isReleased = false;
		return tempBool;
	}
	
	void setIsReleased(boolean val) {
		isReleased = val;
	}
	
}