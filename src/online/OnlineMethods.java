package online;

public interface OnlineMethods {
	
	public abstract void start();
	
	public abstract void ping();
	
	public abstract void onConnectionChange(int connections);
	
	public abstract void stop();
	
}
