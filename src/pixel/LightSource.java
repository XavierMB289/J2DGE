package pixel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;

import backend.Vector2D;

public class LightSource { //TODO: Pixelate
	
	Vector2D point = null;
	int angle;
	
	Color color = null;
	
	int length;
	
	RadialGradientPaint rgp = null;
	
	public void init() {
		angle = 1;
		color = Color.yellow;
		length = 1;
	}
	
	public void setAngle(int a) {
		angle = Math.min(180, a);
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setRadialGradient(int x, int y, int length, int alphaMin) {
		if(point == null) {
			point = new Vector2D();
		}
		point.set(x, y);
		this.length = length;
		rgp = new RadialGradientPaint(x, y, length, new float[] {0f, 1f}, new Color[] {color, new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.min(alphaMin, 255))});
	}
	
	public void paint(Graphics2D g) {
		if(point != null) {
			g.setPaint(rgp);
			g.fillArc((int)point.getX()-(length/2), (int)point.getY()-(length/2), length, length, 270-(angle/2), angle);
		}
	}
	
}
