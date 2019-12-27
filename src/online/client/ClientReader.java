package online.client;

import java.nio.channels.SocketChannel;

public class ClientReader implements Runnable{
	
	Client c;
	
	SocketChannel channel;	
	
	transient Thread t;
	
	public ClientReader(Client c){
		this.c = c;
	}
	
	public ClientReader setChannel(SocketChannel sc){
		channel = sc;
		return this;
	}
	
	public ClientReader start(){
		t = new Thread(this);
		t.start();
		return this;
	}

	@Override
	public void run() {
		while(channel.isConnected()){
			c.parse(channel, c.read(channel));
		}
	}

}
