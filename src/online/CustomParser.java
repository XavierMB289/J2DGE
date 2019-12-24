package online;

import java.nio.channels.SocketChannel;

public abstract class CustomParser {
	
	public abstract void parse(SocketChannel client, String input);
	
}
