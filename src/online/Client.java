package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;

	public Client start(String ip, int port) throws UnknownHostException, IOException {
		client = new Socket(ip, port);
		out = new PrintWriter(client.getOutputStream());
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		return this;
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
