package handler;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GameFileHandler {
	
	public Image loadImage(String filename) {
		if(filename.charAt(1) == ':') { //if filename is absolute path
			try {
				return ImageIO.read(getClass().getResource(filename));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File loaded = loadFile(filename);
		if(loaded != null && loaded.exists()) {
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
		return new File(filename);
	}
	
	public String readFile(String filename) {
		String[] lines = readFileLines(filename);
		String ret = "";
		for(String line : lines) {
			ret = ret + line;
		}
		return ret;
	}
	
	public String[] readFileLines(String filename) {
		String[] ret = new String[0];
		
		File file = loadFile(filename);
		try {
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				ret = Arrays.copyOf(ret, ret.length+1);
				ret[ret.length-1] = scan.nextLine();
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public void writeFile(String filename, String[] lines) {
		try {
			FileWriter writer = new FileWriter(filename);
			for(String line : lines) {
				if(line != lines[lines.length-1]) {
					writer.write(line+System.lineSeparator());
				}else {
					writer.write(line);
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
