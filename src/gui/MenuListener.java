package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import engine.FileHandler;

public class MenuListener implements ActionListener{
	
	Main main;
	
	FileHandler FH = null;
	
	String project = null;
	
	public MenuListener(Main m) {
		main = m;
		project = System.getProperty("user.home")+"\\java2DGameEngine\\";
		FH = new FileHandler(project, "https://github.com/XavierMB289/java2DGameEngine/tree/master/src");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String key = e.getActionCommand();
		switch(key) {
			//File
			case "Java Project":
				String filename = JOptionPane.showInputDialog("Project Name:");
				FH.createFile(filename);
				FH.download("/gui/copying/", "BasicMain.java");
				break;
			case "Java File":
				break;
			case "Text File":
				break;
			case "Object File":
				break;
			case "Other...":
				break;
			case "Open Project":
				break;
			case "Save":
				break;
			case "Export...":
				break;
			case "Exit":
				main.stop();
				break;
			//Project
			case "Build":
				break;
			case "Properties":
				break;
			//Window
			case "Toggle Fullscreen":
				break;
			case "New Window...":
				break;
			case "Resize Window":
				break;
			//Help
			case "Search":
				break;
			case "Refresh Plugins":
				break;
			case "Check for Update":
				break;
		}
	}
	
}
