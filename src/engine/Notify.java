package engine;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notify extends ImageHandler{
	
	private Image icon = null;
	
	public Notify() {
		icon = Toolkit.getDefaultToolkit().createImage("icon.png");
	}
	
	/**
	 * @author Xavier Bennett
	 * @param filename The filename and relative path to the image...
	 */
	public void setIcon(Image img) {
		icon = img;
	}
	
	public void WindowsNotify(String title, String desc, String tooltip){
        SystemTray tray = SystemTray.getSystemTray();

        TrayIcon trayIcon = new TrayIcon(icon, "3D Game Engine");
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
