package window;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow{
	
	private GameFrame frame;
	
	private boolean isExclusiveMode;
	
	private ImagePanel panel;
	
	public void preInit() {
		frame = new GameFrame();
		isExclusiveMode = false;
	}
	
	public void init() {
		
	}
	
	public GameFrame getFrame() {
		return frame;
	}
	
	public void setFullscreen() {
		GraphicsDevice gd = frame.getGraphicsConfiguration().getDevice();
		if(frame.isResizable()) {
			frame.setResizable(false);
			frame.setPreferredSize(new Dimension(gd.getDefaultConfiguration().getBounds().getSize()));
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}
	
	public void fullscreenExclusive() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if(device.isFullScreenSupported()) {
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			device.setFullScreenWindow(frame);
			frame.setFocusable(true);
			frame.requestFocus();
			isExclusiveMode = true;
		}else {
			setFullscreen();
		}
	}
	
	public void setWindowSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.requestFocus();
	}
	
	public void changeLocation(int addX, int addY) {
		frame.setLocation(frame.getX() + addX, frame.getY() + addY);
	}
	
	public void finalize() {
		frame.setVisible(false);
		frame.validate();
		frame.pack();
		frame.setVisible(true);
		
		//Flicker to add panel
		panel = new ImagePanel(new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB));
		frame.setVisible(false);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
		
		frame.init();
	}
	
	public void stop() {
		if(isExclusiveMode) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		}
		frame.setVisible(false);
		frame.dispose();
	}
	
	public void paint(BufferedImage img) {
		panel.setImg(img);
		panel.repaint();
	}
	
}

class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 1664315044350953717L;
	
	BufferedImage paintImg;
	
	ImagePanel(BufferedImage img){
		super(true);
		paintImg = img;
		setSize(new Dimension(paintImg.getWidth(), paintImg.getHeight()));
	}
	
	public void setImg(BufferedImage img) {
		paintImg = img;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(paintImg.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);
	}
	
}
