package handler;

import java.awt.Graphics2D;
import java.util.ArrayList;

import backend.obj.GameEntity;

public class EntityHandler {
	
	ArrayList<GameEntity> ents;
	ArrayList<GameEntity> remove;
	
	public void preInit() {
		ents = new ArrayList<GameEntity>();
		remove = new ArrayList<GameEntity>();
	}
	
	public void paint(Graphics2D g) {
		for(int i = 0; i < ents.size(); i++) { //Making sure that the Ents are painted in order
			ents.get(i).paint(g);
		}
	}
	
	public void update(double delta) {
		for(int i = 0; i < ents.size(); i++) {
			ents.get(i).update(delta);
			if (ents.get(i).isDeleted()) remove.add(ents.get(i));
		}
		for(GameEntity e : remove) {
			ents.remove(e);
		}
		remove = new ArrayList<>();
	}
	
	public void addEntity(GameEntity e) {
		e.init();
		ents.add(e);
	}
	
	public void addEntity(GameEntity e, int zIndex) {
		ents.add(zIndex, e);
	}
	
	public void addEntities(GameEntity[] e) {
		for(GameEntity ge : e) {
			addEntity(ge);
		}
	}
	
	public void changeIndex(int current, int next) {
		GameEntity ent = ents.get(current);
		ents.remove(current);
		addEntity(ent, next);
	}
	
}
