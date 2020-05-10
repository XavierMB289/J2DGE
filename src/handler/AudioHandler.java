package handler;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import backends.objs.Clipping;

public class AudioHandler implements Serializable{
	
	private static final long serialVersionUID = 5403259306717693728L;

	public Clipping getClipping(String filename) {
		return new Clipping(filename);
	}
	
	public Clipping[] loadClippings(String filename) {
		URL clips = getClass().getClassLoader().getResource(filename);
		if(clips != null) {
			File folder = new File(clips.getFile());
			File[] files = folder.listFiles();
			ArrayList<Clipping> list = new ArrayList<>();
			for(File f: files) {
				if(f.isFile()) {
					list.add(new Clipping(f.getName()));
				}
			}
			Clipping[] ret = new Clipping[list.size()];
			for(int i = 0; i < list.size(); i++) {
				ret[i] = list.get(i);
			}
			return ret;
		}
		return new Clipping[] {};
	}
	
	public void playSound(String filename) {
		getClipping(filename).play();
	}
	
}
