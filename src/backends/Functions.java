package backends;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Functions {
	
	AffineTransform pushpop = null;

	public String ArrayContains(String[] arr, String checking) {
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
	
	public String md5(String input) {
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
	
	public void push(Graphics2D g) {
		pushpop = g.getTransform();
	}
	
	public void pop(Graphics2D g) {
		g.setTransform(pushpop);
	}
	
	public double map(double num, double start1, double stop1, double start2, double stop2, boolean withinBounds) {
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
