package interfaces;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Functions {
	
	public default String ArrayContains(String[] arr, String checking) {
		if(arr.equals(null) || checking.equals(null)) {
			return null;
		}
		for(String s : arr) {
			if(s.equals(checking) || s.contains(checking)) {
				return s;
			}
		}
		return null;
	}
	
	public default String md5(String input) {
		// Static getInstance method is called with hashing MD5 
        MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

        // digest() method is called to calculate message digest 
        //  of an input digest() return array of byte 
        byte[] messageDigest = md.digest(input.getBytes()); 

        // Convert byte array into signum representation 
        BigInteger no = new BigInteger(1, messageDigest); 

        // Convert message digest into hex value 
        String hashtext = no.toString(16); 
        while (hashtext.length() < 32) { 
            hashtext = "0" + hashtext; 
        } 
        return hashtext; 
	}
	
}
