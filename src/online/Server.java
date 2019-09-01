package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	
	private ServerSocket server;
	private ArrayList<ClientHandler> CH;
	private Thread t;
	public boolean ACCEPTING = true;
	public ArrayList<String> inputs;
	ArrayList<String> removeInput;
	Logger logger;
	public int PORT = 0;
	
	public void start() {
		logger = new Logger();
		try {
			server = new ServerSocket(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PORT = server.getLocalPort();
		CH = new ArrayList<>();
		inputs = new ArrayList<>();
		removeInput = new ArrayList<>();
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while(ACCEPTING) {
			try {
				ClientHandler temp = new ClientHandler(this, server.accept());
				temp.start();
				CH.add(temp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void subInput(String in) {
		removeInput.add(in);
	}
	
	public int getInputsSize() {
		return inputs == null ? 0 : inputs.size();
	}
	
	public void stop() throws IOException {
		ACCEPTING = false;
		for(ClientHandler c : CH) {
			c.stop();
		}
		server.close();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class ClientHandler implements Runnable{
		private Server server;
		private Socket client;
		private PrintWriter out;
		private BufferedReader in;
		private Thread t;
		
		public ClientHandler(Server ser, Socket s) {
			server = ser;
			client = s;
			t = new Thread(this);
		}
		
		public void start() {
			t.start();
		}
		
		public void stop() {
			try {
				in.close();
				out.close();
				client.close();
				t.join();
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String input;
				while((input = in.readLine()) != null) {
					if(input.equals("closeServer")) {
						//Closing Server
						break;
					}
					out.println("Recieved input");
					//Logic Here
					server.inputs.add(input);
					server.logger.print(input);
				}
				
				stop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

}
