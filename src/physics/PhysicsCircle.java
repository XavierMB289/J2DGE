package physics;

import backends.objs.Vector2D;

public class PhysicsCircle extends PhysicsObj{

	private double radius;
	
	public PhysicsCircle(double r, Vector2D p) {
		radius = r;
		center = p;
	}

	@SuppressWarnings("unused")
	public void collide(PhysicsCircle c) {
		Vector2D now = center.clone();
		Vector2D later = Vector2D.add(center, vel);
		
		double dist = Math.sqrt(Math.pow(later.x - c.center.x, 2) + Math.pow(later.y - c.center.y, 2));
		double radi = radius + c.radius;
		
		if(dist == radi) { //touching
			
		}else if(dist > radi) { //not touching
			
		}else if(dist < radi) { //intersecting
			
		}
	}
}
