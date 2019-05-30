package doomlike;

import java.awt.Graphics2D;

public class Boundary {
	
	Vector a, b;
	
	public Boundary(int x1, int y1, int x2, int y2) {
		a = new Vector(x1, y1);
		b = new Vector(x2, y2);
	}
	
	public void paint(Graphics2D g) {
		g.drawLine((int)a.x, (int)a.y, (int)b.x, (int)b.y);
	}
	
}
