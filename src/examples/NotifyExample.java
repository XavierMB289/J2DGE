package examples;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import engine.Notify;
import engine.Window;

public class NotifyExample extends Window{
	
	private static final long serialVersionUID = 6482284381788890188L;
	Notify notify = null;
	
	public NotifyExample() {
		super("Notify Example");
		
		notify = new Notify();
		
		setWindowSize(new Dimension(400, 400));
		addMouseListen(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				notify.WindowsNotify("Notification Example", "("+x+", "+y+")", "Example");
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		});
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
	
	public static void main(String[] args) {
		new NotifyExample();
	}
	
}
