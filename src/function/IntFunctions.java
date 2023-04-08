package function;

public class IntFunctions {
	
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
	
}
