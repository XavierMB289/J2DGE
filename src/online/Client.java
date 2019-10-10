package online;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client extends Wrapper implements Runnable{
	
	private static final long serialVersionUID = 6799828793921830730L;

	transient Thread t;
	
	public String message;

	OnlineMethods OM;
	
	InetSocketAddress address;
	SocketChannel channel;
	
	public Client addMethods(OnlineMethods o) {
		OM = o;
		return this;
	}
	
	public Client start() {
		return this.start("127.0.0.1", 0);
	}
	
	public Client start(String IP, int PORT) {
		address = new InetSocketAddress(IP, PORT);
		try {
			channel = SocketChannel.open(address);
			if(OM!=null) OM.start();
			t = new Thread(this);
			t.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		setConnections(1);
		if(OM!=null) OM.onConnectionChange(1);
		while(channel.isConnected()) {
			if(message != null && !message.equals("")) {
				ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
				try {
					client.write(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				buffer.clear();
				message = "";
				try {
					t.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(OM!=null) OM.ping();
		}
		try {
			client.close();
			if(OM!=null) OM.stop();
			t.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void stop() {
		try {
			channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
