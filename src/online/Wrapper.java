package online;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Wrapper extends Logger{
	
	private static final long serialVersionUID = -7537729586644209586L;
	
	SocketChannel client;
	
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

}
