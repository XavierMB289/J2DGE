package online.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class GameHTTPRequest {
	
	/**
	 * Sends an HTTPS Request.
	 * @param website The url beginning with HTTPS://...
	 * @param jsonData The data parsed into JSON
	 * @return The JSON return data
	 */
	public static String SendRequest(String website, String jsonData) {
		try {
			URL url = new URL(website);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			try(OutputStream os = con.getOutputStream()){
				byte[] input = jsonData.getBytes("utf-8");
				os.write(input);
			}
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String line = null;
				while((line = br.readLine()) != null) {
					response.append(line.trim());
				}
				return response.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String GetData(String website) {
		try {
			URL url = new URL(website);
			Scanner scan = new Scanner(url.openStream());
			StringBuffer buff = new StringBuffer();
			while(scan.hasNextLine()) {
				buff.append(scan.nextLine().trim());
			}
			scan.close();
			return buff.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
