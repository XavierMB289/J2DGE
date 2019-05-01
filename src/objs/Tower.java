package objs;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

public class Tower {
	
	public AffineTransform rotateTowards(Graphics2D g, Point tower, Point enemy) {
		AffineTransform oldAT = g.getTransform();
		
		double angle = Math.atan2(tower.getY() - enemy.getY(), tower.getX() - enemy.getX()) - Math.PI ;

		g.rotate(angle);
		
		return oldAT;
	}
	
}
