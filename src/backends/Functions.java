package backends;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Functions implements Serializable{
	
	private static final long serialVersionUID = -3371131927516977319L;
	
	private GraphicsEnvironment ge;
	
	AffineTransform pushpop = null;

	public String arrayContains(String[] arr, String checking) {
		if(arr == null || checking == null) {
			return null;
		}
		for(String s : arr) {
			if(s != null) {
				if(s.equals(checking) || s.contains(checking)) {
					return s;
				}
			}
		}
		return null;
	}
	
	public String[] combineArrays(String[] a, String[] b) {
		int len = a.length+b.length;
		String[] ret = new String[len];
		System.arraycopy(a, 0, ret, 0, a.length);
		System.arraycopy(b, 0, ret, a.length, b.length);
		return ret;
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
	
	public void drawCenteredString(Graphics2D g, String text, Rectangle rect, int addX, int addY) {
		Font font = g.getFont();
		
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2 + addX;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + addY;
	    g.drawString(text, x, y);
	}
	
	public void drawCenteredImage(Graphics2D g, Image i, Rectangle rect, int addX, int addY) {
		int x = rect.x + (rect.width - i.getWidth(null)) / 2 + addX;
		int y = rect.y + (rect.height - i.getHeight(null)) / 2 + addY;
		g.drawImage(i, x, y, null);
	}
	
	public Rectangle createCenteredRect(Image i, Rectangle rect, int addX, int addY) {
		int x = rect.x + (rect.width - i.getWidth(null)) / 2 + addX;
		int y = rect.y + (rect.height - i.getHeight(null)) / 2 + addY;
		return new Rectangle(x, y, i.getWidth(null), i.getHeight(null));
	}
	
	public Rectangle createCenteredRect(Graphics2D g, String text, Rectangle rect, int addX, int addY) {
		FontMetrics mets = g.getFontMetrics(g.getFont());
		int x = rect.x + (rect.width - mets.stringWidth(text)) / 2 + addX;
		int y = rect.y + ((rect.height - mets.getHeight()) / 2) + mets.getAscent() + addY;
		return new Rectangle(x, y, mets.stringWidth(text), mets.getHeight());
	}
	
	public void addFont(int fontFormat, String fontNamePath) {
		try {
			URL url = getClass().getClassLoader().getResource(fontNamePath);
			Font f = null;
			if(ge == null) {
				ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			}
			if(url == null) {
				String temp = getClass().getName().replace(".", "/")+".class";
				url = getClass().getClassLoader().getResource(temp);
			}
			if(url.getProtocol().equals("jar")) {
				f = Font.createFont(fontFormat, getClass().getClassLoader().getResourceAsStream(fontNamePath));
			}else {
				f = Font.createFont(fontFormat, new File(url.getFile()));
			}
			ge.registerFont(f);
			System.out.println("The font "+f.getFontName()+" has been added.");
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}
	
	public Color blend(Color c0, Color c1) {
		double totalAlpha = c0.getAlpha() + c1.getAlpha();
		double weight0 = c0.getAlpha() / totalAlpha;
		double weight1 = c1.getAlpha() / totalAlpha;

		double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		double a = Math.max(c0.getAlpha(), c1.getAlpha());

		return new Color((int) r, (int) g, (int) b, (int) a);
	}
	
}
