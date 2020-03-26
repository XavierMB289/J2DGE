package online;

import backends.Functions;

public class Logger{
	
	private String[] lastFive;
	
	private Functions f;

	public Logger() {
		lastFive = new String[5];
		f = new Functions();
	}
	
	public void print(String input) {
		if(f.arrayContains(lastFive, input) == null) {
			lastFive[0] = lastFive[1];
			lastFive[1] = lastFive[2];
			lastFive[2] = lastFive[3];
			lastFive[3] = lastFive[4];
			lastFive[4] = input;
			System.out.println(input);
		}
	}
	
	public String getRecentLog() {
		return lastFive[4] == null ? "" : lastFive[4];
	}

}
