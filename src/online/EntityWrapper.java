package online;

import java.io.Serializable;

import backends.objs.EntityBase;

public class EntityWrapper implements Serializable{
	
	private static final long serialVersionUID = -6774441827094028752L;

	private EntityBase e;
	
	private String change;
	
	public static final String ADD = "add";
	public static final String REMOVE = "remove";
	public static final String CHANGE = "change";
	
	public EntityWrapper(EntityBase e, String change){
		this.e = e;
		this.change = change;
	}
	
	public String getChange(){
		return change;
	}
	
	public EntityBase getEnt(){
		return e;
	}
	
}
