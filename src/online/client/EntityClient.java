package online.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import backends.Entity;
import engine.Window;
import online.EntityWrapper;

public class EntityClient {
	
	Window w;

	private SocketChannel client;
	
	ArrayList<EntityWrapper> changes;
	
	public EntityClient(Window w, String IP, int PORT){
		try {
			this.w = w;
			client = SocketChannel.open(new InetSocketAddress(IP, PORT));
			changes = new ArrayList<>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg){
		write();
		read();
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityClient
	 */
	public void write(){
		if(changes.size() > 0){
			try {
				ObjectOutputStream  oos = new ObjectOutputStream(client.socket().getOutputStream());
				oos.writeObject(changes.get(0));
				oos.close();
				changes.remove(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityClient
	 */
	public void read(){
		try {
			ObjectInputStream ois = new ObjectInputStream(client.socket().getInputStream());
			
			EntityWrapper ew = (EntityWrapper)ois.readObject();
			
			if(ew.getChange().equals("add")){
				w.EntityH.addEntity(ew.getEnt());
			}else if(ew.getChange().equals("remove")){
				w.EntityH.removeEntity(ew.getEnt());
			}else if(ew.getChange().equals("change")){
				w.EntityH.changeEntity(ew.getEnt());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addChange(Entity e, String str){
		changes.add(new EntityWrapper(e, str));
	}

}