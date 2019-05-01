package backends;


import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Clipping {
	
	Long currentFrame;
	Clip clip;
	
	String filename;
	String status;
	
	AudioInputStream ais;
	
	public Clipping(String filename) {
		
		this.filename = filename;
		
		try {
			ais = AudioSystem.getAudioInputStream(this.getClass().getResource(filename));
			clip = AudioSystem.getClip();
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("There's an error in the audio file."+
		"See if you got the relative location wrong, or if the file is in the wrong place...");
			System.exit(-1);
		}
		
	}
	
	public void play() {
		if(status.equals("pause") != true) {
			status = "play";
			clip.start();
		}else {
			clip.close();
			resetAudioStream();
			clip.setMicrosecondPosition(currentFrame);
			status = "";
			play();
		}
	}
	
	public void pause() {
		if(status.equals("play")) {
			currentFrame = clip.getMicrosecondPosition();
			clip.stop();
			status = "pause";
		}
	}
	
	public void restart() {
		clip.stop();
		clip.close();
		resetAudioStream();
		currentFrame = 0L;
		clip.setMicrosecondPosition(0L);
		play();
	}
	
	public void stop() {
		currentFrame = 0L;
		clip.stop();
		clip.close();
	}
	
	public void jump(long pos) {
		if(pos > 0 && pos < clip.getMicrosecondLength()) {
			clip.stop();
			clip.close();
			resetAudioStream();
			currentFrame = pos;
			clip.setMicrosecondPosition(pos);
			play();
		}
	}
	
	public void resetAudioStream() {
		try {
			ais = AudioSystem.getAudioInputStream(this.getClass().getResource(filename));
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
