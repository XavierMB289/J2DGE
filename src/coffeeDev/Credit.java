package coffeeDev;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import backends.AppPage;
import engine.Window;

public class Credit extends AppPage{
	
	private static final long serialVersionUID = 8848415916613610875L;
	
	ImageIcon icon = null;
	
	//Animation variables
	int x, y, topEdge, lineTimer;
	double strNum, darkness;
	float transparency = 0f;
	String str, line;
	
	public Credit(Window w) {
		super(w);
		icon = w.ImageH.getImage("/coffeeDev/icon.png");
	}
	
	public void init() {
		icon = w.ImageH.resizeImage(w, icon, (w.W12*12)/icon.getIconWidth());
		x = w.WIDTH/2-icon.getIconWidth()/2;
		y = icon.getIconHeight()*-1;
		topEdge = w.HEIGHT/2 - icon.getIconHeight()/2;
		transparency = 0f;
		strNum = 0;
		lineTimer = 0;
		darkness = 60.0;
		str = "CoffeeDev";
		line = "";
	}
	
	public void paint(Graphics2D g) {
		g.drawImage(icon.getImage(), x, y, null);
		if(transparency < 1) {
			g.setColor(new Color(1-transparency, 1-transparency, 1-transparency, transparency));
		}else {
			g.setColor(Color.black);
		}
		g.fillRect(0, 0, w.WIDTH, w.HEIGHT);
		if(transparency < 1) {
			g.setColor(new Color(transparency, transparency, transparency, 1f));
		}else {
			g.setColor(new Color(1f, 1f, 1f, 1f));
		}
		g.setFont(new Font("Courier New", Font.PLAIN, Math.max(((int)w.H12/2), 12)));
		w.functions.drawCenteredString(g, str.substring(0, (int)(strNum))+line, w.WINDOW_RECT, 0, (int)w.H12);
	}
	
	public void update(double delta) {
		if(y < topEdge) {
			y+=2;
		}else {
			if(strNum < str.length()) {
				strNum += 0.1;
			}else {
				transparency = transparency < 1 ? transparency + 0.01f : 1f;
				if(transparency >= 1f) {
					if(darkness <= 0d) {
						w.setCurrentPage("mainMenu");
					}
					darkness-=0.2d;
				}
			}
			if(lineTimer >= 20) {
				line = line.equals("|") ? "" : "|";
				lineTimer = 0;
			}else {
				lineTimer++;
			}
		}
	}
	
}
