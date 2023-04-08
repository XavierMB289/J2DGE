package handler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import backend.obj.GameScreen;
import engine.GameEngine;

public class ScreenHandler {
	
	ScreenGetter sg;
	
	GameScreen currentScreen;
	GameScreen overlay;
	GameScreen nextScreen;
	
	public void preInit() {
		sg = new ScreenGetter();
	}
	
	public void setScreens(GameEngine ge, Class<?> clazz, String filename) {
		currentScreen = sg.getGameScreen(ge, clazz, filename);
		nextScreen = currentScreen;
	}
	
	public void setOverlay(GameEngine ge, Class<?> clazz, String filename) {
		overlay = sg.getGameScreen(ge, clazz, filename);
	}
	
	public void changeScreen(GameEngine ge, Class<?> clazz, String filename) {
		nextScreen = sg.getGameScreen(ge, clazz, filename); //TODO: screen transitions
	}
	
	public GameScreen getScreen() {
		return currentScreen;
	}
	
}

class ScreenGetter{
	
	private String getSingleScreen(Class<?> clazz, String filename) {
		URL url = clazz.getClassLoader().getResource("pages/");
		if(url != null && url.getProtocol().equals("file")) {
			try {
				String[] ret = new File(url.toURI()).list();
				for(String r : ret) {
					if(r.contains(filename)) {
						return r;
					}
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(url == null) {
			String temp = clazz.getName().replace(".", "/")+".class";
			url = clazz.getClassLoader().getResource(temp);
		}
		if(url.getProtocol().equals("jar")) {
			String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
			JarFile jar = null;
			try {
				jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			} catch (IOException e) {
				System.err.println("Cannot find JAR in FileHandler.getFilesFromDir");
			}
			Enumeration<JarEntry> entries = jar.entries();
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if(name.startsWith(filename)) {
					String entry = name.substring(filename.length());
					int checkSubdir = entry.indexOf("/");
					if(checkSubdir >= 0) {
						entry = entry.substring(0, checkSubdir);
					}
					return entry;
				}
			}
		}
		return "";
	}
	
	public GameScreen getGameScreen(GameEngine en, Class<?> clazz, String filename) {
		String pn = getSingleScreen(clazz, filename);
		try {
			if(!pn.isEmpty() && !pn.matches(".*\\d.*")) {
				Class<?> c = Class.forName("pages."+pn.split("\\.")[0]);
				Constructor<?> con = c.getConstructor(new Class[] { GameEngine.class });
				Object o = con.newInstance(en);
				if(o instanceof GameScreen) {
					GameScreen gs = (GameScreen) o;
					gs.init();
					return gs;
				}
			}
		}catch(ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println("ERROR when loading screens in ScreenGetter.getGameScreen");
			e.printStackTrace();
		}
		return null;
	}
	
}
