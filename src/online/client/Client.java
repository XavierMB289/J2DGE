package online.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import online.OnlineMethods;

public class Client extends ClientWrapper implements Runnable{

	transient Thread t;
	
	public String message = "0";

	OnlineMethods OM;
	
	InetSocketAddress address;
	SocketChannel channel;
	
	ClientReader CR;
	
	public Client addMethods(OnlineMethods o) {
		OM = o;
		return this;
	}
	
	public Client start() {
		try {
			return this.start(InetAddress.getLocalHost().getHostAddress(), 55555);
		} catch (UnknownHostException e) {
			System.err.println("UHE in Client.start()");
			return this;
		}
	}
	
	public Client start(String IP, int PORT) {
		address = new InetSocketAddress(IP, PORT);
		try {
			channel = SocketChannel.open(address);
			channel.configureBlocking(false);
			if(OM!=null) OM.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		CR = new ClientReader(this).setChannel(channel).start();
		
		t = new Thread(this);
		t.start();
		return this;
	}

	@Override
	public void run() {
		
		setConnections(1);
		if(OM!=null) OM.onConnectionChange(1);
		while(channel.isConnected()) {
			if(message != null && !message.equals("0")) {
				write(channel, message);
				message = "0";
			}
			if(OM!=null) OM.ping();
		}
		
	}
	
	public void stop() {
		try {
			channel.close();
			if(OM!=null) OM.stop();
			t.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
