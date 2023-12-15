package window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppWindow {
	
	private JFrame frame;
	
	private ImagePanel panel;
	
	private boolean isExclusiveFullscreen;
	
	/**
	 * Runs during the GameEngine.preInit()
	 * DO NOT MANUALLY CALL
	 */
	public void preInit() {
		frame = new JFrame();
		isExclusiveFullscreen = false;
	}
	
	/**
	 * Runs during the GameEngine.init()
	 * DO NOT MANUALLY CALL
	 */
	public void init() {
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setUndecorated(true);
	}
	
	/**
	 * Runs during the GameEngine.postInit()
	 * @param w Width of the JPanel
	 * @param h Height of the JPanel
	 * DO NOT MANUALLY CALL
	 */
	public void postInit(int w, int h) {
		panel = new ImagePanel();
		frame.setContentPane(panel);
		frame.validate();
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Stops the AppWindow
	 * DO NOT MANUALLY CALL
	 */
	public void stop() {
		if(isExclusiveFullscreen) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
		}
		frame.setVisible(false);
		frame.dispose();
	}
	
	/**
	 * Paints the given Image on the JPanel
	 * @param img Image to paint
	 * DO NOT MANUALLY CALL
	 */
	public void paint(BufferedImage img) {
		panel.setImg(img);
		panel.repaint();
	}
	
	/**
	 * Sets the title of the window
	 * @param title String representing the Title wanted
	 */
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	/**
	 * Sets the frame to Windowed Fullscreen
	 * This uses slightly more RAM/CPU but allows for the user
	 * to switch between windows easily
	 */
	public void setFullscreen() {
		GraphicsDevice gd = frame.getGraphicsConfiguration().getDevice();
		if(frame.isResizable()) {
			frame.setResizable(false);
			frame.setPreferredSize(new Dimension(gd.getDefaultConfiguration().getBounds().getSize()));
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}
	
	/**
	 * Sets the frame to Fullscreen Exclusive Mode
	 * This uses less ram, but locks the window to the monitor
	 * (Automatically fullscreens on the DEFAULT screen)
	 */
	public void setFullscreenExclusive() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = env.getDefaultScreenDevice();
		if(device.isFullScreenSupported()) {
			device.setFullScreenWindow(frame);
			isExclusiveFullscreen = true;
		}else {
			setFullscreen();
		}
	}
	
	/**
	 * Sets the window size to the Dimension given
	 * @param minimumSize Dimension of the window
	 */
	public void setWindowSize(Dimension minimumSize) {
		frame.setMinimumSize(minimumSize);
		frame.setLocationRelativeTo(null);
		frame.setFocusable(true);
		frame.requestFocus();
	}
	
	/**
	 * Changes the Location of the window by ADDING
	 * @param addX Amount to ADD to the X Coord
	 * @param addY Amount to ADD to the Y Coord
	 */
	public void changeLocation(int addX, int addY) {
		frame.setLocation(frame.getX() + addX, frame.getY() + addY);
	}
	
	/**
	 * Adds a KeyListener to the JFrame
	 * @param listener Custom KeyListener
	 */
	public void addKeyListener(KeyListener listener) {
		frame.addKeyListener(listener);
	}
	
	/**
	 * Adds a MouseListener to the JFrame
	 * @param listener Custom MouseListener
	 */
	public void addMouseListener(MouseListener listener) {
		frame.addMouseListener(listener);
	}
	
	/**
	 * Adds a MouseMotionListener to the JFrame
	 * @param listener Custom MouseMotionListener
	 */
	public void addMouseMotionListener(MouseMotionListener listener) {
		frame.addMouseMotionListener(listener);
	}
	
	/**
	 * WARNING: Use at own risk.
	 * @param comp Component to add to JFrame
	 */
	public void addGeneric(Component comp) {
		frame.add(comp);
	}
	
	/**
	 * Gets the Resolution of the Screen/JFrame
	 * @return Integer of the Resolution Width
	 */
	public int getResWidth() {
		return frame.getWidth();
	}
	
	/**
	 * Gets the Resolution of the Screen/JFrame
	 * @return Integer of the Resolution Height
	 */
	public int getResHeight() {
		return frame.getHeight();
	}
	
}

/**
 * This is how the image is painted on the screen.
 * @author silve
 * DO NOT USE!
 */
class ImagePanel extends JPanel {
	
	private static final long serialVersionUID = 1664315044350953717L;
	
	private BufferedImage paintImg;
	
	public void setImg(BufferedImage img) {
		paintImg = img;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(paintImg != null) {
			g.drawImage(paintImg.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);
		}
	}
	
}