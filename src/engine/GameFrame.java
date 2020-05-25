package engine;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;

import backends.objs.Vector2D;

public class GameFrame extends JFrame{

	private static final long serialVersionUID = -9161955526907699634L;
	
	GameWindow w;

	private int HALF_W = -1, HALF_H = -1;
	private double H12, W12;
	private Rectangle WINDOW_RECT, TOP_RECT, BOTTOM_RECT, LEFT_RECT, RIGHT_RECT;
	private Vector2D SCREEN_CENTER;
	
	public GameFrame(GameWindow w) {
		this.w = w;
	}
	
	public void init() {
		HALF_W = getWidth() / 2;
		HALF_H = getHeight() / 2;

		WINDOW_RECT = new Rectangle(getWidth(), getHeight());
		TOP_RECT = new Rectangle(0, 0, getWidth(), HALF_H);
		BOTTOM_RECT = new Rectangle(0, HALF_H, getWidth(), HALF_H);
		LEFT_RECT = new Rectangle(0, 0, HALF_W, getHeight());
		RIGHT_RECT = new Rectangle(HALF_W, 0, HALF_W, getHeight());
		W12 = (int) Math.floor(getWidth() / 12);
		H12 = (int) Math.floor(getHeight() / 12);
		
		//Setting middle of screen
		SCREEN_CENTER = new Vector2D(getLocationOnScreen().x+HALF_W, getLocationOnScreen().y+HALF_H);
	}
	
	@Override
	public void paint(Graphics g) {
		w.paint(g);
	}

	public int getHALF_W() {
		return HALF_W;
	}

	public int getHALF_H() {
		return HALF_H;
	}

	public double getH12() {
		return H12;
	}

	public double getW12() {
		return W12;
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
