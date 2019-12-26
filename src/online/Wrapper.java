package online;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Wrapper extends Logger{
	
	private static final long serialVersionUID = -7537729586644209586L;
	
	protected SocketChannel client;
	
	protected int connections;
	
	protected CustomParser cp;
	
	protected void setClient(SocketChannel sc) {
		client = sc;
	}
	
	public void setParser(CustomParser c){
		cp = c;
	}

	public void parse(String input) {
		switch(input) {
			case "closeConnection":
				try {
					write("Connect Soon!");
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				if(cp != null){
					cp.parse(client, input);
				}else{
					write("ping");
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
	
	private void write(String msg){
		ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
		try {
			client.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buffer.clear();
	}

}
