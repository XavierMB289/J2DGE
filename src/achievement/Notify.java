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
	 * @param filename The filename and relative path to the image...
	 */
	public void setIcon(ImageIcon img) {
		icon = img;
	}
	
	public void setIcon(String name) {
		icon = getImage(name);
	}
	
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
