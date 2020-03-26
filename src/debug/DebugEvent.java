package debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import engine.Window;

public abstract class DebugEvent {
	
	Window w;
	
	public DebugEvent(Window w) {
		this.w = w;
	}

	public abstract void event(Window w, Graphics2D g, Rectangle rect);
	
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
