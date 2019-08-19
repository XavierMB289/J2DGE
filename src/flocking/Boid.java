package flocking;

import java.util.ArrayList;

import backends.Vector2D;
import engine.Window;

/*
 * Original Author: Coding Train
 * Transcribed by: Xavier Bennett
 */
public class Boid {
	
	protected Window w;
	
	protected Vector2D position, velocity, acceleration;
	protected double maxForce;
	protected double maxSpeed;
	protected double perception = 25;
	protected double mousePerc = 150;

	public Boid(Window w, double x, double y) {
		this.w = w;
		position = new Vector2D(x, y);
		velocity = Vector2D.random2D();
		velocity.setMag(Math.round(Math.random()*2)+2);
		acceleration = new Vector2D();
		maxForce = 1;
		maxSpeed = 1;
	}
	
	public void setPos(double x, double y) {
		position.set(x, y);
	}
	
	public Vector2D getPos() {
		return position;
	}
	
	public Vector2D align(ArrayList<? extends Boid> boids) {
		Vector2D steer = new Vector2D();
		double total = 0;
		for(Boid b : boids) {
			double d = Vector2D.dist(position, b.position);
			if(b != this && d < perception) {
				steer.add(b.velocity);
				total++;
			}
		}
		if(total > 0) {
			steer.div(total);
			steer.setMag(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}
	
	public Vector2D separation(ArrayList<? extends Boid> boids) {
		Vector2D steer = new Vector2D();
		double total = 0;
		for(Boid b : boids) {
			double d = Vector2D.dist(position, b.position);
			if(b != this && d < perception) {
				Vector2D diff = Vector2D.sub(position, b.position);
				diff.div(d*d);
				steer.add(diff);
				total++;
			}
		}
		if(total > 0) {
			steer.div(total);
			steer.setMag(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}
	
	public Vector2D cohesion(ArrayList<? extends Boid> boids) {
		Vector2D steer = new Vector2D();
		double total = 0;
		for(Boid b : boids) {
			double d = Vector2D.dist(position, b.position);
			if(b != this && d < perception*2) {
				steer.add(b.position);
				total++;
			}
		}
		if(total > 0) {
			steer.div(total);
			steer.sub(position);
			steer.setMag(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}
	
	public void towardsMouse(ArrayList<? extends Boid> boids, int mx, int my) {
		Vector2D steer = new Vector2D();
		Vector2D mouse = new Vector2D(mx, my);
		double d = Vector2D.dist(position, mouse);
		if(d < mousePerc) {
			steer.add(Vector2D.sub(mouse, position));
		}
		steer.div(50);
		acceleration.add(steer);
	}
	
	public void flock(ArrayList<? extends Boid> boids) {
		Vector2D alignment = align(boids);
		Vector2D coh = cohesion(boids);
		Vector2D sep = separation(boids);
		
		alignment.mult(0.5);
		coh.mult(1);
		sep.mult(4);
		
		acceleration.add(alignment);
		acceleration.add(coh);
		acceleration.add(sep);
	}
	
	public void update() {
		position.add(velocity);
		velocity.add(acceleration);
		velocity.limit(maxSpeed);
		acceleration.set(0, 0);
	}

}
