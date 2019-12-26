package online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server extends Wrapper implements Runnable{
	
	private static final long serialVersionUID = -1181155211514559101L;
	
	transient Thread t = null;
	Selector sel = null;
	ServerSocketChannel socket = null;
	InetSocketAddress address = null;
	
	OnlineMethods OM;
	
	public String ip;
	public int port;
	
	boolean running;
	private String message;
	
	public Server setMethods(OnlineMethods o) {
		OM = o;
		return this;
	}
	
	public Server start() {
		return this.start("127.0.0.1", 0);
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
				
				int tempNum = 0;
				
				while(iterator.hasNext()) {
					
					tempNum++;
					
					SelectionKey myKey = iterator.next();
					
					if(myKey.isAcceptable()) {
						SocketChannel client = socket.accept();
						
						client.configureBlocking(false);
						
						client.register(sel, SelectionKey.OP_READ);
						
						print("New Client Connected");
						
					}else if(myKey.isReadable()) {
						SocketChannel client = (SocketChannel) myKey.channel();
						ByteBuffer buffer = ByteBuffer.allocate(256);
						client.read(buffer);
						String result = new String(buffer.array()).trim();
						
						setClient(client);
						parse(result);
					}else if(myKey.isWritable() && !message.equals("") && message != null){
						ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
						try {
							client.write(buffer);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						buffer.clear();
					}
					iterator.remove();
				}
				if(OM!=null) OM.ping();
				if(getConnections() != tempNum) {
					setConnections(tempNum);
					if(OM!=null) OM.onConnectionChange(tempNum);
				}
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
	
	public void sendMessage(String s){
		message = s;
	}
	
	public void stop() {
		running = false;
	}

}
