package de.spinanddrain.supportchat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class VersionInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5216056446387109197L;

	private String lastModifiedVersion, fileSystem;
	
	public VersionInfo(String lastModifiedVersion, String fileSystem) {
		this.lastModifiedVersion = lastModifiedVersion;
		this.fileSystem = fileSystem;
	}
	
	public String getFileSystem() {
		return fileSystem;
	}
	
	public String getLastModifiedVersion() {
		return lastModifiedVersion;
	}
	
	public static void store(VersionInfo info, File file) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(info);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static VersionInfo read(File file) {
		if(!file.exists())
			return null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			VersionInfo info = (VersionInfo) ois.readObject();
			ois.close();
			return info;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
