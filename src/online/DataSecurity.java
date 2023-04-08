package online;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DataSecurity {
	
	public String md5(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e) {
			System.err.println("ArrayFunctions");
		}
		byte[] messageDigest = md.digest(input.getBytes());
		BigInteger num = new BigInteger(1, messageDigest);
		String hashtext = num.toString(16);
		while(hashtext.length() < 32) {
			hashtext = "0" + hashtext;
		}
		return hashtext;
	}
	
}
