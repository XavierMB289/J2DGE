package backend;

public class Vector2D {
	
	private double x, y;
	
	public Vector2D() {
		set(0,0);
	}
	
	/**
	 * Creates a Vector2D from a set of coords
	 * @param x An X coord
	 * @param y A Y coord
	 */
	public Vector2D(double x, double y) {
		set(x,y);
	}
	
	/**
	 * Creates a Vector2D from an Angle in Radians
	 * @param a An angle in Radians
	 * @return Vector2D
	 */
	public static Vector2D fromAngle(double a) {
		return new Vector2D(Math.cos(a), Math.sin(a));
	}
	
	/**
	 * Creates a Vector2D from an Angle in Radians with a selected length
	 * @param a An angle in Radians
	 * @param l A length for the angle
	 * @return Vector2D
	 */
	public static Vector2D fromAngle(double a, double l) {
		return new Vector2D(l * Math.cos(a), l * Math.sin(a));
	}
	
	/**
	 * Sets the angle of the Vector2D
	 * @param a An angle in Radians
	 */
	public void setAngle(double a) {
		set(Math.cos(a), Math.sin(a));
	}
	
	/**
	 * Sets the angle and length of the Vector2D
	 * @param a An angle in Radians
	 * @param l A length
	 */
	public void setAngle(double a, double l) {
		set(l * Math.cos(a), l * Math.sin(a));
	}
	
	/**
	 * Creates a Vector2D from a random angle
	 * @return Vector2D
	 */
	public static Vector2D random2D() {
		return fromAngle(Math.random() * Math.PI * 2);
	}
	
	@Override
	public String toString() {
		return "Vector2D Object: ("+x+", "+y+")";
	}
	
	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}
	
	public void set(double num) {
		this.x = num;
		this.y = num;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(double num) {
		this.x += num;
		this.y += num;
	}
	
	public void add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
	}
	
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}
	
	public void sub(double num) {
		this.x -= num;
		this.y -= num;
	}
	
	public void sub(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}
	
	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}
	
	public void mult(double num) {
		this.x *= num;
		this.y *= num;
	}
	
	public void mult(Vector2D v) {
		this.x *= v.x;
		this.y *= v.y;
	}
	
	public void mult(double x, double y) {
		this.x *= x;
		this.y *= y;
	}
	
	public void div(double num) {
		this.x /= num;
		this.y /= num;
	}
	
	public void div(Vector2D v) {
		this.x /= v.x;
		this.y /= v.y;
	}
	
	public void div(double x, double y) {
		this.x /= x;
		this.y /= y;
	}
	
	public double mag() {
		return Math.sqrt(magSq());
	}
	
	public double magSq() {
		return x*x+y*y;
	}
	
	public double dot(Vector2D v) {
		return dot(v.x,v.y);
	}
	
	public double dot(double x, double y) {
		return this.x*x+this.y*y;
	}
	
	public double dist(Vector2D v) {
		Vector2D ret = v.clone();
		ret.sub(this);
		return ret.mag();
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
	
	public void setMag(double num) {
		normalize();
		mult(num);
	}
	
	public double heading() {
		double h = Math.atan2(y,x);
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
	
	public boolean equals(Vector2D v) {
		return x == v.x && y == v.y;
	}
	
	public boolean equals(double x, double y) {
		return this.x == x && this.y == y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}
