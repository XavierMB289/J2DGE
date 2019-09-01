package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Wrapper implements Runnable{
	
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	
	public Client() {
		super();
	}

	public Client start(String ip, int port) throws UnknownHostException, IOException {
		client = new Socket(ip, port);
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		t = new Thread(this);
		t.start();
		return this;
	}
	
	@Override
	public void run() {
		String input;
		try {
			while((input = in.readLine()) != null) {
				if(input.equals("closeServer")) {
					//Closing Server
					break;
				}
				if(!nextMessage.equals("")) {
					nextMessage = "";
					sendMessage(nextMessage);
				}
				//Logic Here
				inputs.add(input);
				logger.print(input);
			}
			stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		return in.readLine();
	}
	
	public void stop() throws IOException {
		in.close();
		out.close();
		client.close();
	}

}
