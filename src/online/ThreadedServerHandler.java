package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadedServerHandler extends Thread{
	
	private ServerData sd = null;
	
	private Socket client = null;
	
	public ThreadedServerHandler(ServerData sd, Socket client) {
		this.sd = sd;
		this.client = client;
	}
	
	private void processClient(BufferedReader in, PrintWriter out) {
		String line;
		boolean done = false;
		
		try {
			while(!done) {
				if((line = in.readLine()) == null) {
					done = true;
				}else {
					if(line.trim().equals("bye")) {
						done = true;
					}else {
						sd.doRequest(line, out);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			
			processClient(in, out);
			
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
