package physics;

public class PhysicsObject {

	private double VEL;
	
	private double ACC;
	
	private double MASS;
	
	private double FORCE;
	
	public void setVelocity(double vel) {
		VEL = vel;
	}
	
	public void setMass(double mass) {
		MASS = mass;
	}
	
	public void updateSpeed() {
		ACC += VEL;
		FORCE = MASS * ACC;
	}

}
