package handler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

import backends.objs.AppPage;
import backends.objs.EntityBase;
import backends.objs.Overlay;
import engine.GameWindow;

public class TransitionHandler implements Serializable{
	
	private static final long serialVersionUID = 2303151858356010675L;

	GameWindow w;
	
	//Generic Variables
	private int x = 0, y = 0;
	private float transparency = 0f;
	private boolean flipped = false;
	
	//TransitionHandler Variables
	private int speed = 3;
	public boolean transitioning = false;
	private AppPage oldPage, newPage;
	private Overlay oldOverlay, newOverlay;
	private int transition = -1;
	private int appTrans = -1;
	private String audioFile = "";
	
	public TransitionHandler(GameWindow w) {
		this.w = w;
	}
	
	public void setOnlyTrans(int a) {
		if(appTrans == -1) {
			appTrans = a;
		}
	}
	
	public void setAudio(String s) {
		audioFile = s;
	}
	
	public void setTransition(AppPage oldPage, AppPage newPage) {
		if(!transitioning) {
			transitioning = true;
			this.oldPage = oldPage;
			oldPage.onChange();
			oldOverlay = w.getHandlers().getPageHandler().getLoadedOverlay(oldPage.getID());
			this.newPage = newPage;
			newPage.init();
			newOverlay = w.getHandlers().getPageHandler().getLoadedOverlay(newPage.getID());
			if(!audioFile.equals("")) {
				w.getHandlers().getAudioHandler().playSound(audioFile);
			}
			w.getHandlers().getPageHandler().addPage(newPage);
		}
	}
	
	public void setTransNoInit(AppPage oldPage, AppPage newPage) {
		if(!transitioning) {
			transitioning = true;
			this.oldPage = oldPage;
			oldPage.onChange();
			oldOverlay = w.getHandlers().getPageHandler().getLoadedOverlay(oldPage.getID());
			this.newPage = newPage;
			newOverlay = w.getHandlers().getPageHandler().getLoadedOverlay(newPage.getID());
			if(!audioFile.equals("")) {
				w.getHandlers().getAudioHandler().playSound(audioFile);
			}
			w.getHandlers().getPageHandler().addPage(newPage);
		}
	}
	
	public void setTransition(AppPage oldPage, AppPage newPage, int t) {
		if(!transitioning) {
			this.transition = t;
			setTransition(oldPage, newPage);
		}
	}
	
	public ImageIcon getTransition() {
		if(transition == -1 && appTrans == -1) {
			transition = (int) Math.round(Math.random() * 1);
		}else if(transition == -1){
			transition = appTrans;
		}
		ImageIcon ret = null;
		
		switch(transition) {
			case 0:
				ret = topDown();
				break;
			case 1:
				ret = leftRight();
				break;
			case 2:
				ret = fadeInOut();
				break;
		}
		return ret;
	}
	
	public ImageIcon topDown() {
		BufferedImage temp = new BufferedImage(w.getFrame().getWidth(), w.getFrame().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		
		g.translate(0, -w.getFrame().getHeight()+y);
		newPage.paint(g);
		for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
			if(e.getID().equals(newPage.getID())) {
				e.paint(g);
			}
		}
		if(newOverlay != null) {
			newOverlay.paint(g);
		}
		g.translate(0, w.getFrame().getHeight());
		oldPage.paint(g);
		for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
			if(e.getID().equals(oldPage.getID())) {
				e.paint(g);
			}
		}
		if(oldOverlay != null) {
			oldOverlay.paint(g);
		}
		g.dispose();
		
		if(y >= w.getFrame().getHeight()) {
			w.getHandlers().getPageHandler().setCurrentPage(newPage.getID(), false);
			y = 0;
			transitioning = false;
			transition = -1;
		}else{
			y+=speed;
		}
		return w.getHandlers().getImageHandler().toImage(temp);
	}
	
	public ImageIcon leftRight() {
		BufferedImage temp = new BufferedImage(w.getFrame().getWidth(), w.getFrame().getWidth(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		
		g.translate(-w.getFrame().getWidth()+x, 0);
		newPage.paint(g);
		for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
			if(e.getID().equals(newPage.getID())) {
				e.paint(g);
			}
		}
		if(newOverlay != null) {
			newOverlay.paint(g);
		}
		g.translate(w.getFrame().getWidth(), 0);
		oldPage.paint(g);
		for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
			if(e.getID().equals(oldPage.getID())) {
				e.paint(g);
			}
		}
		if(oldOverlay != null) {
			oldOverlay.paint(g);
		}
		g.dispose();
		
		if(x >= w.getFrame().getWidth()) {
			w.getHandlers().getPageHandler().setCurrentPage(newPage.getID(), false);
			x = 0;
			transitioning = false;
			transition = -1;
		}else{
			x+=speed;
		}
		return w.getHandlers().getImageHandler().toImage(temp);
	}
	
	public ImageIcon fadeInOut() {
		BufferedImage temp = new BufferedImage(w.getFrame().getWidth(), w.getFrame().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		
		if(!flipped) {
			oldPage.paint(g);
			for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
				if(e.getID().equals(oldPage.getID())) {
					e.paint(g);
				}
			}
			if(oldOverlay != null) {
				oldOverlay.paint(g);
			}
		}else {
			newPage.paint(g);
			for(EntityBase e : w.getHandlers().getEntityHandler().getEntities()) {
				if(e.getID().equals(newPage.getID())) {
					e.paint(g);
				}
			}
			if(newOverlay != null) {
				newOverlay.paint(g);
			}
		}
		
		g.setColor(new Color(0f, 0f, 0f, transparency));
		g.fillRect(0, 0, w.getFrame().getWidth(), w.getFrame().getHeight());
		
		g.dispose();
		
		transparency += !flipped ? 0.02 : -0.02;
		if(transparency >= 1f) {
			flipped = true;
			transparency = 1f;
		}else if(flipped == true && transparency <= 0f) {
			w.getHandlers().getPageHandler().setCurrentPage(newPage.getID(), false);
			transparency = 0f;
			flipped = false;
			transitioning = false;
			transition = -1;
		}
		
		return w.getHandlers().getImageHandler().toImage(temp);
	}
}
