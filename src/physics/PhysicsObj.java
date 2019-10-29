package physics;

import backends.Vector2D;

public abstract class PhysicsObj {
	
	protected Vector2D acc;
	
	protected Vector2D vel;
	
	protected double maxSpeed;
	
	protected Vector2D center;
	
	public void updateSpeed() {
		vel.add(acc);
		vel.limit(maxSpeed);
		center.add(vel);
	}

}
