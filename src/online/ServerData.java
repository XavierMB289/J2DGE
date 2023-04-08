package online;

import java.io.PrintWriter;

public abstract class ServerData {
	public abstract void doRequest(String line, PrintWriter out);
}
