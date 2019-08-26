package interfacing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

public class GetPost {
	
	public InputStream post(String url, Map<String, String> values) {
		InputStream ret = null;
		try {
			URLConnection urlConnection = new URL(url).openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			urlConnection.connect();
			OutputStream outputStream = urlConnection.getOutputStream();
			for(Entry<String, String> entry : values.entrySet()) {
				outputStream.write(("{\""+entry.getKey()+"\": \"" + entry.getValue() + "\"}").getBytes("UTF-8"));
			}
			outputStream.flush();
			ret = urlConnection.getInputStream();
		} catch (IOException e) {
			System.err.println("The URL ("+url+") couldn't be reached in GetPost.Post");
		}
		return ret;
	}

}
