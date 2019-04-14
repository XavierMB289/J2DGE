package gui;

import java.awt.Dimension;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import engine.Window;

public class Main extends Window{
	
	private static final long serialVersionUID = -7748570942727928342L;
	
	MenuListener ML = null;
	
	public void addNewItem(JMenuItem jmi, JMenu m) {
		jmi.addActionListener(ML);
		m.add(jmi);
	}

	public JMenuBar createMenu() {
		//Menu Bar
		JMenuBar ret = new JMenuBar();
		//file system
		JMenu file = new JMenu("File");
		JMenu fileNew = new JMenu("Create New...");
		addNewItem(new JMenuItem("Java Project"), fileNew);
		addNewItem(new JMenuItem("Java File"), fileNew);
		addNewItem(new JMenuItem("Text File"), fileNew);
		addNewItem(new JMenuItem("Object File"), fileNew);
		addNewItem(new JMenuItem("Other..."), fileNew);
		file.add(fileNew);
		addNewItem(new JMenuItem("Open Project"), file);
		file.addSeparator();
		addNewItem(new JMenuItem("Save"), file);
		addNewItem(new JMenuItem("Export..."), file);
		file.addSeparator();
		addNewItem(new JMenuItem("Exit"), file);
		//project
		JMenu project = new JMenu("Project");
		addNewItem(new JMenuItem("Build"), project);
		project.addSeparator();
		addNewItem(new JMenuItem("Properties"), project);
		//window
		JMenu window = new JMenu("Window");
		addNewItem(new JMenuItem("Toggle Fullscreen"), window);
		addNewItem(new JMenuItem("New Window..."), window);
		window.addSeparator();
		addNewItem(new JMenuItem("Resize Window"), window);
		//help
		JMenu help = new JMenu("Help");
		addNewItem(new JMenuItem("Search"), help);
		help.addSeparator();
		addNewItem(new JMenuItem("Refresh Plugins"), help);
		addNewItem(new JMenuItem("Check for Update"), help);
		
		ret.add(file);
		ret.add(project);
		ret.add(window);
		ret.add(help);
		return ret;
	}
	
	public Main() {
		super("2D Java Game Engine");
		ML = new MenuListener(this);
		setWindowSize(new Dimension(500, 300));
		setMenuBar(createMenu());
		setVisible();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}
