package handler.event;

public abstract class TimedEvent extends BaseEvent implements Runnable {
	
	transient Thread t;
	private int time;
	private boolean unlocked = false;
	
	public TimedEvent(String id, int time){
		super(id);
		this.time = time;
	}
	
	@Override
	public void init(){
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void update(){
		if(!unlocked){
			return;
		}
	}
	
	@Override
	public void run(){
		try {
			Thread.sleep(time);
			unlocked = true;
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
