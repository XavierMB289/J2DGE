package backends.objs;

/*
 * Credit to the Javascript P5 Library Team
 * Transcribed by Xavier Bennett
 */
public class Vector2D {
	
	public double x, y;

	public Vector2D() { 
		set(0, 0);
	}
	
	public Vector2D(double x, double y) {
		set(x, y);
	}
	
	public Vector2D(Vector2D v) {
		set(v);
	}
	
	public void set(Vector2D v) {
		x = v.x;
		y = v.y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vector2D v) {
		x += v.x;
		y += v.y;
	}
	
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public static Vector2D add(Vector2D a, Vector2D b) {
		Vector2D ret = a.clone();
		ret.add(b);
		return ret;
	}
	
	public void sub(Vector2D v) {
		x -= v.x;
		y -= v.y;
	}
	
	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}
	
	public static Vector2D sub(Vector2D a, Vector2D b) {
		Vector2D ret = a.clone();
		ret.sub(b);
		return ret;
	}
	
	public void mult(double num) {
		x *= num;
		y *= num;
	}
	
	public static Vector2D mult(Vector2D a, double n) {
		Vector2D ret = a.clone();
		ret.mult(n);
		return ret;
	}
	
	public void div(double num) {
		num = num > 0 ? num : 1;
		x /= num;
		y /= num;
	}
	
	public static Vector2D div(Vector2D a, double n) {
		Vector2D ret = a.clone();
		ret.div(n);
		return ret;
	}
	
	public double mag() {
		return Math.sqrt(magSq());
	}
	
	public double magSq() {
		return x * x + y * y;
	}
	
	public double dot(Vector2D v) {
		return dot(v.x, v.y);
	}
	
	public double dot(double x, double y) {
		return this.x * x + this.y * y;
	}
	
	public static double dot(Vector2D a, Vector2D b) {
		return a.dot(b);
	}
	
	public double dist(Vector2D v) {
		Vector2D ret = v.clone();
		ret.sub(this);
		return ret.mag();
	}
	
	public static double dist(Vector2D a, Vector2D b) {
		return a.dist(b);
	}
	
	public void normalize() {
		double len = mag();
		if(len != 0) mult(1/len);
	}
	
	public void limit(double max) {
		double msq = magSq();
		if(msq > max*max) {
			div(Math.sqrt(msq));
			mult(max);
		}
	}
	
	public void setMag(double n) {
		normalize();
		mult(n);
	}
	
	public double heading() {
		double h = Math.atan2(y, x);
		return h;
	}
	
	public void rotate(double a) {
		double newH = heading() + a;
		double mag = mag();
		x = Math.cos(newH)*mag;
		y = Math.sin(newH)*mag;
	}
	
	public double angleBetween(Vector2D v) {
		double dotmag = dot(v) / (mag() * v.mag());
		double angle = Math.acos(Math.min(1, Math.max(-1, dotmag)));
		return angle;
	}
	
	public void lerp(Vector2D v, double amt) {
		lerp(v.x, v.y, amt);
	}
	
	public void lerp(double x, double y, double amt) {
		this.x += (x - this.x) * amt;
		this.y += (y - this.y) * amt;
	}
	
	public static Vector2D lerp(Vector2D a, Vector2D b, double amt) {
		Vector2D ret = a.clone();
		ret.lerp(b, amt);
		return ret;
	}
	
	public static Vector2D fromAngle(double a) {
		return new Vector2D(Math.cos(a), Math.sin(a));
	}
	
	public static Vector2D fromAngle(double a, double l) {
		return new Vector2D(l * Math.cos(a), l * Math.sin(a));
	}
	
	public static Vector2D random2D() {
		return fromAngle(Math.random() * Math.PI * 2);
	}
	
	@Override
	public String toString() {
		return "Vector2D Obj: ("+x+", "+y+")";
	}
	
	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}
	
	public boolean equals(Vector2D v) {
		return x == v.x && y == v.y;
	}
	
	public boolean equals(double x, double y) {
		return this.x == x && this.y == y;
	}

}
