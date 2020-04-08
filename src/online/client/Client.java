package online.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import online.Logger;

public class Client extends Logger{

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
	
	public String sendMessage(String msg){
		buffer = ByteBuffer.wrap(msg.getBytes());
		String ret = "";
		
		try {
			client.write(buffer);
			buffer.clear();
			client.read(buffer);
			ret = new String(buffer.array()).trim();
			print("C: "+ret);
			buffer.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

}
