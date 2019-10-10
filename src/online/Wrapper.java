package online;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Wrapper extends Logger{
	
	private static final long serialVersionUID = -7537729586644209586L;
	
	protected SocketChannel client;
	
	private int connections;
	
	public void setClient(SocketChannel sc) {
		client = sc;
	}

	public void parse(String input) {
		switch(input) {
			case "closeConnection":
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
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

}
