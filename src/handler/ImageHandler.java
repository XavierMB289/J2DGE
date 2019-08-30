package handler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import backends.ImageItem;
import engine.Window;

public class ImageHandler implements Serializable{
	
	private static final long serialVersionUID = -317953162923412609L;
	
	Window w;
	
	public ImageHandler(Window w) {
		this.w = w;
	}

	/**
	 * @author Xavier Bennett
	 * 
	 * @param name Name of Image File
	 * 
	 * @return returns the image from the image name
	 * 
	 * Gets an image from inside the JAR file.
	 */
	public ImageIcon getImage(String name) {
		return new ImageIcon(getClass().getResource(name));
	}
	
	public ImageIcon toImage(BufferedImage bimg) {
		return new ImageIcon(bimg);
	}
	
	public BufferedImage toBuffImage(ImageIcon icon) {
		if(icon.getImage() instanceof BufferedImage) {
			return (BufferedImage) icon.getImage();
		}
		BufferedImage ret = new BufferedImage(icon.getIconWidth(), icon.getIconWidth(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = ret.createGraphics();
		g.drawImage(icon.getImage(), 0, 0, null);
		g.dispose();
		return ret;
	}
	
	/**
	 * @author Xavier Bennett
	 * 
	 * @param filename Filename of directory with images in it
	 * @return returns an array of images
	 */
	public ArrayList<ImageItem> getAllImages(String filename) {
		File folder = new File(getClass().getResource(filename).getFile());
		if(folder.exists()) {
			File[] files = folder.listFiles();
			ArrayList<ImageItem> ret = new ArrayList<ImageItem>();
			for(File f: files) {
				if(f.isFile()) {
					ImageIcon temp = getImage(filename+f.getName());
					if(temp != null) {
						ret.add(new ImageItem(temp, f.getName().split("\\.")[0]));
					}
				}
			}
			return ret;
		}
		return null;
	}
	
	public ImageIcon resizeImage(Window w, ImageIcon img, double scale) {
		return new ImageIcon(img.getImage().getScaledInstance((int)(img.getIconWidth()*scale), (int)(img.getIconHeight()*scale), Image.SCALE_SMOOTH));
	}
	
	public ImageIcon getSprite(Window w, Image img, int x, int y, int size) {
		BufferedImage i = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = i.getGraphics();
		g.drawImage(img, -x, -y, null);
		g.dispose();
		return new ImageIcon(i);
	}
	
	public ImageIcon[] getSprites(Window w, ImageIcon img, int size) {
		int sheetW = img.getIconWidth()/size, sheetH = img.getIconHeight()/size;
		ImageIcon[] ret = new ImageIcon[sheetW*sheetH];
		for(int y = 0; y < sheetH; y++) {
			for(int x = 0; x < sheetW; x++) {
				ret[(y*sheetH)+x] = getSprite(w, img.getImage(), x*size, y*size, size);
			}
		}
		return ret;
	}
	
	public ImageIcon colorize(ImageIcon icon, Color base) {
		BufferedImage image = toBuffImage(icon);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				if( (rgb>>24) != 0x00 ) {
					Color blended = w.functions.blend(new Color(image.getRGB(x, y), true), base);
					image.setRGB(x, y, blended.getRGB());
				}
			}
		}
		return toImage(image);
	}
	
}
