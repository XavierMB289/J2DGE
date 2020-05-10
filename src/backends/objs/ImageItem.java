package backends.objs;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class ImageItem implements Serializable{
	
	private static final long serialVersionUID = -2621555721063050236L;

	public ImageIcon img;
	
	public String ID;
	
	public ImageItem(ImageIcon i, String id) {
		img = i;
		ID = id;
	}
	
}
