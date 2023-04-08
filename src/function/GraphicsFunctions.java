package function;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GraphicsFunctions {
	
	private GraphicsEnvironment ge;
	
	private AffineTransform pushpop = null;
	
	public void push(Graphics2D g) {
		pushpop = g.getTransform();
	}
	
	public void pop(Graphics2D g) {
		g.setTransform(pushpop);
	}
	
	public void drawCenteredString(Graphics2D g, String text, Rectangle rect, int addX, int addY) {
		Font font = g.getFont();
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2 + addX;
		int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + addY;
		g.drawString(text, x, y);
	}
	
	public void drawCenteredString(Graphics2D g, String text, Rectangle rect) {
		drawCenteredString(g, text, rect, 0, 0);
	}
	
	public void drawCenteredImage(Graphics2D g, Image i, Rectangle rect, int addX, int addY) {
		int x = rect.x + (rect.width - i.getWidth(null)) / 2 + addX;
		int y = rect.y + (rect.height - i.getHeight(null)) / 2 + addY;
		g.drawImage(i, x, y, null);
	}
	
	public void drawCenteredImage(Graphics2D g, Image i, Rectangle rect) {
		drawCenteredImage(g, i, rect, 0, 0);
	}
	
	public Rectangle createTextRect(Graphics2D g, String text, Rectangle rect, int addX, int addY) {
		FontMetrics mets = g.getFontMetrics(g.getFont());
		int x = rect.x + (rect.width - mets.stringWidth(text)) / 2 + addX;
		int y = rect.y + ((rect.height - mets.getHeight()) / 2) + mets.getAscent() + addY;
		return new Rectangle(x, y, mets.stringWidth(text), mets.getHeight());
	}
	
	public void addFont(int fontFormat, String fontNamePath) {
		try {
			URL url = getClass().getClassLoader().getResource(fontNamePath);
			Font f = null;
			if(ge == null) {
				ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			}
			if(url == null) {
				String temp = getClass().getName().replace(".", "/")+".class";
				url = getClass().getClassLoader().getResource(temp);
			}
			if(url.getProtocol().equals("jar")) {
				f = Font.createFont(fontFormat, getClass().getClassLoader().getResourceAsStream(fontNamePath));
			}else {
				f = Font.createFont(fontFormat, new File(url.getFile()));
			}
			ge.registerFont(f);
			System.out.println("Font Added: "+f.getFontName());
		}catch (IOException | FontFormatException e) {
			System.err.println("ERROR in GraphicsFunctions.addFont");
		}
	}
	
	public Color blend(Color c0, Color c1) {
		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;
		
		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());
		
		return new Color((int) r, (int) g, (int) b, (int) a);
	}
	
}
