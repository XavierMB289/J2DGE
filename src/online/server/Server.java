package online.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import engine.GameWindow;
import online.Logger;

public class Server extends Logger implements Runnable{
	
	GameWindow w;
	
	private Thread t;
	
	private Selector sel;
	private ServerSocketChannel socket;
	protected ByteBuffer buffer;
	
	private boolean running = true;
	
	public Server(GameWindow w, String IP, int PORT){
		try {
			sel = Selector.open();
			socket = ServerSocketChannel.open();
			socket.bind(new InetSocketAddress(IP, PORT));
			socket.configureBlocking(false);
			socket.register(sel, SelectionKey.OP_ACCEPT);
			buffer = ByteBuffer.allocate(256);
			
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
	
	@SuppressWarnings("static-access")
	private void answer(SelectionKey key){
		try {
			SocketChannel client = (SocketChannel) key.channel();
			client.read(buffer);
			read(client);
			if(new String(buffer.array()).trim().equals(w.SERVER_STOP)){
				client.close();
				print("Not accepting client messages");
			}
			write(client);
			buffer.flip();
			client.write(buffer);
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityServer
	 */
	public void write(SocketChannel client){
		return;
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityServer
	 */
	public void read(SocketChannel client){
		return;
	}
	
	public void register(){
		SocketChannel client;
		try {
			client = socket.accept();
			client.configureBlocking(false);
			client.register(sel, SelectionKey.OP_READ);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
