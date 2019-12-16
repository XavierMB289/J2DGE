package achievement;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javax.swing.ImageIcon;

import engine.Window;
import handler.ImageHandler;

public class Notify extends ImageHandler{
	
	private static final long serialVersionUID = -8044873457720248395L;
	
	private ImageIcon icon = null;
	
	public Notify(Window w) {
		super(w);
	}
	
	/**
	 * @author Xavier Bennett
	 * @param img An ImageIcon.
	 * Sets the icon for the notification using the set ImageIcon
	 */
	public void setIcon(ImageIcon img) {
		icon = img;
	}
	
	/**
	 * @author Xavier Bennett
	 * @param name The name of an image.
	 * Sets the icon for the notification using the set filename
	 */
	public void setIcon(String name) {
		icon = getImage(name);
	}
	
	/**
	 * @author Xavier Bennett
	 * @param title Notification Title
	 * @param desc Description of Notification
	 * @param tooltip When hovered, display the string...
	 */
	public void windowsNotify(String title, String desc, String tooltip){
		if(SystemTray.isSupported()) {
			//if(icon == null) { //Cant use due to Java bug
				icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("icon.png"));
			//}
			
	        SystemTray tray = SystemTray.getSystemTray();
	
	        TrayIcon trayIcon = new TrayIcon(icon.getImage(), "3D Game Engine");
	        trayIcon.setImageAutoSize(true);
	        trayIcon.setToolTip(tooltip);
	        
	        try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        trayIcon.displayMessage(title, desc, MessageType.INFO);
		}else {
			System.err.println("SystemTray not supported...");
		}
    }
}
