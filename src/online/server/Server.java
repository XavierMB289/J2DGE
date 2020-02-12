package online.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import online.OnlineMethods;

public class Server extends ServerWrapper implements Runnable{
	
	transient Thread t = null;
	Selector sel = null;
	ServerSocketChannel socket = null;
	InetSocketAddress address = null;
	
	OnlineMethods OM;
	
	public String ip;
	public int port;
	
	boolean running;
	
	private String message = "0";
	
	public Server setMethods(OnlineMethods o) {
		OM = o;
		return this;
	}
	
	public Server start() {
		try {
			return this.start(InetAddress.getLocalHost().getHostAddress(), 55555);
		} catch (UnknownHostException e) {
			System.err.println("UHE in Server.start()");
			return this;
		}
	}
	
	public Server start(String ip, int port) {
		running = true;
		this.ip = ip;
		this.port = port;
		if(OM!=null) OM.start();
		t = new Thread(this);
		t.start();
		return this;
	}

	@Override
	public void run() {
		try {
			sel = Selector.open();
			socket = ServerSocketChannel.open();
			address = new InetSocketAddress(ip, port);
			
			
			socket.bind(address);
			socket.configureBlocking(false);
			
			int ops = socket.validOps();
			SelectionKey key = socket.register(sel, ops, null);
			
			this.port = address.getPort();
			
			while(running) {
				sel.select();
				
				Set<SelectionKey> keys = sel.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				
				
				while(iterator.hasNext()) {
					
					SelectionKey myKey = iterator.next();
					
					if(!myKey.isValid()){
						continue;
					}
					
					if(myKey.isAcceptable()) {
						SocketChannel client = socket.accept();
						
						client.configureBlocking(false);
						
						client.register(sel, SelectionKey.OP_READ);
						
						print("New Client Connected");
						
					}else if(myKey.isReadable()) {
						SocketChannel client = (SocketChannel) myKey.channel();
						String result = read(client);
						parse(client, result);
						client.register(sel, SelectionKey.OP_WRITE);
						
					}else if(myKey.isWritable()){
						SocketChannel client = (SocketChannel) myKey.channel();
						write(client, message);
						if(!message.equals("0")){
							message = "0";
						}
						client.register(sel, SelectionKey.OP_READ);
					}
					iterator.remove();
				}
				if(OM!=null) OM.ping();
			}
			if(OM!=null) OM.stop();
			socket.close();
			sel.close();
			t.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String m){
		message = m;
	}
	
	public void stop() {
		running = false;
	}

}
