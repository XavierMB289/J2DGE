package function;

import java.util.Arrays;

public class ArrayFunctions {
	
	public boolean arrayContains(Object[] oArr, Object o) {
		return Arrays.asList(oArr).contains(o);
	}
	
	public Object[] combineArrays(Object[] a, Object[] b) {
		int len = a.length + b.length;
		Object[] ret = new Object[len];
		System.arraycopy(a, 0, ret, 0, a.length);
		System.arraycopy(b, 0, ret, a.length, b.length);
		return ret;
	}
	
}
