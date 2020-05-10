package coffeeDev;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import backends.objs.AppPage;
import engine.GameWindow;

public class Credit extends AppPage{

	private static final long serialVersionUID = 89647535374755949L;
	
	ImageIcon icon;
	
	int rotate = 0;

	public Credit(GameWindow w) {
		super(w);
	}

	@Override
	public void init() {
		icon = w.getHandlers().getImageHandler().resizeImage(w.getHandlers().getImageHandler().getImage("coffeeDev/CDicon.png"), 5);
		w.loadAssets();
	}
	
	@Override
	public void onChange() {
		w.getHandlers().getEntityHandler().resetEntities();
	}

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, w.getWIDTH(), w.getHEIGHT());
		w.getFunctions().push(g);
		g.translate(w.getHALF_W(), w.getHALF_H());
		g.rotate(Math.toRadians(rotate));
		g.drawImage(icon.getImage(), 0, 0, null);
		w.getFunctions().pop(g);
	}

	@Override
	public void update(double delta) {
		
		//w.getHandlers().getTransHandler().setTransition(this, w.getHandlers().getPageHandler().getLoadedPage("mainMenu"), 2);
	}

}