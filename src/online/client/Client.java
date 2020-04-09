package online.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client{

	private SocketChannel client;
	private ByteBuffer buffer;
	
	public Client(String IP, int PORT){
		try {
			client = SocketChannel.open(new InetSocketAddress(IP, PORT));
			buffer = ByteBuffer.allocate(256);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String msg){
		buffer = ByteBuffer.wrap(msg.getBytes());
		
		try {
			write();
			client.write(buffer);
			buffer.clear();
			client.read(buffer);
			read();
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityClient
	 */
	public void write(){
		return;
	}
	
	/*
	 * @author Xavier Bennett
	 * @desc A "dummy" method. Used more in EntityClient
	 */
	public void read(){
		return;
	}

}
