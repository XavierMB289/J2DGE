package handler;

import engine.GameWindow;
import handler.event.EventHandler;

public class HandlerCollection {

	private EventHandler EventH = null;
	private AudioHandler AudioH = null;
	private ImageHandler ImageH = null;
	private TransitionHandler TransH = null;
	private FileHandler FileH = null;
	private EntityHandler EntityH = null;
	private PageHandler PageH = null;
	
	public void preInitPages(){
		PageH = new PageHandler();
	}
	
	public void start(GameWindow w){
		EventH = new EventHandler();
		AudioH = new AudioHandler();
		ImageH = new ImageHandler(w);
		TransH = new TransitionHandler(w);
		FileH = new FileHandler(w);
		EntityH = new EntityHandler(w);
	}
	
	public EventHandler getEventHandler() {
		return EventH;
	}
	
	public AudioHandler getAudioHandler() {
		return AudioH;
	}
	
	public ImageHandler getImageHandler() {
		return ImageH;
	}
	
	public TransitionHandler getTransHandler() {
		return TransH;
	}
	
	public FileHandler getFileHandler() {
		return FileH;
	}
	
	public EntityHandler getEntityHandler() {
		return EntityH;
	}
	
	public PageHandler getPageHandler() {
		return PageH;
	}
}
