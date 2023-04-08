package window;

import java.awt.Rectangle;

import javax.swing.JFrame;

import backend.Vector2D;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = 3324959059143922163L;
	
	private int HALF_W = -1, HALF_H = -1;
	private double H9, W16;
	private Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT, LEFT_RECT, RIGHT_RECT;
	private Vector2D SCREEN_CENTER;
	
	public void init() {
		HALF_W = getWidth() / 2;
		HALF_H = getHeight() / 2;
		
		WINDOW_RECT = new Rectangle(getWidth(), getHeight());
		TOP_RECT = new Rectangle(0, 0, getWidth(), HALF_H);
		BOTTOM_RECT = new Rectangle(0, HALF_H, getWidth(), HALF_H);
		LEFT_RECT = new Rectangle(0, 0, HALF_W, getHeight());
		RIGHT_RECT = new Rectangle(HALF_W, 0, HALF_W, getHeight());
		W16 = Math.floor(getWidth() / 16);
		H9 = Math.floor(getHeight() / 9);
		
		SCREEN_CENTER = new Vector2D(getLocationOnScreen().x+HALF_W, getLocationOnScreen().y+HALF_H);
	}

	public int getHALF_W() {
		return HALF_W;
	}

	public int getHALF_H() {
		return HALF_H;
	}

	public double getH9() {
		return H9;
	}

	public double getW16() {
		return W16;
	}

	public Rectangle getWINDOW_RECT() {
		return WINDOW_RECT;
	}

	public Rectangle getTOP_RECT() {
		return TOP_RECT;
	}

	public Rectangle getBOTTOM_RECT() {
		return BOTTOM_RECT;
	}

	public Rectangle getLEFT_RECT() {
		return LEFT_RECT;
	}

	public Rectangle getRIGHT_RECT() {
		return RIGHT_RECT;
	}

	public Vector2D getSCREEN_CENTER() {
		return SCREEN_CENTER;
	}
	
}
