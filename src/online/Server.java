package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private ServerSocket server;
	public boolean ACCEPTING;
	ArrayList<ClientHandler> CH;
	
	public void start(int port) throws IOException {
		server = new ServerSocket(port);
		ACCEPTING = true;
		CH = new ArrayList<>();
		
		while(ACCEPTING) {
			ClientHandler temp = new ClientHandler(server.accept());
			temp.start();
			CH.add(temp);
		}
	}
	
	public void stop() throws IOException {
		ACCEPTING = false;
		server.close();
	}
	
	private static class ClientHandler extends Thread{
		private Socket client;
		private PrintWriter out;
		private BufferedReader in;
		
		public ClientHandler(Socket s) {
			client = s;
		}
		
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream());
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				String input;
				while((input = in.readLine()) != null) {
					if(input.equals("closeServer")) {
						//Closing Server
						break;
					}
					//Logic Here
				}
				
				in.close();
				out.close();
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

}
