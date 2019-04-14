package examples;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import engine.Window;

public class CreateBasicWindow extends Window{
	
	private static final long serialVersionUID = -7405447395960943332L;
	
	//Painting/updating variables
	int x = 0;

	public CreateBasicWindow() {
		super("Example Window");
		setWindowSize(new Dimension(100, 100));
		addKeyListen(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if(key == KeyEvent.VK_ESCAPE) {
					stop();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}
			
		});
		setVisible();		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillOval(x, 30, 20, 40);
	}
	
	@Override
	public void update() {
		x = x > 100 ? -20 : x + 1;
	}
	
}
