package achieve;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notify {
	
	/**
	 *  Creates a Notification on the windows platform
	 *  @param i Image on the Notification
	 *  @param title Title of the Notification
	 *  @param desc Description of the Notification
	 *  
	 */
	public static void WindowsNotify(Image i, String title, String desc) {
		SystemTray tray = SystemTray.getSystemTray();
		Image temp = i;
		
		if(temp == null) {
			temp = Toolkit.getDefaultToolkit().createImage("icon.png");
		}
		
		TrayIcon icon = new TrayIcon(temp, "J2DGE");
		icon.setImageAutoSize(true);
		try {
			tray.add(icon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		icon.displayMessage(title, desc, MessageType.NONE);
	}
	
}
