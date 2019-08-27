package engine;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javax.swing.ImageIcon;

import handler.ImageHandler;

public class Notify extends ImageHandler{
	
	private static final long serialVersionUID = -8044873457720248395L;
	
	private transient ImageIcon icon = null;
	
	/**
	 * @author Xavier Bennett
	 * @param filename The filename and relative path to the image...
	 */
	public void setIcon(ImageIcon img) {
		icon = img;
	}
	
	public void WindowsNotify(String title, String desc, String tooltip){
		
		icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("icon.png"));
		
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
    }
}
