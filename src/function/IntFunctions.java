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
	public static int Map(double num, double start1, double stop1, double start2, double stop2, boolean withinBounds) {
		double newval = (num - start1) / (stop1 - start1) * (stop2 - start2) + start2;
		if(!withinBounds) {
			return (int)newval;
		}
		if(start2 < stop2) {
			return (int)Math.max(Math.min(newval, stop2), start2);
		}else {
			return (int)Math.max(Math.min(newval, start2), stop2);
		}
	}
	
	/**
	 * Fits the given number into a grid of squares
	 * @param num the number to change
	 * @param gridSize the size of a grid square
	 * @return the number formatted to the "floor'd" gridsquare
	 */
	public static int GridMap(double num, int gridSize) {
		return (int) (num-(num%gridSize));
	}
	
	/**
	 * A WAAAYYY Faster Math.Floor
	 * @param num The number to Floor
	 * @return the Floor'd Number
	 */
	public static int Fast_Floor(double num) {
		int ret = (int)num;
		return num < ret ? ret - 1 : ret;
	}
	
}
