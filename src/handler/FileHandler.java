package handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFileChooser;

import engine.Window;

public class FileHandler implements Serializable{
	
	private static final long serialVersionUID = -5163715433825135015L;
	
	Window w;
	
	public FileHandler(Window w) {
		this.w = w;
	}
	
	public void save(String filepath, Object o) {
		File f = new File(getClass().getClassLoader().getResource(filepath).getFile());
		if(f != null && f.exists()) {
			try {
				FileOutputStream file = new FileOutputStream(f.getAbsolutePath()); 
				ObjectOutputStream out = new ObjectOutputStream(file);
				out.writeObject(o);
				out.close(); 
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
				deleteContents(filepath);
			}
		}
	}
	
	public Object load(String filepath) {
		
		Object ret = null;
		
		File f = new File(getClass().getClassLoader().getResource(filepath).getFile());
		if(f != null && f.exists()) {
			if(f.length() == 0) {
				return null;
			}else {
				FileInputStream file;
				try {
					file = new FileInputStream(f);
					ObjectInputStream in = new ObjectInputStream(file);
					ret = in.readObject();
					in.close(); 
					file.close();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	public void writeToFile(String filepath, String[] lines) {
		try {
			FileWriter fw = new FileWriter(getClass().getClassLoader().getResource(filepath).getFile());
			
			for(String line : lines) {
				fw.write(line + System.lineSeparator());
			}
			
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String> readArrayFromFile(String filepath) {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		try {
			Scanner scan = new Scanner(new File(getClass().getClassLoader().getResource(filepath).getFile()));
			
			while(scan.hasNextLine()) {
				ret.add(scan.nextLine());
			}
			
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public String readStringFromFile(String filepath){
		ArrayList<String> temp = readArrayFromFile(filepath);
		String ret = "";
		for(String s : temp){
			ret += s;
		}
		return ret;
	}
	
	public String[] getFilesFromDir(Class<?> clazz, String path) {
		URL url = clazz.getClassLoader().getResource(path);
		if(url != null && url.getProtocol().equals("file")) {
			try {
				return new File(url.toURI()).list();
			} catch (URISyntaxException e) {
				System.err.println("URISE in FileHandler.getFilesFromDir");
			}
		}
		if(url == null) {
			String temp = clazz.getName().replace(".", "/")+".class";
			url = clazz.getClassLoader().getResource(temp);
		}
		
		if(url.getProtocol().equals("jar")) {
			String jarPath = url.getPath().substring(5, url.getPath().indexOf("!"));
			JarFile jar = null;
			try {
				jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Enumeration<JarEntry> entries = jar.entries();
			Set<String> result = new HashSet<String>();
			while(entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if(name.startsWith(path)) {
					String entry = name.substring(path.length());
					int checkSubdir = entry.indexOf("/");
					if(checkSubdir >= 0) {
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			return result.toArray(new String[result.size()]);
		}
		return new String[] {};
	}
	
	public void deleteContents(String filePath) {
		try {
			new FileWriter(getClass().getClassLoader().getResource(filePath).getFile(), false).close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isEmpty(String filepath) {
		File f = new File(getClass().getClassLoader().getResource(filepath).getFile());
		return f.length() == 0;
	}
	
	public boolean fileExists(String filepath) {
		URL u = getClass().getClassLoader().getResource(filepath);
		if(u != null) {
			File f = new File(u.getFile());
			return f.exists();
		}
		return false;
	}
	
	public String getDocsLoc() {
		return new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
	}
	
	public void writeToDocs(String filename, String[] lines) {
		String filepath = getDocsLoc();
		writeToFile(filepath+filename, lines);
	}
}
