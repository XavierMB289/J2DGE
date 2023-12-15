package function;

import java.util.Arrays;

public class ArrayFunctions {
	
	public static boolean arrayContains(Object[] oArr, Object o) {
		return Arrays.asList(oArr).contains(o);
	}
	
	public static Object[] combineArrays(Object[] a, Object[] b) {
		int len = a.length + b.length;
		Object[] ret = new Object[len];
		System.arraycopy(a, 0, ret, 0, a.length);
		System.arraycopy(b, 0, ret, a.length, b.length);
		return ret;
	}
	
	public static Object[] remove(Object[] a, int index) {
		Object[] ret = new Object[a.length-1];
		System.arraycopy(a, 0, ret, 0, index);
		System.arraycopy(a, index+1, ret, index, a.length-index-1);
		return ret;
	}
	
}
