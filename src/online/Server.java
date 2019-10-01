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

public class Server extends Wrapper{
	
	private static final long serialVersionUID = -1181155211514559101L;
	
	transient Thread t = null;
	Selector sel = null;
	ServerSocketChannel socket = null;
	InetSocketAddress address = null;
	
	boolean running;
	
	public Server start() {
		return this.start("127.0.0.1", 0);
	}
	
	public Server start(String ip, int port) {
		running = true;
		t = new Thread(new Runnable() {
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
					
					while(running) {
						sel.select();
						
						Set<SelectionKey> keys = sel.selectedKeys();
						Iterator<SelectionKey> iterator = keys.iterator();
						
						while(iterator.hasNext()) {
							
							SelectionKey myKey = iterator.next();
							
							if(myKey.isAcceptable()) {
								SocketChannel client = socket.accept();
								
								client.configureBlocking(false);
								
								client.register(sel, SelectionKey.OP_READ);
								
							}else if(myKey.isReadable()) {
								SocketChannel client = (SocketChannel) myKey.channel();
								ByteBuffer buffer = ByteBuffer.allocate(256);
								client.read(buffer);
								String result = new String(buffer.array()).trim();
								
								setClient(client);
								parse(result);
							}
							iterator.remove();
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
		return this;
	}

}
