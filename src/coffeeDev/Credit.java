package coffeeDev;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

import backends.AppPage;
import engine.ImageHandler;
import engine.Window;

public class Credit extends AppPage{
	
	Window window;
	
	Image icon = null;
	
	//Animation variables
	int x, y, topEdge, lineTimer;
	double strNum;
	float transparency = 0f;
	String str, line;
	
	public Credit(Window w) {
		super();
		window = w;
		icon = new ImageHandler().getImage("/coffeeDev/icon.png");
	}
	
	public void init() {
		x = window.WIDTH/2-icon.getWidth(null)/2;
		y = icon.getHeight(null)*-1;
		topEdge = (int) (window.HEIGHT*-0.1);
		transparency = 0f;
		strNum = 0;
		lineTimer = 0;
		str = "CoffeeDev";
		line = "";
	}
	
	public void paint(Graphics2D g) {
		if(transparency < 1f) {
			g.drawImage(icon, x, y, null);
			g.setColor(new Color(1-transparency, 1-transparency, 1-transparency, transparency));
			g.fillRect(0, 0, window.WIDTH, window.HEIGHT);
			g.setColor(new Color(transparency, transparency, transparency, 1f));
			g.setFont(new Font("Courier New", Font.PLAIN, 24));
			window.drawCenteredString(g, str.substring(0, (int)(strNum))+line, window.WINDOW_RECT);
		}
	}
	
	public void update() {
		if(transparency < 1f) {
			if(y < topEdge) {
				y+=2;
			}else {
				if(strNum < str.length()) {
					strNum += 0.1;
				}else {
					transparency = transparency < 1 ? transparency + 0.01f : 1f;
				}
				if(lineTimer >= 20) {
					line = line.equals("|") ? "" : "|";
					lineTimer = 0;
				}else {
					lineTimer++;
				}
			}
		}else {
			window.currentPage = "mainMenu";
		}
		
	}
	
}
