package engine;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import backends.ImageItem;

public class ImageHandler {
	
	
	/**
	 * @author Xavier Bennett
	 * 
	 * @param name Name of Image File
	 * 
	 * @return returns the image from the image name
	 * 
	 * Gets an image from inside the JAR file.
	 */
	public Image getImage(String name) {
		return new ImageIcon(getClass().getResource(name)).getImage();
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
					Image temp = getImage(filename+f.getName());
					if(temp != null) {
						ret.add(new ImageItem(temp, f.getName().split("\\.")[0]));
					}
				}
			}
			return ret;
		}
		return null;
	}
	
	/**
	 * @author Xavier Bennett
	 * 
	 * @param img the Image to separate
	 * @param info an array with 4 values [width, height, xoffset, yoffset]
	 */
	public Image[] separateSprites(Image img, int[] info) {
		ArrayList<Image> ret = new ArrayList<>();
		
		if(img.equals(null) || info.equals(null)) {
			return null;
		}
		if(info.length > 4) {
			return null;
		}
		
		int width = info[0];
		int height = info[1];
		int xoff = info[2];
		int yoff = info[3];
		
		for(int y = 0; y < img.getHeight(null); y+=height+yoff) {
			for(int x = 0; x < img.getWidth(null); x+=width+xoff) {
				ret.add((Image)((BufferedImage)img).getSubimage(x, y, width, height));
			}
		}
		
		return (Image[]) ret.toArray();
	}
	
}
