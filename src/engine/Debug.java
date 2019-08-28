package engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Map;

public class Debug {

	Window w;
	private Rectangle DEBUG_RECT;
	private String DEBUG_LEVEL = "0";
	
	public Debug(Window w) {
		this.w = w;
	}
	
	public void init(String[] args) {
		DEBUG_LEVEL = w.functions.arrayContains(args, "-debug=");
		DEBUG_LEVEL = DEBUG_LEVEL.equals(null) ? "0" : DEBUG_LEVEL.split("=")[1];
	}
	
	public void setupRect() {
		DEBUG_RECT = new Rectangle(0, 0, (int) w.W12, (int) w.H12 * 2);
	}
	
	public void paint(Graphics2D g) {
		if (DEBUG_LEVEL.equals("1")) {
			debugText(g, "FPS: "+w.customPanel.FPS, DEBUG_RECT);
		}else if(DEBUG_LEVEL.equals("2")) {
			String buttons = "";
			for (Map.Entry<Integer, Boolean> entry : w.getKeys().entrySet()) {
				if (entry.getValue()) {
					buttons += KeyEvent.getKeyText(entry.getKey());
				}
			}
			g.setFont(new Font("Arial", Font.PLAIN, 24));
			debugText(g, buttons, DEBUG_RECT);
		}
	}
	
	public void debugText(Graphics2D g, String text, Rectangle rect) {
		w.functions.push(g);
		Font font = g.getFont();
		
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    g.drawString(text, x, y);
	    
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = g.getFont().createGlyphVector(frc, text);
		
		g.setStroke(new BasicStroke(5));
		g.translate(x, y);
		g.setColor(Color.black);
		g.draw(gv.getOutline());
		w.functions.pop(g);
		g.setColor(Color.white);
		g.drawString(text, x, y);
	}

}
