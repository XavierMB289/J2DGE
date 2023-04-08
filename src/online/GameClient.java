package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
	
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	public String sendCmd(String cmd) {
		try {
			out.println(cmd);
			String line = in.readLine();
			return line;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void closeLink() {
		try {
			out.println("bye");
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GameClient(String IP, int PORT) {
		try {
			client = new Socket(IP, PORT);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static GameClient connect(String IP, int PORT) {
		return new GameClient(IP, PORT);
	}
	
}
