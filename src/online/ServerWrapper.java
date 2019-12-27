package online;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerWrapper extends Logger{
	
	protected int connections;
	
	protected CustomParser cp;
	
	public void setParser(CustomParser c){
		cp = c;
	}

	public void parse(SocketChannel client, String input) {
		switch(input) {
			case "closeConnection":
				try {
					write(client, "Connect Soon!");
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				if(cp != null){
					cp.parse(client, input);
				}else{
					print(input);
					write(client, "ping");
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
