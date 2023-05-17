package handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GameFileHandler {
	
	public BufferedImage loadImage(String filename) {
		try {
			return ImageIO.read(loadFile(filename));
		} catch (IOException e) {
			System.err.println("Couldn't find resource in "+filename+" in GameFileHandler.loadImage");
			return null;
		}
	}
	
	public File loadFile(String filename) {
		try {
			return new File(getClass().getResource(filename.substring(0, 1).equals("/") ? filename : "/"+filename).toURI());
		} catch (URISyntaxException e) {
			System.err.println("Couldn't find resource in "+filename+" in GameFileHandler.loadFile");
			return null;
		}
	}
	
	public String readFile(String filename) {
		String ret = "";
		
		File f = loadFile(filename);
		Scanner r = null;
		try {
			r = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't read resource in "+filename+" in GameFileHandler.readFile");
			return "";
		}
		while(r.hasNextLine()) {
			ret = ret + r.nextLine();
		}
		r.close();
		
		return ret;
	}
	
}
