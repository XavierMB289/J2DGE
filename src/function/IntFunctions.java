package function;

public class IntFunctions {
	
	/**
	 * Maps a number between values to another set of values
	 * @param num Number to map
	 * @param start1 The original starting value
	 * @param stop1 The original ending value
	 * @param start2 The new starting value
	 * @param stop2 The new stopping value
	 * @param withinBounds Forces the map to be within bounds
	 * @return the newly mapped number
	 */
	public static double Map(double num, double start1, double stop1, double start2, double stop2, boolean withinBounds) {
		double newval = (num - start1) / (stop1 - start1) * (stop2 - start2) + start2;
		if(!withinBounds) {
			return newval;
		}
		if(start2 < stop2) {
			return Math.max(Math.min(newval, stop2), start2);
		}else {
			return Math.max(Math.min(newval, start2), stop2);
		}
	}
	
	public static int Fast_Floor(double num) {
		int ret = (int)num;
		return num < ret ? ret - 1 : ret;
	}
	
}
