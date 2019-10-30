package coffeeDev;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.ImageIcon;

import backends.AppPage;
import engine.Window;
import objs.progress.ProgressBar;

public class Credit extends AppPage{

	private static final long serialVersionUID = 89647535374755949L;
	
	public ProgressBar PB;
	
	ImageIcon icon;
	
	String[] str;
	int stringNum;
	int index;
	int indexMult;
	int timer;

	public Credit(Window w) {
		super(w);
	}

	@Override
	public void init() {
		icon = w.ImageH.resizeImage(w, w.ImageH.getImage("coffeeDev/icon.png"), 0.1);
		PB = new ProgressBar(w, icon, new Point((int)w.W12*2, (int)w.H12*11), (int)w.W12*8, 5);
		PB.setColor(Color.LIGHT_GRAY);
		PB.setRelativeSpeed(3);
		PB.addCheck(w.imageCheck);
		PB.addCheck(w.pageCheck);
		w.EntityH.addEntity(PB);
		str = new String[] {"Java", "CoffeeDev"};
		stringNum = 0;
		index = 0;
		indexMult = 1;
		timer = 15;
	}
	
	@Override
	public void onChange() {
		
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, w.WIDTH, w.HEIGHT);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.PLAIN, 48));
		w.functions.drawCenteredString(g, str[stringNum].substring(0, index), w.WINDOW_RECT, 0, 0);
	}

	@Override
	public void update(double delta) {
		if(--timer <= 0) {
			timer = 15;
			index+=indexMult;
			if(index >= str[stringNum].length()) {
				if(stringNum == 0) {
					indexMult=-1;
				}else {
					index = 9;
				}
			}
			if(index <= 0) {
				if(stringNum == 0) {
					stringNum = 1;
				}
				indexMult = 1;
			}
		}
		if(PB.FINISHED) {
			w.TransH.setTransition(this, w.getLoadedPage("mainMenu"), 2);
		}
	}

}