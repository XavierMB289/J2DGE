package debug;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import engine.GameWindow;

public class Debug {

	GameWindow w;
	private String[] args;
	private Rectangle DEBUG_RECT;
	
	Map<String, DebugEvent> events;
	
	boolean enabled = false;
	
	public Debug(GameWindow w) {
		this.w = w;
	}
	
	public void init(String[] args) {
		this.args = args;
		
		events = new HashMap<>();
		events.put("-fps", new DebugEvent(w) {

			@Override
			public void event(Graphics2D g, Rectangle rect) {
				debugText(g, Integer.toString(w.getLoop().FPS), rect);
			}
			
		});
		
		enabled = getCommands("-debug") != null;
	}
	
	public String getCommands(String splitter) {
		String temp = w.getFunctions().arrayContains(args, splitter);
		return temp == null ? "" : temp.split("=")[1];
	}
	
	public void setupRect() {
		DEBUG_RECT = new Rectangle(0, 0, (int) w.getFrame().getW12(), (int) w.getFrame().getH12() * 2);
	}
	
	public void paint(Graphics2D g) {
		if(enabled) {
			g.setFont(new Font("Arial", Font.PLAIN, 24));
			for(Map.Entry<String, DebugEvent> entry : events.entrySet()) {
				if(getCommands(entry.getKey()) != null) {
					entry.getValue().event(g, DEBUG_RECT);
				}
			}
		}
	}

}
