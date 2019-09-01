package online;

import backends.Functions;

public class Logger extends Functions{
	
	private static final long serialVersionUID = 8312500307616997687L;
	
	private String[] lastFive;

	public Logger() {
		lastFive = new String[5];
	}
	
	public void print(String input) {
		if(arrayContains(lastFive, input) != null) {
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
