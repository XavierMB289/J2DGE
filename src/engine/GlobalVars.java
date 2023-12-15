package engine;

import java.util.HashMap;
import java.util.Map;

public class GlobalVars {
	
	Map<String, Object> globals;
	
	public void preInit() {
		globalFlush();
	}
	
	public Object getGlobal(String key, boolean singleUse) {
		if(globals.containsKey(key)) {
			if(singleUse) {
				Object obj = globals.get(key);
				globals.remove(key);
				return obj;
			}
			return globals.containsKey(key) ? globals.get(key) : null;
		}
		return null;
	}
	
	public String getStringGlobal(String key, boolean singleUse) {
		Object tempObj = getGlobal(key, singleUse);
		return (String)(tempObj == null ? "" : tempObj);
	}
	
	public int getIntGlobal(String key, boolean singleUse) {
		Object tempObj = getGlobal(key, singleUse);
		return (int)(tempObj == null ? -1 : tempObj);
	}
	
	public boolean getBooleanGlobal(String key, boolean singleUse) {
		Object tempObj = getGlobal(key, singleUse);
		return (boolean)(tempObj == null ? false : tempObj);
	}
	
	public void setGlobal(String key, Object value) {
		globals.put(key, value);
	}
	
	public void setGlobals(String[] keys, Object[] values) {
		if(keys.length != values.length) {
			return;
		}
		for(int i = 0; i < keys.length; i++) {
			setGlobal(keys[i], values[i]);
		}
	}
	
	public void removeGlobal(String key) {
		globals.remove(key);
	}
	
	public void globalFlush() {
		globals = new HashMap<>();
	}
	
	public Map<String, Object> globalDump(){
		return globals;
	}
	
}
