package online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadedServer {
	
	ServerData sd = null;
	
	ServerSocket server = null;
	Socket client = null;
	
	public ThreadedServer(ServerData sd) {
		this.sd = sd;
	}
	
	public void start(int PORT) {
		try {
			server = new ServerSocket(PORT);
			while(true) {
				client = server.accept();
				new ThreadedServerHandler(sd, client).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
