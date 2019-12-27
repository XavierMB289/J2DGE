package online;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
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
		try {
			return this.start(InetAddress.getLocalHost().getHostAddress(), 55555);
		} catch (UnknownHostException e) {
			System.err.println("UHE in Client.start()");
			return this;
		}
	}
	
	public Client start(String IP, int PORT) {
		address = new InetSocketAddress(IP, PORT);
		t = new Thread(this);
		t.start();
		return this;
	}

	@Override
	public void run() {
		
		try {
			channel = SocketChannel.open(address);
			channel.configureBlocking(false);
			if(OM!=null) OM.start();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		setConnections(1);
		if(OM!=null) OM.onConnectionChange(1);
		while(channel.isConnected()) {
			if(message != null && !message.equals("")) {
				writeToChannel(message);
				message = "";
				setClient(channel);
				parse(readFromChannel());
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
	
	public void writeToChannel(String msg){
		if(msg != null && !msg.equals("")){
			try {
				ByteBuffer buf = ByteBuffer.allocate(256);
				buf.clear();
				buf.put(msg.getBytes());
				buf.flip();
				while(buf.hasRemaining()){
					channel.write(buf);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String readFromChannel(){
		try {
			ByteBuffer buffer = ByteBuffer.allocate(256);
			channel.read(buffer);
			String result = new String(buffer.array()).trim();
			return result;
		} catch (IOException e) {
			return "error";
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
