package online;

import java.util.ArrayList;

public class Wrapper{
	
	protected ArrayList<String> inputs;
	protected Thread t;
	protected Logger logger;
	protected String nextMessage = "";

	public Wrapper() {
		inputs = new ArrayList<>();
		logger = new Logger();
	}
	
	public String getLastLog() {
		String log = logger.getRecentLog();
		if(inputs.size() > 0) {
			String input = inputs.get(inputs.size()-1);
			return log.equals(input) ? log : input;
		}
		return log;
	}
	
	public ArrayList<String> copyInputs(){
		return new ArrayList<>(inputs);
	}
	
	public void setMessage(String s) {
		nextMessage = s;
	}
	
	public String getMessage() {
		return nextMessage;
	}

}
