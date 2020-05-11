package engine;

import achievement.Trophy;
import backends.Functions;
import backends.GlobalVars;
import configs.EngineConfig;
import debug.Debug;
import handler.HandlerCollection;

public class Collector extends EngineConfig{
	
	private Debug debug = null;
	private HandlerCollection handlers = null;
	private Functions functions = null;
	private Trophy trophy = null;
	
	private GlobalVars globals = null;
	
	public void start(GameWindow w, String[] args) {
		globals = new GlobalVars();
		debug = new Debug(w);
		functions = new Functions();
		handlers = new HandlerCollection(w);
		
		//if Commands then init
		if (args != null && args.length > 0) {
			debug.init(args);
		}
	}
	
	void postInit(GameWindow w) {
		debug.setupRect();
		trophy = new Trophy(w);
	}
	
	public Debug getDebug() {
		return debug;
	}
	
	public HandlerCollection getHandlers() {
		return handlers;
	}
	
	public Functions getFunctions() {
		return functions;
	}
	
	public Trophy getTrophy() {
		return trophy;
	}
	
	public GlobalVars getGlobalVars() {
		return globals;
	}

}
