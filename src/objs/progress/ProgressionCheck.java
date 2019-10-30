package objs.progress;

public class ProgressionCheck {

	private final int max = 100;
	
	private int progress = 0;
	
	public void setProgress(int p) {
		progress = p;
	}
	
	public double getProgress() {
		return progress / max;
	}

}
