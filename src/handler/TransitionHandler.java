package handler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

import backends.AppPage;
import backends.Entity;
import engine.Window;

public class TransitionHandler implements Serializable{
	
	private static final long serialVersionUID = 2303151858356010675L;

	Window w;
	
	//Generic Variables
	private int x = 0, y = 0;
	
	//TransitionHandler Variables
	private int speed = 3;
	public boolean transitioning = false;
	private AppPage oldPage, newPage;
	private int transition = -1;
	private int appTrans = -1;
	private String audioFile = "";
	
	public TransitionHandler(Window w) {
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
			this.newPage = newPage;
			if(!audioFile.equals("")) {
				w.AudioH.playSound(audioFile);
			}
		}
	}
	
	public void setTransition(AppPage oldPage, AppPage newPage, int t) {
		if(!transitioning) {
			transitioning = true;
			this.oldPage = oldPage;
			this.newPage = newPage;
			this.transition = t;
			if(!audioFile.equals("")) {
				w.AudioH.playSound(audioFile);
			}
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
		}
		return ret;
	}
	
	public ImageIcon topDown() {
		BufferedImage temp = new BufferedImage(w.WIDTH, w.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		
		g.translate(0, -w.HEIGHT+y);
		newPage.paint(g);
		for(Entity e : w.EntityH.getEntities()) {
			if(e.getID().equals(newPage.getID())) {
				e.paint(g);
			}
		}
		g.translate(0, w.HEIGHT);
		oldPage.paint(g);
		for(Entity e : w.EntityH.getEntities()) {
			if(e.getID().equals(oldPage.getID())) {
				e.paint(g);
			}
		}
		g.dispose();
		
		if(y >= w.HEIGHT) {
			w.setCurrentPage(newPage.getID());
			y = 0;
			transitioning = false;
			transition = -1;
		}else{
			y+=speed;
		}
		return w.ImageH.toImage(temp);
	}
	
	public ImageIcon leftRight() {
		BufferedImage temp = new BufferedImage(w.WIDTH, w.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = temp.createGraphics();
		
		g.translate(-w.WIDTH+x, 0);
		newPage.paint(g);
		for(Entity e : w.EntityH.getEntities()) {
			if(e.getID().equals(newPage.getID())) {
				e.paint(g);
			}
		}
		g.translate(w.WIDTH, 0);
		oldPage.paint(g);
		for(Entity e : w.EntityH.getEntities()) {
			if(e.getID().equals(oldPage.getID())) {
				e.paint(g);
			}
		}
		g.dispose();
		
		if(x >= w.WIDTH) {
			w.setCurrentPage(newPage.getID());
			x = 0;
			transitioning = false;
			transition = -1;
		}else{
			x+=speed;
		}
		return w.ImageH.toImage(temp);
	}
}
