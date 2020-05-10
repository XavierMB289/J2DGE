package debug;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

import engine.GameWindow;

public abstract class DebugEvent {
	
	GameWindow w;
	
	public DebugEvent(GameWindow w) {
		this.w = w;
	}

	public abstract void event(Graphics2D g, Rectangle rect);
	
	public void debugText(Graphics2D g, String text, Rectangle rect) {
		w.getFunctions().push(g);
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
		w.getFunctions().pop(g);
		g.setColor(Color.white);
		g.drawString(text, x, y);
	}

}
