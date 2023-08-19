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
		if(filename.charAt(1) == ':') { //if filename is absolute path
			try {
				return ImageIO.read(new File(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File loaded = loadFile(filename);
		if(loaded.exists()) {
			try {
				return ImageIO.read(loaded);
			} catch (IOException e) {
				System.err.println("Couldn't find resource in "+filename+" in GameFileHandler.loadImage");
				return loadImage(new File(this.getClass().getResource("../img/missingNo.png").getFile()).getAbsolutePath());
			}
		}else {
			System.err.println("File does not exist: "+filename);
			return loadImage(new File(this.getClass().getResource("../img/missingNo.png").getFile()).getAbsolutePath());
		}
	}
	
	public File loadFile(String filename) {
		try {
			return new File(getClass().getResource(filename).toURI());
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
