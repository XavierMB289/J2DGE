package engine;

import java.io.File;
import java.util.ArrayList;

import backends.Clipping;

public class AudioHandler {
	
	public Clipping getClipping(String filename) {
		return new Clipping(filename);
	}
	
	public Clipping[] loadClippings(String filename) {
		File folder = new File(filename);
		File[] files = folder.listFiles();
		ArrayList<Clipping> ret = new ArrayList<>();
		for(File f: files) {
			if(f.isFile()) {
				ret.add(new Clipping(f.getName()));
			}
		}
		return (Clipping[]) ret.toArray();
	}
	
}
