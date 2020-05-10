package online.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import backends.objs.EntityBase;
import engine.GameWindow;
import online.EntityWrapper;

public class EntityClient implements Runnable{
	
	GameWindow w;
	
	Thread t;
	
	boolean running = true;

	private SocketChannel client;
	
	ArrayList<EntityWrapper> changes;
	
	public EntityClient(GameWindow w, String IP, int PORT){
		try {
			this.w = w;
			client = SocketChannel.open(new InetSocketAddress(IP, PORT));
			changes = new ArrayList<>();
			w.CLIENT_ENABLED = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start(){
		t = new Thread(this);
		t.start();
	}
	
	public void stop(){
		running = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				w.getHandlers().getEntityHandler().addEntity(ew.getEnt());
			}else if(ew.getChange().equals("remove")){
				w.getHandlers().getEntityHandler().removeEntity(ew.getEnt());
			}else if(ew.getChange().equals("change")){
				w.getHandlers().getEntityHandler().changeEntity(ew.getEnt());
			}
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void addChange(EntityBase e, String str){
		changes.add(new EntityWrapper(e, str));
	}

	@Override
	public void run() {
		while(running){
			if(client.isConnected()){
				write();
				read();
			}
		}
	}

}
