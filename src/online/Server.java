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
	private ArrayList<ClientHandler> CH;
	public boolean ACCEPTING;
	public ArrayList<String> inputs;
	public int PORT = 0;
	
	public void start() throws IOException {
		server = new ServerSocket(0);
		PORT = server.getLocalPort();
		CH = new ArrayList<>();
		ACCEPTING = true;
		inputs = new ArrayList<>();
		
		while(ACCEPTING) {
			ClientHandler temp = new ClientHandler(this, server.accept());
			temp.start();
			CH.add(temp);
		}
	}
	
	public int getInputsSize() {
		return inputs == null ? 0 : inputs.size();
	}
	
	public void stop() throws IOException {
		ACCEPTING = false;
		server.close();
	}
	
	private static class ClientHandler extends Thread{
		private Server server;
		private Socket client;
		private PrintWriter out;
		private BufferedReader in;
		
		public ClientHandler(Server ser, Socket s) {
			server = ser;
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
					server.inputs.add(input);
					//Then returns the recieved Data
					out.println("Recieved input ("+input+")");
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
