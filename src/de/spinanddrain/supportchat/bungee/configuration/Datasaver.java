package de.spinanddrain.supportchat.bungee.configuration;

import java.io.File;

import de.spinanddrain.supportchat.bungee.ConfigAdapter;
import net.md_5.bungee.config.Configuration;

public class Datasaver {

	/*
	 * Created by SpinAndDrain on 01.04.2020
	 */
	
	private final ConfigAdapter adapter;
	
	public Datasaver() {
		this.adapter = new ConfigAdapter(new File("plugins/SupportChat/mysql.yml")) {
			@Override
			public void copyDefaults(Configuration cfg) {
				addDefault("use", false);
				addDefault("host", "localhost");
				addDefault("port", 3306);
				addDefault("database", "supportchat");
				addDefault("user", "root");
				addDefault("password", "pw123");
				addDefault("useSSL", false);
			}
		};
	}
	
	public ConfigAdapter getAdapter() {
		return adapter;
	}
	
	public boolean use() {
		return adapter.cfg.getBoolean("use");
	}
	
	public String getHost() {
		return adapter.cfg.getString("host");
	}
	
	public int getPort() { 
		return adapter.cfg.getInt("port");
	}
	
	public String getDatabase() {
		return adapter.cfg.getString("database");
	}
	
	public String getUser() {
		return adapter.cfg.getString("user");
	}
	
	public String getPassword() {
		return adapter.cfg.getString("password");
	}
	
	public boolean useSSL() {
		return adapter.cfg.getBoolean("useSSL");
	}
	
}