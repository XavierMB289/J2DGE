package backend;

import java.awt.Image;
import java.awt.Rectangle;

public class ImageCollider {
	
	Image img;
	Vector2D coords;
	Rectangle rect;
	
	public ImageCollider(Image img) {
		this.img = img;
		coords = new Vector2D();
		rect = new Rectangle();
	}
	
	public void setCoords(int x, int y) {
		coords.set(x, y);
		rect.setBounds(x, y, img.getWidth(null), img.getHeight(null));
	}
	
	public double getX() {
		return coords.getX();
	}
	
	public double getY() {
		return coords.getY();
	}
	
	/**
	 * Uses the rectangle and checks if the given coords are inside
	 * @param x X Coord to check
	 * @param y Y Coord to check
	 * @return true if coords provided are inside the image
	 */
	public boolean fastCheck(int x, int y) {
		return rect.contains(x, y);
	}
	
	/**
	 * Uses the rectangle and checks if the coords and image collide with it
	 * @param other
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean fastCheck(Image other, int x, int y) {
		return rect.intersects(x, y, other.getWidth(null), other.getHeight(null));
	}
	
}
