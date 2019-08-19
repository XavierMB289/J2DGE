package flocking;

import java.util.ArrayList;

import engine.Window;

public class Flock {
	
	protected Window w;

	protected ArrayList<Boid> boids;
	protected  boolean use_mouse = false;
	protected  int mx, my = 0;
	
	public Flock(Window w) {
		this.w = w;
		boids = new ArrayList<>();
		mx = w.WIDTH/2;
		my = w.HEIGHT/2;
	}
	
	public void addBoid(double x, double y) {
		boids.add(new Boid(w, x, y));
	}
	
	public void update() {
		for(Boid b : boids) {
			b.flock(boids);
			if(use_mouse) {
				b.towardsMouse(boids, mx, my);
			}
			b.update();
		}
	}
	
	public ArrayList<Boid> getBoids(){
		return boids;
	}
	
}
