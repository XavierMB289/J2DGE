package backends;

import java.util.HashMap;
import java.util.Map;

public class GlobalVars {
	
	Map<String, Object> globals;

	public GlobalVars() {
		globals = new HashMap<>();
	}
	
	public Object getGlobal(String key) {
		return globals.containsKey(key) ? globals.get(key) : -1;
	}
	
	public void setGlobal(String key, Object value) {
		globals.put(key, value);
	}
	
	public Map<String, Object> globalDump(){
		return globals;
	}

}
