package engine;

import java.util.HashMap;
import java.util.Map;

public class GlobalVars {
	
	Map<String, Object> globals;
	
	public void preInit() {
		globalFlush();
	}
	
	public Object getGlobal(String key) {
		return globals.containsKey(key) ? globals.get(key) : null;
	}
	
	public void setGlobal(String key, Object value) {
		globals.put(key, value);
	}
	
	public void globalFlush() {
		globals = new HashMap<>();
	}
	
	public Map<String, Object> globalDump(){
		return globals;
	}
	
}
