package online.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import backends.Entity;
import engine.Window;
import online.EntityWrapper;
import online.Logger;

public class EntityServer extends Logger implements Runnable{
	
	Window w;
	
	private Thread t;
	
	private Selector sel;
	private ServerSocketChannel socket;
	
	ArrayList<EntityWrapper> changes;
	
	private boolean running = true;
	
	public EntityServer(Window w, String IP, int PORT){
		try {
			sel = Selector.open();
			socket = ServerSocketChannel.open();
			socket.bind(new InetSocketAddress(IP, PORT));
			socket.configureBlocking(false);
			socket.register(sel, SelectionKey.OP_ACCEPT);
			
			changes = new ArrayList<>();
			
			w.SERVER_ENABLED = true;
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

	@Override
	public void run() {
		while(running){
			try {
				sel.select();
				Set<SelectionKey> keys = sel.selectedKeys();
				Iterator<SelectionKey> iter = keys.iterator();
				while(iter.hasNext()){
					
					SelectionKey key = iter.next();
					
					if(key.isAcceptable()){
						register();
					}
					
					if(key.isReadable()){
						answer(key);
					}
					
					iter.remove();
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void answer(SelectionKey key){
		SocketChannel client = (SocketChannel) key.channel();
		read(client);
		write(client);
	}
	
	public void write(SocketChannel client){
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
	
	public void read(SocketChannel client){
		
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
	
	public void register(){
		SocketChannel client;
		try {
			client = socket.accept();
			client.configureBlocking(false);
			client.register(sel, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addChange(Entity e, String str){
		changes.add(new EntityWrapper(e, str));
	}

}
