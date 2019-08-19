package objs;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import backends.Entity;
import engine.Window;

public abstract class Tower extends Entity implements Serializable{
	
	private static final long serialVersionUID = 8531681416717313527L;
	
	protected Point tower;
	protected Ellipse2D.Double base;
	
	public Tower(Window w, String pageName) {
		super(w, pageName);
	}

	public AffineTransform rotateTowards(Graphics2D g, Point enemy) {
		AffineTransform oldAT = g.getTransform();
		
		double angle = Math.atan2(tower.getY() - enemy.getY(), tower.getX() - enemy.getX()) - Math.PI ;

		g.rotate(angle);
		
		return oldAT;
	}
	
}
