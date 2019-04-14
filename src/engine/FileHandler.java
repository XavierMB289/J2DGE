package engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FileHandler {
	
	String projectPath = null;
	String githubPath = null;
	
	public FileHandler(String p) {
		projectPath = p;
		File project = new File(projectPath);
		if(project.exists() != true) {
			project.mkdirs();
		}
	}
	
	public FileHandler(String p, String g) {
		projectPath = p;
		githubPath = g;
		File project = new File(projectPath);
		if(project.exists() != true) {
			project.mkdirs();
		}
	}
	
	public void createFile(String filename) {
		if(filename.equals(null) != true) {
			if(filename.equals("")) {
				filename = "unicornFarts";
			}
		}
		
		File project = new File(projectPath+filename+"\\");
		if(project.exists() != true) {
			project.mkdir();
		}
	}
	
	private static boolean isRedirected( Map<String, List<String>> header ) {
		for( String hv : header.get( null )) {
			if(   hv.contains( " 301 " ) || hv.contains( " 302 " )) {
				return true;
			}
		}
		return false;
	}
	
	public void download(String filepath, String fileName) {
		try {
			String link = githubPath + filepath + fileName;
			URL url  = new URL( link );
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			Map< String, List< String >> header = http.getHeaderFields();
			while( isRedirected( header )) {
				link = header.get( "Location" ).get( 0 );
				url = new URL( link );
				http = (HttpURLConnection)url.openConnection();
				header = http.getHeaderFields();
			}
			InputStream input  = http.getInputStream();
			byte[] buffer = new byte[4096];
			int n = -1;
			OutputStream output = new FileOutputStream( new File( projectPath+fileName ));
			while ((n = input.read(buffer)) != -1) {
				output.write( buffer, 0, n );
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
