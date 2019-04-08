package engine;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notify {
	
	private Image icon = null;
	
	public Notify() {
		icon = Toolkit.getDefaultToolkit().createImage("icon.png");
	}
	
	public void setIcon() {
		
	}
	
	public void WindowsNotify(String title, String desc){
        SystemTray tray = SystemTray.getSystemTray();

        TrayIcon trayIcon = new TrayIcon(icon, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        
        try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        trayIcon.displayMessage(title, desc, MessageType.INFO);
    }
}
