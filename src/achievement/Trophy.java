package achievement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import engine.GameWindow;

public class Trophy extends Notify implements Serializable{
	
	private static final long serialVersionUID = 6902391967908717996L;

	GameWindow w;
	
	Rectangle rect;
	ImageIcon icon;
	double scale = 0.1;
	int timer;
	boolean isMini = false;
	
	TrophyCallback callback;
	
	Map<String, String> added;
	String recent = "";
	
	public Trophy(GameWindow w) {
		super(w);
		this.w = w;
		icon = w.getHandlers().getImageHandler().resizeImage(w.getHandlers().getImageHandler().getImage("achievement/trophy.png"), 2);
		rect = new Rectangle(10, 10, (int)(w.getFrame().getW12()*3), (int)w.getFrame().getH12());
		added = new HashMap<String, String>();
		setIcon(icon);
	}
	
	public void resetImg(ImageIcon i){
		icon = i;
	}
	
	/**
	 * @author Xavier Bennett
	 * @param TITLE Title of achievement.
	 * @param DESC Description of achievement earned.
	 */
	public void addTrophy(String TITLE, String DESC) {
		if(!added.containsKey(TITLE) && !added.containsValue(DESC)) {
			added.put(TITLE, DESC);
			recent = TITLE;
			windowsNotify(TITLE, DESC, "Created from: "+w.getFrame().getTitle());
			timer = 150;
		}
	}
	
	/**
	 * @author Xavier Bennett
	 * @param TITLE Title of achievement.
	 * @param DESC Description of achievement.
	 * @param callback A callback method for the achievement.
	 */
	public void addTrophy(String TITLE, String DESC, TrophyCallback callback) {
		if(!added.containsKey(TITLE) && !added.containsValue(DESC)) {
			this.callback = callback;
			addTrophy(TITLE, DESC);
		}
	}
	
	public void paint(Graphics2D g) { 
		if(!recent.equals("")) {
			BufferedImage buff = new BufferedImage((int)Math.floor(rect.width*scale), rect.height, BufferedImage.TYPE_INT_ARGB);
			Rectangle tempRect = new Rectangle(0, 0, rect.width, rect.height);
			Graphics2D bg = buff.createGraphics();
			
			bg.setColor(new Color(0f, 0f, 0f, 0.5f));
			bg.fillRect(0, 0, buff.getWidth(), buff.getHeight());
			bg.drawImage(icon.getImage(), 10, 0, null);
			bg.setColor(Color.white);
			w.getFunctions().drawCenteredString(bg, recent+": "+added.get(recent), tempRect, 0, 0);
			
			bg.dispose();
			g.drawImage(buff, rect.x, rect.y, null);
		}
	}
	
	public void update() {
		if(!recent.equals("") && !isMini) {
			if(scale + 0.01 >= 1d) {
				isMini = true;
			}else {
				scale += 0.01;
			}
		}
		if(isMini) {
			if(--timer <= 0) {
				scale = 0.1;
				if(callback != null) {
					callback.call();
					callback = null;
				}
				recent = "";
				isMini = false;
			}
		}
	}

}
