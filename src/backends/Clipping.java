package backends;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Clipping implements LineListener, Serializable{
	
	private static final long serialVersionUID = -1613757221445051139L;
	
	Long currentFrame;
	Clip clip;
	
	public String filename;
	String status = "";
	
	AudioInputStream ais;
	
	/**
	 * @author Xavier Bennett
	 * @param filename filename of an audio file.
	 * Loads an audio file for simple manipulation.
	 */
	public Clipping(String filename) {
		
		this.filename = filename;
		
		try {
			File temp = new File(getClass().getClassLoader().getResource(filename).getFile());
			ais = AudioSystem.getAudioInputStream(temp);
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * @author Xavier Bennett
	 * Plays the selected audio.
	 */
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
	
	/**
	 * @author Xavier Bennett
	 * Pauses the selected audio.
	 */
	public void pause() {
		if(status.equals("play")) {
			currentFrame = clip.getMicrosecondPosition();
			clip.stop();
			status = "pause";
		}
	}
	
	/**
	 * @author Xavier Bennett
	 * Restarts the selected audio.
	 */
	public void restart() {
		clip.stop();
		clip.close();
		resetAudioStream();
		currentFrame = 0L;
		clip.setMicrosecondPosition(0L);
		play();
	}
	
	/**
	 * @author Xavier Bennett
	 * Stops the selected audio.
	 */
	public void stop() {
		currentFrame = 0L;
		clip.stop();
		clip.close();
		try {
			ais.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Xavier Bennett
	 * @param pos A long representing an audio position
	 * Moves to a position in the audio based on the given long location
	 */
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

	@Override
	public void update(LineEvent event) {
		Type type = event.getType();
		if(type == Type.STOP) {
			stop();
			try {
				this.finalize();
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
