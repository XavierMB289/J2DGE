package online;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

public class ServerWrapper implements Runnable{
	
	private Server server;
	private ArrayList<Client> clients;
	private Thread back;
	public int PORT = 0;
	
	public ServerWrapper init() {
		back = new Thread(this);
		back.start();
		return this;
	}
	
	@Override
	public void run() {
		clients = new ArrayList<>();
		server = new Server();
		try {
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ServerWrapper initialized..");
	}
	
	public int clientSize() {
		return clients == null ? 0 : clients.size();
	}
	
	public void addClient(String ip, int port) {
		Client client;
		try {
			System.out.println("worked");
			client = new Client().start(ip, port);
			System.out.println("worked2");
			client.sendMessage("connectTo:"+InetAddress.getLocalHost()+":"+PORT);
			System.out.println("worked3");
			clients.add(client);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean clientContains(String ip, int port) {
		for(Client c : clients) {
			if(c.compare(ip, port)) {
				return true;
			}
		}
		return false;
	}
	
	//This is the update function for ServerWrapper
	public void filter() {
		if(PORT == 0) {
			if(server != null) {
				PORT = server.PORT;
			}
		}
		if(server.getInputsSize() > 0) {
			for(String input : server.inputs) {
				if(input.contains(":")) {
					String[] args = input.split(":");
					switch(args[0]) {
						case "connectTo":
							if(!clientContains(args[1], Integer.parseInt(args[2]))) {
								addClient(args[1], Integer.parseInt(args[2]));
							}
							break;
					}
				}
			}
		}
	}

}
