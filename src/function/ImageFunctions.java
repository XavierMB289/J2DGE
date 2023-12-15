package function;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageFunctions {
	
	/**
	 * Converts the Image to a BufferedImage and uses .getSubImage()
	 * @param spritesheet Image containing sprite (a spritesheet)
	 * @param x xCoord of Sprite location (multiplies by width to locate Sprite)
	 * @param y yCoord of Sprite location (multiplies by height to locate Sprite)
	 * @param width width of Sprite
	 * @param height height of Sprite
	 * @return Sprite Image
	 */
	public static Image GetSprite(Image spritesheet, int x, int y, int width, int height) {
		return ((BufferedImage) spritesheet).getSubimage(x*width,y*height,width,height);
	}
	
	/**
	 * Calls GetSprite in order to locate Sprite
	 * @param spritesheet Image containing sprite (a spritesheet)
	 * @param x xCoord of Sprite location (multiplies by size to locate Sprite)
	 * @param y yCoord of Sprite location (multiplies by size to locate Sprite)
	 * @param size size of Sprite
	 * @return Sprite Image
	 */
	public static Image GetSquareSprite(Image spritesheet, int x, int y, int size) {
		return GetSprite(spritesheet, x, y, size, size);
	}
	
}
