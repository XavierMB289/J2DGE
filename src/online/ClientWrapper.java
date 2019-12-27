package online;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientWrapper extends Logger{
	
	private static final long serialVersionUID = -7537729586644209586L;
	
	protected int connections;
	
	protected CustomParser cp;
	
	public void setParser(CustomParser c){
		cp = c;
	}

	public void parse(SocketChannel client, String input) {
		switch(input) {
			case "closeConnection":
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case "0":
				break;
			default:
				if(cp != null){
					cp.parse(client, input);
				}else{
					print(input);
				}
				break;
		}
	}

	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}
	
	public void write(SocketChannel client, String msg){
		try {
			ByteBuffer buf = ByteBuffer.allocate(256);
			buf.clear();
			buf.put(msg.getBytes());
			buf.flip();
			while(buf.hasRemaining()){
				client.write(buf);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String read(SocketChannel client){
		try {
			ByteBuffer buffer = ByteBuffer.allocate(256);
			client.read(buffer);
			String result = new String(buffer.array()).trim();
			return result;
		} catch (IOException e) {
			return "error";
		}
	}

}
